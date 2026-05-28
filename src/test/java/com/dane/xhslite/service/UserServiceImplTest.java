package com.dane.xhslite.service;

import com.dane.xhslite.dto.UserDTO;
import com.dane.xhslite.entity.User;
import com.dane.xhslite.exception.ResourceNotFoundException;
import com.dane.xhslite.repository.UserRepository;
import com.dane.xhslite.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getUserById_shouldReturnUser() {
        User user = new User();
        user.setId(1L);
        user.setNickname("dane");
        user.setAvatarUrl("avatar");
        user.setBio("bio");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO dto = userService.getUserById(1L);
        assertEquals(1L, dto.id());
        assertEquals("dane", dto.nickname());
    }

    @Test
    void getUserById_shouldThrowWhenNotFound() {
        when(userRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(100L));
    }
}
