package aster.yummy.dto;

import aster.yummy.enums.ResultMessage;
import aster.yummy.model.Shop;
import lombok.Data;

@Data
public class LoginShopDTO {

    private ResultMessage re;

    private Shop shop;

    public LoginShopDTO(ResultMessage re, Shop shop) {
        this.re = re;
        this.shop = shop;
    }
}
