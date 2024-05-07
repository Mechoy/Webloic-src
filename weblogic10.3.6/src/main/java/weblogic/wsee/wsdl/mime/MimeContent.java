package weblogic.wsee.wsdl.mime;

import java.util.List;
import org.w3c.dom.Element;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlWriter;

public class MimeContent implements WsdlExtension {
   public static final String KEY = "mime-content";
   private String part;
   private String type;

   public String getPart() {
      return this.part;
   }

   public void setPart(String var1) {
      this.part = var1;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String var1) {
      this.type = var1;
   }

   public String getKey() {
      return "mime-content";
   }

   public void write(Element var1, WsdlWriter var2) {
      Element var3 = var2.addChild(var1, "content", "http://schemas.xmlsoap.org/wsdl/mime/");
      if (this.part != null) {
         var3.setAttribute("part", this.part);
      }

      if (this.type != null) {
         var3.setAttribute("type", this.type);
      }

   }

   public void parse(Element var1) throws WsdlException {
      this.part = var1.getAttribute("part");
      if ("".equals(this.part)) {
         this.part = null;
      }

      this.type = var1.getAttribute("type");
      if ("".equals(this.type)) {
         this.type = null;
      }

   }

   public static List<MimeContent> narrow(MimePart var0) {
      return var0.getExtensionList("mime-content");
   }

   public static List<MimeContent> narrow(WsdlBindingMessage var0) {
      return var0.getExtensionList("mime-content");
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("part", this.part);
      var1.writeField("type", this.type);
      var1.end();
   }
}
