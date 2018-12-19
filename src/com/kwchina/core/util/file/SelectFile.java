package com.kwchina.core.util.file;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFilenameFilter;

public class SelectFile implements SmbFilenameFilter {
	public SelectFile(){}
	/**
	    * accept
	    *
	    * @param smbFile SmbFile
	    * @param string String
	    * @return boolean
	    */
	   
	   //该方法实现要读取文件的过滤，具体原因不详
	//&& name.endsWith(".txt")||name.startsWith("分钟数据")
	   
	   public boolean accept(SmbFile dir, String name) {
	    if(name!=null)
	      return true;
	    else{
	      return false;
	    }
	   }
}
