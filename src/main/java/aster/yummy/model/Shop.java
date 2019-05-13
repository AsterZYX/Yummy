package aster.yummy.model;

import aster.yummy.enums.ShopState;
import aster.yummy.enums.ShopType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Shop {

    //编号
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //识别码
    private String identifyCode;

    //餐厅名称
    private String name;

    //登录密码
    private String password;

    //餐厅地址
    private String address;

    //餐厅类型
    private ShopType shopType;

    //餐厅状态
    private ShopState shopState;

    //餐厅头像
    private String avatar;

    //餐厅余额
    private Double balance;

    //省
    private String province;

    //市
    private String city;

    //注册时间
    private String date;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "shop_goods", joinColumns = {@JoinColumn(name = "shop_id")}, inverseJoinColumns = {@JoinColumn(name = "goods_item_id")})
    private List<GoodsItem> goodsList;

    //餐厅食品分类
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "shop_item_type")
    @Column(name = "item_type")
    private List<String> types;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "shop_discount", joinColumns = {@JoinColumn(name = "shop_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "discount_id", referencedColumnName = "id")})
    private List<Discount> discountList;

    public Shop() {
    }

    public Shop(String name, String password, String address, ShopType shopType, String province, String city) {
        this.name = name;
        this.password = password;
        this.address = address;
        this.shopType = shopType;
        this.shopState = ShopState.SUBMITTED;
        this.balance = 0.0;
        this.province = province;
        this.city = city;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.date = sdf.format(new Date());
    }

}
