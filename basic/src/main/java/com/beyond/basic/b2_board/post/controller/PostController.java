package com.beyond.basic.b2_board.post.controller;

import com.beyond.basic.b2_board.author.dto.CommonDto;
import com.beyond.basic.b2_board.post.dto.PostCreateDto;
import com.beyond.basic.b2_board.post.dto.PostDetailDto;
import com.beyond.basic.b2_board.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody PostCreateDto dto) {
        postService.save(dto);
        return new ResponseEntity<>(new CommonDto("ok", HttpStatus.CREATED.value(), "게시물 작성 성공"), HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<?> postList() {
        return new ResponseEntity<>(new CommonDto(postService.findAll(), HttpStatus.OK.value(), "게시물 목록 조회 성공"), HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        PostDetailDto dto = postService.findById(id);

        return new ResponseEntity<>(new CommonDto(dto, HttpStatus.OK.value(), "게시물 상세 조회 성공"), HttpStatus.OK);
    }

}
