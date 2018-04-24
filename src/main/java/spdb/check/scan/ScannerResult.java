package spdb.check.scan;

/**
 * 定义扫描结果数据结构
 * User: luotao
 * Date: 2018/4/23
 * Time: 下午3:38
 * To change this template use File | Settings | File Templates.
 */
public class ScannerResult {

    private int number;
    private String filePath;
    private String line;
    private String target;

    public String toString(){
        return " file : " + filePath +
                "\n number : " + number +
                "\n target : " + target +
                "\n line : " + line;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }
}
