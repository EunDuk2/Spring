package com.beyond.basic.b2_board.author.controller;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.service.AuthorService;
import com.beyond.basic.b2_board.author.dto.*;
import com.beyond.basic.b2_board.common.CommonDto;
import com.beyond.basic.b2_board.common.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@RestController // @Controller + @ResponseBody (화면 리턴 X)
@RequiredArgsConstructor
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    @PostMapping("/create")

    /* 아래 코드 포스트맨 테스트 데이터 예시
        1. form-data 선택
        2. authorCreateDto를 Text로 json 값 세팅 / content-type을 application/json 설정
        3. profileImage는 file로 세팅하면서 content-typedmf multipart/form-dat
    */

    // dto에 있는 validation어노테이션과 controller @Valid 한쌍
    public ResponseEntity<String> save(@RequestPart(name = "authorCreateDto") @Valid AuthorCreateDto authorCreateDto,
                                       @RequestPart(name = "profileImage") MultipartFile profileImage
    ) {
//        try {
//            this.authorService.save(authorCreateDto);
//            return new ResponseEntity<>("ok", HttpStatus.CREATED);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//            // 생성자 매개변수 body부분의 객체와 header부에 상태코드
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
        // controllerAdvice가 없었으면 위와 같이 개별적인 예외처리가 필요하나, 이제는 전역적인 예외처리가 가능.
        this.authorService.save(authorCreateDto, profileImage);
        return new ResponseEntity<>("ok", HttpStatus.CREATED);
    }

    // 로그인 (/author/doLogin)
    @PostMapping("/doLogin")
    public ResponseEntity<?> doLogin(@Valid @RequestBody AuthorLoginDto dto) {
        Author author = authorService.doLogin(dto);

        // 토큰 생성 및 return
        String token = jwtTokenProvider.createAtToken(author);

        return new ResponseEntity<>(new CommonDto(token, HttpStatus.OK.value(), "로그인 성공"), HttpStatus.OK);
    }

    // 회원 목록 조회 (/list)
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')") // 2명 권한
    public List<AuthorListDto> listAuthors() {
        List<AuthorListDto> authorListDto = this.authorService.findAll();
        System.out.println(authorListDto);
        return authorListDto;
    }

    // 회원 상세 조회 : id로 조회 (/detail/1)
    // 서버에서 별도의 try catch를 하지 않으면, 에러 발생 시 500에러 + 스프링의 포맷으로 에러 리턴.
    @GetMapping("/detail/{inputId}")
    // ADMIN 권한이 있는 지를 autentication 객체에서 쉽게 확인
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> findById(@PathVariable Long inputId) { // AuthorDetailDto
//        AuthorDetailDto authorDetailDto = null;
//        try {
//            authorDetailDto = this.authorService.findById(inputId);
////            return new ResponseEntity<>(authorDetailDto, HttpStatus.OK); // authorDetailDto
//            return new ResponseEntity<>(new CommonDto(authorDetailDto,
//                    HttpStatus.CREATED.value(), "author created successfully!"), HttpStatus.OK);
//        } catch(NoSuchElementException e) {
//            e.printStackTrace();
////            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//            return new ResponseEntity<>(new CommonErrorDto(HttpStatus.NO_CONTENT.value(),
//                    e.getMessage()), HttpStatus.NOT_FOUND);
//        }
        AuthorDetailDto authorDetailDto = null;
        authorDetailDto = this.authorService.findById(inputId);
        return new ResponseEntity<>(new CommonDto(authorDetailDto,
                HttpStatus.CREATED.value(), "author select successfully!"), HttpStatus.OK);
    }

    @GetMapping("/myInfo")
    public ResponseEntity<?> findMyInfo() {
        AuthorDetailDto authorDetailDto = this.authorService.findByAuthentication();
        return new ResponseEntity<>(new CommonDto(authorDetailDto,
                HttpStatus.CREATED.value(), "마이페이지 출력 성공"), HttpStatus.OK);
    }


    // 비밀번호 수정 (email, password -> json) (/updatepw)
    // get : 조회, post : 등록, patch : 부분 수정, put : 대체, delete :
    // patch method 사용
    @PatchMapping("/updatepw")
    public ResponseEntity<String> updatePassword(@RequestBody AuthorUpdatePwDto authorUpdatePwDto) {
        try {
            this.authorService.updatePassword(authorUpdatePwDto);
            return new ResponseEntity<>("ok", HttpStatus.OK);
        } catch(NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        }
    }

    // 회원 탈퇴 (/author/1)
    // delete method 사용
    @DeleteMapping("/delete/{inputId}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long inputId) {
        try {
            authorService.delete(inputId);
            return new ResponseEntity<>("ok", HttpStatus.OK);
        } catch(NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        }
    }
}
