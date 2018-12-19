package com.kwchina.core.util.jmesa;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jmesa.facade.TableFacade;
import org.jmesa.facade.TableFacadeFactory;
import org.jmesa.util.ItemUtils;
import org.jmesa.view.editor.CellEditor;
import org.jmesa.view.html.HtmlBuilder;
import org.jmesa.view.html.HtmlComponentFactory;
import org.jmesa.view.html.component.HtmlColumn;

/**
 * Jmesa的Utils函数集合.
 * 
 * @author Kanine
 */
public class JmesaUtils {
	protected static String idName;
	public JmesaUtils() {
	}

	/**
	 * 根据已完成排序、过滤的Page分页类,创建TableFacade对象. 默认的Jmesa divID为jmesa.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static TableFacade buildTableFacade(final PageForMesa page,
			HttpServletRequest request, HttpServletResponse response) {
		return buildTableFacade("jmesa", page, request, response);
	}

	/**
	 * 根据已完成排序、过滤的Page分页类及Jmesa divID,创建TableFacade对象.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static TableFacade buildTableFacade(final String divID,
			final PageForMesa page, HttpServletRequest request,
			HttpServletResponse response) {
		TableFacade tableFacade = TableFacadeFactory.createTableFacade(divID,
				request);
		tableFacade.setMaxRowsIncrements(5, 10, 15);
		tableFacade.setMaxRows(page.getPageSize());
		tableFacade.setItems(page.getAll());
		tableFacade.setStateAttr("restore");
		tableFacade.setTotalRows((int) page.getTotalCount());
//		tableFacade.setExportTypes(response, CSV, EXCEL, PDFP);
		return tableFacade;
	}

	/**
	 * 重新渲染TableFacade，使其具有修改、删除的默认操作选项.
	*/
	public static TableFacade reOrnamentFacade(TableFacade facade,String id) {
		HtmlComponentFactory factory = new HtmlComponentFactory(facade
				.getWebContext(), facade.getCoreContext());
		HtmlColumn column = factory.createColumn("");
		column.setTitle("操作");
		column.setWidth("100px");
		column.setSortable(false);
		column.setFilterable(false);
		idName = id;
		CellEditor cellEditor = new CellEditor() {
			public Object getValue(Object item, String property, int rowcount) {
				Object id = ItemUtils.getItemValue(item, idName);
				String modifyJS = "javascript:modify(" + id + ")";
				String removeJS = "javascript:if(confirm('删除后不可恢复,确定要删除该信息么?')){remove(" + id + ")}";
//				String viewJS = "javascript:clone(" + id + ")";
				HtmlBuilder html = new HtmlBuilder();
				html.a().href().quote().append(modifyJS).quote().close();
				html.append("[修改]");
				html.aEnd();
//				html.append("、").a().href().quote().append(viewJS).quote().close();
//				html.append("复制新增");
//				html.aEnd();
				html.append(" ").a().href().quote().append(removeJS).quote().close();
                html.append("[删除]");
				html.aEnd();
				return html.toString();
			}
		};

		column.getCellRenderer().setCellEditor(cellEditor);
		facade.getTable().getRow().addColumn(column);
		return facade;
	}
}
