package aster.yummy.model;

import aster.yummy.enums.ApplyState;
import aster.yummy.enums.ShopState;
import aster.yummy.enums.ShopType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class ShopApply {

    //编号
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //申请商家
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "shop_id", referencedColumnName = "id")
    private Shop shop;

    //识别码
    private String identifyCode;

    //餐厅名称
    private String name;

    //餐厅地址
    private String address;

    //餐厅类型
    private ShopType shopType;

    //餐厅状态
    private ApplyState applyState;

    //餐厅头像
    private String avatar;

    public ShopApply() {
    }

    public ShopApply(String identifyCode, String name, String address, ShopType shopType, ApplyState applyState, String avatar) {
        this.identifyCode = identifyCode;
        this.name = name;
        this.address = address;
        this.shopType = shopType;
        this.applyState = applyState;
        this.avatar = avatar;
    }
}
