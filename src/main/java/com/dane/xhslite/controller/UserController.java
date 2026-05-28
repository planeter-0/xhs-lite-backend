package com.dane.xhslite.controller;

import com.dane.xhslite.common.ApiResponse;
import com.dane.xhslite.dto.UserDTO;
import com.dane.xhslite.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Validated
@Tag(name = "User", description = "User profile APIs")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user profile by userId")
    public ApiResponse<UserDTO> getUserById(@PathVariable @Positive Long userId) {
        return ApiResponse.success(userService.getUserById(userId));
    }
}
