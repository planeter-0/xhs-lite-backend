package com.dane.xhslite.service.impl;

import com.dane.xhslite.dto.UserDTO;
import com.dane.xhslite.entity.User;
import com.dane.xhslite.exception.ResourceNotFoundException;
import com.dane.xhslite.repository.UserRepository;
import com.dane.xhslite.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        return new UserDTO(user.getId(), user.getNickname(), user.getAvatarUrl(), user.getBio());
    }
}
