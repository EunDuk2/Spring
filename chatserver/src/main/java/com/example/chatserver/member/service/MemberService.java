package com.example.chatserver.member.service;

import com.example.chatserver.member.domain.Member;
import com.example.chatserver.member.dto.MemberSaveReqDto;
import com.example.chatserver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member create(MemberSaveReqDto dto) {
        // 이미 가입되어 있는 이메일 검증
        if(memberRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        Member newMember = Member.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
        Member member = memberRepository.save(newMember);

        return member;
    }
}
