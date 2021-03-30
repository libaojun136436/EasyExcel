package com.example.gomeexcel.untils;

import com.alibaba.excel.EasyExcel;
import com.example.gomeexcel.entity.TestEntity;
import com.example.gomeexcel.handler.CustomCellWriteHandler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @author chaoming
 * @desc
 * @date 20:26 2020/12/25
 */
public class ExcelUtils {

    /**
     * 通过EasyExcle创建并保存到本地临时目录
     *
     * @param <T>
     * @param fileNameWithTimestamp 临时文件时间戳
     * @param index                 序列
     * @param includeColumnNames    包含的数据
     * @param dataList              表头数据
     * @param path                  临时文件路径
     */
    public static <T> void createOneExcel(final String fileNameWithTimestamp,
                                          int index,
                                          final List<TestEntity> includeColumnNames,
                                          final List<String> dataList,
                                          String path) throws FileNotFoundException {
        OutputStream out = new FileOutputStream(path + fileNameWithTimestamp + index + ".xlsx");
        EasyExcel.write(out, TestEntity.class).registerWriteHandler(new CustomCellWriteHandler()).includeColumnFiledNames(dataList).sheet("模板")
                .doWrite(includeColumnNames);
    }

}
