package com.beyond.board.author;

import com.beyond.board.author.domain.Author;
import com.beyond.board.author.repository.AuthorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional // 테스트 완료 후 데이터가 실제 insert되지 않고, 롤백
public class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void authorSaveTest() {
        // 테스트 검증 로직 : 객체 생성 -> save -> 재조회 -> 조회한 객체와 저장한 객체가 동일한지 비교
        // 준비 (prepare, given)
        Author author = Author.builder()
                .name("abc")
                .email("abc123@naver.com")
                .password("1234")
                .build();

        // 실행 (execute, when)
        authorRepository.save(author);

        // 검증 (then)
        Author authorDb = authorRepository.findByEmail("abc123@naver.com").orElse(null);
        Assertions.assertEquals(author.getEmail(), authorDb.getEmail());

    }
}