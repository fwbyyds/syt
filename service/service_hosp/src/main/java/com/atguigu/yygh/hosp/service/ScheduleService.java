package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.ScheduleOrderVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ScheduleService {
    void save(Map<String, Object> paramMap);
    Page<Schedule> findPageDepartment(int page, int limit, ScheduleOrderVo scheduleOrdervo);

    void remove(String hoscode, String hosScheduleId);
}
