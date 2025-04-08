package com.example.forum.Controller;

import com.example.forum.Entity.Post;
import com.example.forum.Entity.User;
import com.example.forum.Repository.PostRepository;
import com.example.forum.Repository.ReactionRepository;
import com.example.forum.Repository.UserRepository;
import com.example.forum.Service.FileStorageService;
import com.example.forum.Service.IPostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200") // Allow Angular frontend to access the API
@RestController
@AllArgsConstructor
@RequestMapping("/post")
public class PostRestController {
    @Autowired
    IPostService postService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    ReactionRepository reactionRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FileStorageService fileStorageService;

    //http://localhost:8089/forum/swagger-ui/index.html

    /*@GetMapping("/get-all-posts")
    public List<Post> listAllPosts(){
        List<Post> postList=postService.retrieveAllPosts();
        return postList;
    }*/

    @GetMapping("/get-all-posts")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<Post> posts = postService.retrieveAllPosts();
        List<PostDTO> postDTOs = posts.stream().map(PostDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(postDTOs);
    }

    /*@GetMapping("/get-all-posts")
    public List<PostDTO> getAllPosts() {
        List<Post> posts = postService.retrieveAllPosts();
        return posts.stream().map(PostDTO::new).collect(Collectors.toList()); // ✅ Returns posts with reactions
    }*/




   /* @GetMapping("/display-post/{post-id}")
    public Optional<Post> displayPost(@PathVariable("post-id") Integer id){
        Optional<Post> post= postService.retrievePost(id);
        return  post;
    }*/

    @GetMapping("/display-post/{post-id}")
    public ResponseEntity<PostDTO> displayPostWithReplies(@PathVariable("post-id") Integer id) {
        Optional<Post> post = postService.retrievePost(id);
        if (post.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Post> replies = postService.retrieveReplies(id);
        PostDTO response = new PostDTO(post.get(), replies);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-parent-posts")
    public List<Post> listParentPosts(){
        return postService.retrieveParentPosts();
    }

    @GetMapping("/get-replies/{post-id}")
    public List<Post> getReplies(@PathVariable("post-id") Integer id){
        return postService.retrieveReplies(id);
    }

    @PostMapping(value = "/add-post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addPost(
            @RequestPart("post") String postJson,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            // Debug logging
            System.out.println("Received post creation request");

            // Parse JSON to a DTO first to handle parentId separately
            PostDTO postDto = objectMapper.readValue(postJson, PostDTO.class);

            // Validate required fields
            if (postDto.getContent() == null || postDto.getContent().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Post content cannot be empty"));
            }

            // Create new Post entity
            Post post = new Post();
            post.setContent(postDto.getContent());

            // Handle parent post for replies
            if (postDto.getParentId() != null) {
                Post parentPost = postRepository.findById(postDto.getParentId())
                        .orElseThrow(() -> new RuntimeException("Parent post not found"));
                post.setParent(parentPost);
            }

            // Handle file upload
            if (file != null && !file.isEmpty()) {
                String mediaUrl = fileStorageService.storeFile(file);
                post.setMediaURL(mediaUrl);
                System.out.println("Successfully stored file: " + mediaUrl);
            }

            // Set creation timestamp and author
            post.setCreatedAt(LocalDateTime.now());
            post.setAuthor(userRepository.findById(postDto.getAuthorId())
                    .orElseThrow(() -> new RuntimeException("User not found")));

            // Save post
            Post savedPost = postRepository.save(post);

            // Return the complete post with replies structure
            return ResponseEntity.ok(new PostDTO(savedPost));

        } catch (JsonProcessingException e) {
            System.out.println("JSON parsing error: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid post data format"));
        } catch (RuntimeException e) {
            System.out.println("Business logic error: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to create post"));
        }
    }
    private void validateFile(MultipartFile file) throws IllegalArgumentException {
        // Validate file type
        String contentType = file.getContentType();
        List<String> allowedTypes = Arrays.asList("image/jpeg", "image/png", "image/gif", "video/mp4");

        if (contentType == null || !allowedTypes.contains(contentType)) {
            throw new IllegalArgumentException(
                    "Unsupported file type. Only JPEG, PNG, GIF images and MP4 videos are allowed.");
        }

        // Validate file size (5MB max)
        long maxSize = 5 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException(
                    "File size exceeds maximum limit of 5MB");
        }
    }

    @DeleteMapping("/delete/{postID}")
    public ResponseEntity<String> deletePost(@PathVariable Integer postID) {
        postService.deletePost(postID);
        return ResponseEntity.ok("Post deleted successfully.");
    }

    @PutMapping(value = "/update/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePost(
            @PathVariable Integer postId,
            @RequestPart("post") String postJson,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            // 1. Parse the JSON
            Post postUpdates = objectMapper.readValue(postJson, Post.class);

            // 2. Get existing post
            Post existingPost = postService.findById(postId)
                    .orElseThrow(() -> new RuntimeException("Post not found"));

            // 3. Handle file upload if present
            if (file != null && !file.isEmpty()) {
                validateFile(file); // Use same validation as in addPost
                String mediaUrl = fileStorageService.storeFile(file);
                existingPost.setMediaURL(mediaUrl);
            }

            // 4. Update other fields
            if (postUpdates.getContent() != null) {
                existingPost.setContent(postUpdates.getContent());
            }

            // 5. Save the updated post
            Post updatedPost = postService.updatePost(existingPost);

            return ResponseEntity.ok(updatedPost);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to update post"));
        }
    }


    @GetMapping("/search")
    public ResponseEntity<List<PostDTO>> searchPosts(@RequestParam String keyword) {
        List<Post> posts = postService.searchPosts(keyword);
        List<PostDTO> postDTOs = posts.stream()
                .map(PostDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postDTOs);
    }

    @GetMapping("/get-posts")
    public ResponseEntity<Page<PostDTO>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.getAllPosts(page, size));
    }


}