package aster.yummy.model;

import aster.yummy.enums.AddressState;
import lombok.Data;

import javax.persistence.*;

@Data
//@Embeddable
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    //地址状态
    private AddressState addressState;

    public Address() {
    }

    public Address(String name, String phone, String addressDisp, String room, AddressState addressState, String province, String city) {
        this.name = name;
        this.phone = phone;
        this.addressDisp = addressDisp;
        this.room = room;
        this.addressState = addressState;
        this.province = province;
        this.city = city;
    }

    public Address(Long id, String name, String phone, String addressDisp, String room, String province, String city) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.addressDisp = addressDisp;
        this.room = room;
        this.province = province;
        this.city = city;
    }
}
