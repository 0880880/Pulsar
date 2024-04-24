package com.pulsar;

import com.pulsar.api.Component;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Set;

public class JavaComponentClassLoader extends URLClassLoader {

	private final JavaComponentLoader loader;
	private final Map<String, Class<?>> classes = new java.util.concurrent.ConcurrentHashMap<String, Class<?>>();
	private final File file;
	Component component;

	JavaComponentClassLoader(final JavaComponentLoader loader, final ClassLoader parent, final File file) throws MalformedURLException, ClassNotFoundException {
		super(new URL[] {file.toURI().toURL()}, parent);
		Validate.notNull(loader, "Loader cannot be null");

		this.loader = loader;
		this.file = file;

		try {
			Class<?> jarClass;
            jarClass = Class.forName(FilenameUtils.removeExtension(file.getName()), true, this);

            Class<? extends Component> scriptClass;
            scriptClass = jarClass.asSubclass(Component.class);

			component = scriptClass.getConstructor().newInstance();
		} catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| IllegalAccessException | InstantiationException | SecurityException e) {
            e.printStackTrace();
        }
	}

	@Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return findClass(name, true);
    }

	Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
        Class<?> result = classes.get(name);

        if (result == null) {
            if (checkGlobal) {
            	result = loader.getClassByName(name);
            }

            if (result == null) {
                result = super.findClass(name);

                if (result != null) {
                    loader.setClass(name, result);
                }
            }

            classes.put(name, result);
        }

        return result;
    }

	Set<String> getClasses() {
        return classes.keySet();
    }

}
