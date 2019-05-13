package aster.yummy.enums;

import lombok.Getter;

@Getter
public enum ShopType {

    CATE("美食",0), FAST_FOOD("快餐便当",1), SPECICAL_CUISINES("特色菜系",2), EXOTIC_CUISINE("异国料理",3), SNACK("小吃夜宵",4), DESSERTS("甜品饮品", 5), FRUIT_AND_VEG("果蔬生鲜",6), STORE("商店超市",7),
            PLANTS("鲜花绿植",8), MEDICINE("医药健康",9);

    private String type;

    private Integer code;

    ShopType(String type, Integer code){
        this.type = type;
        this.code = code;
    }
}
