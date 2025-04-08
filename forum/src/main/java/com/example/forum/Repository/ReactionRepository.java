package com.example.forum.Repository;

import com.example.forum.Entity.Post;
import com.example.forum.Entity.Reaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Integer> {
    Optional<Reaction> findByParentPostIDAndUserUserId(Integer postId, Integer userId);

    List<Reaction> findByParentPostID(Integer postId);
}
