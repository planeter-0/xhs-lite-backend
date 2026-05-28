package com.dane.xhslite.service.impl;

import com.dane.xhslite.common.ErrorCode;
import com.dane.xhslite.dto.FollowUserDTO;
import com.dane.xhslite.dto.PageResult;
import com.dane.xhslite.entity.User;
import com.dane.xhslite.entity.UserFollow;
import com.dane.xhslite.exception.BusinessException;
import com.dane.xhslite.exception.ResourceNotFoundException;
import com.dane.xhslite.repository.UserFollowRepository;
import com.dane.xhslite.repository.UserRepository;
import com.dane.xhslite.service.FollowService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FollowServiceImpl implements FollowService {

    private final UserRepository userRepository;
    private final UserFollowRepository userFollowRepository;

    public FollowServiceImpl(UserRepository userRepository, UserFollowRepository userFollowRepository) {
        this.userRepository = userRepository;
        this.userFollowRepository = userFollowRepository;
    }

    @Override
    @Transactional
    public void follow(Long followerId, Long followeeId) {
        validateFollowRelation(followerId, followeeId);
        ensureUserExists(followerId);
        ensureUserExists(followeeId);

        UserFollow relation = userFollowRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)
                .orElse(null);

        if (relation == null) {
            UserFollow newRelation = new UserFollow();
            newRelation.setFollowerId(followerId);
            newRelation.setFolloweeId(followeeId);
            newRelation.setStatus(UserFollow.STATUS_ACTIVE);
            userFollowRepository.save(newRelation);
            return;
        }

        if (relation.getStatus() == UserFollow.STATUS_ACTIVE) {
            throw new BusinessException(ErrorCode.CONFLICT, "You already follow this user");
        }

        relation.setStatus(UserFollow.STATUS_ACTIVE);
        userFollowRepository.save(relation);
    }

    @Override
    @Transactional
    public void unfollow(Long followerId, Long followeeId) {
        validateFollowRelation(followerId, followeeId);
        ensureUserExists(followerId);
        ensureUserExists(followeeId);

        UserFollow relation = userFollowRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Follow relation not found"));

        if (relation.getStatus() == UserFollow.STATUS_INACTIVE) {
            throw new BusinessException(ErrorCode.CONFLICT, "You already unfollowed this user");
        }

        relation.setStatus(UserFollow.STATUS_INACTIVE);
        userFollowRepository.save(relation);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<FollowUserDTO> listFollowees(Long userId, int page, int size) {
        ensureUserExists(userId);
        Pageable pageable = PageRequest.of(page, size);
        Page<UserFollow> relationPage = userFollowRepository
                .findByFollowerIdAndStatusOrderByCreatedAtDesc(userId, UserFollow.STATUS_ACTIVE, pageable);

        List<Long> followeeIds = relationPage.getContent().stream().map(UserFollow::getFolloweeId).toList();
        Map<Long, User> users = findUsersAsMap(followeeIds);

        List<FollowUserDTO> records = relationPage.getContent().stream()
                .map(relation -> toFollowUserDTO(users.get(relation.getFolloweeId()), relation))
                .toList();

        return toPageResult(records, relationPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<FollowUserDTO> listFollowers(Long userId, int page, int size) {
        ensureUserExists(userId);
        Pageable pageable = PageRequest.of(page, size);
        Page<UserFollow> relationPage = userFollowRepository
                .findByFolloweeIdAndStatusOrderByCreatedAtDesc(userId, UserFollow.STATUS_ACTIVE, pageable);

        List<Long> followerIds = relationPage.getContent().stream().map(UserFollow::getFollowerId).toList();
        Map<Long, User> users = findUsersAsMap(followerIds);

        List<FollowUserDTO> records = relationPage.getContent().stream()
                .map(relation -> toFollowUserDTO(users.get(relation.getFollowerId()), relation))
                .toList();

        return toPageResult(records, relationPage);
    }

    private void validateFollowRelation(Long followerId, Long followeeId) {
        if (followerId.equals(followeeId)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "User cannot follow/unfollow self");
        }
    }

    private void ensureUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found: " + userId);
        }
    }

    private Map<Long, User> findUsersAsMap(List<Long> userIds) {
        if (userIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return userRepository.findAllById(userIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));
    }

    private FollowUserDTO toFollowUserDTO(User user, UserFollow relation) {
        if (user == null) {
            return new FollowUserDTO(null, "[deleted]", null, null, relation.getCreatedAt());
        }
        return new FollowUserDTO(user.getId(), user.getNickname(), user.getAvatarUrl(), user.getBio(), relation.getCreatedAt());
    }

    private PageResult<FollowUserDTO> toPageResult(List<FollowUserDTO> records, Page<UserFollow> page) {
        return new PageResult<>(
                records,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
