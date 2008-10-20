package com.logicaldoc.web.admin;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.icesoft.faces.component.tree.IceUserObject;
import com.icesoft.faces.component.tree.Tree;
import com.logicaldoc.core.communication.EMailAccount;
import com.logicaldoc.core.communication.dao.EMailAccountDAO;
import com.logicaldoc.core.security.Menu;
import com.logicaldoc.core.security.dao.MenuDAO;
import com.logicaldoc.util.Context;
import com.logicaldoc.web.SessionManagement;
import com.logicaldoc.web.document.Directory;
import com.logicaldoc.web.document.DirectoryTreeModel;
import com.logicaldoc.web.i18n.Messages;
import com.logicaldoc.web.util.FacesUtil;

/**
 * This is the account editing form
 * 
 * @author Marco Meschieri
 * @version $Id:$
 * @since ###release###
 */
public class AccountForm {
	protected static Log log = LogFactory.getLog(AccountForm.class);

	private EMailAccount account;

	private DirectoryTreeModel directoryModel;

	private UIComponent mailAddress;

	private UIComponent accountUser;

	private UIComponent accountPassword;

	private UIComponent provider;

	private UIComponent deleteFromMailbox;

	private UIComponent host;

	private UIComponent port;

	private UIComponent allowedTypes;

	private UIComponent language;

	private String password;

	private boolean showFolderSelector = false;

	public String getPassword() {
		return password;
	}

	public boolean isShowFolderSelector() {
		return showFolderSelector;
	}

	public void setShowFolderSelector(boolean showFolderSelector) {
		this.showFolderSelector = showFolderSelector;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private void clear() {
		((UIInput) mailAddress).setSubmittedValue(null);
		((UIInput) accountUser).setSubmittedValue(null);
		((UIInput) accountPassword).setSubmittedValue(null);
		((UIInput) provider).setSubmittedValue(null);
		((UIInput) deleteFromMailbox).setSubmittedValue(null);
		((UIInput) host).setSubmittedValue(null);
		((UIInput) port).setSubmittedValue(null);
		((UIInput) allowedTypes).setSubmittedValue(null);
		((UIInput) language).setSubmittedValue(null);
		password = null;
	}

	private boolean deletePassword;

	public EMailAccount getAccount() {
		return account;
	}

	public void setAccount(EMailAccount account) {
		this.account = account;
		this.directoryModel = new DirectoryTreeModel();
		clear();
		this.password = account.getPassword();
	}

	public void nodeClicked(ActionEvent event) {
		Tree tree = (Tree) event.getSource();
		DefaultMutableTreeNode node = tree.getNavigatedNode();
		IceUserObject userObject = (IceUserObject) node.getUserObject();

		if (userObject.isExpanded()) {
			directoryModel.reload(node);
		}
	}

	public DirectoryTreeModel getDirectoryModel() {
		if (directoryModel == null) {
			loadTree();
		}

		return directoryModel;
	}

	public String save() {
		if (SessionManagement.isValid()) {
			try {
				if (directoryModel.getSelectedDir() != null) {
					account.setTargetFolderId(directoryModel.getSelectedDir().getMenuId());
					account.setTargetFolder(directoryModel.getSelectedDir().getMenu());
				}

				EMailAccountDAO accountDao = (EMailAccountDAO) Context.getInstance().getBean(EMailAccountDAO.class);
				MenuDAO menuDao = (MenuDAO) Context.getInstance().getBean(MenuDAO.class);

				if (account.getTargetFolderId() != null) {
					Menu menu = menuDao.findById(account.getTargetFolderId().longValue());
					account.setTargetFolder(menu);
				} else {
					account.setTargetFolder(null);
				}

				account.setUserId(SessionManagement.getUserId());

				if (StringUtils.isNotEmpty(password)) {
					account.setPassword(password);
				} else if (deletePassword) {
					account.setPassword(null);
				}

				boolean stored = accountDao.store(account);

				if (stored) {
					Messages.addLocalizedInfo("msg.action.saveemail");
				} else {
					Messages.addLocalizedError("errors.action.saveemail");
				}

				AccountsRecordsManager recordsManager = ((AccountsRecordsManager) FacesUtil.accessBeanFromFacesContext(
						"accountsRecordsManager", FacesContext.getCurrentInstance(), log));
				recordsManager.reload();
				recordsManager.setSelectedPanel("list");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				Messages.addLocalizedError("errors.action.saveemail");
			}
		} else {
			return "login";
		}

		return null;
	}

	void loadTree() {
		directoryModel = new DirectoryTreeModel();
	}

	public String selectDirectory() {
		return null;
	}

	public UIComponent getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(UIComponent mailAddress) {
		this.mailAddress = mailAddress;
	}

	public UIComponent getAccountUser() {
		return accountUser;
	}

	public void setAccountUser(UIComponent accountUser) {
		this.accountUser = accountUser;
	}

	public UIComponent getAccountPassword() {
		return accountPassword;
	}

	public void setAccountPassword(UIComponent accountPassword) {
		this.accountPassword = accountPassword;
	}

	public UIComponent getProvider() {
		return provider;
	}

	public void setProvider(UIComponent provider) {
		this.provider = provider;
	}

	public UIComponent getDeleteFromMailbox() {
		return deleteFromMailbox;
	}

	public void setDeleteFromMailbox(UIComponent deleteFromMailbox) {
		this.deleteFromMailbox = deleteFromMailbox;
	}

	public UIComponent getHost() {
		return host;
	}

	public void setHost(UIComponent host) {
		this.host = host;
	}

	public UIComponent getPort() {
		return port;
	}

	public void setPort(UIComponent port) {
		this.port = port;
	}

	public UIComponent getAllowedTypes() {
		return allowedTypes;
	}

	public void setAllowedTypes(UIComponent allowedTypes) {
		this.allowedTypes = allowedTypes;
	}

	public UIComponent getLanguage() {
		return language;
	}

	public void setLanguage(UIComponent language) {
		this.language = language;
	}

	public String removePassword() {
		setPassword(null);
		deletePassword = true;
		return null;
	}

	public boolean isEmptyPassword() {
		return StringUtils.isEmpty(password);
	}

	public String getTargetDirectoryName() {
		if (account != null && account.getTargetFolder() != null)
			return account.getTargetFolder().getText();
		else
			return null;
	}

	public void openFolderSelector(ActionEvent e) {
		showFolderSelector = true;
		if (account != null && account.getTargetFolderId() != null)
			directoryModel.selectDirectory(account.getTargetFolderId());
	}

	public void closeFolderSelector(ActionEvent e) {
		showFolderSelector = false;
	}

	public void folderSelected(ActionEvent e) {
		showFolderSelector = false;
		Directory dir = directoryModel.getSelectedDir();
		Menu menu = dir.getMenu();
		account.setTargetFolder(menu);
	}
}
