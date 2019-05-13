package aster.yummy.vo;

import lombok.Data;

@Data
public class AddressVO {

    private Long id;

    //联系人
    private String name;

    //联系电话
    private String phone;

    //详细地址
    private String addressDisp;

    //门牌号
    private String room;

    //省
    private String province;

    //城市
    private String city;

}
