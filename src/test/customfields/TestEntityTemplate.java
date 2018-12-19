package test.customfields;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

//测试根据实体模板生成hbm文件
public class TestEntityTemplate {

	public static void main(String[] args) throws Exception {
		
		Configuration cfg=new Configuration();   
        cfg.setDirectoryForTemplateLoading(new File("D:/tomcat55/webapps/ROOT/workflow/templates"));   
        Template tem=cfg.getTemplate("entityTemplate.ftl");   
           
        OutputStreamWriter out = new OutputStreamWriter(System.out);   
        Map data=new HashMap();   
        data.put("_TableName","CusEntity1");   
        StringWriter writer=new StringWriter();   
        tem.process(data,writer);   
        String content=writer.toString();   
        writer.close();   
        String filePath="D:/tomcat55/webapps/ROOT/WEB-INF/classes/com/kwchina/oa/workflow/customfields/domain/CusEntity1.hbm.xml";   
        File fileXml=new File(filePath);   
        if(!fileXml.exists()){
            fileXml.createNewFile();   
        }   
        OutputStreamWriter osWriter=new OutputStreamWriter(new FileOutputStream(fileXml), Charset.forName("utf-8"));        
        osWriter.write(content);   
        osWriter.close();   
        out.close(); 
	}
}