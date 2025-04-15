package ru.volnenko.maven.plugin.json.goal;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;
import ru.volnenko.maven.plugin.json.parser.RootParser;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class JsonGenerate extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Getter
    @Setter
    @Parameter(property = "files")
    private List<String> files = new ArrayList<>();

    @Parameter(defaultValue = "${settings}", required = true, readonly = true)
    private Settings settings;

    @Override
    @SneakyThrows
    public void execute() {
        @NonNull final File buildPath = new File(project.getBuild().getDirectory());
        buildPath.mkdirs();

        for (final Object dependencyObject: project.getDependencyArtifacts()) {
            if (dependencyObject == null) continue;
            final DefaultArtifact dependency = (DefaultArtifact) dependencyObject;
            if (!"compile".equalsIgnoreCase(dependency.getScope())) continue;
            if ("json".equalsIgnoreCase(dependency.getType()) || "yaml".equalsIgnoreCase(dependency.getType())) {
                System.out.println(dependency);
                final String name = dependency.getGroupId().replace(".", "/") + "/"
                        + dependency.getArtifactId() + "/" + dependency.getVersion() + "/"
                        + dependency.getArtifactId() + "-" + dependency.getVersion() + "." + dependency.getType();
                final File file = new File(settings.getLocalRepository(), name);
                final String filename = file.getAbsolutePath();
                if (!file.exists()) {
                    System.err.println("Error! File `"+ filename + "` is not exists...");
                    continue;
                }
                if (!files.contains(file.getAbsolutePath())) {
                    files.add(filename);
                    System.out.println("ADD:" + file);
                }
            }
        }

        @NonNull final String sourceNameJSON = project.getBuild().getFinalName() + "." + project.getPackaging();
        @NonNull final File build = new File(project.getBuild().getDirectory(), sourceNameJSON);

        if (!files.isEmpty()) {
            @NonNull final RootParser rootParser = new RootParser();
            @NonNull final String json = rootParser.files(files).json();
            Files.write(build.toPath(), json.getBytes(StandardCharsets.UTF_8));
        } else {
            @NonNull final ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(build, Collections.emptyMap());
        }
    }

}
