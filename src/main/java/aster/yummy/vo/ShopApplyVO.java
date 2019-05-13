package aster.yummy.vo;

import aster.yummy.enums.ApplyState;
import aster.yummy.enums.ShopType;
import lombok.Data;

@Data
public class ShopApplyVO {

    //编号
    private Long id;

    //识别码
    private String identifyCode;

    //餐厅名称
    private String name;

    //餐厅地址
    private String address;

    //餐厅类型
    private String shopType;

    //餐厅状态
    private ApplyState applyState;

    //餐厅头像
    private String avatar;

    public ShopApplyVO() {
    }

    public ShopApplyVO(Long id, String identifyCode, String name, String address, String shopType, ApplyState applyState, String avatar) {
        this.id = id;
        this.identifyCode = identifyCode;
        this.name = name;
        this.address = address;
        this.shopType = shopType;
        this.applyState = applyState;
        this.avatar = avatar;
    }
}
