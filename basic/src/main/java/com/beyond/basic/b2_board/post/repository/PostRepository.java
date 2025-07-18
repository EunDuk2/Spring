package com.beyond.basic.b2_board.post.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
//    select * from post where author_id = ? and title = ?;
//     List<Post> findByAuthorIdAndTitle(Long author, String title);

//    select * from post where author_id = ? and title = ? order by createdTime desc;
//     List<Post> findByAuthorIdAndTitleOrderByCreatedTimeDesc(Long author, String title);



    List<Post> findByAuthorId(Long authorId);
    List<Post> findByAuthor(Author author);

}
