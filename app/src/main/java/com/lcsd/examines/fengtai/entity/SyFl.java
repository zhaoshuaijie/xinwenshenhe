package com.lcsd.examines.fengtai.entity;

/**
 * Created by Administrator on 2017/11/22.
 */
public class SyFl {
    private	Integer	total;	/*0 未审核*/
    private Integer total2;//未通过
    private	String	id;	/*43*/
    private	Integer	son;	/*1 用于首页判断有没有子分类*/
    private	String	title;	/*新闻*/
    private	String	ico;	/**/
    private	String	cate_id;	/*7*/
    private	String	identifier;	/*news*/
    private	String	url;	/*http://www.ahft.tv/index.php?c=cate&pid=43*/

    public void setTotal(Integer value){
        this.total = value;
    }
    public Integer getTotal(){
        return this.total;
    }

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

    public void setIco(String value){
        this.ico = value;
    }
    public String getIco(){
        return this.ico;
    }

    public void setCate_id(String value){
        this.cate_id = value;
    }
    public String getCate_id(){
        return this.cate_id;
    }

    public void setIdentifier(String value){
        this.identifier = value;
    }
    public String getIdentifier(){
        return this.identifier;
    }

    public void setUrl(String value){
        this.url = value;
    }
    public String getUrl(){
        return this.url;
    }

    public Integer getTotal2() {
        return total2;
    }

    public void setTotal2(Integer total2) {
        this.total2 = total2;
    }

    public Integer getSon() {
        return son;
    }

    public void setSon(Integer son) {
        this.son = son;
    }
}
