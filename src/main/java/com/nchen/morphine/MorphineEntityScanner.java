package com.nchen.morphine;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class MorphineEntityScanner {
    private final static String CLASS_EXT = ".class";

    public ArrayList<URL> getJarListUrls() {
        ArrayList<URL> urls = new ArrayList<URL>();
        ClassLoader[] classloaders = {
                getClass().getClassLoader(),
                Thread.currentThread().getContextClassLoader()
        };

        for (int i = 0; i < classloaders.length; i++) {
            if (classloaders[i] instanceof URLClassLoader) {
                urls.addAll(Arrays.asList(((URLClassLoader) classloaders[i]).getURLs()));
            } else {
                throw new RuntimeException("classLoader is not an instanceof URLClassLoader");
            }
        }
        return urls;
    }

    static List<Class> scanPackageForEntities(String scanPackageName) throws IOException {
        Enumeration<URL> resources = Thread.currentThread()
                .getContextClassLoader()
                .getResources(scanPackageName.replace('.', '/'));

        List<Class> entities = new ArrayList<Class>();

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File file = new File(resource.getPath());
            setEntities(scanPackageName, file, entities);
        }

        return entities;
    }

    private static void setEntities(String scanPackageName, File file, List<Class> classes) {
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                setEntities(scanPackageName, f, classes);
            } else if(f.getName().endsWith(CLASS_EXT)){
                try {
                    String classId = f.getPath().replace('\\', '.');
                    String className = classId.substring(classId.indexOf(scanPackageName), classId.length()  - CLASS_EXT.length());
                    Class annoClass = Class.forName(className,true,
                            Thread.currentThread().getContextClassLoader());

                    if(annoClass.isAnnotationPresent(Entity.class)) {
                        classes.add(annoClass);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
