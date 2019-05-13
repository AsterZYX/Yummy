package aster.yummy.service;

import aster.yummy.dto.FinanceDTO;
import aster.yummy.dto.ShopDataDTO;
import aster.yummy.dto.TimeDataDTO;
import aster.yummy.enums.ResultMessage;
import aster.yummy.model.ShopApply;

import java.util.List;

public interface AdminService {

    public List<ShopApply> getAllSubmittedShopApply();

    public ResultMessage examineShopApply(Long id, Boolean res);

    public ResultMessage examineShopSignApply(String identifyCode, Boolean res);

    public List<TimeDataDTO> getUserSignData();

    public List<ShopDataDTO> getShopTypeData();

    public List<ShopDataDTO> getShopCityData();

    public List<TimeDataDTO> getShopSignData();

    public FinanceDTO getFinanceData();
}
