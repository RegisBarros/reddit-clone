package com.redditclone.controllers;

import java.util.List;

import com.redditclone.dtos.PostRequest;
import com.redditclone.dtos.PostResponse;
import com.redditclone.services.PostService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody PostRequest postRequest) {
        postService.save(postRequest);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<PostResponse> get(@PathVariable Long id) {
        return status(HttpStatus.OK).body(postService.getPost(id));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> get() {
        return status(HttpStatus.OK).body(postService.getAll());
    }

    @GetMapping({"/by-subreddit/{id}"})
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@PathVariable Long id) {
        return status(HttpStatus.OK).body(postService.getPostsBySubreddit(id));
    }

    @GetMapping({"/by-user/{name}"})
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String name) {
        return status(HttpStatus.OK).body(postService.getPostsByUsername(name));
    }
}
