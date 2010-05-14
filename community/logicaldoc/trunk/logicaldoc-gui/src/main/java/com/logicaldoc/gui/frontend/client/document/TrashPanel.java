package com.logicaldoc.gui.frontend.client.document;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.logicaldoc.gui.common.client.I18N;
import com.logicaldoc.gui.common.client.Session;
import com.logicaldoc.gui.common.client.data.GarbageDS;
import com.logicaldoc.gui.common.client.formatters.DateCellFormatter;
import com.logicaldoc.gui.common.client.log.Log;
import com.logicaldoc.gui.common.client.util.Util;
import com.logicaldoc.gui.frontend.client.services.DocumentService;
import com.logicaldoc.gui.frontend.client.services.DocumentServiceAsync;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellContextClickEvent;
import com.smartgwt.client.widgets.grid.events.CellContextClickHandler;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * This panel shows the current user's garbage
 * 
 * @author Marco Meschieri - Logical Objects
 * @since 6.0
 */
public class TrashPanel extends VLayout {
	private DocumentServiceAsync service = (DocumentServiceAsync) GWT.create(DocumentService.class);

	private ListGrid list;

	private static TrashPanel instance;

	public static TrashPanel get() {
		if (instance == null)
			instance = new TrashPanel();
		return instance;
	}

	private TrashPanel() {
		setMembersMargin(3);

		ListGridField id = new ListGridField("id");
		id.setHidden(true);

		ListGridField title = new ListGridField("title", I18N.getMessage("title"), 150);
		title.setCanFilter(true);

		ListGridField icon = new ListGridField("icon", " ", 24);
		icon.setType(ListGridFieldType.IMAGE);
		icon.setCanSort(false);
		icon.setAlign(Alignment.CENTER);
		icon.setShowDefaultContextMenu(false);
		icon.setImageURLPrefix(Util.imagePrefix() + "/application/");
		icon.setImageURLSuffix(".png");
		icon.setCanFilter(false);

		ListGridField lastModified = new ListGridField("lastModified", I18N.getMessage("lastmodified"), 110);
		lastModified.setAlign(Alignment.CENTER);
		lastModified.setType(ListGridFieldType.DATE);
		lastModified.setCellFormatter(new DateCellFormatter());
		lastModified.setCanFilter(false);

		ListGridField customId = new ListGridField("customId", I18N.getMessage("customid"), 110);
		customId.setType(ListGridFieldType.TEXT);
		customId.setCanFilter(true);

		list = new ListGrid();
		list.setWidth100();
		list.setHeight100();
		list.setAutoFetchData(true);
		list.setFields(icon, title, customId, lastModified);
		list.setSelectionType(SelectionStyle.SINGLE);
		list.setDataSource(GarbageDS.get());
		list.setShowFilterEditor(true);
		list.setFilterOnKeypress(true); 
		addMember(list);

		list.addCellDoubleClickHandler(new CellDoubleClickHandler() {
			@Override
			public void onCellDoubleClick(CellDoubleClickEvent event) {
				ListGridRecord record = event.getRecord();
				restore(Long.parseLong(record.getAttributeAsString("id")), Long.parseLong(record
						.getAttributeAsString("folderId")));
			}
		});

		list.addCellContextClickHandler(new CellContextClickHandler() {
			@Override
			public void onCellContextClick(CellContextClickEvent event) {
				showContextMenu();
				event.cancel();
			}
		});
	}

	private void restore(final long docId, final long folderId) {
		service.restore(Session.get().getSid(), docId, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.serverError(caught);
			}

			@Override
			public void onSuccess(Void ret) {
				list.removeSelectedData();
				Log.info(I18N.getMessage("documentrestored"), I18N.getMessage("documentrestoreddetail", Long
						.toString(docId)));

				// If the case force a refresh
				if (Session.get().getCurrentFolder() != null && Session.get().getCurrentFolder().getId() == folderId)
					Session.get().setCurrentFolder(Session.get().getCurrentFolder());
			}
		});
	}

	private void showContextMenu() {
		Menu contextMenu = new Menu();

		MenuItem execute = new MenuItem();
		execute.setTitle(I18N.getMessage("restore"));
		execute.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			public void onClick(MenuItemClickEvent event) {
				ListGridRecord record = list.getSelectedRecord();
				restore(Long.parseLong(record.getAttribute("id")), Long.parseLong(record
						.getAttributeAsString("folderId")));
			}
		});

		contextMenu.setItems(execute);
		contextMenu.showContextMenu();
	}

	public void appendRecord(ListGridRecord record) {
		record.setAttribute("folderId", Long.toString(Session.get().getCurrentFolder().getId()));
		list.addData(record);
	}
}