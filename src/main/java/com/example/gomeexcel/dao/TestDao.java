package com.example.gomeexcel.dao;

import com.example.gomeexcel.entity.TestEntity;
import com.example.gomeexcel.vo.ParameterPage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: libaojun
 * @Description:
 * @Date: Created in 17:42 2021/3/25
 * @Modified By:
 */
@Mapper
public interface TestDao {

    List<TestEntity> queryAll(ParameterPage parameterPage);

    int queryTotal();

}
