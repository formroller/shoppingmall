package com.example.malls.domain.controller;

import com.example.malls.domain.dto.MemberJoinDTO;
import com.example.malls.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@Log4j2
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public void login(String error, String logout){
        log.info(" -------------- login get -----------");
        log.info("logout : "+logout);

        if(logout != null){
            log.info(" --- user logout ---");
        }
    }

    /* 회원가입 처리 */
    @GetMapping("/join")
    public void join(){
        log.info(" ----- join get ----");
    }

    @PostMapping("/join")
    public String join(MemberJoinDTO memberJoinDTO, RedirectAttributes redirectAttributes){
        log.info("---------------  join Post --------------- ");
        log.info(memberJoinDTO);

        try{
            memberService.join(memberJoinDTO);
        }catch (MemberService.MidExistException e){

            redirectAttributes.addFlashAttribute("error", "mid");
            return "redirect:/member/join";

        }

        redirectAttributes.addFlashAttribute("result", "success");

        return "redirect:/member/login"; // 회원 가입 후 로그인
    }
}
