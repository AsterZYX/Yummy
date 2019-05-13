package aster.yummy.enums;

import lombok.Getter;

@Getter
public enum DiscountState {

    NOT_STARTED("未开始", 0), UNDERWAY("进行中", 1), FINISHED("已结束", 2);

    private String state;

    private Integer code;

    DiscountState(String state, Integer code){
        this.state = state;
        this.code = code;
    }
}
