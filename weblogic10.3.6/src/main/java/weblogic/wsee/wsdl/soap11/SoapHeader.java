package weblogic.wsee.wsdl.soap11;

import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlReader;
import weblogic.wsee.wsdl.WsdlWriter;
import weblogic.wsee.wsdl.mime.MimePart;

public class SoapHeader extends SoapMessageBase implements WsdlExtension {
   private static final String KEY = "SOAP11-header";
   private String part;
   private QName message;

   protected String getSOAPNS() {
      return "http://schemas.xmlsoap.org/wsdl/soap/";
   }

   public String getUse() {
      return this.use == null ? "literal" : this.use;
   }

   public String getKey() {
      return "SOAP11-header";
   }

   public String getPart() {
      return this.part;
   }

   public void setPart(String var1) {
      this.part = var1;
   }

   public QName getMessage() {
      return this.message;
   }

   public void setMessage(QName var1) {
      this.message = var1;
   }

   public void parse(Element var1) throws WsdlException {
      super.parse(var1);
      String var2 = WsdlReader.getMustAttribute(var1, (String)null, "message");
      this.message = WsdlReader.createQName(var1, var2);
      this.part = WsdlReader.getMustAttribute(var1, (String)null, "part");
   }

   public static SoapHeader narrow(WsdlBindingMessage var0) {
      return (SoapHeader)var0.getExtension("SOAP11-header");
   }

   public static SoapHeader narrow(MimePart var0) {
      return (SoapHeader)var0.getExtension("SOAP11-header");
   }

   public void write(Element var1, WsdlWriter var2) {
      Element var3 = var2.addChild(var1, "header", this.getSOAPNS());
      var2.setAttribute(var3, "message", (String)null, (QName)this.message);
      var2.setAttribute(var3, "part", (String)null, (String)this.part);
      if (this.use != null) {
         var2.setAttribute(var3, "use", (String)null, (String)this.use);
      }

      if (this.encodingStyle != null) {
         var2.setAttribute(var3, "encodingStyle", (String)null, (String)this.encodingStyle);
      }

   }

   public static SoapHeader attach(WsdlBindingMessage var0) {
      SoapHeader var1 = new SoapHeader();
      var0.putExtension(var1);
      return var1;
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("message", this.message);
      var1.writeField("encodingStyle", this.encodingStyle);
      var1.writeField("use", this.use);
      var1.writeField("namespace", this.namespace);
      var1.writeField("parts", this.part);
      var1.end();
   }
}
