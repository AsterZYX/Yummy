package aster.yummy.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class GoodsItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String avatar;

    private Double price;

    private String goodsType;

    private Integer num;

    private String description;

    public GoodsItem() {
    }

    public GoodsItem(String name, String avatar, Double price, String goodsType, Integer num, String description) {
        this.name = name;
        this.avatar = avatar;
        this.price = price;
        this.goodsType = goodsType;
        this.num = num;
        this.description = description;
    }

    public GoodsItem(String name, Double price, String goodsType, Integer num, String description) {
        this.name = name;
        this.price = price;
        this.goodsType = goodsType;
        this.num = num;
        this.description = description;
    }
}
