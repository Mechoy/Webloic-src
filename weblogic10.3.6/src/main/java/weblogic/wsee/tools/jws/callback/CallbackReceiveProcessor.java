package weblogic.wsee.tools.jws.callback;

import java.util.Iterator;
import javax.xml.namespace.QName;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCProcessor;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCWebServiceInfo;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlPartnerLinkType;
import weblogic.wsee.wsdl.WsdlPortType;

public class CallbackReceiveProcessor extends JAXRPCProcessor {
   private JAXRPCWebServiceInfo webServiceInfo;

   protected void processImpl(JAXRPCWebServiceInfo var1) throws WsBuildException {
      if (var1.getWebService().getCallbackService() != null) {
         this.webServiceInfo = var1;

         try {
            this.addPartnerLink(var1.getWebService().getCallbackService().getName());
         } catch (WsdlException var3) {
            throw new WsBuildException(var3.getMessage(), var3);
         }
      }
   }

   private WsdlPartnerLinkType addPartnerLink(String var1) throws WsdlException {
      WsdlDefinitions var2 = this.webServiceInfo.getDefinitions();
      WsdlPartnerLinkType var3 = new WsdlPartnerLinkType(new QName(var2.getTargetNamespace(), var1));
      Iterator var4 = var2.getPortTypes().values().iterator();
      if (var4.hasNext()) {
         WsdlPortType var5 = (WsdlPortType)var4.next();
         var3.addRole("Service", var5.getName());
         if (var4.hasNext()) {
            var5 = (WsdlPortType)var4.next();
            var3.addRole("Callback", var5.getName());
            if (var4.hasNext()) {
               throw new IllegalStateException("Failed to create a partnerLinkType. There are more than two portTypes in wsdl.");
            } else {
               var3.attach(var2);
               return var3;
            }
         } else {
            throw new IllegalStateException("There is only one portType in wsdl partnerLinkType needs two portType.");
         }
      } else {
         throw new IllegalStateException("There is no portType in wsdl");
      }
   }
}
