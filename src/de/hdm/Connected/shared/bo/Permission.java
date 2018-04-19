package de.hdm.Connected.shared.bo;


/**
 * 
 * @author Moritz
 *
 */
public class Permission extends BusinessObject{


	private static final long serialVersionUID = 1L;
	private int receiverUserID = 0;
	private int businessObjectID = 0;
	private int shareUserID = 0;
	/**
	 * @return the userID
	 */
	public int getReceiverUserID() {
		return receiverUserID;
	}
	/**
	 * @param userID the userID to set
	 */
	public void setReceiverUserID(int receiverUserID) {
		this.receiverUserID = receiverUserID;
	}
	/**
	 * @return the businessObjectID
	 */
	public int getBusinessObjectID() {
		return businessObjectID;
	}
	/**
	 * @param businessObjectID the businessObjectID to set
	 */
	public void setBusinessObjectID(int businessObjectID) {
		this.businessObjectID = businessObjectID;
	}
	/**
	 * @return the shareUserID
	 */
	public int getShareUserID() {
		return shareUserID;
	}
	/**
	 * @param shareUserID the shareUserID to set
	 */
	public void setShareUserID(int shareUserID) {
		this.shareUserID = shareUserID;
	}
	

}
