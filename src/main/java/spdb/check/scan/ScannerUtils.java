package spdb.check.scan;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: luotao
 * Date: 2018/4/23
 * Time: 下午4:42
 * To change this template use File | Settings | File Templates.
 */
public class ScannerUtils {

    public static List<String> loadFile(String filePath){
        List<String> targer_key = new ArrayList<String>();
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
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
     * 将结果写入磁盘文件.txt
     * @param targetPath
     * @param results
     * @return 结果文件路径
     */
    public static String wirteResult(String targetPath, List<ScannerResult> results)  {
        File targetDir = new File(targetPath);
        if  (!targetDir .exists()  && !targetDir .isDirectory()){
            targetDir .mkdir();
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String filename = "scan_result_" + df.format(new Date()) + ".txt"; //输出文件名
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(targetDir + "/" + filename));
            for (ScannerResult sr : results) {
                out.write(sr.toString());
                out.newLine();
                out.newLine();  //注意\n不一定在各种计算机上都能产生换行的效果
            }
            out.close();
        }catch (IOException e){

        }finally {
            return targetPath + "/" + filename;
        }

    }
}
