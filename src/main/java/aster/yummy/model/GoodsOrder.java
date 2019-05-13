package aster.yummy.model;

import aster.yummy.enums.OrderState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class GoodsOrder {

    //编号
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //订单编号
    private String orderId;

    //店铺编码
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "shop_id", referencedColumnName = "id")
    private Shop shop;

    //订单总价
    private Double price;

    //折扣
    private Double discount;

    //下单人
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    //联系人
    private String linkman;

    //联系电话
    private String phone;

    //详细地址
    private String addressDisp;

    //门牌号
    private String room;

    //订单状态
    private OrderState orderState;

    //下单时间
    private String createTime;

    //支付时间
    private String payTime;

    //接单时间
    private String acceptTime;

    //完成时间
    private String doneTime;

    //商品列表
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "order_item_list", joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "item_id", referencedColumnName = "id")})
    private List<OrderItem> itemList;

    public GoodsOrder() {
    }

    public GoodsOrder(Shop shop, User user, String linkman, String phone, String addressDisp, String room, OrderState orderState, String createTime, List<OrderItem> itemList) {
        this.shop = shop;
        this.user = user;
        this.linkman = linkman;
        this.phone = phone;
        this.addressDisp = addressDisp;
        this.room = room;
        this.orderState = orderState;
        this.createTime = createTime;
        this.itemList = itemList;
    }
}
