/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.addin;

import com.lumens.LumensException;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class AddinDefaultContext implements AddinContext {

    private Map<String, ServiceEntity> services = new HashMap<>();
    private List<Addin> addinList = new ArrayList<>();
    private AddinEngine addinEngine;

    @Override
    public List<Addin> getAddins() {
        return addinList;
    }

    @Override
    public List<ServiceEntity> getServices() {
        return new ArrayList<>(services.values());
    }

    protected static class AddinImpl implements Addin {

        private AddinContext addinContext;
        private String parentPath;
        private AddinURLClassLoader urlClassLoader;
        private Manifest manifest;
        private String addinName;

        public AddinImpl(AddinContext context, String parentPath, AddinURLClassLoader urlClassLoader) {
            this.addinContext = context;
            this.parentPath = parentPath;
            this.urlClassLoader = urlClassLoader;
        }

        @Override
        public String toString() {
            return "Addin {" + "Name=" + addinName + ", Path=" + parentPath + '}';
        }

        @Override
        public String getName() {
            return addinName;
        }

        @Override
        public ServiceEntity getService(String serviceIdentifier) {
            return addinContext.getService(serviceIdentifier);
        }

        @Override
        public void start() {
            // Lazy loading
            try {
                File path = new File(parentPath);
                if (path.isFile()) {
                    manifest = new JarFile(path).getManifest();
                    addinName = manifest.getMainAttributes().getValue(AddinConstants.ADDIN_NAME);
                    String activatorClassname = manifest.getMainAttributes().getValue(AddinConstants.ADDIN_ACTIVATOR);
                    AddinActivator activator = (AddinActivator) (urlClassLoader.addJarLocationOrPathLoactionURL(path.toURI().toURL()).loadClass(activatorClassname).newInstance());
                    activator.start(addinContext);
                } else {
                    for (File jar : path.listFiles(AddinURLClassLoader.jarFliter)) {
                        manifest = new JarFile(jar).getManifest();
                        addinName = manifest.getMainAttributes().getValue(AddinConstants.ADDIN_NAME);
                        loadDependencyJarFile(parentPath, manifest, urlClassLoader);
                        String activatorClassname = manifest.getMainAttributes().getValue(AddinConstants.ADDIN_ACTIVATOR);
                        AddinActivator activator = (AddinActivator) (urlClassLoader.addJarLocationOrPathLoactionURL(path.toURI().toURL()).loadClass(activatorClassname).newInstance());
                        activator.start(addinContext);
                    }
                }
            } catch (Exception ex) {
                throw new LumensException(ex);
            }
        }

        @Override
        public void stop() {
            // TODO
        }

        private static void loadDependencyJarFile(String path, Manifest mf, AddinURLClassLoader acl) throws Exception {
            String classPathDefine = mf.getMainAttributes().getValue(AddinConstants.ADDIN_CLASSPATH);
            if (classPathDefine != null && !classPathDefine.isEmpty()) {
                classPathDefine = classPathDefine.trim();
                String[] classPaths = classPathDefine.split(",");
                for (String classPath : classPaths) {
                    File dependencyClassPathFile = new File(path + '/' + classPath);
                    acl.addJarLocationOrPathLoactionURL(dependencyClassPathFile.toURI().toURL());
                }
            }
        }

        @Override
        public Addin getAddin(String name) {
            return addinContext.getAddin(name);
        }
    }

    public AddinDefaultContext(AddinEngine engine) {
        this.addinEngine = engine;
    }

    @Override
    public <T> T registerService(String identifier, T service, Map<String, Object> props) {
        // TODO check ID, Name, ICON
        services.put(identifier, new ServiceEntity<>(service, props));
        return service;
    }

    @Override
    public ServiceEntity getService(String identifier) {
        return services.get(identifier);
    }

    @Override
    public Addin installAddIn(URL url) {
        try {
            File path = new File(url.toURI());
            if (!path.exists())
                throw new LumensException(String.format("Path '%s' can not be found !", url.toString()));
            Addin addin = new AddinImpl(this, path.getAbsolutePath(), addinEngine.newDefaultAddinClassLoader());
            addinList.add(addin);
            return addin;
        } catch (URISyntaxException ex) {
            throw new LumensException(ex);
        }
    }

    @Override
    public void start() {
    }

    @Override
    public Addin getAddin(String name) {
        for (Addin addin : addinList)
            if (addin.getName().equals(name))
                return addin;
        return null;
    }

    @Override
    public void stop() {
        services = new HashMap<>();
    }
}