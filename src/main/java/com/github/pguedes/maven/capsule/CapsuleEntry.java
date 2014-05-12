package com.github.pguedes.maven.capsule;

import java.io.IOException;
import java.io.InputStream;

public interface CapsuleEntry {
    public String getName();
    public InputStream getInputStream() throws IOException;
}
