package com.bee.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.bee.dao.UserDao;
import com.bee.utils.DBUtil;
import com.bee.utils.ExcelUtil;
import com.bee.utils.ResponseUtil;

public class ExportExcelServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private DBUtil dbUtil = new DBUtil();
	private UserDao userDao = new UserDao();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8"); // 设定编码
		String searchKey = request.getParameter("searchKey");

		Connection con = null;
		try {
			con = dbUtil.getConnection();
			ResultSet rs = null;
			if (searchKey != null) {
				// HTTP GET的URL参数默认是ISO-8859-1编码
				searchKey = new String(searchKey.getBytes("ISO-8859-1"), "UTF-8");
				System.out.println("导出结果：" + searchKey);
				rs = userDao.exportExcel(con, searchKey);
			} else {
				rs = userDao.exportExcel(con);
			}
			Workbook wb = ExcelUtil.fillExcelData(rs, "template.xls");
			ResponseUtil.export(response, wb, searchKey != null ? "导出搜索结果（" + searchKey + "）.xls" : "导出全量.xls");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeConnection(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
