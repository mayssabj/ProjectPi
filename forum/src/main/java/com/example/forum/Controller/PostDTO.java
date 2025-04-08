package com.example.forum.Controller;

import com.example.forum.Entity.Post;
import com.example.forum.Entity.Reaction;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class PostDTO {
    private Integer postID;
    private String content;
    private String mediaURL;
    private LocalDateTime createdAt;
    private Integer authorId;
    private String authorUsername; // Added for username display
    private List<PostDTO> replies;
    private Integer parentId;
    private List<ReactionDTO> reactions;
    private Map<String, Long> reactionCounts;

    public PostDTO() {}

    public PostDTO(Post post) {
        this.postID = post.getPostID();
        this.content = post.getContent();
        this.mediaURL = post.getMediaURL();
        this.createdAt = post.getCreatedAt();
        this.authorId = post.getAuthor().getUserId();
        this.authorUsername = post.getAuthor().getFirstName()+" "+post.getAuthor().getLastName(); // Get username

        // Initialize parentId if this is a reply
        this.parentId = post.getParent() != null ? post.getParent().getPostID() : null;

        // Convert replies recursively and include reaction counts
        this.replies = post.getReplies() != null
                ? post.getReplies().stream()
                .sorted(Comparator.comparing(Post::getCreatedAt))
                .map(PostDTO::new)  // 🛠 This ensures replies have reaction counts
                .collect(Collectors.toList())
                : new ArrayList<>();

        // Initialize reactions
        this.reactions = post.getReactions() != null
                ? post.getReactions().stream()
                .map(ReactionDTO::new)
                .collect(Collectors.toList())
                : new ArrayList<>();

        // Compute reaction counts for posts & replies
        this.reactionCounts = post.getReactions() != null
                ? post.getReactions().stream()
                .collect(Collectors.groupingBy(
                        reaction -> reaction.getEmoji().toString(),
                        Collectors.counting()
                ))
                : new HashMap<>();
    }

    public PostDTO(Post post, List<Post> replies) {
        this.postID = post.getPostID();
        this.content = post.getContent();
        this.mediaURL = post.getMediaURL();
        this.createdAt = post.getCreatedAt();
        this.authorId = post.getAuthor().getUserId();
        this.authorUsername = post.getAuthor().getFirstName() + " " + post.getAuthor().getLastName();
        this.parentId = post.getParent() != null ? post.getParent().getPostID() : null;
        this.replies = replies.stream()
                .map(PostDTO::new)
                .collect(Collectors.toList());
    }

    // Getters and setters...
    // Getters
    public Integer getPostID() {
        return postID;
    }

    public String getContent() {
        return content;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public List<PostDTO> getReplies() {
        return replies;
    }

    public Integer getParentId() {
        return parentId;
    }

    // Setters (if needed for testing or other purposes)
    public void setPostID(Integer postID) {
        this.postID = postID;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMediaURL(String mediaURL) {
        this.mediaURL = mediaURL;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public void setReplies(List<PostDTO> replies) {
        this.replies = replies;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Map<String, Long> getReactionCounts() {
        return reactionCounts;
    }

    public void setReactionCounts(Map<String, Long> reactionCounts) {
        this.reactionCounts = reactionCounts;
    }

    // Setters if needed...
}