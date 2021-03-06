package com.logicaldoc.web.data;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.logicaldoc.core.security.Folder;
import com.logicaldoc.core.security.UserSession;
import com.logicaldoc.core.security.dao.FolderDAO;
import com.logicaldoc.util.Context;
import com.logicaldoc.web.util.SessionUtil;

/**
 * This servlet is responsible for folders data.
 * 
 * @author Marco Meschieri - Logical Objects
 * @since 6.0
 */
public class FoldersDataServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(FoldersDataServlet.class);

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		try {
			UserSession session = SessionUtil.validateSession(request);

			long parent = Long.parseLong(request.getParameter("parent"));

			response.setContentType("text/xml");
			response.setCharacterEncoding("UTF-8");

			// Headers required by Internet Explorer
			response.setHeader("Pragma", "public");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0,pre-check=0");
			response.setHeader("Expires", "0");

			Context context = Context.getInstance();
			FolderDAO dao = (FolderDAO) context.getBean(FolderDAO.class);

			PrintWriter writer = response.getWriter();
			writer.write("<list>");

			// if (parent == Constants.ROOT_FOLDERID &&
			// dao.isReadEnable(Folder.ROOTID, session.getUserId())) {
			// // Add the 'Documents' root
			// writer.print("<folder>");
			// writer.print("<folderId>" + Folder.ROOTID + "</folderId>");
			// writer.print("<parent>" + parent + "</parent>");
			// writer.print("<name>/</name>");
			// writer.print("<type>1</type>");
			// writer.print("</folder>");
			// writer.write("</list>");
			// return;
			// }

			/*
			 * Get the visible children
			 */
			List<Folder> folders = dao.findChildren(parent, session.getUserId());

			// Sort for name asc
			Collections.sort(folders, new Comparator<Folder>() {
				@Override
				public int compare(Folder o1, Folder o2) {
					return o1.getName().compareTo(o2.getName());
				}

			});

			/*
			 * Iterate over records composing the response XML document
			 */
			for (Folder folder : folders) {
				writer.print("<folder>");
				writer.print("<folderId>" + folder.getId() + "</folderId>");
				writer.print("<parent>" + folder.getParentId() + "</parent>");
				writer.print("<name><![CDATA[" + folder.getName() + "]]></name>");
				writer.print("<type>" + folder.getType() + "</type>");
				writer.print("</folder>");
			}

			writer.write("</list>");
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			if (e instanceof ServletException)
				throw (ServletException) e;
			else if (e instanceof IOException)
				throw (IOException) e;
			else
				throw new ServletException(e.getMessage(), e);
		}
	}
}