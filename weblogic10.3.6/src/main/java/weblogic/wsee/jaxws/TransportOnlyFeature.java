package weblogic.wsee.jaxws;

import javax.xml.ws.WebServiceFeature;

public class TransportOnlyFeature extends WebServiceFeature {
   public String getID() {
      return TransportOnlyFeature.class.getName();
   }
}
