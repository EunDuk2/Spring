package com.example.oauth.member.controller;

import com.example.oauth.common.auth.JwtTokenProvider;
import com.example.oauth.member.domain.Member;
import com.example.oauth.member.domain.SocialType;
import com.example.oauth.member.dto.*;
import com.example.oauth.member.service.GoogleService;
import com.example.oauth.member.service.KakaoService;
import com.example.oauth.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GoogleService googleService;
    private final KakaoService kakaoService;

    @PostMapping("/create")
    public ResponseEntity<?> memberCreate(@RequestBody MemberCreateDto memberCreateDto)  {
        Member member = memberService.create(memberCreateDto);
        return new ResponseEntity<>(member.getId(), HttpStatus.CREATED);
    }

    @PostMapping("/doLogin")
    public ResponseEntity<?> doLogin(@RequestBody MemberLoginDto memberLoginDto) {
        // 검증
        Member member = memberService.login(memberLoginDto);

        // 일치한 경우 jwt token 생성
        String jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole().toString());

        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("id", member.getId());
        loginInfo.put("token", jwtToken);

        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }

    @PostMapping("/google/doLogin")
    public ResponseEntity<?> googleLogin(@RequestBody RedirectDto redirectDto) {
        // accessToken 발급
        AccessTokenDto accessTokenDto = googleService.getAccessToken(redirectDto.getCode());
        // 사용자 정보 얻기
        GoogleProfileDto googleProfileDto = googleService.getGoogleProfile(accessTokenDto.getAccess_token());
        // 회원가입이 되어있지 않다면 회원가입
        Member originalMember = memberService.getMemberBySocialId(googleProfileDto.getSub());
        if(originalMember == null) {
            originalMember = memberService.createOauth(googleProfileDto.getSub(), googleProfileDto.getEmail(), SocialType.GOOGLE);
        }

        // 회원가입이 되어 있는 회원이라면 토큰 발급
        String jwtToken = jwtTokenProvider.createToken(originalMember.getEmail(), originalMember.getRole().toString());

        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("id", originalMember.getId());
        loginInfo.put("token", jwtToken);

        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }

    @PostMapping("/kakao/doLogin")
    public ResponseEntity<?> kakaoLogin(@RequestBody RedirectDto redirectDto) {

        AccessTokenDto accessTokenDto = kakaoService.getAccessToken(redirectDto.getCode());

        KakaoProfileDto kakaoProfileDto = kakaoService.getKakaoProfile(accessTokenDto.getAccess_token());

        Member originalMember = memberService.getMemberBySocialId(kakaoProfileDto.getId());
        if(originalMember == null) {
            originalMember = memberService.createOauth(kakaoProfileDto.getId(), kakaoProfileDto.getKakao_account().getEmail(), SocialType.KAKAO);
        }


        String jwtToken = jwtTokenProvider.createToken(originalMember.getEmail(), originalMember.getRole().toString());

        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("id", originalMember.getId());
        loginInfo.put("token", jwtToken);

        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }
}

