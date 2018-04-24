package spdb.check;

import org.apache.maven.model.Build;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import spdb.check.scan.Scanner;

import java.util.ArrayList;
import java.util.List;

/**
 * User: luotao(T-luot1@spdbdev.com)
 * Date: 2018/4/23
 * Time: 下午1:25
 * 扫描源文件插件
 * @goal scanner
 * @phase  process-sources
 */
public class ScannerMojo extends AbstractMojo {

    /**
     * @parameter expression="${project}"
     * @readonly
     */
    private MavenProject project;



    /**
     * 规则文件
     * @parameter expression="${scanner.config}"
     * default-value=" "
     */
    private String config = "";



    public void execute() throws MojoExecutionException {
        Build build = project.getBuild();
        List<String> scanPathList = new ArrayList<String>();
        getLog().info("---------------------------------------------------");
        getLog().info("*         spdb-maven-scanner-plugin begin          *");
        getLog().info("---------------------------------------------------");
        String targetPath = build.getDirectory();

        getLog().info("src source root: " + build.getSourceDirectory());
        getLog().info( "junit source root: " + build.getTestSourceDirectory());
        scanPathList.add(build.getSourceDirectory());
        scanPathList.add(build.getTestSourceDirectory());
        List<Resource> resources = build.getResources();
        for (Resource resource: resources) {
            getLog().info("resource root: " + resource.getDirectory());
            scanPathList.add(resource.getDirectory());
        }
        getLog().info("config : " + config);
        Scanner scanner = new Scanner(getLog());
        scanner.loadConfig(config);
        scanner.start(scanPathList);
        scanner.showResult(targetPath);

    }
}
