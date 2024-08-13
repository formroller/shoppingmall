package com.example.malls.domain.service;

import com.example.malls.domain.dto.ReplyDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class ReplyServiceImplTest {

    @Autowired
    private ReplyService replyService;

    @DisplayName("Insert Test")
    @Test
    public void testInsert(){
        ReplyDTO replyDTO = ReplyDTO.builder()
                .replyText("123-ReplyDTO Text")
                .replyer("replyer112")
                .bno(302L)
                .build();

        log.info(replyService.register(replyDTO));
    }

}