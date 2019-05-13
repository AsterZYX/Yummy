package aster.yummy.service;

import aster.yummy.dto.NewOrderDTO;
import aster.yummy.enums.OrderState;
import aster.yummy.enums.ResultMessage;
import aster.yummy.model.GoodsOrder;
import aster.yummy.model.Shop;

import java.util.List;

public interface OrderService {

    public NewOrderDTO createNewOrder(GoodsOrder order);

    public ResultMessage payOrder(Long id);

    public Integer cancelOrder(Long id);

    public ResultMessage confirmGoodsReceipt(Long id);

    public List<GoodsOrder> getOrderListByState(Long id, OrderState state);

    public GoodsOrder getOrderInfoById(Long id);

    public List<GoodsOrder> getOrderListByShop(String identifyCode);

    public List<GoodsOrder> getOrderListByShopAndState(String identifyCode, OrderState state);

    public List<GoodsOrder> searchShopOrder(String identifyCode,String type, String startDate, String endDate);

    public List<GoodsOrder> getWaitOrderList(Long id);

    public List<GoodsOrder> getDistributeOrderList(Long id);

    public List<GoodsOrder> getCancelOrderList(Long id);

    public List<GoodsOrder> getDoneOrderList(Long id);

    public ResultMessage acceptShopOrder(Long id);

    public ResultMessage refuseShopOrder(Long id);

    public ResultMessage acceptCancelOrder(Long id);

    public ResultMessage refuseCancelOrder(Long id);
}
