package com.logicaldoc.web.admin;

import java.util.ArrayList;

import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.logicaldoc.core.security.Group;
import com.logicaldoc.core.security.SecurityManager;
import com.logicaldoc.core.security.User;
import com.logicaldoc.core.security.dao.GroupDAO;
import com.logicaldoc.core.security.dao.UserDAO;
import com.logicaldoc.util.Context;
import com.logicaldoc.web.SessionManagement;
import com.logicaldoc.web.i18n.Messages;
import com.logicaldoc.web.util.FacesUtil;

/**
 * Form for user editing
 * 
 * @author Marco Meschieri
 * @version $Id: UserForm.java,v 1.1 2007/10/16 16:10:33 marco Exp $
 * @since 3.0
 */
public class UserForm {
	protected static Log log = LogFactory.getLog(UserForm.class);

	private long[] group;

	private User user;

	private boolean createNew = false;

	private String password;

	private String repass;

	public User getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepass() {
		return repass;
	}

	public void setRepass(String repass) {
		this.repass = repass;
	}

	public void setUser(User usr) {
		this.user = usr;
		createNew = StringUtils.isEmpty(this.user.getUserName());
		group = this.user.getGroupIds();
	}

	public String save() {
		return save(true);
	}

	public String saveWithoutPassword() {
		return save(false);
	}

	private String save(boolean withPassword) {
		if (SessionManagement.isValid()) {
			try {
				UserDAO dao = (UserDAO) Context.getInstance().getBean(UserDAO.class);

				User tempuser = dao.findByUserName(user.getUserName());

				if ((tempuser != null) && !tempuser.equals(user)) {
					Messages.addLocalizedError("errors.action.userexists");

					return null;
				}

				if (withPassword) {
					if (!getPassword().equals(getRepass())) {
						Messages.addLocalizedError("msg.jsp.adduser.repass");

						return null;
					}

					if (StringUtils.isNotEmpty(getPassword()) && !user.getPassword().equals(getPassword())) {
						// The password was changed
						user.setDecodedPassword(getPassword());
					}

					user.setRepass("");
				}

				SecurityManager manager = (SecurityManager) Context.getInstance().getBean(SecurityManager.class);
				manager.removeUserFromAllGroups(user);

				if (createNew) {
					dao.store(user);
				}

				GroupDAO gdao = (GroupDAO) Context.getInstance().getBean(GroupDAO.class);
				Group adminGroup = gdao.findByName("admin");

				// The admin user must always member of admin group
				if ("admin".equals(user.getUserName())) {
					ArrayList<Long> tmp = new ArrayList<Long>();
					boolean adminFound = false;
					for (int i = 0; i < group.length; i++) {
						tmp.add(group[i]);
						if (adminGroup.getId() == group[i])
							adminFound = true;
					}
					if (!adminFound)
						tmp.add(adminGroup.getId());

					group = new long[tmp.size()];
					for (int i = 0; i < tmp.size(); i++) {
						group[i] = tmp.get(i).longValue();
					}
				}

				manager.assignUserToGroups(user, group);

				boolean stored = dao.store(user);

				if (!stored) {
					Messages.addLocalizedError("errors.action.saveuser.notstored");
				} else {
					Messages.addLocalizedInfo("msg.action.changeuser");
				}

				UsersRecordsManager recordsManager = ((UsersRecordsManager) FacesUtil.accessBeanFromFacesContext(
						"usersRecordsManager", FacesContext.getCurrentInstance(), log));
				recordsManager.reload();
				recordsManager.setSelectedPanel("list");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				Messages.addLocalizedError("errors.action.saveuser.notstored");
			}

			return null;
		} else {
			return "login";
		}
	}

	public int getGroupCount() {
		return group.length;
	}

	public long[] getGroup() {
		return group;
	}

	public void setGroup(long[] group) {
		this.group = group;
	}
}
