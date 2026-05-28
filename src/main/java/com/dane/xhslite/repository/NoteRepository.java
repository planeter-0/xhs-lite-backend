package com.dane.xhslite.repository;

import com.dane.xhslite.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query("""
            SELECT n FROM Note n
            WHERE (:keyword IS NULL
                OR LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(COALESCE(n.tags, '')) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<Note> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
