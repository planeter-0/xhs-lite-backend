package com.dane.xhslite.service.impl;

import com.dane.xhslite.dto.NoteSummaryDTO;
import com.dane.xhslite.dto.PageResult;
import com.dane.xhslite.entity.Note;
import com.dane.xhslite.repository.NoteRepository;
import com.dane.xhslite.service.NoteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    private static final int PREVIEW_MAX_LEN = 120;

    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<NoteSummaryDTO> searchNotes(String keyword, int page, int size) {
        String normalizedKeyword = normalizeKeyword(keyword);
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Note> notePage = noteRepository.searchByKeyword(normalizedKeyword, pageable);
        List<NoteSummaryDTO> records = notePage.getContent().stream()
                .map(this::toSummaryDTO)
                .toList();

        return new PageResult<>(records, notePage.getNumber(), notePage.getSize(), notePage.getTotalElements(), notePage.getTotalPages());
    }

    private String normalizeKeyword(String keyword) {
        if (keyword == null) {
            return null;
        }
        String trimmed = keyword.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private NoteSummaryDTO toSummaryDTO(Note note) {
        return new NoteSummaryDTO(
                note.getId(),
                note.getUserId(),
                note.getTitle(),
                toPreview(note.getContent()),
                note.getTags(),
                note.getCreatedAt()
        );
    }

    private String toPreview(String content) {
        if (content == null || content.length() <= PREVIEW_MAX_LEN) {
            return content;
        }
        return content.substring(0, PREVIEW_MAX_LEN) + "...";
    }
}
