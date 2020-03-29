package com.basic.bb.tools.util;

import com.basic.bb.tools.domain.Module;
import com.basic.bb.tools.web.vo.ModuleVo;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
                .isRoot(isRoot)
                .build();

        // 这个是为了配置pom.xml里面的parent节点
        if (!StringUtils.isEmpty(moduleVo.getParentInfo())) {

        }

        // 这个是为了设置module对象的信息
        if(!Objects.isNull(parent)){
            module.setGroup(parent.getGroup());
            module.setVersion(parent.getVersion());
            module.setPackageName(parent.getPackageName()+module.getArtifact());
        }

        // 设置子模块信息
        if (CollectionUtils.isEmpty(moduleVo.getModuleInfos())) {
            module.setPackaging("jar");
        } else {
            module.setPackaging("pom");
            List<Module> childModules = new ArrayList<>(10);
            moduleVo.getModuleInfos().stream().forEach(moduleInfo -> {
                childModules.add(convert(moduleInfo, module,false));
            });
            module.setModules(childModules);
        }
        return module;
    }
}
