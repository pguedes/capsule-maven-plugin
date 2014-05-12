package com.github.pguedes.maven.capsule;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;

public class SingleFileEntry implements Iterable<CapsuleEntry> {
    private final File file;

    public SingleFileEntry(File file) {
        this.file = file;
    }

    @Override
    public Iterator<CapsuleEntry> iterator() {
        return Collections.<CapsuleEntry>singleton(new FileCapsuleEntry(file)).iterator();
    }
}
