package common.excel;

import common.excel.bean.ReportInfo;

/**
 * 
 * <PRE>
 * 。
 * 
 * 2019/01/22 phamxuantung 新規作成
 * </PRE>
 * @author phamxuantung
 */
public class ProductivityStatisticsExportFactory {
	
	private static ProductivityStatisticsExportFactory reportFactoryBean= null;
	
	private ProductivityStatisticsExportFactory(){}
	
	/**
	 * 
	 * <PRE>
	 * 。
	 * </PRE>
	 * @return
	 */
	public static ProductivityStatisticsExportFactory getInstance(){
		if(reportFactoryBean!=null){
			return reportFactoryBean;
		}
		else{
			reportFactoryBean = new ProductivityStatisticsExportFactory();
			return reportFactoryBean;
		}			
	}
	
	/**
	 * 
	 * <PRE>
	 * 。
	 * </PRE>
	 * @param reportInfo
	 * @return
	 */
	public AbstractReportExport createReportExport(ReportInfo reportInfo){
		AbstractReportExport reportBean = null;
		if(reportInfo.getTemplateName().equalsIgnoreCase("export")){
			reportBean = new ProductivityStatisticsExport();
			reportBean.setReportInfo(reportInfo);			
		}
		return reportBean;
	}
	
}