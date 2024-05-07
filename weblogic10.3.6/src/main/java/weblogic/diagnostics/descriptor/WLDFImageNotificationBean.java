package weblogic.diagnostics.descriptor;

public interface WLDFImageNotificationBean extends WLDFNotificationBean {
   String getImageDirectory();

   void setImageDirectory(String var1);

   int getImageLockout();

   void setImageLockout(int var1);
}
