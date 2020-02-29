package com.bee.web;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bee.dao.UserDao;
import com.bee.utils.DBUtil;
import com.bee.utils.ResponseUtil;

import net.sf.json.JSONObject;

public class UserDeleteServlet extends HttpServlet {

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

		String delId = request.getParameter("delId");

		Connection con = null;
		try {
			con = dbUtil.getConnection();
			JSONObject result = new JSONObject();
			int delNums = userDao.userDelete(con, delId);
			if (delNums == 1) {
				result.put("success", "true");
			} else {
				result.put("errorMsg", "删除失败");
			}
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