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

    public static Module convert(ModuleVo moduleVo, Module parent, boolean isRoot) {
        if (Objects.isNull(moduleVo)) {
            throw new NullPointerException("moduleVo is null!");
        }

        // 基本属性的复制
        Module module = Module.builder()
                .name(moduleVo.getArtifact())
                .group(moduleVo.getGroup())
                .artifact(moduleVo.getArtifact())
                .version(moduleVo.getVersion())
                .description(moduleVo.getDescription())
                .packageName(moduleVo.getPackageName())
                .isRoot(String.valueOf(isRoot))
                .optional(moduleVo.getOptional())
                .build();

        // 这个是为了配置pom.xml里面的parent节点
        if (!StringUtils.isEmpty(moduleVo.getParentInfo())) {
            String[] infos = moduleVo.getParentInfo().split(":");
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
        if (CollectionUtils.isEmpty(moduleVo.getModuleInfos())) {
            module.setPackaging("jar");
        } else {
            module.setPackaging("pom");
            List<Module> childModules = new ArrayList<>(10);
            moduleVo.getModuleInfos().stream().forEach(moduleInfo -> {
                childModules.add(convert(moduleInfo, module, false));
            });
            module.setModules(childModules);
        }

        // 设置maven依赖
        if (!CollectionUtils.isEmpty(moduleVo.getDependencyInfos())) {
            List<Dependency> dependencies = new ArrayList<>(10);
            moduleVo.getDependencyInfos().stream().forEach(d -> {
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
}
