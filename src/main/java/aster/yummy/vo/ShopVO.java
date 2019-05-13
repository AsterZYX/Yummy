package aster.yummy.vo;

import aster.yummy.enums.ShopState;
import aster.yummy.enums.ShopType;
import aster.yummy.model.GoodsItem;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ShopVO {

    //识别码
    private String identifyCode;

    //餐厅名称
    private String name;

    //餐厅地址
    private String address;

    //餐厅类型
    private String shopType;

    //餐厅状态
    private ShopState shopState;

    //餐厅头像
    private String avatar;

    //商品列表
    private List<GoodsItem> goodsList;

    //商品类型
    private List<String> itemType;

    //餐厅余额
    private Double balance;

    //省
    private String province;

    //市
    private String city;

    public ShopVO() {
    }

    public ShopVO(String identifyCode, String name, String address, String shopType, ShopState shopState, String avatar, List<GoodsItem> goodsList, List<String> itemType, Double balance, String province, String city) {
        this.identifyCode = identifyCode;
        this.name = name;
        this.address = address;
        this.shopType = shopType;
        this.shopState = shopState;
        this.avatar = avatar;
        this.goodsList = goodsList;
        this.itemType = itemType;
        this.balance = balance;
        this.province = province;
        this.city = city;
    }
}
