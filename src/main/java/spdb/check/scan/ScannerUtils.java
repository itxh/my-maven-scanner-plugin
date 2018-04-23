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
 * Time: 下午4:42
 * To change this template use File | Settings | File Templates.
 */
public class ScannerUtils {

    public static List<String> loadRule(File ruleFile){
        List<String> targer_key = new ArrayList<String>();
        InputStream is = null;
        try {
            is = new FileInputStream(ruleFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 512);
            // 读取一行，存储于字符串列表中
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                line = line.trim();
                targer_key.add(line);
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
            return targer_key;
        }

    }

    /**
     * 打印结果
     * @throws MojoExecutionException
     */
    public static void showResult(List<ScannerResult> results,Log log) throws MojoExecutionException {
        if (results != null && results.size() > 0){
            log.error("scanner result count : " + results.size());
            for (ScannerResult sr : results){
                log.error(sr.toString());
            }
            throw new MojoExecutionException("scann");
        } else {
            log.info("[ok] Scan succeeded ! ");
        }
    }
}
