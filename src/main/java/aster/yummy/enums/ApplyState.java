package aster.yummy.enums;

import lombok.Getter;

@Getter
public enum ApplyState {

    SUBMITTED("审核中", 0), PASS("审核通过", 1), REJECT("审核未通过", 2);

    private String state;

    private Integer code;

    ApplyState(String state, Integer code){
        this.state = state;
        this.code = code;
    }

}
