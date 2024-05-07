package weblogic.wsee.wsdl.http;

import org.w3c.dom.Element;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlWriter;

public class UrlEncoded implements WsdlExtension {
   public static final String KEY = "url-encoded";

   public String getKey() {
      return "url-encoded";
   }

   public void write(Element var1, WsdlWriter var2) {
      var2.addChild(var1, "urlEncoded", "http://schemas.xmlsoap.org/wsdl/http/");
   }

   public void parse(Element var1) throws WsdlException {
   }

   public static UrlEncoded narrow(WsdlBindingMessage var0) {
      return (UrlEncoded)var0.getExtension("url-encoded");
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
