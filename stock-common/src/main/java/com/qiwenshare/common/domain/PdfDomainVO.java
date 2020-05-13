package com.qiwenshare.common.domain;

import java.util.Date;

public class PdfDomainVO {
    //实体类的名称:pdfDomainVO

    private Integer id;//id

    private Date time;//操作时间

    private String filename;//文件名

    private String filesize;//文件大小

    private String filetype;//文件类型

    private String details;//操作详情

    private String content;//pdf中内容

    private String outputfile;//输出路径(保存路径)

    private String inputfile;//要操作的pdf路径

    private String strtofind;//需要替换的文本

    private String message;//替换的文本

    private String imagefile;//图片路径

    private String imagelist;//图片集合

    private Integer pageno;//指定页码

    private Integer pages;//总页数

    private Integer rid;//...

    private Integer pageoperation;//操作页数

    private Integer pagestart;//开始页

    private Integer pageend;//结束页

    private String position;//位置:X,Y

    private String fileSizeAfter;//操作后文件大小

    private Integer status;//状态

    private Integer afterPages;//操作后页码

    private Integer imgSize;//图片大小

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOutputfile() {
        return outputfile;
    }

    public void setOutputfile(String outputfile) {
        this.outputfile = outputfile;
    }

    public String getInputfile() {
        return inputfile;
    }

    public void setInputfile(String inputfile) {
        this.inputfile = inputfile;
    }

    public String getStrtofind() {
        return strtofind;
    }

    public void setStrtofind(String strtofind) {
        this.strtofind = strtofind;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImagefile() {
        return imagefile;
    }

    public void setImagefile(String imagefile) {
        this.imagefile = imagefile;
    }

    public String getImagelist() {
        return imagelist;
    }

    public void setImagelist(String imagelist) {
        this.imagelist = imagelist;
    }

    public Integer getPageno() {
        return pageno;
    }

    public void setPageno(Integer pageno) {
        this.pageno = pageno;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getPageoperation() {
        return pageoperation;
    }

    public void setPageoperation(Integer pageoperation) {
        this.pageoperation = pageoperation;
    }

    public Integer getPagestart() {
        return pagestart;
    }

    public void setPagestart(Integer pagestart) {
        this.pagestart = pagestart;
    }

    public Integer getPageend() {
        return pageend;
    }

    public void setPageend(Integer pageend) {
        this.pageend = pageend;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getFileSizeAfter() {
        return fileSizeAfter;
    }

    public void setFileSizeAfter(String fileSizeAfter) {
        this.fileSizeAfter = fileSizeAfter;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAfterPages() {
        return afterPages;
    }

    public void setAfterPages(Integer afterPages) {
        this.afterPages = afterPages;
    }

    public Integer getImgSize() {
        return imgSize;
    }

    public void setImgSize(Integer imgSize) {
        this.imgSize = imgSize;
    }
}
