package aster.yummy.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Admin {

    //编号
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //登录识别码
    private String identifyCode;

    //密码
    private String password;

    public Admin(String identifyCode, String password) {
        this.identifyCode = identifyCode;
        this.password = password;
    }

    public Admin() {
    }
}
