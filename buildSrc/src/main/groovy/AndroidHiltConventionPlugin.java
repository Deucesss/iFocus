import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.VersionCatalog;
import org.gradle.api.artifacts.VersionCatalogsExtension;

class AndroidHiltConventionPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPluginManager().apply("dagger.hilt.android.plugin");
        project.getPluginManager().apply("org.jetbrains.kotlin.kapt");

        VersionCatalog libs = project.getExtensions().getByType(VersionCatalogsExtension.class).named("libs");
        project.getDependencies().add("implementation", libs.findLibrary("hilt.android").get());
        project.getDependencies().add("kapt", libs.findLibrary("hilt.compiler").get());
        project.getDependencies().add("kaptAndroidTest", libs.findLibrary("hilt.compiler").get());
    }
}