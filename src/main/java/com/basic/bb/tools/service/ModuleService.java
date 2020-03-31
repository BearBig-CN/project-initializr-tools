package com.basic.bb.tools.service;

import com.basic.bb.tools.domain.Module;

import java.io.File;

/**
 * @author BB
 * Create: 2020/3/29 0029 20:45
 */
public interface ModuleService {

    /**
     * 生成项目
     *
     * @param module 模块信息
     * @return
     */
    File generated(Module module);
}
