package com.a.router_processor.bean;

import javax.lang.model.element.Element;

public class RouterBean {

    public enum TypeEnum {
        ACTIVITY,
        CALL
    }

    /**
     * 枚举类型
     */
    private TypeEnum typeEnum;

    /**
     * 类节点
     */
    private Element element;

    /**
     * 被注解的class对象
     */
    private Class<?> myClass;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 路由组
     */
    private String group;

    public TypeEnum getTypeEnum() {
        return typeEnum;
    }

    public Element getElement() {
        return element;
    }

    public Class<?> getMyClass() {
        return myClass;
    }

    public String getPath() {
        return path;
    }

    public void setTypeEnum(TypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public void setMyClass(Class<?> myClass) {
        this.myClass = myClass;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public RouterBean(TypeEnum typeEnum, Class<?> myClass, String path, String group) {
        this.typeEnum = typeEnum;
        this.myClass = myClass;
        this.path = path;
        this.group = group;
    }

    public static RouterBean create(TypeEnum typeEnum, Class<?> myClass, String path, String group) {
        return new RouterBean(typeEnum, myClass, path, group);
    }

    private RouterBean(Builder builder) {
        this.typeEnum = builder.type;
        this.element = builder.element;
        this.myClass = builder.clazz;
        this.path = builder.path;
        this.group = builder.group;
    }

    /**
     * 建造者模式
     */
    public static class Builder {
        // 枚举类型：Activity
        private TypeEnum type;
        // 类节点
        private Element element;
        // 注解使用的类对象
        private Class<?> clazz;
        // 路由地址
        private String path;
        // 路由组
        private String group;

        public Builder addType(TypeEnum typeEnum) {
            this.type = typeEnum;
            return this;
        }

        public Builder addElement(Element element) {
            this.element = element;
            return this;
        }

        public Builder addClazz(Class<?> clazz) {
            this.clazz = clazz;
            return this;
        }

        public Builder addPath(String path) {
            this.path = path;
            return this;
        }

        public Builder addGroup(String group) {
            this.group = group;
            return this;
        }

        public RouterBean build() {
            if (path == null || path.length() == 0) {
                throw new IllegalArgumentException("path 不能为空");
            }
            return new RouterBean(this);
        }
    }

    @Override
    public String toString() {
        return "RouterBean{" +
                " path='" + path + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
