package weblogic.wsee.wsdl.soap11;

import org.w3c.dom.Element;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlWriter;

public class SoapFault extends SoapMessageBase implements WsdlExtension {
   private static final String KEY = "SOAP11-fault";
   private String name;

   protected String getSOAPNS() {
      return "http://schemas.xmlsoap.org/wsdl/soap/";
   }

   public String getUse() {
      return this.use == null ? "literal" : this.use;
   }

   public String getKey() {
      return "SOAP11-fault";
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public void parse(Element var1) throws WsdlException {
      super.parse(var1);
      this.name = var1.getAttribute("name");
      if (this.name == null) {
         Verbose.log((Object)"SOAP Fault name is null");
      }

   }

   public static SoapFault narrow(WsdlBindingMessage var0) {
      return (SoapFault)var0.getExtension("SOAP11-fault");
   }

   public void write(Element var1, WsdlWriter var2) {
      Element var3 = var2.addChild(var1, "fault", this.getSOAPNS());
      if (this.name != null) {
         var3.setAttribute("name", this.name);
      }

      if (this.encodingStyle != null) {
         var2.setAttribute(var3, "encodingStyle", (String)null, (String)this.encodingStyle);
      }

      if (this.use != null) {
         var2.setAttribute(var3, "use", (String)null, (String)this.use);
      }

      if (this.namespace != null) {
         var2.setAttribute(var3, "namespace", (String)null, (String)this.namespace);
      }

   }

   public static SoapFault attach(WsdlBindingMessage var0) {
      SoapFault var1 = new SoapFault();
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
      var1.writeField("name", this.name);
      var1.writeField("encodingStyle", this.encodingStyle);
      var1.writeField("use", this.use);
      var1.writeField("namespace", this.namespace);
      var1.end();
   }
}
