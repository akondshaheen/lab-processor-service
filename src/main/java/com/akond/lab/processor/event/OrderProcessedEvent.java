package com.akond.lab.processor.event;

import lombok.Builder;
import lombok.Data;

/**
 * This class represents an event that is published AFTER the lab order
 * has been processed by the lab-processor-service.
 *
 * In the event-driven workflow:
 *
 * 1. lab-order-service publishes OrderCreatedEvent → RabbitMQ
 * 2. lab-processor-service consumes the event and processes the lab test
 * 3. After processing is completed, this OrderProcessedEvent is published
 *    to notify other services that the lab result is ready.
 *
 * This event is typically sent to AWS SNS so that multiple services
 * (notification service, patient service, reporting service, etc.)
 * can subscribe and react to the result.
 *
 * This class acts as the message payload that is published to SNS.
 */
@Data
@Builder
public class OrderProcessedEvent {
    private String orderId;
    private String status;
    private String result;
    private String eventTarget = "Event sent to Aws";
}
