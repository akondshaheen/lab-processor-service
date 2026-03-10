package com.akond.lab.processor.service;


import com.akond.lab.processor.event.OrderProcessedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import tools.jackson.databind.ObjectMapper;

/*
 * This service is responsible for publishing events to AWS SNS.
 *
 * SNS (Simple Notification Service) is a messaging system
 * that allows a service to broadcast events to multiple
 * subscribers.
 *
 * In our architecture:
 *
 * Lab Processor Service
 *        ↓
 * Publish OrderProcessedEvent
 *        ↓
 * SNS Topic
 *        ↓
 * Multiple subscribers (SQS queues / microservices)
 *
 * This enables loose coupling between microservices,
 * meaning services do not need to know about each other
 * directly.
 */
@Service
@RequiredArgsConstructor
public class SnsPublisherService {
    /*
     * SNS client used to communicate with AWS SNS.
     * This client was configured in AwsConfig class.
     */
    private final SnsClient snsClient;

    /*
     * ObjectMapper is used to convert Java objects
     * into JSON format before sending them as messages.
     *
     * Messaging systems typically send data as JSON.
     */
    private final ObjectMapper objectMapper;

    /*
     * The SNS topic ARN where messages will be published.
     *
     * This value is injected from application.properties.
     *
     * Example:
     * aws.sns.topicArn=arn:aws:sns:eu-west-1:123456789012:lab-results-topic
     */
    @Value("${aws.sns.topicArn}")
    private String topicArn;

    /*
     * Publishes an OrderProcessedEvent to the SNS topic.
     *
     * Steps:
     * 1. Convert event object to JSON
     * 2. Create SNS publish request
     * 3. Send message to SNS topic
     */
    public void publish(OrderProcessedEvent event) {

        try {
            /*
             * Convert the event object into JSON string
             * so it can be transmitted through SNS.
             */
            String message = objectMapper.writeValueAsString(event);

            /*
             * Build the SNS publish request containing:
             * - Topic ARN (destination)
             * - Message payload
             */
            PublishRequest request = PublishRequest.builder()
                    .topicArn(topicArn)
                    .message(message)
                    .build();

            /*
             * Send the message to AWS SNS.
             * SNS will distribute this message to all
             * subscribed systems automatically.
             */
            snsClient.publish(request);

        } catch (Exception e) {
            /*
             * If publishing fails, the exception is wrapped
             * in a RuntimeException so it can be handled
             * by higher-level error handlers.
             */
            throw new RuntimeException(e);
        }
    }
}