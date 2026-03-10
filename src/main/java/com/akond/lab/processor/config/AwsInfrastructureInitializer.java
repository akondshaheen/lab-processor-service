package com.akond.lab.processor.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueAttributesRequest;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;

/*
 * This component initializes AWS messaging infrastructure
 * when the application starts.
 *
 * It performs three main tasks:
 *
 * 1. Creates an SNS topic
 * 2. Creates an SQS queue
 * 3. Subscribes the SQS queue to the SNS topic
 *
 * This is useful for local development (especially when using LocalStack)
 * because it avoids manually creating infrastructure every time
 * the application starts.
 *
 * In production environments, this infrastructure is usually created
 * using Infrastructure-as-Code tools such as Terraform or CloudFormation.
 */

@Component
@RequiredArgsConstructor
public class AwsInfrastructureInitializer {

    private final SnsClient snsClient;
    private final SqsClient sqsClient;

    private String topicArn;
    private String queueUrl;

    @PostConstruct
    public void initializeInfrastructure() {

        /*
         * STEP 1
         * Create SNS Topic
         *
         * SNS topics act as message broadcasters.
         * When a message is published to the topic,
         * all subscribed systems receive the message.
         */

        var topicResponse = snsClient.createTopic(
                CreateTopicRequest.builder()
                        .name("lab-order-processed-topic")
                        .build()
        );

        topicArn = topicResponse.topicArn();

        /*
         * STEP 2
         * Create SQS Queue
         *
         * SQS acts as a durable queue that stores messages
         * until a consumer processes them.
         */

        var queueResponse = sqsClient.createQueue(
                CreateQueueRequest.builder()
                        .queueName("lab-result-queue")
                        .build()
        );

        queueUrl = queueResponse.queueUrl();

        /*
         * STEP 3
         * Retrieve Queue ARN
         *
         * To subscribe an SQS queue to SNS,
         * we need the Queue ARN (not just the URL).
         */

        var queueAttributes = sqsClient.getQueueAttributes(
                GetQueueAttributesRequest.builder()
                        .queueUrl(queueUrl)
                        .attributeNames(QueueAttributeName.valueOf("QueueArn"))
                        .build()
        );

        String queueArn = queueAttributes.attributes().get("QueueArn");

        /*
         * STEP 4
         * Subscribe the Queue to the Topic
         *
         * This connects SNS → SQS.
         *
         * Now when the application publishes a message
         * to the SNS topic, the message will automatically
         * be delivered to the SQS queue.
         */

        snsClient.subscribe(
                SubscribeRequest.builder()
                        .topicArn(topicArn)
                        .protocol("sqs")
                        .endpoint(queueArn)
                        .build()
        );
    }
}