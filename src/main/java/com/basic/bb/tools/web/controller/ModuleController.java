package com.basic.bb.tools.web.controller;

import com.basic.bb.tools.service.ModuleService;
import com.basic.bb.tools.util.ConvertUtil;
import com.basic.bb.tools.util.DownloadUtil;
import com.basic.bb.tools.web.vo.ModuleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 接口
 *
 * @author BB
 * Create: 2020/3/29 0029 20:41
 */
@Slf4j
@Controller
public class ModuleController {

    @Value("${temp.save.path}")
    private String tempSavePath;

    @Autowired
    private ModuleService moduleService;


    @GetMapping("/gen")
    public String index() {
        return "index";
    }


    @PostMapping("/gen")
    public String projectGen(ModuleVo moduleVo, HttpServletResponse response) throws UnsupportedEncodingException {
        Assert.notNull(moduleVo, "moduleVo不能为Null");
        String path = tempSavePath + File.separatorChar + moduleVo.getArtifact();
        File rootPath = moduleService.generated(ConvertUtil.convert(moduleVo, null, true), path);

        // 配置文件下载
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        // 下载文件能正常显示中文
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(rootPath.getName() + ".zip", "UTF-8"));

        try {
            DownloadUtil.folder2zip(rootPath, response.getOutputStream());
            log.info("Download the song successfully!");
        } catch (Exception e) {
            log.error("Download the song failed, 错误信息={}", e);
        }
        return null;
    }


}
