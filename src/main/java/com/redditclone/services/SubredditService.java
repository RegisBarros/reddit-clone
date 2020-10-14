package com.redditclone.services;

import java.util.List;
import java.util.stream.Collectors;

import com.redditclone.dtos.SubredditDto;
import com.redditclone.exceptions.RedditException;
import com.redditclone.mappers.SubredditMapper;
import com.redditclone.models.Subreddit;
import com.redditclone.repositories.SubredditRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SubredditService {
    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit subreddit = subredditMapper.mapDtoToSubreddit(subredditDto);

        subreddit = subredditRepository.save(subreddit);

        subredditDto.setId(subreddit.getId());
        return subredditDto;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll().stream().map(subredditMapper::mapSubredditToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubredditDto get(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
            .orElseThrow(() -> new RedditException("No subreddit found with id " + id));

        return subredditMapper.mapSubredditToDto(subreddit);
    }
}
