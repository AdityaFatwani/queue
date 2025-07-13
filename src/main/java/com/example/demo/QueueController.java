package main.java.com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/queue")
public class QueueController {

    @Autowired private QueueEntryRepository queueRepo;
    @Autowired private UserRepository userRepo;

    @PostMapping("/join")
    public ResponseEntity<?> joinQueue(Authentication auth) {
        User user = userRepo.findByUsername(auth.getName());
        if (queueRepo.existsByUserAndActiveTrue(user)) {
            return ResponseEntity.badRequest().body("Already in queue");
        }
        QueueEntry entry = new QueueEntry();
        entry.setUser(user);
        entry.setActive(true);
        entry.setJoinedAt(LocalDateTime.now());
        queueRepo.save(entry);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/leave")
    public ResponseEntity<?> leaveQueue(Authentication auth) {
        User user = userRepo.findByUsername(auth.getName());
        Optional<QueueEntry> entry = queueRepo.findByUserAndActiveTrue(user);
        if (entry.isPresent()) {
            entry.get().setActive(false);
            queueRepo.save(entry.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("Not in queue");
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> listQueue() {
        List<QueueEntry> entries = queueRepo.findByActiveTrueOrderByJoinedAtAsc();
        List<String> users = entries.stream().map(e -> e.getUser().getUsername()).collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }
}
