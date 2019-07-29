package common.excel.bean;

import java.util.Map;

/**
 * 
 * <PRE>
 * 。
 * 
 * 2019/01/21 phamxuantung 新規作成
 * </PRE>
 * @author phamxuantung
 */
public class ReportInfo {

    private String templateName;

    private String inportFileName;

    private String exportFileName;

    private Map<String, Map<String, Object>> reportData;

    public String getInportFileName() {
        return inportFileName;
    }

    public void setInportFileName(String inportFileName) {
        this.inportFileName = inportFileName;
    }

    public String getExportFileName() {
        return exportFileName;
    }

    public void setExportFileName(String exportFileName) {
        this.exportFileName = exportFileName;
    }

    public Map<String, Map<String, Object>> getReportData() {
        return reportData;
    }

    public void setReportData(Map<String, Map<String, Object>> reportData) {
        this.reportData = reportData;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

}
