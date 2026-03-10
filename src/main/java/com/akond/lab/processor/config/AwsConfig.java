package com.akond.lab.processor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sqs.SqsClient;

/*
 * This configuration class creates AWS SDK clients used by the application.
 *
 * These clients allow the microservice to communicate with AWS services
 * such as SNS (for publishing events) and SQS (for consuming events).
 *
 * In local development we typically run AWS services using LocalStack,
 * which emulates AWS services locally.
 *
 * Spring will register these clients as beans so they can be injected
 * into other services such as publishers or consumers.
 */

@Configuration
public class AwsConfig {

    /*
     * Creates an SNS client.
     *
     * SNS (Simple Notification Service) is used as a message broadcaster.
     * Our application will publish events such as OrderProcessedEvent
     * to an SNS topic.
     *
     * Any system subscribed to that topic will receive the event.
     */

    @Bean
    public SnsClient snsClient() {
        return SnsClient.builder()
                .region(Region.EU_WEST_1)
                .build();
    }

    /*
     * Creates an SQS client.
     *
     * SQS (Simple Queue Service) is used as a queue subscriber
     * to the SNS topic. Messages published to the SNS topic
     * will be delivered to this queue.
     */

    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .region(Region.EU_WEST_1)
                .build();
    }
}