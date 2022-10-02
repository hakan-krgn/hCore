package com.hakan.core.dependency;

import com.hakan.core.dependency.annotations.Dependency;
import com.hakan.core.utils.Validate;

import javax.annotation.Nonnull;
import java.io.File;

/**
 * DependencyAttribute class to store
 * dependency attributes from Dependency
 * annotation.
 */
public final class DependencyAttribute {

    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String website;

    /**
     * Creates a new dependency attribute.
     *
     * @param dependency The dependency.
     */
    public DependencyAttribute(@Nonnull Dependency dependency) {
        Validate.notNull(dependency, "dependency cannot be null!");
        this.artifactId = Validate.notNull(dependency.artifactId(), "artifactId cannot be null!");
        this.groupId = Validate.notNull(dependency.groupId(), "groupId cannot be null!");
        this.version = Validate.notNull(dependency.version(), "version cannot be null!");
        this.website = Validate.notNull(dependency.website(), "website cannot be null!");
    }

    /**
     * Creates a new dependency attribute.
     *
     * @param groupId    The groupId.
     * @param artifactId The artifactId.
     * @param version    The version.
     * @param website    The website.
     */
    public DependencyAttribute(@Nonnull String groupId,
                               @Nonnull String artifactId,
                               @Nonnull String version,
                               @Nonnull String website) {
        this.artifactId = Validate.notNull(artifactId, "artifactId cannot be null!");
        this.groupId = Validate.notNull(groupId, "groupId cannot be null!");
        this.version = Validate.notNull(version, "version cannot be null!");
        this.website = Validate.notNull(website, "website cannot be null!");
    }

    /**
     * Gets the groupId.
     *
     * @return The groupId.
     */
    @Nonnull
    public String getGroupId() {
        return this.groupId;
    }

    /**
     * Gets the artifactId.
     *
     * @return The artifactId.
     */
    @Nonnull
    public String getArtifactId() {
        return this.artifactId;
    }

    /**
     * Gets the version.
     *
     * @return The version.
     */
    @Nonnull
    public String getVersion() {
        return this.version;
    }

    /**
     * Gets the website.
     *
     * @return The website.
     */
    @Nonnull
    public String getWebsite() {
        return this.website;
    }

    /**
     * Gets the dependency name.
     *
     * @return The dependency name.
     */
    @Nonnull
    public String asPath(@Nonnull File defaultFolder) {
        Validate.notNull(defaultFolder, "default file cannot be null!");
        return this.asPath(defaultFolder.getPath());
    }

    /**
     * Calculates save path from default path.
     * with dependency artifactId and version.
     *
     * @param defaultPath The default path.
     * @return The save path.
     */
    @Nonnull
    public String asPath(@Nonnull String defaultPath) {
        Validate.notNull(defaultPath, "default path cannot be null!");

        String path = (defaultPath.endsWith(File.separator)) ? defaultPath : defaultPath + File.separator;
        if (this.website.endsWith(".jar"))
            return path + this.website.substring(this.website.lastIndexOf("/") + 1);
        return path + this.artifactId + "-" + this.version + ".jar";
    }

    /**
     * Calculates URL from given groupId,
     * artifactId, version and website.
     *
     * @return The URL of jar.
     */
    @Nonnull
    public String asUrl() {
        if (this.website.endsWith(".jar"))
            return this.website;

        String url = (this.website.endsWith("/")) ? this.website : this.website + "/";
        String groupId = this.groupId.replace(".", "/");
        String artifactId = this.artifactId;
        String version = this.version;

        return url + groupId + "/" + artifactId + "/" + version + "/" + artifactId + "-" + version + ".jar";
    }
}