//package oldClasses
//
//import java.io.File
//import java.util.TimerTask
//import javax.xml.parsers.SAXParser
//
//import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
//import parser.XMLHandler
//
//class Run(producer:KafkaProducer[String, String], parser:SAXParser, data:List[File], topicName:String) extends TimerTask
//{
//
//  val xmlHandler:XMLHandler = new XMLHandler()
//
//  override def run() {
//
//    var count:Int = 1
//
//    data.foreach(f => {
//
//      parser.parse(f, xmlHandler)
//      val res = xmlHandler.seqJSON
//
//      res.foreach(obj => {
//
//        val record:ProducerRecord[String,String] = new ProducerRecord[String,String](topicName, obj.toString)
//        producer.send(record)
//        println("Messages Sent: ", count)
//        count+=1
//
//      })
//
//
//    })
//
//
//
//  }
//
//}
