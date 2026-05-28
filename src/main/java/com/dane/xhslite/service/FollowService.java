package com.dane.xhslite.service;

import com.dane.xhslite.dto.FollowUserDTO;
import com.dane.xhslite.dto.PageResult;

public interface FollowService {
    void follow(Long followerId, Long followeeId);

    void unfollow(Long followerId, Long followeeId);

    PageResult<FollowUserDTO> listFollowees(Long userId, int page, int size);

    PageResult<FollowUserDTO> listFollowers(Long userId, int page, int size);
}
