package spdb.check.scan;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
    private List<String> targer_key = new ArrayList<String>();//需要过滤的字符串,从文本中加载
    private List<ScannerResult> results = new ArrayList<ScannerResult>();//扫描后命中的目标

    public Scanner(){}
    public Scanner(Log log) {
        super();
        this.log = log;
    }

    public void loadRule(String rulePath) throws MojoExecutionException{
        File file = new File(rulePath);
        if (file.isFile()){
            targer_key = ScannerUtils.loadRule(file);
        } else {
            log.error("rule file load error");
            throw new MojoExecutionException("load error");
        }

    }
    public void start(List<String> sacnPathList) throws MojoExecutionException  {
        for(String path: sacnPathList){
            scan(path);
        }
        ScannerUtils.showResult(results, log);
    }


    /**
     * 扫描目录
     * @param path 路径
     * @return 是否包含敏感字符
     */
    private  void scan(String path) {
        log.info("[*] scan path : " + path);
        File file = new File(path);
        if (file.isDirectory()){//该root目录存在
            String[] dirFils = file.list();
            for (String dir : dirFils){
                scan(path + "/" + dir);
            }
        } else if (file.isFile()){
            scan_file(file);
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
                for (String targer: targer_key){
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
