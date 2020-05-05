package com.blog.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 敏感词替换
 */
public class SensitiveUtils {
    
    private static Set<String> sensitiveSet = new HashSet<>();
    
    private static Set<String> getSensitiveSet() {
        if(null==sensitiveSet ||sensitiveSet.isEmpty()){
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(SensitiveUtils.class.getResourceAsStream("/sensitive-word.txt"), "utf-8"))){
                String line;
                while ((line = reader.readLine()) != null) {
                    sensitiveSet.add(line.trim());
                }
            }catch (Exception e) {
                sensitiveSet =  new HashSet<>();
            }
        }
        return sensitiveSet;
    }
    /**
     * 清除非法字符
     *
     * @param str
     * @return 返回清除非法字符后的结果
     */
    private static String invalidClear(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;,\\[\\].<>/?！￥…（）—【】｛｝｜／《》‘；：＋——＊&……％$＃@！～”“’。，、？·\\s\t\r\n]";
        Matcher m = Pattern.compile(regEx).matcher(str);
        return m.replaceAll("").trim();
    }
    /**
     * 替换敏感词为*
     */
    public static String SensitiveReplace(String str){
        String reStr = invalidClear(str);
        for(String senStr : getSensitiveSet()){
            reStr.replaceAll(senStr, "*");
        }
        return reStr;
    }
}