package aster.yummy.dto;

import lombok.Data;

@Data
public class FinanceDTO {

    Double turnoverToday;

    Double turnoverYesterday;

    public FinanceDTO() {
    }

    public FinanceDTO(Double turnoverToday, Double turnoverYesterday) {
        this.turnoverToday = turnoverToday;
        this.turnoverYesterday = turnoverYesterday;
    }

}
