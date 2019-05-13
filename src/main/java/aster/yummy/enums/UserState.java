package aster.yummy.enums;

import lombok.Getter;

@Getter
public enum UserState {
    VALID("有效的", 0), INVALID("已注销的", 1);

    private String state;

    private Integer code;

    UserState(String state, Integer code){
        this.state = state;
        this.code = code;
    }
}
