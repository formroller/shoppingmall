package com.example.pracboard.domain.board.controller;

import com.example.pracboard.domain.board.dto.BoardDTO;
import com.example.pracboard.domain.board.service.BoardService;
import com.example.pracboard.global.page.PageRequestDTO;
import com.example.pracboard.global.page.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
@Log4j2
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/register")
    public void register(){
        log.info(" -------------- product register ----------------- ");
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                           BoardDTO dto, Model model){
        log.info("Board DTO : "+dto);

        Long bno = boardService.register(dto);

        model.addAttribute("requestDTO",requestDTO);

        return "redirect:/board/list";
    }

    @GetMapping("/list")
    public void list(PageRequestDTO requestDTO, Model model){

        log.info(requestDTO);

        model.addAttribute("result", boardService.getList(requestDTO));

    }

    @GetMapping({"/read", "/modify"})
    public void read(Long bno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model){

        log.info("requestDTO : "+bno);

        BoardDTO dto = boardService.get(bno);

        model.addAttribute("dto", dto);

    }

    @PostMapping("/modify")
    public String modify(BoardDTO boardDTO,
                       @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                       RedirectAttributes redirectAttributes){
        log.info("Modify Board Num : "+boardDTO);

        boardService.modify(boardDTO);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("bno", boardDTO.getBno());

        return "redirect:/board/list";
    }
}


//    @GetMapping("/index")
//    public String getBasic(){return "index";}
//}
