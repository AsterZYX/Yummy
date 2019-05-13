package aster.yummy.vo;

import lombok.Data;

@Data
public class UserSignVO {

    private Long id;

    //登录邮箱
    private String email;

    //登录密码
    private String password;

    //电话
    private String phone;

    //姓名
    private String name;

}
