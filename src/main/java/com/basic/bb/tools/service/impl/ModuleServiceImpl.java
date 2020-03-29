package com.basic.bb.tools.service.impl;

import com.basic.bb.tools.domain.Module;
import com.basic.bb.tools.service.ModuleService;
import com.basic.bb.tools.util.FreemarkerUtil;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;

/**
 * @author BB
 * Create: 2020/3/29 0029 21:05
 */
@Slf4j
@Service
public class ModuleServiceImpl implements ModuleService {

    private static final String CLASS_PATH = "/src/main/java/";
    private static final String RESOURCE_PATH = "/src/main/resources";

    @Override
    public InputStream generated(Module module, String path) {
        log.info("接收到的结果为:{}", module);
        try {
            // 递归创建目录
            if (!CollectionUtils.isEmpty(module.getModules())) {
                module.getModules().stream().forEach((m) -> generated(m, path + File.separatorChar + m.getParent().getArtifact()));
            }

            // 创建模块自己的根目录
            File rootPath = new File(path + module.getArtifact());
            // 存在则先删除
            if (rootPath.exists()) {
                rootPath.delete();
            }
            // 创建目录
            rootPath.mkdir();
            File rootPomFile = new File(rootPath.getAbsolutePath() + File.separatorChar + "pom.xml");
            if (generatedFile(rootPomFile, "pom.ftl", module)) {
                String packageName = module.getPackageName().replace(".", "" + File.separatorChar);
                // 创建包目录
                File packageDir = new File(rootPomFile.getAbsolutePath() + CLASS_PATH + packageName + File.separatorChar + module);
                packageDir.mkdirs();
                // 创建resources目录
                File resourcesDir = new File(rootPomFile.getAbsolutePath() + RESOURCE_PATH);
                resourcesDir.mkdirs();
            }
        } catch (Exception ex) {
            log.error("异常", ex);
        }

        return null;
    }

    private boolean generatedFile(File targetFile, String templateName, Object vo) {
        Template template = FreemarkerUtil.getTemplate(templateName);
        if (ObjectUtils.isEmpty(template)) {
            log.warn("模板:{}, 获取失败, 使用默认模板!", template);
            return generatedFile(targetFile, "defaultPom.ftl", vo);
        }

        // 写入文件
        try (FileWriter out = new FileWriter(targetFile)) {
            template.process(vo, out);
        } catch (Exception ex) {
            log.error("{}写入异常", targetFile.getAbsolutePath(), ex);
            return false;
        }
        return true;
    }
}
