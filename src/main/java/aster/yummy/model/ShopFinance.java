package aster.yummy.model;

import aster.yummy.enums.FinanceType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class ShopFinance {

    //编号
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //店铺编码
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "shop_id", referencedColumnName = "id")
    private Shop shop;

    //条目类型
    private FinanceType type;

    //结算日期
    private String date;

    //条目金额
    private Double amount;

    //总余额
    private Double sum;

    public ShopFinance() {
    }

    public ShopFinance(Shop shop, FinanceType type, String date, Double amount, Double sum) {
        this.shop = shop;
        this.type = type;
        this.date = date;
        this.amount = amount;
        this.sum = sum;
    }
}
