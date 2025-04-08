package com.example.forum.Repository;

import com.example.forum.Entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
@Repository
public interface PostRepository  extends JpaRepository<Post, Integer> {
    // Retrieve only parent posts (posts without a parent)
    List<Post> findByParentIsNull();

    // Retrieve replies for a specific post
    List<Post> findByParentPostID(Integer parentId);

    @EntityGraph(attributePaths = {"replies", "reactions"})
    @Query("SELECT p FROM Post p WHERE p.notified = false")
    List<Post> findAllWithRepliesAndReactions();

    List<Post> findByContentContainingIgnoreCase(String keyword);
    Page<Post> findAll(Pageable pageable);

    Page<Post> findByAuthor_UserId(Integer userId, Pageable pageable);
}
