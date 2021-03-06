package com.logicaldoc.gui.frontend.client.document;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.logicaldoc.gui.common.client.Session;
import com.logicaldoc.gui.common.client.beans.GUIDocument;
import com.logicaldoc.gui.common.client.beans.GUIFolder;
import com.logicaldoc.gui.common.client.beans.GUIVersion;
import com.logicaldoc.gui.common.client.data.VersionsDS;
import com.logicaldoc.gui.common.client.formatters.DateCellFormatter;
import com.logicaldoc.gui.common.client.i18n.I18N;
import com.logicaldoc.gui.common.client.log.Log;
import com.logicaldoc.gui.common.client.widgets.PreviewPopup;
import com.logicaldoc.gui.frontend.client.services.DocumentService;
import com.logicaldoc.gui.frontend.client.services.DocumentServiceAsync;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellContextClickEvent;
import com.smartgwt.client.widgets.grid.events.CellContextClickHandler;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * This panel shows a list of versions of a document in a tabular way.
 * 
 * @author Marco Meschieri - Logical Objects
 * @since 6.0
 */
public class VersionsPanel extends DocumentDetailTab {

	private DocumentServiceAsync documentService = (DocumentServiceAsync) GWT.create(DocumentService.class);

	private VersionsDS dataSource;

	private ListGrid listGrid;

	private Menu contextMenu;

	public VersionsPanel(final GUIDocument document) {
		super(document, null);
		ListGridField id = new ListGridField("id");
		id.setHidden(true);

		ListGridField user = new ListGridField("user", I18N.message("user"), 100);
		ListGridField event = new ListGridField("event", I18N.message("event"), 200);
		ListGridField version = new ListGridField("version", I18N.message("version"), 70);
		ListGridField fileVersion = new ListGridField("fileVersion", I18N.message("fileversion"), 70);
		ListGridField date = new ListGridField("date", I18N.message("date"), 110);
		date.setAlign(Alignment.CENTER);
		date.setType(ListGridFieldType.DATE);
		date.setCellFormatter(new DateCellFormatter(false));
		date.setCanFilter(false);
		ListGridField comment = new ListGridField("comment", I18N.message("comment"));

		ListGridField type = new ListGridField("type", I18N.message("type"), 55);
		type.setType(ListGridFieldType.TEXT);
		type.setAlign(Alignment.CENTER);

		listGrid = new ListGrid();
		listGrid.setEmptyMessage(I18N.message("notitemstoshow"));
		listGrid.setCanFreezeFields(true);
		listGrid.setAutoFetchData(true);
		dataSource = new VersionsDS(document.getId(), null, 100);
		listGrid.setDataSource(dataSource);
		listGrid.setFields(user, event, type, fileVersion, version, date, comment);
		addMember(listGrid);

		setupContextMenu();

		listGrid.addCellDoubleClickHandler(new CellDoubleClickHandler() {
			@Override
			public void onCellDoubleClick(CellDoubleClickEvent event) {
				ListGridRecord record = event.getRecord();
				if (Session.get().getCurrentFolder().isDownload()
						&& "download".equals(Session.get().getInfo().getConfig("gui.doubleclick")))
					onDownload(document, record);
				else
					onPreview(document, record);
			}
		});

		listGrid.addCellContextClickHandler(new CellContextClickHandler() {
			@Override
			public void onCellContextClick(CellContextClickEvent event) {
				contextMenu.showContextMenu();
				event.cancel();
			}
		});
	}

	protected void onDownload(final GUIDocument document, ListGridRecord record) {
		if (document.getFolder().isDownload())
			Window.open(
					GWT.getHostPageBaseURL() + "download?sid=" + Session.get().getSid() + "&docId=" + document.getId()
							+ "&versionId=" + record.getAttribute("id") + "&open=true", "_blank", "");
	}

	protected void onPreview(final GUIDocument document, ListGridRecord record) {
		long id = document.getId();
		String filename = document.getFileName();
		String fileVersion = record.getAttribute("fileVersion");

		if (filename == null)
			filename = record.getAttribute("title") + "." + record.getAttribute("type");

		GUIFolder folder = document.getFolder();
		PreviewPopup iv = new PreviewPopup(id, fileVersion, filename, folder != null && folder.isDownload());
		iv.show();
	}

	/**
	 * Prepares the context menu.
	 */
	private void setupContextMenu() {
		contextMenu = new Menu();
		MenuItem compare = new MenuItem();
		compare.setTitle(I18N.message("compare"));
		compare.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			public void onClick(MenuItemClickEvent event) {
				// Detect the two selected records
				ListGridRecord[] selection = listGrid.getSelectedRecords();
				if (selection == null || selection.length != 2) {
					SC.warn(I18N.message("select2versions"));
					return;
				}

				documentService.getVersionsById(Session.get().getSid(),
						Long.parseLong(selection[0].getAttribute("id")),
						Long.parseLong(selection[1].getAttribute("id")), new AsyncCallback<GUIVersion[]>() {
							@Override
							public void onFailure(Throwable caught) {
								Log.serverError(caught);
							}

							@Override
							public void onSuccess(GUIVersion[] result) {
								VersionsDiff diffWinfow = new VersionsDiff(result[0], result[1]);
								diffWinfow.show();
							}
						});
			}
		});

		MenuItem download = new MenuItem();
		download.setTitle(I18N.message("download"));
		download.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			public void onClick(MenuItemClickEvent event) {
				// Detect the two selected records
				ListGridRecord[] selection = listGrid.getSelectedRecords();
				if (selection == null || selection.length < 1) {
					return;
				}
				onDownload(document, selection[0]);
			}
		});
		download.setEnabled(document.getFolder().isDownload());

		MenuItem preview = new MenuItem();
		preview.setTitle(I18N.message("preview"));
		preview.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			public void onClick(MenuItemClickEvent event) {
				// Detect the two selected records
				ListGridRecord[] selection = listGrid.getSelectedRecords();
				if (selection == null || selection.length < 1) {
					return;
				}
				onPreview(document, selection[0]);
			}
		});

		contextMenu.setItems(preview, download, compare);
	}

	@Override
	public void destroy() {
		super.destroy();
		if (dataSource != null)
			dataSource.destroy();
	}
}