package aster.yummy.vo;

import aster.yummy.enums.DiscountState;
import lombok.Data;

@Data
public class DiscountVO {

    //编号
    private Long id;

    //活动名称
    private String name;

    //满减标准
    private Double spend;

    //满减折扣
    private Double discount;

    //开始时间
    private String startDate;

    //结束时间
    private String endDate;

    //活动状态
    private String discountState;

    public DiscountVO() {
    }

    public DiscountVO(Long id, String name, Double spend, Double discount, String startDate, String endDate, String discountState) {
        this.id = id;
        this.name = name;
        this.spend = spend;
        this.discount = discount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountState = discountState;
    }
}
