package weblogic.wsee.wstx.wsat.common.client;

import weblogic.wsee.wstx.wsat.common.CoordinatorIF;
import weblogic.wsee.wstx.wsat.common.WSATVersion;

public abstract class CoordinatorProxyBuilder<T> extends BaseProxyBuilder<T, CoordinatorProxyBuilder<T>> {
   protected CoordinatorProxyBuilder(WSATVersion<T> var1) {
      super(var1);
   }

   public abstract CoordinatorIF<T> build();

   protected String getDefaultCallbackAddress() {
      return this.version.getWSATHelper().getParticipantAddress();
   }
}
