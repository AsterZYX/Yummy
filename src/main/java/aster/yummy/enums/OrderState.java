package aster.yummy.enums;

import lombok.Getter;

@Getter
public enum OrderState {

    INITIAL("未支付",0), PAID("已支付",1), DISTRIBUTED("配送中",2), CONFIRMED("已完成",3),EXPIRED("已过期",4), CANCELED("已退订", 5), REFUSED("已拒绝", 6), WAIT_CANCEL("待退订", 7), ACCEPT("已接单", 8);

    private String state;

    private Integer code;

    OrderState(String state, Integer code){
        this.state = state;
        this.code = code;
    }

}
