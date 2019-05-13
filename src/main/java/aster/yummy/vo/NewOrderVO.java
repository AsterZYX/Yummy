package aster.yummy.vo;

import aster.yummy.enums.OrderState;
import aster.yummy.model.OrderItem;
import lombok.Data;

import java.util.List;

@Data
public class NewOrderVO {

    private Long id;

    //订单编号
    private String orderId;

    //店铺编码
    private String shopCode;

    //订单总价
    private Double price;

    //折扣
    private Double discount;

    //下单人
    private Long userId;

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
    private List<OrderItem> itemList;

    public NewOrderVO() {
    }

}
