package com.web.demo.controller;

import java.time.Duration;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.web.demo.dto.MemberForm;
import com.web.demo.dto.MemberUserDto;
import com.web.demo.service.CommonService;
import com.web.demo.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;


@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {
	private final MemberService memberService;
	private final CommonService commonService;

	
	// 회원가입 파트
	@GetMapping("/signup")
	public String signup(MemberForm memberForm) {
		return "signup_form";
	}

	@PostMapping("/signup")
	public String signup(@Valid MemberForm memberForm, BindingResult bindingResult) {
		System.out.println("회원가입 요청 확인");
		if (bindingResult.hasErrors()) {
			return "signup_form";
		}
		if (!memberForm.getPassword().equals(memberForm.getPassword1())) {
			bindingResult.rejectValue("password1", "passwordInCorrect", "패스워드가 일치하지 않습니다.");
			return "signup_form";
		}
		try {
			this.memberService.create(memberForm);
		} catch (DataIntegrityViolationException e) {
			bindingResult.reject("signupError", "이미 사용 중인 이메일 또는 닉네임입니다.");
			return "signup_form";
		} catch (Exception e) {
			bindingResult.reject("signupError", e.getMessage());
			return "signup_form";
		}
		return "redirect:/member/login";
	}

	//로그인
	@GetMapping("/login")
    public String login(HttpServletRequest request) {
		/* 쿠키 부분 아직 미도입
		 Cookie[] list = request.getCookies();
		//System.out.println("쿠키 획득" + list.length);
		for(Cookie cookie:list) {
			if(cookie.getName().equals("access_token")) {
				//System.out.println("access_token=" +  cookie.getValue() );			
			}
		}	
		 */
        return "login_form";
    }
	
	// 유저 페이지(비밀번호, 닉네임 변경 가능)
	@GetMapping("/detail")
	public String detail(@AuthenticationPrincipal MemberUserDto userDetails, Model model
			,MemberForm memberForm) {
		this.commonService.commonBoardList(model, userDetails);
		model.addAttribute("email",userDetails.getEmail());
		return "member_detail";
	}
	// 비밀번호 변경
	@PostMapping("/modify/password")
	public String modifyPassword(@RequestParam("password") String password
			,@RequestParam("newPassword") String newPassword
			,@AuthenticationPrincipal MemberUserDto userDetails
			,RedirectAttributes redirectAttributes) {
	    if (this.memberService.checkPassword(userDetails.getEmail(), password)) {
	        this.memberService.modifyPassword(userDetails.getEmail(), newPassword);
	        redirectAttributes.addFlashAttribute("passwordModifyMessage", "비밀번호가 변경됐습니다");
	    } else {
	    	redirectAttributes.addFlashAttribute("passwordModifyMessage", "기존 비밀번호가 일치하지 않습니다");
	    }
		return "redirect:/member/detail";
	}
	// 닉네임 변경
	@PostMapping("/modify/nickname")
	public String modifyNickname(@RequestParam("nickname") String nickname
			,@AuthenticationPrincipal MemberUserDto userDetails
			,RedirectAttributes redirectAttributes) {
        if (this.memberService.modifyNickname(userDetails.getEmail(),nickname)) {
        	redirectAttributes.addFlashAttribute("nicknameModifyMessage", "닉네임 변경이 완료됐습니다.");
        } else {
        	redirectAttributes.addFlashAttribute("nicknameModifyMessage", "닉네임 변경이 실패했습니다. 중복 확인을 해주세요.");
        }
		return "redirect:/member/detail";
	}
	// 이메일 중복 확인
	@PostMapping("/duplication/email")
	@ResponseBody
	public boolean checkDuplicationEmail(@RequestParam("email") String email) {
		return this.memberService.checkEmailDuplication(email);
	}
	// 닉네임 중복 확인
	@PostMapping("/duplication/nickname")
	@ResponseBody
	public boolean checkDuplicationNickname(@RequestParam("nickname") String nickname) {
		return this.memberService.checkNicknameDuplication(nickname);
		}
	// 사용자 관리 페이지
	@GetMapping("/control")
	public String control(Model model 
			,@AuthenticationPrincipal MemberUserDto userDetails) {
		this.memberService.memberFormList(model);
		this.commonService.commonBoardList(model, userDetails);
		return "member_control";
	}
	// 사용자 권한 수정
	@PostMapping("/setrole")
	public String postMethodName(Model model 
			,@RequestParam("memberId") Long memberId
			,@RequestParam("roleNum") int roleNum) {
		this.memberService.setRole(memberId, roleNum);
		return "redirect:/member/control";
	}
	
	
}
