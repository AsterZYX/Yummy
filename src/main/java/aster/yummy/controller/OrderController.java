package aster.yummy.controller;

import aster.yummy.dto.NewOrderDTO;
import aster.yummy.enums.OrderState;
import aster.yummy.enums.ResultMessage;
import aster.yummy.model.GoodsOrder;
import aster.yummy.model.OrderItem;
import aster.yummy.model.Shop;
import aster.yummy.model.User;
import aster.yummy.service.OrderService;
import aster.yummy.service.ShopService;
import aster.yummy.service.UserService;
import aster.yummy.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class OrderController {

    @Autowired
    ShopService shopService;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @PostMapping("/order/new")
    public ResultVO<Long> createNewOrder(@RequestBody NewOrderVO newOrderVO){
        Shop shop = shopService.getShopByIdentifyCode(newOrderVO.getShopCode());
        User user = userService.getUserById(newOrderVO.getUserId());
        if (shop != null && user != null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(new Date());
            newOrderVO.setCreateTime(time);
            GoodsOrder goodsOrder = new GoodsOrder(shop, user, newOrderVO.getLinkman(), newOrderVO.getPhone(), newOrderVO.getAddressDisp(), newOrderVO.getRoom(), OrderState.INITIAL, newOrderVO.getCreateTime(), newOrderVO.getItemList());
            NewOrderDTO dto = orderService.createNewOrder(goodsOrder);
            if (dto != null){
                return new ResultVO<>(dto.getRe().getCode(), dto.getRe().getValue(), dto.getOrderId());
            }
        }
        return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), null);
    }

    @PostMapping("/order/pay")
    public ResultVO<String> payOrder(@RequestParam("id") Long id){
        ResultMessage re = orderService.payOrder(id);
        return new ResultVO<>(re.getCode(), re.getValue(), null);
    }

    @PostMapping("/order/cancel")
    public ResultVO<Integer> cancelOrder(@RequestParam("id") Long id){
        Integer re = orderService.cancelOrder(id);
        if (re != null){
            return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), re);
        }
        return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), null);
    }

    @PostMapping("/order/accept")
    public ResultVO<String> acceptShopOrder(@RequestParam("id") Long id){
        ResultMessage re = orderService.acceptShopOrder(id);
        return new ResultVO<>(re.getCode(), re.getValue(), null);
    }

    @PostMapping("/order/refuse")
    public ResultVO<String> refuseShopOrder(@RequestParam("id") Long id){
        ResultMessage re = orderService.refuseShopOrder(id);
        return new ResultVO<>(re.getCode(), re.getValue(), null);
    }

    @PostMapping("/order/receive")
    public ResultVO<String> confirmGoodsReceipt(@RequestParam("id") Long id){
        ResultMessage re = orderService.confirmGoodsReceipt(id);
        return new ResultVO<>(re.getCode(), re.getValue(), null);
    }

    @GetMapping("/order/list")
    public ResultVO<List<OrderVO>> getOrderListByState(@RequestParam("id") Long id,
                                                       @RequestParam("state") String orderState){
        OrderState state = OrderState.values()[Integer.parseInt(orderState)];
        List<GoodsOrder> list = orderService.getOrderListByState(id, state);
        List<OrderVO> result = new ArrayList<>();
        if (list != null){
            for (GoodsOrder temp : list){
                OrderVO orderVO = goodsOrderToOrderVO(temp);
                result.add(orderVO);
            }
            return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), result);
        }
        return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), null);
    }

    @GetMapping("/order/list/wait")
    public ResultVO<List<OrderVO>> getWaitOrderList(@RequestParam("id") Long id){
        List<GoodsOrder> list = orderService.getWaitOrderList(id);
        List<OrderVO> result = new ArrayList<>();
        if (list != null){
            for (GoodsOrder temp : list){
                OrderVO orderVO = goodsOrderToOrderVO(temp);
                result.add(orderVO);
            }
            return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), result);
        }
        return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), null);
    }

    @GetMapping("/order/list/distribute")
    public ResultVO<List<OrderVO>> getDistributeOrderList(@RequestParam("id") Long id){
        List<GoodsOrder> list = orderService.getDistributeOrderList(id);
        List<OrderVO> result = new ArrayList<>();
        if (list != null){
            for (GoodsOrder temp : list){
                OrderVO orderVO = goodsOrderToOrderVO(temp);
                result.add(orderVO);
            }
            return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), result);
        }
        return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), null);
    }

    @GetMapping("/order/list/cancel")
    public ResultVO<List<OrderVO>> getCancelOrderList(@RequestParam("id") Long id){
        List<GoodsOrder> list = orderService.getCancelOrderList(id);
        List<OrderVO> result = new ArrayList<>();
        if (list != null){
            for (GoodsOrder temp : list){
                OrderVO orderVO = goodsOrderToOrderVO(temp);
                result.add(orderVO);
            }
            return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), result);
        }
        return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), null);
    }

    @GetMapping("/order/list/done")
    public ResultVO<List<OrderVO>> getDoneOrderList(@RequestParam("id") Long id){
        List<GoodsOrder> list = orderService.getDoneOrderList(id);
        List<OrderVO> result = new ArrayList<>();
        if (list != null){
            for (GoodsOrder temp : list){
                OrderVO orderVO = goodsOrderToOrderVO(temp);
                result.add(orderVO);
            }
            return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), result);
        }
        return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), null);
    }

    @GetMapping("/order/info")
    public ResultVO<OrderVO> getOrderInfoById(@RequestParam("id") Long id){
        GoodsOrder temp = orderService.getOrderInfoById(id);
        if (temp != null){
            OrderVO orderVO = goodsOrderToOrderVO(temp);
            return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), orderVO);
        }
        return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), null);
    }

    @GetMapping("/order/list/shop/history")
    public ResultVO<List<OrderVO>> getOrderListByShopAndState(@RequestParam("code") String identifyCode){
        List<GoodsOrder> orderList = orderService.getOrderListByShop(identifyCode);
        List<OrderVO> result = new ArrayList<>();
        if (orderList != null){
            for (GoodsOrder temp: orderList){
                OrderVO orderVO = goodsOrderToOrderVO(temp);
                result.add(orderVO);
            }
            return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), result);
        }
        return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), null);
    }

    @GetMapping("/order/list/search")
    public ResultVO<List<OrderVO>> searchShopOrder(@RequestParam("code") String identifyCode,
                                                   @RequestParam("type") String type,
                                                   @RequestParam(name = "startDate", required = false) String startDate,
                                                   @RequestParam(name = "endDate", required = false) String endDate){
        List<OrderVO> result = new ArrayList<>();
        List<GoodsOrder> orderList;
        if (startDate != null && startDate.length() != 0 && endDate != null && endDate.length() != 0) {
            orderList = orderService.searchShopOrder(identifyCode, type, startDate, endDate);
        }
        else {
            if (type.equals("all")){
                orderList = orderService.getOrderListByShop(identifyCode);
            } else if (type.equals("accept")){
                orderList = orderService.getOrderListByShopAndState(identifyCode, OrderState.CONFIRMED);
            } else if (type.equals("cancel")){
                orderList = orderService.getOrderListByShopAndState(identifyCode, OrderState.CANCELED);
            } else {
                orderList = orderService.getOrderListByShopAndState(identifyCode, OrderState.REFUSED);
            }
        }
        if (orderList != null){
            for (GoodsOrder temp: orderList){
                OrderVO orderVO = goodsOrderToOrderVO(temp);
                result.add(orderVO);
            }
            return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), result);
        }
        return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), null);
    }

    @GetMapping("/order/list/shop")
    public ResultVO<List<OrderVO>> getOrderListByShopAndState(@RequestParam("code") String identifyCode,
                                                             @RequestParam("state") String stateOrder){
        List<GoodsOrder> orderList = orderService.getOrderListByShopAndState(identifyCode, OrderState.values()[Integer.parseInt(stateOrder)]);
        List<OrderVO> result = new ArrayList<>();
        if (orderList != null){
            for (GoodsOrder temp: orderList){
                OrderVO orderVO = goodsOrderToOrderVO(temp);
                result.add(orderVO);
            }
            return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), result);
        }
        return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), null);
    }

    @PostMapping("/order/cancel/accept")
    public ResultVO<String> acceptShopCancelOrder(@RequestParam("id") Long id){
        ResultMessage re = orderService.acceptCancelOrder(id);
        return new ResultVO<>(re.getCode(), re.getValue(), null);
    }

    @PostMapping("/order/cancel/refuse")
    public ResultVO<String> refuseShopCancelOrder(@RequestParam("id") Long id){
        ResultMessage re = orderService.refuseCancelOrder(id);
        return new ResultVO<>(re.getCode(), re.getValue(), null);
    }

    private OrderVO goodsOrderToOrderVO(GoodsOrder temp){
        if (temp.getShop() != null && temp.getUser() != null) {
            ShopCardVO shop = new ShopCardVO(temp.getShop().getIdentifyCode(), temp.getShop().getName(), temp.getShop().getAddress(), temp.getShop().getShopType().getType(), temp.getShop().getAvatar());
            UserVO user = new UserVO(temp.getUser().getId(), temp.getUser().getEmail(), temp.getUser().getPhone(), temp.getUser().getName(), temp.getUser().getAvatar(), temp.getUser().getUserLevel(), temp.getUser().getBalance());
            OrderVO orderVO = new OrderVO(temp.getId(), temp.getOrderId(), shop, temp.getPrice(), temp.getDiscount(), user, temp.getLinkman(), temp.getPhone(), temp.getAddressDisp(), temp.getRoom(), temp.getOrderState().getCode(), temp.getCreateTime(), temp.getPayTime(), temp.getAcceptTime(), temp.getDoneTime(), temp.getItemList());
            return orderVO;
        }
        return null;
    }
}
