package com.bbebig.user_server.application;

import com.bbebig.user_server.domain.User;
import com.bbebig.user_server.domain.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository){this.userRepository=userRepository;}

    public User searchUser(Long userId) {
        return userRepository.findById(userId).get();
    }
}
