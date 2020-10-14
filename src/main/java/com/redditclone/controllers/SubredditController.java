package com.redditclone.controllers;

import java.util.List;

import com.redditclone.dtos.SubredditDto;
import com.redditclone.services.SubredditService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/subreddits")
@AllArgsConstructor
public class SubredditController {
    private final SubredditService subredditService;

    @PostMapping
    public ResponseEntity<SubredditDto> create(@RequestBody SubredditDto subredditDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subredditService.save(subredditDto));
    }

    @GetMapping
    public ResponseEntity<List<SubredditDto>> get() {
        return ResponseEntity.status(HttpStatus.OK).body(subredditService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<SubredditDto> get(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(subredditService.get(id));
    }
}
