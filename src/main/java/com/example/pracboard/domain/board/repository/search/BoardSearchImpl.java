package com.example.pracboard.domain.board.repository.search;

import com.example.pracboard.domain.board.dto.BoardListReplyCountDTO;
import com.example.pracboard.domain.board.entity.Board;
import com.example.pracboard.domain.board.entity.QBoard;
import com.example.pracboard.domain.reply.entity.QReply;
import com.example.pracboard.global.page.PageRequestDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> search(Pageable pageable) {

        QBoard board = QBoard.board;

        JPQLQuery<Board> query = from(board);

        query.where(board.title.contains("1"));

        // paging
        this.getQuerydsl().applyPagination(pageable, query);


        List<Board> list = query.fetch();

        long count = query.fetchCount();

        return null;
    }

    @Override
    public Page<BoardListReplyCountDTO> searchKeyword(String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> query = from(board);

        query.leftJoin(reply).on(reply.board.eq(board));

        query.groupBy(board);

        if (keyword != null && keyword.length() > 0) {
            BooleanBuilder builder = new BooleanBuilder();
//            ArrayList subject = new ArrayList(Arrays.asList("title", "content", "writer"));
//            List<String> subject = new ArrayList(Arrays.asList("title", "content", "writer"));
            String[] subject = new String[]{"title", "content", "writer"};
//
//            for(int i=0; subject.length(); i++){
//                builder.or(board.getClass(subject[i]).contains(keyword));
//            }

            if (subject.equals("title")) {
                builder.or(board.title.contains(keyword));
            } else if (subject.equals("content")) {
                builder.or(board.content.contains(keyword));
//            }else if(subject.equals("writer")){
//                builder.or(board.writer.contains(keyword));
//            }
            }
            query.where(builder);
        }

        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(
                Projections.bean(BoardListReplyCountDTO.class,
                        board.bno,
                        board.title,
                        board.content,
                        reply.count().as("replyCnt"))
        );

        this.getQuerydsl().applyPagination(pageable, dtoQuery);

        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();

        long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }

    @Override
    public Page<Object[]> getSearchList(PageRequestDTO requestDTO, Pageable pageable) {
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        String keyword = requestDTO.getKeyword();

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(reply).on(board.eq(board));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, reply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(!keyword.isEmpty() || keyword.length()!=0){
            BooleanExpression expression = board.title.contains(keyword);

            expression.and(board.content.contains(keyword));

            booleanBuilder.and(expression);
        }

        // order by
        Sort sort = pageable.getSort();

        sort.stream().forEach(order ->{
            Order direction = order.isAscending() ?Order.ASC : Order.DESC;
            String prop = order.getProperty();

            PathBuilder orderExpression = new PathBuilder(Board.class, "board");
            tuple.orderBy(new OrderSpecifier<>(direction, orderExpression.get(prop))); // jpqlQuery -> tuple
        });

        jpqlQuery.groupBy(board);

        // Paging
        jpqlQuery.offset(pageable.getOffset());
        jpqlQuery.limit(pageable.getPageSize());


//        List<Board> result = jpqlQuery.fetch();
        List<Tuple> result = tuple.fetch();

        Long count = tuple.fetchCount();;


        return new PageImpl<Object[]>(
                result.stream().map(t->t.toArray()).collect(Collectors.toList()),
                pageable,
                count);
    }

    @Override
    public Page<Object[]> getSearchKeyword(String keyword, Pageable pageable) {
        return null;
    }
}
