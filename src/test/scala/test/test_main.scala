package test

import java.io.{BufferedWriter, File, FileWriter}
import java.nio.file.{Files, Paths}

import org.junit.{Before, Ignore, Test}
import com.google.common.io.Resources

import scala.io.Source
import javax.xml.parsers.{DocumentBuilderFactory, SAXParser, SAXParserFactory}

import app.kafka.RunKafkaProducer
import oldClasses.RunAppTemp
import org.json.{JSONException, JSONObject, XML}
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

import scala.xml.{Elem, NodeSeq}


// MapR Maven Repo:
// SAX Parser: https://www.journaldev.com/1198/java-sax-parser-example

// todo: http://www.johnarutz.com/programming-resources/data-conversion-between-xml-and-json-w-scala/
// todo: kafka connect https://www.confluent.io/blog/simplest-useful-kafka-connect-data-pipeline-world-thereabouts-part-1/


class test_main {

  val path:String = Resources.getResource("DIGST/Myndigheder_eksport.xml").getPath
  val pathSave:String = Resources.getResource("files/").getPath
  val patCopy:String = Resources.getResource("copy/Myndigheder_eksport.xml").getPath
  var runApp:RunAppTemp = _


  @Test
  def parse_data_xml(): Unit = {

    val id:String = "id="
    val tid:String = "tid="
    val name:String = "name="
    var fileID:String = null

    var parrentMessages = ""
    var count = 0

    for (line <- Source.fromFile(path).getLines())
      {
        if (count==0)
          {
            val idx_id = line.indexOf(id)
            val idx_tid = line.indexOf(tid)

            fileID = line.slice(idx_id+5, idx_tid-3)
            count+=1
          }

        if (line.startsWith("</item"))
          {
            parrentMessages += line

            saveFile(parrentMessages, pathSave+"/"+fileID +".xml")
            parrentMessages = ""
            count+=1
          }
        else
        {
          parrentMessages+=line + "\n"
        }


      }


    println("number of organisations: ", count)

  }

  @Test
  def parse_single_file(): Unit =
  {

    val file_test = pathSave + "0B47D949-FFA8-48BA-9D26-92F7D441CB66.xml"
    val parserFactory = SAXParserFactory.newInstance()
    parserFactory.setValidating(true)
    val parser = parserFactory.newSAXParser()
    val file = new File(file_test)
    val xMLHandler:XMLHandler = new XMLHandler()
    parser.parse(file, xMLHandler)
    val res = xMLHandler.seqJSON


    for (x <- res)
      {
        println(x.toString())
      }



  }

  @Test
  def parse_big_file(): Unit =
  {

    val file_test =patCopy
    val parserFactory = SAXParserFactory.newInstance()
    parserFactory.setValidating(true)
    val parser = parserFactory.newSAXParser()
    val file = new File(file_test)
    parser.parse(file,new XMLHandler())


  }

  @Test
  def test_new_xm_to_json_parser(): Unit = {


    var PRETTY_PRINT_INDENT_FACTOR = 4
    var path = Paths.get(pathSave + "0B47D949-FFA8-48BA-9D26-92F7D441CB66.xml")
    var TEST_XML_STRING_list = Files.readAllLines(path)
    var str:StringBuilder = StringBuilder.newBuilder

    TEST_XML_STRING_list.forEach(x=> {

      str.append(x)
    })

    var res:String = new String


    try {
      val xmlJSONObj = XML.toJSONObject(str.toString())
      res = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR)
      System.out.println(res)
    } catch {
      case je: JSONException =>
        System.out.println(je.toString)
    }


    saveFile(res, pathSave+"/"+"Esbjerg_kommune.json")

  }

//  @Test
//  def test_new_class(): Unit = {
//
//    val pathConfig:String = Resources.getResource("config").getPath
//    val pathDataStorage:String = Resources.getResource("database").getPath
//
//    val producercf = pathConfig + "/" + "producer.properties"
//    val topcf = pathConfig + "/" + "topics.properties"
//
//
//    val producer = new RunKafkaProducer(producercf,topcf,pathDataStorage)
//
//    val data:List[File] = producer.readData()
//    var res:String = null
//
//    data.foreach(x => {
//
//
//      res = producer.parserData(x.getAbsolutePath)
//
//    } )
//
//    assert(data.size>0)
//    assert(res!=null)
//
//  }

  def saveFile(data:String, fileName:String): Unit = {

    val file = new File(fileName)
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(data)
    bw.close()

  }


}

class XMLHandler extends DefaultHandler
{
  val id = "id"
  val parentid = "parentid"
  val key = "key"
  val name = "name"
  val tfid = "tfid"
  val typee = "type"
  val content = "content"
  val template = "template"

  var tfId_v:String = _
  var key_v:String = _
  var types_v:String = _
  var content_v:String = _

  var isContent:Boolean = false
  var seqJSON:Seq[JSONObject] = Seq[JSONObject]()
  var json:JSONObject = _
  var json1:JSONObject = _




  override def startElement(s: String, s1: String, qName: String, attributes: Attributes): Unit =
  {

    if (qName.equals("item"))
      {

        if (json!=null)
          {
            seqJSON = seqJSON :+ json
          }

        json = new JSONObject()
        val id_v =attributes.getValue(id)
        val parentId_v = attributes.getValue(parentid)
        val key_v = attributes.getValue(key)
        val template_v = attributes.getValue(template)
//        val name_v = attributes.getValue(name)

        json.put(id, id_v)
        json.put(parentid, parentId_v)
        json.put(key, key_v)
        json.put(template, template_v)
//        json.put(name, name_v)



      }
    if (qName.equals("field"))
      {

        tfId_v = attributes.getValue(tfid)
        key_v = attributes.getValue(key)
        types_v = attributes.getValue(typee)

        json1 = new JSONObject()
        json1.put(tfid, tfId_v)
        json1.put(key, key_v)
        json1.put(typee, types_v)

      }
    if (qName.equals("content"))
      {

        isContent=true

      }

  }

  override def characters(ch: Array[Char], start: Int, length: Int): Unit =
    {
      if (isContent)
        {
          var content_v = new String(ch, start, length)
          json1.put(content, content_v)
          json.put(key_v, json1)
          isContent= false
        }
    }

}
