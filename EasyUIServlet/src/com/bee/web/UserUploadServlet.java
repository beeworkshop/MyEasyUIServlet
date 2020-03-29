package com.bee.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

import com.bee.dao.UserDao;
import com.bee.model.User;
import com.bee.utils.DBUtil;
import com.bee.utils.DateUtil;
import com.bee.utils.ExcelUtil;
import com.bee.utils.ResponseUtil;

import net.sf.json.JSONObject;

public class UserUploadServlet extends HttpServlet {

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

		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);

		Iterator<FileItem> it = null;
		try {
			List<FileItem> items = upload.parseRequest(new ServletRequestContext(request));
			it = items.iterator();
		} catch (FileUploadException e1) {
			e1.printStackTrace();
		}

		String userUploadFile = null;

		while (it.hasNext()) {
			FileItem item = it.next();
			userUploadFile = item.getName();
			System.out.println("上载的文件：" + userUploadFile);
			if (!item.isFormField() && userUploadFile != null) { // 是上传的文件（二进制数据）
				try {
					// 取当前日期作为文件名，取上传文件的扩展名为服务器端存储文件的扩展名。
					String fileName = DateUtil.getCurrentDateStr();
					String extName = userUploadFile.split("\\.")[1];// java中由于转义\变\\
					// 在后端访问文件系统只能使用绝对路径
					String filePath = "e:/tmp/" + fileName + "." + extName;
					System.out.println("上传文件存储在这里 --> " + filePath);
					userUploadFile = filePath;

					item.write(new File(userUploadFile));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		JSONObject result = new JSONObject();
		if (userUploadFile == null) {
			result.put("errorMsg", "我去，上传失败！");
			try {
				ResponseUtil.write(response, result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}

		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(new File(userUploadFile)));
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFSheet hssfSheet = wb.getSheetAt(0); // 获取第一个Sheet页
		if (hssfSheet != null) {
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) { // 第二行开始
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				// 用户的实体类（Entity/Model）：POJO/DTO/JavaBean
				User user = new User();
				user.setName(ExcelUtil.formatCell(hssfRow.getCell(0)));
				user.setPhone(ExcelUtil.formatCell(hssfRow.getCell(1)));
				user.setEmail(ExcelUtil.formatCell(hssfRow.getCell(2)));
				user.setQq(ExcelUtil.formatCell(hssfRow.getCell(3)));
				Connection con = null;
				try {
					con = dbUtil.getConnection();
					userDao.userAdd(con, user);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					dbUtil.closeConnection(con);
				}
			}
		}

		result.put("success", "true");
		try {
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
