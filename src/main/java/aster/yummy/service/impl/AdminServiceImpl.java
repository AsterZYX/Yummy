package aster.yummy.service.impl;

import aster.yummy.dao.GoodsOrderDao;
import aster.yummy.dao.ShopApplyDao;
import aster.yummy.dao.ShopDao;
import aster.yummy.dao.UserDao;
import aster.yummy.dto.FinanceDTO;
import aster.yummy.dto.ShopDataDTO;
import aster.yummy.dto.TimeDataDTO;
import aster.yummy.enums.*;
import aster.yummy.model.GoodsOrder;
import aster.yummy.model.Shop;
import aster.yummy.model.ShopApply;
import aster.yummy.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {

    private static String fileSeparator = System.getProperty("file.separator");

    private static String SHOP_AVATAR_UPLOADED_FOLDER = "images" + fileSeparator + "shop";

    @Autowired
    ShopApplyDao shopApplyDao;

    @Autowired
    ShopDao shopDao;

    @Autowired
    UserDao userDao;

    @Autowired
    GoodsOrderDao goodsOrderDao;

    @Override
    public List<ShopApply> getAllSubmittedShopApply() {
        List<ShopApply> list = shopApplyDao.findByApplyState(ApplyState.SUBMITTED);
        return list;
    }

    @Override
    public ResultMessage examineShopApply(Long id, Boolean res) {
        ShopApply shopApply = shopApplyDao.findById(id).orElse(null);
        if (shopApply != null){
            if (res){
                Shop shop = shopApply.getShop();
                shop.setName(shopApply.getName());
                shop.setAddress(shopApply.getAddress());
                shop.setShopType(shopApply.getShopType());
                if (shopApply.getAvatar() != null){
                    if (shop.getAvatar() != null){
                        File file = new File(SHOP_AVATAR_UPLOADED_FOLDER + fileSeparator + shop.getId() + fileSeparator + shop.getAvatar());
                        if (file.exists() && file.isFile()){
                            Boolean re = file.delete();
                        }
                    }
                    shop.setAvatar(shopApply.getAvatar());
                }
                shopDao.save(shop);
                shopApply.setApplyState(ApplyState.PASS);
                shopApplyDao.save(shopApply);
            } else {
                shopApply.setApplyState(ApplyState.REJECT);
                shopApplyDao.save(shopApply);
            }
            return ResultMessage.SUCCESS;
        }
        return ResultMessage.FAILED;
    }

    @Override
    public ResultMessage examineShopSignApply(String identifyCode, Boolean res) {
        Shop shop = shopDao.findByIdentifyCode(identifyCode);
        if (shop != null){
            if (res){
                shop.setShopState(ShopState.PASS);
            } else {
                shop.setShopState(ShopState.REJECT);
            }
            shopDao.save(shop);
            return ResultMessage.SUCCESS;
        }
        return ResultMessage.FAILED;
    }

    @Override
    public List<TimeDataDTO> getUserSignData(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -14);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<TimeDataDTO> list = new ArrayList<>();
        for (int i=0; i<15; i++){
            String now = sdf.format(calendar.getTime());
            Integer total = userDao.findByDateLessThanEqual(now).size();
            Integer growth = userDao.findByDate(now).size();
            TimeDataDTO dto = new TimeDataDTO(now, total, growth);
            list.add(dto);
            calendar.add(Calendar.DATE, 1);
        }
        return list;
    }

    @Override
    public List<ShopDataDTO> getShopTypeData() {
        List<ShopDataDTO> list = new ArrayList<>();
        for (int i=0; i<ShopType.values().length; i++){
            Integer num = shopDao.findByShopType(ShopType.values()[i]).size();
            ShopDataDTO dto = new ShopDataDTO(ShopType.values()[i].getType(), num);
            list.add(dto);
        }
        return list;
    }

    @Override
    public List<ShopDataDTO> getShopCityData(){
        List<Shop> shopList = shopDao.findAll();
        List<ShopDataDTO> result = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        for (Shop shop: shopList){
            if (shop.getProvince().equals("北京市") || shop.getProvince().equals("天津市") || shop.getProvince().equals("上海市") || shop.getProvince().equals("重庆市") || shop.getProvince().equals("台湾省") ){
                String city = shop.getProvince();
                if (city.contains("市") || city.contains("省")){
                    city = city.substring(0, city.length()-1);
                }
                if (map.containsKey(city)) {
                    map.replace(city, map.get(city) + 1);
                } else {
                    map.put(city, 1);
                }
            } else {
                String city = shop.getCity();
                if (city.contains("市") || city.contains("省")){
                    city = city.substring(0, city.length()-1);
                }
                if (map.containsKey(city)) {
                    map.replace(city, map.get(city) + 1);
                } else {
                    map.put(city, 1);
                }
            }
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()){
            ShopDataDTO dto = new ShopDataDTO(entry.getKey(), entry.getValue());
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<TimeDataDTO> getShopSignData(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -6);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<TimeDataDTO> list = new ArrayList<>();
        for (int i=0; i<7; i++){
            String now = sdf.format(calendar.getTime());
            Integer total = shopDao.findByDateLessThanEqual(now).size();
            Integer growth = shopDao.findByDate(now).size();
            TimeDataDTO dto = new TimeDataDTO(now, total, growth);
            list.add(dto);
            calendar.add(Calendar.DATE, 1);
        }
        return list;
    }

    @Override
    public FinanceDTO getFinanceData() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Double today = 0.0;
        Double yesterday = 0.0;
        List<GoodsOrder> todayList = findOrderByOrderStateAndDate(OrderState.CONFIRMED, sdf.format(new Date()));
        List<GoodsOrder> yesterdayList = findOrderByOrderStateAndDate(OrderState.CONFIRMED, sdf.format(calendar.getTime()));

        for (GoodsOrder temp: todayList){
            today = today + temp.getPrice() - temp.getDiscount();
        }
        for (GoodsOrder temp: yesterdayList){
            yesterday = yesterday + temp.getPrice() - temp.getDiscount();
        }

        return new FinanceDTO(today, yesterday);
    }

    private List<GoodsOrder> findOrderByOrderStateAndDate(OrderState orderState, String date){
        return goodsOrderDao.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicatesList = new ArrayList<>();
            if (orderState != null) {
                predicatesList.add(
                        criteriaBuilder.and(
                                criteriaBuilder.equal(
                                        root.get("orderState"), orderState)));
            }
            if (date != null) {
                String start = date + " 00:00:00";
                predicatesList.add(
                        criteriaBuilder.and(
                                criteriaBuilder.greaterThanOrEqualTo(
                                        root.get("createTime").as(String.class), start)));
                String end = date + " 23:59:59";
                predicatesList.add(
                        criteriaBuilder.and(
                                criteriaBuilder.lessThanOrEqualTo(
                                        root.get("createTime").as(String.class), end)));
            }
            return criteriaBuilder.and(
                    predicatesList.toArray(new Predicate[predicatesList.size()]));
        });
    }
}
