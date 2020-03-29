package com.basic.bb.tools.domain;

import lombok.Builder;
import lombok.Data;

/**
 * 依赖
 *
 * @author BB
 * Create: 2020/3/29 0029 20:09
 */
@Builder
@Data
public class Dependency {

    private String group;
    private String artifact;
    private String version;
    private String scope;

}
