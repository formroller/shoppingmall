//package com.example.pracboard.domain.board.repository.search;
//
//import com.example.pracboard.domain.board.dto.BoardDTO;
//import com.example.pracboard.domain.board.entity.Board;
//import com.example.pracboard.global.page.PageRequestDTO;
//import jakarta.persistence.criteria.CriteriaBuilder;
//import jakarta.persistence.criteria.CriteriaQuery;
//import jakarta.persistence.criteria.Predicate;
//import jakarta.persistence.criteria.Root;
//import org.springframework.data.jpa.domain.Specification;
//
//public class BoardSpecification {
//    public static Specification<Board> likeTitle(String title){
//        return new Specification<Board>() {
//            @Override
//            public Predicate toPredicate(Root<Board> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//
//
////                return criteriaBuilder.like(root.get(containKeyword(requestDTO)))
//                return criteriaBuilder.like(boardDTO.getTitle(key))
//            }
//        }
//    }
//}
