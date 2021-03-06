/*******************************************************************************
 * Copyright (c) 2008, 2010 VMware Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   VMware Inc. - initial contribution
 *******************************************************************************/

package org.eclipse.virgo.snaps.core.internal.webapp.container;

import java.io.IOException;

import javax.servlet.ServletException;

import org.eclipse.virgo.snaps.core.internal.webapp.SnapServletContext;


final class ManagerUtils {
    
    static Class<?> loadComponentClass(String className, ClassLoader fallbackClassLoader) throws ClassNotFoundException {
        try {
            return ManagerUtils.class.getClassLoader().loadClass(className);
        } catch (ClassNotFoundException cnfe) {
            return fallbackClassLoader.loadClass(className);
        }
    }
    
    static <T> T doWithThreadContextClassLoader(ClassLoader classLoader, ClassLoaderCallback<T> callback) throws ServletException, IOException {
        Thread currentThread = Thread.currentThread();
        ClassLoader contextClassLoader = currentThread.getContextClassLoader();
        try {
            currentThread.setContextClassLoader(classLoader);
            return callback.doWithClassLoader();
        } finally {
            currentThread.setContextClassLoader(contextClassLoader);
        }
    }

    static interface ClassLoaderCallback<T> {

        T doWithClassLoader() throws ServletException, IOException;
    }
    
    static String expandMapping(String urlPattern, SnapServletContext snapServletContext) {
        if ("/".equals(urlPattern) || urlPattern.startsWith("*.")) {
            return urlPattern;
        } else {
            return snapServletContext.getSnapContextPath() + urlPattern;
        }
    }
}
