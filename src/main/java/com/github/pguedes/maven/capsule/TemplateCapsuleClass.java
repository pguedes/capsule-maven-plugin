package com.github.pguedes.maven.capsule;

import org.codehaus.plexus.util.IOUtil;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.*;

import java.io.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class TemplateCapsuleClass implements Iterable<CapsuleEntry>, CapsuleEntry {
    public static final String CAPSULE_CLASS = "Capsule.class";

    private final RepositorySystem repositorySystem;
    private final RepositorySystemSession repositorySystemSession;
    private final List<RemoteRepository> remoteRepositories;

    public TemplateCapsuleClass(RepositorySystem repositorySystem, RepositorySystemSession repositorySystemSession,
                                List<RemoteRepository> remoteRepositories) {

        this.repositorySystem = repositorySystem;
        this.repositorySystemSession = repositorySystemSession;
        this.remoteRepositories = remoteRepositories;
    }

    @Override
    public String getName() {
        return CAPSULE_CLASS;
    }

    @Override
    public InputStream getInputStream() throws IOException {
//        InputStream capsuleInputStream = getClass().getClassLoader().getResourceAsStream("capsule-0.4.0-SNAPSHOT.jar");
        InputStream capsuleInputStream = new FileInputStream(resolveCapsule());

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

    private File resolveCapsule() throws IOException {
        final ArtifactResult artifactResult;
        try {
            artifactResult = this.resolve("co.paralleluniverse", "capsule", getLastCapsuleVersion());
        } catch (ArtifactResolutionException e) {
            throw new IOException("Could not find the latest version of capsule in maven repositories");
        } catch (VersionRangeResolutionException e) {
            throw new IOException("Failed to calculate latest version of capsule for inclusion");
        }
        return artifactResult.getArtifact().getFile();
    }

    private ArtifactResult resolve(final String groupId, final String artifactId, final String version)
            throws ArtifactResolutionException {

        String coords = groupId + ":" + artifactId;
        if (version != null && !version.isEmpty()){
            coords += ":" + version;
        }
        return repositorySystem.resolveArtifact(repositorySystemSession, new ArtifactRequest(new DefaultArtifact(coords), remoteRepositories, null));
    }

    private String getLastCapsuleVersion() throws VersionRangeResolutionException {
        DefaultArtifact artifact = new DefaultArtifact("co.paralleluniverse", "capsule", null, null, "[0,)");
        VersionRangeRequest request = new VersionRangeRequest().setRepositories(remoteRepositories).setArtifact(artifact);
        final VersionRangeResult result = repositorySystem.resolveVersionRange(repositorySystemSession, request);

        return result.getHighestVersion() != null ? result.getHighestVersion().toString() : null;
    }

    @Override
    public Iterator<CapsuleEntry> iterator() {
        return Collections.<CapsuleEntry>singleton(this).iterator();
    }
}
