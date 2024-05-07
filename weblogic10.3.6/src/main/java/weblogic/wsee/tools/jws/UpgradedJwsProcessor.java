package weblogic.wsee.tools.jws;

import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCProcessor;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCWebServiceInfo;
import weblogic.wsee.wsdl.WsdlService;

public class UpgradedJwsProcessor extends JAXRPCProcessor {
   protected void processImpl(JAXRPCWebServiceInfo var1) throws WsBuildException {
      if (var1.getWebService().isWlw81UpgradedService()) {
         WsdlService var2 = (WsdlService)var1.getDefinitions().getServices().get(var1.getWebService().getServiceQName());
         if (var2 == null) {
            throw new WsBuildException("Wsdl service " + var1.getWebService().getServiceQName() + " not found");
         } else {
            UpgradedJwsWsdlWriter.attach(var2);
         }
      }
   }
}
