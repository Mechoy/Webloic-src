package weblogic.diagnostics.descriptor;

public interface WLDFWatchNotificationBean extends WLDFBean {
   boolean isEnabled();

   void setEnabled(boolean var1);

   String getSeverity();

   void setSeverity(String var1);

   String getLogWatchSeverity();

   void setLogWatchSeverity(String var1);

   WLDFWatchBean[] getWatches();

   WLDFWatchBean createWatch(String var1);

   void destroyWatch(WLDFWatchBean var1);

   WLDFNotificationBean[] getNotifications();

   WLDFNotificationBean lookupNotification(String var1);

   WLDFImageNotificationBean[] getImageNotifications();

   WLDFImageNotificationBean createImageNotification(String var1);

   void destroyImageNotification(WLDFImageNotificationBean var1);

   WLDFImageNotificationBean lookupImageNotification(String var1);

   WLDFJMSNotificationBean[] getJMSNotifications();

   WLDFJMSNotificationBean createJMSNotification(String var1);

   void destroyJMSNotification(WLDFJMSNotificationBean var1);

   WLDFJMSNotificationBean lookupJMSNotification(String var1);

   WLDFJMXNotificationBean[] getJMXNotifications();

   WLDFJMXNotificationBean createJMXNotification(String var1);

   void destroyJMXNotification(WLDFJMXNotificationBean var1);

   WLDFJMXNotificationBean lookupJMXNotification(String var1);

   WLDFSMTPNotificationBean[] getSMTPNotifications();

   WLDFSMTPNotificationBean createSMTPNotification(String var1);

   void destroySMTPNotification(WLDFSMTPNotificationBean var1);

   WLDFSMTPNotificationBean lookupSMTPNotification(String var1);

   WLDFSNMPNotificationBean[] getSNMPNotifications();

   WLDFSNMPNotificationBean createSNMPNotification(String var1);

   void destroySNMPNotification(WLDFSNMPNotificationBean var1);

   WLDFSNMPNotificationBean lookupSNMPNotification(String var1);
}
