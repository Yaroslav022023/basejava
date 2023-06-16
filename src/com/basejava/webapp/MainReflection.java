package com.basejava.webapp;

import com.basejava.webapp.model.Resume;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainReflection {
    public static void main(String[] arg) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Resume resume = new Resume();

        Method method = resume.getClass().getDeclaredMethod("toString");
        String invokeToString = (String) method.invoke(resume);

        System.out.println(invokeToString);
    }
}