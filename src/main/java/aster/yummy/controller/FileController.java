package aster.yummy.controller;

import aster.yummy.dto.ShopItemDTO;
import aster.yummy.enums.ApplyState;
import aster.yummy.enums.ResultMessage;
import aster.yummy.enums.ShopType;
import aster.yummy.model.GoodsItem;
import aster.yummy.model.Shop;
import aster.yummy.model.ShopApply;
import aster.yummy.service.ShopService;
import aster.yummy.service.UserService;
import aster.yummy.vo.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
public class FileController {

    private static String fileSeparator = System.getProperty("file.separator");

    private static String UPLOADED_FLODER = "images";

    private static String USER_AVATAR_UPLOADED_FOLDER = "images" + fileSeparator +"user";

    private static String SHOP_AVATAR_UPLOADED_FOLDER = "images" + fileSeparator + "shop";

    private static String ITEM_AVATAR_UPLOADED_FOLDER = "images" + fileSeparator + "item";

    @Autowired
    ShopService shopService;

    @Autowired
    UserService userService;

    @PostMapping("/upload/item")
    public ResultVO<GoodsItem> addDishItem(@RequestParam("file") MultipartFile file, GoodsItemVO goodsItemVO){
        try {
            File dir = new File(UPLOADED_FLODER);
            if(!dir.exists() || !dir.isDirectory()){
                dir.mkdir();
            }

            dir = new File(ITEM_AVATAR_UPLOADED_FOLDER);
            if(!dir.exists() || !dir.isDirectory()){
                dir.mkdir();
            }

            GoodsItem goodsItem = new GoodsItem(goodsItemVO.getName(), goodsItemVO.getPrice(), goodsItemVO.getGoodsType(), goodsItemVO.getNum(), goodsItemVO.getDescription());
            ShopItemDTO dto = shopService.addGoodsItem(goodsItemVO.getShopCode(), goodsItem);
            if(dto != null){
                dir = new File(ITEM_AVATAR_UPLOADED_FOLDER + fileSeparator + dto.getShop().getId());
                if(!dir.exists() || !dir.isDirectory()){
                    dir.mkdir();
                }

                byte[] bytes = file.getBytes();
                String fileName = file.getOriginalFilename();
                String extName = fileName.substring(fileName.lastIndexOf("."));
                String uuid = UUID.randomUUID().toString().replace("-", "");
                String newName = uuid+extName;
                Path path = Paths.get(ITEM_AVATAR_UPLOADED_FOLDER + fileSeparator + dto.getShop().getId() + fileSeparator + newName);
                Files.write(path, bytes);

                ResultMessage re = shopService.updateItemAvatar(dto.getGoodsItem().getId(), newName);
                return new ResultVO<>(re.getCode(), re.getValue(), dto.getGoodsItem());
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), null);
    }

    @PostMapping("/modify/item/{code}")
    public ResultVO<GoodsItem> modifyDishItem(@PathVariable String code, @RequestParam(name = "file", required = false) MultipartFile file, GoodsItem vo){
        Shop shop = shopService.getShopByIdentifyCode(code);
        if (shop == null){
            return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), null);
        }
        Long id = shop.getId();
        if(file != null){
            try {
                File dir = new File(UPLOADED_FLODER);
                if(!dir.exists() || !dir.isDirectory()){
                    dir.mkdir();
                }

                dir = new File(ITEM_AVATAR_UPLOADED_FOLDER);
                if(!dir.exists() || !dir.isDirectory()){
                    dir.mkdir();
                }

                dir = new File(ITEM_AVATAR_UPLOADED_FOLDER + fileSeparator + id);
                if(!dir.exists() || !dir.isDirectory()){
                    dir.mkdir();
                }

                byte[] bytes = file.getBytes();
                String fileName = file.getOriginalFilename();
                String extName = fileName.substring(fileName.lastIndexOf("."));
                String uuid = UUID.randomUUID().toString().replace("-", "");
                String newName = uuid+extName;
                Path path = Paths.get(ITEM_AVATAR_UPLOADED_FOLDER + fileSeparator + id + fileSeparator + newName);
                Files.write(path, bytes);
                vo.setAvatar(newName);

                GoodsItem goodsItem = shopService.modifyGoodsItem(vo, id);
                if (goodsItem != null){
                    return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), goodsItem);
                }

            } catch (IOException e){
                e.printStackTrace();
            }
        }else {
            GoodsItem goodsItem = shopService.modifyGoodsItem(vo, id);
            if (goodsItem != null){
                return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), goodsItem);
            }
        }
        return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), null);
    }

    @RequestMapping("/item/avatar/{code}/{filename}")
    public byte[] showItemAvatar(@PathVariable String code, @PathVariable String filename) throws IOException{
        Shop shop = shopService.getShopByIdentifyCode(code);
        if(shop != null) {
            String imagePath = ITEM_AVATAR_UPLOADED_FOLDER + fileSeparator + shop.getId() + fileSeparator + filename;
            FileInputStream inputStream = new FileInputStream(imagePath);
            byte[] result = IOUtils.toByteArray(inputStream);
            inputStream.close();
            return result;
        }
        return null;
    }

    @PostMapping("/upload/user")
    public ResultVO<String> modifyUserInfo(@RequestParam(name = "file", required = false) MultipartFile file, UserModifyVO vo){
        if(file != null){
            try {
                File dir = new File(UPLOADED_FLODER);
                if(!dir.exists() || !dir.isDirectory()){
                    dir.mkdir();
                }

                dir = new File(USER_AVATAR_UPLOADED_FOLDER);
                if(!dir.exists() || !dir.isDirectory()){
                    dir.mkdir();
                }

                dir = new File(USER_AVATAR_UPLOADED_FOLDER + fileSeparator + vo.getId());
                if(!dir.exists() || !dir.isDirectory()){
                    dir.mkdir();
                }

                byte[] bytes = file.getBytes();
                String fileName = file.getOriginalFilename();
                String extName = fileName.substring(fileName.lastIndexOf("."));
                String uuid = UUID.randomUUID().toString().replace("-", "");
                String newName = uuid+extName;
                Path path = Paths.get(USER_AVATAR_UPLOADED_FOLDER + fileSeparator + vo.getId() + fileSeparator + newName);
                Files.write(path, bytes);

                ResultMessage re = userService.modifyUserGeneralInfo(vo.getEmail(), vo.getName(), vo.getPhone(), newName);
                return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), newName);

            } catch (IOException e){
                e.printStackTrace();
            }
        }
        else {
            ResultMessage re = userService.modifyUserGeneralInfo(vo.getEmail(), vo.getName(), vo.getPhone(), null);
            return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), null);
        }
        return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), null);
    }

    @RequestMapping("/user/avatar/{id}/{filename}")
    public byte[] showUserAvatar(@PathVariable String id, @PathVariable String filename) throws IOException{
        String imagePath = USER_AVATAR_UPLOADED_FOLDER + fileSeparator + id + fileSeparator + filename;
        FileInputStream inputStream = new FileInputStream(imagePath);
        byte[] result = IOUtils.toByteArray(inputStream);
        inputStream.close();
        return result;
    }

    @PostMapping("/upload/shop")
    public ResultVO<String> modifyShopInfo(@RequestParam(name = "file", required = false) MultipartFile file, ShopApplyVO vo){
        Shop shop = shopService.getShopByIdentifyCode(vo.getIdentifyCode());
        if (shop == null){
            return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), null);
        }
        if (file != null){
            try {
                File dir = new File(UPLOADED_FLODER);
                if(!dir.exists() || !dir.isDirectory()){
                    dir.mkdir();
                }

                dir = new File(SHOP_AVATAR_UPLOADED_FOLDER);
                if(!dir.exists() || !dir.isDirectory()){
                    dir.mkdir();
                }

                dir = new File(SHOP_AVATAR_UPLOADED_FOLDER + fileSeparator + shop.getId());
                if(!dir.exists() || !dir.isDirectory()){
                    dir.mkdir();
                }

                byte[] bytes = file.getBytes();
                String fileName = file.getOriginalFilename();
                String extName = fileName.substring(fileName.lastIndexOf("."));
                String uuid = UUID.randomUUID().toString().replace("-", "");
                String newName = uuid+extName;
                Path path = Paths.get(SHOP_AVATAR_UPLOADED_FOLDER + fileSeparator + shop.getId() + fileSeparator + newName);
                Files.write(path, bytes);

                ShopApply shopApply = new ShopApply(vo.getIdentifyCode(), vo.getName(), vo.getAddress(), ShopType.values()[Integer.parseInt(vo.getShopType())], ApplyState.SUBMITTED, newName);

                ResultMessage re = shopService.submitShopApply(shopApply);
                return new ResultVO<>(re.getCode(), re.getValue(), null);

            } catch (IOException e){
                e.printStackTrace();
            }
        } else {
            ShopApply shopApply = new ShopApply(vo.getIdentifyCode(), vo.getName(), vo.getAddress(), ShopType.values()[Integer.parseInt(vo.getShopType())], ApplyState.SUBMITTED, null);
            ResultMessage re = shopService.submitShopApply(shopApply);
            return new ResultVO<>(re.getCode(), re.getValue(), null);
        }
        return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), null);
    }

    @RequestMapping("/shop/avatar/{identifyCode}/{filename}")
    public byte[] showShopAvatar(@PathVariable String identifyCode, @PathVariable String filename) throws IOException{
        Shop shop = shopService.getShopByIdentifyCode(identifyCode);
        if (shop == null){
            return null;
        }
        String imagePath = SHOP_AVATAR_UPLOADED_FOLDER + fileSeparator + shop.getId() + fileSeparator + filename;
        FileInputStream inputStream = new FileInputStream(imagePath);
        byte[] result = IOUtils.toByteArray(inputStream);
        inputStream.close();
        return result;
    }
}
