package com.example.forum.Service;

import com.example.forum.Entity.Notification;
import com.example.forum.Entity.Post;
import com.example.forum.Entity.Reaction;
import com.example.forum.Repository.ReactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReactionServiceImpl implements IReactionService{

    @Autowired
    ReactionRepository reactionRepository;
    @Override
    public List<Reaction> retrieveAllReactions() {
        return reactionRepository.findAll();
    }

    @Override
    public Optional<Reaction> retrieveReaction(Integer id) {
        return reactionRepository.findById(id);
    }

    @Override
    public Reaction addReaction(Reaction reaction) {
        return reactionRepository.save(reaction);
    }

    @Override
    public void deleteReaction(Integer reaction) {
        reactionRepository.deleteById(reaction);
    }

    @Override
    public Reaction updateReaction(Reaction reaction) {
        return reactionRepository.save(reaction);
    }

}
