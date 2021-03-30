package com.example.gomeexcel.controller;

import com.example.gomeexcel.service.ExcleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: libaojun
 * @Description:关于活动效果数据报表导出迭代测试
 * @Date: Created in 16:29 2021/3/25
 * @Modified By:
 */
@Slf4j
@RestController
@RequestMapping("/gome-activities/actActivityExcel")
public class ActActivityEasyExcleController {

    @Autowired
    private ExcleService excleService;

    /**
     * 导出活动效果excel
     */
    @PostMapping("/uploadExcle")
    public void uploadActActivityExcle(HttpServletRequest request, HttpServletResponse response) throws InterruptedException {
        //标题就先用stream流简单创建一个
        Long startTime = System.currentTimeMillis();
        List<String> includeColumnNames = Stream.of("APP_ACCOUNT", "AD_ACCOUNT", "EMPLOYEENUMBER", "CN", "POSITION_CODE",
                "POSITION_DESC", "LEVEL", "LEVEL_DETAIL").collect(Collectors.toList());
        excleService.load(request, response, "测试001", includeColumnNames);
        Long endTime = System.currentTimeMillis();
        log.info("总用时：" + (endTime - startTime) + "ms");
    }

}
