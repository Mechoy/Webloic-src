package weblogic.wsee.wsdl;

import org.w3c.dom.Element;

public class UnknownExtensionParser implements WsdlExtensionParser {
   public WsdlExtension parseMessage(WsdlMessage var1, Element var2) throws WsdlException {
      return this.createUnknownExtension(var2);
   }

   public WsdlExtension parseOperation(WsdlOperation var1, Element var2) {
      return this.createUnknownExtension(var2);
   }

   public WsdlExtension parseBinding(WsdlBinding var1, Element var2) throws WsdlException {
      return this.createUnknownExtension(var2);
   }

   private WsdlExtension createUnknownExtension(Element var1) {
      UnknownExtension var2 = new UnknownExtension();
      var2.parse(var1);
      return var2;
   }

   public WsdlExtension parseBindingOperation(WsdlBindingOperation var1, Element var2) throws WsdlException {
      return this.createUnknownExtension(var2);
   }

   public WsdlExtension parseBindingMessage(WsdlBindingMessage var1, Element var2) {
      return this.createUnknownExtension(var2);
   }

   public WsdlExtension parseService(WsdlService var1, Element var2) throws WsdlException {
      return this.createUnknownExtension(var2);
   }

   public WsdlExtension parsePort(WsdlPort var1, Element var2) throws WsdlException {
      return this.createUnknownExtension(var2);
   }

   public WsdlExtension parseDefinitions(WsdlDefinitions var1, Element var2) throws WsdlException {
      return this.createUnknownExtension(var2);
   }

   public void cleanUp() {
   }
}
