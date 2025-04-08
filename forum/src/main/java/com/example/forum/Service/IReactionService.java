package com.example.forum.Service;

import com.example.forum.Entity.EmojiType;
import com.example.forum.Entity.Reaction;

import java.util.List;
import java.util.Optional;

public interface IReactionService {
    public List<Reaction> retrieveAllReactions();
    public Optional<Reaction> retrieveReaction(Integer id);
    public Reaction addReaction(Reaction reaction);
    public void deleteReaction(Integer reaction);
    public Reaction updateReaction(Reaction reaction);
    public List<Reaction> getReactionsByPost(Integer postId);
    public Reaction toggleReaction(Integer postId, Integer userId, EmojiType emoji);
}
