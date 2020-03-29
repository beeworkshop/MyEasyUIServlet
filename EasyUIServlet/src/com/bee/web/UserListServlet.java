package com.bee.web;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bee.dao.UserDao;
import com.bee.model.PageBean;
import com.bee.utils.DBUtil;
import com.bee.utils.JsonUtil;
import com.bee.utils.ResponseUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class UserListServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	DBUtil dbUtil = new DBUtil();
	UserDao userDao = new UserDao();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String page = request.getParameter("page");
		String rows = request.getParameter("rows");

		String searchKey = request.getParameter("searchVal");
		System.out.println("查找：" + searchKey);

		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));

		Connection con = null;
		try {
			con = dbUtil.getConnection();
			JSONObject result = new JSONObject();
			JSONArray jsonArray = null;
			int total = 0;
			if (searchKey != null) {
				jsonArray = JsonUtil.formatRsToJsonArray(userDao.userList(con, pageBean, searchKey));
				total = userDao.userCount(con, searchKey);
			} else {
				jsonArray = JsonUtil.formatRsToJsonArray(userDao.userList(con, pageBean));
				total = userDao.userCount(con);
			}
			result.put("rows", jsonArray);
			result.put("total", total);
			ResponseUtil.write(response, result);
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