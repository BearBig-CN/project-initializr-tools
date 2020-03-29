package com.basic.bb.tools.service;

import com.basic.bb.tools.domain.Module;

import java.io.InputStream;

/**
 * @author BB
 * Create: 2020/3/29 0029 20:45
 */
public interface ModuleService {

    /**
     * 生成项目
     *
     * @param module 模块信息
     * @param path 路径
     * @return
     */
    InputStream generated(Module module, String path);
}
