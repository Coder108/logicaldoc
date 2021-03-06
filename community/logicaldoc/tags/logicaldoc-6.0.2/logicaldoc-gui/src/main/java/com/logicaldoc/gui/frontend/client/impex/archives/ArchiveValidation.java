package com.logicaldoc.gui.frontend.client.impex.archives;

import com.logicaldoc.gui.common.client.beans.GUISostConfig;
import com.logicaldoc.gui.common.client.i18n.I18N;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * This popup window is used to start an archive validation.
 * 
 * @author Matteo Caruso - Logical Objects
 * @since 6.0
 */
public class ArchiveValidation extends Window {

	private VLayout layout = null;

	private TabSet tabs = new TabSet();

	private int currentTabIndex = 0;
	
	private ExportArchivesList archivesList = null;
	
	private int configsNum = 0;

	public ArchiveValidation(ExportArchivesList list, GUISostConfig[] configs, long archiveId) {
		this.archivesList = list;
		this.configsNum = configs.length;

		setHeaderControls(HeaderControls.HEADER_LABEL, HeaderControls.CLOSE_BUTTON);

		setTitle(I18N.message("archivevalidation"));
		setWidth(650);
		setHeight(400);
		setCanDragResize(true);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		layout = new VLayout(10);
		layout.setMargin(30);

		tabs = new TabSet();
		tabs.setWidth(600);
		tabs.setHeight(350);

		Tab tab = null;
		int i = 0;
		for (GUISostConfig guiSostConfig : configs) {
			tab = new ValidationTab(this, i, guiSostConfig, archiveId);
			tabs.addTab(tab);
			i++;
		}
		tabs.selectTab(currentTabIndex);

		layout.addMember(tabs);
		layout.redraw();

		addChild(layout);
	}

	public TabSet getTabs() {
		return tabs;
	}

	public int getCurrentTabIndex() {
		return currentTabIndex;
	}

	public void setCurrentTabIndex(int currentTabIndex) {
		this.currentTabIndex = currentTabIndex;
	}

	public ExportArchivesList getArchivesList() {
		return archivesList;
	}

	public int getConfigsNum() {
		return configsNum;
	}
}
