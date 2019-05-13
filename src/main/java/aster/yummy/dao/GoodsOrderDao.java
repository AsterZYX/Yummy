package aster.yummy.dao;

import aster.yummy.enums.OrderState;
import aster.yummy.model.GoodsOrder;
import aster.yummy.model.Shop;
import aster.yummy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface GoodsOrderDao extends JpaRepository<GoodsOrder, Long>, JpaSpecificationExecutor<GoodsOrder> {

    public List<GoodsOrder> findByUserAndOrderState(User user, OrderState orderState);

    public List<GoodsOrder> findByShopAndOrderState(Shop shop, OrderState orderState);

    public List<GoodsOrder> findByShop(Shop shop);

}
