package aster.yummy.service;

import aster.yummy.dto.ShopItemDTO;
import aster.yummy.enums.ResultMessage;
import aster.yummy.enums.ShopState;
import aster.yummy.enums.ShopType;
import aster.yummy.model.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ShopService {

    public Page<Shop> getAllShops(Integer size, Integer currentPage);

    public Page<Shop> getShopsByCity(Integer size, Integer currentPage, String city);

    public List<Shop> getShopByState(ShopState shopState);

    public Shop getShopByIdentifyCode(String identifyCode);

    public ShopItemDTO addGoodsItem(String identifyCode, GoodsItem goodsItem);

    public ResultMessage addItemType(String identifyCode, String itemType);

    public GoodsItem getItemById(Long id);

    public ResultMessage updateItemAvatar(Long id, String name);

    public ResultMessage deleteItemType(String identifyCode, String itemType);

    public ResultMessage deleteGoodsItem(String identifyCode, Long id);

    public ResultMessage clearGoodsAmount(Long id);

    public GoodsItem modifyGoodsItem(GoodsItem goodsItem, Long id);

    public ResultMessage submitShopApply(ShopApply shopApply);

    public ResultMessage depositShopBalance(String identifyCode, Double amount);

    public Discount addShopDiscount(Discount discount, String identifyCode);

    public List<Discount> getAllShopDiscount(String identifyCode);

    public Discount deleteShopDiscount(Long id);

    public List<Discount> getShopValidDiscount(String identifyCode);

    public List<ShopFinance> getShopFinanceList(String identifyCode);

    public Page<Shop> searchShop(String city, ShopType shopType, String keyword,Integer size, Integer currentPage);
}
