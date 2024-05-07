package weblogic.wsee.tools.jws.conversation;

import java.util.Iterator;
import java.util.Locale;
import javax.xml.namespace.QName;
import weblogic.wsee.conversation.ConversationPhase;
import weblogic.wsee.conversation.wsdl.ConversationWsdlPhase;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCProcessor;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCWebServiceInfo;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlService;

public class ConversationProcessor extends JAXRPCProcessor {
   protected void processImpl(JAXRPCWebServiceInfo var1) throws WsBuildException {
      if (var1.getWebService().isConversational()) {
         WsdlService var2 = (WsdlService)var1.getDefinitions().getServices().get(var1.getWebService().getServiceQName());
         if (var2 == null) {
            throw new WsBuildException("Wsdl service " + var1.getWebService().getServiceQName() + " not found");
         } else {
            Iterator var3 = var2.getPorts().values().iterator();

            while(var3.hasNext()) {
               WsdlPort var4 = (WsdlPort)var3.next();
               Iterator var5 = var1.getWebService().getWebMethods();

               while(var5.hasNext()) {
                  WebMethodDecl var6 = (WebMethodDecl)var5.next();
                  String var7 = var4.getBinding().getPortType().getName().getNamespaceURI();
                  WsdlBindingOperation var8 = (WsdlBindingOperation)var4.getBinding().getOperations().get(new QName(var7, var6.getName()));
                  ConversationPhase var9 = ConversationPhase.valueOf(var6.getConverationPhase().toString().toLowerCase(Locale.ENGLISH));
                  ConversationWsdlPhase.attach(var8, var9);
               }
            }

         }
      }
   }
}
