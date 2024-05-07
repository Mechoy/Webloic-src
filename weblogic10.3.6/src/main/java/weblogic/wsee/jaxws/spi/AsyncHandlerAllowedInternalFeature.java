package weblogic.wsee.jaxws.spi;

import weblogic.jws.jaxws.WLSWebServiceFeature;

public class AsyncHandlerAllowedInternalFeature extends WLSWebServiceFeature {
   public static final String ID = "AsyncHandlerAllowedInternalFeature";

   public AsyncHandlerAllowedInternalFeature() {
      super.enabled = true;
      this.setTubelineImpact(false);
   }

   public String getID() {
      return "AsyncHandlerAllowedInternalFeature";
   }
}
