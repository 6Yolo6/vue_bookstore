package com.example.bookstore;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class CodeGenerator {

    /**
     *
     * @Title: main
     * @Description: 生成
     * @param args
     */
    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(System.getProperty("user.dir")+"/src/main/java");//输出文件路径
        gc.setFileOverride(true); // 是否文件覆盖
        gc.setActiveRecord(false);// 不需要ActiveRecord(实体类继承Model)特性的请改为false
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(true);// XML ColumnList
        gc.setAuthor("author");// 作者

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setControllerName("%sController");
        // 默认service接口名IXXXService 自定义指定之后就不会用I开头了
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("Root@123");
        dsc.setUrl("jdbc:mysql://localhost:3306/bookstore?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=UTC");
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略(下划线转驼峰)
        strategy.setInclude(new String[]{"user","book","category"}); // 需要生成的表名

        strategy.setSuperServiceClass("");
        strategy.setSuperServiceImplClass("");
        strategy.setSuperMapperClass(null);

        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.example.bookstore"); //根目录包名
        pc.setController("controller");
        pc.setService("service");
        pc.setServiceImpl("impl");
        pc.setMapper("mapper");
        pc.setEntity("entity");
        pc.setXml("xml");
        mpg.setPackageInfo(pc);

        // 执行生成
        mpg.execute();

    }

}
