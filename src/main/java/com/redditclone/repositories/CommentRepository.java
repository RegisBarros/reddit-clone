package com.redditclone.repositories;

import java.util.List;

import com.redditclone.models.Comment;
import com.redditclone.models.Post;
import com.redditclone.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByPost(Post post);

	List<Comment> findAllByUser(User user);
}
