package com.dane.xhslite.controller;

import com.dane.xhslite.common.ApiResponse;
import com.dane.xhslite.dto.NoteSummaryDTO;
import com.dane.xhslite.dto.PageResult;
import com.dane.xhslite.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notes")
@Validated
@Tag(name = "Note", description = "Note search APIs")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/search")
    @Operation(summary = "Search notes by keyword")
    public ApiResponse<PageResult<NoteSummaryDTO>> searchNotes(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {
        return ApiResponse.success(noteService.searchNotes(keyword, page, size));
    }
}
