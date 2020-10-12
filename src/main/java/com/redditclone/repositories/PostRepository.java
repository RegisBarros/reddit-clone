package com.redditclone.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.redditclone.models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
