package com.example.malls.domain.controller;

import com.example.malls.domain.dto.PageRequestDTO;
import com.example.malls.domain.dto.PageResponseDTO;
import com.example.malls.domain.dto.ReplyDTO;
import com.example.malls.domain.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/replies")
@RequiredArgsConstructor
@Log4j2
public class ReplyController {

    private final ReplyService replyService;



    @PostMapping(value = "/")
    public ResponseEntity<Map<String, Long>> register(
            @RequestBody ReplyDTO replyDTO,
            BindingResult bindingResult) throws BindException {


        log.info(replyDTO);

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        Map<String, Long> resultMap = new HashMap<>();

        Long rno = replyService.register(replyDTO);

        resultMap.put("rno", rno);

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @GetMapping("/list/{bno}") // 게시물의 댓글 목록
    public ResponseEntity<PageResponseDTO<ReplyDTO>> getList(@PathVariable("bno") Long bno, PageRequestDTO requestDTO){
        PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfBoard(bno, requestDTO);

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/{rno}")
    public ResponseEntity<ReplyDTO> getReplyDTO(@PathVariable("rno") Long rno){
        ReplyDTO replyDTO = replyService.read(rno);

        return new ResponseEntity<>(replyDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{rno}")
    public ResponseEntity<Map<String, Long>> remove(@PathVariable("rno") Long rno){
        replyService.remove(rno);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("rno", rno);

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @PutMapping("/{rno}")
    public ResponseEntity<Map<String,Long>> modify(@PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO){

        replyDTO.setRno(rno); // 번호 일치

        replyService.modify(replyDTO);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("rno", rno);

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }
}
