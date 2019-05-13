package aster.yummy.enums;

import lombok.Getter;

@Getter
public enum UserLevel {
    LEVEL_ONE("普通会员",1),LEVEL_TWO("白金会员",2),LEVEL_THREE("钻石贵宾",3);

    private String state;

    private Integer code;

    UserLevel(String state, Integer code){
        this.state = state;
        this.code = code;
    }
}
