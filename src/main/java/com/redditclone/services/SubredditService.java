package com.redditclone.services;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.redditclone.dtos.SubredditDto;
import com.redditclone.models.Subreddit;
import com.redditclone.repositories.SubredditRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {
    private final SubredditRepository subredditRepository;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit subreddit = mapSubredditDto(subredditDto);

        subreddit = subredditRepository.save(subreddit);

        subredditDto.setId(subreddit.getId());
        return subredditDto;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    private SubredditDto mapToDto(Subreddit subreddit) {
        return SubredditDto
                .builder()
                .id(subreddit.getId())
                .name(subreddit.getName())
                .description(subreddit.getDescription())
                .postCount(subreddit.getPosts().size())
                .build();
    }

    private Subreddit mapSubredditDto(SubredditDto subredditDto) {
        return Subreddit.builder().name(subredditDto.getName())
                    .description(subredditDto.getDescription())
                    .build();
    }
}
