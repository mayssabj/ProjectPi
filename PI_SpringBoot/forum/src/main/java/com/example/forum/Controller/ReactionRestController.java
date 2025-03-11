package com.example.forum.Controller;

import com.example.forum.Entity.Notification;
import com.example.forum.Entity.Post;
import com.example.forum.Entity.Reaction;
import com.example.forum.Service.IReactionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/reaction")
public class ReactionRestController {

    @Autowired
    IReactionService reactionService;

    @GetMapping("/get-all-reactions")
    public List<Reaction> listAllReactions(){
        return reactionService.retrieveAllReactions();
    }

    @GetMapping("/display-reaction/{reaction-id}")
    public Optional<Reaction> displayReaction(@PathVariable("reaction-id") Integer id){
        return reactionService.retrieveReaction(id);
    }

    @PostMapping("/add-reaction")
    public Post addReaction(@RequestBody Reaction r) {
        return reactionService.addReaction(r).getParent();
    }

    @DeleteMapping("/delete-reaction/{reaction-id}")
    public  void deleteReaction(@PathVariable ("reaction-id")Integer id){
        reactionService.deleteReaction(id);
    }

    @PutMapping("/update/{reactionID}")
    public Post updateReaction(@RequestBody Reaction reaction){
        return reactionService.updateReaction(reaction).getParent();
    }

}
