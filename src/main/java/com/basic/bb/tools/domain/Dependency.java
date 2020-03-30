package com.basic.bb.tools.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 依赖
 *
 * @author BB
 * Create: 2020/3/29 0029 20:09
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dependency {

    private String group;
    private String artifact;
    private String version;
    private String scope;
    private String optional;

}
