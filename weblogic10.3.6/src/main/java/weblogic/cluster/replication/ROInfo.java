package weblogic.cluster.replication;

public final class ROInfo {
   private final ROID roid;
   private Object secondaryROInfo;

   ROInfo(ROID var1) {
      this.roid = var1;
   }

   public final Object getSecondaryROInfo() {
      return this.secondaryROInfo;
   }

   public final void setSecondaryROInfo(Object var1) {
      this.secondaryROInfo = var1;
   }

   public final ROID getROID() {
      return this.roid;
   }
}
