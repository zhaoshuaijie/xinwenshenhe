package com.lcsd.examines.fengtai.http;

/**
 * Created by Administrator on 2017/9/8.
 */
public class AppConfig {

    /**
     * 管理员登录：c=login ,post:user,pass
     * 管理员信息：c=usercp
     * 退出 ：c=logout
     * 测试账号：test，123123
     *  首页get http://www.ahft.tv/check/index.php
     *  分类：上传 c=cate&pid
     *  未审核列表：{pid:“项目id”,cateid:“分类id”,pageid:“当前页码”,psize:“显示数量”}
     *  修改审核状态：c=status&f=edit上传：{id:“文章id”，status:“审核状态：1通过，2不通过”，note:“不通过原因”，audio:“语音”}
     */
    public static String mianurl="http://www.ahft.tv/check/index.php";

}
