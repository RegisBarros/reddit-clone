package com.redditclone.services;

import static java.util.stream.Collectors.toList;

import java.util.List;

import com.redditclone.dtos.CommentDto;
import com.redditclone.exceptions.PostNotFoundException;
import com.redditclone.mappers.CommentMapper;
import com.redditclone.models.Comment;
import com.redditclone.models.NotificationEmail;
import com.redditclone.models.Post;
import com.redditclone.models.User;
import com.redditclone.repositories.CommentRepository;
import com.redditclone.repositories.PostRepository;
import com.redditclone.repositories.UserRepository;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentService {
    private static final String POST_URL = "";
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final MailService mailService;
    private final MailContentBuilder mailContentBuilder;

    @Transactional
    public void save(CommentDto commentDto) {
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentDto.getPostId().toString()));

        User user = authService.getCurrentUser();

        Comment comment = commentMapper.map(commentDto, post, user);

        commentRepository.save(comment);

        String message = mailContentBuilder
                .build(post.getUser().getUserName() + " post a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(
                new NotificationEmail(user.getUserName() + " commented on your post", user.getEmail(), message));
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));

        List<Comment> comments = commentRepository.findByPost(post);

        return comments.stream().map(commentMapper::mapToDto).collect(toList());
	}

    @Transactional(readOnly = true)
	public List<CommentDto>  getAllCommentsForUser(String userName) {
        User user = userRepository.findByUserName(userName)
            .orElseThrow(() -> new UsernameNotFoundException(userName));

        List<Comment> comments = commentRepository.findAllByUser(user);

		return comments.stream().map(commentMapper::mapToDto).collect(toList());
	}
}
