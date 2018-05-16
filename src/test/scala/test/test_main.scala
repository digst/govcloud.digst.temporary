package test

import java.io.{BufferedWriter, File, FileWriter}
import java.nio.file.{Files, Paths}

import org.junit.{Before, Ignore, Test}
import com.google.common.io.Resources

import scala.io.Source
import javax.xml.parsers.{DocumentBuilderFactory, SAXParser, SAXParserFactory}

import app.parser.XMLHandler
import org.json.{JSONException, JSONObject, XML}
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

import scala.xml.{Elem, NodeSeq}


// MapR Maven Repo:
// SAX Parser: https://www.journaldev.com/1198/java-sax-parser-example

// todo: http://www.johnarutz.com/programming-resources/data-conversion-between-xml-and-json-w-scala/
// todo: kafka connect https://www.confluent.io/blog/simplest-useful-kafka-connect-data-pipeline-world-thereabouts-part-1/


class test_main {

  val path:String = "/home/datascience/GovcloudData/DIGST/Myndigheder_eksport.xml"
  val pathFiles:String = "/home/datascience/GovcloudData/files/"



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

            saveFile(parrentMessages, pathFiles   +"/"+fileID +".xml")
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

    val file_test = pathFiles + "0B47D949-FFA8-48BA-9D26-92F7D441CB66.xml"
    val parserFactory = SAXParserFactory.newInstance()
    parserFactory.setValidating(true)
    val parser = parserFactory.newSAXParser()
    val file = new File(file_test)

    val xMLHandler:XMLHandler = new XMLHandler()
    parser.parse(file, xMLHandler)



  }

  @Test
  def parse_single_file_new(): Unit = {


    val file_test = pathFiles + "67472A23-44BB-4BD6-AC7F-9AA288334EBB.xml"
    val parserFactory = SAXParserFactory.newInstance()
    parserFactory.setValidating(true)
    val parser = parserFactory.newSAXParser()
    val file = new File(file_test)

    val xMLHandler:XMLHandler = new XMLHandler()
    parser.parse(file, xMLHandler)

  }


  @Test
  def parse_big_file(): Unit =
  {

    val file_test =path
    val parserFactory = SAXParserFactory.newInstance()
    parserFactory.setValidating(true)
    val parser = parserFactory.newSAXParser()
    val file = new File(file_test)
    parser.parse(file,new XMLHandler())


  }

  @Test
  def test_new_xm_to_json_parser(): Unit = {


    var PRETTY_PRINT_INDENT_FACTOR = 4
    var path = Paths.get(pathFiles + "0B47D949-FFA8-48BA-9D26-92F7D441CB66.xml")
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


  }


  def saveFile(data:String, fileName:String): Unit = {

    val file = new File(fileName)
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(data)
    bw.close()

  }


}


