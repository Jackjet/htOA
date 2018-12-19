package com.kwchina.core.common.page;

import javax.servlet.http.HttpServletRequest;

import com.kwchina.core.util.SysGeneralMethod;
import com.kwchina.oa.sys.SystemConstant;

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
 *          Class Name: Pages
 */


public class Pages {

    HttpServletRequest request = null;
    String filename = ""; //文件�?
    int page = 1; //页号
    int totals = -1; //记录总数
    int perpagenum = 20; //每页显示记录�?
    int style = 0; //分页字串样式
    int allpage = 1; //总页�?
    int cpage = 1; //当前�?
    int spage = 1; //�?始记录数
    String listPageBreak = "";
    String[] pagesign = null;
    
    private int totalPage;
    private int currPage;
    

    public int getCurrPage() {
		return this.page;
	}
	public int getTotalPage() {
		return this.allpage ;
	}

	public Pages() {
    }

    public Pages(HttpServletRequest request) {
        this.request = request;
        this.pagesign = SysGeneralMethod.getPagesign(request);
    }

    public Pages(HttpServletRequest request, int page, int totals, int perpagenum,
                 int style) {
        this.request = request;
        this.page = page;
        this.totals = totals;
        this.perpagenum = perpagenum;
        this.style = style;
        this.pagesign = SysGeneralMethod.getPagesign(request);
    }

    public Pages(HttpServletRequest request, int page, int totals, int perpagenum) {
        this.request = request;
        this.page = page;
        this.totals = totals;
        this.perpagenum = perpagenum;
        this.pagesign = SysGeneralMethod.getPagesign(request);
    }

    public Pages(HttpServletRequest request, int page, int perpagenum) {
        this.request = request;
        this.page = page;
        this.perpagenum = perpagenum;
        this.pagesign = SysGeneralMethod.getPagesign(request);
    }

    public String getFileName() {
        return this.filename;
    }

    public void setFileName(String aFileName) {
        this.filename = aFileName;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int aPage) {
        this.page = aPage;
    }

    public int getTotals() {
        return this.totals;
    }

    public void setTotals(int aTotals) {
        this.totals = aTotals;
    }

    public int getPerPageNum() {
        return this.perpagenum;
    }

    public void setPerPageNum(int aperpagenum) {
        this.perpagenum = aperpagenum;
    }

    public int getStyle() {
        return this.style;
    }

    public void setStyle(int aStyle) {
        this.style = aStyle;
    }

    public void setPagesign(String[] apagesign) {
        this.pagesign = apagesign;
    }

    public int getSpage() {
        return this.spage;
    }

    public void doPageBreak() {
        this.allpage = (int) Math.ceil((this.totals + this.perpagenum - 1) /
                this.perpagenum);
        int intPage = this.page;
        if (intPage > this.allpage) { // pages == 0
            this.cpage = 1;
        } else {
            this.cpage = intPage;
        }
        this.spage = (this.cpage - 1) * this.perpagenum;
        getPageBreakStr();
    }

    public String getListPageBreak() {
        return this.listPageBreak;
    }

    private void getPageBreakStr() {

        if (this.filename.indexOf("?") == -1 &&
                (this.filename.endsWith(".do") ||
                this.filename.endsWith(SystemConstant.FILEPREFIX))) {
            this.filename = this.filename + "?";
        } else {
            if (!this.filename.endsWith("&")) {
                this.filename = this.filename + "&";
            }
        }

        StringBuffer sb = new StringBuffer();

        if (this.style == 0) {
            if (this.cpage > 1) {
                sb.append("[<a href='");
                sb.append(this.filename);
                sb.append("page=1' >");
                sb.append(pagesign[0]);
                sb.append("</a>] [<a href='");
                sb.append(this.filename);
                sb.append("page=");
                sb.append((cpage - 1));
                //new
                sb.append("&t=");
                sb.append(this.totals);
                //new
                sb.append("'>");
                sb.append(pagesign[1]);
                sb.append("</a>] ");
                this.listPageBreak = sb.toString();
                /*
                         this.listPageBreak += "[<a href='" + this.filename +
                    "page=1' >" + pagesign[0] + "</a>] [<a href='" +
                    this.filename + "page=" +
                    (cpage - 1) + "'>" + pagesign[1] + "</a>] ";*/
            }
            if (this.cpage < this.allpage) {
                sb.append("[<a href='");
                sb.append(this.filename);
                sb.append("page=");
                sb.append((cpage + 1));
                sb.append("' >");
                sb.append(pagesign[2]);
                sb.append("</a>] [<a href='");
                sb.append(this.filename);
                sb.append("page=");
                sb.append(this.allpage);
                //new
                sb.append("&t=");
                sb.append(this.totals);
                //new

                sb.append("' >");
                sb.append(pagesign[3]);
                sb.append("</a>] ");
                this.listPageBreak = sb.toString();
                /*
                         this.listPageBreak += "[<a href='" + this.filename + "page=" +
                    (cpage + 1) + "' >" + pagesign[2] + "</a>] [<a href='" +
                    this.filename + "page=" + this.allpage +
                    "'' >" + pagesign[3] + "</a>] ";*/
            }
            return;
        }
        if (this.style == 1) {
            if (this.cpage > 1) {
                sb.append("[<a href='");
                sb.append(this.filename);
                sb.append("page=1");
                //new
                sb.append("&t=");
                sb.append(this.totals);
                //new
                sb.append("'>");
                sb.append(pagesign[0]);
                sb.append("</a>] [<a href='");
                sb.append(this.filename);
                sb.append("page=");
                sb.append((cpage - 1));
                //new
                sb.append("&t=");
                sb.append(this.totals);
                //new
                sb.append("'>");
                sb.append(pagesign[1]);
                sb.append("</a>] ");
                /*
                         this.listPageBreak += "[<a href='" + this.filename +
                    "page=1' >" + pagesign[0] + "</a>] [<a href='" +
                    this.filename + "page=" +
                    (cpage - 1) + "'>" + pagesign[1] + "</a>] ";*/
            }
            if (this.cpage < this.allpage) {
                sb.append("[<a href='");
                sb.append(this.filename);
                sb.append("page=");
                sb.append((cpage + 1));
                //new
                sb.append("&t=");
                sb.append(this.totals);
                //new
                sb.append("'>");
                sb.append(pagesign[2]);
                sb.append("</a>] [<a href='");
                sb.append(this.filename);
                sb.append("page=");
                sb.append(this.allpage);
                //new
                sb.append("&t=");
                sb.append(this.totals);
                //new

                sb.append("'>");
                sb.append(pagesign[3]);
                sb.append("</a>] ");
                /*
                         this.listPageBreak += "[<a href='" + this.filename + "page=" +
                    (cpage + 1) + "' >" + pagesign[2] + "</a>] [<a href='" +
                    this.filename + "page=" + this.allpage +
                    "'' >" + pagesign[3] + "</a>] ";*/
            }
            int _cpage = 0;
            if (this.allpage == 0) {
                _cpage = 0;
            } else {
                _cpage = cpage;
            }
            /*sb.append(SystemConstant.MESSAGE.getMessage(this.request.getLocale(),
                    "bbscs.pages.per",
                    String.valueOf(this.totals),
                    String.valueOf(_cpage),
                    String.valueOf(this.allpage)));*/
            sb.append(SysGeneralMethod.getPagesign());
            /*
                  this.listPageBreak +=
                      Constant.MESSAGE.getMessage(this.request.getLocale(),
                                                  "bbscs.pages.str",
                                                  String.valueOf(this.totals),
                                                  String.valueOf(_cpage),
                                                  String.valueOf(this.allpage));*/
            this.listPageBreak = sb.toString();
            return;
        }
        if (this.style == 2) {
            if (this.cpage > 1) {
                sb.append("[<a href='");
                sb.append(this.filename);
                sb.append("inpages=1");
                //new
                sb.append("&t=");
                sb.append(this.totals);
                //new
                sb.append("'>");
                sb.append(pagesign[0]);
                sb.append("</a>] [<a href='");
                sb.append(this.filename);
                sb.append("inpages=");
                sb.append((cpage - 1));
                //new
                sb.append("&t=");
                sb.append(this.totals);
                //new
                sb.append("'>");
                sb.append(pagesign[1]);
                sb.append("</a>] ");
                /*
                         this.listPageBreak += "[<a href='" + this.filename +
                    "inpages=1' >" + pagesign[0] + "</a>] [<a href='" +
                    this.filename + "inpages=" +
                    (cpage - 1) + "'>" + pagesign[1] + "</a>] ";*/
            }
            if (this.cpage < this.allpage) {
                sb.append("[<a href='");
                sb.append(this.filename);
                sb.append("inpages=");
                sb.append((cpage + 1));
                //new
                sb.append("&t=");
                sb.append(this.totals);
                //new
                sb.append("'>");
                sb.append(pagesign[2]);
                sb.append("</a>] [<a href='");
                sb.append(this.filename);
                sb.append("inpages=");
                sb.append(this.allpage);
                //new
                sb.append("&t=");
                sb.append(this.totals);
                //new
                sb.append("'>");
                sb.append(pagesign[3]);
                sb.append("</a>] ");
                /*
                 this.listPageBreak += "[<a href='" + this.filename + "inpages=" +
                    (cpage + 1) + "' >" + pagesign[2] + "</a>] [<a href='" +
                    this.filename + "inpages=" + this.allpage +
                    "'' >" + pagesign[3] + "</a>] ";*/
            }
            int _cpage = 0;
            if (this.allpage == 0) {
                _cpage = 0;
            } else {
                _cpage = cpage;
            }
            /*sb.append(SystemConstant.MESSAGE.getMessage(this.request.getLocale(),
                    "bbscs.pages.per",
                    String.valueOf(this.totals),
                    String.valueOf(_cpage),
                    String.valueOf(this.allpage)));*/
            sb.append(SysGeneralMethod.getPagesign());
            this.listPageBreak = sb.toString();
            /*
                   this.listPageBreak +=
                Constant.MESSAGE.getMessage(this.request.getLocale(),
                                            "bbscs.pages.str",
                                            String.valueOf(this.totals),
                                            String.valueOf(_cpage),
                                            String.valueOf(this.allpage));*/
            return;
        }

        if (this.style == 3) {
            String postto;
            if (this.filename != null && this.filename.length() > 0) {

                sb.append("<table width=\"100%\"  border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n");
                //filename = filename.toLowerCase();                
                int index = filename.indexOf("?");
                if (index != -1) {
                    postto = filename.substring(0, index);
                    filename = filename.substring(index + 1, filename.length());
                } else {
                    postto = filename;
                    filename = "";
                }
                                
                sb.append("<FORM METHOD=POST ACTION=\"" + postto + "\">\n");
                sb.append("<tr><td><div align=\"right\">\n");
                //System.out.println(filename);
                if (filename != null && filename.length() > 0) {
                    String[] ss = filename.split("&");
                    if (ss != null) {
                        //System.out.println(ss.length);
                        for (int i = 0; i < ss.length; i++) {
                            String[] p = ss[i].split("=");
                            if (p != null && p.length == 2) {
                            	if (!p[0].equals("page")){
                                sb.append("<INPUT TYPE=\"hidden\" name=\"" + p[0] +
                                        "\" value=\"" +
                                        p[1] + "\">\n");
                            	}

                            }
                        }
                    }
                }

                if (filename != null && filename.length() > 0) {
                    filename = postto + "?" + filename;
                } else {
                    filename = postto;
                }

                if (this.cpage > 1) {
                    sb.append("[<a href='");
                    sb.append(this.filename);
                    sb.append("page=1");
                    //new
                    sb.append("&t=");
                    sb.append(this.totals);
                    //new
                    sb.append("'>");
                    sb.append(pagesign[0]);
                    sb.append("</a>] [<a href='");
                    sb.append(this.filename);
                    sb.append("inpages=");
                    sb.append((cpage - 1));
                    //new
                    sb.append("&t=");
                    sb.append(this.totals);
                    //new
                    sb.append("'>");
                    sb.append(pagesign[1]);
                    sb.append("</a>] ");

                }
                if (this.cpage < this.allpage) {
                    sb.append("[<a href='");
                    sb.append(this.filename);
                    sb.append("inpages=");
                    sb.append((cpage + 1));
                    //new
                    sb.append("&t=");
                    sb.append(this.totals);
                    //new
                    sb.append("'>");
                    sb.append(pagesign[2]);
                    sb.append("</a>] [<a href='");
                    sb.append(this.filename);
                    sb.append("inpages=");
                    sb.append(this.allpage);
                    //new
                    sb.append("&t=");
                    sb.append(this.totals);
                    //new
                    sb.append("'>");
                    sb.append(pagesign[3]);
                    sb.append("</a>] ");

                }
                int _cpage = 0;
                if (this.allpage == 0) {
                    _cpage = 0;
                } else {
                    _cpage = cpage;
                }
                /*sb.append(SystemConstant.MESSAGE.getMessage(this.request.getLocale(),
                        "bbscs.pages.str",
                        String.valueOf(this.totals),
                        String.valueOf(_cpage),
                        String.valueOf(this.allpage)));*/
                //sb.append(SysGeneralMethod.getPagesign());
                sb.append(" ");
                /*sb.append(SystemConstant.MESSAGE.getMessage(this.request.getLocale(),
                        "bbscs.pages.post"));*/
                //sb.append(SysGeneralMethod.getPagesign());
                sb.append("</div></td></tr>\n");
                sb.append("</FORM>\n");
                sb.append("</table>\n");

                this.listPageBreak = sb.toString();
            }
            return;
        }
        
        if (this.style == 4) {
            if (this.cpage > 1) {
                sb.append("[<a href='");
                sb.append(this.filename);
                sb.append("inpages=1");
                //new
                sb.append("&t=");
                sb.append(this.totals);
                //new
                sb.append("'>");
                sb.append(pagesign[0]);
                sb.append("</a>] [<a href='");
                sb.append(this.filename);
                sb.append("inpages=");
                sb.append((cpage - 1));
                //new
                sb.append("&t=");
                sb.append(this.totals);
                //new
                sb.append("'>");
                sb.append(pagesign[1]);
                sb.append("</a>] ");
                /*
                         this.listPageBreak += "[<a href='" + this.filename +
                    "inpages=1' >" + pagesign[0] + "</a>] [<a href='" +
                    this.filename + "inpages=" +
                    (cpage - 1) + "'>" + pagesign[1] + "</a>] ";*/
            }
            if (this.cpage < this.allpage) {
                sb.append("[<a href='");
                sb.append(this.filename);
                sb.append("inpages=");
                sb.append((cpage + 1));
                //new
                sb.append("&t=");
                sb.append(this.totals);
                //new
                sb.append("'>");
                sb.append(pagesign[2]);
                sb.append("</a>] [<a href='");
                sb.append(this.filename);
                sb.append("inpages=");
                sb.append(this.allpage);
                //new
                sb.append("&t=");
                sb.append(this.totals);
                //new
                sb.append("'>");
                sb.append(pagesign[3]);
                sb.append("</a>] ");
                /*
                 this.listPageBreak += "[<a href='" + this.filename + "inpages=" +
                    (cpage + 1) + "' >" + pagesign[2] + "</a>] [<a href='" +
                    this.filename + "inpages=" + this.allpage +
                    "'' >" + pagesign[3] + "</a>] ";*/
            }
            int _cpage = 0;
            if (this.allpage == 0) {
                _cpage = 0;
            } else {
                _cpage = cpage;
            }
            /*sb.append(SystemConstant.MESSAGE.getMessage(this.request.getLocale(),
                    "bbscs.pages.str",
                    String.valueOf(this.totals),
                    String.valueOf(_cpage),
                    String.valueOf(this.allpage)));*/
            sb.append(SysGeneralMethod.getPagesign());
            this.listPageBreak = sb.toString();
            /*
                   this.listPageBreak +=
                Constant.MESSAGE.getMessage(this.request.getLocale(),
                                            "bbscs.pages.str",
                                            String.valueOf(this.totals),
                                            String.valueOf(_cpage),
                                            String.valueOf(this.allpage));*/
            return;
        }

    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

}
