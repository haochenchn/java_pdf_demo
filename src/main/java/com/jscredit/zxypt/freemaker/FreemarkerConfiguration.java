package com.jscredit.zxypt.freemaker;

import java.io.File;
import java.io.IOException;

import com.jscredit.zxypt.utils.ResourceLoader;

import freemarker.template.Configuration;

/*
 * freemaker 的配置
 */
public class FreemarkerConfiguration {
    private static Configuration config = null;
    /**
     * 获取 FreemarkerConfiguration
     */
    public static synchronized Configuration getConfiguation() {
        if (config == null) {
            setConfiguation();
        }
        return config;
    }

    /**
     * 设置 配置
     */
    private static void setConfiguation() {
        config = new Configuration();
        String path = ResourceLoader.getPath("template/");
        System.out.println(">>>>>>>>>>>>>>>>>>>"+path);
        try {
            config.setDirectoryForTemplateLoading(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}