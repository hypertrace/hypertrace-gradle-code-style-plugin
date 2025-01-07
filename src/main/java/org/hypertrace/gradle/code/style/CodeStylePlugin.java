package org.hypertrace.gradle.code.style;

import build.buf.gradle.BufExtension;
import build.buf.gradle.BufPlugin;
import build.buf.gradle.BufSupportKt;
import com.diffplug.gradle.spotless.SpotlessExtension;
import com.diffplug.gradle.spotless.SpotlessPlugin;
import java.io.File;
import java.io.IOException;
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
    pluginContainer.apply(SpotlessPlugin.class);
    pluginContainer.apply(BufPlugin.class);
    configureFormatting(project);
  }

  private void configureFormatting(Project project) {
    SpotlessExtension spotlessExtension =
        project.getExtensions().getByType(SpotlessExtension.class);

    spotlessExtension.java(
        format -> {
          format.importOrder();
          format.removeUnusedImports();
          format.googleJavaFormat();
          format.target("src/**/*.java");
        });

    spotlessExtension.kotlinGradle(
        format -> {
          try {
            format.ktlint("0.50.0").editorConfigOverride(Map.of("indent_size", "2"));
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });

    BufExtension bufExtension = project.getExtensions().getByType(BufExtension.class);
    spotlessExtension.protobuf(
        format -> {
          File bufBinary =
              project
                  .getConfigurations()
                  .getByName(BufSupportKt.BUF_BINARY_CONFIGURATION_NAME)
                  .getSingleFile();
          if (!bufBinary.canExecute()) {
            bufBinary.setExecutable(true);
          }
          format.buf(bufExtension.getToolVersion()).pathToExe(bufBinary.getAbsolutePath());
        });
    bufExtension.setEnforceFormat(false);

    spotlessExtension.format(
        "misc",
        format -> {
          format.target("*.md", ".gitignore", "*.yaml");
          format.leadingTabsToSpaces(2);
          format.trimTrailingWhitespace();
          format.endWithNewline();
        });
  }
}
