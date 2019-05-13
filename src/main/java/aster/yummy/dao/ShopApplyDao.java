package aster.yummy.dao;

import aster.yummy.enums.ApplyState;
import aster.yummy.model.ShopApply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopApplyDao extends JpaRepository<ShopApply, Long> {

    public List<ShopApply> findByApplyState(ApplyState applyState);

    public List<ShopApply> findByIdentifyCode(String identifyCode);
}
