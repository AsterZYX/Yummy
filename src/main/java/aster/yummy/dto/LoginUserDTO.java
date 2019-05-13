package aster.yummy.dto;

import aster.yummy.enums.ResultMessage;
import aster.yummy.model.User;
import lombok.Data;

@Data
public class LoginUserDTO {

    private ResultMessage re;

    private User user;

    public LoginUserDTO(ResultMessage re, User user) {
        this.re = re;
        this.user = user;
    }
}
