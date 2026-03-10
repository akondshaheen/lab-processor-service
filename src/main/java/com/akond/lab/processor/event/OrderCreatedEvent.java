package com.akond.lab.processor.event;

import com.akond.lab.processor.model.OrderCreationStatus;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;
/**
 * This class represents an EVENT that is received from RabbitMQ.
 *
 * In an event-driven architecture, services communicate by publishing
 * and consuming events instead of calling each other directly.
 *
 * The lab-order-service publishes this event when a new lab order is created.
 * The lab-processor-service consumes this event to start processing the lab test.
 *
 * This class acts as a Data Transfer Object (DTO) for the message
 * that travels through the message broker (RabbitMQ).
 */
@Data
public class OrderCreatedEvent {
    private UUID id;
    private Long patientId;
    private String testType;
    private OrderCreationStatus status;
    Instant createdAt;
}
