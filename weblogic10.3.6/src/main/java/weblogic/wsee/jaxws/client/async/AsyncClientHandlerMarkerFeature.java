package weblogic.wsee.jaxws.client.async;

import javax.xml.ws.WebServiceFeature;

public class AsyncClientHandlerMarkerFeature extends WebServiceFeature {
   public AsyncClientHandlerMarkerFeature() {
      this.enabled = true;
   }

   public String getID() {
      return "AsyncClientHandlerMarkerFeature";
   }
}
