//package oldClasses
//
//import java.io.{File, FileInputStream, InputStream}
//import java.util.{Properties, Timer}
//import javax.xml.parsers.{SAXParser, SAXParserFactory}
//
//import org.apache.kafka.clients.producer.KafkaProducer
//
//class RunAppTemp(producer:KafkaProducer[String, String], parser:SAXParser, connectionString:String, topicName:String) {
//
//
//  def run(): Unit = {
//
//    val timer:Timer = new Timer()
//    val data:List[File] = readData()
//    val app = new Run(producer,parser,data, topicName)
//    app.run()
//    timer.schedule(app, 0, 1000)
//
//  }
//
//
//  def readData(): List[File] = {
//
//    val f = new File(connectionString)
//
//    if (f.exists() && f.isDirectory)
//      {
//        f.listFiles().filter(_.isFile).toList
//
//      }
//    else
//      {
//        List[File]()
//      }
//  }
//
//
//
//}
//
//object RunAppTemp {
//
//
//  /***
//    *
//    * @param args
//    *             add path to producer properties
//    *             add path to topics properties
//    *             add connection string to data storage (files)
//    */
//  def main(args: Array[String]): Unit =
//  {
//    if (args.length<3)
//    {
//      println("No arguments is added")
//      sys.exit(-1)
//    }
//
//    val pathConfigProducer:String = args(0)
//    val pathConfigTopics:String = args(1)
//    val connectionString:String = args(2)
//
//    val propsProducer = readProperties(pathConfigProducer)
//    val propsTopics = readProperties(pathConfigTopics)
//    val topic:String = propsTopics.getProperty("topic.name")
//    val parser = setupParser()
//    val producer:KafkaProducer[String,String] = new KafkaProducer[String,String](propsProducer)
//    val app = new RunAppTemp(producer, parser, connectionString, topic)
//    app.run()
//
//  }
//
//  def setupParser(): SAXParser =
//  {
//
//    val parserFactory:SAXParserFactory = SAXParserFactory.newInstance()
//    parserFactory.setValidating(true)
//    val parser = parserFactory.newSAXParser()
//    parser
//
//  }
//
//
//  def readProperties(path:String): Properties = {
//
//    val props:Properties = new Properties()
//    val inStream:InputStream = new FileInputStream(path)
//    props.load(inStream)
//    props
//
//  }
//
//
//
//}