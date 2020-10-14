package com.redditclone.repositories;

import java.util.Optional;

import com.redditclone.models.Post;
import com.redditclone.models.User;
import com.redditclone.models.Vote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

	Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User user);
}
