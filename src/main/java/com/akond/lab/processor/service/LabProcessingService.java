package com.akond.lab.processor.service;


import com.akond.lab.processor.event.OrderCreatedEvent;
import com.akond.lab.processor.event.OrderProcessedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*
 * This service contains the business logic responsible for
 * processing a lab order.
 *
 * Responsibilities of this class:
 *
 * 1. Receive the OrderCreatedEvent from the consumer
 * 2. Simulate or execute the lab test processing
 * 3. Create a new OrderProcessedEvent containing the result
 * 4. Publish the processed result event to AWS SNS
 *
 * This follows a common event-driven workflow:
 *
 * OrderCreatedEvent (RabbitMQ)
 *          ↓
 * LabProcessingService
 *          ↓
 * OrderProcessedEvent (SNS)
 **/
@Service
@RequiredArgsConstructor
public class LabProcessingService {
    /*
     * This service is responsible for publishing events
     * to AWS SNS after the order has been processed.
     *
     * It decouples business logic from infrastructure logic.
     **/
    private final SnsPublisherService snsPublisherService;

    /*
     * This method simulates the processing of a lab test.
     *
     * Steps:
     * 1. Receive the OrderCreatedEvent
     * 2. Perform lab processing logic
     * 3. Generate a result
     * 4. Create a new OrderProcessedEvent
     * 5. Publish the event to SNS
     **/
    public void processOrder(OrderCreatedEvent event) {

        /*
         * Simulate lab processing.
         *
         * In a real system this could involve:
         * - calling laboratory equipment APIs
         * - running data analysis
         * - querying medical databases
         **/
        String result = "NEGATIVE";

        /*
         * Create the event that represents the completed lab result.
         * This event will be sent to other microservices.
         **/
        OrderProcessedEvent processedEvent =
                OrderProcessedEvent.builder()
                        .orderId(String.valueOf(event.getId()))
                        .status("COMPLETED")
                        .result(result)
                        .build();

        /*
         * Publish the processed result event to AWS SNS.
         * SNS will distribute this message to all subscribed systems.
         */
        snsPublisherService.publish(processedEvent);

    }
}