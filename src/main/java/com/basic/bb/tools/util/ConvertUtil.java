package com.basic.bb.tools.util;

import com.basic.bb.tools.domain.Dependency;
import com.basic.bb.tools.domain.Module;
import com.basic.bb.tools.web.vo.ModuleVo;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author BB
 * Create: 2020/3/29 0029 20:49
 */
public class ConvertUtil {

    /**
     * 转换
     *
     * @param vo
     * @param parent
     * @param rootPath
     * @return
     */
    public static Module convert(ModuleVo vo, Module parent, String rootPath) {
        if (Objects.isNull(vo)) {
            throw new NullPointerException("moduleVo is null!");
        }

        // 基本属性的复制
        Module module = Module.builder()
                .name(vo.getArtifact())
                .group(vo.getGroup())
                .artifact(vo.getArtifact())
                .version(vo.getVersion())
                .description(vo.getDescription())
                .packageName(vo.getPackageName())
                .isRoot(Objects.isNull(parent) ? "true" : "false")
                .optional(vo.getOptional())
                .rootPath(concat(rootPath, vo.getArtifact()))
                .build();

        // 这个是为了配置pom.xml里面的parent节点
        if (!StringUtils.isEmpty(vo.getParentInfo())) {
            String[] infos = vo.getParentInfo().split(":");
            Module parentModule = Module.builder()
                    .group(infos[0])
                    .artifact(infos[1])
                    .version(infos[2])
                    .build();
            module.setParent(parentModule);
        }

        // 这个是为了设置module对象的信息
        if (!Objects.isNull(parent)) {
            module.setParent(parent);
            module.setGroup(parent.getGroup());
            module.setVersion(parent.getVersion());
            if (StringUtils.isEmpty(module.getPackageName())) {
                module.setPackageName(parent.getPackageName() + "." + (module.getArtifact().replaceAll("-", "").toLowerCase()));
            }
        }

        // 设置子模块信息
        if (CollectionUtils.isEmpty(vo.getModuleInfos())) {
            module.setPackaging("jar");
        } else {
            module.setPackaging("pom");
            List<Module> childModules = new ArrayList<>(10);
            vo.getModuleInfos().stream().forEach(child -> {
                childModules.add(convert(child, module, module.getRootPath()));
            });
            module.setModules(childModules);
        }

        // 设置maven依赖
        if (!CollectionUtils.isEmpty(vo.getDependencyInfos())) {
            List<Dependency> dependencies = new ArrayList<>(10);
            vo.getDependencyInfos().stream().forEach(d -> {
                if (StringUtils.isEmpty(d)) {
                    return;
                }
                String[] strArrays = d.split(":");
                Dependency dependency = Dependency.builder()
                        .group(strArrays[0])
                        .artifact(strArrays[1])
                        .build();
                switch (strArrays.length) {
                    case 3:
                        if (!StringUtils.isEmpty(strArrays[2])) {
                            dependency.setVersion(strArrays[2]);
                        }
                        break;
                    case 4:
                        if (!StringUtils.isEmpty(strArrays[2])) {
                            dependency.setVersion(strArrays[2]);
                        }
                        if (!StringUtils.isEmpty(strArrays[3])) {
                            dependency.setScope(strArrays[3]);
                        }
                        break;
                    case 5:
                        if (!StringUtils.isEmpty(strArrays[2])) {
                            dependency.setVersion(strArrays[2]);
                        }
                        if (!StringUtils.isEmpty(strArrays[3])) {
                            dependency.setScope(strArrays[3]);
                        }
                        if (!StringUtils.isEmpty(strArrays[4])) {
                            dependency.setOptional(strArrays[4]);
                        }
                        break;
                    default:
                        break;
                }
                dependencies.add(dependency);
            });
            module.setDependencies(dependencies);
        }

        return module;
    }

    /**
     * 设置module的rootPath
     *
     * @param module
     * @param rootPath
     */
    private static void setRootPath(Module module, String rootPath) {
        if (Objects.isNull(module)) {
            throw new NullPointerException("module对象不能为null!");
        }
        module.setRootPath(concat(rootPath, module.getArtifact()));
    }

    /**
     * 合并多个path,  path1+/+path2
     *
     * @param path1
     * @param paths
     * @return
     */
    public static String concat(String path1 ,String... paths) {
        if (StringUtils.isEmpty(path1) || Objects.isNull(paths)) {
            throw new NullPointerException("path1或paths为空");
        }
        StringBuilder sb = new StringBuilder(path1);
        for (String path : paths) {
            sb.append(File.separatorChar).append(path);
        }
        return sb.toString();
    }
}
