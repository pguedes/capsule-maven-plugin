package com.github.pguedes.maven.capsule;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import static java.util.jar.Attributes.Name.MAIN_CLASS;

public class CapsuleManifest implements Iterable<CapsuleEntry>, CapsuleEntry {
    private static final Attributes.Name APPLICATION_CLASS = new Attributes.Name("Application-Class");
    private static final Attributes.Name BUILT_BY = new Attributes.Name("Built-By");

    private final String mainClass;

    public CapsuleManifest(String mainClass) {
        this.mainClass = mainClass;
    }

    @Override
    public Iterator<CapsuleEntry> iterator() {
        return Collections.<CapsuleEntry>singleton(this).iterator();
    }

    @Override
    public String getName() {
        return JarFile.MANIFEST_NAME;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        Manifest manifest = new Manifest();
        Attributes attributes = manifest.getMainAttributes();
        attributes.put(MAIN_CLASS, "Capsule");
        attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
        attributes.put(BUILT_BY, "Maven Capsule plugin");
        attributes.put(APPLICATION_CLASS, mainClass);

        // write to a temp byte array to conform to API
        ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
        manifest.write(dataStream);
        // return input stream based on byte array
        byte[] bytes = dataStream.toByteArray();
        System.out.printf("\n\nbuilt manifest bytes: %d\n\n", bytes.length);
        return new ByteArrayInputStream(bytes);
    }
}
