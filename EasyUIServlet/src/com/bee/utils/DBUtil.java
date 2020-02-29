package com.bee.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBUtil {
	private DataSource ds;

	public DBUtil() {
		Context ctx;
		try {
			ctx = new InitialContext();
			if (ctx == null) {
				throw new Exception("No MySQL DB Context");
			}
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/MyDB");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() throws Exception {
		return ds.getConnection();
	}

	public void closeConnection(Connection con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void closePrepStmt(PreparedStatement prepStmt) {
		try {
			if (prepStmt != null) {
				prepStmt.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void closeResultSet(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}