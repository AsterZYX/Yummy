package aster.yummy.enums;

import lombok.Getter;

@Getter
public enum  FinanceType {

    EXPENSE("支出", 0), INCOME("收入", 1);
    private String type;

    private Integer code;

    FinanceType(String type, Integer code){
        this.type = type;
        this.code = code;
    }

}
