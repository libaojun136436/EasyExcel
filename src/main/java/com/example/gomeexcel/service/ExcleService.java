package com.example.gomeexcel.service;

import com.example.gomeexcel.dao.TestDao;
import com.example.gomeexcel.entity.TestEntity;
import com.example.gomeexcel.untils.ExcelUtils;
import com.example.gomeexcel.untils.FileUtils;
import com.example.gomeexcel.untils.ZipUtils;
import com.example.gomeexcel.vo.ParameterPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: libaojun
 * @Description: 按条件查询数据 并导出为excel
 * @Date: Created in 20:25 2021/3/25
 * @Modified By:
 */
@Slf4j
@Service
public class ExcleService {
    @Autowired
    private TestDao testDao;

    //设置一个固定的临时目录用于生成临时excle
    final private String path = "D:\\temp\\test\\";
    //设置一个固定的导出zip包名称
    final private String downloadZipFileName = "Test";
    //创建一个系统全局线程池
    final int numOfCpuCores = Runtime.getRuntime().availableProcessors();
    final double blockingCoefficient = 0.9;// 阻尼系数
    final int maximumPoolSize = (int) (numOfCpuCores / (1 - blockingCoefficient));
    ExecutorService threadPool = new ThreadPoolExecutor(numOfCpuCores,
            maximumPoolSize,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(),
            Executors.privilegedThreadFactory(),
            new ThreadPoolExecutor.DiscardOldestPolicy());

    /**
     * 分页查询数据并将数据通过easyexcel分装到excel中
     *
     * @param request            响应头
     * @param response           响应尾
     * @param fileName           文件名称
     * @param includeColumnNames excle包含的标题名称
     * @throws InterruptedException
     */
    public void load(HttpServletRequest request, HttpServletResponse response, String fileName, List<String> includeColumnNames) throws InterruptedException {
        // 1.这里每个Excel放6万条数据（分6个sheet页，每个1万条），当数据量超过6万条时，数据采用分段查询
        //  传递（起始行，结束行）参数，分段查询，即分步生成报表的同时分步生成EXCEL
        int SINGLE_EXCEPORT_EXCEL_MAX_NUM = 60000;
        //查询符合条件的一共有多少数据
        int count = testDao.queryTotal();
        final String fileNameWithTimestamp = fileName + "_" + System.currentTimeMillis();
        if (count > SINGLE_EXCEPORT_EXCEL_MAX_NUM) {
            int excelCount = count / SINGLE_EXCEPORT_EXCEL_MAX_NUM +
                    (count % SINGLE_EXCEPORT_EXCEL_MAX_NUM != 0 ? 1 : 0);
            final CountDownLatch latch = new CountDownLatch(excelCount);
            //要检查是否有当前路径，没有需要重新创建
            FileUtils.creatFileDirs(path);
            for (int i = 1; i <= excelCount; i++) {
                final ParameterPage bo = new ParameterPage();
                bo.setPageNum(i);
                bo.setPageSize(SINGLE_EXCEPORT_EXCEL_MAX_NUM);
                final int index = i;
                // 2.取一线程执行本次查询
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<TestEntity> testEntityList = testDao.queryAll(bo);
                        try {
                            // 2.生成单个excel
                            ExcelUtils.createOneExcel(fileNameWithTimestamp, index,
                                    testEntityList, includeColumnNames, path);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        latch.countDown();
                    }
                });
            }
            // 3.压缩excel文件并导出
            latch.await();
            ZipUtils.createZip(request, response, path, downloadZipFileName);
            // 4.删除文件
            FileUtils.deleteAllFiles(new File(path));
        }

    }

}