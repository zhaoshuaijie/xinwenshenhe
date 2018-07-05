package com.lcsd.examines.fengtai.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/24.
 */
public class RecordList {

    public static class TRslist{

        private	String	id;	/*18*/
        private	String	audio;	/*http://www.ahft.tv/res/status/2017/11/24/9b1eb55f75ae925f.mp3*/
        private	String	admin;	/*test*/
        private	String	dateline;	/*1511507773*/
        private	String	note;	/*mmmmmnnnnnbbvvvvvvvvfgg*/
        private boolean isPlaying;//是否正在播放

        public void setId(String value){
            this.id = value;
        }
        public String getId(){
            return this.id;
        }

        public void setAudio(String value){
            this.audio = value;
        }
        public String getAudio(){
            return this.audio;
        }

        public void setAdmin(String value){
            this.admin = value;
        }
        public String getAdmin(){
            return this.admin;
        }

        public void setDateline(String value){
            this.dateline = value;
        }
        public String getDateline(){
            return this.dateline;
        }

        public void setNote(String value){
            this.note = value;
        }
        public String getNote(){
            return this.note;
        }

        public boolean isPlaying() {
            return isPlaying;
        }

        public void setPlaying(boolean playing) {
            isPlaying = playing;
        }
    }
    private List<TRslist> rslist;	/*List<TRslist>*/
    public void setRslist(List<TRslist> value){
        this.rslist = value;
    }
    public List<TRslist> getRslist(){
        return this.rslist;
    }

    private	Integer	total_page;	/*2*/
    private	Integer	psize;	/*10*/
    private	Integer	pageid;	/*1*/

    public void setTotal_page(Integer value){
        this.total_page = value;
    }
    public Integer getTotal_page(){
        return this.total_page;
    }

    public void setPsize(Integer value){
        this.psize = value;
    }
    public Integer getPsize(){
        return this.psize;
    }

    public void setPageid(Integer value){
        this.pageid = value;
    }
    public Integer getPageid(){
        return this.pageid;
    }

}
