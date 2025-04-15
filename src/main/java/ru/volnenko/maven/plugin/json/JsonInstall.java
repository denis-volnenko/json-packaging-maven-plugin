package ru.volnenko.maven.plugin.json;

import lombok.SneakyThrows;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Mojo(name = "install", defaultPhase = LifecyclePhase.INSTALL)
public class JsonInstall extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(defaultValue = "${localRepository}", readonly = true, required = true)
    private ArtifactRepository localRepository;

    @Parameter(defaultValue = "${settings}", readonly = true)
    private Settings settings;

    @Override
    @SneakyThrows
    public void execute() {
        System.out.println("[ INSTALL ]");
        final File m2 = new File(settings.getLocalRepository());
        final File fileSource = project.getFile();
        final String groupPath = project.getGroupId().replace(".", "/");
        final File localRepo = new File(m2, groupPath);
        final File localRepoVersion = new File(localRepo, project.getVersion());

        {
            final String targetPOM = project.getArtifactId() + "-" + project.getVersion() + ".pom";
            final File targetPom = new File(localRepoVersion, targetPOM);
            localRepoVersion.mkdirs();
            System.out.println("Installing (source): " + fileSource.getAbsolutePath());
            System.out.println("Installing (target): " + targetPom.getAbsolutePath());
            Files.copy(fileSource.toPath(), targetPom.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        final String targetJSON = project.getArtifactId() + "-" + project.getVersion() + "." + project.getPackaging();
        final File targetM2JSON = new File(localRepoVersion, targetJSON);

        final String sourceNameJSON = project.getBuild().getFinalName() + "." + project.getPackaging();
        final File build = new File(project.getBuild().getDirectory(), sourceNameJSON);

        System.out.println("Installing (source): " + build.getAbsolutePath());
        System.out.println("Installing (target): " + targetM2JSON.getAbsolutePath());

        Files.copy(build.toPath(), targetM2JSON.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }


}
