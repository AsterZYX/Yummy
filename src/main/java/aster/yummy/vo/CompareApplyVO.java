package aster.yummy.vo;

import lombok.Data;

@Data
public class CompareApplyVO {

    ShopApplyVO newInfo;

    ShopCardVO oldInfo;

    public CompareApplyVO() {
    }

    public CompareApplyVO(ShopApplyVO newInfo, ShopCardVO oldInfo) {
        this.newInfo = newInfo;
        this.oldInfo = oldInfo;
    }
}
