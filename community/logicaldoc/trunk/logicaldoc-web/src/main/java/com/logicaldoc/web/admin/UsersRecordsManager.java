package com.logicaldoc.web.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.logicaldoc.core.FileBean;
import com.logicaldoc.core.communication.EMail;
import com.logicaldoc.core.communication.dao.EMailDAO;
import com.logicaldoc.core.document.History;
import com.logicaldoc.core.document.dao.HistoryDAO;
import com.logicaldoc.core.security.SecurityManager;
import com.logicaldoc.core.security.User;
import com.logicaldoc.core.security.UserDoc;
import com.logicaldoc.core.security.dao.MenuDAO;
import com.logicaldoc.core.security.dao.UserDAO;
import com.logicaldoc.core.security.dao.UserDocDAO;
import com.logicaldoc.util.Context;
import com.logicaldoc.util.config.SettingsConfig;
import com.logicaldoc.web.SessionManagement;
import com.logicaldoc.web.components.SortableList;
import com.logicaldoc.web.i18n.Messages;
import com.logicaldoc.web.util.FacesUtil;

/**
 * <p>
 * The <code>UsersRecordsManager</code> class is responsible for constructing
 * the list of <code>User</code> beans which will be bound to a ice:dataTable
 * JSF component. <p/>
 * <p>
 * Large data sets could be handle by adding a ice:dataPaginator. Alternatively
 * the dataTable could also be hidden and the dataTable could be added to
 * scrollable ice:panelGroup.
 * </p>
 * 
 * @author Marco Meschieri
 * @version $Id: DocumentsRecordsManager.java,v 1.1 2007/06/29 06:28:29 marco
 *          Exp $
 * @since 3.0
 */
public class UsersRecordsManager extends SortableList {

	protected static Log log = LogFactory.getLog(UsersRecordsManager.class);

	private List<User> users = new ArrayList<User>();

	private String selectedPanel = "list";

	public UsersRecordsManager() {
		// We don't sort by default
		super("xxx");
	}

	public void reload() {
		users.clear();

		try {
			MenuDAO mdao = (MenuDAO) Context.getInstance().getBean(MenuDAO.class);
			String uname = SessionManagement.getUsername();

			if (mdao.isReadEnable(6, uname)) {
				UserDAO dao = (UserDAO) Context.getInstance().getBean(UserDAO.class);
				Collection<User> tmpusers = dao.findAll();
				users.addAll(tmpusers);

				for (User user : users) {
					user.initGroupNames();
				}
			} else {
				Messages.addLocalizedError("errors.noaccess");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			Messages.addLocalizedError("errors.error");
		}
	}

	public String getSelectedPanel() {
		return selectedPanel;
	}

	public void setSelectedPanel(String panel) {
		this.selectedPanel = panel;
	}

	public String addUser() {
		selectedPanel = "add";

		UserForm userForm = ((UserForm) FacesUtil.accessBeanFromFacesContext("userForm", FacesContext
				.getCurrentInstance(), log));

		User user = new User();
		user.setLanguage(SessionManagement.getLanguage());
		userForm.setUser(user);

		return null;
	}

	public String edit() {
		selectedPanel = "edit";

		UserForm userForm = ((UserForm) FacesUtil.accessBeanFromFacesContext("userForm", FacesContext
				.getCurrentInstance(), log));
		User user = (User) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("user");
		userForm.setUser(user);

		return null;
	}

	public String password() {
		selectedPanel = "passwd";

		UserForm userForm = ((UserForm) FacesUtil.accessBeanFromFacesContext("userForm", FacesContext
				.getCurrentInstance(), log));
		User user = (User) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("user");
		userForm.setUser(user);

		return null;
	}

	public String list() {
		selectedPanel = "list";
		reload();

		return null;
	}

	/**
	 * Gets the list of UserRecord which will be used by the ice:dataTable
	 * component.
	 */
	public Collection<User> getUsers() {
		if (users.size() == 0) {
			reload();
		}

		return users;
	}

	public int getCount() {
		return getUsers().size();
	}

	public String delete() {
		User user = (User) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("user");

		if (SessionManagement.isValid()) {
			try {
				UserDAO dao = (UserDAO) Context.getInstance().getBean(UserDAO.class);
				SecurityManager manager = (SecurityManager) Context.getInstance().getBean(SecurityManager.class);

				// get the user's groups and check if he is member of
				// "admin" group
				User toBeDeletedUser = dao.findByPrimaryKey(user.getUserName());
				boolean isAdmin = false;

				if (toBeDeletedUser != null) {
					toBeDeletedUser.initGroupNames();

					String[] userGroups = toBeDeletedUser.getGroupNames();

					if (userGroups != null) {
						for (int i = 0; i < userGroups.length; i++) {
							if (userGroups[i].equals("admin")) {
								isAdmin = true;

								break;
							}
						}
					}
				}

				// if the user is member of "admin", we have to check that
				// he is not the last user in that group;
				// here we count how many users still belong to group admin
				int adminsFound = 0;

				if (isAdmin) {
					Collection<User> allUsers = dao.findAll();
					Iterator<User> userIter = allUsers.iterator(); // get all
					// users
					while (userIter.hasNext()) {
						User currUser = userIter.next();
						currUser.initGroupNames(); // we always to call
						// this before accessing
						// the groups

						String[] groups = currUser.getGroupNames();

						if (groups != null) {
							for (int i = 0; i < groups.length; i++) {
								if (groups[i].equals("admin")) {
									adminsFound++;

									break; // for performance reasons we
									// break if we found enough
									// users
								}
							}
						}

						// basically we are just interested that there are
						// at least 2 users,
						// so we can safely delete one
						if (adminsFound > 2) {
							break;
						}
					}
				}

				// now we can try to delete the user
				if (!isAdmin || (isAdmin && (adminsFound > 1))) {
					// delete emails and email accounts
					EMailDAO emailDao = (EMailDAO) Context.getInstance().getBean(EMailDAO.class);
					Collection coll = emailDao.findByUserName(user.getUserName());
					Iterator iter = coll.iterator();

					while (iter.hasNext()) {
						EMail email = (EMail) iter.next();
						emailDao.delete(email.getMessageId());
					}

					// delete user doc entries (recently accessed files)
					UserDocDAO userDocDao = (UserDocDAO) Context.getInstance().getBean(UserDocDAO.class);
					Collection<UserDoc> userDocColl = userDocDao.findByUserName(user.getUserName());
					Iterator<UserDoc> userDocIter = userDocColl.iterator();

					while (userDocIter.hasNext()) {
						UserDoc userDoc = userDocIter.next();
						userDocDao.delete(user.getUserName(), userDoc.getDocId());
					}

					// delete all history entries connected to this user
					HistoryDAO historyDAO = (HistoryDAO) Context.getInstance().getBean(HistoryDAO.class);
					Collection<History> historyColl = historyDAO.findByUsername(user.getUserName());
					Iterator<History> historyIter = historyColl.iterator();

					while (historyIter.hasNext()) {
						History history = historyIter.next();
						historyDAO.delete(history.getHistoryId());
					}

					manager.removeUserFromAllGroups(toBeDeletedUser);

					boolean deleted = dao.delete(user.getUserName());

					if (!deleted) {
						Messages.addLocalizedError("errors.action.deleteuser");
					} else {
						Messages.addLocalizedInfo("msg.action.deleteuser");

						SettingsConfig conf = (SettingsConfig) Context.getInstance().getBean(SettingsConfig.class);
						String userdir = conf.getValue("userdir") + "/" + user.getUserName();
						FileBean.deleteDir(userdir);
					}
				} else if (isAdmin && (adminsFound < 2)) {
					Messages.addLocalizedInfo("msg.action.deleteuser.admingroup");
				}

			} catch (Exception e) {
				log.error(e.getMessage(), e);
				Messages.addLocalizedError("errors.action.deleteuser");
			}
		} else {
			return "login";
		}

		setSelectedPanel("list");
		reload();

		return null;
	}

	@Override
	protected boolean isDefaultAscending(String sortColumn) {
		return true;
	}

	/**
	 * Sorts the list of DocumentRecord data.
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void sort(final String column, final boolean ascending) {

		Comparator comparator = new Comparator() {
			public int compare(Object o1, Object o2) {

				User c1 = (User) o1;
				User c2 = (User) o2;
				if (column == null) {
					return 0;
				}
				if (column.equals("name")) {
					return ascending ? c1.getFirstName().compareTo(c2.getFirstName()) : c2.getFirstName().compareTo(
							c1.getFirstName());
				} else if (column.equals("userName")) {
					return ascending ? c1.getUserName().compareTo(c2.getUserName()) : c2.getUserName().compareTo(
							c1.getUserName());
				} else
					return 0;
			}
		};

		Collections.sort(users, comparator);
	}
}
