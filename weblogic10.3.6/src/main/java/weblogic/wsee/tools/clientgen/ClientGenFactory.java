package weblogic.wsee.tools.clientgen;

import weblogic.wsee.WebServiceType;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.clientgen.jaxrpc.ClientGenImpl;

public class ClientGenFactory {
   private ClientGenFactory() {
   }

   public static ClientGen newInstance(WebServiceType var0) throws WsBuildException {
      if (var0 == WebServiceType.JAXRPC) {
         return new ClientGenImpl();
      } else if (var0 == WebServiceType.JAXWS) {
         return new weblogic.wsee.tools.clientgen.jaxws.ClientGenImpl();
      } else {
         throw new WsBuildException("Unknown web service type " + var0);
      }
   }
}
