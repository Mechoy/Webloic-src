package weblogic.wsee.wsdl.soap11;

import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlReader;
import weblogic.wsee.wsdl.WsdlWriter;
import weblogic.wsee.wsdl.mime.MimeMultipartRelated;
import weblogic.wsee.wsdl.mime.MimePart;

public class SoapBody extends SoapMessageBase implements WsdlExtension {
   public static final String KEY = "SOAP11-body";
   private String parts;

   protected String getSOAPNS() {
      return "http://schemas.xmlsoap.org/wsdl/soap/";
   }

   public String getUse() {
      return this.use == null ? "literal" : this.use;
   }

   public String getParts() {
      return this.parts;
   }

   public void setParts(String var1) {
      this.parts = var1;
   }

   public String getKey() {
      return "SOAP11-body";
   }

   public static SoapBody narrow(WsdlBindingMessage var0) {
      SoapBody var1 = (SoapBody)var0.getExtension("SOAP11-body");
      if (var1 == null) {
         MimeMultipartRelated var2 = MimeMultipartRelated.narrow(var0);
         if (var2 != null) {
            List var3 = var2.getParts();
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
               MimePart var5 = (MimePart)var4.next();
               var1 = narrow(var5);
               if (var1 != null) {
                  if (var1.getNamespace() == null) {
                     var1.setNamespace(var0.getBindingOperation().getName().getNamespaceURI());
                  }
                  break;
               }
            }
         }
      }

      return var1;
   }

   public static SoapBody narrow(MimePart var0) {
      return (SoapBody)var0.getExtension("SOAP11-body");
   }

   public void parse(Element var1) throws WsdlException {
      super.parse(var1);
      this.parts = WsdlReader.getAttribute(var1, (String)null, "parts");
   }

   public void write(Element var1, WsdlWriter var2) {
      Element var3 = var2.addChild(var1, "body", this.getSOAPNS());
      if (this.encodingStyle != null) {
         var2.setAttribute(var3, "encodingStyle", (String)null, (String)this.encodingStyle);
      }

      if (this.use != null) {
         var2.setAttribute(var3, "use", (String)null, (String)this.use);
      }

      if (this.namespace != null) {
         var2.setAttribute(var3, "namespace", (String)null, (String)this.namespace);
      }

      if (this.parts != null) {
         var2.setAttribute(var3, "parts", (String)null, (String)this.parts);
      }

   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("encodingStyle", this.encodingStyle);
      var1.writeField("use", this.use);
      var1.writeField("namespace", this.namespace);
      var1.writeField("parts", this.parts);
      var1.end();
   }

   public static SoapBody attach(WsdlBindingMessage var0) {
      SoapBody var1 = new SoapBody();
      var0.putExtension(var1);
      return var1;
   }

   public void setNamespace(String var1) {
      this.namespace = var1;
   }
}
