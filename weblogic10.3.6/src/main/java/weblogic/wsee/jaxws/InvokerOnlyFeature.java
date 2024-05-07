package weblogic.wsee.jaxws;

import javax.xml.ws.WebServiceFeature;

public class InvokerOnlyFeature extends WebServiceFeature {
   public String getID() {
      return InvokerOnlyFeature.class.getName();
   }
}
