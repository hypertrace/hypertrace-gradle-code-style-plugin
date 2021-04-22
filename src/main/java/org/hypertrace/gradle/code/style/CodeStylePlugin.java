package org.hypertrace.gradle.code.style;

import com.diffplug.gradle.spotless.KotlinGradleExtension;
import com.diffplug.gradle.spotless.SpotlessExtension;
import com.diffplug.gradle.spotless.SpotlessPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.PluginContainer;

import javax.annotation.Nonnull;

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
        KotlinGradleExtension::ktfmt);

    spotlessExtension.format(
        "misc,",
        format -> {
          format.target("*.md", "**/*.proto", ".gitignore");
          format.targetExclude("build/**/*.proto");
          format.indentWithSpaces();
          format.trimTrailingWhitespace();
          format.endWithNewline();
        });
  }
}
