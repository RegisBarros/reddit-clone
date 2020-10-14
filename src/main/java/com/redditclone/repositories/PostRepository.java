package com.redditclone.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.redditclone.models.Post;
import com.redditclone.models.Subreddit;
import com.redditclone.models.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllBySubreddit(Subreddit subreddit);

	List<Post> findByUser(User user);
}
