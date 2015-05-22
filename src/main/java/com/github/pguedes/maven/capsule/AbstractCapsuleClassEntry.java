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

public abstract class AbstractCapsuleClassEntry implements Iterable<CapsuleEntry>, CapsuleEntry {
    public static final String CAPSULE_CLASS = "Capsule.class";

    private final RepositorySystem repositorySystem;
    private final RepositorySystemSession repositorySystemSession;
    private final List<RemoteRepository> remoteRepositories;

    public AbstractCapsuleClassEntry(RepositorySystem repositorySystem, RepositorySystemSession repositorySystemSession,
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
        InputStream capsuleInputStream = new FileInputStream(resolveLatestCapsuleArtifact());

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

    /**
     * Get the {@link File} representing the jar of the capsule's latest artifact found in repositories.
     * @return the file pointing to the latest version of the jar
     * @throws IOException if we cannot find the latest version of the artifact
     */
    private File resolveLatestCapsuleArtifact() throws IOException {
        final ArtifactResult artifactResult;
        try {
            final String version = getCapsuleVersion();
            String artifactRef = "co.paralleluniverse:capsule";
            if (version != null && !version.isEmpty()){
                artifactRef += ":" + version;
            }
            ArtifactRequest request = new ArtifactRequest(new DefaultArtifact(artifactRef), remoteRepositories, null);
            artifactResult = repositorySystem.resolveArtifact(repositorySystemSession, request);
        } catch (ArtifactResolutionException e) {
            throw new IOException("Could not find the latest version of capsule in maven repositories");
        } catch (VersionRangeResolutionException e) {
            throw new IOException("Failed to calculate latest version of capsule for inclusion");
        }
        return artifactResult.getArtifact().getFile();
    }

    protected abstract String getCapsuleVersion() throws VersionRangeResolutionException;

    @Override
    public Iterator<CapsuleEntry> iterator() {
        return Collections.<CapsuleEntry>singleton(this).iterator();
    }

    protected RepositorySystem getRepositorySystem() {

        return repositorySystem;
    }

    protected List<RemoteRepository> getRemoteRepositories() {

        return remoteRepositories;
    }

    protected RepositorySystemSession getRepositorySystemSession() {

        return repositorySystemSession;
    }

}
