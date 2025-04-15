package ru.volnenko.maven.plugin.json;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "json", defaultPhase = LifecyclePhase.COMPILE)
public class JsonPlugin extends AbstractMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println("JSON");
    }

}
