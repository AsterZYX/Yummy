package aster.yummy.vo;

import aster.yummy.enums.ShopType;
import lombok.Data;

@Data
public class ShopSignVO {

    //餐厅名称
    private String name;

    //登录密码
    private String password;

    //餐厅地址
    private String address;

    //餐厅类型
    private ShopType shopType;

    //省
    private String province;

    //市
    private String city;

}
