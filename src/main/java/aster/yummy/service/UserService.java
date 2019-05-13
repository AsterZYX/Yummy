package aster.yummy.service;

import aster.yummy.dto.LoginUserDTO;
import aster.yummy.enums.ResultMessage;
import aster.yummy.model.Address;
import aster.yummy.model.User;

import java.util.List;

public interface UserService {

    public User getUserById(Long id);

    public User getUserByEmail(String email);

    public ResultMessage modifyUserGeneralInfo(String email, String name, String phone, String avatar);

    public List<Address> getUserAddressByEmail(String email);

    public List<Address> getUserAddressByEmailAndCity(String email, String city);

    public Address addUserAddress(String email, Address address);

    public ResultMessage modifyUserAddress(Address address);

    public ResultMessage deleteUserAddress(Long id);

    public ResultMessage chargeUserBalance(String email, Double charge);

    public ResultMessage shutDownUserByEmail(String email);

}
