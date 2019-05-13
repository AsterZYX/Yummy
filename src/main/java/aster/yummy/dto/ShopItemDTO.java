package aster.yummy.dto;

import aster.yummy.model.GoodsItem;
import aster.yummy.model.Shop;
import lombok.Data;

@Data
public class ShopItemDTO {

    Shop shop;

    GoodsItem goodsItem;

    public ShopItemDTO() {
    }

    public ShopItemDTO(Shop shop, GoodsItem goodsItem) {
        this.shop = shop;
        this.goodsItem = goodsItem;
    }
}
