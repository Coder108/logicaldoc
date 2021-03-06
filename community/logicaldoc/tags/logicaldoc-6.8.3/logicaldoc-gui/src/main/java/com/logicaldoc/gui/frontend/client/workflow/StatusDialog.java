package com.logicaldoc.gui.frontend.client.workflow;

import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.logicaldoc.gui.common.client.i18n.I18N;
import com.logicaldoc.gui.common.client.util.ItemFactory;
import com.logicaldoc.gui.frontend.client.services.FolderService;
import com.logicaldoc.gui.frontend.client.services.FolderServiceAsync;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

/**
 * This is the form used for the workflow joins and forks.
 * 
 * @author Marco Meschieri - Logical Objects
 * @since 6.4
 */
public class StatusDialog extends Window {
	protected FolderServiceAsync folderService = (FolderServiceAsync) GWT.create(FolderService.class);

	private ValuesManager vm = new ValuesManager();

	private DynamicForm form;

	private StateWidget widget;

	public StatusDialog(StateWidget widget) {
		this.widget = widget;

		setHeaderControls(HeaderControls.HEADER_LABEL, HeaderControls.CLOSE_BUTTON);
		setTitle(I18N.message("editworkflowstate", I18N.message("task")));
		setWidth(250);
		setAutoHeight();
		setCanDragResize(true);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();
		setPadding(5);
		setMargin(3);
		setAutoSize(true);

		form = new DynamicForm();
		form.setTitleOrientation(TitleOrientation.TOP);
		form.setNumCols(1);
		form.setValuesManager(vm);

		TextItem name = ItemFactory.newTextItem("name", "name", widget.getTransition().getText());
		name.setRequired(true);

		ButtonItem save = new ButtonItem("save", I18N.message("save"));
		save.setAutoFit(true);
		save.addClickHandler(new ClickHandler() {
			@SuppressWarnings("unchecked")
			public void onClick(ClickEvent event) {
				Map<String, Object> values = (Map<String, Object>) vm.getValues();

				if (vm.validate()) {
					if (vm.validate()) {
						StatusDialog.this.widget.getWfState().setName((String) values.get("name"));

						StatusDialog.this.widget.setContents("<b>" + (String) values.get("name") + "</b>");
						StatusDialog.this.widget.getDrawingPanel().getDiagramController().update();

						destroy();
					}
				}
			}
		});
		
		form.setItems(name, save);
		addItem(form);
	}
}