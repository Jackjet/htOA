package com.kwchina.core.cms.vo;


public class InforDocumentVo{
	
    private Integer inforId;
    private String inforTitle;		//信息主题
    private String issueUnit;		//发布单位
    private boolean homepage;		//是否需要首页显示:0-不需要;1-需要.
    private String topp;		//是否需要首页显示:0-不需要;1-需要.
    private String startDateStr;			//首页开始时间
    private String endDateStr;			//首页结束时间
    private boolean important;		//是否重要:0-普通;1-重要.
    private String keyword;			//关键字
    private String inforContent;	//信息内容
    private String attachment;		//附件路径
    private String createTimeStr;		//发布时间
    private String inforTimeStr;			//信息时间
    private String relateUrl;		//相关链接
    private int hits;				//点击数
    private String source;			//信息来源
    private String defaultPicUrl;	//图片路径
    private String htmlFilePath;	//html文件保存路径
    private String defStr1;			//自定义字符1
    private String defStr2;			//自定义字符2
    private String defStr3;			//自定义字符3
    private String defDate1Str;			//自定义时间1
    private String defDate2Str;			//自定义时间2
    private boolean defBool1;		//自定义Boolean型
    private double defDec1D;		//自定义Decimal型
   
    private Integer categoryId;	//所属分类
    
  //静态字段，只用于“荣誉室”
    private String kind;         //所属类别
    private String honorKind;           //荣誉类别


    private boolean handOut;		//是否与总/分公司互通:0-不需要;1-需要.
    private boolean received;       //是否是接收到的
	public Integer getInforId() {
		return inforId;
	}
	public void setInforId(Integer inforId) {
		this.inforId = inforId;
	}
	public String getInforTitle() {
		return inforTitle;
	}
	public void setInforTitle(String inforTitle) {
		this.inforTitle = inforTitle;
	}
	public String getIssueUnit() {
		return issueUnit;
	}
	public void setIssueUnit(String issueUnit) {
		this.issueUnit = issueUnit;
	}
	public boolean isHomepage() {
		return homepage;
	}
	public void setHomepage(boolean homepage) {
		this.homepage = homepage;
	}
	public String getStartDateStr() {
		return startDateStr;
	}
	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}
	public String getEndDateStr() {
		return endDateStr;
	}
	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}
	public boolean isImportant() {
		return important;
	}
	public void setImportant(boolean important) {
		this.important = important;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getInforContent() {
		return inforContent;
	}
	public void setInforContent(String inforContent) {
		this.inforContent = inforContent;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	public String getInforTimeStr() {
		return inforTimeStr;
	}
	public void setInforTimeStr(String inforTimeStr) {
		this.inforTimeStr = inforTimeStr;
	}
	public String getRelateUrl() {
		return relateUrl;
	}
	public void setRelateUrl(String relateUrl) {
		this.relateUrl = relateUrl;
	}
	public int getHits() {
		return hits;
	}
	public void setHits(int hits) {
		this.hits = hits;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDefaultPicUrl() {
		return defaultPicUrl;
	}
	public void setDefaultPicUrl(String defaultPicUrl) {
		this.defaultPicUrl = defaultPicUrl;
	}
	public String getHtmlFilePath() {
		return htmlFilePath;
	}
	public void setHtmlFilePath(String htmlFilePath) {
		this.htmlFilePath = htmlFilePath;
	}
	public String getDefStr1() {
		return defStr1;
	}
	public void setDefStr1(String defStr1) {
		this.defStr1 = defStr1;
	}
	public String getDefStr2() {
		return defStr2;
	}
	public void setDefStr2(String defStr2) {
		this.defStr2 = defStr2;
	}
	public String getDefStr3() {
		return defStr3;
	}
	public void setDefStr3(String defStr3) {
		this.defStr3 = defStr3;
	}
	public String getDefDate1Str() {
		return defDate1Str;
	}
	public void setDefDate1Str(String defDate1Str) {
		this.defDate1Str = defDate1Str;
	}
	public String getDefDate2Str() {
		return defDate2Str;
	}
	public void setDefDate2Str(String defDate2Str) {
		this.defDate2Str = defDate2Str;
	}
	public boolean isDefBool1() {
		return defBool1;
	}
	public void setDefBool1(boolean defBool1) {
		this.defBool1 = defBool1;
	}
	public double getDefDec1D() {
		return defDec1D;
	}
	public void setDefDec1D(double defDec1D) {
		this.defDec1D = defDec1D;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public boolean isHandOut() {
		return handOut;
	}
	public void setHandOut(boolean handOut) {
		this.handOut = handOut;
	}
	public boolean isReceived() {
		return received;
	}
	public void setReceived(boolean received) {
		this.received = received;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getHonorKind() {
		return honorKind;
	}
	public void setHonorKind(String honorKind) {
		this.honorKind = honorKind;
	}

	public String getTopp() {
		return topp;
	}

	public void setTopp(String topp) {
		this.topp = topp;
	}
}


