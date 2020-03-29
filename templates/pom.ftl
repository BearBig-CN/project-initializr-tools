<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <#if parent?? >
    <parent>
        <groupId>${parent.group}</groupId>
        <artifactId>${parent.artifact}</artifactId>
        <version>${parent.version}</version>
    </parent>
    </#if>
    <artifactId>${artifact}</artifactId>
    <#if isRoot>
    <groupId>${group}</groupId>
    <version>${version}</version>
    </#if>
    <name>${name}</name>
    <packaging>${packaging}</packaging>
    <description>${description}</description>

    <#if modules?? && (modules?size > 0) >
        <modules>
            <#list modules! as module>
                <module>${module}</module>
            </#list>
        </modules>
    </#if>

    <dependencies>
        <#list dependencies! as dependency>
            <dependency>
                <groupId>${dependency.group}</groupId>
                <artifactId>${dependency.artifact}</artifactId>
                <version>${dependency.version}</version>
                <#if dependency.scope??>
                <scope>${dependency.scope}</scope>
                </#if>
            </dependency>
        </#list>
    </dependencies>
</project>
