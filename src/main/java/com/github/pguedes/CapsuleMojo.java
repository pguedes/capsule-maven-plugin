package com.github.pguedes;

import com.github.pguedes.maven.capsule.*;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.*;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.RemoteRepository;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Maven mojo for encapsulation of java apps in capsule - https://github.com/puniverse/capsule.
 */
@Mojo(name = "capsule", defaultPhase = LifecyclePhase.PACKAGE, requiresDependencyCollection = ResolutionScope.RUNTIME)
public class CapsuleMojo extends AbstractMojo {
    @Parameter(property = "capsule.mainClass", required = true)
    private String mainClass;
    @Parameter(property = "capsule.name", defaultValue = "${project.build.finalName}-capsule.jar")
    private String capsuleName;
    @Parameter(property = "capsule.outputDirectory", defaultValue = "${project.build.directory}")
    private File outputDirectory;
    @Parameter(defaultValue = "${project.artifacts}")
    private Collection<Artifact> artifacts;
    @Parameter(property = "capsule.mainJar", defaultValue = "${project.build.finalName}.jar")
    private String mainJar;

    @Component
    private RepositorySystem repoSystem;
    @Parameter(defaultValue = "${repositorySystemSession}", readonly = true)
    private RepositorySystemSession repoSession;
    @Parameter(defaultValue = "${project.remoteProjectRepositories}", readonly = true)
    private List<RemoteRepository> remoteRepos;
    @Parameter(property = "capsule.version")
    private String capsuleVersion;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        CapsuleWriter capsuleWriter = null;
        try {
            // create a new capsule (jar)
            File capsuleOutputPath = new File(outputDirectory, capsuleName);
            capsuleWriter = new CapsuleWriter(capsuleOutputPath);
            // edit manifest to use capsule classes to load app
            capsuleWriter.write(new CapsuleManifest(mainClass, capsuleName));
            // add capsule classes to it
            capsuleWriter.write(createCapsuleClassEntry());
            // add the main jar for the application
            File mainJarFile = new File(outputDirectory, mainJar);
            capsuleWriter.write(new SingleFileEntry(mainJarFile));
            // add dependencies to the jar                                                                                                       <
            capsuleWriter.write(new MavenProjectDependencies(artifacts));
        } catch (IOException e) {
            throw new MojoExecutionException("failed to create capsule", e);
        } finally {
            if (capsuleWriter != null) {
                capsuleWriter.close();
            }
        }
    }

    private AbstractCapsuleClassEntry createCapsuleClassEntry() {
        if (capsuleVersion == null) {
            return new LatestCapsuleClass(repoSystem, repoSession, remoteRepos);
        }
        return new SpecificCapsuleClass(repoSystem, repoSession, remoteRepos, capsuleVersion);
    }
}
