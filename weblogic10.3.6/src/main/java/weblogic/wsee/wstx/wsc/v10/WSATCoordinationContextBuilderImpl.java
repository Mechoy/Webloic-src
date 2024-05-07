package weblogic.wsee.wstx.wsc.v10;

import weblogic.wsee.wstx.wsat.WSATHelper;
import weblogic.wsee.wstx.wsc.common.WSATCoordinationContextBuilder;

public class WSATCoordinationContextBuilderImpl extends WSATCoordinationContextBuilder {
   protected CoordinationContextBuilderImpl newCoordinationContextBuilder() {
      return new CoordinationContextBuilderImpl();
   }

   protected String getCoordinationType() {
      return "http://schemas.xmlsoap.org/ws/2004/10/wsat";
   }

   protected String getDefaultRegistrationCoordinatorAddress() {
      return WSATHelper.V10.getRegistrationCoordinatorAddress();
   }
}
