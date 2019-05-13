package aster.yummy.vo;

import aster.yummy.enums.UserLevel;
import aster.yummy.model.Address;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserVO {

    private Long id;

    //登录邮箱
    private String email;

    //电话
    private String phone;

    //姓名
    private String name;

    //等级
    private UserLevel userLevel;

    //头像
    private String avatar;

    //地址
    private List<Address> addresses;

    //余额
    private Double balance;

    public UserVO() {
    }

    public UserVO(Long id, String email, String phone, String name, UserLevel userLevel, String avatar,  List<Address> addresses, Double balance) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.userLevel = userLevel;
        this.avatar = avatar;
        this.addresses = addresses;
        this.balance = balance;
    }

    public UserVO(Long id, String email, String phone, String name, String avatar,  UserLevel userLevel, Double balance) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.avatar = avatar;
        this.userLevel = userLevel;
        this.balance = balance;
    }
}
