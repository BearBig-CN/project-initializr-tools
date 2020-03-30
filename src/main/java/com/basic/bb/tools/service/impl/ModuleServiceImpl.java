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
import java.util.Objects;

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
    public File generated(Module module, String path) {
        log.info("接收到的结果为:{}", module);
        // 创建模块自己的根目录
        File rootPath = null;
        try {
            // 递归创建目录
            if (!CollectionUtils.isEmpty(module.getModules())) {
                module.getModules().stream().forEach((m) -> generated(m, path + File.separatorChar));
            }

            if (module.getIsRoot().equalsIgnoreCase(Boolean.toString(Boolean.FALSE))) {
                rootPath = new File(path + module.getArtifact());
            } else {
                rootPath = new File(path);
            }

            // 不存在，则创建
            if (!rootPath.exists()) {
                rootPath.mkdirs();
            }

            File file = new File(rootPath.getAbsolutePath() + File.separatorChar + "pom.xml");
            if (generatedFile(file, "pom.ftl", module) && module.getIsRoot().equalsIgnoreCase(Boolean.toString(Boolean.FALSE))) {
                String packageName = module.getPackageName().replace(".", "" + File.separatorChar);
                // 创建包目录
                File packageDir = new File(rootPath.getAbsolutePath() + CLASS_PATH + packageName);
                packageDir.mkdirs();
                // 创建resources目录
                File resourcesDir = new File(rootPath.getAbsolutePath() + RESOURCE_PATH);
                resourcesDir.mkdirs();
                // 创建启动类
                if (!Objects.isNull(module.getOptional()) && module.getOptional().isCreateBootStart()) {
                    file = new File(packageDir.getAbsolutePath() + File.separatorChar + "StartApplication.java");
                    generatedFile(file, "bootStart.ftl", module);

                    file = new File(resourcesDir.getAbsolutePath() + File.separatorChar + "application.properties");
                    // 创建配置文件
                    generatedFile(file, "application.ftl", module);
                }
            }
        } catch (Exception ex) {
            log.error("异常", ex);
        }
        return rootPath;
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
