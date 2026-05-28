package com.dane.xhslite.dto;

import java.util.List;

public record PageResult<T>(List<T> records, int page, int size, long totalElements, int totalPages) {
}
