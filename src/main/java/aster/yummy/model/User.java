package aster.yummy.model;

import aster.yummy.enums.UserLevel;
import aster.yummy.enums.UserState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class User {

    //编号
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //登录邮箱
    private String email;

    //登录密码
    private String password;

    //电话
    private String phone;

    //姓名
    private String name;

    //头像
    private String avatar;

    //等级
    private UserLevel userLevel;

    //送餐地址
//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name = "address")
//    @Embedded
//    @AttributeOverrides({@AttributeOverride(name = "linkman", column = @Column(name = "name")),
//                        @AttributeOverride(name = "contact_phone", column = @Column(name = "phone")),
//                        @AttributeOverride(name = "address_disp", column = @Column(name = "address_disp")),
//                        @AttributeOverride(name = "room", column = @Column(name = "room")),
//                        @AttributeOverride(name = "address_state", column = @Column(name = "address_state"))})
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_address", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "address_id", referencedColumnName = "id")})
    private List<Address> addresses;

    //用户状态
    private UserState userState;

    //用户余额
    private Double balance;

    //注册时间
    private String date;

    public User() {
    }

    public User(String email, String password, String phone, String name) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.name = name;
        this.userLevel = UserLevel.LEVEL_ONE;
        this.userState = UserState.VALID;
        this.balance = 0.0;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.date = sdf.format(new Date());
    }
}
