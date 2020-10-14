package com.redditclone.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.redditclone.dtos.AuthenticationReponse;
import com.redditclone.dtos.LoginRequest;
import com.redditclone.dtos.RegisterRequest;
import com.redditclone.exceptions.RedditException;
import com.redditclone.models.NotificationEmail;
import com.redditclone.models.User;
import com.redditclone.models.VerificationToken;
import com.redditclone.repositories.UserRepository;
import com.redditclone.repositories.VerificationTokenRepository;
import com.redditclone.security.JwtProvider;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Transactional
    public void signup(RegisterRequest registerRequest) {
        var user = new User();
        user.setUserName(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);

        NotificationEmail notificationEmail = createTokenNotificationEmail(user.getEmail(), token);

        mailService.sendMail(notificationEmail);
    }

    public AuthenticationReponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String token = jwtProvider.generateToken(authenticate);

        return new AuthenticationReponse(token, loginRequest.getUsername());
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new RedditException("Invalid Token"));

        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        return userRepository.findByUserName(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();

        var verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    private NotificationEmail createTokenNotificationEmail(String userEmail, String token) {
        var notificationEmail = new NotificationEmail();
        notificationEmail.setSubject("Please Activate your Account");
        notificationEmail.setRecipient(userEmail);
        notificationEmail.setBody("Thank you for signing up to Reddit Clone, "
                + "please click on the below url to activate your account: "
                + "http://localhost:8080/api/auth/accountVerification/" + token);

        return notificationEmail;
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUserName();

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RedditException("User not found with name " + username));

        user.setEnabled(true);
        userRepository.save(user);
    }
}
