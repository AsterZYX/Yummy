package aster.yummy.dao;

import aster.yummy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDao extends JpaRepository<User, Long> {

    public User findByEmail(String email);

    public List<User> findByDate(String data);

    public List<User> findByDateLessThanEqual(String date);

}
