package aster.yummy.controller;

import aster.yummy.enums.DiscountState;
import aster.yummy.enums.ResultMessage;
import aster.yummy.enums.ShopType;
import aster.yummy.model.Discount;
import aster.yummy.model.GoodsItem;
import aster.yummy.model.Shop;
import aster.yummy.model.ShopFinance;
import aster.yummy.service.ShopService;
import aster.yummy.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class ShopController {

    @Autowired
    ShopService shopService;

    @GetMapping("/shop/list")
    public ResultVO<PageVO<ShopCardVO>> getAllShopInfo(@RequestParam("size") Integer size,
                                                       @RequestParam("page") Integer currentPage){
        Page<Shop> shopPage = shopService.getAllShops(size, currentPage);
        List<Shop> list = shopPage.getContent();
        List<ShopCardVO> voList = new ArrayList<>();
        for(Shop shop: list){
            voList.add(new ShopCardVO(shop.getIdentifyCode(), shop.getName(), shop.getAddress(), shop.getShopType().getType(), shop.getAvatar()));
        }
        Integer empty = 0;
        if(shopPage.getNumberOfElements()%4 != 0){
            empty = 4 - (shopPage.getNumberOfElements()%4);
        }
        PageVO<ShopCardVO> shopCardVOS =  new PageVO<>(currentPage, shopPage.getSize(), shopPage.getTotalPages(), shopPage.getTotalElements(), voList, empty);
        return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), shopCardVOS);
    }

    @GetMapping("/shop/list/city")
    public ResultVO<PageVO<ShopCardVO>> getAllShopInfo(@RequestParam("size") Integer size,
                                                       @RequestParam("page") Integer currentPage,
                                                       @RequestParam("city") String city){
        Page<Shop> shopPage = shopService.getShopsByCity(size, currentPage, city);
        List<Shop> list = shopPage.getContent();
        List<ShopCardVO> voList = new ArrayList<>();
        for(Shop shop: list){
            voList.add(new ShopCardVO(shop.getIdentifyCode(), shop.getName(), shop.getAddress(), shop.getShopType().getType(), shop.getAvatar()));
        }
        Integer empty = 0;
        if(shopPage.getNumberOfElements()%4 != 0){
            empty = 4 - (shopPage.getNumberOfElements()%4);
        }
        PageVO<ShopCardVO> shopCardVOS =  new PageVO<>(currentPage, shopPage.getSize(), shopPage.getTotalPages(), shopPage.getTotalElements(), voList, empty);
        return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), shopCardVOS);
    }

    @GetMapping("/shop/search")
    public ResultVO<PageVO<ShopCardVO>> getAllShopInfo(@RequestParam("size") Integer size,
                                                       @RequestParam("page") Integer currentPage,
                                                       @RequestParam("city") String city,
                                                       @RequestParam(name = "shopType", required = false) String shopType,
                                                       @RequestParam(name = "keyword", required = false) String keyword){
        Page<Shop> shopPage;
        if (shopType != null) {
            shopPage = shopService.searchShop(city, ShopType.values()[Integer.parseInt(shopType)], keyword, size, currentPage);
        }else {
            shopPage = shopService.searchShop(city, null, keyword, size, currentPage);
        }
        List<Shop> list = shopPage.getContent();
        List<ShopCardVO> voList = new ArrayList<>();
        for(Shop shop: list){
            voList.add(new ShopCardVO(shop.getIdentifyCode(), shop.getName(), shop.getAddress(), shop.getShopType().getType(), shop.getAvatar()));
        }
        Integer empty = 0;
        if(shopPage.getNumberOfElements()%4 != 0){
            empty = 4 - (shopPage.getNumberOfElements()%4);
        }
        PageVO<ShopCardVO> shopCardVOS =  new PageVO<>(currentPage, shopPage.getSize(), shopPage.getTotalPages(), shopPage.getTotalElements(), voList, empty);
        return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), shopCardVOS);
    }

    @GetMapping("/shop/{identifyCode}")
    public ResultVO<ShopVO> getShopInfoByIdentifyCode(@PathVariable("identifyCode") String code){
        Shop shop = shopService.getShopByIdentifyCode(code);
        ShopVO shopVO = new ShopVO(shop.getIdentifyCode(), shop.getName(), shop.getAddress(), shop.getShopType().getType(), shop.getShopState(), shop.getAvatar(), shop.getGoodsList(), shop.getTypes(), shop.getBalance(), shop.getProvince(), shop.getCity());
        return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), shopVO);
    }

    @PostMapping("/type/add")
    public ResultVO<String> addItemType(@RequestParam("type") String type,
                                        @RequestParam("identifyCode") String code){
        ResultMessage re = shopService.addItemType(code, type);
        return new ResultVO<>(re.getCode(), re.getValue(), null);
    }

    @PostMapping("/type/delete")
    public ResultVO<String> deleteItemType(@RequestParam("type") String type,
                                           @RequestParam("identifyCode") String code){
        ResultMessage re = shopService.deleteItemType(code, type);
        return new ResultVO<>(re.getCode(), re.getValue(), null);
    }

    @PostMapping("/item/delete")
    public ResultVO<String> deleteGoodsItem(@RequestParam("identifyCode") String identifyCode,
                                            @RequestParam("id") Long id){
        ResultMessage re = shopService.deleteGoodsItem(identifyCode, id);
        return new ResultVO<>(re.getCode(), re.getValue(), null);
    }

    @PostMapping("/item/clear")
    public ResultVO<String> clearGoodsAmount(@RequestParam("id") Long id){
        ResultMessage re = shopService.clearGoodsAmount(id);
        return new ResultVO<>(re.getCode(), re.getValue(), null);
    }

    @PostMapping("/shop/deposit")
    public ResultVO<String> depositShopBalance(@RequestParam("identifyCode") String code,
                                               @RequestParam("amount") Double amount){
        ResultMessage re = shopService.depositShopBalance(code, amount);
        return new ResultVO<>(re.getCode(), re.getValue(), null);
    }

    @PostMapping("/shop/discount/{identifyCode}")
    public ResultVO<DiscountVO> addShopDiscount(@RequestBody Discount discount,
                                              @PathVariable String identifyCode) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = sdf.parse(discount.getStartDate());
        Date end = sdf.parse(discount.getEndDate());
        if (end.before(new Date())){
            discount.setDiscountState(DiscountState.FINISHED);
        } else if (start.after(new Date())){
            discount.setDiscountState(DiscountState.NOT_STARTED);
        } else {
            discount.setDiscountState(DiscountState.UNDERWAY);
        }
        Discount dis = shopService.addShopDiscount(discount, identifyCode);
        if (dis != null){
            DiscountVO vo = new DiscountVO(dis.getId(), dis.getName(), dis.getSpend(), dis.getDiscount(), dis.getStartDate(), dis.getEndDate(), dis.getDiscountState().getState());
            return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), vo);
        } else {
            return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), null);
        }
    }

    @PostMapping("/shop/discount/list")
    public ResultVO<List<DiscountVO>> getAllShopDiscount(@RequestParam("identifyCode") String identifyCode){
        List<Discount> list = shopService.getAllShopDiscount(identifyCode);
        List<DiscountVO> result = new ArrayList<>();
        for (Discount discount: list){
            DiscountVO vo = new DiscountVO(discount.getId(), discount.getName(), discount.getSpend(), discount.getDiscount(), discount.getStartDate(), discount.getEndDate(), discount.getDiscountState().getState());
            result.add(vo);
        }
        return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), result);
    }

    @PostMapping("/shop/discount/delete")
    public ResultVO<DiscountVO> deleteShopDiscount(@RequestParam("id") Long id){
        Discount discount = shopService.deleteShopDiscount(id);
        if (discount != null){
            DiscountVO vo = new DiscountVO(discount.getId(), discount.getName(), discount.getSpend(), discount.getDiscount(), discount.getStartDate(), discount.getEndDate(), discount.getDiscountState().getState());
            return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), vo);
        }
        return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), null);
    }

    @GetMapping("/shop/discount/valid")
    public ResultVO<List<DiscountVO>> getShopValidDiscount(@RequestParam("identifyCode") String identifyCode){
        List<Discount> list = shopService.getShopValidDiscount(identifyCode);
        List<DiscountVO> result = new ArrayList<>();
        for (Discount discount: list){
            DiscountVO vo = new DiscountVO(discount.getId(), discount.getName(), discount.getSpend(), discount.getDiscount(), discount.getStartDate(), discount.getEndDate(), discount.getDiscountState().getState());
            result.add(vo);
        }
        return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), result);
    }

    @GetMapping("/shop/finance/list")
    public ResultVO<List<FinanceVO>> getShopFinanceListByShop(@RequestParam("identifyCode") String identifyCode){
        List<ShopFinance> list = shopService.getShopFinanceList(identifyCode);
        List<FinanceVO> res = new ArrayList<>();
        if (list != null){
            for (ShopFinance finance: list){
                FinanceVO vo = new FinanceVO(finance.getId(), finance.getType().getType(), finance.getDate(), finance.getAmount(), finance.getSum());
                res.add(vo);
            }
            return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), res);
        }
        return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), null);
    }
}
