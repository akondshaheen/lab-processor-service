package com.akond.lab.processor.consumer;

import com.akond.lab.processor.event.OrderCreatedEvent;
import com.akond.lab.processor.service.LabProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for consuming events coming from RabbitMQ.
 *
 * In an event-driven microservice architecture, services communicate
 * asynchronously using messages instead of direct HTTP calls.
 *
 * In our system, the Lab Order Service publishes an OrderCreatedEvent
 * to RabbitMQ when a new lab order is created.
 *
 * This consumer listens to that message queue and triggers the
 * lab processing logic when a new order event arrives.
 *
 * Key responsibilities of this class:
 * 1. Listen for incoming messages from RabbitMQ.
 * 2. Deserialize the message into OrderCreatedEvent.
 * 3. Pass the event to the LabProcessingService for business processing.
 **/
@Component
@RequiredArgsConstructor
public class OrderEventConsumer {
    /**
     * LabProcessingService contains the business logic
     * that processes the lab order and generates the final result.
     *
     * Constructor injection is automatically generated
     * by Lombok's @RequiredArgsConstructor annotation.
     **/
    private final LabProcessingService processingService;

    /**
     * @RabbitListener tells Spring to listen to a specific RabbitMQ queue.
     *
     * queues = "lab-order-created"
     *
     * Whenever a message arrives in this queue:
     * - Spring automatically converts the message
     *   into OrderCreatedEvent
     * - This method is triggered
     *
     * This mechanism allows the microservice to react
     * to events asynchronously.
     **/
    @RabbitListener(queues = "lab.order.created.queue")
    public void consume(OrderCreatedEvent event) {
        /**
         * Once the event is received, we delegate the work
         * to the service layer that performs the lab processing.
         **/
        processingService.processOrder(event);

    }
}