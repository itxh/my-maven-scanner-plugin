package spdb.check;

import org.apache.maven.model.Build;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import spdb.check.scan.FileScanner;

import java.util.List;

/**
 * User: luotao(T-luot1@spdbdev.com)
 * Date: 2018/4/23
 * Time: 下午1:25
 * 扫描源文件插件
 * @goal scanner
 * @phase  pre-integration-test
 */
public class ScannerMojo extends AbstractMojo {

    /**
     * @parameter expression="${project}"
     * @readonly
     */
    private MavenProject project;

    /**
     * @parameter expression="${buildinfo.prefix}"
     * default-value="+++"
     */
    private String prefix;

    public void execute() throws MojoExecutionException {
        Build build = project.getBuild();

        getLog().info("==========================Project build info:");
        getLog().info("\t" + prefix + " src source root: " + build.getSourceDirectory());
        getLog().info("\t" + prefix + " junit source root: " + build.getTestSourceDirectory());
        List<Resource> resources = build.getResources();
        for (Resource resource: resources) {
            getLog().info("\t" + prefix + " resource root: " + resource.getDirectory());
        }

        FileScanner.scan(build.getSourceDirectory(),getLog());
        FileScanner.scan(build.getTestSourceDirectory(), getLog());
        for (Resource resource: resources) {
            FileScanner.scan(resource.getDirectory(), getLog());
        }

        getLog().info("=======================");

    }
}
