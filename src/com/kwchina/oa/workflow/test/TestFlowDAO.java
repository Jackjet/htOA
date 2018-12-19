package com.kwchina.oa.workflow.test;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.kwchina.core.base.dao.SystemUserInforDAO;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.oa.workflow.dao.FlowDefinitionDAO;
import com.kwchina.oa.workflow.entity.FlowDefinition;
import com.kwchina.oa.workflow.service.FlowDefinitionManager;

//@ContextConfiguration(locations={"classpath:/config/applicationContext.xml"})
@ContextConfiguration
public class TestFlowDAO extends AbstractTransactionalJUnit4SpringContextTests {
    //@Autowired
    //private FlowDefinitionManager userService;
	
	@Autowired
	private FlowDefinitionManager flowDefinitionManager;
   
    @Autowired
    private FlowDefinitionDAO flowDefinitionDAO;
   
    @Autowired
    private SystemUserInforDAO systemUserInforDAO;
   
    /**
    @Before �� ׼��
    public void prepareTestData() {
        
    }*/
   
    @Ignore("如果想忽略掉某个测试,则加此注解")    
    @Test
    public void flowDefineDAOSave(){
    	FlowDefinition flowDefine  = new FlowDefinition();
    	flowDefine.setFlowName("AAA");
    	flowDefine.setStatus(1);
    	flowDefine.setFlowType(0);
    	
    	flowDefine.setTransType(0);
    	flowDefine.setTemplate("");
    	flowDefine.setValid(true);
    	
    	SystemUserInfor charger = this.systemUserInforDAO.get(1);
    	flowDefine.setCharger(charger);
    	
    	this.flowDefinitionDAO.save(flowDefine);    
    	
    	
    	//读取保存的数据
    	List ls = this.flowDefinitionDAO.getAll();
    	System.out.print("---------");
    	System.out.print(ls.size());
    	System.out.print("---------");
    }
    
    
    @Test
    public void flowDefineDAOGet(){    	
//    	FlowDefinition flowDefine = this.flowDefinitionDAO.get(4);
    	
    	//读取数据
    	System.out.print("---------");
    	int a = Integer.valueOf("111");
    	int b = Integer.valueOf(null);
//    	System.out.print(flowDefine.getFlowName());
    	System.out.print("---------");
    }
}
 

