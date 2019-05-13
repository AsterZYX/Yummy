package aster.yummy.service.schedule;

import aster.yummy.dao.DiscountDao;
import aster.yummy.dao.GoodsOrderDao;
import aster.yummy.dao.ShopDao;
import aster.yummy.dao.ShopFinanceDao;
import aster.yummy.enums.DiscountState;
import aster.yummy.enums.FinanceType;
import aster.yummy.enums.OrderState;
import aster.yummy.enums.ShopState;
import aster.yummy.model.Discount;
import aster.yummy.model.GoodsOrder;
import aster.yummy.model.Shop;
import aster.yummy.model.ShopFinance;
import aster.yummy.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class ScheduledTasks {

    private Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private DiscountDao discountDao;

    @Autowired
    private ShopDao shopDao;

    @Autowired
    private ShopFinanceDao shopFinanceDao;

    @Autowired
    private OrderService orderService;

    @Scheduled(cron = "0 0 0 * * *") //每天0时0分进行一次
    public void updateDiscountList() throws ParseException{
        logger.info("每日零点满减活动更新");

        List<Discount> list1 = discountDao.findByDiscountState(DiscountState.NOT_STARTED);
        List<Discount> list2 = discountDao.findByDiscountState(DiscountState.UNDERWAY);
        List<Discount> list = new ArrayList<>();
        list.addAll(list1);
        list.addAll(list2);
        for (Discount temp: list){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf.parse(temp.getStartDate());
            Date end = sdf.parse(temp.getEndDate());
            if ((new Date().equals(start) || new Date().after(start)) && (new Date().equals(end) || new Date().before(end))){
                temp.setDiscountState(DiscountState.UNDERWAY);
                discountDao.save(temp);
            }
            if (new Date().after(start)){
                temp.setDiscountState(DiscountState.FINISHED);
                discountDao.save(temp);
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *") //每天0时0分进行一次
    public void settleAccounts(){
        logger.info("每日零点结算昨日收益");

        List<Shop> shopList = shopDao.findByShopState(ShopState.PASS);
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
        Date yesterday = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String day = sdf.format(yesterday);
        for (Shop shop: shopList){
            Double price = 0.0;
            List<GoodsOrder> orderList = orderService.searchShopOrder(shop.getIdentifyCode(), "accept", day, day);
            if (orderList != null && orderList.size() != 0) {
                for (GoodsOrder order : orderList) {
                    price = price + order.getPrice() - order.getDiscount();
                }

                ShopFinance shopFinance = new ShopFinance(shop, FinanceType.INCOME, day, price * 0.9, shop.getBalance() + price * 0.9);
                shopFinanceDao.save(shopFinance);

                shop.setBalance(shop.getBalance() + price * 0.9);
                shopDao.save(shop);
            }
        }
    }
}
