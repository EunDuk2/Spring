package com.beyond.ordersystem.member.controller;

import com.beyond.ordersystem.common.auth.JwtTokenProvider;
import com.beyond.ordersystem.common.dto.CommonSuccessDto;
import com.beyond.ordersystem.member.domain.Member;
import com.beyond.ordersystem.member.dto.LoginReqDto;
import com.beyond.ordersystem.member.dto.LoginResDto;
import com.beyond.ordersystem.member.dto.MemberCreateDto;
import com.beyond.ordersystem.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/create")
    public ResponseEntity<?> memberCreate(@RequestBody @Valid MemberCreateDto dto) {
        Long id = memberService.save(dto);
        return new ResponseEntity<>(new CommonSuccessDto(id, HttpStatus.CREATED.value(), "회원가입 성공"), HttpStatus.CREATED);
    }

    @PostMapping("/doLogin")
    public ResponseEntity<?> memberDoLogin(@RequestBody @Valid LoginReqDto dto) {
        Member member = memberService.doLogin(dto);
        String accessToken = jwtTokenProvider.createAtToken(member);
        return new ResponseEntity<>(new CommonSuccessDto(new LoginResDto(accessToken, "refreshToken"), HttpStatus.OK.value(), "로그인 성공"), HttpStatus.OK);
    }

}
