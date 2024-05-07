package weblogic.management.internal;

import java.io.Serializable;

public class ServerInfo implements Serializable {
   private static final long serialVersionUID = 3990262384160459292L;
   private String serverName = null;
   private String adminURL = null;

   public ServerInfo(String var1, String var2) {
      this.serverName = var1;
      this.adminURL = var2;
   }

   public String getServerName() {
      return this.serverName;
   }

   public String getAdministrationURL() {
      return this.adminURL;
   }

   public void setServerName(String var1) {
      this.serverName = var1;
   }

   public void setAdministrationURL(String var1) {
      this.adminURL = var1;
   }

   public String getT3URL() {
      return this.getAdministrationURL();
   }
}
