package aster.yummy.dao;

import aster.yummy.model.Shop;
import aster.yummy.model.ShopFinance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopFinanceDao extends JpaRepository<ShopFinance, Long> {

    public List<ShopFinance> findByShop(Shop shop);

}
