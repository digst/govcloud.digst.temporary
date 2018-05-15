package app.kafka

import java.io.{File}
import java.nio.file.{Files, Paths}
import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.json.{JSONException, XML}

class RunKafkaProducer(propertiesConfigProducer:Properties,propertiesConfigTopics:Properties, connectionString:String)
{

  val topic: String = propertiesConfigTopics.getProperty("topic.name")
  val producer:KafkaProducer[String,String] = new KafkaProducer[String,String](propertiesConfigProducer)


  def produceData(): Unit = {

    var count:Int = 1
    var data = readData()

    data.foreach(f => {

      val res = parserData(f.getAbsolutePath)
      val record:ProducerRecord[String,String] = new ProducerRecord[String,String](topic, res)
      producer.send(record)
      println("Messages Sent: ", count)
      count+=1

    })
  }

  def readData(): List[File] = {

    val f = new File(connectionString)

    if (f.exists() && f.isDirectory)
    {
      f.listFiles().filter(_.isFile).toList

    }
    else
    {
      List[File]()
    }
  }

  def parserData(pathToFile:String): String = {


    var PRETTY_PRINT_INDENT_FACTOR = 4
    var path = Paths.get(pathToFile)
    var TEST_XML_STRING_list = Files.readAllLines(path)
    var str:StringBuilder = StringBuilder.newBuilder

    TEST_XML_STRING_list.forEach(x=> {

      str.append(x)
    })

    var res:String = new String()


    try {
      val xmlJSONObj = XML.toJSONObject(str.toString())
      res = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR)
      System.out.println(res)
    } catch {
      case je: JSONException =>
        System.out.println(je.toString)
    }

    res


  }


}
