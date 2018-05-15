package test

import com.google.common.io.Resources
import oldClasses.RunAppTemp
import org.junit.{Before, Test}

class test_main_producer_app {

  val pathConfig:String = Resources.getResource("config").getPath
  val pathDataStorage:String = Resources.getResource("database").getPath
  @Test
  def test_APP_Producer(): Unit = {

    val args:Array[String] = new Array[String](3)
    args(0) = pathConfig + "/" + "producer.properties"
    args(1) = pathConfig + "/" + "topics.properties"
    args(2) = pathDataStorage
    RunAppTemp.main(args)

  }


}
