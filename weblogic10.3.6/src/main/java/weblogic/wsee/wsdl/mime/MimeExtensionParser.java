package weblogic.wsee.wsdl.mime;

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
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlReader;
import weblogic.wsee.wsdl.WsdlService;

public class MimeExtensionParser implements WsdlExtensionParser {
   public WsdlExtension parseMessage(WsdlMessage var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseOperation(WsdlOperation var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseBinding(WsdlBinding var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseBindingOperation(WsdlBindingOperation var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseBindingMessage(WsdlBindingMessage var1, Element var2) throws WsdlException {
      if (WsdlReader.tagEquals(var2, "multipartRelated", "http://schemas.xmlsoap.org/wsdl/mime/")) {
         MimeMultipartRelated var5 = new MimeMultipartRelated();
         var5.parse(var2);
         return var5;
      } else if (WsdlReader.tagEquals(var2, "content", "http://schemas.xmlsoap.org/wsdl/mime/")) {
         MimeContent var4 = new MimeContent();
         var4.parse(var2);
         return var4;
      } else if (WsdlReader.tagEquals(var2, "mimeXml", "http://schemas.xmlsoap.org/wsdl/mime/")) {
         MimeXml var3 = new MimeXml();
         var3.parse(var2);
         return var3;
      } else {
         return null;
      }
   }

   public WsdlExtension parseService(WsdlService var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parsePort(WsdlPort var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseDefinitions(WsdlDefinitions var1, Element var2) throws WsdlException {
      return null;
   }

   public void cleanUp() {
   }
}
