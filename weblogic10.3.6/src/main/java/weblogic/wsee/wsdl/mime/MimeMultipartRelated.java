package weblogic.wsee.wsdl.mime;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlReader;
import weblogic.wsee.wsdl.WsdlWriter;

public class MimeMultipartRelated implements WsdlExtension {
   public static final String KEY = "SOAP11-mime-multipart-related";
   public List<MimePart> parts = new LinkedList();

   public List<MimePart> getParts() {
      return this.parts;
   }

   public String getKey() {
      return "SOAP11-mime-multipart-related";
   }

   public void write(Element var1, WsdlWriter var2) {
      Element var3 = var2.addChild(var1, "multipartRelated", "http://schemas.xmlsoap.org/wsdl/mime/");
      Iterator var4 = this.parts.iterator();

      while(var4.hasNext()) {
         MimePart var5 = (MimePart)var4.next();
         var5.write(var3, var2);
      }

   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeArray("part", this.parts.iterator());
      var1.end();
   }

   public static MimeMultipartRelated attach(WsdlBindingMessage var0) {
      MimeMultipartRelated var1 = new MimeMultipartRelated();
      var0.putExtension(var1);
      return var1;
   }

   public static MimeMultipartRelated narrow(WsdlBindingMessage var0) {
      return (MimeMultipartRelated)var0.getExtension("SOAP11-mime-multipart-related");
   }

   public void parse(Element var1) throws WsdlException {
      NodeList var2 = var1.getChildNodes();

      for(int var3 = 0; var3 < var2.getLength(); ++var3) {
         Node var4 = var2.item(var3);
         if (!WsdlReader.isWhiteSpace(var4)) {
            WsdlReader.checkDomElement(var4);
            if (!"part".equals(var4.getLocalName())) {
               throw new WsdlException("The XML document specified is not a valid WSDL document. The name of the child element of a multipartRelated element should be 'part' but found '" + var4.getLocalName() + "'");
            }

            String var5 = var4.getNamespaceURI();
            if (!"http://schemas.xmlsoap.org/wsdl/mime/".equals(var5)) {
               throw new WsdlException("Found an element with unexpected namespace '" + var5 + "' . Was expecting '" + "http://schemas.xmlsoap.org/wsdl/mime/" + "'");
            }

            MimePart var6 = new MimePart();
            var6.parse((Element)var4);
            this.parts.add(var6);
         }
      }

   }
}
