package com.github.ragudos.kompeter.auth;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.jetbrains.annotations.NotNull;

import com.github.ragudos.kompeter.database.dto.user.UserMetadataDto;

public class Session {

	private @NotNull UserMetadataDto user;
	private final @NotNull Timestamp expiresAt;
	private final @NotNull String sessionToken;
	private final String ipAddress;

	public Session(@NotNull UserMetadataDto user, @NotNull String sessionToken, @NotNull Timestamp expiresAt,
			String ipAddress) {
		this.user = user;
		this.sessionToken = sessionToken;
		this.expiresAt = expiresAt;
		this.ipAddress = ipAddress;
	}

	public static boolean isExpired(final @NotNull Timestamp ts) {
		return ts.before(Timestamp.valueOf(LocalDateTime.now()));
	}

	public boolean isExpired() {
		return isExpired(expiresAt);
	}

	public @NotNull UserMetadataDto user() {
		return user;
	}

	public @NotNull Timestamp expiresAt() {
		return expiresAt;
	}

	public @NotNull String sessionToken() {
		return sessionToken;
	}

	public String ipAddress() {
		return ipAddress;
	}
}
