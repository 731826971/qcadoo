package com.qcadoo.maven.plugins.schema;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.maven.model.Profile;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.util.DefaultFileSet;
import org.codehaus.plexus.archiver.zip.ZipArchiver;
import org.codehaus.plexus.util.FileUtils;

/**
 * @goal schema
 * @execute phase="package"
 */
public class SchemaMojo extends AbstractMojo {

    /**
     * @parameter expression="${basedir}/../"
     * @readonly
     */
    private File baseDirectory;

    /**
     * @parameter expression="${basedir}/target/schema/"
     * @readonly
     */
    private File workingDirectory;

    /**
     * @parameter expression="${basedir}/target/schema/modules"
     * @readonly
     */
    private File modulesWorkingDirectory;

    /**
     * @parameter expression="${basedir}/target/schema/common"
     * @readonly
     */
    private File commonWorkingDirectory;

    /**
     * @parameter expression="${basedir}/target/${project.artifactId}.zip"
     * @readonly
     */
    private File target;

    /**
     * @parameter expression="${project}"
     * @readonly
     */
    private MavenProject project;

    /**
     * @component role="org.codehaus.plexus.archiver.Archiver" roleHint="zip"
     */
    private ZipArchiver zipArchiver;

    @Override
    @SuppressWarnings({ "unchecked" })
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            prepareWorkingDirectory();

            boolean skipVersion = isSkipVersionProfileActive();

            String version = prepareVersion();

            for (File file : (Collection<File>) FileUtils
                    .getFiles(baseDirectory, "*/src/main/resources/com/qcadoo/*/*.xsd", null)) {
                FileUtils.copyFile(file, prepareDestinationFile(file, version, skipVersion, workingDirectory));
            }

            for (File file : (Collection<File>) FileUtils.getFiles(baseDirectory,
                    "*/src/main/resources/com/qcadoo/*/modules/*.xsd", null)) {
                FileUtils.copyFile(file, prepareDestinationFile(file, version, skipVersion, modulesWorkingDirectory));
            }

            for (File file : (Collection<File>) FileUtils.getFiles(baseDirectory,
                    "*/src/main/resources/com/qcadoo/*/common/*.xsd", null)) {
                FileUtils.copyFile(file, prepareDestinationFile(file, version, skipVersion, commonWorkingDirectory));
            }

            createArchive();
            registerArtifact();

        } catch (ArchiverException e) {
            throw new MojoExecutionException("Exception while creating zip", e);
        } catch (IOException e) {
            throw new MojoExecutionException("Exception while creating zip", e);
        }
    }

    private File prepareDestinationFile(final File file, final String version, final boolean skipVersion,
            final File targetDirectory) {
        if (skipVersion) {
            return new File(targetDirectory, file.getName());
        } else {
            return new File(targetDirectory, file.getName().replaceAll(".xsd", "") + "-" + version + ".xsd");
        }
    }

    private String prepareVersion() {
        String version = project.getVersion();
        String[] splittedVersion = version.split("\\.");
        return splittedVersion[0] + "." + splittedVersion[1];
    }

    private void registerArtifact() {
        project.getArtifact().setFile(target);
    }

    @SuppressWarnings("unchecked")
    private boolean isSkipVersionProfileActive() {
        for (Profile profile : ((List<Profile>) project.getActiveProfiles())) {
            if ("skipVersion".equals(profile.getId())) {
                return true;
            }
        }
        return false;
    }

    private void createArchive() throws ArchiverException, IOException {
        zipArchiver.setDestFile(target);
        zipArchiver.setDirectoryMode(493);
        zipArchiver.setFileMode(420);

        DefaultFileSet fileSet = new DefaultFileSet();
        fileSet.setDirectory(workingDirectory);

        zipArchiver.addFileSet(fileSet);
        zipArchiver.createArchive();
    }

    private void prepareWorkingDirectory() throws IOException {
        workingDirectory.mkdirs();

        FileUtils.cleanDirectory(workingDirectory);

        modulesWorkingDirectory.mkdirs();
        commonWorkingDirectory.mkdirs();
    }

}
