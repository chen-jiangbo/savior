package com.jiangbo.savior.coder.file;

import java.io.*;

public class FileGenerator {

    public static void generateFile(String basdDir, String modleName, String content) throws Exception {
        System.out.println(content);
        String entityFileUrl = basdDir + "/" + modleName + ".java";
        File entityFile = new File(entityFileUrl);
        if (!entityFile.exists()) {
            //实体类若存在则不重新生成
            entityFile.delete();
        }
        if (!entityFile.getParentFile().exists()) {
            entityFile.getParentFile().mkdirs();
        }
        entityFile.createNewFile();
        InputStream is = new ByteArrayInputStream(content.getBytes("UTF-8"));
        OutputStream out = new FileOutputStream(entityFile);
        int i;
        while ((i = is.read()) != -1) {
            out.write(i);
        }
        out.flush();
        out.close();
    }
}
