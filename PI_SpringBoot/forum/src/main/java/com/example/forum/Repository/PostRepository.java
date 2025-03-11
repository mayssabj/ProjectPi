package com.example.forum.Repository;

import com.example.forum.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository  extends JpaRepository<Post, Integer> {
    // Retrieve only parent posts (posts without a parent)
    List<Post> findByParentIsNull();

    // Retrieve replies for a specific post
    List<Post> findByParentPostID(Integer parentId);
}
