package weblogic.connector.outbound;

import javax.resource.spi.ConnectionRequestInfo;
import weblogic.common.resourcepool.PooledResourceInfo;
import weblogic.connector.security.outbound.SecurityContext;

public class ConnectionReqInfo implements PooledResourceInfo {
   private ConnectionRequestInfo connRequestInfo;
   private SecurityContext secCtx;
   private boolean shareable;

   public ConnectionReqInfo(ConnectionRequestInfo var1, SecurityContext var2) {
      this.connRequestInfo = var1;
      this.secCtx = var2;
   }

   public boolean equals(PooledResourceInfo var1) {
      return this == var1;
   }

   public void setInfo(ConnectionRequestInfo var1) {
      this.connRequestInfo = var1;
   }

   void setShareable(boolean var1) {
      this.shareable = var1;
   }

   public ConnectionRequestInfo getInfo() {
      return this.connRequestInfo;
   }

   public SecurityContext getSecurityContext() {
      return this.secCtx;
   }

   boolean isShareable() {
      return this.shareable;
   }
}
