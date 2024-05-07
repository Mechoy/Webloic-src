package weblogic.wsee.wsdl.http;

import org.w3c.dom.Element;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlWriter;

public class UrlReplacement implements WsdlExtension {
   public static final String KEY = "url-replacement";

   public String getKey() {
      return "url-replacement";
   }

   public void write(Element var1, WsdlWriter var2) {
      var2.addChild(var1, "urlReplacement", "http://schemas.xmlsoap.org/wsdl/http/");
   }

   public void parse(Element var1) throws WsdlException {
   }

   public static UrlReplacement narrow(WsdlBindingMessage var0) {
      return (UrlReplacement)var0.getExtension("url-replacement");
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.end();
   }
}
