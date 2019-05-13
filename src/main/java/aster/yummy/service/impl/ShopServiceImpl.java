package aster.yummy.service.impl;

import aster.yummy.dao.*;
import aster.yummy.dto.ShopItemDTO;
import aster.yummy.enums.*;
import aster.yummy.model.*;
import aster.yummy.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ShopServiceImpl implements ShopService {

    private static String fileSeparator = System.getProperty("file.separator");

    private static String ITEM_AVATAR_UPLOADED_FOLDER = "images" + fileSeparator + "item";

    @Autowired
    ShopDao shopDao;

    @Autowired
    GoodsItemDao goodsItemDao;

    @Autowired
    ShopApplyDao shopApplyDao;

    @Autowired
    DiscountDao discountDao;

    @Autowired
    ShopFinanceDao shopFinanceDao;

    @Override
    public Page<Shop> getAllShops(Integer size, Integer currentPage) {
        Pageable pageable = PageRequest.of(currentPage-1, size);
        return shopDao.findAll(pageable);
    }

    @Override
    public Page<Shop> getShopsByCity(Integer size, Integer currentPage, String city) {
        Pageable pageable = PageRequest.of(currentPage-1, size);
        return shopDao.findByCityAndShopState(city, ShopState.PASS, pageable);
    }

    @Override
    public List<Shop> getShopByState(ShopState shopState) {
        return shopDao.findByShopState(shopState);
    }

    @Override
    public Shop getShopByIdentifyCode(String identifyCode) {
        return shopDao.findByIdentifyCode(identifyCode);
    }

    @Override
    public ShopItemDTO addGoodsItem(String identifyCode, GoodsItem goodsItem) {
        Shop shop = shopDao.findByIdentifyCode(identifyCode);
        if(shop != null){
            GoodsItem newItem = goodsItemDao.save(goodsItem);
            shop.getGoodsList().add(newItem);
            Shop newShop = shopDao.save(shop);
            return new ShopItemDTO(newShop, newItem);
        }
        return null;
    }

    @Override
    public ResultMessage addItemType(String identifyCode, String itemType) {
        Shop shop = shopDao.findByIdentifyCode(identifyCode);
        if(shop != null){
            if(!shop.getTypes().contains(itemType)){
                shop.getTypes().add(itemType);
                shopDao.save(shop);
                return ResultMessage.SUCCESS;
            }
            else {
                return ResultMessage.EXIST;
            }
        }
        return ResultMessage.FAILED;
    }

    @Override
    public GoodsItem getItemById(Long id) {
        GoodsItem goodsItem = goodsItemDao.findById(id).orElse(null);
        return goodsItem;
    }

    @Override
    public ResultMessage updateItemAvatar(Long id, String name) {
        GoodsItem goodsItem = goodsItemDao.findById(id).orElse(null);
        if(goodsItem != null){
            goodsItem.setAvatar(name);
            goodsItemDao.save(goodsItem);
            return ResultMessage.SUCCESS;
        }
        return ResultMessage.FAILED;
    }

    @Override
    public ResultMessage deleteItemType(String identifyCode, String itemType) {
        Shop shop = shopDao.findByIdentifyCode(identifyCode);
        if(shop != null){
            List<GoodsItem> goodsItems = shop.getGoodsList();
            Boolean isEmpty = true;
            if (goodsItems != null){
                for(int i=0; i<goodsItems.size(); i++){
                    if(goodsItems.get(i).getGoodsType().equals(itemType)){
                        isEmpty = false;
                        break;
                    }
                }
            }
            if(isEmpty) {
                List<String> types = shop.getTypes();
                if (types != null) {
                    if (types.contains(itemType)){
                        types.remove(itemType);
                        shop.setTypes(types);
                        shopDao.save(shop);
                        return ResultMessage.SUCCESS;
                    }
                }
            }
        }
        return ResultMessage.FAILED;
    }

    @Override
    public ResultMessage deleteGoodsItem(String identifyCode, Long id) {
        Shop shop = shopDao.findByIdentifyCode(identifyCode);
        if (shop != null){
            List<GoodsItem> goodsItems = shop.getGoodsList();
            if(goodsItems != null){
                for(int i=0; i<goodsItems.size(); i++){
                    if (goodsItems.get(i).getId().equals(id)){
                        goodsItems.remove(i);
                        shop.setGoodsList(goodsItems);
                        shopDao.save(shop);
                        return ResultMessage.SUCCESS;
                    }
                }
            }
        }
        return ResultMessage.FAILED;
    }

    @Override
    public ResultMessage clearGoodsAmount(Long id) {
        GoodsItem goodsItem = goodsItemDao.findById(id).orElse(null);
        if(goodsItem != null){
            goodsItem.setNum(0);
            goodsItemDao.save(goodsItem);
            return ResultMessage.SUCCESS;
        }
        return ResultMessage.FAILED;
    }

    @Override
    public GoodsItem modifyGoodsItem(GoodsItem goodsItem, Long id) {
        GoodsItem oldItem = goodsItemDao.findById(goodsItem.getId()).orElse(null);
        if (oldItem != null){
            if (goodsItem.getAvatar() != null){
                if (oldItem.getAvatar() != null){
                    File file = new File(ITEM_AVATAR_UPLOADED_FOLDER + fileSeparator + id + fileSeparator + oldItem.getAvatar());
                    if (file.exists() && file.isFile()){
                        Boolean re = file.delete();
                    }
                }
                oldItem.setAvatar(goodsItem.getAvatar());
            }
            oldItem.setName(goodsItem.getName());
            oldItem.setNum(goodsItem.getNum());
            oldItem.setDescription(goodsItem.getDescription());
            oldItem.setGoodsType(goodsItem.getGoodsType());
            oldItem.setPrice(goodsItem.getPrice());
            GoodsItem newItem = goodsItemDao.save(oldItem);
            return newItem;
        }
        return null;
    }

    @Override
    public ResultMessage submitShopApply(ShopApply shopApply) {
        Shop shop = shopDao.findByIdentifyCode(shopApply.getIdentifyCode());
        if (shop == null){
            return ResultMessage.FAILED;
        }

        List<ShopApply> list = shopApplyDao.findByIdentifyCode(shopApply.getIdentifyCode());
        for (ShopApply temp : list){
            temp.setApplyState(ApplyState.REJECT);
            shopApplyDao.save(temp);
        }

        shopApply.setShop(shop);
        ShopApply newApply = shopApplyDao.save(shopApply);
        if (newApply != null){
            return ResultMessage.SUCCESS;
        } else {
            return ResultMessage.FAILED;
        }
    }

    @Override
    public ResultMessage depositShopBalance(String identifyCode, Double amount) {
        Shop shop = shopDao.findByIdentifyCode(identifyCode);
        if (shop != null){
            if (shop.getBalance() >= amount){
                shop.setBalance(shop.getBalance() - amount);
                shopDao.save(shop);
                return ResultMessage.SUCCESS;
            }
        }
        return ResultMessage.FAILED;
    }

    @Override
    public Discount addShopDiscount(Discount discount, String identifyCode) {
        Shop shop = shopDao.findByIdentifyCode(identifyCode);
        if (shop != null){
            Discount dis = discountDao.save(discount);
            List<Discount> list = shop.getDiscountList();
            list.add(dis);
            shop.setDiscountList(list);
            shopDao.save(shop);
            return dis;
        }
        return null;
    }

    @Override
    public List<Discount> getAllShopDiscount(String identifyCode) {
        Shop shop = shopDao.findByIdentifyCode(identifyCode);
        if (shop != null){
            return shop.getDiscountList();
        }
        return null;
    }

    @Override
    public Discount deleteShopDiscount(Long id) {
        Discount discount = discountDao.findById(id).orElse(null);
        if (discount != null){
            discount.setDiscountState(DiscountState.FINISHED);
            Discount dis = discountDao.save(discount);
            return dis;
        }
        return null;
    }

    @Override
    public List<Discount> getShopValidDiscount(String identifyCode) {
        Shop shop = shopDao.findByIdentifyCode(identifyCode);
        if (shop != null){
            List<Discount> all = shop.getDiscountList();
            List<Discount> res = new ArrayList<>();
            for (Discount temp: all){
                if (temp.getDiscountState() == DiscountState.UNDERWAY){
                    res.add(temp);
                }
            }
            return res;
        }
        return null;
    }

    @Override
    public List<ShopFinance> getShopFinanceList(String identifyCode) {
        Shop shop = shopDao.findByIdentifyCode(identifyCode);
        if (shop != null){
            List<ShopFinance> financeList = shopFinanceDao.findByShop(shop);
            Collections.sort(financeList, new FinanceComparator());
            return financeList;
        }
        return null;
    }

    @Override
    public Page<Shop> searchShop(String city, ShopType shopType, String keyword,Integer size, Integer currentPage) {
        Pageable pageable = PageRequest.of(currentPage-1, size);
        return shopDao.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicatesList = new ArrayList<>();
            predicatesList.add(
                    criteriaBuilder.and(
                            criteriaBuilder.equal(
                                    root.get("shopState"), 1)));
            if (city != null) {
                predicatesList.add(
                        criteriaBuilder.and(
                                criteriaBuilder.equal(
                                        root.get("city"), city)));
            }
            if (shopType != null) {
                predicatesList.add(
                        criteriaBuilder.and(
                                criteriaBuilder.equal(
                                        root.get("shopType"), shopType.getCode())));
            }
            if (keyword != null && !keyword.equals("")){
                predicatesList.add(
                        criteriaBuilder.and(
                                criteriaBuilder.like(
                                        root.get("name").as(String.class), "%"+keyword+"%")));
            }
            return criteriaBuilder.and(
                    predicatesList.toArray(new Predicate[predicatesList.size()]));
        }, pageable);
    }

    private class FinanceComparator implements Comparator<ShopFinance> {
        @Override
        public int compare(ShopFinance o1, ShopFinance o2) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = sdf.parse(o1.getDate());
                Date date2 = sdf.parse(o2.getDate());
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
