package com.github.pguedes.maven.capsule;

import org.codehaus.plexus.util.IOUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

/**
 * An object that knows how to write the capsule (jar) file.
 */
public class CapsuleWriter {
    /**
     * The file name for the capsule created
     */
    private final File capsulePath;
    /**
     * The capsule output stream to write the capsule to.
     */
    private JarOutputStream target;

    public CapsuleWriter(File capsulePath) {
        this.capsulePath = capsulePath;
    }

    /**
     * Get the target {@link JarOutputStream} to write the capsule to.
     *
     * @return {@link JarOutputStream} to write the capsule to
     */
    private JarOutputStream getTarget() throws IOException {
        if (this.target == null) {
            FileOutputStream capsuleFileStream = new FileOutputStream(capsulePath);
            this.target = new JarOutputStream(capsuleFileStream);
        }
        return this.target;
    }

    public void write(Iterable<CapsuleEntry> entries) throws IOException {
        JarOutputStream outputStream = getTarget();
        for (CapsuleEntry entry : entries) {
            outputStream.putNextEntry(new ZipEntry(entry.getName()));
            IOUtil.copy(entry.getInputStream(), outputStream);
            outputStream.closeEntry();
        }
    }

    /**
     * Close the underlying stream
     */
    public void close() {
        if (target != null) {
            IOUtil.close(target);
        }
    }
}
