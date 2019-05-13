package aster.yummy.service;

import aster.yummy.dto.LoginShopDTO;
import aster.yummy.dto.LoginUserDTO;
import aster.yummy.enums.ResultMessage;
import aster.yummy.model.Shop;
import aster.yummy.model.User;

public interface LoginService {

    public LoginUserDTO loginUser(String email, String password);

    public ResultMessage registerUser(User user);

    public LoginShopDTO registerShop(Shop shop);

    public LoginShopDTO loginShop(String identifyCode, String password);

    public ResultMessage loginAdmin(String id, String password);
}
