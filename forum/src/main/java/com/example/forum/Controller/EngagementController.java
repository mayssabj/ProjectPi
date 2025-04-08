package com.example.forum.Controller;
import com.example.forum.Service.EngagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/engagement")
public class EngagementController {

    private final EngagementService engagementService;

    @Autowired
    public EngagementController(EngagementService engagementService) {
        this.engagementService = engagementService;
    }

    @GetMapping("/check")
    public ResponseEntity<String> checkEngagement() {
        engagementService.checkPostEngagement();
        return ResponseEntity.ok("Engagement check executed.");
    }
}
