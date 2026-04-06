package com.dinosaur.infrastructure.out.messaging;

import java.time.LocalDateTime;

public record DinosaurStatusUpdateMessage(Long dinosaurId, String newStatus, LocalDateTime timestamp) {
    
}
