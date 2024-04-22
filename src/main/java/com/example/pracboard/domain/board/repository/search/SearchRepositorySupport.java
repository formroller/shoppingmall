package com.example.pracboard.domain.board.repository.search;
//
//import com.example.pracboard.domain.board.dto.BoardListReplyCountDTO;
//import com.example.pracboard.domain.board.entity.Board;
//import com.example.pracboard.global.page.PageRequestDTO;
//import com.querydsl.core.BooleanBuilder;
//import com.querydsl.core.types.Projections;
//import com.querydsl.core.types.dsl.BooleanExpression;
//import com.querydsl.jpa.JPQLQuery;
//import com.querydsl.jpa.JPQLQueryFactory;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
//
//
//import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

//@Repository
//public class SearchRepositorySupport {
//
//    private final JPAQueryFactory jpaQueryFactory;
//
//    public SearchRepositorySupport(Class<?> domainClass) {
//        super(domainClass);
//    }

//}
    /*------------*/
//
//    private JPQLQueryFactory queryFactory;
//
//    public SearchKeywordRepositoryImpl(JPQLQueryFactory queryFactory) {
//        super(com.example.pracboard.domain.board.entity.Board.class);
//        this.queryFactory=queryFactory;
//    }
//
//    @Override
//    public Page<Board> findAllKeywordWithConditions(PageRequestDTO requestDTO, Pageable pageable) {
//
//        JPQLQuery<Board> query = queryFactory
//                .select(Projections.bean(Board.class,
//                        board.bno,
//                        board.title,
//                        board.content,
//                        board.regDate,
//                        reply.count().as("replyCnt")
//                )).from(board)
//                .leftJoin(reply).on(board.eq(board))
//                .where(keywordSearchPredicate(requestDTO));
//
//        // count query 분리
//        long count = queryFactory.selectFrom(board).where(keywordSearchPredicate(requestDTO)).fetchCount();
//
//        List<Board> result = getQuerydsl().applyPagination(pageable, query).fetch();
//
//        return new PageImpl<>(result, pageable, count);
//    }
//
//    // where 절에 적용할 Predicate
//    private BooleanBuilder keywordSearchPredicate(PageRequestDTO requestDTO){
//        return new BooleanBuilder()
//                .and(getTitle(requestDTO.getKeyword()))
//                .and(getContent(requestDTO.getKeyword()));
//    }
//
//    private BooleanExpression getTitle(String keyword){
//        // todo - boardDTO.getTitle null 아니면 keyword 포함된 boardDTO 가져오기
//        return keyword != null ? board.title.contains(keyword) : null;
//    }
//
//    private BooleanExpression getContent(String keyword){
//        // todo - boardDTO.getContent null 아니면 keyword 포함된 boardDTO 가져오기
//        return keyword != null ? board.content.contains(keyword) : null;
//    }
//}
