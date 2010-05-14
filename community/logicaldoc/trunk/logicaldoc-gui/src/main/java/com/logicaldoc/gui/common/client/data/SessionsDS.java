package com.logicaldoc.gui.common.client.data;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceTextField;

/**
 * Datasource to retrieve a list of sessions. It is based on Xml parsing.
 * 
 * @author Marco Meschieri - Logical Objects
 * @since 6.0
 */
public class SessionsDS extends DataSource {
	private static SessionsDS instance;

	private SessionsDS() {
		setTitleField("sid");
		setRecordXPath("/list/session");

		DataSourceTextField sid = new DataSourceTextField("sid");
		sid.setPrimaryKey(true);

		DataSourceTextField status = new DataSourceTextField("status");
		DataSourceTextField statusLabel = new DataSourceTextField("statusLabel");
		DataSourceTextField username = new DataSourceTextField("username");
		DataSourceDateField created = new DataSourceDateField("created");
		DataSourceDateField renew = new DataSourceDateField("renew");

		setFields(sid, status, statusLabel, username, created, renew);
		setDataURL("data/sessions.xml");
		setClientOnly(true);
	}

	public static SessionsDS get() {
		if (instance == null)
			instance = new SessionsDS();
		return instance;
	}
}