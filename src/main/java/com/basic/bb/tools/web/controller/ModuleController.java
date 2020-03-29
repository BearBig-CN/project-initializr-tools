package com.basic.bb.tools.web.controller;

import com.basic.bb.tools.service.ModuleService;
import com.basic.bb.tools.util.ConvertUtil;
import com.basic.bb.tools.web.vo.ModuleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 接口
 *
 * @author BB
 * Create: 2020/3/29 0029 20:41
 */
@Slf4j
@Controller
public class ModuleController {

    @Value("${temp.save.path}")
    private String tempSavePath;

    @Autowired
    private ModuleService moduleService;


    @GetMapping("/gen")
    public String index() {
        return "index";
    }


    @PostMapping("/gen")
    @ResponseBody
    public String projectGen(ModuleVo moduleVo) {
        Assert.notNull(moduleVo, "moduleVo不能为Null");
        moduleService.generated(ConvertUtil.convert(moduleVo, null, true), tempSavePath);
        return "true";
    }
}
