package com.dane.xhslite.controller;

import com.dane.xhslite.common.ApiResponse;
import com.dane.xhslite.dto.FollowUserDTO;
import com.dane.xhslite.dto.PageResult;
import com.dane.xhslite.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Validated
@Tag(name = "Follow", description = "Follow relation APIs")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/{userId}/follow/{targetUserId}")
    @Operation(summary = "Follow a target user")
    public ApiResponse<String> follow(@PathVariable @Positive Long userId,
                                      @PathVariable @Positive Long targetUserId) {
        followService.follow(userId, targetUserId);
        return ApiResponse.success("followed");
    }

    @DeleteMapping("/{userId}/follow/{targetUserId}")
    @Operation(summary = "Unfollow a target user")
    public ApiResponse<String> unfollow(@PathVariable @Positive Long userId,
                                        @PathVariable @Positive Long targetUserId) {
        followService.unfollow(userId, targetUserId);
        return ApiResponse.success("unfollowed");
    }

    @GetMapping("/{userId}/followees")
    @Operation(summary = "List users that current user follows")
    public ApiResponse<PageResult<FollowUserDTO>> listFollowees(@PathVariable @Positive Long userId,
                                                                 @RequestParam(defaultValue = "0") @Min(0) int page,
                                                                 @RequestParam(defaultValue = "10") @Min(1) int size) {
        return ApiResponse.success(followService.listFollowees(userId, page, size));
    }

    @GetMapping("/{userId}/followers")
    @Operation(summary = "List users that follow current user")
    public ApiResponse<PageResult<FollowUserDTO>> listFollowers(@PathVariable @Positive Long userId,
                                                                 @RequestParam(defaultValue = "0") @Min(0) int page,
                                                                 @RequestParam(defaultValue = "10") @Min(1) int size) {
        return ApiResponse.success(followService.listFollowers(userId, page, size));
    }
}
