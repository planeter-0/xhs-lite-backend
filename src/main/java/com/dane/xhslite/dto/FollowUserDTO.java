package com.dane.xhslite.dto;

import java.time.LocalDateTime;

public record FollowUserDTO(Long userId, String nickname, String avatarUrl, String bio, LocalDateTime followedAt) {
}
