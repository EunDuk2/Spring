package com.beyond.basic.b2_board.Service;

import com.beyond.basic.b2_board.Repository.AuthorMemoryRepository;
import com.beyond.basic.b2_board.domain.Author;
import com.beyond.basic.b2_board.dto.AuthorCreateDto;
import com.beyond.basic.b2_board.dto.AuthorUpdatePwDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service // Component로도 대체 가능 (트랜잭션 처리가 없는 경우에만)
// @Transaction 필요 (DB 사용할 때)
@RequiredArgsConstructor
public class AuthorService {

    // 의존성 주입(DI) 방법 1. Autowired 어노테이션 사용 -> 필드 주입
//    @Autowired
//    private AuthorRepository authorRepository;

    // 의존성 주입(DI) 방법 2. 생성자 주입 방식 (가장 많이 쓰는 방식)
    // 장점1) final을 통해 상수로 사용가능 (안전성 향상)
    // 장점2) 다형성 구현 가능 (인터페이스/상속)
    // 장점3) 순환참조 방지 (컴파일 타임에 check) / 이제는 둘 다 방지되긴 함. 순환참조 용어만 확인
//    private final AuthorRepositoryInterface authorMemoryRepository;
//    // 객체로 만들어지는 시점에 스프링에서 authorRepository 객체를 매개변수로 주입
//    @Autowired // 생성자가 하나밖에 없을 때에는 @Autowired 생략 가능
//    // 주입의 객체가 바뀌면 AuthorMemoryRepository 타입만 바꿔준다.
//    public AuthorService(AuthorMemoryRepository authorMemoryRepository) {
//        this.authorMemoryRepository = authorMemoryRepository;
//    }

    // 의존성 주입(DI) 방법 3. RequiredArgs 어노테이션 사용
    // -> 반드시 초기화 되어야 하는 필드(final 등)을 대상으로 생성자를 자동 생성 해주는 어노테이션
    // 다형성 설계는 불가능
    private final AuthorMemoryRepository authorMemoryRepository;



    // 회원가입 (객체 조립은 서비스 담당)
    public void save(AuthorCreateDto authorCreateDto) throws IllegalArgumentException {
        // 이메일 중복 검증
        boolean isValidEmail = this.authorMemoryRepository.isValidEmail(authorCreateDto.getEmail());
        if(isValidEmail) throw new IllegalArgumentException("중복되는 이메일입니다.");

        // 비밀번호 길이 검증
        if(authorCreateDto.getPassword().length() < 5) throw new IllegalArgumentException("비밀번호 길이가 짧습니다. (5자 이상)");

        // 회원가입 완료
        Author author = new Author(authorCreateDto.getName(), authorCreateDto.getEmail(),
                authorCreateDto.getPassword());
        this.authorMemoryRepository.save(author);
    }

    public List<Author> findAll() {
        return this.authorMemoryRepository.findAll();
    }

    // orElseThrow -> NoSuchElement
    // 회원 상세 조회
    public Author findById(Long id) throws NoSuchElementException {
        Optional<Author> optionalAuthor = this.authorMemoryRepository.findById(id);
        return optionalAuthor.orElseThrow(() -> new NoSuchElementException("없는 회원입니다."));
    }

    // 비밀번호 변경
    public void updatePassword(AuthorUpdatePwDto authorUpdatePwDto) {
        String newPassword = authorUpdatePwDto.getPassword();
        String email = authorUpdatePwDto.getEmail();
        Optional<Author> optionalAuthor = authorMemoryRepository.getAuthorByEmail(email);
        Author author = optionalAuthor.orElseThrow(() -> new NoSuchElementException("없는 회원입니다."));
        author.updatePw(newPassword);
    }

    // 회원 탈퇴
    public void delete(Long id) {
        this.authorMemoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("없는 회원입니다."));
        this.authorMemoryRepository.delete(id);
    }
}
