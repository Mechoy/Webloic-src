package weblogic.servlet.cluster;

import java.io.Serializable;

public final class SessionState {
   private final Serializable state;
   private final int maxInactiveInterval;
   private final long sessionCreationTime;
   private final long accessTime;

   public SessionState(Serializable var1, int var2, long var3, long var5) {
      this.state = var1;
      this.maxInactiveInterval = var2;
      this.sessionCreationTime = var3;
      this.accessTime = var5;
   }

   public Serializable getSessionState() {
      return this.state;
   }

   public int getMaxAnInactiveInterval() {
      return this.maxInactiveInterval;
   }

   public long getSessionCreationTime() {
      return this.sessionCreationTime;
   }

   public long getAccessTime() {
      return this.accessTime;
   }
}
