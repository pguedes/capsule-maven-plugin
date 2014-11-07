package com.github.pguedes.maven.capsule;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface for all entries to be included in the capsule jar file.
 */
public interface CapsuleEntry {
    /**
     * The name of the entry to include in the capsule.
     *
     * @return name of the entry to include in the capsule.
     */
    public String getName();

    /**
     * The {@link InputStream} with the data to write out into the capsule.
     *
     * @return the input stream that where we can read the data to write into the capsule.
     * @throws IOException if we cannot get the input stream
     */
    public InputStream getInputStream() throws IOException;
}
