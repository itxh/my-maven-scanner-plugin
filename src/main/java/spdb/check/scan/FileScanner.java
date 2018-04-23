package spdb.check.scan;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: luotao
 * Date: 2018/4/23
 * Time: 下午1:40
 * 文件扫描类
 */
public class FileScanner {

    private static String [] targer_key ={"PASSWORD"};
    /**
     * 扫描目录
     * @param path 路径
     * @param log
     * @return 是否包含敏感字符
     */
    public static void scan(String path, Log log) throws MojoExecutionException {
        log.info("[*] scan path : " + path);
        File file = new File(path);
        if (file.isDirectory()){//该root目录存在
            String[] dirFils = file.list();
            for (String dir : dirFils){
                scan(path + "/" + dir, log);
            }
        } else if (file.isFile()){
            scan_file(file, log);
        }
    }

    /**
     * 扫描文件
     * @param file
     * @param log
     */
    private static void scan_file(File file, Log log) throws MojoExecutionException {
        log.info("[+] scan file : " + file.getAbsolutePath());
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 512);

            int i = 0;
            // 读取一行，存储于字符串列表中
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                i ++;
                line = line.trim();
                boolean status = isTarger(line);
                if (status) {
                    log.error(" file : " + file.getAbsolutePath() + " [" + i + "]\n" + line);
                    throw new MojoExecutionException("scan error");
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

    /**
     * 扫描该行 是否包含需扫描字符
     * @param line
     * @return
     */
    private static boolean isTarger(String line){
        for (String targer : targer_key){
            boolean status = line.contains(targer);
            if (status) {
                return true;
            }
        }
        return false;
    }
}
