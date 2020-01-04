# savior使用手册

## savior框架介绍

   作者是一个现实中很实在的一个JAVA服务端码农,在平时的开发项目中我发现我们有很多的代码是在操作数据库，目前可供我们选择的就只有mybatis和hibernate两个主流框架.由于hibernate的笨重,现在很少有项目使用.所以大多数的公司都使用mybatis框架.我不得不承认这些都是很优秀的框架.我个人是个比较懒的人，能一行代码搞定的就不想多些一行。由于这样的洁癖促使我萌生了一个想法,能不能让我们的数据库操作在任何地方想用就用,而且只需要调用一行代码就能搞定一次数据操作.就这样我开始了我的这个想法........

## savior基础入门

### 1.创建dao操作类

#### 1-1.java原生创建

```java
DataSource dataSource=initDataSource(); //初始化dataSource
SaviorDao dao = new SaviorDao(dataSource, DbTypeEnum.MYSQL);
```

#### 1-2.spring-jdbc项目创建

```java
@Autowired
private NamedParameterJdbcTemplate namedParameterJdbcTemplate;//注入jdbcTemplate
SaviorDao dao = SaviorDao(namedParameterJdbcTemplate, DbTypeEnum dbTypeEnum)
```

### 2.helloword走起

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

### 3.基础总结

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
            this.dataSource.setUrl("jdbc:mysql://193.112.2.226:9086/savior?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai&generateSimpleParameterMetadata=true&useInformationSchema=true");
            this.dataSource.setUsername("anzun");
            this.dataSource.setPassword("anzun");
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



