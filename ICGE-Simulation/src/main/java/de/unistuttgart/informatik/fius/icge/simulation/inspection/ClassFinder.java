/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
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
     *     The filter to check each class against.
     * @return A list of classes
     * @throws IOException
     *     When an IO Error occurs.
     */
    public static List<Class<?>> getClassesInClassLoader(final Predicate<Class<?>> filter) throws IOException {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final List<URL> urls = Collections.list(loader.getResources("de"));
        
        final List<Class<?>> classes = new ArrayList<>();
        
        for (final URL url : urls) {
            if (url.getProtocol().equals("jar")) {
                ClassFinder.loadClassesFromJar(url, filter, classes);
            } else {
                ClassFinder.loadClassesFromFS(url, filter, classes, loader);
            }
        }
        
        return classes;
    }
    
    private static void loadClassesFromJar(final URL url, final Predicate<Class<?>> filter, final List<Class<?>> classes)
            throws IOException {
        final String urlS = url.getPath();
        final String outerUrl = urlS.substring(0, urlS.indexOf('!'));
        final String innerUrl = urlS.substring(urlS.indexOf('!') + 2);
        try (JarFile jar = new JarFile(new File(new URL(outerUrl).toURI()))) {
            final List<JarEntry> entries = Collections.list(jar.entries());
            for (final JarEntry e : entries) {
                if (e.getName().endsWith(".class") && e.getName().startsWith(innerUrl)) {
                    String className = ClassFinder.convertPathToClassName(e.getName(), "");
                    className = className.substring(0, className.length() - 6);
                    if (className.equals("module-info")) {
                        continue;
                    }
                    final Class<?> cls = Class.forName(className);
                    if (filter.test(cls)) {
                        classes.add(cls);
                    }
                }
            }
        } catch (final ClassNotFoundException e1) {
            throw new IOException(e1);
        } catch (final URISyntaxException e2) {
            throw new IOException(e2);
        }
    }
    
    private static void loadClassesFromFS(
            final URL url, final Predicate<Class<?>> filter, final List<Class<?>> classes, final ClassLoader loader
    ) throws IOException {
        try {
            final File rootDir = new File(url.toURI()).getParentFile();
            ClassFinder.loadClassInFile(rootDir, classes, loader, rootDir.getPath(), filter);
        } catch (URISyntaxException | ClassNotFoundException e) {
            throw new IOException(e);
        }
    }
    
    private static void loadClassInFile(
            final File file, final List<Class<?>> classes, final ClassLoader loader, final String rootDir, final Predicate<Class<?>> filter
    ) throws ClassNotFoundException {
        if (!file.exists()) throw new IllegalArgumentException("File does not exist.");
        if (file.isDirectory()) {
            for (final File f : file.listFiles()) {
                ClassFinder.loadClassInFile(f, classes, loader, rootDir, filter);
            }
        } else {
            final String path = file.getPath();
            if (path.endsWith(".class")) {
                String className = ClassFinder.convertPathToClassName(path, rootDir);
                className = className.substring(0, className.length() - 6);
                if (className.equals("module-info")) return;
                final Class<?> cls = Class.forName(className);
                if (filter.test(cls)) {
                    classes.add(cls);
                }
            }
        }
    }
    
    private static String convertPathToClassName(final String path, final String rootDir) {
        if (!path.startsWith(rootDir)) throw new IllegalStateException("File not starting with root dir!");
        final String[] fileSeps = new String[] { System.getProperty("file.separator"), "/" };
        String relPath = path.substring(rootDir.length());
        for (final String fileSep : fileSeps) {
            if (relPath.startsWith(fileSep)) {
                relPath = relPath.substring(1);
            }
            relPath = relPath.replace(fileSep, ".");
        }
        return relPath;
        
    }
}
