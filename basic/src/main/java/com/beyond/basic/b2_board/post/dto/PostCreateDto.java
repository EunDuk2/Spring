package com.beyond.basic.b2_board.post.dto;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.domain.Post;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateDto {
    @NotEmpty(message = "제목 비어있음")
    private String title;
    private String contents;
    @Builder.Default
    private String appointment = "N";
    // 시간정보는 직접 LocalDateTime으로 형변환 하는 경우가 많음.
    private String appointmentTime;
//    @NotNull // 숫자는 NotEmpty 사용불가
//    private Long authorId;

    public Post toEntity(Author author, LocalDateTime localDateTime) {
        return Post.builder()
                .title(this.title)
                .contents(this.contents)
//                .authorId(this.authorId)
                .author(author)
                .delYn("N")
                .appointment(this.appointment)
                .appointmentTIme(localDateTime)
                .build();
    }

}
