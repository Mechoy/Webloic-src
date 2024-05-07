package weblogic.wsee.wsdl.http;

import org.w3c.dom.Element;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlReader;
import weblogic.wsee.wsdl.WsdlWriter;
import weblogic.wsee.wsdl.builder.WsdlBindingBuilder;

public class HttpBinding implements WsdlExtension {
   public static final String KEY = "HTTP-binding";
   public static final String VERB_GET = "GET";
   public static final String VERB_POST = "POST";
   private String verb;

   public String getKey() {
      return "HTTP-binding";
   }

   public String getVerb() {
      return this.verb;
   }

   public static HttpBinding narrow(WsdlBinding var0) {
      return (HttpBinding)var0.getExtension("HTTP-binding");
   }

   public static HttpBinding attach(WsdlBinding var0) throws WsdlException {
      HttpBinding var1 = new HttpBinding();
      if (var0 instanceof WsdlBindingBuilder) {
         WsdlBindingBuilder var2 = (WsdlBindingBuilder)var0;
         var2.setBindingType("HTTP-binding");
         var2.setTransportProtocol("http");
      }

      var0.putExtension(var1);
      return var1;
   }

   public void parse(Element var1) throws WsdlException {
      this.verb = WsdlReader.getMustAttribute(var1, (String)null, "verb");
   }

   public void write(Element var1, WsdlWriter var2) {
      Element var3 = var2.addChild(var1, "binding", "http://schemas.xmlsoap.org/wsdl/http/");
      var2.setAttribute(var3, "verb", (String)null, (String)this.verb);
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("verb", this.verb);
      var1.end();
   }
}
