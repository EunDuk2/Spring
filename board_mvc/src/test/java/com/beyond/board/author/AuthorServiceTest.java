package com.beyond.board.author;

import com.beyond.board.author.domain.Author;
import com.beyond.board.author.dto.AuthorCreateDto;
import com.beyond.board.author.repository.AuthorRepository;
import com.beyond.board.author.service.AuthorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional // 테스트 완료 후 데이터가 실제 insert되지 않고, 롤백
public class AuthorServiceTest {
    @Autowired
    private AuthorService authorService;
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void authorSaveTest() {
        // given
        AuthorCreateDto authorCreateDto = AuthorCreateDto.builder()
                .name("abc")
                .email("abc123@naver.com")
                .password("123412341234")
                .build();

        // when
        authorService.save(authorCreateDto);

        // then
        Author authorDb = authorRepository.findByEmail("abc123@naver.com").orElse(null);
        Assertions.assertEquals(authorCreateDto.getEmail(), authorDb.getEmail());
    }

    @Test
    public void findAllTest() {
        // findAll을 통한 결과값의 개수가 맞는지를 검증

        // 최초개수 구하고
        Long beforeSize = authorService.findAll().stream().count();

        // 데이터 추가(3개)하고
        authorRepository.save(Author.builder().name("abc").email("a1@naver.com").password("12341234").build());
        authorRepository.save(Author.builder().name("abc").email("a2@naver.com").password("12341234").build());
        authorRepository.save(Author.builder().name("abc").email("a3@naver.com").password("12341234").build());


        // 마지막 개수 구해서 최초개수+3하고 일치하는지 여부 검증
        Long afterSize = authorService.findAll().stream().count();

        Assertions.assertEquals(beforeSize+3, afterSize);

    }


}
