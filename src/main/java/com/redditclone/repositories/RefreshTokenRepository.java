package com.redditclone.repositories;

import java.util.Optional;

import com.redditclone.models.RefreshToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByToken(String token);

	void deleteByToken(String token);
}
