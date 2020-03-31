package com.basic.bb.tools.domain;

import lombok.Builder;
import lombok.Data;

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
public class Module {

    private String isRoot;
    private String name;
    private String group;
    private String artifact;
    private String version;
    private String description;
    private String packageName;
    private String packaging;
    /**
     * 根路径
     */
    private String rootPath;

    /**
     * 可选操作
     */
    private Optional optional;

    /**
     * 父模块
     */
    private Module parent;

    /**
     * 子模块
     */
    private List<Module> modules;

    /**
     * 依赖列表
     */
    private List<Dependency> dependencies;

    @Override
    public String toString() {
        return "Module{" +
                "isRoot=" + isRoot +
                ", name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", artifact='" + artifact + '\'' +
                ", version='" + version + '\'' +
                ", description='" + description + '\'' +
                ", packageName='" + packageName + '\'' +
                ", packaging='" + packaging + '\'' +
                '}';
    }
}
