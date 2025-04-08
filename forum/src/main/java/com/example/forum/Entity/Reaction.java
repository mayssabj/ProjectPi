package com.example.forum.Entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reactionID;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmojiType emoji;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post parent;

    public Integer getReactionID() {
        return reactionID;
    }

    public void setReactionID(Integer reactionID) {
        this.reactionID = reactionID;
    }

    public EmojiType getEmoji() {
        return emoji;
    }

    public void setEmoji(EmojiType emoji) {
        this.emoji = emoji;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Post getParent() {
        return parent;
    }

    public void setParent(Post parent) {
        this.parent = parent;
    }
}