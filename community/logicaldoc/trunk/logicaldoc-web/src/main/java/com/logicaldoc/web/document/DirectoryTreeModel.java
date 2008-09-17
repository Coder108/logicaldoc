package com.logicaldoc.web.document;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.logicaldoc.core.security.Menu;
import com.logicaldoc.core.security.dao.MenuDAO;
import com.logicaldoc.util.Context;

import com.icesoft.faces.component.tree.IceUserObject;
import com.icesoft.faces.component.tree.Tree;
import com.logicaldoc.web.SessionManagement;
import com.logicaldoc.web.StyleBean;
import com.logicaldoc.web.i18n.Messages;

/**
 * A tree model specialized for LogicalDOC's directories
 * 
 * @author Marco Meschieri
 * @version $Id:$
 * @since 3.0
 */
public class DirectoryTreeModel extends DefaultTreeModel {
	private static final long serialVersionUID = 1L;

	protected static Log log = LogFactory.getLog(DirectoryTreeModel.class);

	// Utility map of all directories (key is the menuId)
	private Map<Integer, Directory> directories = new HashMap<Integer, Directory>();

	private Directory selectedDir;

	private DefaultMutableTreeNode selectedNode;

	private boolean countChildren = false;

	public DirectoryTreeModel() {
		super(new DefaultMutableTreeNode());
		reload();
	}

	public boolean isCountChildren() {
		return countChildren;
	}

	public void setCountChildren(boolean countChildren) {
		this.countChildren = countChildren;
	}

	public void reloadAll() {
		init();
		reload((DefaultMutableTreeNode) getRoot(), -1);
	}

	public void reload(DefaultMutableTreeNode node, int depth) {
		Directory dir = ((Directory) node.getUserObject());
		String username = SessionManagement.getUsername();
		MenuDAO menuDao = (MenuDAO) Context.getInstance().getBean(MenuDAO.class);

		try {
			if (username != null) {
				List<Menu> menus = (List<Menu>) menuDao.findByUserName(username, dir.getMenuId(),
						Menu.MENUTYPE_DIRECTORY);

				// Sort by name
				Collections.sort(menus, new Comparator<Menu>() {
					@Override
					public int compare(Menu arg0, Menu arg1) {
						return arg0.getMenuText().compareTo(arg1.getMenuText());
					}
				});

				for (Menu menu : menus) {
					addDir(username, node, menu, depth);
					dir.setLeaf(false);
				}

				if (countChildren)
					dir.setCount(menuDao.countByUserName(username, dir.getMenuId(), null));
			}
		} catch (Throwable t) {
			log.error(t.getMessage(), t);
		}
	}

	public void reload(DefaultMutableTreeNode node) {
		reload(node, 1);
	}

	private void init() {
		directories.clear();

		// build root node so that children can be attached
		MenuDAO menuDao = (MenuDAO) Context.getInstance().getBean(MenuDAO.class);
		Menu rootMenu = menuDao.findByPrimaryKey(Menu.MENUID_DOCUMENTS);
		Directory rootObject = new Directory(rootMenu);
		rootObject.setIcon(StyleBean.XP_BRANCH_CONTRACTED_ICON);
		rootObject.setDisplayText(null);
		rootObject.setContentTitle(null);
		rootObject.setPageContent(true);

		String label = Messages.getMessage(rootMenu.getMenuText());
		rootObject.setDisplayText(label);
		rootObject.setContentTitle(label);

		DefaultMutableTreeNode rootTreeNode = new DefaultMutableTreeNode(rootObject);
		rootObject.setWrapper(rootTreeNode);
		setRoot(rootTreeNode);
		rootTreeNode.setUserObject(rootObject);
		directories.put(rootMenu.getMenuId(), rootObject);
		if (countChildren)
			rootObject.setCount(menuDao.countByUserName(SessionManagement.getUsername(), Menu.MENUID_DOCUMENTS, null));
	}

	public void reload() {
		init();
		reload((DefaultMutableTreeNode) getRoot());
	}

	public Map<Integer, Directory> getDirectories() {
		return directories;
	}

	public Directory getDirectory(int id) {
		return directories.get(id);
	}

	/**
	 * Adds a new directory in the specified place
	 * 
	 * @param dir The new directory's menu
	 * @param parent The parent directory
	 */
	public void addNewDir(Menu dir, Directory parent) {
		Directory parentDir = parent;

		if (parentDir == null) {
			parentDir = selectedDir;
		}

		if (countChildren)
			parentDir.setCount(parentDir.getCount() + 1);
		parentDir.setLeaf(false);

		DefaultMutableTreeNode parentNode = findDirectoryNode(parentDir.getMenuId(), (DefaultMutableTreeNode) getRoot());
		addDir(SessionManagement.getUsername(), parentNode, dir);
	}

	private DefaultMutableTreeNode addDir(String username, DefaultMutableTreeNode parentNode, Menu dir) {
		return addDir(SessionManagement.getUsername(), parentNode, dir, -1);
	}

	/**
	 * Changes the currently selected directory and updates the documents list.
	 * 
	 * @param directoryId
	 */
	public void selectDirectory(Directory directory) {
		selectedDir = directory;
		selectedDir.setSelected(true);
		setTreeSelectedState((DefaultMutableTreeNode) getRoot());
		expandNodePath(selectedNode);
	}

	/**
	 * Finds the directory node with the specified identifier contained in a
	 * sublevel of the the passed tree node
	 * 
	 * @param direcoryId The directory identifier
	 * @param parent The node in which the directory must be searched
	 * @return The found tree node, null if not found
	 */
	private DefaultMutableTreeNode findDirectoryNode(int direcoryId, DefaultMutableTreeNode parent) {
		Directory dir = (Directory) parent.getUserObject();

		if (dir.getMenu().getMenuId() == direcoryId) {
			return parent;
		} else {
			Enumeration<DefaultMutableTreeNode> enumer = (Enumeration<DefaultMutableTreeNode>) parent.children();

			while (enumer.hasMoreElements()) {
				DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) enumer.nextElement();
				DefaultMutableTreeNode node = findDirectoryNode(direcoryId, childNode);

				if (node != null) {
					return node;
				}
			}
		}

		return null;
	}

	/**
	 * Adds a directory and all it's childs
	 * 
	 * @param username
	 * @param parent
	 * @param dir
	 * @param depth the maximum depth
	 * @return
	 */
	private DefaultMutableTreeNode addDir(String username, DefaultMutableTreeNode parent, Menu dir, int depth) {
		MenuDAO menuDao = (MenuDAO) Context.getInstance().getBean(MenuDAO.class);

		Directory cachedDir = directories.get(dir.getMenuId());

		if ((cachedDir != null) && cachedDir.isLoaded()) {
			DefaultMutableTreeNode node = findDirectoryNode(dir.getMenuId(), parent);

			if (node != null) {
				return node;
			}
		}

		// Component menu item
		Directory branchObject = new Directory(dir);
		branchObject.setDisplayText(dir.getMenuText());
		branchObject.setContentTitle(dir.getMenuText());
		branchObject.setIcon(StyleBean.XP_BRANCH_CONTRACTED_ICON);

		DefaultMutableTreeNode branchNode = new DefaultMutableTreeNode(branchObject);
		branchObject.setWrapper(branchNode);
		branchObject.setPageContent(false);
		branchObject.setLeaf(true);
		branchObject.setExpanded(false);
		branchNode.setUserObject(branchObject);

		// Iterate over subdirs
		if (depth != 0) {
			List<Menu> children = (List<Menu>)menuDao.findByUserName(username, dir.getMenuId(), Menu.MENUTYPE_DIRECTORY);
			Collections.sort(children, new Comparator<Menu>(){
				@Override
				public int compare(Menu menu1, Menu menu2) {
					return menu1.getMenuText().compareTo(menu2.getMenuText());
				}
			});
			for (Menu child : children) {
				branchObject.setLeaf(false);

				if (depth > 0) {
					addDir(username, branchNode, child, depth - 1);
				} else if (depth == -1) {
					addDir(username, branchNode, child, depth);
				}
			}
		} else {
			branchObject.setLeaf(menuDao.countByUserName(username, dir.getMenuId(), Menu.MENUTYPE_DIRECTORY) == 0);
		}

		branchObject.setLoaded(true);
		directories.put(dir.getMenuId(), branchObject);
		parent.add(branchNode);
		if (countChildren)
			branchObject.setCount(menuDao.countByUserName(username, dir.getMenuId(), null));
		log.debug("added dir " + branchObject.getDisplayText());

		return branchNode;
	}

	public void selectDirectory(int id) {
		selectedDir = directories.get(id);
		selectDirectory(selectedDir);
	}

	/**
	 * Set the selection state for all directories in the tree
	 */
	protected void setTreeSelectedState(DefaultMutableTreeNode node) {
		if ((node.getUserObject() != null) && node.getUserObject() instanceof Directory) {
			Directory dir = (Directory) node.getUserObject();

			if ((selectedDir != null) && (dir.getMenu() != null) && dir.getMenu().equals(selectedDir.getMenu())) {
				dir.setSelected(true);
				dir.setIcon(dir.getBranchExpandedIcon());
				selectedNode = node;
			} else {
				dir.setSelected(false);
				dir.setIcon(dir.getBranchContractedIcon());
			}
		}

		Enumeration<DefaultMutableTreeNode> enumer = (Enumeration<DefaultMutableTreeNode>) node.children();

		while (enumer.hasMoreElements()) {
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) enumer.nextElement();
			setTreeSelectedState(childNode);
		}
	}

	public Directory getSelectedDir() {
		return selectedDir;
	}

	public DefaultMutableTreeNode getSelectedNode() {
		return selectedNode;
	}

	/**
	 * Expand all nodes from the passed one back to the root following the
	 * parent/child relation.
	 * 
	 * @param node the leaf node to expand
	 */
	private void expandNodePath(DefaultMutableTreeNode node) {
		IceUserObject obj = (IceUserObject) node.getUserObject();
		obj.setExpanded(true);

		if (!node.equals(getRoot())) {
			expandNodePath((DefaultMutableTreeNode) node.getParent());
		}
	}

	/**
	 * Opens and selects the specified folder. The algorithm is optimised so
	 * that the minimal path is explored and database accesses are reduced.
	 * 
	 * @param menuId The folder menuId
	 */
	public void openFolder(int menuId) {
		// Reset the tree
		init();

		// Now try to construct the minimal portion of the tree, just to show
		// opened folder
		MenuDAO menuDao = (MenuDAO) Context.getInstance().getBean(MenuDAO.class);
		Menu folderMenu = menuDao.findByPrimaryKey(menuId);
		Collection<Menu> parents = folderMenu.getParents();
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) getRoot();
		String username = SessionManagement.getUsername();
		for (Menu folderParent : parents) {
			if (folderParent.getMenuId() == Menu.MENUID_HOME || folderParent.getMenuId() == Menu.MENUID_DOCUMENTS)
				continue;
			parentNode = addDir(username, parentNode, folderParent, 0);
		}
		parentNode = addDir(username, parentNode, folderMenu, 0);
		expandNodePath(parentNode);
	}

	public void onSelectDirectory(ActionEvent event) {
		int directoryId = Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("directoryId"));
		selectDirectory(directoryId);
		reload(getSelectedNode());
	}

	public void nodeClicked(ActionEvent event) {
		Tree tree = (Tree) event.getSource();
		DefaultMutableTreeNode node = tree.getNavigatedNode();
		IceUserObject userObject = (IceUserObject) node.getUserObject();

		if (userObject.isExpanded()) {
			reload(node);
		}
	}
}
