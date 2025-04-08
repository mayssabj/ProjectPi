package com.example.forum.Controller;

import com.example.forum.Service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/admin/posts")
public class AdminPostController {

    @Autowired
    IPostService postService;

    @GetMapping
    public ResponseEntity<Page<PostDTO>> getAllPostsForAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.getAllPosts(page, size));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePostAsAdmin(@PathVariable Integer postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

}