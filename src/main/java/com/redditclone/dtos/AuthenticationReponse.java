package com.redditclone.dtos;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationReponse {
    private String authenticationToken;
    private String username;
    private Instant expiresAt;
    private String refreshToken;
}
