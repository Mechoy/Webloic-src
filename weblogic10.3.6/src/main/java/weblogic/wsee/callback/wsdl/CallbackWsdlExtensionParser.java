package weblogic.wsee.callback.wsdl;

import java.util.HashSet;
import java.util.Set;
import org.w3c.dom.Element;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlExtensionParser;
import weblogic.wsee.wsdl.WsdlMessage;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPartnerLinkType;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlReader;
import weblogic.wsee.wsdl.WsdlService;

public class CallbackWsdlExtensionParser implements WsdlExtensionParser {
   private static final Set<String> CALLBACK_ROLE_NAMES = new HashSet();

   public WsdlExtension parseMessage(WsdlMessage var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseOperation(WsdlOperation var1, Element var2) {
      return null;
   }

   public WsdlExtension parseBinding(WsdlBinding var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseBindingOperation(WsdlBindingOperation var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseBindingMessage(WsdlBindingMessage var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseService(WsdlService var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parsePort(WsdlPort var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseDefinitions(WsdlDefinitions var1, Element var2) throws WsdlException {
      if (WsdlReader.tagEquals(var2, WsdlPartnerLinkType.PLINK.getLocalPart(), WsdlPartnerLinkType.PLINK.getNamespaceURI())) {
         WsdlPartnerLinkType var3 = new WsdlPartnerLinkType();
         var3.parse(var2, var1.getTargetNamespace());
         if (isCallbackPartnerLinkType(var3)) {
            return var3;
         }
      }

      return null;
   }

   public void cleanUp() {
   }

   private static boolean isCallbackPartnerLinkType(WsdlPartnerLinkType var0) {
      HashSet var1 = new HashSet();
      var1.add(var0.getRole1Name());
      var1.add(var0.getRole2Name());
      return CALLBACK_ROLE_NAMES.equals(var1);
   }

   static {
      CALLBACK_ROLE_NAMES.add("Service");
      CALLBACK_ROLE_NAMES.add("Callback");
   }
}
