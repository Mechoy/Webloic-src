package weblogic.management.mbeanservers.edit.internal;

import java.io.Serializable;
import weblogic.management.mbeanservers.edit.ServerStatus;

public class ServerStatusImpl implements Serializable, ServerStatus {
   private String serverName;
   private int serverState;
   private Exception serverException;

   public ServerStatusImpl(String var1, int var2, Exception var3) {
      this.serverName = var1;
      this.serverState = var2;
      this.serverException = var3;
   }

   public String getServerName() {
      return this.serverName;
   }

   public int getServerState() {
      return this.serverState;
   }

   public Exception getServerException() {
      return this.serverException;
   }
}
