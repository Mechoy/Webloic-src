package weblogic.wsee.wsdl.mime;

import org.w3c.dom.Element;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlWriter;

public class MimeXml implements WsdlExtension {
   public static final String KEY = "mime-xml";
   private String part;

   public String getPart() {
      return this.part;
   }

   public void setPart(String var1) {
      this.part = var1;
   }

   public String getKey() {
      return "mime-xml";
   }

   public void write(Element var1, WsdlWriter var2) {
      Element var3 = var2.addChild(var1, "mimeXml", "http://schemas.xmlsoap.org/wsdl/mime/");
      if (this.part != null) {
         var3.setAttribute("part", this.part);
      }

   }

   public void parse(Element var1) throws WsdlException {
      this.part = var1.getAttributeNS((String)null, "part");
      if ("".equals(this.part)) {
         this.part = null;
      }

   }

   public static MimeXml narrow(MimePart var0) {
      return (MimeXml)var0.getExtension("mime-xml");
   }

   public static MimeXml narrow(WsdlBindingMessage var0) {
      return (MimeXml)var0.getExtension("mime-xml");
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("part", this.part);
      var1.end();
   }
}
