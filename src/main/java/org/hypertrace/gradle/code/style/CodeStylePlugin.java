package org.hypertrace.gradle.code.style;

import com.diffplug.gradle.spotless.SpotlessExtension;
import com.diffplug.gradle.spotless.SpotlessPlugin;

import java.io.IOException;
import java.util.HashMap;
import javax.annotation.Nonnull;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.PluginContainer;

public class CodeStylePlugin implements Plugin<Project> {

  @Override
  public void apply(@Nonnull Project target) {
    configureCodeStyle(target);
  }

  private void configureCodeStyle(Project project) {
    PluginContainer pluginContainer = project.getPlugins();
    pluginContainer.apply(SpotlessPlugin.class);

    SpotlessExtension spotlessExtension =
        project.getExtensions().getByType(SpotlessExtension.class);
    configureFormatting(spotlessExtension);
  }

  private void configureFormatting(SpotlessExtension spotlessExtension) {
    spotlessExtension.java(
        format -> {
          format.importOrder();
          format.removeUnusedImports();
          format.googleJavaFormat();
          format.target("src/**/*.java");
        });

    spotlessExtension.kotlinGradle(
        format ->
        {
            try {
                format
                    .ktlint("0.50.0")
                    .editorConfigOverride(
                        new HashMap<String, Object>() {
                          {
                            put("indent_size", "2");
                          }
                        });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    spotlessExtension.format(
        "misc",
        format -> {
          format.target("*.md", "src/**/*.proto", ".gitignore");
          format.indentWithSpaces(2);
          format.trimTrailingWhitespace();
          format.endWithNewline();
        });
  }
}
