package com.example.malls.domain.controller;

import com.example.malls.domain.dto.*;
import com.example.malls.domain.entity.Board;
import com.example.malls.domain.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
@Log4j2
public class BoardController {
    @Value("${com.example.malls.path}")
    private String uploadPath;

    private final BoardService boardService;

    @GetMapping("/list")
    public void list(PageRequestDTO requestDTO, Model model){
//        PageResponseDTO<BoardDTO> responseDTO = boardService.getList(requestDTO);
//        PageResponseDTO<BoardListReplyCountDTO> responseDTO = boardService.listWithReplyCount(requestDTO);

        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(requestDTO);

//        log.info(responseDTO.getDtoList().stream().map(boardDTO ->  boardDTO.getBno()).collect(Collectors.toList()));
////        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping({"/read", "/modify"})
    public void read(Long bno, @ModelAttribute("requestDTO")PageRequestDTO requestDTO, Model model){
        BoardDTO dto = boardService.get(bno);

        log.info(dto.getTitle(), dto.getBno());

        model.addAttribute("dto", dto);
    }

    @GetMapping("/register")
    public void register(){}

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/register")
    public String register(@Valid BoardDTO dto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes){

        log.info("Board Register");

        if(bindingResult.hasErrors()){
            log.info(" -- Register Error --");

            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            return "redirect:/board/register";
        }
        log.info(dto);

        Long bno = boardService.register(dto);

        redirectAttributes.addFlashAttribute("result", bno);

        return "redirect:/board/list";
    }

    @PreAuthorize("principal.username == #boardDTO.writer") // principal.user -> 현재 로그인된 사용자 & #boardDTO -> 현재 파라미터 수집된 boardDTO 의미
    @PostMapping("/modify")
    public String modify(PageRequestDTO requestDTO,
                         @Valid BoardDTO boardDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes
                         ){
        log.info("(Post)Board Modify  --- "+boardDTO);


        if(bindingResult.hasErrors()){
            log.info("== Has Error ==");

            String link = requestDTO.getLink();

            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            redirectAttributes.addAttribute("bno", boardDTO.getBno());

            return "redirect:/board/modify?"+link;
        }

        boardService.modify(boardDTO);

        redirectAttributes.addFlashAttribute("result", "modified");

        redirectAttributes.addAttribute("bno", boardDTO.getBno());

        return "redirect:/board/read";
    }


    @PreAuthorize("principal.username == #boardDTO.writer")
    @PostMapping("/remove")
    public String remove(BoardDTO boardDTO, RedirectAttributes redirectAttributes){

        Long bno = boardDTO.getBno();
        log.info("(Post) remove post -- "+bno);

        boardService.remove(bno);

        // 게시물이 데이터베이스상에 삭제되었다면 첨부파일 삭제
        log.info(boardDTO.getFileNames());
        List<String> fileNames = boardDTO.getFileNames();
        if(fileNames != null && fileNames.size() > 0){
            removeFiles(fileNames);
        }

        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/board/list";
    }

    public void removeFiles(List<String> files){
        for(String fileName:files){
            Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

            String resourceName = resource.getFilename();

            try{
                String contentType = Files.probeContentType(resource.getFile().toPath());

                resource.getFile().delete();

                // 섬네일 있을 경우
                if(contentType.startsWith("image")){
                    File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);
                    thumbnailFile.delete();
                }

            }catch (Exception e){
                log.error(e.getMessage());
            } // end for
        }
    }

}
