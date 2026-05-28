package com.dane.xhslite.dto;

import java.time.LocalDateTime;

public record NoteSummaryDTO(Long id, Long userId, String title, String contentPreview, String tags, LocalDateTime createdAt) {
}
