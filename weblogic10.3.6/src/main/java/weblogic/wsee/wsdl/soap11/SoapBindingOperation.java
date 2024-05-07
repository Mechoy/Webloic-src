package weblogic.wsee.wsdl.soap11;

import org.w3c.dom.Element;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlReader;
import weblogic.wsee.wsdl.WsdlWriter;

public class SoapBindingOperation implements WsdlExtension {
   public static final String KEY = "SOAP11-binding-operation";
   private String style;
   private String soapAction;

   protected String getSOAPNS() {
      return "http://schemas.xmlsoap.org/wsdl/soap/";
   }

   public String getKey() {
      return "SOAP11-binding-operation";
   }

   public String getStyle() {
      return this.style;
   }

   public void setStyle(String var1) {
      if (!"rpc".equals(var1) && !"document".equals(var1)) {
         throw new IllegalArgumentException("Invalid soap binding style value:" + var1);
      } else {
         this.style = var1;
      }
   }

   public String getSoapAction() {
      return this.soapAction;
   }

   public void setSoapAction(String var1) {
      this.soapAction = var1;
   }

   public static SoapBindingOperation narrow(WsdlBindingOperation var0) {
      return (SoapBindingOperation)var0.getExtension("SOAP11-binding-operation");
   }

   public void parse(Element var1) {
      this.style = WsdlReader.getAttribute(var1, (String)null, "style");
      this.soapAction = WsdlReader.getAttribute(var1, (String)null, "soapAction", true);
   }

   public void write(Element var1, WsdlWriter var2) {
      Element var3 = var2.addChild(var1, "operation", this.getSOAPNS());
      if (this.style != null) {
         var2.setAttribute(var3, "style", (String)null, (String)this.style);
      }

      if (this.soapAction != null && !this.soapAction.trim().equals("")) {
         var2.setAttribute(var3, "soapAction", (String)null, (String)this.soapAction);
      }

   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("style", this.style);
      var1.writeField("soapAction", this.soapAction);
      var1.end();
   }

   public static SoapBindingOperation attach(WsdlBindingOperation var0) {
      SoapBindingOperation var1 = new SoapBindingOperation();
      var0.putExtension(var1);
      return var1;
   }
}
