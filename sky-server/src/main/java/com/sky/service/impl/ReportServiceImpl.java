package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public TurnoverReportVO getTurnoverByTime(LocalDate begin, LocalDate end) {
        // select * from orders where status = 6 and order_time between #{begin} and #{end} order by order_time desc
//        List<String> time = reportMapper.getTimes(begin,end);
        ArrayList<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        String list = StringUtils.join(dateList, ",");
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            // 查询date日期对应的营业额数据
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap<>();
            map.put("begin",beginTime);
            map.put("end",endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.sunByMap(map);
            turnover = turnover == null ? 0.0 : turnover;
            //select sun(amount) from orders where order_time > ? and order_time < ? and status = 5
            turnoverList.add(turnover);
        }
        String turnover = StringUtils.join(turnoverList, ",");

        //List<String> turnover = reportMapper.getTurnover(begin,end);
        TurnoverReportVO turnoverReportVO = new TurnoverReportVO();
        turnoverReportVO.setDateList(list);
        turnoverReportVO.setTurnoverList(turnover);
//        turnoverReportVO.setDateList(String.valueOf(time));
//        turnoverReportVO.setTurnoverList(String.valueOf(turnover));
        return turnoverReportVO;
    }
}
