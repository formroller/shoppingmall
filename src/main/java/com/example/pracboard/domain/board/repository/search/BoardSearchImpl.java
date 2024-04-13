package com.example.pracboard.domain.board.repository.search;

import com.example.pracboard.domain.board.dto.BoardListReplyCountDTO;
import com.example.pracboard.domain.board.entity.Board;
import com.example.pracboard.domain.board.entity.QBoard;
import com.example.pracboard.domain.reply.entity.QReply;
import com.example.pracboard.global.page.PageRequestDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public BooleanBuilder getSearch(PageRequestDTO requestDTO) {

        QBoard board = QBoard.board;

        BooleanBuilder builder = new BooleanBuilder();

        String keyword = requestDTO.getKeyword();

        if(keyword == null || keyword.length()==0){
            return builder;
        }

        // 검색 조건
        BooleanBuilder condition = new BooleanBuilder();

        if(board.title.equals(keyword)){
            condition.or(board.title.contains(keyword));
        }
        if(board.content.equals(keyword)){
            condition.or(board.content.contains(keyword));
        }

        builder.and(condition);
        return builder;
    }
}
