# savior使用手册

## 1.savior框架介绍

   作者是一个现实中很实在的一个JAVA服务端码农,在平时的开发项目中我发现我们有很多的代码是在操作数据库，目前可供我们选择的就只有mybatis和hibernate两个主流框架.由于hibernate的笨重,现在很少有项目使用.所以大多数的公司都使用mybatis框架.我不得不承认这些都是很优秀的框架.我个人是个比较懒的人，能一行代码搞定的就不想多些一行。由于这样的洁癖促使我萌生了一个想法,能不能让我们的数据库操作在任何地方想用就用,而且只需要调用一行代码就能搞定一次数据操作.就这样我开始了我的这个想法........

## 2.savior基础入门

### 2-1.创建dao操作类

#### 2-1-1.java原生创建

```java
DataSource dataSource=initDataSource(); //初始化dataSource
SaviorDao dao = new SaviorDao(dataSource, DbTypeEnum.MYSQL);
```

#### 2-1-2.spring-jdbc项目创建

```java
@Autowired
private NamedParameterJdbcTemplate namedParameterJdbcTemplate;//注入jdbcTemplate
SaviorDao dao = SaviorDao(namedParameterJdbcTemplate, DbTypeEnum dbTypeEnum)
```

### 2-2.helloword走起

```java
/**
     * 创建表
     */
    @Test
    public void testCreateTable(){
        dao.langTemplate.execute("CREATE TABLE `tb_savior`(  \n" +
                "  `id` BIGINT(10) NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                "  `name` VARCHAR(50) COMMENT '名称',\n" +
                "  `create_time` DATETIME COMMENT '创建时间',\n" +
                "  PRIMARY KEY (`id`)\n" +
                ")");
    }
    /**
     * 插入表
     */
    @Test
    public void testInserData(){
        dao.langTemplate.insert("INSERT INTO tb_savior (name,create_time) VALUES (?,?)","张三",new Date());
    }

    /**
     * 插入表
     */
    @Test
    public void testDeleteData(){
        /**
        * String tableName 数据库表名
        * String primaryKey 不要认为只是主键ID,我们可以是其它的任意字段名
        * Object val 对应primaryKey的值
        */
        dao.langTemplate.delete("tb_savior","name","张三");
    }
```

### 2-3.基础总结

​    看了这个简单的demo后，你肯定会想，就这么简单用jdbc不就跟你的一样了，我还用你做什么,就听我下一说.

## 3.savior代码生成器

### 3-1.代码生成器介绍

​     代码生成器是框架原生自带，可以生成生成文件，也可以生成byte数组.方便我们在开发的时候，集成到接口文档中生成代码，也可以直接在test中生成,而且,我们的代码生成器还支持配置是否生成swagger注解，是否要生成Lombokod类型的实体.

### 3-2.代码生成器演示

```java
/**
 * 代码生成器测试类
 */
public class SaviorCoderTest extends BaseSaviorTest {

    /**
     * 生成代码文件
     */
    @Test
    public void testGenerateFile() {
        /**
         * String basdDir 生成文件的路径地址
         * String packageName 实体类型的包名
         * String[] tableNames 表名的数组(支持多表同时生成)
         */
        dao.coder.generator("D://","com.jiangbo.savior.model",new String[]{"tb_savior"});

    }

    /**
     * 生成代码字符串
     */
    @Test
    public void testGenerateString() {
        /**
         * String packageName 实体类型的包名
         * String[] tableNames 表名的数组(支持多表同时生成)
         */
        System.out.println(dao.coder.generator("com.jiangbo.savior.model",new String[]{"tb_savior"}));
    }

    /**
     * 测试自定义配置的代码生成规则
     */
    @Test
    public void testGenerateCustomString(){
        CoderConfig coderConfig=new CoderConfig();
        //设置是否生成swagger文档(默认true)
        coderConfig.setGenSwaggerAnnotation(true);
        //设置是否支持Lombok(默认true)
        coderConfig.setSupportLombok(true);
        System.out.println(dao.coder.generator("com.jiangbo.savior.model",new String[]{"tb_savior"},coderConfig));
    }
}
```





