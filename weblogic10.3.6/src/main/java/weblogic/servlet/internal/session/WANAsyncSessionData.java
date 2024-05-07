package weblogic.servlet.internal.session;

import java.io.Serializable;
import weblogic.cluster.replication.AsyncReplicatable;
import weblogic.cluster.replication.AsyncReplicationManager;

public class WANAsyncSessionData extends WANSessionData implements AsyncReplicatable {
   private static final long serialVersionUID = 1539991387938295130L;
   private transient ReplicatedSessionChange asyncChange;
   private boolean blockingFlushCall;

   public WANAsyncSessionData() {
      this.blockingFlushCall = false;
   }

   public WANAsyncSessionData(String var1, SessionContext var2) {
      this(var1, var2, true);
   }

   protected WANAsyncSessionData(String var1, SessionContext var2, boolean var3) {
      super(var1, var2, var3);
      this.blockingFlushCall = false;
   }

   protected void initializeChange() {
      this.asyncChange = new AsyncReplicatedSessionChange();
   }

   protected ReplicatedSessionChange getSessionChange() {
      return this.asyncChange;
   }

   void syncSession() {
      super.syncSession();
      if (this.blockingFlushCall) {
         ((AsyncReplicationManager)this.getReplicationServices()).blockingFlush();
         this.blockingFlushCall = false;
      }

   }

   public void removeInternalAttribute(String var1) throws IllegalStateException {
      super.removeInternalAttribute(var1);
      if (var1.equals("weblogic.authuser")) {
         this.blockingFlushCall = true;
      }

   }

   protected Serializable getUpdateObject() {
      return this;
   }

   public void setQueued() {
      ((AsyncReplicatedSessionChange)this.asyncChange).setQueued();
   }

   public boolean isQueued() {
      return ((AsyncReplicatedSessionChange)this.asyncChange).isQueued();
   }

   public Serializable getBatchedChanges() {
      return this.asyncChange;
   }

   public void commit() {
      synchronized(this.asyncChange) {
         ((AsyncReplicatedSessionChange)this.asyncChange).commit();
      }
   }
}
