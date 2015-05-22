package com.github.pguedes.maven.capsule;

import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.resolution.VersionRangeResult;

import java.util.List;

public class LatestCapsuleClass extends AbstractCapsuleClassEntry {

    public LatestCapsuleClass(RepositorySystem repositorySystem, RepositorySystemSession repositorySystemSession,
                              List<RemoteRepository> remoteRepositories) {

        super(repositorySystem, repositorySystemSession, remoteRepositories);
    }

    protected String getCapsuleVersion() throws VersionRangeResolutionException {

        return getLatestCapsuleVersion();
    }

    /**
     * Query the repositories to find the latest version of the capsule.
     *
     * @return latest version string found in repositories for capsule's artifact
     * @throws org.eclipse.aether.resolution.VersionRangeResolutionException
     *          if we cannot resolve the latest version of capsule
     */
    private String getLatestCapsuleVersion() throws VersionRangeResolutionException {

        DefaultArtifact artifact = new DefaultArtifact("co.paralleluniverse", "capsule", null, null, "[0,)");
        VersionRangeRequest request = new VersionRangeRequest().setRepositories(getRemoteRepositories()).setArtifact(artifact);
        final VersionRangeResult result = getRepositorySystem().resolveVersionRange(getRepositorySystemSession(), request);

        return result.getHighestVersion() != null ? result.getHighestVersion().toString() : null;
    }
}
