package aster.yummy.service.impl;

import aster.yummy.dao.AdminDao;
import aster.yummy.dao.ShopDao;
import aster.yummy.dao.UserDao;
import aster.yummy.dto.LoginShopDTO;
import aster.yummy.dto.LoginUserDTO;
import aster.yummy.enums.ResultMessage;
import aster.yummy.enums.ShopState;
import aster.yummy.enums.UserState;
import aster.yummy.model.Admin;
import aster.yummy.model.Shop;
import aster.yummy.model.User;
import aster.yummy.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {

    private UserDao userDao;

    private AdminDao adminDao;

    private ShopDao shopDao;

    private static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3",
            "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

    @Autowired
    private LoginServiceImpl(UserDao userDao, AdminDao adminDao, ShopDao shopDao){
        this.userDao = userDao;
        this.adminDao = adminDao;
        this.shopDao = shopDao;
        this.initialDefaultAdmin();
    }

    private void initialDefaultAdmin(){
        if(adminDao.count() == 0){
            Admin admin = new Admin("2019207", "rootAdmin");
            adminDao.save(admin);
        }
    }

    @Override
    public LoginUserDTO loginUser(String email, String password) {
        User user = userDao.findByEmail(email);
        if(user == null) {
            return new LoginUserDTO(ResultMessage.NOT_EXIST, null);
        }
        else {
            if(user.getUserState() == UserState.VALID) {
                if (user.getPassword().equals(password)) {
                    return new LoginUserDTO(ResultMessage.SUCCESS, user);
                } else {
                    return new LoginUserDTO(ResultMessage.FAILED, null);
                }
            }
            else{
                return new LoginUserDTO(ResultMessage.NOT_EXIST, null);
            }
        }
    }

    @Override
    public ResultMessage registerUser(User user) {
        if(userDao.findByEmail(user.getEmail()) == null){
            User newUser = userDao.save(user);
            if(newUser != null){
                return ResultMessage.SUCCESS;
            }
            else {
                return ResultMessage.FAILED;
            }
        }
        else {
            return ResultMessage.EAMIL_EXIST;
        }
    }

    @Override
    public LoginShopDTO registerShop(Shop shop) {
        String shopId = generateShortUUId();
        while(shopDao.findByIdentifyCode(shopId) != null){
            shopId = generateShortUUId();
        }
        shop.setIdentifyCode(shopId);
        Shop newShop = shopDao.save(shop);
        if(newShop != null){
            return new LoginShopDTO(ResultMessage.SUCCESS, newShop);
        }
        else {
            return new LoginShopDTO(ResultMessage.FAILED, null);
        }
    }

    @Override
    public LoginShopDTO loginShop(String identifyCode, String password) {
        Shop shop = shopDao.findByIdentifyCode(identifyCode);
        if(shop != null && shop.getShopState() == ShopState.PASS){
            if(shop.getPassword().equals(password)){
                return  new LoginShopDTO(ResultMessage.SUCCESS, shop);
            }
            else {
                return new LoginShopDTO(ResultMessage.FAILED, null);
            }
        }
        else {
            return new LoginShopDTO(ResultMessage.NOT_EXIST, null);
        }
    }

    private String generateShortUUId(){
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 7; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }

    @Override
    public ResultMessage loginAdmin(String id, String password) {
        Admin admin = adminDao.findByIdentifyCode(id);
        if(admin != null){
            if(admin.getPassword().equals(password)){
                return ResultMessage.SUCCESS;
            }
            else {
                return ResultMessage.FAILED;
            }
        }
        else {
            return ResultMessage.NOT_EXIST;
        }
    }
}
