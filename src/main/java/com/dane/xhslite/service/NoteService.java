package com.dane.xhslite.service;

import com.dane.xhslite.dto.NoteSummaryDTO;
import com.dane.xhslite.dto.PageResult;

public interface NoteService {
    PageResult<NoteSummaryDTO> searchNotes(String keyword, int page, int size);
}
