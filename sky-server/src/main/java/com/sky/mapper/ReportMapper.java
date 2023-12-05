package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ReportMapper {
    @Select("select order_time from orders where status = 6 and order_time between #{begin} and #{end} ORDER BY order_time DESC")
    List<String> getTimes(LocalDate begin, LocalDate end);

    @Select("select amount from orders where status = 6 and order_time between #{begin} and #{end} ORDER BY order_time DESC")
    List<String> getTurnover(LocalDateTime begin, LocalDateTime end);
}
