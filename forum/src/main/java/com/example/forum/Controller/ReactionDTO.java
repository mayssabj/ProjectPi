package com.example.forum.Controller;

import com.example.forum.Entity.EmojiType;
import com.example.forum.Entity.Reaction;

import java.time.LocalDateTime;

public class ReactionDTO {
    private Integer reactionId;
    private EmojiType emoji;
    private Integer userId;
    private String username;
    private LocalDateTime createdAt;

    public ReactionDTO(Reaction reaction) {
        this.reactionId = reaction.getReactionID();
        this.emoji = reaction.getEmoji();
        this.userId = reaction.getUser().getUserId();
        this.username = reaction.getUser().getFirstName() + " " + reaction.getUser().getLastName();
        this.createdAt = reaction.getCreatedAt();
    }
}
