package app;

import app.util.UploadFile;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.xml.sax.SAXException;
import scala.sys.Prop;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RunApp {

    private String producerConfig = null;
    private String topicConfig = null;
    private String webPort = null;
    private String dataBaseLocation = null;
    private String webAppDir = null;
    private String webConfigXML = null;

    public static void main(String[] args) throws LifecycleException, IOException, ParserConfigurationException, SAXException {

        if (args.length<1)
        {
            System.exit(-1);
        }

        String confDir = args[0];
        RunApp app = new RunApp();
        app.setup(confDir);
        app.run();

    }


    public void run() throws LifecycleException, IOException, ParserConfigurationException, SAXException {


        Properties producerProps = readProperties(producerConfig);
        Properties topicProps = readProperties(topicConfig);

        Tomcat tomcat = new Tomcat();

        tomcat.setPort(Integer.parseInt(webPort));
        tomcat.setBaseDir(webAppDir);

        Context ctx = tomcat.addContext("/", webAppDir);
        ctx.setAltDDName(webConfigXML);
        //ctx.addWelcomeFile("/home/datascience/GovCloudApplications/src/test/scala/WebApp/index.html");

        tomcat.addWebapp(tomcat.getHost(),"/app",webAppDir);
        tomcat.addServlet(ctx, "UpLoadFile", new UploadFile(dataBaseLocation, producerProps, topicProps));
        ctx.addServletMapping("/*", "UpLoadFile");

        tomcat.start();
        tomcat.getServer().await();
    }

    public void setup(String confDir) throws IOException {

        Properties applicationProps = readProperties(confDir);
        producerConfig = applicationProps.getProperty("producer.config");
        topicConfig = applicationProps.getProperty("topic.config");
        webPort = applicationProps.getProperty("port");
        dataBaseLocation = applicationProps.getProperty("tmp.files");
        webAppDir = applicationProps.getProperty("web.config");
        webConfigXML =applicationProps.getProperty("web.config.xml");

    }

    public Properties readProperties(String path) throws IOException {

        Properties props = new Properties();
        InputStream inStream = new FileInputStream(path);
        props.load(inStream);
        return props;


    }

}
