package aster.yummy.vo;

import aster.yummy.enums.ShopType;
import lombok.Data;

@Data
public class ShopCardVO {

    //识别码
    private String identifyCode;

    //餐厅名称
    private String name;

    //餐厅地址
    private String address;

    //餐厅类型
    private String shopType;

    //餐厅头像
    private String avatar;

    public ShopCardVO() {
    }

    public ShopCardVO(String identifyCode, String name, String address, String shopType, String avatar) {
        this.identifyCode = identifyCode;
        this.name = name;
        this.address = address;
        this.shopType = shopType;
        this.avatar = avatar;
    }
}
