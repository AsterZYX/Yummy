package aster.yummy.enums;

import lombok.Getter;

@Getter
public enum AddressState {
    VALID("有效的", 0), INVALID("已删除的", 1);

    private String state;

    private Integer code;

    AddressState(String state, Integer code){
        this.state = state;
        this.code = code;
    }
}
