package app.util;

import app.kafka.RunKafkaProducer;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

@WebServlet("/app/UploadDownloadFileServlet")
public class UploadFile extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ServletFileUpload uploader = null;
    private String pathToData = null;
    private RunKafkaProducer runKafkaProducer = null;

    public UploadFile()
    {

    }

    public UploadFile(String pathToData, Properties propertiesProducerConfig, Properties propertiesTopicConfig)
    {
        this.pathToData = pathToData;
        this.runKafkaProducer = new RunKafkaProducer(propertiesProducerConfig,propertiesTopicConfig, pathToData);
    }



    @Override
    public void init() throws ServletException{
        DiskFileItemFactory fileFactory = new DiskFileItemFactory();
//        File filesDir = (File) getServletContext().getAttribute("FILES_DIR_FILE");
        File filesDir = new File(pathToData);
        fileFactory.setRepository(filesDir);
        this.uploader = new ServletFileUpload(fileFactory);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(!ServletFileUpload.isMultipartContent(request)){
            throw new ServletException("Content type is not multipart/form-data");
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.write("<html><head></head><body>");
        try {
            List<FileItem> fileItemsList = uploader.parseRequest(request);
            Iterator<FileItem> fileItemsIterator = fileItemsList.iterator();
            while(fileItemsIterator.hasNext()){
                FileItem fileItem = fileItemsIterator.next();
                System.out.println("FieldName="+fileItem.getFieldName());
                System.out.println("FileName="+fileItem.getName());
                System.out.println("ContentType="+fileItem.getContentType());
                System.out.println("Size in bytes="+fileItem.getSize());

                //String path = String.valueOf(request.getServletContext().getAttribute("FILES_DIR_FILE"));

                File file = new File(pathToData+File.separator+fileItem.getName());
                System.out.println("Absolute Path at server="+file.getAbsolutePath());
                fileItem.write(file);


                out.write("File "+fileItem.getName().toUpperCase()+ " uploaded successfully.");
                out.write("<br>");
                //out.write("<a href=\"UploadDownloadFileServlet?fileName="+fileItem.getName()+"\">Download "+fileItem.getName()+"</a>");

                this.runKafkaProducer.produceData();

            }
        } catch (FileUploadException e) {
            out.write("Exception in uploading file.");
        } catch (Exception e) {
            out.write("Exception in uploading file.");
        } finally {

            out.write("</body></html>");

            FileUtils.cleanDirectory(new File(pathToData));


        }
    }






}