package com.beyond.board.author.controller;

import com.beyond.board.author.dto.*;
import com.beyond.board.author.service.AuthorService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;

    // 회원가입
    @PostMapping("/create")
    public String save(@Valid AuthorCreateDto authorCreateDto) {
        this.authorService.save(authorCreateDto);
        return null;
    }

    // 로그인


    // 회원 목록 조회
    @GetMapping("/list")
    public String listAuthors() {
        List<AuthorListDto> authorListDto = this.authorService.findAll();
        return "author/author_list";
    }

    // 회원 상세 조회
    @GetMapping("/detail/{inputId}")
    public String findById(@PathVariable Long inputId) {
        AuthorDetailDto authorDetailDto = authorService.findById(inputId);
        return null;
    }

//    // 비밀번호 수정
//    @PatchMapping("/updatepw")
//    public ResponseEntity<String> updatePassword(@RequestBody AuthorUpdatePwDto authorUpdatePwDto) {
//        try {
//            this.authorService.updatePassword(authorUpdatePwDto);
//            return new ResponseEntity<>("ok", HttpStatus.OK);
//        } catch(NoSuchElementException e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
//        }
//    }
//
//    // 회원 탈퇴
//    @DeleteMapping("/delete/{inputId}")
//    public ResponseEntity<String> deleteAuthor(@PathVariable Long inputId) {
//        try {
//            authorService.delete(inputId);
//            return new ResponseEntity<>("ok", HttpStatus.OK);
//        } catch(NoSuchElementException e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
//        }
//    }
}
