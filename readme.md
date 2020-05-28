# **savior使用手册**

## savior框架介绍

随时随地savior只需要用一行代码操作数据库,做我们想做的操作，查询我们想要的结果....

### 1.savior框架由来

   作者是一个现实中很实在的一个JAVA服务端码农,在平时的开发项目中我发现我们有很多的代码是在操作数据库，目前可供我们选择的就只有mybatis和hibernate两个主流框架.由于hibernate的笨重,现在很少有项目使用.所以大多数的公司都使用mybatis框架.我不得不承认这些都是很优秀的框架.我个人是个比较懒的人，能一行代码搞定的就不想多些一行。mybatis 要写接口还要到xml中配置映射，我感觉着实有点麻烦.由于这样的洁癖促使我萌生了一个想法,能不能让我们的数据库操作在任何地方想用就用,而且只需要调用一行代码就能搞定一次数据操作.就这样我经过了两个月左右的不停改版,和我在公司项目的实战应用,一步步优化savior框架就此诞生了,我希望就像框架名一样,savior框架能成为我们JAVA开发人员的救世主，能把我们从以前操作数据库的困苦中解救出来.....

### 2.savior框架性能

   在框架中我尽量的避免了反射,大多数的对应创建和属性值的设置都是直接调用实体的方法来设置的,在我现网的项目中做过几次数据量比较大的接口测试，性能是略优于mybatis框架的.由于我工作的公司不是很大没有专业的性能测试人员，如果伙伴们使用后有整体测试过，麻烦告知我一下.

## savior基础入门

### 1.savior依赖导入

   可以直接在中央仓库中下载

```xml
   <dependency>
       <groupId>com.github.chen-jiangbo</groupId>
       <artifactId>savior</artifactId>
       <version>1.1.0</version>
   </dependency>
```

### 2.创建dao操作类

#### 2-1.java原生创建

```java
DataSource dataSource=initDataSource(); //初始化dataSource
/**
 *dataSource 数据库存连接池
 *DbTypeEnum 这个是数据库类型(暂时框架支持mysql,oracle)
 */
SaviorDao dao = new SaviorDao(dataSource, DbTypeEnum.MYSQL);
```

#### 2-2.spring-jdbc项目创建

```java
@Autowired
private NamedParameterJdbcTemplate namedParameterJdbcTemplate;//注入jdbcTemplate
SaviorDao dao = SaviorDao(namedParameterJdbcTemplate, DbTypeEnum dbTypeEnum)
```

### 3.helloword走起

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

### 4.基础总结

​    看了这个简单的demo后，你肯定会想，就这么简单用jdbc不就跟你的一样了，我还用你做什么,就听我下一说.

## savior测试用例准备

创建基础测试类,所有有演示类继承此类

```java
public class BaseSaviorTest {
    /**
     * 数据库连接池
     */
    private DruidDataSource dataSource;
    /**
     * dao操作类
     */
    protected SaviorDao dao;

    private void initDataSource() {
        try {
            this.dataSource = new DruidDataSource();
            this.dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            this.dataSource.setUrl("jdbc:mysql://IP:端口/数据库名?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai&generateSimpleParameterMetadata=true&useInformationSchema=true");
            this.dataSource.setUsername("用户名");
            this.dataSource.setPassword("密码");
            this.dataSource.setInitialSize(1);
            this.dataSource.setMinIdle(1);
            this.dataSource.setMaxActive(1);
            this.dataSource.setMaxActive(60000);
            this.dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
            this.dataSource.init();
            System.out.println("数据库连接池初始化完成");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Before
    public void initDao() {
        initDataSource();
        this.dao = new SaviorDao(this.dataSource, DbTypeEnum.MYSQL);
    }

    @After
    public void closeDataSource() {
        this.dataSource.close();
        System.out.println("数据库连接池已关闭");
    }
}
```



## savior代码生成器

### 1.代码生成器介绍

​     代码生成器是框架原生自带，可以生成生成文件，也可以生成byte数组.方便我们在开发的时候，集成到接口文档中生成代码，也可以直接在test中生成,而且,我们的代码生成器还支持配置是否生成swagger注解，是否要生成Lombokod类型的实体.

 **注:要把mysql的注释生成到实体中，我们必须在连接数据库的url后面增加useInformationSchema=true这个参数**

### 2.代码生成器演示

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

## savior中template类型

  我们在框架中主要定义四种Template操作类.这几种操作类熟练运用后可以适应我们任何的数据库数据操作，下面我们对这四种操作类作一个简单的解释:

| template名称   | template解释                                                 |
| -------------- | ------------------------------------------------------------ |
| LangTemplate   | LangTemplate类主要是对一些基础类型(Integer,String,Long...)等基础数据类型进行增删改的操作,同时在这个数也提供了原生的sql更新插入操作，这个类是template的基础类，其它的操作类都是基于些类进行扩展而来的 |
| RecordTemplate | RecordTemplate类主要是对我们的封装的一个Record对象的操作,这个类相当于map类型字段可以随意扩展,适合于我们在做service业务层时，有些有业务中用到的中间查询不返回给客户端显示了. |
| ModelTemplate  | ModelTemplate类是我们使用比较多的一个类，他可以操作我们多表关连的返回查询，或者我们自己定义的页面结构查询. |
| TableTemplate  | TableTemplate类主要是跟数据表映射比较全的单表操作就使用这个类，Table实体可以通过我们的代码生成器来生成. |

### 1.LangTemplate实战

```java
/**
 * 基础类型数据操作测试类
 */
public class LangTemplateTest extends BaseSaviorTest {

    /**
     * 创建表
     */
    @Test
    public void testCreateTable() {
        dao.langTemplate.execute("CREATE TABLE `tb_savior`(  \n" +
                "  `id` BIGINT(10) NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                "  `name` VARCHAR(50) COMMENT '名称',\n" +
                "  `create_time` DATETIME COMMENT '创建时间',\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") COMMENT='savior框架测试表' ");
    }

    /**
     * 插入表
     */
    @Test
    public void testInserData() {
        dao.langTemplate.insert("INSERT INTO tb_savior (id,name,create_time) VALUES (1,?,?)", "张三", new Date());
    }

    /**
     * 插入表
     */
    @Test
    public void testDeleteData() {
        dao.langTemplate.delete("tb_savior", "name", "张三");
    }


    /**
     * 查询单条数据
     */
    @Test
    public void testQueryData() {
        System.out.println(dao.langTemplate.query(String.class, "select name from tb_savior where id=1"));
    }

    /**
     * 测试查询列表
     */
    @Test
    public void testQueryListData() {
        System.out.println(Arrays.toString(dao.langTemplate.queryList(String.class, "select name from tb_savior").toArray()));
    }
}
```



### 2.RecordTemplate实战

```java
/**
 * 基础类型数据操作测试类
 */
public class RecordTemplateTest extends BaseSaviorTest {

    private Record record=new Record()
            .setColumn("id",2)
            .setColumn("name","李四")
            .setColumn("create_time",new Date());


    /**
     * 插入Record
     */
    @Test
    public void testInsertRecord(){
        //如果是要返回数据库自动生成的key 需要调用 insertReturnGeneratedKey
        dao.recordTemplate.insert("tb_savior",record);
    }

    /**
     * 更新数据
     */
    @Test
    public void testUpdateRecord(){
        dao.recordTemplate.update("tb_savior",record,"id");
    }

    /**
     * 多条件更新数据(多个条件之间and连接)
     */
    @Test
    public void testUpdateRecordBMultipleKey(){
        dao.recordTemplate.update("tb_savior",record, new String[]{"id","name"});
    }

    /**
     * 查询单条数据
     */
    @Test
    public void testQueryRecord(){
        Record query = dao.recordTemplate.query("select * from tb_savior where id = 2 limit 1");
        System.out.println("name==>"+query.getString("name"));
        System.out.println("id==>"+query.getLong("id"));
    }

    /**
     * 查询集合
     */
    @Test
    public void testQueryListRecord(){
        List<Record> querys = dao.recordTemplate.queryList("select * from tb_savior where id = 2");
        System.out.println("size==>"+querys.size());
        System.out.println("name==>"+querys.get(0).getString("name"));
    }


    /**
     * 查询分页
     */
    @Test
    public void testQueryPageRecord(){
        Page<Record> querys = dao.recordTemplate.queryPage("select * from tb_savior where id = 2" ,1,10);
        System.out.println("size==>"+querys.getTotalCount());
        System.out.println("name==>"+((List<Record>)querys.getList()).get(0).getString("name"));
    }


    /**
     * 删除数据
     */
    @Test
    public void testDeleteRecord(){
        /**
         * String tableName 表名
         * Record records map数据
         * String primaryKey 用于删除时使用的key(只要与这个key值相同的数据都会被删除)
         */
        dao.recordTemplate.delete("tb_savior",record,"id");
    }

    /**
     * 批量删除数据
     */
    @Test
    public void testBatchDeleteRecord(){
        /**
         * String tableName 表名
         * List<?> records map集合数据
         * String primaryKey 用于删除时使用的key(只要与这个key值相同的数据都会被删除)
         */
        dao.recordTemplate.batchDelete("tb_savior", Arrays.asList(record),"id");
    }

}
```



### 3.ModelTemplate实战

```java
/**
 * 基础类型数据操作测试类
 */
public class ModelTemplateTest extends BaseSaviorTest {

    private TbSaviorExtendsModel initExtendsModel(){
        TbSaviorExtendsModel extendsModel=new TbSaviorExtendsModel();
        extendsModel.setId(4l);
        extendsModel.setName("李六");
        extendsModel.seteStr("....");
        extendsModel.setCreateTime(new Date());
        return extendsModel;

    }

    private TbSaviorCustomModel initCustomModel(){
        TbSaviorCustomModel customModel=new TbSaviorCustomModel();
        customModel.setmId(3l);
        customModel.setmName("王五");
        customModel.setmCreateTime(new Date());
        return customModel;
    }

    /**
     * 插入数据
     */
    @Test
    public void testInsertCustomModel() {
        //如果是要返回数据库自动生成的key 需要调用 insertReturnGeneratedKey
        dao.modelTemplate.insert("tb_savior",
                (TbSaviorCustomModel model) -> new Record()
                                .setColumn("id", model.getmId())
                                .setColumn("name", model.getmName())
                                .setColumn("create_time", model.getmCreateTime()),
                initCustomModel());
    }

    /**
     * 插入数据
     */
    @Test
    public void testInsertExtendsModel() {
        //如果是要返回数据库自动生成的key 需要调用 insertReturnGeneratedKey
        dao.modelTemplate.insert("tb_savior",
                (TbSaviorExtendsModel model) -> model.reversal(),
                initExtendsModel());
    }

    /**
     * 更新数据
     */
    @Test
    public void testUpdateRecord() {
        dao.modelTemplate.update("tb_savior",
                initCustomModel(),
                (TbSaviorCustomModel model) -> new Record()
                                .setColumn("id", model.getmId())
                                .setColumn("name", model.getmName())
                                .setColumn("create_time", model.getmCreateTime()),
                "id"
        );
    }

    /**
     * 多条件更新数据(多个条件之间and连接)
     */
    @Test
    public void testUpdateRecordBMultipleKey(){
        dao.modelTemplate.update("tb_savior",
                initCustomModel(),
                (TbSaviorCustomModel model) -> new Record()
                        .setColumn("id", model.getmId())
                        .setColumn("name", model.getmName())
                        .setColumn("create_time", model.getmCreateTime()),
                new String[]{"id","name"}
        );
    }

    /**
     * 查询单条数据
     */
    @Test
    public void testQueryCustomeModel() {
        TbSaviorCustomModel query = dao.modelTemplate.query((record) -> {
            TbSaviorCustomModel rs = new TbSaviorCustomModel();
            rs.setmId(record.getLong("id"));
            rs.setmName(record.getString("name"));
            rs.setmCreateTime(record.getDate("create_time"));
            return rs;
        }, "select * from tb_savior where id = 3 limit 1");
        System.out.println("name==>" + query.getmName());
        System.out.println("id==>" + query.getmId());
    }

    /**
     * 查询单条数据
     */
    @Test
    public void testQueryExtendsModel() {
        TbSaviorExtendsModel query = dao.modelTemplate.query((record) -> {
            TbSaviorExtendsModel rs = new TbSaviorExtendsModel();
            rs.convert(record);
            rs.seteStr("......");
            return rs;
        }, "select * from tb_savior where id = 3 limit 1");
        System.out.println("name==>" + query.getName());
        System.out.println("id==>" + query.getId());
    }

    /**
     * 查询集合
     */
    @Test
    public void testQueryListModel(){
        List<TbSaviorExtendsModel> querys = dao.modelTemplate.queryList((record) -> {
            TbSaviorExtendsModel rs = new TbSaviorExtendsModel();
            rs.convert(record);
            rs.seteStr("......");
            return rs;
        },"select * from tb_savior where id = 4");
        System.out.println("size==>"+querys.size());
        System.out.println("name==>"+querys.get(0).getName());
    }


    /**
     * 查询分页
     */
    @Test
    public void testQueryPageModel(){
        Page<TbSaviorExtendsModel> querys = dao.modelTemplate.queryPage((record)->{
            TbSaviorExtendsModel rs = new TbSaviorExtendsModel();
            rs.convert(record);
            rs.seteStr("......");
            return rs;
        },"select * from tb_savior where id = 4" ,1,10);
        System.out.println("size==>"+querys.getTotalCount());
        System.out.println("name==>"+((List<TbSaviorExtendsModel>)querys.getList()).get(0).getName());
    }

}
```



### 4.TableTemplate实战

```java
/**
 * 基础类型数据操作测试类
 */
public class TableTemplateTest extends BaseSaviorTest {

    private TbSaviorTable initTbSaviorTable() {
        TbSaviorTable rs = new TbSaviorTable();
        rs.setId(5l);
        rs.setName("姚七");
        rs.setCreateTime(new Date());
        return rs;
    }

    /**
     * 插入数据
     */
    @Test
    public void testInsertTable() {
        //如果是要返回数据库自动生成的key 需要调用 insertReturnGeneratedKey
        dao.tableTemplate.insert("tb_savior", initTbSaviorTable());
    }

    /**
     * 更新数据
     */
    @Test
    public void testUpdateTable() {
        dao.tableTemplate.update("tb_savior", initTbSaviorTable(), "id");
    }

    /**
     * 多条件更新数据(多个条件之间and连接)
     */
    @Test
    public void testUpdateTableBMultipleKey() {
        dao.tableTemplate.update("tb_savior", initTbSaviorTable(), new String[]{"id", "name"});
    }

    /**
     * 查询单条数据
     */
    @Test
    public void testQueryTable() {
        TbSaviorTable query = dao.tableTemplate.query(TbSaviorTable::new, "select * from tb_savior where id = 5 limit 1");
        System.out.println("name==>" + query.getName());
        System.out.println("id==>" + query.getId());
    }

    /**
     * 查询集合
     */
    @Test
    public void testQueryListRecord() {
        List<TbSaviorTable> querys = dao.tableTemplate.queryList(TbSaviorTable::new, "select * from tb_savior where id = 5");
        System.out.println("size==>" + querys.size());
        System.out.println("name==>" + querys.get(0).getName());
    }


    /**
     * 查询分页
     */
    @Test
    public void testQueryPageTable() {
        Page<TbSaviorTable> querys = dao.tableTemplate.queryPage(TbSaviorTable::new, "select * from tb_savior where id = 5", 1, 10);
        System.out.println("size==>" + querys.getTotalCount());
        System.out.println("name==>" + ((List<TbSaviorTable>) querys.getList()).get(0).getName());
    }


    /**
     * 删除数据
     */
    @Test
    public void testDeleteTable() {
        /**
         * String tableName 表名
         * Record records map数据
         * String primaryKey 用于删除时使用的key(只要与这个key值相同的数据都会被删除)
         */
        dao.tableTemplate.delete("tb_savior", initTbSaviorTable(), "id");
    }

    /**
     * 批量删除数据
     */
    @Test
    public void testBatchDeleteTable() {
        /**
         * String tableName 表名
         * List<?> records map集合数据
         * String primaryKey 用于删除时使用的key(只要与这个key值相同的数据都会被删除)
         */
        dao.recordTemplate.batchDelete("tb_savior", Arrays.asList(initTbSaviorTable()), "id");
    }
}
```



## savior高级查询(SqlBuilder)

​       SqlBuilder是框架内封装的一个sql拼接工具类，主要应对列表查询中查询条件不固定的一些比较复杂的SQL,SqlBuilder把我们平常的sql主要分成三段,第一段就是select阶段，第二段就是where条件段,剩下的就是第三段，大部份的SQL都可以通过这三个阶段拼接后都能生成完整的SQL,如果确实很复杂的SQL我们只能使用字符串拼接了.....

### 1.SqlBuilder初体验

```java
public class SqlBuilderTest extends BaseSaviorTest {

    /**
     * 通过sqlBuilder查询分页
     */
    @Test
    public void testQueryModelPageBySqlBuilder(){
        SqlBuilder sqlBuilder=SqlBuilder.getSelect("select * from tb_savior");
        sqlBuilder.and(Condition.eq("id",5l));
        sqlBuilder.and(Condition.like("name","%七%"));
        Page<TbSaviorExtendsModel> pageModel = dao.modelTemplate.queryPage((Record record) -> {
            TbSaviorExtendsModel rs = new TbSaviorExtendsModel();
            rs.convert(record);
            rs.seteStr("...........");
            return rs;
        }, sqlBuilder, 1, 10);
        System.out.println("size==>"+pageModel.getTotalCount());
        System.out.println("name==>"+((List<TbSaviorExtendsModel>)pageModel.getList()).get(0).getName());
    }

    /**
     * 通过sqlBuilder查询分页
     */
    @Test
    public void testQueryTablePageBySqlBuilder(){
        SqlBuilder sqlBuilder=SqlBuilder.getSelect("select * from tb_savior");
        sqlBuilder.and(Condition.eq("id",5l));
        sqlBuilder.and(Condition.like("name","%七%"));
        Page<TbSaviorTable> pageModel = dao.tableTemplate.queryPage(TbSaviorTable::new, sqlBuilder, 1, 10);
        System.out.println("size==>"+pageModel.getTotalCount());
        System.out.println("name==>"+((List<TbSaviorExtendsModel>)pageModel.getList()).get(0).getName());
    }
}
```

### 2.SqlBuilder介绍

#### 2-1.SqlBuilder第一段构造器

  SqlBuilder只有一个单例的构造方法,我们可以通过静态方法getSelect()来创建SqlBuilder.在这里面如果是很复杂的查询这个里面的语句是在最后一个where单词前的都放到这个方法的参数里面.(注:最后的where关键字不用写，框架会根据第二阶段是否有条件存在自动加上where关键字)例:

```java
SqlBuilder sqlBuilder=SqlBuilder.getSelect("select * from tb_savior a inner join tb_savior2 b on (a.id=b.pid)");
```

#### 2-2.SqlBuilder第二段条件组装

   在这个阶段主要是做where关键字后面的条件组装，提供了比较全的条件封装类，在组装时每个条件分成两级，第一级是条件连接词(or/and),第二级条件类型(in/like .....)

```java
    @Test
    public void testSqlBuilderContition(){
        SqlBuilder sqlBuilder=SqlBuilder.getSelect("select * from tb_savior");
        //等于条件
        sqlBuilder.and(Condition.eq("id",5l));
        //不等于
        sqlBuilder.and(Condition.uEq("id",5l));
        //小于
        sqlBuilder.and(Condition.lt("id",5l));
        //大于
        sqlBuilder.and(Condition.gt("id",5l));
        //包含
        sqlBuilder.and(Condition.in("id", Arrays.asList(5l,6l)));
        //不包含
        sqlBuilder.and(Condition.uIn("id", Arrays.asList(5l,6l)));
        //模糊查询
        sqlBuilder.and(Condition.like("name","%张%"));
        //直接拼接条件值
        sqlBuilder.and(Condition.appendSql("id=4"));
        //多条件要用括号包裹起来优先执行
        //or (id=5 and id=6)
        sqlBuilder.orListAnd(Arrays.asList(Condition.eq("id",5l),Condition.eq("id",6l)));
        //and (id=5 or id=6)
        sqlBuilder.andListOr(Arrays.asList(Condition.eq("id",5l),Condition.eq("id",6l)));
        dao.recordTemplate.queryList(sqlBuilder);
    }
```

#### 2-3.SqlBuilder第三段排序分组

​    在这段中我们主要对SQL三个关键字的(group by / having / order by)进行了封装,分页没有放到这里面,是因为我们的API中封装好了分布查询，框架会根据不同的数据库变换成不同的分页语句(现只支持java程序员主流的oracel/mysql数据库,后面会增加其它的数据库)

```java
    @Test
    public void testSqlBuilder(){
        SqlBuilder sqlBuilder=SqlBuilder.getSelect("select name,count(1) as num from tb_savior");
        //增加group
        sqlBuilder.addGroupBy(GroupBy.by("name"));
        //增加order by
        sqlBuilder.addOrderBy(OrderBy.asc("name"));
        dao.recordTemplate.queryPage(sqlBuilder,1,10);
    }
```

## savior框架跨数据库支持

​      savior框架设计初期就是支持一套代码多数据库部署的,我在解决跨数据库的思路主要是分为两种情况来进行处理,一种情况是两种数据库SQL几乎都一样，只是其中的一两个函数不一样，这样我们就封装了DaoAdapter的工具类,这个类中包含了一些我们经常使用到的各数据库不同函数的适配(作者没有适配全,如果伙伴们后期在使用中遇到很适用的函数可以发邮件(863313313@qq.com)告诉作者适配到框架中),第二种情况就是我们直接在代码中写多套数据库对应的sql,到时框架根据不同的数据库存执行不同的SQL,下面我对这两种情况分别作介绍.

### 1.函数式适配

DaoAdapter类中我们适配了几个常用了sql函数,玉要如下 

| 方法                                                | 解释             |
| --------------------------------------------------- | ---------------- |
| public String concat(String... params);             | 字符连接         |
| public String ifNull(String col, String val);       | 参数中ifnull判断 |
| public String subStr(String col, int beg, int end); | 截断字符         |
| public String formateDate(String col);              | 格式化日期       |
| public String formatDateTime(String col);           | 格式化时间       |
| public String parseDate(String str);                | String转日期类型 |

下面举例说明一下:

```java
@Test
    public void testFunDaoAdapter(){
        System.out.println(dao.langTemplate.query(String.class,"select "+dao.adapter.concat("name","id") +"from tb_savior where id=1"));
    }
```

### 2.库类型适配

  根据数据库类型适配这个主要是调用compatible这个方法,这个方法会根据初始化SaviorDao这个框架里传入的数据库类型调用方法中不同的方法，而达到数据库适配的目的，例:

```java
/**
     * 根据数据库类型适配
     */
    @Test
    public void testDbTypeDaoAdapter(){
        dao.compatible(new ICompatibleCallBack<TbSaviorTable>() {
            //mysql会执行下面的方法
            @Override
            public TbSaviorTable executeMysql() {
                return dao.tableTemplate.query(TbSaviorTable::new,"select * from tb_savior limit 1");
            }
            //oracle会执行下面的方法
            @Override
            public TbSaviorTable executeOracel() {
                return dao.tableTemplate.query(TbSaviorTable::new,"SELECT * FROM  (SELECT T.* FROM (  select * from tb_savior  ) T   WHERE ROWNUM <= 10 )WHERE ROWNUM >= 1");
            }
        });
    }
```

## savior框架兼容mybatis模式

​     如果我们用了savior框架,我的宗旨是随处想用就用，在我的项目开发中我很多接口就是直接在controller中调用SaviorDao直接查询返回数据,就没有了service层和dao层这样会少了很多调用的代码,不是很大项的项目我比较建议这样做，这样代码少，开发周期短.缺点就是没有做结构分层我们到时数据表改了结果后，我们只能全局搜索对应的表名，再对其进行相应的修改.到此我针对比较怀念mybatis的,框架也提供了BaseMapper抽像类，继承他后一些基础的单表操作框架已经实现了

自定义mapper类

```java
 public class TbSaviorMapper extends BaseMapper<TbSaviorTable,Long>{

       @Override
       protected MapperTableInfo<TbSaviorTable> mapperTableInfo() {
           return new MapperTableInfo<>("tb_savior",TbSaviorTable::new,"id");
       }
       //由于测试时没有使用spring,所以加了这个构造函数，项目中用了spring不用的
       public TbSaviorMapper(SaviorDao dao){
           super(dao);
       }
       public TbSaviorExtendsModel queryModel(){
           return dao.modelTemplate.query((record) -> {
               TbSaviorExtendsModel rs = new TbSaviorExtendsModel();
               rs.convert(record);
               rs.seteStr("......");
               return rs;
           }, "select * from tb_savior where id = 3 limit 1");
       }
   }
```

测试方法

```java
   @Test
    public void testQueryTable(){
       TbSaviorMapper tbSaviorMapper=new TbSaviorMapper(this.dao);
       System.out.println(tbSaviorMapper.queryTable(1l).getName());
   }
```

