package test.customfields;

import java.io.File;
import java.util.Enumeration;

import freemarker.core.TemplateElement;
import freemarker.template.Configuration;
import freemarker.template.Template;

//获取自定义表单模板内的元素信息
public class TestFormTemplate {

	public static void main(String[] args) throws Exception {
		
		Configuration cfg = new Configuration();   
        cfg.setDirectoryForTemplateLoading(new File("D:/tomcat55/webapps/ROOT/workflow/templates"));   
        Template template = cfg.getTemplate("customizeForm.ftl");   
        
        //获取模板文件中的自定义标签
        TemplateElement element = template.getRootTreeNode();
        for(Enumeration children = element.children(); children.hasMoreElements();){  
            Object obj = children.nextElement();
            if("class freemarker.core.UnifiedCall".equals(obj.getClass().toString())){   
                System.out.println(obj.toString());
                int sIndex = obj.toString().indexOf("controlName=\"");
                int eIndex = obj.toString().lastIndexOf("\"/>");
                String fieldName = obj.toString().substring(sIndex+13, eIndex);
                System.out.println(fieldName);
                if (obj.toString().contains("<@input") || obj.toString().contains("<@textarea")) {
                	System.out.println("field type is String");
                }else if (obj.toString().contains("<@depSelect") || obj.toString().contains("<@perSelect")) {
                	System.out.println("field type is Integer");
                }
                System.out.println("======");
            }
        }

	}
}