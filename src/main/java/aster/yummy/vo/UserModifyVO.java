package aster.yummy.vo;

import lombok.Data;

@Data
public class UserModifyVO {

    private Long id;

    private String email;

    private String name;

    private String phone;

    private String avatar;

    private Boolean isAvatarModified;

    public UserModifyVO() {
    }

    public UserModifyVO(Long id, String email, String name, String phone, String avatar) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.avatar = avatar;
    }
}
