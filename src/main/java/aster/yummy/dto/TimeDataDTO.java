package aster.yummy.dto;

import lombok.Data;

@Data
public class TimeDataDTO {

    //日期
    String date;

    //总人数
    Integer total;

    //增长人数
    Integer growth;

    public TimeDataDTO() {
    }

    public TimeDataDTO(String date, Integer total, Integer growth) {
        this.date = date;
        this.total = total;
        this.growth = growth;
    }
}
