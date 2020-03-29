package com.basic.bb.tools.web.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 模块,包含以下属性
 * name,
 * group,
 * artifact,
 * description,
 * packageName
 *
 * @author BB
 * Create: 2020/3/29 0029 20:00
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleVo {

    private String group;
    private String artifact;
    private String version;
    private String description;
    private String packageName;

    /**
     * 父模块信息，格式：
     */
    private String parentInfo;

    /**
     * 子模块信息
     */
    private List<ModuleVo> moduleInfos;

    /**
     * 模块依赖列表，key:moduleName,  value: dependencyInfo
     */
    private Map<ModuleVo, List<String>> moduleDependencyInfos;
}
