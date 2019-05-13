package aster.yummy.dao;

import aster.yummy.enums.ShopState;
import aster.yummy.enums.ShopType;
import aster.yummy.model.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ShopDao extends JpaRepository<Shop, Long>, JpaSpecificationExecutor<Shop> {

    public Shop findByIdentifyCode(String identifyCode);

    public Page<Shop> findByCityAndShopState(String city, ShopState shopState, Pageable pageable);

    public List<Shop> findByShopState(ShopState shopState);

    public List<Shop> findByShopType(ShopType shopType);

    public List<Shop> findByDate(String date);

    public List<Shop> findByDateLessThanEqual(String date);
}
