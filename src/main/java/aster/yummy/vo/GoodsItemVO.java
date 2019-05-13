package aster.yummy.vo;

import lombok.Data;

@Data
public class GoodsItemVO {

    private String name;

    private String avatar;

    private Double price;

    private String goodsType;

    private Integer num;

    private String description;

    private String shopCode;

    public GoodsItemVO() {
    }

    public GoodsItemVO(String name, String avatar, Double price, String goodsType, Integer num, String description, String shopCode) {
        this.name = name;
        this.avatar = avatar;
        this.price = price;
        this.goodsType = goodsType;
        this.num = num;
        this.description = description;
        this.shopCode = shopCode;
    }
}
