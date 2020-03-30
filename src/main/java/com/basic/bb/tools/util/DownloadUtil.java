package com.basic.bb.tools.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 下载工具类
 *
 * @author BB
 * createTime 2020-03-30 21:43
 */
@Slf4j
public class DownloadUtil {


    /**
     * 压缩当前目录path下的所有文件 , 生成文件名称为 zipname , 放在路径zippath下 ;
     * 有异常则抛出 ;
     *
     * @param src          下载目录的路径
     * @param outputStream 响应流
     * @throws IOException
     */
    public static void folder2zip(File src, OutputStream outputStream) {
        String path = src.getAbsolutePath();
        if (src == null || !src.exists() || !src.isDirectory()) {
            // 源目录不存在 或不是目录 , 则异常
            log.error("压缩源目录不存在或非目录!" + path);
            throw new RuntimeException("压缩源目录不存在或非目录!" + path);
        }

        try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(outputStream))) {
            // 递归压缩目录下所有的文件  ;
            compress(zos, src, src.getName());
        } catch (FileNotFoundException e) {
            log.error("压缩文件不存在", e);
            throw new RuntimeException("压缩目标文件不存在!" + e.getMessage());
        } catch (IOException e) {
            log.error("压缩文件IO异常", e);
            throw new RuntimeException("压缩文件IO异常!" + e.getMessage());
        } finally {
            // 下载完成后，删除源目录
            if (src.exists()) {
                src.delete();
            }
        }
    }

    /**
     * 递归压缩文件
     *
     * @param zos
     * @param src
     * @throws IOException
     */
    public static void compress(ZipOutputStream zos, File src, String name) throws IOException {
        if (src == null || !src.exists()) {
            return;
        }
        if (src.isFile()) {
            byte[] buf = new byte[10240];
            ZipEntry zEntry = new ZipEntry(name);
            zos.putNextEntry(zEntry);
            log.debug("压缩:{}", src.getAbsolutePath());
            FileInputStream in = new FileInputStream(src);
            BufferedInputStream bin = new BufferedInputStream(in, 10240);
            int readcount = 0;
            while ((readcount = bin.read(buf, 0, 10240)) != -1) {
                zos.write(buf, 0, readcount);
            }

            zos.closeEntry();
            bin.close();
        } else {
            // 文件夹
            File[] fs = src.listFiles();
            if (fs == null || fs.length == 0) {
                zos.putNextEntry(new ZipEntry(name + File.separator));
                zos.closeEntry();
                return;
            }

            for (File f : fs) {
                compress(zos, f, name + File.separator + f.getName());
            }
        }
    }
}
