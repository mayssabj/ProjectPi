package com.example.forum.Service;

import com.example.forum.Entity.Post;
import com.example.forum.Repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostServiceImpl implements IPostService{

    @Autowired
    PostRepository postRepository;
    @Override
    public List<Post> retrieveAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Optional<Post> retrievePost(Integer id) {
        return postRepository.findById(id);
    }

    @Override
    public Post addPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public void deletePost(Integer postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public Post updatePost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public List<Post> retrieveParentPosts() {
        return postRepository.findByParentIsNull();
    }

    @Override
    public List<Post> retrieveReplies(Integer postId) {
        return postRepository.findByParentPostID(postId);
    }
}
