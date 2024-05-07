package weblogic.deploy.internal.targetserver.state;

import java.io.Serializable;

public final class AppTransition implements Serializable {
   private static final long serialVersionUID = 1L;
   private final String xition;
   private final long gentime;
   private final String serverName;

   AppTransition(String var1, long var2, String var4) {
      this.xition = var1;
      this.gentime = var2;
      this.serverName = var4;
   }

   public String getXition() {
      return this.xition;
   }

   public String getServerName() {
      return this.serverName;
   }

   public long getGentime() {
      return this.gentime;
   }
}
