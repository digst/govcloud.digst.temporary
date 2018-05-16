package app.kafka

import java.util.Properties

import com.govcloud.digst.Organisation
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

class ProduceData(propertiesConfigProducer:Properties,propertiesConfigTopics:Properties)
{

  val topic: String = propertiesConfigTopics.getProperty("topic.name")
  val producer:KafkaProducer[String,Organisation] = new KafkaProducer[String,Organisation](propertiesConfigProducer)
  var count:Int = 1



  def produceData(record:Organisation): Unit = {


    try
      {

        val producerRecord:ProducerRecord[String,Organisation] = new ProducerRecord[String,Organisation](topic, record)
        producer.send(producerRecord)
        println("Messages Sent: ", count)
        count+=1


      }catch
      {
        case e:Exception => println("Error in ProducerData: ", e.printStackTrace())
      }finally
      {
        producer.flush()
      }
  }


}
