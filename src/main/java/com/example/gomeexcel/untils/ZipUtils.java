package com.example.gomeexcel.untils;

import lombok.extern.slf4j.Slf4j;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @Author: libaojun
 * @Description:将文件打包并输出到浏览器中
 * @Date: Created in 10:05 2021/3/29
 * @Modified By:
 */
@Slf4j
public class ZipUtils {

    /**
     * 将文件打包并输出到浏览器中
     *
     * @param request             请求头
     * @param response            请求尾
     * @param sourcePath          目录地址
     * @param downloadZipFileName 下载zip文件名称
     */
    public static void createZip(HttpServletRequest request, HttpServletResponse response, String sourcePath, String downloadZipFileName) {
        byte[] buf = new byte[4096];
        FileOutputStream fos = null;
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(response.getOutputStream());//--设置成这样可以不用保存在本地,再输出,通过response流输出,直接输出到客户端浏览器中。
            out.setEncoding("gbk");//此处修改字节码方式。
            if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
                downloadZipFileName = new String(downloadZipFileName.getBytes("GB2312"), "ISO-8859-1");
            } else {
                // 对文件名进行编码处理中文问题
                downloadZipFileName = URLEncoder.encode(downloadZipFileName, "UTF-8");// 处理中文文件名的问题
                downloadZipFileName = new String(downloadZipFileName.getBytes("UTF-8"), "GBK");// 处理中文文件名的问题
            }
            response.reset(); // 重点突出
            response.setCharacterEncoding("UTF-8"); // 重点突出
            response.setContentType("application/x-msdownload");// 不同类型的文件对应不同的MIME类型 // 重点突出
            // 默认为inline方式
            response.setHeader("Content-Disposition", "attachment;filename=" + downloadZipFileName);
            writeZip(new File(sourcePath), "", out);
            out.flush();
            out.close();
            log.info("当前目录以及子目录和文件已经压缩完成。。。。");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                    out = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                    fos = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 创建zip文件
     *
     * @param file       文件或者目录
     * @param parentPath 父路径（默认为""）
     * @param zos        ZipOutputStream
     */
    private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
        if (file.exists()) {
            if (file.isDirectory()) {//处理文件夹
                parentPath += file.getName() + File.separator;
                File[] files = file.listFiles();
                if (files.length != 0) {
                    for (File f : files) {
                        writeZip(f, parentPath, zos);
                    }
                } else {//空目录则创建当前目录
                    try {
                        zos.putNextEntry(new ZipEntry(parentPath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    ZipEntry ze = new ZipEntry(parentPath + file.getName());
                    zos.putNextEntry(ze);
                    byte[] content = new byte[1024];
                    int len;
                    while ((len = fis.read(content)) != -1) {
                        zos.write(content, 0, len);
                        zos.flush();
                    }

                } catch (FileNotFoundException e) {
                    log.info("创建ZIP文件失败");
                } catch (IOException e) {
                    log.info("创建ZIP文件失败");
                } finally {
                    try {
                        if (fis != null) {
                            fis.close();
                        }
                    } catch (IOException e) {
                        log.info("创建ZIP文件失败");
                    }
                }
            }
        }
    }

}
