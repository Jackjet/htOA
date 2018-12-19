package com.kwchina.oa.workflow;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

public class FlowConnectionManager {

	private static FlowConnectionManager instance;

	public ComboPooledDataSource ds;
//	private static String c3p0B1Properties = "c3p0.b1.properties";
//	private static String c3p0B2Properties = "c3p0.b2.properties";

	private FlowConnectionManager(String sourcePath) throws Exception {
		Properties p = new Properties();
		p.load(this.getClass().getResourceAsStream(sourcePath));
			
		ds = new ComboPooledDataSource();
		//ds.setProperties(p); 设置不成功
		ds.setUser(p.getProperty("c3p0.user"));
		ds.setPassword(p.getProperty("c3p0.password"));
		ds.setJdbcUrl(p.getProperty("c3p0.jdbcUrl"));
		ds.setDriverClass(p.getProperty("c3p0.driverClass"));
		ds.setInitialPoolSize(Integer.parseInt(p.getProperty("c3p0.initialPoolSize")));
		ds.setMinPoolSize(Integer.parseInt(p.getProperty("c3p0.minPoolSize")));
		ds.setMaxPoolSize(Integer.parseInt(p.getProperty("c3p0.maxPoolSize")));
		ds.setMaxStatements(Integer.parseInt(p.getProperty("c3p0.maxStatements")));
		ds.setMaxIdleTime(Integer.parseInt(p.getProperty("c3p0.maxIdleTime")));
	}

	public static final FlowConnectionManager getInstance(String sourcePath) {
		if (instance == null) {
			try {
				instance = new FlowConnectionManager(sourcePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	public synchronized final Connection getConnection() {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void finalize() throws Throwable {
		// 关闭datasource
		DataSources.destroy(ds); 
		super.finalize();
	}
	
	/*Statement stmt = null;
	ResultSet rs = null;
	Connection conn = null;
	
	try{
		conn=FlowConnectionManager.getInstance(sourcePath).getConnection();
		stmt = conn.createStatement();
		String sql = "select d.PROCESSDEFINITIONID,d.NAME from WFPROCESSDEFINITION d left join Core_VirtualResource v ON d.name = v.resourceName where d.PROCESSDEFINITIONID like 'process%' and v.parentId = "+parentId+" and v.hided=0 order by d.NAME";
		rs = stmt.executeQuery(sql);
		
		while(rs.next()){
			Map<String, String> map = new HashMap<String, String>();
			String processDefinitionId = rs.getString("PROCESSDEFINITIONID");
			String flowName = rs.getString("NAME");
			map.put("PROCESSDEFINITIONID", processDefinitionId);
			map.put("NAME", flowName);
			list.add(map);
		}
		rs.close();
	}catch(Exception ex){
		
	}finally{
		if(!conn.isClosed())
			conn.close();
	}	*/
}
