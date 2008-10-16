package com.logicaldoc.core.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.logicaldoc.core.PersistentObject;

/**
 * This class represents the key concept of security. A Menu not only models
 * menues but also it is used as an element to build hierarchies. With
 * menugroups you can associate groups to a given menu and grant some
 * permissions.
 * 
 * @author Michael Scholz
 * @author Marco Meschieri
 * @version 1.0
 */
public class Menu extends PersistentObject {

	public static final long MENUID_HOME = 1;

	public static final long MENUID_PERSONAL = 4;

	public static final long MENUID_DOCUMENTS = 5;

	public static final long MENUID_MESSAGES = 13;

	public static final long MENUID_EDITME = 19;

	public static final int MENUTYPE_DIRECTORY = 3;

	public static final int MENUTYPE_ACTION = 1;

	public static final int MENUTYPE_HOME = 0;

	private long id = 0;

	private String text = "";

	private long parentId = 0;

	private int sort = 0;

	private String icon = "";

	private String path = "";

	private int type = 2;

	private long size = 0;

	private String ref = "";

	protected Set<MenuGroup> menuGroups = new HashSet<MenuGroup>();

	public Menu() {
	}

	public long getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public long getParentId() {
		return parentId;
	}

	public int getSort() {
		return sort;
	}

	public String getIcon() {
		return icon;
	}

	public String getPath() {
		return path;
	}

	public int getType() {
		return type;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public Set<MenuGroup> getMenuGroups() {
		return menuGroups;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setMenuGroups(Set<MenuGroup> mgroup) {
		menuGroups = mgroup;
	}

	public String[] getMenuGroupNames() {
		ArrayList<String> ids = new ArrayList<String>();
		for (MenuGroup mg : menuGroups) {
			ids.add(mg.getGroupName());
		}
		return (String[]) ids.toArray(new String[] {});
	}

	/**
	 * Adds MenuGroup object given in a String array to the ArrayList of
	 * MenuGroups.
	 * 
	 * @param groups
	 */
	public void setMenuGroup(String[] groups) {
		menuGroups.clear();
		for (int i = 0; i < groups.length; i++) {
			MenuGroup mg = new MenuGroup();
			mg.setGroupName(groups[i]);
			mg.setWriteEnable(1);
			menuGroups.add(mg);
		}
	}

	public MenuGroup getMenuGroup(String groupName) {
		for (MenuGroup mg : menuGroups) {
			if (mg.getGroupName().equals(groupName))
				return mg;
		}
		return null;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
}