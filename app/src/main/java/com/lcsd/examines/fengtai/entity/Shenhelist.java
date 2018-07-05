package com.lcsd.examines.fengtai.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/22.
 */
public class Shenhelist {
    public static class TRslist{

        private	String	id;	/*3725*/
        private	String	title;	/*撒上水水水水水水水水水*/
        private	String	status;	/*0*/
        private	String	cate_id;	/*637*/
        private	String	dateline;	/*1448349041*/
        private	String	thumb;	/**/
        private	String	url;	/*http://www.ahft.tv/check/index.php?c=content&id=3725*/
        private	String	note;	/**/
        private	String	cate_name;	/*今日推荐*/

        public void setId(String value){
            this.id = value;
        }
        public String getId(){
            return this.id;
        }

        public void setTitle(String value){
            this.title = value;
        }
        public String getTitle(){
            return this.title;
        }

        public void setStatus(String value){
            this.status = value;
        }
        public String getStatus(){
            return this.status;
        }

        public void setCate_id(String value){
            this.cate_id = value;
        }
        public String getCate_id(){
            return this.cate_id;
        }

        public void setDateline(String value){
            this.dateline = value;
        }
        public String getDateline(){
            return this.dateline;
        }

        public void setThumb(String value){
            this.thumb = value;
        }
        public String getThumb(){
            return this.thumb;
        }

        public void setUrl(String value){
            this.url = value;
        }
        public String getUrl(){
            return this.url;
        }

        public void setNote(String value){
            this.note = value;
        }
        public String getNote(){
            return this.note;
        }

        public void setCate_name(String value){
            this.cate_name = value;
        }
        public String getCate_name(){
            return this.cate_name;
        }

    }
    private	List<TRslist>	rslist;	/*List<TRslist>*/
    public void setRslist(List<TRslist> value){
        this.rslist = value;
    }
    public List<TRslist> getRslist(){
        return this.rslist;
    }

    private	Integer	total_page;	/*1*/
    private	String	psize;	/*10*/
    private	String	pageid;	/*1*/

    public void setTotal_page(Integer value){
        this.total_page = value;
    }
    public Integer getTotal_page(){
        return this.total_page;
    }

    public void setPsize(String value){
        this.psize = value;
    }
    public String getPsize(){
        return this.psize;
    }

    public void setPageid(String value){
        this.pageid = value;
    }
    public String getPageid(){
        return this.pageid;
    }
}
