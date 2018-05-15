package test

import java.io.{File, FileInputStream, InputStream}
import java.util
import java.util.Properties

import com.google.common.io.Resources
import org.apache.kafka.clients.consumer.{ConsumerRecord, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.junit.{After, Test}

class test_producer_consumer {

  val topic:String = "test-sit-new"
  val pathConfigProducer:String = Resources.getResource("config/producer.properties").getPath

  val pathConfigConsumer:String = Resources.getResource("config/consumer.properties").getPath
  val pathConfigFiles:String = Resources.getResource("files").getPath



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
  def test_avro(): Unit = {

    val properties:Properties = readProperties(pathConfigProducer)
    producer = new KafkaProducer[String,String](properties)


    val record:ProducerRecord[String,String] = new ProducerRecord[String,String](topic,"hello")

    producer.send(record)


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
