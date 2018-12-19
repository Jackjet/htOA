package test;

import java.util.ArrayList;

import javax.annotation.Resource;

import junit.framework.TestCase;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kwchina.core.base.dao.OrganizeInforDAO;
import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.sys.CoreConstant;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2009-8-18
 * Time: 10:37:06
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"config/applicationContext.xml"})
public class UserGroupTest extends TestCase{
    @Resource
    private OrganizeInforDAO organizeInforDAO;
    
	@Autowired
	private OrganizeManager organizeManager;
	
	@Autowired
	private SystemUserManager systemUserManager;

    @org.junit.Test
    public void addTest(){
    	/*OrganizeInfor organize = new OrganizeInfor();
    	organize.setOrganizeName("cc");
    	organize.setDeleted(false);
    	organize.setLayer(0);
    	organize.setLevelId(1);
    	organize.setOrderNo(1);
    	organizeInforDAO.save(organize);*/
    	/*OrganizeInfor organizeTmp = organizeInforDAO.get(1);
    	System.out.println("----修改前："+organizeTmp.getOrganizeName()+"----");
    	organizeTmp.setOrganizeName("bb");
    	organizeInforDAO.save(organizeTmp);
    	System.out.println("----修改后："+organizeTmp.getOrganizeName()+"----");*/
    	//OrganizeInfor organizeTmp = organizeInforDAO.get(1);
    	//organizeInforDAO.remove(organizeTmp);
    	//ArrayList returnArray = organizeManager.getOrganizeAsTree(CoreConstant.Organize_Begin_Id);
    	try {
			this.systemUserManager.checkUser("admin", "333333");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
