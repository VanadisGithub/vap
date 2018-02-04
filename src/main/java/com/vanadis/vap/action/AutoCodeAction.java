package com.vanadis.vap.action;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoCodeAction {

    private static Logger log = Logger.getLogger(AutoCodeAction.class);

    /**
     * 属性类型枚举类
     */
    public enum PropertyType {
        Byte, Short, Int, Long, Boolean, Float, Double, String, ByteArray, Date
    }

    private static File javaFile = null;

    public static void autoCode() {
        Configuration cfg = new Configuration();
        try {
            // 步骤一：指定 模板文件从何处加载的数据源，这里设置一个文件目录
            cfg.setDirectoryForTemplateLoading(new File("src/main/resources/static/template"));
            cfg.setObjectWrapper(new DefaultObjectWrapper());

            // 步骤二：获取 模板文件
            Template template = cfg.getTemplate("entity.ftl");

            // 步骤三：创建 数据模型
            Map<String, Object> root = createDataModel();

            // 步骤四：合并 模板 和 数据模型
            // 创建.java类文件
            if (javaFile != null) {
                Writer javaWriter = new FileWriter(javaFile);
                template.process(root, javaWriter);
                javaWriter.flush();
                System.out.println("文件生成路径：" + javaFile.getCanonicalPath());

                javaWriter.close();
            }
            // 输出到Console控制台
            Writer out = new OutputStreamWriter(System.out);
            template.process(root, out);
            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }

    }

    /**
     * 创建数据模型
     *
     * @return
     */
    private static Map<String, Object> createDataModel() {
        Map<String, Object> root = new HashMap<String, Object>();
        Entity user = new Entity();
        user.setPackageName("com.vanadis.vap.model"); // 创建包名
        user.setClassName("Users");  // 创建类名
        user.setConstructors(true); // 是否创建构造函数
        // user.setSuperclass("person");

        List<Property> propertyList = new ArrayList<Property>();

        // 创建实体属性一
        Property attribute1 = new Property();
        attribute1.setJavaType("String");
        attribute1.setPropertyName("name");
        attribute1.setPropertyType(PropertyType.String);

        // 创建实体属性二
        Property attribute2 = new Property();
        attribute2.setJavaType("int");
        attribute2.setPropertyName("age");
        attribute2.setPropertyType(PropertyType.Int);

        // 创建实体属性二
        Property attribute3 = new Property();
        attribute3.setJavaType("int");
        attribute3.setPropertyName("ageYear");
        attribute3.setPropertyType(PropertyType.Int);

        propertyList.add(attribute1);
        propertyList.add(attribute2);
        propertyList.add(attribute3);

        // 将属性集合添加到实体对象中
        user.setProperties(propertyList);

        // 创建.java类文件
        File outDirFile = new File("./src/main/java");
        if (!outDirFile.exists()) {
            outDirFile.mkdir();
        }

        javaFile = toJavaFilename(outDirFile, user.getPackageName(), user.getClassName());

        root.put("entity", user);
        return root;
    }

    /**
     * 创建.java文件所在路径 和 返回.java文件File对象
     *
     * @param outDirFile    生成文件路径
     * @param javaPackage   java包名
     * @param javaClassName java类名
     * @return
     */
    private static File toJavaFilename(File outDirFile, String javaPackage, String javaClassName) {
        String packageSubPath = javaPackage.replace('.', '/');
        File packagePath = new File(outDirFile, packageSubPath);
        File file = new File(packagePath, javaClassName + ".java");
        if (!packagePath.exists()) {
            packagePath.mkdirs();
        }
        return file;
    }

    /**
     * 实体类
     */
    public static class Entity {
        // 实体所在的包名
        private String packageName;
        // 实体类名
        private String className;
        // 父类名
        private String superClassName;
        // 接口
        private String implementsList;
        // 属性集合
        List<Property> properties;
        // 是否有构造函数
        private boolean constructors;

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getSuperClassName() {
            return superClassName;
        }

        public void setSuperClassName(String superClassName) {
            this.superClassName = superClassName;
        }

        public String getImplementsList() {
            return implementsList;
        }

        public void setImplementsList(String implementsList) {
            this.implementsList = implementsList;
        }

        public List<Property> getProperties() {
            return properties;
        }

        public void setProperties(List<Property> properties) {
            this.properties = properties;
        }

        public boolean isConstructors() {
            return constructors;
        }

        public void setConstructors(boolean constructors) {
            this.constructors = constructors;
        }
    }

    /**
     * 实体对应的属性类
     */
    public static class Property {
        // 属性数据类型
        private String javaType;
        // 属性名称
        private String propertyName;

        private PropertyType propertyType;

        public String getJavaType() {
            return javaType;
        }

        public void setJavaType(String javaType) {
            this.javaType = javaType;
        }

        public String getPropertyName() {
            return propertyName;
        }

        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        public PropertyType getPropertyType() {
            return propertyType;
        }

        public void setPropertyType(PropertyType propertyType) {
            this.propertyType = propertyType;
        }

    }

}
