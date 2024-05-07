package weblogic.deploy.internal.targetserver.datamanagement;

import java.util.List;

public class ModuleRedeployDataUpdateRequestInfo implements DataUpdateRequestInfo {
   private final List moduleIds;
   private final long requestId;

   public ModuleRedeployDataUpdateRequestInfo(List var1, long var2) {
      this.moduleIds = var1;
      this.requestId = var2;
   }

   public List getDeltaFiles() {
      return this.moduleIds;
   }

   public long getRequestId() {
      return this.requestId;
   }

   public boolean isStatic() {
      return false;
   }

   public boolean isDelete() {
      return false;
   }

   public boolean isPlanUpdate() {
      return false;
   }
}
