package spdb.check.scan;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: luotao
 * Date: 2018/4/23
 * Time: 下午1:40
 * 文件扫描类
 */
public class Scanner {
    private Log log;
    private long scanFileCount; //扫描的文件数
    private List<String> target_key = new ArrayList<String>();//需要过滤的字符串,从文本中加载
    private List<String> suffixFilter = new ArrayList<String>();//过滤指定后缀文件
    private List<String> suffixMatch = new ArrayList<String>();//扫描特定后缀文件,优先级高于suffixFilter
    private boolean isMatch = false; //是否扫描指定后缀文件
    private List<ScannerResult> results = new ArrayList<ScannerResult>();//扫描后命中的目标


    public Scanner(){}
    public Scanner(Log log) {
        super();
        this.log = log;
    }

    public void loadConfig(String config) throws MojoExecutionException {
        try {
            InputStream input = new FileInputStream(config);
            Yaml yaml = new Yaml();
            Map<String, Object> object = (Map<String, Object>) yaml.load(input);
            log.info("rule_file:" + object.get("rule_file"));
            log.info("suffix_filter_file:" + object.get("suffix_filter_file"));
            log.info("suffix_match_file:" + object.get("suffix_match_file"));
            target_key = ScannerUtils.loadFile((String) object.get("rule_file"));
            suffixFilter = ScannerUtils.loadFile((String) object.get("suffix_filter_file"));
            suffixMatch = ScannerUtils.loadFile((String) object.get("suffix_match_file"));

            if (suffixMatch.size() > 0){
                log.info("current scan policy : suffix Match.");
                for (String match: suffixMatch){
                    log.info("suffix match : " + match);
                }
                isMatch = true;
            } else {
                log.info("current scan policy : suffix Filter.");
                for (String filter: suffixFilter){
                    log.info("suffix Filter : " + filter);
                }
                isMatch = false;
            }

        }catch (Exception e){
            throw new MojoExecutionException("load error");
        }

    }
    public void start(List<String> sacnPathList) throws MojoExecutionException  {
        for(String path: sacnPathList){
            scan(path);
        }
    }

    public void showResult(String targetPath) throws MojoExecutionException{
        if (results != null && results.size() > 0){
            log.error("scanner result count : " + results.size());
            for (ScannerResult sr : results){
                log.error(sr.toString());
            }
            String filename = ScannerUtils.wirteResult(targetPath, results);
            log.info("result write file : " + filename );
            throw new MojoExecutionException("scann");
        } else {
            log.info("[ok] Scan succeeded ! ");
        }
    }


    /**
     * 扫描目录
     * @param path 路径
     * @return 是否包含敏感字符
     */
    private  void scan(String path) {

        File file = new File(path);
        if (file.isDirectory()){//该root目录存在
            String[] dirFils = file.list();
            for (String dir : dirFils){
                scan(path + "/" + dir);
            }
        } else if (file.isFile()){
            boolean isCan = isScanFile(file.getName());
            if (isCan){
                scan_file(file);
            }
        }
    }

    /**
     * 判断该文件是否需要扫描
     * @param fileName
     * @return
     */
    private boolean isScanFile(String fileName){
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (isMatch) { //扫描指定后缀文件
            for (String match : suffixMatch) {
                if (match.toUpperCase().indexOf(suffix.toUpperCase()) != -1){
                    return true;
                }
            }
            return false;
        } else {//过滤指定后缀文件
            for (String filter : suffixFilter) {
                if (filter.toUpperCase().indexOf(suffix.toUpperCase()) != -1) {
                    return false;
                }
            }
            return true;
        }

    }
    /**
     * 扫描文件
     * @param file
     */
    private void scan_file(File file) {
        log.info("["+(scanFileCount++)+"] scan file : " + file.getAbsolutePath());
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 512);

            int number = 0;
            // 读取一行，存储于字符串列表中
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                number ++;
                line = line.trim();
                // 核心扫描处理
                for (String targer: target_key){
                     if (line.toUpperCase().indexOf(targer.toUpperCase()) != -1){
                         ScannerResult sr = new ScannerResult();
                         sr.setFilePath(file.getAbsolutePath());
                         sr.setLine(line);
                         sr.setNumber(number);
                         sr.setTarget(targer);
                         results.add(sr);
                         break;
                     }

                }

            }

        }catch (FileNotFoundException fnfe){
            fnfe.printStackTrace();
        }catch (IOException ioe){
            ioe.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
