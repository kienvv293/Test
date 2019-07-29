package common.excel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import common.excel.bean.ReportInfo;

/**
 * 
 * <PRE>
 * 。
 * 
 * 2019/01/23 phamxuantung 新規作成
 * </PRE>
 * @author phamxuantung
 */
public abstract class AbstractReportExport implements Serializable {

	private static final long serialVersionUID = -3644460645549851030L;
	
	private ReportInfo reportInfo;

	public ReportInfo getReportInfo() {
		return reportInfo;
	}

	public void setReportInfo(ReportInfo reportInfo) {
		this.reportInfo = reportInfo;
	}
	
	/**
	 * 
	 * <PRE>
	 * 。
	 * </PRE>
	 */
	public boolean reportExport() {
		boolean exportFlg = true;
		
		FileOutputStream out = null;
		Workbook wb = null;
		if (getReportInfo().getInportFileName().indexOf(".xlsx") > 0) {
			wb = new XSSFWorkbook();
		} else {
			wb = new HSSFWorkbook();
		}
		wb = readExportTemplate(getReportInfo().getInportFileName());
		fillInReportData(wb);
//		rebuildReportFrame(wb);
		try {
			out = new FileOutputStream(getReportInfo().getExportFileName());
			wb.write(out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			exportFlg = false;
		} catch (IOException e) {
			e.printStackTrace();
			exportFlg = false;
		} finally {
			try {
				if (out != null) {
					out.close();
					wb = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return exportFlg;
	}

	public abstract Workbook readExportTemplate(String inportFileName);

	public abstract void fillInReportData(Workbook wb);

	public abstract void rebuildReportFrame(Workbook wb);
}