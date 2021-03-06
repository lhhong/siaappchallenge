package com.rep5.sialah.ibm.kafka.tests;

import com.rep5.sialah.common.models.SiaMessage;
import com.rep5.sialah.ibm.kafka.SiaConsumer;
import com.rep5.sialah.ibm.kafka.SiaProducer;
import com.rep5.sialah.ibm.kafka.ConsumerListener;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Created by low on 17/5/16.
 * Complete test of kafka producer and consumer, requires kafka service running
 */
public class KafkaTest {

    @Ignore
    @Test
    public void testManualPoll(){
        String testString = "TEST " + new Date(System.currentTimeMillis());
        String topic = "test";
        List<String> topics = new ArrayList<>();
        SiaConsumer consumer;
        SiaProducer producer = new SiaProducer();

        topics.add(topic);
        consumer = new SiaConsumer("sia-group", topics);


        try {
            sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        producer.write(topic, testString);

        try {
            sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("consumer to receive");

       String message = consumer.manualPollOne();

        consumer.shutdown();

        Assert.assertEquals(testString, message);
    }

    @Ignore
    @Test
    public void testKafka(){
        String testString = "TEST " + new Date(System.currentTimeMillis());
        String topic = "test";
        List<String> topics = new ArrayList<>();
        SiaConsumer consumer;
        SiaProducer producer = new SiaProducer();
        SiaMessage msg = new SiaMessage();

        topics.add(topic);
        consumer = new SiaConsumer("sia-group", topics);

        ConsumerListener listener = msg::setMessage;

        consumer.addListener(listener);
        Thread thread = new Thread(consumer);
        thread.start();

        try {
            sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        producer.write(topic, testString);
        try {
            sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        consumer.shutdown();

        Assert.assertEquals(testString, msg.getMessage());
    }
}
