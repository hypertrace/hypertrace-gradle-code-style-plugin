package org.hypertrace.gradle.code.style;

import com.diffplug.gradle.spotless.SpotlessExtension;
import com.diffplug.gradle.spotless.SpotlessPlugin;
import java.util.Map;
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
    pluginContainer.apply(SpotlessPlugin.class).apply(project);

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

    spotlessExtension.kotlin(
        format -> {
          format.ktlint().userData(Map.of("indent_size", "2", "continuation_indent_size", "2"));
        });

    spotlessExtension.format(
        "misc,",
        format -> {
          format.target("*.md", "**/*.proto", ".gitignore");
          format.indentWithSpaces();
          format.trimTrailingWhitespace();
          format.endWithNewline();
        });
  }
}
