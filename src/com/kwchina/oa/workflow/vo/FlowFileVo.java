package com.kwchina.oa.workflow.vo;

/**
 * Create by yuanjl on 2018/3/7
 */
public class FlowFileVo {


    private int fid;
    private String departmentw;     //文件科室
    private String departmentl;     //流程科室
    private String flowName;        //流程名
    private String fileName;        //文件名
    private String fileNo;          //文件编号
    private String flowEdition;//流程版本号
    private String fileEdition;//文件版本号
    private String flowSup;//流程支持文件
    private String flowUpdatePo;//流程修改位置及原因
    private String fileUpdatePo;//文件修改位置及原因
    private String flowNew;//流程修改后内容
    private String fileNew;//文件修改后内容
    private String beAffl;//受影响的其他流程
    private String beAffw;//受影响的其他流程
    private String flowDe;//流程销毁原因
    private String fileDe;//文件销毁原因
    private String status;//状态
    private String type;//类型



    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getDepartmentw() {
        return departmentw;
    }

    public void setDepartmentw(String departmentw) {
        this.departmentw = departmentw;
    }

    public String getDepartmentl() {
        return departmentl;
    }

    public void setDepartmentl(String departmentl) {
        this.departmentl = departmentl;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getFlowEdition() {
        return flowEdition;
    }

    public void setFlowEdition(String flowEdition) {
        this.flowEdition = flowEdition;
    }

    public String getFileEdition() {
        return fileEdition;
    }

    public void setFileEdition(String fileEdition) {
        this.fileEdition = fileEdition;
    }

    public String getFlowSup() {
        return flowSup;
    }

    public void setFlowSup(String flowSup) {
        this.flowSup = flowSup;
    }

    public String getFlowUpdatePo() {
        return flowUpdatePo;
    }

    public void setFlowUpdatePo(String flowUpdatePo) {
        this.flowUpdatePo = flowUpdatePo;
    }

    public String getFileUpdatePo() {
        return fileUpdatePo;
    }

    public void setFileUpdatePo(String fileUpdatePo) {
        this.fileUpdatePo = fileUpdatePo;
    }

    public String getFlowNew() {
        return flowNew;
    }

    public void setFlowNew(String flowNew) {
        this.flowNew = flowNew;
    }

    public String getFileNew() {
        return fileNew;
    }

    public void setFileNew(String fileNew) {
        this.fileNew = fileNew;
    }

    public String getBeAffl() {
        return beAffl;
    }

    public void setBeAffl(String beAffl) {
        this.beAffl = beAffl;
    }

    public String getBeAffw() {
        return beAffw;
    }

    public void setBeAffw(String beAffw) {
        this.beAffw = beAffw;
    }

    public String getFlowDe() {
        return flowDe;
    }

    public void setFlowDe(String flowDe) {
        this.flowDe = flowDe;
    }

    public String getFileDe() {
        return fileDe;
    }

    public void setFileDe(String fileDe) {
        this.fileDe = fileDe;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
