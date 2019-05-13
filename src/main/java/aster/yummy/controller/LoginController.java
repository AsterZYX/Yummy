package aster.yummy.controller;

import aster.yummy.dto.CodeDTO;
import aster.yummy.dto.LoginShopDTO;
import aster.yummy.dto.LoginUserDTO;
import aster.yummy.enums.ResultMessage;
import aster.yummy.model.Address;
import aster.yummy.model.GoodsItem;
import aster.yummy.model.Shop;
import aster.yummy.model.User;
import aster.yummy.service.CodeService;
import aster.yummy.service.LoginService;
import aster.yummy.service.UserService;
import aster.yummy.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
public class LoginController {

    @Autowired
    private CodeService codeService;

    @Autowired
    private LoginService loginService;

    @GetMapping("/code")
    public void getValidateCode(@RequestParam(value = "codeName", required = true) String name,
                                HttpServletRequest request, HttpServletResponse response){
        try {
            response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);

            CodeDTO codeDTO = codeService.getCodePic();

            HttpSession session = request.getSession();
            session.setAttribute(name, codeDTO.getCode());

            ImageIO.write(codeDTO.getImage(), "JPEG", response.getOutputStream());

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @GetMapping("/code/check")
    public ResultVO<Boolean> checkValidateCode(@RequestParam(value = "validateCode", required = true) String validateCode,
                                               @RequestParam(value = "codeName", required = true) String codeName,
                                               HttpServletRequest request){
        HttpSession session = request.getSession();
        String code = (String) session.getAttribute(codeName);
        System.out.println(validateCode.equals(code)+" "+validateCode+" "+code);
        return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), validateCode.equals(code));
    }

    @PostMapping("/login/user")
    public ResultVO<UserVO> loginUser(@RequestParam(value = "email", required = true) String email,
                                      @RequestParam(value = "password", required = true) String password){
        LoginUserDTO dto = loginService.loginUser(email, password);
        if(dto != null){
            if(dto.getRe() == ResultMessage.SUCCESS) {
                User user = dto.getUser();
                UserVO userVO = new UserVO(user.getId(), user.getEmail(), user.getPhone(), user.getName(), user.getAvatar(), user.getUserLevel(), user.getBalance());
                return new ResultVO<>(dto.getRe().getCode(), dto.getRe().getValue(), userVO);
            }
            else{
                return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), null);
            }
        }
        return new ResultVO<>(ResultMessage.NETWORK_ERROR.getCode(), ResultMessage.NETWORK_ERROR.getValue(), null);
    }

    @PostMapping("/sign/user")
    public ResultVO<String> registerUser(@RequestBody UserSignVO userSignVO){
        User user = new User(userSignVO.getEmail(), userSignVO.getPassword(), userSignVO.getPhone(), userSignVO.getName());
        ResultMessage re = loginService.registerUser(user);
        return new ResultVO<>(re.getCode(), re.getValue(), null);
    }

    @PostMapping("/sign/shop")
    public ResultVO<String> registerShop(@RequestBody ShopSignVO shopSignVO){
        Shop shop = new Shop(shopSignVO.getName(), shopSignVO.getPassword(), shopSignVO.getAddress(), shopSignVO.getShopType(), shopSignVO.getProvince(), shopSignVO.getCity());
        LoginShopDTO dto = loginService.registerShop(shop);
        if(dto != null) {
            if(dto.getShop() != null) {
                return new ResultVO<>(dto.getRe().getCode(), dto.getRe().getValue(), dto.getShop().getIdentifyCode());
            }
            else {
                return new ResultVO<>(dto.getRe().getCode(), dto.getRe().getValue(), null);
            }
        }
        else {
            return new ResultVO<>(ResultMessage.NETWORK_ERROR.getCode(), ResultMessage.NETWORK_ERROR.getValue(), null);
        }
    }

    @PostMapping("/login/shop")
    public ResultVO<ShopVO> loginShop(@RequestParam(value = "identifyCode", required = true) String identifyCode,
                                      @RequestParam(value = "password", required = true) String password){
        LoginShopDTO dto = loginService.loginShop(identifyCode, password);
        if(dto != null){
            Shop shop = dto.getShop();
            if(shop != null) {
                List<GoodsItem> list = shop.getGoodsList();
                ShopVO shopVO = new ShopVO(shop.getIdentifyCode(), shop.getName(), shop.getAddress(), shop.getShopType().getType(), shop.getShopState(), shop.getAvatar(), list, shop.getTypes(), shop.getBalance(), shop.getProvince(), shop.getCity());
                return new ResultVO<>(dto.getRe().getCode(), dto.getRe().getValue(), shopVO);
            }
            else {
                return new ResultVO<>(dto.getRe().getCode(), dto.getRe().getValue(), null);
            }
        }
        return new ResultVO<>(ResultMessage.NETWORK_ERROR.getCode(), ResultMessage.NETWORK_ERROR.getValue(), null);
    }

    @PostMapping("/login/admin")
    public ResultVO<String> loginAdmin(@RequestParam(value = "id", required = true) String identifyCode,
                                       @RequestParam(value = "password", required = true) String password){
        ResultMessage re = loginService.loginAdmin(identifyCode, password);
        if(re != null){
            return new ResultVO<>(re.getCode(), re.getValue(), null);
        }
        else {
            return new ResultVO<>(ResultMessage.NETWORK_ERROR.getCode(), ResultMessage.NETWORK_ERROR.getValue(), null);
        }
    }

}
