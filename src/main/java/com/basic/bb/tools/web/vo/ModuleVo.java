package com.basic.bb.tools.web.vo;

import com.basic.bb.tools.domain.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
     * 可选操作
     */
    private Optional optional;

    /**
     * 父模块信息，格式：
     */
    private String parentInfo;

    /**
     * 子模块信息
     */
    private List<ModuleVo> moduleInfos;

    /**
     * 模块依赖列表
     */
    private List<String> dependencyInfos;
}
