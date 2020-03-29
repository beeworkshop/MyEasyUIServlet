package com.bee.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.bee.model.PageBean;
import com.bee.model.User;

public class UserDao {

	public ResultSet exportExcel(Connection con) throws Exception {
		String sql = "select * from t_user";
		PreparedStatement pstmt = con.prepareStatement(sql);
		return pstmt.executeQuery();
	}

	public ResultSet exportExcel(Connection con, String searchKey) throws Exception {
		String sql = "select * from t_user where name like ? or phone like ? or email like ? or qq like ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, "%" + searchKey + "%");
		pstmt.setString(2, "%" + searchKey + "%");
		pstmt.setString(3, "%" + searchKey + "%");
		pstmt.setString(4, "%" + searchKey + "%");
		return pstmt.executeQuery();
	}

	public ResultSet userList(Connection con, PageBean pageBean) throws Exception {
		String sql = "select * from t_user limit ?,?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, pageBean.getStart());
		pstmt.setInt(2, pageBean.getRows());
		return pstmt.executeQuery();
	}

	public ResultSet userList(Connection con, PageBean pageBean, String searchKey) throws Exception {
		String sql = "select * from t_user where name like ? or phone like ? or email like ? or qq like ? limit ?,?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, "%" + searchKey + "%");
		pstmt.setString(2, "%" + searchKey + "%");
		pstmt.setString(3, "%" + searchKey + "%");
		pstmt.setString(4, "%" + searchKey + "%");
		pstmt.setInt(5, pageBean.getStart());
		pstmt.setInt(6, pageBean.getRows());
		return pstmt.executeQuery();
	}

	public int userCount(Connection con) throws Exception {
		String sql = "select count(*) as total from t_user";
		PreparedStatement pstmt = con.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			return rs.getInt("total");
		} else {
			return 0;
		}
	}

	public int userCount(Connection con, String searchKey) throws Exception {
		String sql = "select count(*) as total from t_user where name like ? or phone like ? or email like ? or qq like ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, "%" + searchKey + "%");
		pstmt.setString(2, "%" + searchKey + "%");
		pstmt.setString(3, "%" + searchKey + "%");
		pstmt.setString(4, "%" + searchKey + "%");
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			return rs.getInt("total");
		} else {
			return 0;
		}
	}

	public int userDelete(Connection con, String delId) throws Exception {
		String sql = "delete from t_user where id=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, delId);
		return pstmt.executeUpdate();
	}

	public int userAdd(Connection con, User user) throws Exception {
		String sql = "insert into t_user values(null,?,?,?,?)";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, user.getName());
		pstmt.setString(2, user.getPhone());
		pstmt.setString(3, user.getEmail());
		pstmt.setString(4, user.getQq());
		return pstmt.executeUpdate();
	}

	public int userModify(Connection con, User user) throws Exception {
		String sql = "update t_user set name=?,phone=?,email=?,qq=? where id=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, user.getName());
		pstmt.setString(2, user.getPhone());
		pstmt.setString(3, user.getEmail());
		pstmt.setString(4, user.getQq());
		pstmt.setInt(5, user.getId());
		return pstmt.executeUpdate();
	}
}