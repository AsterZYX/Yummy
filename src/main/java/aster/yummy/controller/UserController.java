package aster.yummy.controller;

import aster.yummy.enums.AddressState;
import aster.yummy.enums.ResultMessage;
import aster.yummy.model.Address;
import aster.yummy.model.User;
import aster.yummy.service.UserService;
import aster.yummy.vo.AddressVO;
import aster.yummy.vo.ResultVO;
import aster.yummy.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/user/info")
    public ResultVO<UserVO> getUserInfoByEmail(@RequestParam("email") String email){
        User user = userService.getUserByEmail(email);
        if(user != null){
            UserVO vo = new UserVO(user.getId(), user.getEmail(), user.getPhone(), user.getName(), user.getUserLevel(), user.getAvatar(), user.getAddresses(), user.getBalance());
            return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), vo );
        }
        return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), null);
    }

    @GetMapping("/user/address")
    public ResultVO<List<Address>> getUserAddressByEmail(@RequestParam("email") String email){
        List<Address> list = userService.getUserAddressByEmail(email);
        if(list != null){
            return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), list);
        }
        return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), new ArrayList<>());
    }

    @GetMapping("/user/address/city")
    public ResultVO<List<Address>> getUserAddressByEmailAndCity(@RequestParam("email") String email,
                                                                @RequestParam("city") String city){
        List<Address> list = userService.getUserAddressByEmailAndCity(email, city);
        if(list != null){
            return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), list);
        }
        return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), new ArrayList<>());
    }

    @PostMapping("/address/{email}")
    public ResultVO<Address> addUserAddress(@PathVariable String email, @RequestBody AddressVO addressVO){
        System.out.println(addressVO.getProvince()+ " "+addressVO.getCity());
        Address address = new Address(addressVO.getName(), addressVO.getPhone(), addressVO.getAddressDisp(), addressVO.getRoom(), AddressState.VALID, addressVO.getProvince(), addressVO.getCity());
        Address newAddress = userService.addUserAddress(email, address);
        if( newAddress != null && newAddress.getId() != null){
            return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), newAddress);
        }
        return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), null);
    }

    @PostMapping("/address/modify")
    public ResultVO<String> modifyUserAddress(@RequestBody AddressVO addressVO){
        Address address = new Address(addressVO.getId(), addressVO.getName(), addressVO.getPhone(), addressVO.getAddressDisp(), addressVO.getRoom(), addressVO.getProvince(), addressVO.getCity());
        ResultMessage re = userService.modifyUserAddress(address);
        return new ResultVO<>(re.getCode(), re.getValue(), null);
    }

    @PostMapping("/address/delete")
    public ResultVO<String> deleteUserAddress(@RequestParam("id") Long id){
        ResultMessage re = userService.deleteUserAddress(id);
        return new ResultVO<>(re.getCode(), re.getValue(), null);
    }

    @PostMapping("/user/balance/charge")
    public ResultVO<String> chargeUserBalance(@RequestParam("email") String email,
                                              @RequestParam("charge") Double charge){
        ResultMessage re = userService.chargeUserBalance(email, charge);
        return new ResultVO<>(re.getCode(), re.getValue(), null);
    }

    @PostMapping("/user/cancel")
    public ResultVO<String> shutDownUser(@RequestParam("email") String email){
        ResultMessage re = userService.shutDownUserByEmail(email);
        return new ResultVO<>(re.getCode(), re.getValue(), null);
    }

}
