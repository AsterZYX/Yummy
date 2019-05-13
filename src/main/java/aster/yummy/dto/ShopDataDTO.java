package aster.yummy.dto;

import lombok.Data;

@Data
public class ShopDataDTO {

    //类型名称
    String name;

    //类型数量
    Integer value;

    public ShopDataDTO() {
    }

    public ShopDataDTO(String name, Integer value) {
        this.name = name;
        this.value = value;
    }
}
