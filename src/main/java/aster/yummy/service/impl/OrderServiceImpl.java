package aster.yummy.service.impl;

import aster.yummy.dao.*;
import aster.yummy.dto.NewOrderDTO;
import aster.yummy.enums.DiscountState;
import aster.yummy.enums.OrderState;
import aster.yummy.enums.ResultMessage;
import aster.yummy.model.*;
import aster.yummy.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    GoodsItemDao goodsItemDao;

    @Autowired
    GoodsOrderDao goodsOrderDao;

    @Autowired
    OrderItemDao orderItemDao;

    @Autowired
    UserDao userDao;

    @Autowired
    ShopDao shopDao;

    @Override
    public NewOrderDTO createNewOrder(GoodsOrder order){
        Shop shop = order.getShop();
        List<Discount> discountList = shop.getDiscountList();
        List<OrderItem> orderItemList = order.getItemList();
        Double sum = 0.0;
        Double discount = 0.0;
        for (OrderItem temp: orderItemList){
            GoodsItem goodsItem = goodsItemDao.findById(temp.getGoodsId()).orElse(null);
            if (goodsItem != null){
                if (goodsItem.getNum() >= temp.getNum()){
                    temp.setPrice(goodsItem.getPrice());
                    sum = sum + temp.getPrice() * temp.getNum();
                } else {
                    return new NewOrderDTO(ResultMessage.NOT_ENOUGH, null);
                }
            } else {
                return new NewOrderDTO(ResultMessage.GOODS_ERROR, null);
            }
        }
        for (Discount dis: discountList){
            if (dis.getDiscountState() == DiscountState.UNDERWAY && sum >= dis.getSpend() && discount <= dis.getDiscount()){
                discount = dis.getDiscount();
            }
        }
        for(OrderItem item: orderItemList){
            GoodsItem goodsItem = goodsItemDao.findById(item.getGoodsId()).orElse(null);
            goodsItem.setNum(goodsItem.getNum() - item.getNum());
            goodsItemDao.save(goodsItem);
            orderItemDao.save(item);
        }

        order.setOrderId(getNewOrderId(order.getUser().getId()));
        order.setItemList(orderItemList);
        order.setPrice(sum);
        order.setDiscount(discount);
        GoodsOrder newOrder = goodsOrderDao.save(order);

        try {
            Timer timer = new Timer();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(newOrder.getCreateTime());
            Date expireTime = new Date(date.getTime() + 2 * 60 * 1000);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    setOrderExpired(newOrder.getId());
                }
            }, expireTime);
        }catch (ParseException e){
            e.printStackTrace();
        }

        return new NewOrderDTO(ResultMessage.SUCCESS, newOrder.getId());
    }

    @Override
    public ResultMessage payOrder(Long id) {
        GoodsOrder order = goodsOrderDao.findById(id).orElse(null);
        if (order != null){
            User user = order.getUser();
            if (order.getPrice() - order.getDiscount() <= user.getBalance()){
                user.setBalance(user.getBalance() - (order.getPrice() - order.getDiscount()));
                userDao.save(user);
                order.setOrderState(OrderState.PAID);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = sdf.format(new Date());
                order.setPayTime(time);
                goodsOrderDao.save(order);
                return ResultMessage.SUCCESS;
            } else {
                return ResultMessage.NOT_ENOUGH;
            }
        }
        return ResultMessage.FAILED;
    }

    @Override
    public Integer cancelOrder(Long id) {
        GoodsOrder goodsOrder = goodsOrderDao.findById(id).orElse(null);
        if (goodsOrder != null){
            if (goodsOrder.getOrderState() == OrderState.INITIAL){
                goodsOrder.setOrderState(OrderState.CANCELED);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = sdf.format(new Date());
                goodsOrder.setDoneTime(time);
                goodsOrderDao.save(goodsOrder);

                List<OrderItem> list = goodsOrder.getItemList();
                for (OrderItem temp : list){
                    GoodsItem item = goodsItemDao.findById(temp.getGoodsId()).orElse(null);
                    if (item != null){
                        item.setNum(item.getNum() + temp.getNum());
                        goodsItemDao.save(item);
                    }
                }

                return OrderState.CANCELED.getCode();

            } else if (goodsOrder.getOrderState() == OrderState.PAID){
                goodsOrder.setOrderState(OrderState.CANCELED);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = sdf.format(new Date());
                goodsOrder.setDoneTime(time);
                goodsOrderDao.save(goodsOrder);

                List<OrderItem> list = goodsOrder.getItemList();
                for (OrderItem temp : list){
                    GoodsItem item = goodsItemDao.findById(temp.getGoodsId()).orElse(null);
                    if (item != null){
                        item.setNum(item.getNum() + temp.getNum());
                        goodsItemDao.save(item);
                    }
                }

                User user = goodsOrder.getUser();
                user.setBalance(user.getBalance() + goodsOrder.getPrice() - goodsOrder.getDiscount());
                userDao.save(user);

                return OrderState.CANCELED.getCode();

            } else if (goodsOrder.getOrderState() == OrderState.ACCEPT){
                goodsOrder.setOrderState(OrderState.WAIT_CANCEL);
                goodsOrderDao.save(goodsOrder);

                return OrderState.WAIT_CANCEL.getCode();
            }
        }
        return null;
    }

    @Override
    public List<GoodsOrder> getOrderListByState(Long id, OrderState state) {
        User user = userDao.findById(id).orElse(null);
        if (user != null){
            List<GoodsOrder> list = goodsOrderDao.findByUserAndOrderState(user, state);
            return list;
        }
        return null;
    }

    @Override
    public List<GoodsOrder> getWaitOrderList(Long id) {
        User user = userDao.findById(id).orElse(null);
        List<GoodsOrder> list = new ArrayList<>();
        if (user != null){
            List<GoodsOrder> list1 = goodsOrderDao.findByUserAndOrderState(user, OrderState.INITIAL);
            List<GoodsOrder> list2 = goodsOrderDao.findByUserAndOrderState(user, OrderState.PAID);
            list.addAll(list1);
            list.addAll(list2);
            return list;
        }
        return null;
    }

    @Override
    public List<GoodsOrder> getCancelOrderList(Long id) {
        User user = userDao.findById(id).orElse(null);
        List<GoodsOrder> list = new ArrayList<>();
        if (user != null){
            List<GoodsOrder> list4 = goodsOrderDao.findByUserAndOrderState(user, OrderState.CANCELED);
            list.addAll(list4);
            return list;
        }
        return null;
    }

    @Override
    public List<GoodsOrder> getDistributeOrderList(Long id) {
        User user = userDao.findById(id).orElse(null);
        List<GoodsOrder> list = new ArrayList<>();
        if (user != null){
            List<GoodsOrder> list1 = goodsOrderDao.findByUserAndOrderState(user, OrderState.DISTRIBUTED);
            List<GoodsOrder> list2 = goodsOrderDao.findByUserAndOrderState(user, OrderState.ACCEPT);
            list.addAll(list1);
            list.addAll(list2);
            return list;
        }
        return null;
    }

    @Override
    public List<GoodsOrder> getDoneOrderList(Long id) {
        User user = userDao.findById(id).orElse(null);
        List<GoodsOrder> list = new ArrayList<>();
        if (user != null){
            List<GoodsOrder> list1 = goodsOrderDao.findByUserAndOrderState(user, OrderState.REFUSED);
            List<GoodsOrder> list2 = goodsOrderDao.findByUserAndOrderState(user, OrderState.CONFIRMED);
            list.addAll(list1);
            list.addAll(list2);
            Collections.sort(list, new OrderComparator());
            return list;
        }
        return null;
    }

    @Override
    public GoodsOrder getOrderInfoById(Long id) {
        GoodsOrder goodsOrder = goodsOrderDao.findById(id).orElse(null);
        return goodsOrder;
    }

    @Override
    public List<GoodsOrder> getOrderListByShop(String identifyCode) {
        Shop shop = shopDao.findByIdentifyCode(identifyCode);
        if (shop != null){
            List<GoodsOrder> list1 = goodsOrderDao.findByShopAndOrderState(shop, OrderState.CONFIRMED);
            List<GoodsOrder> list2 = goodsOrderDao.findByShopAndOrderState(shop, OrderState.CANCELED);
            List<GoodsOrder> list3 = goodsOrderDao.findByShopAndOrderState(shop, OrderState.REFUSED);
            List<GoodsOrder> res = new ArrayList<>();
            res.addAll(list1);
            res.addAll(list2);
            res.addAll(list3);
            Collections.sort(res, new OrderComparator());
            return res;
        }
        return null;
    }

    @Override
    public List<GoodsOrder> getOrderListByShopAndState(String identifyCode, OrderState state) {
        Shop shop = shopDao.findByIdentifyCode(identifyCode);
        if (shop != null){
            List<GoodsOrder> list = goodsOrderDao.findByShopAndOrderState(shop, state);
            return list;
        }
        return null;
    }

    @Override
    public ResultMessage acceptShopOrder(Long id) {
        GoodsOrder order = goodsOrderDao.findById(id).orElse(null);
        if (order != null){
            order.setOrderState(OrderState.ACCEPT);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            Date distribute = new Date(now.getTime() + 2 * 60 * 1000);
            String acceptTime = sdf.format(now);
            order.setAcceptTime(acceptTime);
            goodsOrderDao.save(order);

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    setOrderDistributed(order.getId());
                }
            }, distribute);
            return ResultMessage.SUCCESS;
        }
        return ResultMessage.FAILED;
    }

    @Override
    public ResultMessage refuseShopOrder(Long id) {
        GoodsOrder order = goodsOrderDao.findById(id).orElse(null);
        if (order != null) {
            order.setOrderState(OrderState.REFUSED);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String doneTime = sdf.format(new Date());
            order.setDoneTime(doneTime);
            goodsOrderDao.save(order);

            List<OrderItem> list = order.getItemList();
            for (OrderItem temp : list){
                GoodsItem item = goodsItemDao.findById(temp.getGoodsId()).orElse(null);
                if (item != null){
                    item.setNum(item.getNum() + temp.getNum());
                    goodsItemDao.save(item);
                }
            }

            return ResultMessage.SUCCESS;
        }
        return ResultMessage.FAILED;
    }

    @Override
    public ResultMessage confirmGoodsReceipt(Long id) {
        GoodsOrder order = goodsOrderDao.findById(id).orElse(null);
        if (order != null){
            order.setOrderState(OrderState.CONFIRMED);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(new Date());
            order.setDoneTime(time);
            goodsOrderDao.save(order);
            return ResultMessage.SUCCESS;
        }
        return ResultMessage.FAILED;
    }

    @Override
    public ResultMessage acceptCancelOrder(Long id) {
        GoodsOrder goodsOrder = goodsOrderDao.findById(id).orElse(null);
        if (goodsOrder != null){
            if (goodsOrder.getOrderState() == OrderState.WAIT_CANCEL){
                goodsOrder.setOrderState(OrderState.CANCELED);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = sdf.format(new Date());
                goodsOrder.setDoneTime(time);
                goodsOrderDao.save(goodsOrder);

                List<OrderItem> list = goodsOrder.getItemList();
                for (OrderItem temp : list){
                    GoodsItem item = goodsItemDao.findById(temp.getGoodsId()).orElse(null);
                    if (item != null){
                        item.setNum(item.getNum() + temp.getNum());
                        goodsItemDao.save(item);
                    }
                }

                Shop shop = goodsOrder.getShop();
                shop.setBalance(shop.getBalance() + (goodsOrder.getPrice() - goodsOrder.getDiscount()) * 0.1);
                shopDao.save(shop);

                User user = goodsOrder.getUser();
                user.setBalance(user.getBalance() + (goodsOrder.getPrice() - goodsOrder.getDiscount()) * 0.9);
                userDao.save(user);

                return ResultMessage.SUCCESS;
            }
        }
        return ResultMessage.FAILED;
    }

    @Override
    public ResultMessage refuseCancelOrder(Long id) {
        GoodsOrder goodsOrder = goodsOrderDao.findById(id).orElse(null);
        if (goodsOrder != null){
            if (goodsOrder.getOrderState() == OrderState.WAIT_CANCEL){
                goodsOrder.setOrderState(OrderState.DISTRIBUTED);
                goodsOrderDao.save(goodsOrder);
                return ResultMessage.SUCCESS;
            }
        }
        return ResultMessage.FAILED;
    }

    @Override
    public List<GoodsOrder> searchShopOrder(String identifyCode, String type, String startDate, String endDate){
        Shop shop = shopDao.findByIdentifyCode(identifyCode);
        if (shop == null){
            return null;
        }
        return goodsOrderDao.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicatesList = new ArrayList<>();
            if (identifyCode != null) {
                predicatesList.add(
                        criteriaBuilder.and(
                                criteriaBuilder.equal(
                                        root.get("shop"), shop)));
            }
            if (startDate != null) {
                String start = startDate + " 00:00:00";
                predicatesList.add(
                        criteriaBuilder.and(
                                criteriaBuilder.greaterThanOrEqualTo(
                                        root.get("createTime").as(String.class), start)));
            }
            if (endDate != null) {
                String end = endDate + " 23:59:59";
                predicatesList.add(
                        criteriaBuilder.and(
                                criteriaBuilder.lessThanOrEqualTo(
                                        root.get("createTime").as(String.class), end)));
            }
            if (type.equals("accept")){
                predicatesList.add(
                        criteriaBuilder.and(
                                criteriaBuilder.equal(
                                        root.get("orderState"), 3)));
            }
            if (type.equals("cancel")){
                predicatesList.add(
                        criteriaBuilder.and(
                                criteriaBuilder.equal(
                                        root.get("orderState"), 5)));
            }
            if (type.equals("refuse")){
                predicatesList.add(
                        criteriaBuilder.and(
                                criteriaBuilder.equal(
                                        root.get("orderState"), 6)));
            }
            return criteriaBuilder.and(
                    predicatesList.toArray(new Predicate[predicatesList.size()]));
        });
    }

    private void setOrderExpired(Long id){
        GoodsOrder goodsOrder = goodsOrderDao.findById(id).orElse(null);
        if (goodsOrder != null){
            if (goodsOrder.getOrderState() == OrderState.INITIAL){
                goodsOrder.setOrderState(OrderState.EXPIRED);
                goodsOrderDao.save(goodsOrder);

                List<OrderItem> list = goodsOrder.getItemList();
                for (OrderItem temp : list){
                    GoodsItem item = goodsItemDao.findById(temp.getGoodsId()).orElse(null);
                    if (item != null){
                        item.setNum(item.getNum() + temp.getNum());
                        goodsItemDao.save(item);
                    }
                }
            }
        }
    }

    private void setOrderDistributed(Long id){
        GoodsOrder goodsOrder = goodsOrderDao.findById(id).orElse(null);
        if (goodsOrder != null){
            if (goodsOrder.getOrderState() == OrderState.ACCEPT){
                goodsOrder.setOrderState(OrderState.DISTRIBUTED);
                goodsOrderDao.save(goodsOrder);
            }
        }
    }

    //获取新的订单编号
    private String getNewOrderId(Long userId){
        Random rand = new Random();
        int num = rand.nextInt(8999) + 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String randId = userId + sdf.format(new Date()) + num;
        return randId;
    }

    private class OrderComparator implements Comparator<GoodsOrder>{
        @Override
        public int compare(GoodsOrder o1, GoodsOrder o2) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date1 = sdf.parse(o1.getCreateTime());
                Date date2 = sdf.parse(o2.getCreateTime());
                if (date1.after(date2)){
                    return -1;
                } else if (date1.equals(date2)){
                    return 0;
                } else {
                    return 1;
                }
            } catch (ParseException e){
                e.printStackTrace();
            }
            return 1;
        }
    }

}
