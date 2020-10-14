package com.redditclone.services;

import java.util.List;
import java.util.stream.Collectors;

import com.redditclone.dtos.PostRequest;
import com.redditclone.dtos.PostResponse;
import com.redditclone.exceptions.PostNotFoundException;
import com.redditclone.exceptions.SubredditNotFoundException;
import com.redditclone.mappers.PostMapper;
import com.redditclone.models.Post;
import com.redditclone.models.Subreddit;
import com.redditclone.models.User;
import com.redditclone.repositories.PostRepository;
import com.redditclone.repositories.SubredditRepository;
import com.redditclone.repositories.UserRepository;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class PostService {
    private final SubredditRepository subredditRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;

    public void save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));

        User currentUser = authService.getCurrentUser();

        Post post = postMapper.map(postRequest, subreddit, currentUser);

        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id " + id));

        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAll() {
        List<Post> posts = postRepository.findAll();

        return posts
            .stream()
            .map(postMapper::mapToDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
            .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));

        List<Post> posts = postRepository.findAllBySubreddit(subreddit);

        return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUserName(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));

        List<Post> posts = postRepository.findByUser(user);

        return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }
}
