package aster.yummy.dao;

import aster.yummy.enums.DiscountState;
import aster.yummy.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountDao extends JpaRepository<Discount, Long> {

    public List<Discount> findByDiscountState(DiscountState discountState);
}
