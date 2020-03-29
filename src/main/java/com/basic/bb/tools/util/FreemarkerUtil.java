package com.basic.bb.tools.util;


import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @ClassName FreemarkerUtil
 * @Description TODO
 * @Author BB
 * @Date 2019-08-08 11:05
 * @Version 1.0
 */
@Slf4j
public class FreemarkerUtil {

    /**
     * freemarker配置信息
     */
    private static final Configuration FREEMARKER_CONFIGURATION = new Configuration(Configuration.VERSION_2_3_28);

    static {
        try {
            File templateFile = new File(System.getProperty("user.dir") + File.separatorChar + "templates");
            FREEMARKER_CONFIGURATION.setDirectoryForTemplateLoading(templateFile);
            FREEMARKER_CONFIGURATION.setDefaultEncoding("UTF-8");
        } catch (Exception ex) {
            log.error("初始化Freemarker异常", ex);
        }
    }

    public static Template getTemplate(String templateName) {
        try {
            return FREEMARKER_CONFIGURATION.getTemplate(templateName);
        } catch (Exception ex) {
            log.error("获取模板异常", ex);
        }
        return null;
    }
}
