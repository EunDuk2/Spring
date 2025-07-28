package com.beyond.ordersystem.member.service;

import com.beyond.ordersystem.member.dto.LoginReqDto;
import com.beyond.ordersystem.member.domain.Member;
import com.beyond.ordersystem.member.dto.MemberCreateDto;
import com.beyond.ordersystem.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long save(MemberCreateDto dto) {
        if(memberRepository.findByEmail(dto.getEmail()).isPresent()) throw new IllegalArgumentException("중복되는 이메일입니다.");

        // member를 리턴하여, 컨트롤러에서 프론트에게 방금 저장한 id를 반환할 수 있게 함
        // 여기서는 id를 반환
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        Member member = memberRepository.save(dto.toEntity(encodedPassword));
        return member.getId();
    }

    public Member doLogin(LoginReqDto dto) {
        Member member = memberRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new EntityNotFoundException("없는 회원입니다."));
        if(!passwordEncoder.matches(dto.getPassword(), member.getPassword())) throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        return member;
    }

}
