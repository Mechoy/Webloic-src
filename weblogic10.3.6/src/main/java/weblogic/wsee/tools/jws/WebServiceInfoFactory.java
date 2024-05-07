package weblogic.wsee.tools.jws;

import java.io.File;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.decl.WebServiceDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCWebServiceInfo;

public class WebServiceInfoFactory {
   public static WebServiceInfo newInstance(ModuleInfo var0, WebServiceDecl var1, File[] var2) throws WsBuildException {
      return (WebServiceInfo)(var1.getType() == WebServiceType.JAXRPC ? new JAXRPCWebServiceInfo(var0, (WebServiceSEIDecl)var1, var2) : new WebServiceInfo(var1));
   }
}
