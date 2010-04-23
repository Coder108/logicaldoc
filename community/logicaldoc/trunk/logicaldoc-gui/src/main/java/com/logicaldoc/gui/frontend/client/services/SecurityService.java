package com.logicaldoc.gui.frontend.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.logicaldoc.gui.common.client.beans.GUIRight;
import com.logicaldoc.gui.common.client.beans.GUISession;

/**
 * The client side stub for the Security Service. This service gives all needed
 * methods to handle user sessions.
 */
@RemoteServiceRelativePath("security")
public interface SecurityService extends RemoteService {
	/**
	 * Logs-in a user
	 * 
	 * @param username
	 * @param password
	 * @return The newly created session
	 */
	public GUISession login(String username, String password);

	/**
	 * Changes the password of a user
	 * 
	 * @param userId The user Identifier
	 * @param oldPassword
	 * @param newPassword
	 * @return 0 if all is ok, 1 if the password is incorrect, otherwise a
	 *         positive number grater than 1
	 */
	public int changePassword(long userId, String oldPassword, String newPassword);

	/**
	 * Logs out the current user
	 */
	public void logout(String sid);

	/**
	 * Retrieves the list of all security entities that are ordinary groups and
	 * user's groups.
	 */
	public GUIRight[] getSecurityEntities(String sid);
}
