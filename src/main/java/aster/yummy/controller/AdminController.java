package aster.yummy.controller;

import aster.yummy.dto.FinanceDTO;
import aster.yummy.dto.ShopDataDTO;
import aster.yummy.dto.TimeDataDTO;
import aster.yummy.enums.ResultMessage;
import aster.yummy.enums.ShopState;
import aster.yummy.model.Shop;
import aster.yummy.model.ShopApply;
import aster.yummy.service.AdminService;
import aster.yummy.service.ShopService;
import aster.yummy.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    ShopService shopService;

    @GetMapping("/apply/list")
    public ResultVO<List<CompareApplyVO>> getAllSubmittedShopApply(){
        List<ShopApply> list = adminService.getAllSubmittedShopApply();
        List<CompareApplyVO> result = new ArrayList<>();
        if (list != null){
            for (ShopApply temp: list){
                ShopApplyVO vo = new ShopApplyVO(temp.getId(), temp.getIdentifyCode(), temp.getName(), temp.getAddress(), temp.getShopType().getType(), temp.getApplyState(), temp.getAvatar());
                Shop shop = shopService.getShopByIdentifyCode(temp.getIdentifyCode());
                ShopCardVO cardVO = new ShopCardVO(shop.getIdentifyCode(), shop.getName(), shop.getAddress(), shop.getShopType().getType(), shop.getAvatar());
                CompareApplyVO compareApplyVO = new CompareApplyVO(vo, cardVO);
                result.add(compareApplyVO);
            }
        }
        return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), result);
    }

    @PostMapping("/apply/examine")
    public ResultVO<String> examineShopApply(@RequestParam("id") Long id,
                                             @RequestParam("res") Boolean res){
        ResultMessage re = adminService.examineShopApply(id, res);
        return new ResultVO<>(re.getCode(), re.getValue(), null);
    }

    @GetMapping("/apply/sign/list")
    public ResultVO<List<ShopVO>> getAllSignApply(){
        List<Shop> shopList = shopService.getShopByState(ShopState.SUBMITTED);
        List<ShopVO> res = new ArrayList<>();
        if (shopList != null){
            for (Shop shop: shopList){
                ShopVO shopVO = new ShopVO(shop.getIdentifyCode(), shop.getName(), shop.getAddress(), shop.getShopType().getType(), shop.getShopState(), shop.getAvatar(), shop.getGoodsList(), shop.getTypes(), shop.getBalance(), shop.getProvince(), shop.getCity());
                res.add(shopVO);
            }
            return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), res);
        }
        return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), null);
    }

    @PostMapping("/apply/sign/examine")
    public ResultVO<String> examineShopApply(@RequestParam("code") String identifyCode,
                                             @RequestParam("res") Boolean res){
        ResultMessage re = adminService.examineShopSignApply(identifyCode, res);
        return new ResultVO<>(re.getCode(), re.getValue(), null);
    }

    @GetMapping("/user/sign/data")
    public ResultVO<List<TimeDataDTO>> getUserSignData(){
        List<TimeDataDTO> list = adminService.getUserSignData();
        return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), list);
    }

    @GetMapping("/shop/type/data")
    public ResultVO<List<ShopDataDTO>> getShopTypeData(){
        List<ShopDataDTO> list = adminService.getShopTypeData();
        return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), list);
    }

    @GetMapping("/shop/city/data")
    public ResultVO<List<ShopDataDTO>> getShopCityData(){
        List<ShopDataDTO> list = adminService.getShopCityData();
        return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), list);
    }

    @GetMapping("/shop/sign/data")
    public ResultVO<List<TimeDataDTO>> getShopSignData(){
        List<TimeDataDTO> list = adminService.getShopSignData();
        return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), list);
    }

    @GetMapping("/platform/finance/data")
    public ResultVO<FinanceDTO> getPlatformFinanceData(){
        FinanceDTO dto = adminService.getFinanceData();
        return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), dto);
    }
}
