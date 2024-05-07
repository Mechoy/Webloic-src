package weblogic.wsee.wstx.wsc.v11;

import weblogic.wsee.wstx.wsat.WSATHelper;
import weblogic.wsee.wstx.wsc.common.WSATCoordinationContextBuilder;

public class WSATCoordinationContextBuilderImpl extends WSATCoordinationContextBuilder {
   protected String getCoordinationType() {
      return "http://docs.oasis-open.org/ws-tx/wsat/2006/06";
   }

   protected String getDefaultRegistrationCoordinatorAddress() {
      return WSATHelper.V11.getRegistrationCoordinatorAddress();
   }

   protected CoordinationContextBuilderImpl newCoordinationContextBuilder() {
      CoordinationContextBuilderImpl var1 = new CoordinationContextBuilderImpl();
      return var1;
   }
}
