package weblogic.servlet.internal.session;

import java.io.Serializable;

public final class SessionInfo implements Serializable {
   private String monitoringId;
   private long creationTime;
   private long modifiedTime;
   private int inactiveInterval;
   private boolean newSession;

   public SessionInfo(SessionData var1) {
      this.monitoringId = var1.getMonitoringId();
      this.creationTime = var1.getCreationTime();
      this.modifiedTime = var1.getLastAccessedTime();
      this.inactiveInterval = var1.getMaxInactiveInterval();
      this.newSession = var1.isNew();
   }

   public String getMonitoringId() {
      return this.monitoringId;
   }

   public long getCreationTime() {
      return this.creationTime;
   }

   public long getLastAccessedTime() {
      return this.modifiedTime;
   }

   public int getMaxInactiveInterval() {
      return this.inactiveInterval;
   }

   public boolean isNew() {
      return this.newSession;
   }

   public int hashCode() {
      return this.monitoringId.hashCode();
   }
}
