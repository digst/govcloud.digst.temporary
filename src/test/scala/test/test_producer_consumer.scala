package test

import java.io.{File, FileInputStream, InputStream}
import java.util
import java.util.Properties

import com.google.common.io.Resources
import com.govcloud.digst.Organisation
import org.apache.avro.Schema
import org.apache.kafka.clients.consumer.{ConsumerRecord, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.junit.{After, Test}
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericRecord
import org.apache.avro.Schema

// https://github.com/gwenshap/kafka-examples/tree/master/AvroProducerExample/src/main/java/com/shapira/examples/producer/avroclicks
// https://medium.com/@stephane.maarek/introduction-to-schemas-in-apache-kafka-with-the-confluent-schema-registry-3bf55e401321
// https://docs.confluent.io/current/installation/docker/docs/quickstart.html7

// todo: schema registry commands: https://github.com/confluentinc/schema-registry

// todo http://cloudurable.com/blog/kafka-avro-schema-registry/index.html

class test_producer_consumer {

  val topic:String = "test-sit-new"
  val pathConfigProducer:String = Resources.getResource("config/producer.properties").getPath

  val pathConfigConsumer:String = Resources.getResource("config/consumer.properties").getPath
  val pathConfigFiles:String = "/home/datascience/GovcloudData/files/"



  var producer:KafkaProducer[String, String] = _
  var consumer:KafkaConsumer[String,String] = _


  @After
  def closedown(): Unit = {

    if (producer!=null)
      {
        producer.close()
      }

    if (consumer!=null)
      {
        consumer.close()
      }


  }

  @Test
  def test_avro_producer(): Unit = {

    val topicAvro:String = "com.govcloud.digst.organisation"
    val properties:Properties = readProperties(pathConfigProducer)
    val producerAvro:KafkaProducer[String, Organisation] = new KafkaProducer[String, Organisation](properties)

    val mapping:Organisation = new Organisation()
    mapping.setName("hello man")
    mapping.setId("12")
    mapping.setParentid("11")
    mapping.setTemplate("www.sss.dk")
    mapping.setFoaauthorityid("no way")
    mapping.setExtras(new util.HashMap[CharSequence, CharSequence]())


    try {


      val record:ProducerRecord[String,Organisation] = new ProducerRecord[String,Organisation](topicAvro, mapping)
      producerAvro.send(record)
      producerAvro.flush()

    }
    catch
      {
        case e:Exception => println(e.printStackTrace())
      }



  }

  @Test
  def test_avro_consumer(): Unit =
  {

    val topicAvro:String = "com.govcloud.digst.organisation"
    val props:Properties = readProperties(pathConfigConsumer)
    var topics:util.ArrayList[String] = new util.ArrayList()
    topics.add(topicAvro)

    val consumerAvro:KafkaConsumer[String, Organisation] = new KafkaConsumer[String, Organisation](props)
    consumerAvro.subscribe(topics)

    try
    {
      while (true)
      {
        val records:ConsumerRecords[String,Organisation] = consumerAvro.poll(100)

        records.forEach(x => {

          val mapping:Organisation = x.value()

          println(x.value())

        })

      }


    }
    catch {

      case e:Exception => e.printStackTrace()

    }


  }


  @Test
  def test_producer(): Unit = {

    val properties:Properties = readProperties(pathConfigProducer)
    producer = new KafkaProducer[String,String](properties)
    val record:ProducerRecord[String,String] = new ProducerRecord[String,String](topic,"hello")
    producer.send(record)

  }

  @Test
  def test_consumer(): Unit = {

    val props:Properties = readProperties(pathConfigConsumer)
    var topics:util.ArrayList[String] = new util.ArrayList()
    topics.add(topic)

    consumer = new KafkaConsumer[String,String](props)
    consumer.subscribe(topics)

    try
    {
      while (true)
        {
          val records:ConsumerRecords[String,String] = consumer.poll(100)

          records.forEach(x => {

            println(x.value())

          })

        }
    }
    catch {

      case e:Exception => e.printStackTrace()

    }

  }

  @Test
  def read_files(): Unit =
  {

    val f = new File(pathConfigFiles)

    if (f.exists() && f.isDirectory)
    {
      f.listFiles().filter(_.isFile).toList

    }
    else
    {
      List[File]()
    }

    println(f)


  }


  def readProperties(path:String): Properties = {

    val props:Properties = new Properties()
    val inStream:InputStream = new FileInputStream(path)
    props.load(inStream)
    props

  }


}
