package com.example.forum.Service;

import com.example.forum.Entity.User;
import com.example.forum.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements  IUserService{

    @Autowired
    UserRepository userRepository;
    @Override
    public Optional<User> findById(Integer userId) {
        return userRepository.findById(userId);
    }
}
