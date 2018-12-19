package com.kwchina.oa.workflow.test;

import java.util.List;




public class DAOImplTestCase  extends BaseDAOTestCase {
	public DAOImplTestCase(String arg0) {
		super(arg0);
	}

	
	//��Java Application��ʽ����
	public static void main(String args[]){
		/*SubmitCategoryDAO categoryDAO = (SubmitCategoryDAO)factory.getBean("submitCategoryDAO");
		
		//dao operate	
		SubmitCategory category = new SubmitCategory();
		category.setCategoryName("����A");
		category.setCategoryType(SubmitCategory.Type_Normal);
		category.setLayer(1);
		category.setParent(null);
		categoryDAO.save(category);*/
		
		/**
		System.out.println(factory);
	
		String sql = "select top 1 accountNo  from WorkFlow_Apply_Fee applyFee where applyFee.accountNo is not null	order by substring(accountNo,7,3) desc";

		OrganizeDAO dao = (OrganizeDAO)factory.getBean("organizeDAO");	
		List ls =  dao.getResultBySQL(sql);
		String no = (String)ls.get(0);
		System.out.println(no);
		
		
		OrganizeDAO organizeDAO = (OrganizeDAO)factory.getBean("organizeDAO");		
		System.out.println(organizeDAO);		
		//OrganizeInfor organize = (OrganizeInfor)organizeDAO.get(new Integer(1));
		 * 
		 */
		
		/**
		JbpmDAO jDAO = (JbpmDAO)factory.getBean("jbpmDAO");
		System.out.println(jDAO);
		*/
		
		/**
		OrganizeInfor organize = new OrganizeInfor();
		organize.setOrganizeName("���̼���");
		organize.setShortName("���̲�");
		organize.setOrganizeNo("1111");
		organize.setLayer(1);
		organize.setLevelId(1);
		organizeDAO.save(organize);		
		*/
	}
	
	//��TestCase ��ʽ����
	public void testGet() {
		/**
		OrganizeDAO organizeDAO = (OrganizeDAO)factory.getBean("organizeDAO");		
		System.out.println(organizeDAO);
		
		OrganizeInfor organize = (OrganizeInfor)organizeDAO.get(new Integer(1));
		System.out.println(organize);
		
		System.out.print(organize.getOrganizeName());
		*/
		
		/**
		 * StructureInforDAO dao = (StructureInforDAO)factory.getBean("structureDAO");
		String sql = " from StructureInfor structure where structure.structrueId in (1,2,3)";
		List ls = dao.getResultByQueryString(sql);
		for(Iterator it = ls.iterator();it.hasNext();){
			StructureInfor structure = (StructureInfor)it.next();
			//System.out.println(structure.getStructrueName());
		}		
		
		AddressCategoryDAO addressCategoryDAO = (AddressCategoryDAO)factory.getBean("addressCategoryDAO");	
		System.out.println("1");
		System.out.println(addressCategoryDAO);
		
		
		AddressPersonDAO addressPersonDAO = (AddressPersonDAO)factory.getBean("addressPersonDAO");	
		System.out.println("2");
		System.out.println(addressPersonDAO);
		*/
	}

}
