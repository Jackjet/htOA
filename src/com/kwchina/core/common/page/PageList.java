package com.kwchina.core.common.page;

/**
 * <p>Title: HR Management System</p>
 * <p>Description: SPCWT-HR-SYSTEM</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: kwchina.com</p>
 *
 * @author zhou lb
 * @version 1.0
 *          Date: 2006-1-20
 *          Time: 11:06:05
 *          Class Name: PageList
 */


public class PageList {

    private String pageShowString;
    private java.util.List objectList;
    private Pages pages;

    public PageList() {
    }

    public String getPageShowString() {
        return pageShowString;
    }

    public void setPageShowString(String pageShowString) {
        this.pageShowString = pageShowString;
    }

    public java.util.List getObjectList() {
        return objectList;
    }

    public void setObjectList(java.util.List objectList) {
        this.objectList = objectList;
    }

    public Pages getPages() {
        return pages;
    }

    public void setPages(Pages pages) {
        this.pages = pages;
    }

}
