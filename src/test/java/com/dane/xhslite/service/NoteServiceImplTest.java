package com.dane.xhslite.service;

import com.dane.xhslite.dto.NoteSummaryDTO;
import com.dane.xhslite.dto.PageResult;
import com.dane.xhslite.entity.Note;
import com.dane.xhslite.repository.NoteRepository;
import com.dane.xhslite.service.impl.NoteServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NoteServiceImplTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteServiceImpl noteService;

    @Test
    void searchNotes_shouldNormalizeBlankKeywordAndReturnPage() {
        Note note = new Note();
        note.setId(1L);
        note.setUserId(2L);
        note.setTitle("title");
        note.setContent("content");
        note.setTags("java");
        note.setCreatedAt(LocalDateTime.now());

        Page<Note> page = new PageImpl<>(List.of(note), PageRequest.of(0, 10), 1);
        when(noteRepository.searchByKeyword(eq(null), eq(PageRequest.of(0, 10, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt")))))
                .thenReturn(page);

        PageResult<NoteSummaryDTO> result = noteService.searchNotes("   ", 0, 10);

        assertEquals(1, result.records().size());
        assertEquals("title", result.records().get(0).title());
    }

    @Test
    void searchNotes_shouldTruncateLongContent() {
        String longText = "x".repeat(130);
        Note note = new Note();
        note.setId(2L);
        note.setUserId(1L);
        note.setTitle("long");
        note.setContent(longText);
        note.setTags("backend");
        note.setCreatedAt(LocalDateTime.now());

        Page<Note> page = new PageImpl<>(List.of(note), PageRequest.of(0, 10), 1);
        when(noteRepository.searchByKeyword(eq("java"), eq(PageRequest.of(0, 10, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt")))))
                .thenReturn(page);

        PageResult<NoteSummaryDTO> result = noteService.searchNotes("java", 0, 10);

        assertEquals(1, result.records().size());
        String preview = result.records().get(0).contentPreview();
        assertEquals(123, preview.length());
    }
}
