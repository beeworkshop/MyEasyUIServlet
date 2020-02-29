package com.bee.web;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bee.dao.UserDao;
import com.bee.model.User;
import com.bee.utils.DBUtil;
import com.bee.utils.ResponseUtil;
import com.bee.utils.StringUtil;

import net.sf.json.JSONObject;

public class UserSaveServlet extends HttpServlet {

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

		request.setCharacterEncoding("utf-8");

		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
		String qq = request.getParameter("qq");
		String id = request.getParameter("id");

		User user = new User(name, phone, email, qq);
		if (StringUtil.isNotEmpty(id)) {
			user.setId(Integer.parseInt(id));
		}

		Connection con = null;
		try {
			int saveNums = 0;
			con = dbUtil.getConnection();
			JSONObject result = new JSONObject();
			if (StringUtil.isNotEmpty(id)) {
				saveNums = userDao.userModify(con, user);
			} else {
				saveNums = userDao.userAdd(con, user);
			}
			if (saveNums == 1) {
				result.put("success", "true");
			} else {
				result.put("success", "true");
				result.put("errorMsg", "保存失败");
			}
			result.put("data", user);
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