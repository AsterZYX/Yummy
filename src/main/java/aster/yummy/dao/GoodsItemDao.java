package aster.yummy.dao;

import aster.yummy.model.GoodsItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsItemDao extends JpaRepository<GoodsItem, Long> {
}
