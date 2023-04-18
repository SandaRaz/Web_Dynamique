package etu2079.framework;

import java.lang.reflect.Method;

public class Mapping {
    String className;
    Method method;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Mapping() {
    }

    public Mapping(String className, Method method) {
        this.setClassName(className);
        this.setMethod(method);
    }
}
