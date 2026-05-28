package com.dane.xhslite.repository;

import com.dane.xhslite.entity.UserFollow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {

    Optional<UserFollow> findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    Page<UserFollow> findByFollowerIdAndStatusOrderByCreatedAtDesc(Long followerId, Integer status, Pageable pageable);

    Page<UserFollow> findByFolloweeIdAndStatusOrderByCreatedAtDesc(Long followeeId, Integer status, Pageable pageable);
}
