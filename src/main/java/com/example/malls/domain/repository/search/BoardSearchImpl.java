package com.example.malls.domain.repository.search;

import com.example.malls.domain.dto.BoardImageDTO;
import com.example.malls.domain.dto.BoardListAllDTO;
import com.example.malls.domain.dto.BoardListReplyCountDTO;
import com.example.malls.domain.entity.Board;
import com.example.malls.domain.entity.QBoard;
import com.example.malls.domain.entity.QReply;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> searchAll(String keyword, Pageable pageable) {

        QBoard board = QBoard.board;

        JPQLQuery<Board> query = from(board);

        query.where(board.bno.gt(0));

        // condition
        BooleanBuilder condition = new BooleanBuilder();

        try{
            condition.or(board.content.contains(keyword))
                    .or(board.title.contains(keyword));
        }catch (NullPointerException e){

        }
        query.where(condition);

        // paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();

        long count = query.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }


    @Override
//    public Page<BoardListReplyCountDTO> searchWithAll(String keyword, Pageable pageable) {
//
//        QBoard board = QBoard.board;
//        QReply reply = QReply.reply;
//
//        JPQLQuery<Board> boardJPQLQuery = from(board);
//        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board)); // left join
//
//        getQuerydsl().applyPagination(pageable, boardJPQLQuery);
//
//        List<Board> dtoList = boardJPQLQuery.fetch();
//
//        dtoList.forEach(brd -> {
//            System.out.println(brd.getBno());
//            System.out.println(brd.getImageSet());
//            System.out.println("-".repeat(30));
//        });
//        return null;
//    }

    public Page<BoardListAllDTO> searchWithAll(String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> boardJPQLQuery = from(board);
        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board)); // left join

        // 조건 처리(keyword)
        boardJPQLQuery.where(board.bno.gt(0L));

        BooleanBuilder condition = new BooleanBuilder();
        try{
            condition.or(board.title.contains(keyword))
                    .or(board.content.contains(keyword));
        }catch (NullPointerException e){
//        }catch (NoSuchElementException e){
//            log.error("No Such Element Exception : ",e);
        }

        boardJPQLQuery.where(condition);


        // Paging
        getQuerydsl().applyPagination(pageable, boardJPQLQuery);

        /* Reply Count
         * Tuple : Board 객체 안 Set과 같은 중첩 구조 처리시 튜플 사용해 DTO로 변환 것이 편리
         */
        JPQLQuery<Tuple> tupleJPQLQuery = boardJPQLQuery.select(board, reply.countDistinct()).groupBy(board);

        tupleJPQLQuery.where(board.bno.gt(0L));

        List<Tuple> tupleList = tupleJPQLQuery.fetch();

        // Tuple -> DTO 변환
        List<BoardListAllDTO> dtoList = tupleList.stream().map(tuple -> {
            Board board1 = (Board) tuple.get(board);
            long replyCnt = tuple.get(1, Long.class);

            BoardListAllDTO dto = BoardListAllDTO.builder()
                    .bno(board1.getBno())
                    .title(board1.getTitle())
                    .price(board1.getPrice())
                    .writer(board1.getWriter())
                    .regDate(board1.getRegDate())
                    .replyCnt(replyCnt)
                    .build();

            // BoardImage -> BoardImageDTO 처리 부분
            List<BoardImageDTO> imageDTOS = board1.getImageSet().stream().sorted()
                    .map(boardImage -> BoardImageDTO.builder()
                            .uuid(boardImage.getUuid())
                            .fileName(boardImage.getFileName())
                            .ord(boardImage.getOrd())
                            .build()
                    ).collect(Collectors.toList());

            dto.setBoardImages(imageDTOS); // 처리된 BoardImages 추가

            return dto;
        }).collect(Collectors.toList());

        long totalCount = boardJPQLQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, totalCount);

    }

    /*Board + Reply Count*/
    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> query = from(board);
        query.leftJoin(reply).on(reply.board.eq(board));

        query.groupBy(board);

        // condition
        BooleanBuilder condition = new BooleanBuilder();

        try{
            condition.or(board.content.contains(keyword))
                    .or(board.title.contains(keyword));
        } catch (NullPointerException e){

        }

        query.where(condition);
        query.where(board.bno.gt(0L));

        // Projections.bean - 쿼리 결과를 한 번에 DTO 처리
        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(
                Projections.bean(BoardListReplyCountDTO.class,
                        board.bno,
                        board.price,
                        board.title,
                        board.writer,
                        board.regDate,
                        reply.count().as("replyCnt")
                        ));

        // Paging
        this.getQuerydsl().applyPagination(pageable, dtoQuery);

        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();

        long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }
}
