package ru.volnenko.maven.plugin.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

@Mojo(name = "package", defaultPhase = LifecyclePhase.PACKAGE)
public class JsonPackage extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Override
    @SneakyThrows
    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println("[ PACKAGE ]");
        final File buildPath = new File(project.getBuild().getDirectory());
        buildPath.mkdirs();

        final String sourceNameJSON = project.getBuild().getFinalName() + "." + project.getPackaging();
        final File build = new File(project.getBuild().getDirectory(), sourceNameJSON);

        final ObjectMapper objectMapper = new ObjectMapper();
        final Map<String, Object> map = new LinkedHashMap<>();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(build, map);

        final Artifact artifact =  project.getArtifact();
        artifact.setFile(build);
    }

}
