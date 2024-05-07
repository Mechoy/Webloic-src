package weblogic.wsee.wstx.wsat.common.client;

import weblogic.wsee.wstx.wsat.common.ParticipantIF;
import weblogic.wsee.wstx.wsat.common.WSATVersion;

public abstract class ParticipantProxyBuilder<T> extends BaseProxyBuilder<T, ParticipantProxyBuilder<T>> {
   protected ParticipantProxyBuilder(WSATVersion<T> var1) {
      super(var1);
   }

   protected String getDefaultCallbackAddress() {
      return this.version.getWSATHelper().getCoordinatorAddress();
   }

   public abstract ParticipantIF<T> build();
}
