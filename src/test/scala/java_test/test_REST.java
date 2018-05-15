package java_test;

import app.RunApp;
import com.google.common.io.Resources;
import org.apache.catalina.LifecycleException;
import org.junit.Test;

import javax.servlet.ServletException;
import java.io.IOException;
import java.net.MalformedURLException;

public class test_REST {

    @Test
    public void test_web() throws ServletException, LifecycleException, IOException {
        String[] args = new String[1];
        args[0] = Resources.getResource("config").getPath()+"/"+"application.properties";
        RunApp.main(args);

    }


}
