package aster.yummy.service.impl;

import aster.yummy.dao.AddressDao;
import aster.yummy.dao.UserDao;
import aster.yummy.dto.LoginUserDTO;
import aster.yummy.enums.AddressState;
import aster.yummy.enums.ResultMessage;
import aster.yummy.enums.UserState;
import aster.yummy.model.Address;
import aster.yummy.model.User;
import aster.yummy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static String fileSeparator = System.getProperty("file.separator");

    private static String USER_AVATAR_UPLOADED_FOLDER = "images" + fileSeparator +"user";

    @Autowired
    private UserDao userDao;

    @Autowired
    private AddressDao addressDao;

    @Override
    public User getUserById(Long id) {
        return userDao.findById(id).orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public ResultMessage modifyUserGeneralInfo(String email, String name, String phone, String avatar) {
        User user = userDao.findByEmail(email);
        if( avatar != null ){
            if (user.getAvatar() != null){
                File file = new File(USER_AVATAR_UPLOADED_FOLDER + fileSeparator + user.getId() + fileSeparator + user.getAvatar());
                if(file.exists() && file.isFile()){
                    Boolean re = file.delete();
                }
            }
            user.setAvatar(avatar);
        }
        user.setName(name);
        user.setPhone(phone);
        userDao.save(user);
        return ResultMessage.SUCCESS;
    }

    @Override
    public List<Address> getUserAddressByEmail(String email) {
        User user = userDao.findByEmail(email);
        if(user != null){
            List<Address> all = user.getAddresses();
            List<Address> validAddress = new ArrayList<>();
            for (int i=0; i<all.size(); i++){
                if(all.get(i).getAddressState() == AddressState.VALID){
                    validAddress.add(all.get(i));
                }
            }
            return validAddress;
        }
        return null;
    }

    @Override
    public List<Address> getUserAddressByEmailAndCity(String email, String city) {
        User user = userDao.findByEmail(email);
        if(user != null){
            List<Address> all = user.getAddresses();
            List<Address> validAddress = new ArrayList<>();
            for (int i=0; i<all.size(); i++){
                if(all.get(i).getAddressState() == AddressState.VALID && all.get(i).getCity().equals(city)){
                    validAddress.add(all.get(i));
                }
            }
            return validAddress;
        }
        return null;
    }

    @Override
    public Address addUserAddress(String email, Address address) {
        User user = userDao.findByEmail(email);
        if(user != null){
            Address newAddress = addressDao.save(address);
            List<Address> list = user.getAddresses();
            list.add(newAddress);
            user.setAddresses(list);
            userDao.save(user);
            return newAddress;
        }
        return null;
    }

    @Override
    public ResultMessage modifyUserAddress(Address address) {
        Address oldAddress = addressDao.findById(address.getId()).orElse(null);
        if(oldAddress != null) {
            oldAddress.setName(address.getName());
            oldAddress.setPhone(address.getPhone());
            oldAddress.setAddressDisp(address.getAddressDisp());
            oldAddress.setRoom(address.getRoom());
            oldAddress.setProvince(address.getProvince());
            oldAddress.setCity(address.getCity());
            addressDao.save(oldAddress);
            return ResultMessage.SUCCESS;
        }
        return ResultMessage.FAILED;
    }

    @Override
    public ResultMessage deleteUserAddress(Long id) {
        Address address = addressDao.findById(id).orElse(null);
        if(address != null) {
            address.setAddressState(AddressState.INVALID);
            addressDao.save(address);
            return ResultMessage.SUCCESS;
        }
        return ResultMessage.FAILED;
    }

    @Override
    public ResultMessage chargeUserBalance(String email, Double charge) {
        User user = userDao.findByEmail(email);
        if (user != null){
            Double balance = user.getBalance();
            balance = balance + charge;
            user.setBalance(balance);
            userDao.save(user);
            return ResultMessage.SUCCESS;
        }
        return ResultMessage.FAILED;
    }

    @Override
    public ResultMessage shutDownUserByEmail(String email) {
        User user = userDao.findByEmail(email);
        if (user != null){
            user.setUserState(UserState.INVALID);
            userDao.save(user);
            return ResultMessage.SUCCESS;
        }
        return ResultMessage.FAILED;
    }
}
