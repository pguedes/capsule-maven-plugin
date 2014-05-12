package com.github.pguedes.maven.capsule;

import org.codehaus.plexus.util.IOUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class TemplateCapsuleClass implements Iterable<CapsuleEntry>, CapsuleEntry {
    public static final String CAPSULE_CLASS = "Capsule.class";

    @Override
    public String getName() {
        return CAPSULE_CLASS;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream capsuleInputStream = getClass().getClassLoader().getResourceAsStream("capsule-0.4.0-SNAPSHOT.jar");

        JarInputStream capsuleJarInputStream = new JarInputStream(capsuleInputStream);

        JarEntry entry;
        while ((entry = capsuleJarInputStream.getNextJarEntry()) != null) {
            if (entry.getName().equals(CAPSULE_CLASS)) {
                byte[] data = IOUtil.toByteArray(capsuleJarInputStream);
                return new ByteArrayInputStream(data);
            }
        }
        throw new IOException("unable to find Capsule.class entry in capsule project jar");
    }

    @Override
    public Iterator<CapsuleEntry> iterator() {
        return Collections.<CapsuleEntry>singleton(this).iterator();
    }
}
