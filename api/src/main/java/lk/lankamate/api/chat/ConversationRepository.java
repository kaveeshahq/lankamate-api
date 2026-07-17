package lk.lankamate.api.chat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    Optional<Conversation> findFirstByUserIdOrderByCreatedAtDesc(UUID userId);
}