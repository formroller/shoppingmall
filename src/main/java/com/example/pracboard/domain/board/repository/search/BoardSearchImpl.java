package com.example.pracboard.domain.board.repository.search;

import com.example.pracboard.domain.board.entity.Board;
import com.example.pracboard.domain.board.entity.QBoard;
import com.example.pracboard.domain.reply.entity.QReply;
import com.example.pracboard.global.page.PageRequestDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Log4j2
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {


    public BoardSearchImpl() {
        super(Board.class);
    }

//    public BoardSearchImpl(JPAQueryFactory queryFactory) {
//        super(Board.class);s
//        this.queryFactory=queryFactory;
//    }

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
    public Page<Object[]> getSearchList(PageRequestDTO requestDTO, Pageable pageable) {
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        String keyword = requestDTO.getKeyword();

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(reply).on(board.eq(board));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, reply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(!keyword.isEmpty() || keyword.length()!=0){
            BooleanExpression expression = board.title.containsIgnoreCase(keyword);

            expression.or(board.content.containsIgnoreCase(keyword));

            booleanBuilder.and(expression);
        }


        // order by
        Sort sort = pageable.getSort();

        sort.stream().forEach(order ->{
            Order direction = order.isAscending() ?Order.ASC : Order.DESC;
            String prop = order.getProperty();

            PathBuilder orderExpression = new PathBuilder(Board.class, "templates/board");
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
    public Page<Object[]> getKeywords(String keyword, Pageable pageable) {


        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, reply.count());

        BooleanBuilder builder = new BooleanBuilder();

        BooleanBuilder conditionBuilder = new BooleanBuilder();
        BooleanExpression condition = board.bno.gt(0);


        try{
            conditionBuilder.or(board.content.contains(keyword))
                    .or(board.title.contains(keyword));

        } catch (NullPointerException e) {
        }

        builder.and(conditionBuilder);

        builder.and(condition);

        tuple.where(builder);

        // Sort
        Sort sort = pageable.getSort();

        sort.stream().forEach(order -> {
            Order direction = order.isDescending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();

            PathBuilder orderExpression = new PathBuilder(Board.class, "board");
            tuple.orderBy(new OrderSpecifier<>(direction, orderExpression.get(prop)));
        });

        tuple.groupBy(board);

        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();
        Long count = tuple.fetchCount();

        return new PageImpl<Object[]>(
                result.stream().map(t->t.toArray()).collect(Collectors.toList()),
//                result.stream().map(Object::toString).collect(Collectors.toList()),
                pageable,
                count
        );
//        return new PageImpl<Object[]>(
//                result.stream().map(t -> t.toArray()).collect(Collectors.toList()),
//                pageable,
//                count
//        );


        // QueryResults - 쿼리 결과 가져오기
//        QueryResults<Tuple> result =  queryFactory.select(
//                board.bno,
//                board.title,
//                board.content,
//                board.price,
//                board.regDate,
//                reply.count().as("replyCnt"))
//                .where(builder)
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetchResults();

//        List<Tuple> content = result.getResults();
//        long total = result.getTotal();

//        return new PageImpl<Object[]>(
//                content.stream().map(t->t.toArray()).collect(Collectors.toList()),
//                pageable,
//                total);
    }
}


    /*BooleanExpress*/
//    private BooleanExpression titleEQ(BoardDTO boardDTO){
//        return boardDTO.getTitle() != null : boardDTO.getTitle().eq()
//    }
//}
