package com.pulsar;

import com.badlogic.gdx.Gdx;
import com.pulsar.api.Component;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class JavaComponentLoader {

	private final Map<String, JavaComponentClassLoader> loaders = new LinkedHashMap<String, JavaComponentClassLoader>();
	private final Map<String, Class<?>> classes = new java.util.concurrent.ConcurrentHashMap<String, Class<?>>();

	public Component loadScript(File file) throws FileNotFoundException, MalformedURLException, ClassNotFoundException {
		Validate.notNull(file, "File cannot be null");

		if (!file.exists()) {
            throw new FileNotFoundException(file.getPath() + " does not exist");
        }

		final JavaComponentClassLoader loader;
        loader = new JavaComponentClassLoader(this, getClass().getClassLoader(), file);

		loaders.put(FilenameUtils.removeExtension(file.getName()), loader);

		return loader.component;
	}

    Class<?> getClassByName(final String name) {
        Class<?> cachedClass = classes.get(name);

        if (cachedClass != null) {
            return cachedClass;
        } else {
            for (String current : loaders.keySet()) {
                JavaComponentClassLoader loader = loaders.get(current);

                try {
                    cachedClass = loader.findClass(name, false);
                } catch (ClassNotFoundException e) {
                    Gdx.app.log("[JCL]", "Class Not Found", e);
                }
                if (cachedClass != null) {
                    return cachedClass;
                }
            }
        }
        return null;
    }

	void setClass(final String name, final Class<?> clazz) {
        if (!classes.containsKey(name)) {
            classes.put(name, clazz);
        }
    }

}
