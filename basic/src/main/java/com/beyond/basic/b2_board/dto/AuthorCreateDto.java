package com.beyond.basic.b2_board.dto;

import com.beyond.basic.b2_board.domain.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data // dto 계층은 데이터의 안정성이 엔티티만큼은 중요하지 않으므로 Setter도 일반적으로 추가한다.
public class AuthorCreateDto {
    // id나 생성일시 같은 값은 X
    private String name;
    private String email;
    private String password;

    public Author authorToEntity() {
        return new Author(this.name, this.email, this.password);
    }

}
