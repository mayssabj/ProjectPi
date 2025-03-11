package com.example.forum.Controller;

import com.example.forum.Entity.Notification;
import com.example.forum.Entity.Post;
import com.example.forum.Entity.User;
import com.example.forum.Repository.NotificationRepository;
import com.example.forum.Repository.PostRepository;
import com.example.forum.Repository.ReactionRepository;
import com.example.forum.Repository.UserRepository;
import com.example.forum.Service.INotificationService;
import com.example.forum.Service.IPostService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    INotificationService notificationService;
    @Autowired
    ReactionRepository reactionRepository;
    //http://localhost:8089/forum/swagger-ui/index.html

    @GetMapping("/get-all-posts")
    public List<Post> listAllPosts(){
        List<Post> postList=postService.retrieveAllPosts();
        return postList;
    }

    @GetMapping("/display-post/{post-id}")
    public Optional<Post> displayPost(@PathVariable("post-id") Integer id){
        Optional<Post> post= postService.retrievePost(id);
        return  post;
    }

    @GetMapping("/get-parent-posts")
    public List<Post> listParentPosts(){
        return postService.retrieveParentPosts();
    }

    @GetMapping("/get-replies/{post-id}")
    public List<Post> getReplies(@PathVariable("post-id") Integer id){
        return postService.retrieveReplies(id);
    }


    @PostMapping("/add-post")
    public Post addPost(@RequestBody Post p) {
        // Fetch the User with id = 1 from the database
        User author = userRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("User with id = 1 not found"));

        // Set the author of the Post to the fetched User
        p.setAuthor(author);

        // Save the Post
        Post post = postService.addPost(p);
        return post;
    }

    @PostMapping("/add-post-reply")
    public Post addPost(@RequestBody Post p, @RequestParam(required = false) Integer parentPostId) {
        // Fetch the User with id = 2 from the database
        User author = userRepository.findById(2)
                .orElseThrow(() -> new RuntimeException("User with id = 1 not found"));

        // Set the author of the Post to the fetched User
        p.setAuthor(author);

        // If parentPostId is provided, fetch the parent post and set it
        if (parentPostId != null) {
            Post parentPost = postRepository.findById(parentPostId)
                    .orElseThrow(() -> new RuntimeException("Parent post not found"));
            p.setParent(parentPost);

            // Check if the reply is made by a different user
            if (!parentPost.getAuthor().getUserID().equals(author.getUserID())) {
                // Create a notification for the author of the parent post
                Notification notification = new Notification();
                notification.setUser(parentPost.getAuthor()); // Notify the parent post's author
                notification.setMessage("Your post has a new reply from " + author.getFirstName() + " " + author.getLastName());
                notification.setCreatedAt(LocalDateTime.now());
                notificationService.addNotification(notification);
            }
        }

        // Save the Post
        Post post = postService.addPost(p);
        return post;
    }

    @DeleteMapping("/delete/{postID}")
    public ResponseEntity<String> deletePost(@PathVariable Integer postID) {
        postService.deletePost(postID);
        return ResponseEntity.ok("Post deleted successfully.");
    }

    @PutMapping("/update/{postID}")
    public Post updatePost(@RequestBody Post post){
        return postService.updatePost(post);
    }


}
