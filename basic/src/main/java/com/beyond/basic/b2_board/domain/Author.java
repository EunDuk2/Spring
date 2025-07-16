package com.beyond.basic.b2_board.domain;

import com.beyond.basic.b2_board.Repository.AuthorMemoryRepository;
import com.beyond.basic.b2_board.dto.AuthorDetailDto;
import com.beyond.basic.b2_board.dto.AuthorListDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
// jpa를 사용할 경우 Entity에 반드시 붙여야 하는 어노테이션이다.
// jpa의 Entity Manager 에게 객체를 위임하기 위한 어노테이션
// Entity Manager는 영속성 Context(Entity의 현재 상황/맥락)를 통해 DB 데이터를 관리한다. (코드가 우선)
@Entity
public class Author {
    @Id // pk 설정
    // IDENTITY : auto_increment, AUTO : id 생성 전략을 jpa에게 자동설정하도록 위임하는 것.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;

    public Author(String name, String email, String password) {
//        this.id = AuthorMemoryRepository.id; // 추후 DB에 저장할 땐 빠지는 코드
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void updatePw(String password) {
        this.password = password;
    }

    public AuthorDetailDto detailfromEntity() {
        return new AuthorDetailDto(this.id, this.name, this.email);
    }
    public AuthorListDto listfromEntity() {
        return new AuthorListDto(this.id, this.name, this.email);
    }
}
