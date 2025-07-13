package main.java.com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface QueueEntryRepository extends JpaRepository<QueueEntry, Long> {
    List<QueueEntry> findByActiveTrueOrderByJoinedAtAsc();
    Optional<QueueEntry> findByUserAndActiveTrue(User user);
    boolean existsByUserAndActiveTrue(User user);
}