package com.github.pguedes.maven.capsule;

import org.apache.maven.artifact.Artifact;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MavenProjectDependencies implements Iterable<CapsuleEntry> {
    private final Collection<Artifact> artifacts;

    public MavenProjectDependencies(Collection<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    private List<CapsuleEntry> collectDepencencies() {
        List<CapsuleEntry> files = new ArrayList<CapsuleEntry>();
        for (Artifact artifact : artifacts) {
            File file = artifact.getFile();
            files.add(new FileCapsuleEntry(file));
        }
        return files;
    }

    @Override
    public Iterator<CapsuleEntry> iterator() {
        return collectDepencencies().iterator();
    }
}
