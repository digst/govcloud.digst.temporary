//package test;
//
//
//import com.google.common.io.Resources;
//import org.apache.curator.test.TestingServer;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.Producer;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.IOException;
//import java.lang.management.ManagementFactory;
//import java.lang.management.OperatingSystemMXBean;
//import java.util.*;
//
//import static org.hamcrest.core.Is.is;
//import static org.junit.Assert.assertThat;
//
///**
// * Kafka testing at its most simple.
// * You'll need the following in your pom:
// *
// <dependencies>
// <dependency>
// <groupId>org.apache.kafka</groupId>
// <artifactId>kafka_2.10</artifactId>
// <version>0.8.2.1</version>
// </dependency>
// <dependency>
// <groupId>junit</groupId>
// <artifactId>junit</artifactId>
// <version>4.11</version>
// <scope>test</scope>
// </dependency>
// <dependency>
// <groupId>org.apache.curator</groupId>
// <artifactId>curator-test</artifactId>
// <version>2.8.0</version>
// <scope>test</scope>
// </dependency>
// </dependencies>
// */
//
//
////Todo kafka confluent: https://hub.docker.com/r/confluent/kafka/
//// todo: https://docs.confluent.io/current/installation/docker/docs/quickstart.html
//
//// todo how to: https://docs.confluent.io/current/installation/docker/docs/quickstart.html
//
//// https://gist.github.com/benstopford/49555b2962f93f6d50e3
//
//public class test_consumer_producer {
//
////    private String configPathConsumer = Resources.getResource("consumer/consumer.properties").getPath();
////    private String configPathProducer = Resources.getResource("consumer/consumer.properties").getPath();
//
//    public static final String topic = "test-sit-new";
//
//    private KafkaProducer<String, String> kafkaProducer;
//    private KafkaConsumer<String, String> kafkaConsumer;
//
//    private String KAFKAHOST = "localhost:9092";
//    private String ZOOKEEPERHOST = "localhost:2181";
//
//    @After
//    public void teardown()
//    {
//        if (kafkaProducer!=null)
//        {
//            kafkaProducer.close();
//        }
//
//        if (kafkaConsumer!=null)
//        {
//            kafkaConsumer.close();
//        }
//    }
//
//
////    @Test
////    public void testProducer()
////    {
////        kafkaProducer = new KafkaProducer<String, String>(producerProps());
////        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, "Peter Sergio Larsen");
////        kafkaProducer.send(record);
////
////    }
////
//    @Test
//    public void testConsumer()
//    {
//        List<String> topics = new ArrayList<String>();
//        topics.add(topic);
//
//        kafkaConsumer = new KafkaConsumer<String, String>(consumerProperties());
//        kafkaConsumer.subscribe(topics);
//        int count = 1;
//
//        try {
//            while (true) {
//                ConsumerRecords<String, String> records = kafkaConsumer.poll(50);
//                for (ConsumerRecord<String, String> record : records) {
//                    System.out.println(record.value());
//                    System.out.println(count);
//                    count+=1;
//
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    private Properties consumerProperties() {
//        Properties props = new Properties();
//        props.put("zookeeper.connect", serverProperties().get("zookeeper.connect"));
//        props.put("bootstrap.servers", KAFKAHOST);
//        props.put("auto.offset.reset", "earliest");
//        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        props.put("group.id", "demo");
//        return props;
//    }
////
////    private Properties producerProps() {
////        Properties props = new Properties();
////        props.put("bootstrap.servers", KAFKAHOST);
////        props.put("retries", 5);
////        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
////        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
////        props.put("request.required.acks", "1");
////        return props;
////    }
////
//    private Properties serverProperties() {
//        Properties props = new Properties();
//        props.put("zookeeper.connect", ZOOKEEPERHOST);
//        props.put("broker.id", "1");
//        return props;
//    }
//
//
// }
//
