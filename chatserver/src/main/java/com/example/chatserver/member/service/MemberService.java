package com.example.chatserver.member.service;

import com.example.chatserver.member.domain.Member;
import com.example.chatserver.member.dto.MemberLoginReqDto;
import com.example.chatserver.member.dto.MemberSaveReqDto;
import com.example.chatserver.member.dto.MemberlistResDto;
import com.example.chatserver.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member create(MemberSaveReqDto dto) {
        // 이미 가입되어 있는 이메일 검증
        if(memberRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        Member newMember = Member.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();
        Member member = memberRepository.save(newMember);

        return member;
    }

    public Member login(MemberLoginReqDto dto) {
        Member member =  memberRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
        if(!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀립니다.");
        }
        return member;
    }

    public List<MemberlistResDto> findAll() {
        List<Member> members = memberRepository.findAll();
        List<MemberlistResDto> memberlistResDtos = new ArrayList<>();
        for (Member member : members) {
            MemberlistResDto memberlistResDto = new MemberlistResDto();
            memberlistResDto.setId(member.getId());
            memberlistResDto.setName(member.getName());
            memberlistResDto.setEmail(member.getEmail());
            memberlistResDtos.add(memberlistResDto);
        }
        return memberlistResDtos;
    }
}
