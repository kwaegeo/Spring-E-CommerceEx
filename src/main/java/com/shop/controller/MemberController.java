package com.shop.controller;


import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.BindingResult;
import javax.validation.Valid;

@RequestMapping("/members")          //members라는 url요청이 들어오면 여기로 들어와서
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")  //new가 있으면 이 컨트롤러가 실행되는데
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto()); //model 객체에 빈 memberDto를 넘기고
        return "member/memberForm";  //이 model 객체랑 같이 member/memberForm이라는 회원 가입 페이지로 넘깁니다.
    }

    @PostMapping(value = "/new")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
                      //memberForm 문서에서 요청이 들어오면 정보를 가진 Dto객체를 가지고 검증을하여 bindingResult에일단 저장된다.
        if(bindingResult.hasErrors()){  //저장된 binding객체에 에러가 있는지 확인하고 있으면 다시 되돌려 보내고
            return "member/memberForm";
        }
        // 그것이 아니라면 멤버 객체를 만들어낸다 패스워드 인코드와 함께 그리고 데이터베이스에 저장한다.
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e){ //email 중복이 있을경우엔 예외를 발생시킨 후 메세지를 띄우며 다시 돌아가도록 설정한다.
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        //회원 가입이 무사히 성공하면 메인페이지로 넘어가게끔한다.
        return "redirect:/";
    }

    @GetMapping(value ="/login")
    public String loginMember(){
        return "/member/memberLoginForm";
    }

    @GetMapping(value ="/login/error")  //login 실패 시 에러메세지를 전송하고 돌려보냄
        public String loginError(Model model){
            model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
            return "/member/memberLoginForm";
        }


}
