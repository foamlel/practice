package com.foam.MyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DBUtil {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("请输入用户名：");
        String name = input.next();
        System.out.println("请输入密码：");
        String password = input.next();
        User my = new User(name,password);
        String sql = "select id,name,password from user where name=? and password=?;";
        List<Object> list = new ArrayList();
        list.add(name);
        list.add(password);
        List<User> list2 = MyUtil.<User>upDate(sql,list, (Class<User>) my.getClass());
        System.out.println(list2.size()>0? "登陆成功:"+list2.get(0).toString():"登录失败");
    }
}
