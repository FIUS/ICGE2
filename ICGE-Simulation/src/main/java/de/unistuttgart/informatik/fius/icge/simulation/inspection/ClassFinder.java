/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/neumantm/ICGE
 * 
 * Copyright (c) 2018 the ICGE project authors.
 */

package de.unistuttgart.informatik.fius.icge.simulation.inspection;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * A class for finding classes in the class loader
 * 
 * @author Tim Neumann
 */
public class ClassFinder {
    private ClassFinder() {
        //hide constructor
    }

    /**
     * Get all classes in the current context class loader, which match the filter.
     * 
     * @param filter
     *            The filter to check each class against.
     * @return A list of classes
     * @throws IOException
     *             When an IO Error occurs.
     */
    public static List<Class<?>> getClassesInClassLoader(Predicate<Class<?>> filter) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        List<URL> urls = Collections.list(loader.getResources("de"));

        List<Class<?>> classes = new ArrayList<>();

        for (URL url : urls) {
            if (url.getProtocol().equals("jar")) {
                loadClassesFromJar(url, filter, classes);
            } else {
                loadClassesFromFS(url, filter, classes, loader);
            }
        }

        return classes;
    }

    private static void loadClassesFromJar(URL url, Predicate<Class<?>> filter, List<Class<?>> classes)
            throws IOException {
        String urlS = url.getPath();
        String outerUrl = urlS.substring(0, urlS.indexOf('!'));
        String innerUrl = urlS.substring(urlS.indexOf('!') + 2);
        try (JarFile jar = new JarFile(new File(new URL(outerUrl).toURI()))) {
            List<JarEntry> entries = Collections.list(jar.entries());
            for (JarEntry e : entries) {
                if (e.getName().endsWith(".class") && e.getName().startsWith(innerUrl)) {
                    String className = convertPathToClassName(e.getName(), "");
                    className = className.substring(0, className.length() - 6);
                    if (className.equals("module-info")) continue;
                    Class<?> cls = Class.forName(className);
                    if (filter.test(cls)) {
                        classes.add(cls);
                    }
                }
            }
        } catch (ClassNotFoundException e1) {
            throw new IOException(e1);
        } catch (URISyntaxException e2) {
            throw new IOException(e2);
        }
    }

    private static void loadClassesFromFS(URL url, Predicate<Class<?>> filter, List<Class<?>> classes, ClassLoader loader)
            throws IOException {
        try {
            File rootDir = new File(url.toURI()).getParentFile();
            loadClassInFile(rootDir, classes, loader, rootDir.getPath(), filter);
        } catch (URISyntaxException | ClassNotFoundException e) {
            throw new IOException(e);
        }
    }

    private static void loadClassInFile(File file, List<Class<?>> classes, ClassLoader loader, String rootDir,
            Predicate<Class<?>> filter) throws ClassNotFoundException {
        if (!file.exists()) throw new IllegalArgumentException("File does not exist.");
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                loadClassInFile(f, classes, loader, rootDir, filter);
            }
        } else {
            String path = file.getPath();
            if (path.endsWith(".class")) {
                String className = convertPathToClassName(path, rootDir);
                className = className.substring(0, className.length() - 6);
                if (className.equals("module-info")) return;
                Class<?> cls = Class.forName(className);
                if (filter.test(cls)) {
                    classes.add(cls);
                }
            }
        }
    }

    private static String convertPathToClassName(String path, String rootDir) {
        if (!path.startsWith(rootDir)) throw new IllegalStateException("File not starting with root dir!");
        String[] fileSeps = new String[] { System.getProperty("file.separator"), "/" };
        String relPath = path.substring(rootDir.length());
        for (String fileSep : fileSeps) {
            if (relPath.startsWith(fileSep)) {
                relPath = relPath.substring(1);
            }
            relPath = relPath.replace(fileSep, ".");
        }
        return relPath;

    }
}
