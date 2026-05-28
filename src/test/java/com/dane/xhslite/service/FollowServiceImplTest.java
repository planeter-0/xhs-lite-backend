package com.dane.xhslite.service;

import com.dane.xhslite.dto.FollowUserDTO;
import com.dane.xhslite.dto.PageResult;
import com.dane.xhslite.entity.User;
import com.dane.xhslite.entity.UserFollow;
import com.dane.xhslite.exception.BusinessException;
import com.dane.xhslite.exception.ResourceNotFoundException;
import com.dane.xhslite.repository.UserFollowRepository;
import com.dane.xhslite.repository.UserRepository;
import com.dane.xhslite.service.impl.FollowServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserFollowRepository userFollowRepository;

    @InjectMocks
    private FollowServiceImpl followService;

    @Test
    void follow_shouldCreateRelationWhenNotExists() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(2L)).thenReturn(true);
        when(userFollowRepository.findByFollowerIdAndFolloweeId(1L, 2L)).thenReturn(Optional.empty());

        followService.follow(1L, 2L);

        ArgumentCaptor<UserFollow> captor = ArgumentCaptor.forClass(UserFollow.class);
        verify(userFollowRepository).save(captor.capture());
        assertEquals(1L, captor.getValue().getFollowerId());
        assertEquals(2L, captor.getValue().getFolloweeId());
        assertEquals(UserFollow.STATUS_ACTIVE, captor.getValue().getStatus());
    }

    @Test
    void follow_shouldThrowWhenFollowingSelf() {
        assertThrows(BusinessException.class, () -> followService.follow(1L, 1L));
    }

    @Test
    void follow_shouldThrowWhenAlreadyFollowing() {
        UserFollow relation = new UserFollow();
        relation.setFollowerId(1L);
        relation.setFolloweeId(2L);
        relation.setStatus(UserFollow.STATUS_ACTIVE);

        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(2L)).thenReturn(true);
        when(userFollowRepository.findByFollowerIdAndFolloweeId(1L, 2L)).thenReturn(Optional.of(relation));

        assertThrows(BusinessException.class, () -> followService.follow(1L, 2L));
    }

    @Test
    void unfollow_shouldMarkInactive() {
        UserFollow relation = new UserFollow();
        relation.setStatus(UserFollow.STATUS_ACTIVE);

        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(2L)).thenReturn(true);
        when(userFollowRepository.findByFollowerIdAndFolloweeId(1L, 2L)).thenReturn(Optional.of(relation));

        followService.unfollow(1L, 2L);

        assertEquals(UserFollow.STATUS_INACTIVE, relation.getStatus());
        verify(userFollowRepository).save(relation);
    }

    @Test
    void unfollow_shouldThrowWhenRelationNotFound() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(2L)).thenReturn(true);
        when(userFollowRepository.findByFollowerIdAndFolloweeId(1L, 2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> followService.unfollow(1L, 2L));
    }

    @Test
    void listFollowees_shouldReturnPagedUsers() {
        when(userRepository.existsById(1L)).thenReturn(true);

        UserFollow relation = new UserFollow();
        relation.setFollowerId(1L);
        relation.setFolloweeId(2L);
        relation.setStatus(UserFollow.STATUS_ACTIVE);
        relation.setCreatedAt(LocalDateTime.now());

        Page<UserFollow> page = new PageImpl<>(List.of(relation), PageRequest.of(0, 10), 1);
        when(userFollowRepository.findByFollowerIdAndStatusOrderByCreatedAtDesc(eq(1L), eq(UserFollow.STATUS_ACTIVE), any()))
                .thenReturn(page);

        User followee = new User();
        followee.setId(2L);
        followee.setNickname("amy");
        when(userRepository.findAllById(List.of(2L))).thenReturn(List.of(followee));

        PageResult<FollowUserDTO> result = followService.listFollowees(1L, 0, 10);

        assertEquals(1, result.records().size());
        assertEquals(2L, result.records().get(0).userId());
    }

    @Test
    void listFollowers_shouldReturnPagedUsers() {
        when(userRepository.existsById(1L)).thenReturn(true);

        UserFollow relation = new UserFollow();
        relation.setFollowerId(3L);
        relation.setFolloweeId(1L);
        relation.setStatus(UserFollow.STATUS_ACTIVE);
        relation.setCreatedAt(LocalDateTime.now());

        Page<UserFollow> page = new PageImpl<>(List.of(relation), PageRequest.of(0, 10), 1);
        when(userFollowRepository.findByFolloweeIdAndStatusOrderByCreatedAtDesc(eq(1L), eq(UserFollow.STATUS_ACTIVE), any()))
                .thenReturn(page);

        User follower = new User();
        follower.setId(3L);
        follower.setNickname("tim");
        when(userRepository.findAllById(List.of(3L))).thenReturn(List.of(follower));

        PageResult<FollowUserDTO> result = followService.listFollowers(1L, 0, 10);

        assertEquals(1, result.records().size());
        assertEquals(3L, result.records().get(0).userId());
    }
}
