package weblogic.wsee.tools.wsdlc;

import weblogic.wsee.WebServiceType;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.wsdlc.jaxrpc.Wsdl2JwsBuilderImpl;

public class Wsdl2JwsBuilderFactory {
   private Wsdl2JwsBuilderFactory() {
   }

   public static Wsdl2JwsBuilder newInstance(WebServiceType var0) throws WsBuildException {
      if (var0 == WebServiceType.JAXRPC) {
         return new Wsdl2JwsBuilderImpl();
      } else if (var0 == WebServiceType.JAXWS) {
         return new weblogic.wsee.tools.wsdlc.jaxws.Wsdl2JwsBuilderImpl();
      } else {
         throw new WsBuildException(var0 + " web service type unknown");
      }
   }
}
