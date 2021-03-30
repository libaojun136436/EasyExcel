package com.example.gomeexcel.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: libaojun
 * @Description:
 * @Date: Created in 17:21 2021/3/25
 * @Modified By:
 */
@Getter
@Setter
public class TestEntity  {

    @ExcelProperty(value = {"账号"} ,index = 0)
    private String APP_ACCOUNT;

    @ExcelProperty(value = {"账户"} ,index = 1)
    private String AD_ACCOUNT;

    @ExcelProperty(value = {"用户编号"} ,index = 2)
    private String EMPLOYEENUMBER;

    @ExcelProperty(value = {"中文名称"} ,index = 3)
    private String CN;

    @ExcelProperty(value = {"编码"} ,index = 4)
    private String POSITION_CODE;

    @ExcelProperty(value = {"公司描述"} ,index = 5)
    private String POSITION_DESC;

    @ExcelProperty(value = {"等级"} ,index = 6)
    private String LEVEL;

    @ExcelProperty(value = {"等级描述"} ,index = 7)
    private String LEVEL_DETAIL;


}
