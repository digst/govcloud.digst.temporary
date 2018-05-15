package oldClasses.helper

import org.json.JSONObject
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

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
