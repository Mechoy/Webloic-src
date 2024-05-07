package weblogic.wsee.tools.jws.jaxrpc;

import weblogic.wsee.WebServiceType;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.JWSProcessor;
import weblogic.wsee.tools.jws.ModuleInfo;
import weblogic.wsee.tools.jws.WebServiceInfo;

public abstract class JAXRPCProcessor implements JWSProcessor {
   protected ModuleInfo moduleInfo = null;

   public void init(ModuleInfo var1) throws WsBuildException {
      this.moduleInfo = var1;
   }

   public void finish() throws WsBuildException {
   }

   public final void process(WebServiceInfo var1) throws WsBuildException {
      if (var1.getWebService().getType() == WebServiceType.JAXRPC) {
         this.processImpl((JAXRPCWebServiceInfo)var1);
      }
   }

   protected abstract void processImpl(JAXRPCWebServiceInfo var1) throws WsBuildException;
}
