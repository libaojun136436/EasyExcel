package com.example.gomeexcel.untils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @Author: libaojun
 * @Description: 关于java文件和文件夹的相关操作
 * @Date: Created in 10:07 2021/3/29
 * @Modified By:
 */
@Slf4j
public class FileUtils {

    /**
     * 删除某目录下的全部文件
     *
     * @param root 注意这里要传入的文件目录
     */
    public static void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {  //判断是否为文件
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }

    /**
     * -创建多个目录(子目录)
     *
     * @param path 路径
     */
    public static void creatFileDirs(String path) {
        //首先要创建目标路径
        File files = new File(path);
        if (!files.exists()) {
            if (files.mkdirs()) {
                log.info("创建多个临时目录成功");
            } else {
                log.info("创建多个临时目录失败.....");
            }
        }
    }

}
