package com.blog.serve;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Scanner;

public class MyBatisGenerator {
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        //1、全局配置
        GlobalConfig config = new GlobalConfig()
                .setActiveRecord(true)
                .setAuthor("haijun.zhang")
                .setOpen(false)
                .setOutputDir(projectPath + "/src/main/java")
                .setFileOverride(true)                              //第二次生成会把第一次生成的覆盖掉
                .setIdType(IdType.AUTO)                             //主键策略
                .setDateType(DateType.ONLY_DATE)                    //配置时间类型策略
                .setServiceName("%sService")                        //生成的service接口名字首字母是否为I，这样设置就没有I
                .setBaseResultMap(true)                             //生成resultMap
                .setBaseColumnList(true);                           //在xml中生成基础列
        //2、数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig()
                .setDbType(DbType.MYSQL)
                .setDriverName("com.mysql.cj.jdbc.Driver")
                .setUrl("jdbc:mysql://localhost:3306/blogdb?tinyInt1isBit=false&serverTimezone=UTC&useUnicode=true&useSSL=false&characterEncoding=utf8")
                .setUsername("root")
                .setPassword("123456");
        //3、策略配置
        StrategyConfig strategyConfig = new StrategyConfig()
                .setNaming(NamingStrategy.underline_to_camel)
                .setColumnNaming(NamingStrategy.underline_to_camel)
                .setCapitalMode(true)                                   //开启全局大写命名
                .setEntityLombokModel(true)                             //使用lombok
                .setRestControllerStyle(true)
                .setControllerMappingHyphenStyle(true)
                .setNaming(NamingStrategy.underline_to_camel)           //下划线到驼峰的命名方式
                .setTablePrefix("")                                     //表名前缀
                .setInclude(scanner("表名，多个英文逗号分割").split(","));
        //4、包名策略配置
        PackageConfig packageConfig = new PackageConfig()
                .setParent("com.blog.mybatis")                                   //设置包名的parent
                .setMapper("mapper")
                .setService("service")
                .setController("controller")
                .setEntity("entity")
                .setXml("mapper.xml");                                  //设置xml文件的目录
        //5、自定义模板配置
//        InjectionConfig cfg = new InjectionConfig() {
//            @Override
//            public void initMap() {
//            }
//        };
//        cfg.setFileOutConfigList(Collections.singletonList(
//                new FileOutConfig("/templates/mapper.xml.ftl") {
//                    @Override
//                    public String outputFile(TableInfo tableInfo) {
//                        return projectPath + "/src/main/resources/mapper/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
//                    }
//                }
//        ));
        //6、整合配置
        AutoGenerator autoGenerator = new AutoGenerator()
                .setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(packageConfig)
//                .setCfg(cfg)
                .setTemplateEngine(new FreemarkerTemplateEngine());
        //7、执行
        autoGenerator.execute();
    }
}
