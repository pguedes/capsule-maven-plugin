package com.github.pguedes.maven.capsule;

import com.github.pguedes.maven.capsule.AbstractCapsuleClassEntry;
import com.github.pguedes.maven.capsule.LatestCapsuleClass;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.VersionRangeResolutionException;

import java.util.List;

public class SpecificCapsuleClass extends AbstractCapsuleClassEntry {

    private final String capsuleVersion;

    public SpecificCapsuleClass(RepositorySystem repoSystem, RepositorySystemSession repoSession,
                                List<RemoteRepository> remoteRepos, String capsuleVersion) {

        super(repoSystem, repoSession, remoteRepos);

        this.capsuleVersion = capsuleVersion;
    }

    @Override
    protected String getCapsuleVersion() throws VersionRangeResolutionException {

        return capsuleVersion;
    }
}
