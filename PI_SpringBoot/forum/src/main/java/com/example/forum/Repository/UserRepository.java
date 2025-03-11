package com.example.forum.Repository;

import com.example.forum.Entity.Reaction;
import com.example.forum.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
