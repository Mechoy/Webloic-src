package weblogic.wsee.wsdl.soap11;

import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Element;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlReader;
import weblogic.wsee.wsdl.WsdlWriter;
import weblogic.wsee.wsdl.builder.WsdlBindingBuilder;

public class SoapBinding implements WsdlExtension {
   public static final String KEY = "SOAP11";
   private String transport;
   private String style;
   protected static final Map knownProtocols = new HashMap();

   protected String getSOAPNS() {
      return "http://schemas.xmlsoap.org/wsdl/soap/";
   }

   public String getKey() {
      return "SOAP11";
   }

   public String getStyle() {
      return this.style;
   }

   public String getTransportProtocol() {
      String var1 = (String)knownProtocols.get(this.transport);
      if (var1 == null) {
         var1 = "unknown";
      }

      return var1;
   }

   public static SoapBinding narrow(WsdlBinding var0) {
      return (SoapBinding)var0.getExtension("SOAP11");
   }

   public static SoapBinding attach(WsdlBinding var0) throws WsdlException {
      SoapBinding var1 = new SoapBinding();
      if (var0 instanceof WsdlBindingBuilder) {
         WsdlBindingBuilder var2 = (WsdlBindingBuilder)var0;
         var2.setBindingType("SOAP11");
         var2.setTransportProtocol(var1.getTransportProtocol());
      }

      var0.putExtension(var1);
      return var1;
   }

   public void parse(Element var1) throws WsdlException {
      this.transport = WsdlReader.getMustAttribute(var1, (String)null, "transport");
      String var2 = var1.getAttribute("style");
      if (!StringUtil.isEmpty(var2)) {
         this.style = var2;
      } else {
         this.style = "document";
      }

   }

   public void write(Element var1, WsdlWriter var2) {
      Element var3 = var2.addChild(var1, "binding", this.getSOAPNS());
      var2.setAttribute(var3, "transport", (String)null, (String)this.transport);
      var2.setAttribute(var3, "style", (String)null, (String)this.style);
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("style", this.style);
      var1.writeField("transport", this.transport);
      var1.end();
   }

   public void setStyle(String var1) {
      this.style = var1;
   }

   public void setTransport(String var1) {
      this.transport = var1;
   }

   public String getTransport() {
      return this.transport;
   }

   static {
      knownProtocols.put("http://schemas.xmlsoap.org/soap/http", "http");
      knownProtocols.put("http://schemas.xmlsoap.org/soap/https", "https");
      knownProtocols.put("http://www.openuri.org/2002/04/soap/jms/", "jms");
   }
}
