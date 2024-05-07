package weblogic.wsee.wsdl.mime;

import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlExtensionParser;
import weblogic.wsee.wsdl.WsdlExtensionRegistry;
import weblogic.wsee.wsdl.WsdlFilter;
import weblogic.wsee.wsdl.WsdlWriter;
import weblogic.wsee.wsdl.internal.WsdlExtensibleImpl;

public class MimePart extends WsdlExtensibleImpl {
   public void parse(Element var1) throws WsdlException {
      super.parse(var1, (String)null);
   }

   protected WsdlExtension parseChild(Element var1, String var2) throws WsdlException {
      WsdlExtensionParser var3 = WsdlExtensionRegistry.getParser();
      WsdlExtension var4 = var3.parseBindingMessage((WsdlBindingMessage)null, var1);
      return var4;
   }

   protected void parseAttributes(Element var1, String var2) throws WsdlException {
   }

   public void write(Element var1, WsdlWriter var2) {
      boolean var3 = true;
      WsdlFilter var4 = var2.getWsdlAddressInfo() == null ? null : var2.getWsdlAddressInfo().getWsdlFilter();
      if (var4 != null) {
         MimeContent var5 = (MimeContent)this.getExtension("mime-content");
         if (var5 != null) {
            Element var6 = (Element)var1.getParentNode();
            QName var7 = new QName(var6.getAttribute("name"));
            var3 = var4.isMessagePartSupported(var7, var5.getPart());
         }
      }

      if (var3) {
         Element var8 = var2.addChild(var1, "part", "http://schemas.xmlsoap.org/wsdl/mime/");
         this.writeExtensions(var8, var2);
      }

   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.end();
   }
}
