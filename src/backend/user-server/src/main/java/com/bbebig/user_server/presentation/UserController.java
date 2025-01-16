package com.bbebig.user_server.presentation;

import com.bbebig.user_server.application.UserService;
import com.bbebig.user_server.domain.User;
import com.bbebig.user_server.presentation.dto.UserSearchResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){this.userService=userService;}

    @GetMapping("/{userId}")
    public ResponseEntity searchUser(@PathVariable Long userId){
        User user = userService.searchUser(userId);
        return ResponseEntity.ok(UserSearchResponse.of(user));
    }
}
