package weblogic.wsee.policy.deployment;

import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.PolicyConstants;
import weblogic.wsee.policy.util.PolicyHelper;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlWriter;

public class UsingPolicy implements WsdlExtension {
   private boolean usingPolicy = false;

   public UsingPolicy(boolean var1) {
      this.usingPolicy = var1;
   }

   public String getKey() {
      return "UsingPolicy";
   }

   public boolean isSet() {
      return this.usingPolicy;
   }

   public void enableUsingPolicy() {
      this.usingPolicy = true;
   }

   public static UsingPolicy narrow(WsdlDefinitions var0) {
      return (UsingPolicy)var0.getExtension("UsingPolicy");
   }

   public void write(Element var1, WsdlWriter var2) {
      String var3;
      if (PolicyHelper.hasWsp15NamespaceUri(var1)) {
         var3 = "http://www.w3.org/ns/ws-policy";
      } else {
         var3 = "http://schemas.xmlsoap.org/ws/2004/09/policy";
      }

      var2.addPrefix("wsp", var3);
      Element var4 = var2.addChild(var1, "UsingPolicy", var3);
      var4.setAttributeNS(PolicyConstants.WSDL_NAMESPACE_URI, "Required", Boolean.toString(this.usingPolicy));
   }
}
