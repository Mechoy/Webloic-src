package weblogic.wsee.tools.jws;

import org.w3c.dom.Element;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlService;
import weblogic.wsee.wsdl.WsdlWriter;

public class UpgradedJwsWsdlWriter implements WsdlExtension {
   private static final String KEY = "upgraded81Jws";
   public static final String UPGRADED_JWS_NS = "http://www.openuri.org/2006/12/wsdl/upgradedJWS";
   public static final String XML_TAG_UPGRADED = "upgraded81";

   public String getKey() {
      return "upgraded81Jws";
   }

   public void write(Element var1, WsdlWriter var2) {
      var2.addChild(var1, "upgraded81", "http://www.openuri.org/2006/12/wsdl/upgradedJWS");
   }

   public static final UpgradedJwsWsdlWriter narrow(WsdlService var0) {
      return (UpgradedJwsWsdlWriter)var0.getExtension("upgraded81Jws");
   }

   public static final UpgradedJwsWsdlWriter attach(WsdlService var0) {
      UpgradedJwsWsdlWriter var1 = new UpgradedJwsWsdlWriter();
      var0.putExtension(var1);
      return var1;
   }
}
