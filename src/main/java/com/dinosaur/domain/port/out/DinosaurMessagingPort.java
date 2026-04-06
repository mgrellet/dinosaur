package com.dinosaur.domain.port.out;

import java.time.LocalDateTime;

public interface DinosaurMessagingPort {
    void sendStatusUpdate(Long dinosaurId, String newStatus, LocalDateTime timestamp);
}
