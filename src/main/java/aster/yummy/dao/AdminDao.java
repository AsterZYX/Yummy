package aster.yummy.dao;

import aster.yummy.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminDao extends JpaRepository<Admin, Long> {

    public Admin findByIdentifyCode(String identifyCode);
}
