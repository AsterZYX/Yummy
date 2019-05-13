package aster.yummy.vo;

import lombok.Data;

@Data
public class FinanceVO {

    private Long id;

    //条目类型
    private String type;

    //结算日期
    private String date;

    //条目金额
    private Double amount;

    //总余额
    private Double sum;

    public FinanceVO() {
    }

    public FinanceVO(Long id, String type, String date, Double amount, Double sum) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.amount = amount;
        this.sum = sum;
    }
}
