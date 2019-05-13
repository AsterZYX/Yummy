package aster.yummy.vo;

import aster.yummy.model.OrderItem;
import lombok.Data;

import java.util.List;

@Data
public class OrderVO {

    //编号
    private Long id;

    //订单编号
    private String orderId;

    //商店
    private ShopCardVO shop;

    //订单总价
    private Double price;

    //折扣
    private Double discount;

    //下单人
    private UserVO user;

    //联系人
    private String linkman;

    //联系电话
    private String phone;

    //详细地址
    private String addressDisp;

    //门牌号
    private String room;

    //订单状态
    private Integer orderState;

    //下单时间
    private String createTime;

    //支付时间
    private String payTime;

    //接单时间
    private String acceptTime;

    //完成时间
    private String doneTime;

    //商品列表
    private List<OrderItem> itemList;

    public OrderVO() {
    }

    public OrderVO(Long id, String orderId, ShopCardVO shop, Double price, Double discount, UserVO user, String linkman, String phone, String addressDisp, String room, Integer orderState, String createTime, String payTime, String acceptTime, String doneTime, List<OrderItem> itemList) {
        this.id = id;
        this.orderId = orderId;
        this.shop = shop;
        this.price = price;
        this.discount = discount;
        this.user = user;
        this.linkman = linkman;
        this.phone = phone;
        this.addressDisp = addressDisp;
        this.room = room;
        this.orderState = orderState;
        this.createTime = createTime;
        this.payTime = payTime;
        this.acceptTime = acceptTime;
        this.doneTime = doneTime;
        this.itemList = itemList;
    }
}
