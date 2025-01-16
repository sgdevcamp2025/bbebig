package com.bbebig.user_server.application;

import com.bbebig.user_server.domain.User;
import com.bbebig.user_server.domain.UserRepository;
import com.bbebig.user_server.presentation.dto.UserModifyRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository){this.userRepository=userRepository;}

    public User searchUser(Long userId) {
        return userRepository.findById(userId).get();
    }

    @Transactional
    public void modifyUser(Long userId,UserModifyRequest request) {
        userRepository.findById(userId).get().modify(request);
    }
}
