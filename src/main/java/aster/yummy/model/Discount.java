package aster.yummy.model;

import aster.yummy.enums.DiscountState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Discount {

    //编号
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private DiscountState discountState;

    public Discount() {
    }

    public Discount(String name, Double spend, Double discount, String startDate, String endDate, DiscountState discountState) {
        this.name = name;
        this.spend = spend;
        this.discount = discount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountState = discountState;
    }

}
