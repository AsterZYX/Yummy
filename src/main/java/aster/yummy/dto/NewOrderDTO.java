package aster.yummy.dto;

import aster.yummy.enums.ResultMessage;
import lombok.Data;

@Data
public class NewOrderDTO {

    ResultMessage re;

    Long orderId;

    public NewOrderDTO() {
    }

    public NewOrderDTO(ResultMessage re, Long orderId) {
        this.re = re;
        this.orderId = orderId;
    }
}
