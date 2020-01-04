package com.jiangbo.savior.coder.template;


import com.jiangbo.savior.coder.config.CoderConfig;
import com.jiangbo.savior.coder.model.Column;
import com.jiangbo.savior.coder.model.Table;

public class TableTemplate implements Template {

    private CoderConfig coderConfig;

    @Override
    public TableTemplate initCoderConfig(CoderConfig coderConfig) {
        this.coderConfig=coderConfig;
        return this;
    }

    @Override
    public String parseTemplate(Table table) {
        return "package " + table.getPackageName() + ";\n" +
                (coderConfig.isGenSwaggerAnnotation()?
                "import io.swagger.annotations.ApiModel;\n" +
                "import io.swagger.annotations.ApiModelProperty;\n":"") +
                "import com.leyou.acgn.base.repository.model.Record;\n" +
                "import com.leyou.acgn.base.repository.model.BaseTable;\n" +
                (coderConfig.isSupportLombok()?"import lombok.Data;\n"+
                "import lombok.EqualsAndHashCode;\n":"")+
                "\n" +
                (coderConfig.isGenSwaggerAnnotation()?"@ApiModel(\"" + table.getTableDesc() + "\")\n":"") +
                (coderConfig.isSupportLombok()?"@Data\n"+
                "@EqualsAndHashCode(callSuper=true)\n":"")+
                "public class " + table.getModleName() + " extends BaseTable{\n" +
                "\n" +
                "public "+ table.getModleName() +"(){\n" +
                "        super();\n" +
                "    }" +
                "\n"+
                "    @Override\n" +
                "    public void convert(Record record) {\n" +
                getConvertStr(table) +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    /**" + table.getTableDesc() + "对象转换成record*/\n" +
                "    public Record reversal(){\n" +
                getReversalStr(table) +
                "    }\n" +
                "\n" +
                getVariablesStr(table) +
                "\n" +
                (coderConfig.isSupportLombok()?"":getMethodsStr(table))+
                "\n" +
                "}";
    }

    private String getConvertStr(Table table) {
        StringBuffer sf = new StringBuffer();
        for (Column column : table.getColumns()) {
            sf.append("     set" + column.getMethodName() + "(record.get" + column.getJavaSimpleTyep() + "(\"" + column.getColumnName() + "\"));\n");
        }
        return sf.toString();
    }

    private String getReversalStr(Table table) {
        StringBuffer sf = new StringBuffer("        return new Record()");
        for (Column column : table.getColumns()) {
            sf.append("\n");
            sf.append("         .setColumn(\"" + column.getColumnName() + "\",this.get" + column.getMethodName() + "())");
        }
        sf.append(";");
        return sf.toString();
    }

    private String getVariablesStr(Table table) {
        StringBuffer sf = new StringBuffer();
        for (Column column : table.getColumns()) {
            sf.append((coderConfig.isGenSwaggerAnnotation()?"    @ApiModelProperty(value =\"" + column.getColumnDesc() + "\",notes = \"" + column.getColumnDesc() + "\")\n":"")+
                    "    private " + column.getJavaType() + " " + column.getPropName() + ";\n");
        }
        return sf.toString();
    }

    private String getMethodsStr(Table table) {
        StringBuffer sf = new StringBuffer();
        for (Column column : table.getColumns()) {
            sf.append("    /**获取" + column.getColumnDesc() + "*/\n" +
                    "    public " + column.getJavaType() + " get" + column.getMethodName() + "(){\n" +
                    "         return this." + column.getPropName() + ";\n" +
                    "    }\n" +
                    "    /**设置" + column.getColumnDesc() + "*/\n" +
                    "    public void set" + column.getMethodName() + "(" + column.getJavaType() + " " + column.getPropName() + "){\n" +
                    "         this." + column.getPropName() + "=" + column.getPropName() + ";\n" +
                    "    }\n");
        }
        return sf.toString();
    }
}
