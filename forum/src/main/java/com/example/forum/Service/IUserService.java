package com.example.forum.Service;

import com.example.forum.Entity.Post;
import com.example.forum.Entity.User;

import java.util.Optional;

public interface IUserService {
    public Optional<User> findById (Integer userId);
}
