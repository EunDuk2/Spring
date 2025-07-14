package com.beyond.basic.b2_board.Controller;

import com.beyond.basic.b2_board.Service.AuthorService;
import com.beyond.basic.b2_board.domain.Author;
import com.beyond.basic.b2_board.dto.AuthorCreateDto;
import com.beyond.basic.b2_board.dto.AuthorUpdatePwDto;
import com.sun.nio.sctp.IllegalReceiveException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController // @Controller + @ResponseBody (화면 리턴 X)
@RequiredArgsConstructor
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;

    // 회원가입
    @PostMapping("/create")
    public String save(@RequestBody AuthorCreateDto authorCreateDto) {
        try {
            this.authorService.save(authorCreateDto);
        } catch (IllegalReceiveException e) {
            return(e.toString());
        }
        return "ok";
    }

    // 회원 목록 조회 (/list)
    @GetMapping("/list")
    public List<Author> listAuthors() {
        List<Author> authorList = this.authorService.findAll();
        System.out.println(authorList);
        return authorList;
    }

    // 회원 상세 조회 : id로 조회 (/detail/1)
    // 서버에서 별도의 try catch를 하지 않으면, 에러 발생 시 500에러 + 스프링의 포맷으로 에러 리턴.
    @GetMapping("/detail/{inputId}")
    public Author findById(@PathVariable Long inputId) {
        Author author = null;
        try {
            author = this.authorService.findById(inputId);
            return author;
        } catch(NoSuchElementException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 비밀번호 수정 (email, password -> json) (/updatepw)
    // get : 조회, post : 등록, patch : 부분 수정, put : 대체, delete :
    // patch method 사용
    @PatchMapping("/updatepw")
    public String updatePassword(@RequestBody AuthorUpdatePwDto authorUpdatePwDto) {
        try {
            this.authorService.updatePassword(authorUpdatePwDto);
        } catch(NoSuchElementException e) {
            e.printStackTrace();
            return "failed";
        }
        return "ok";
    }

    // 회원 탈퇴 (/author/1)
    // delete method 사용
    @DeleteMapping("/delete/{inputId}")
    public void deleteAuthor(@PathVariable Long inputId) {
        try {
            authorService.delete(inputId);
        } catch(NoSuchElementException e) {
            e.printStackTrace();
        }
    }
}
