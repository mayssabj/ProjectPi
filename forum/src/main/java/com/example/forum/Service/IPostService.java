package com.example.forum.Service;

import com.example.forum.Controller.PostDTO;
import com.example.forum.Entity.Post;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IPostService {
    public List<Post> retrieveAllPosts();
    public Optional<Post> retrievePost(Integer id);
    public Post addPost(Post post);
    public void deletePost(Integer post);
    public Post updatePost(Post post);

    public List<Post> retrieveParentPosts();
    public List<Post> retrieveReplies(Integer postId);
    public Optional<Post> findById (Integer postId);
    public List<Post> searchPosts(String keyword);

    Page<PostDTO> getAllPosts(int page, int size);

}
