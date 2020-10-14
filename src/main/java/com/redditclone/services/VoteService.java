package com.redditclone.services;

import java.util.Optional;

import com.redditclone.dtos.VoteDto;
import com.redditclone.exceptions.PostNotFoundException;
import com.redditclone.exceptions.RedditException;
import com.redditclone.models.Post;
import com.redditclone.models.User;
import com.redditclone.models.Vote;
import com.redditclone.models.VoteType;
import com.redditclone.repositories.PostRepository;
import com.redditclone.repositories.VoteRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post not found with id " + voteDto.getPostId()));

        User user = authService.getCurrentUser();

        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, user);

        if (voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType()))
            throw new RedditException("You have already " + voteDto.getVoteType() + "'d for this post");

        if (VoteType.UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }

        Vote vote = mapToVote(voteDto, post, user);

        voteRepository.save(vote);
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post, User user) {
        return Vote.builder().voteType(voteDto.getVoteType()).post(post).user(user).build();
    }
}
