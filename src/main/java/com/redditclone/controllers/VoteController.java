package com.redditclone.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import static org.springframework.http.HttpStatus.OK;

import com.redditclone.dtos.VoteDto;
import com.redditclone.services.VoteService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/votes")
@AllArgsConstructor
public class VoteController {
    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<Void> vote(@RequestBody VoteDto voteDto) {
        voteService.vote(voteDto);

        return new ResponseEntity<>(OK);
    }
}
