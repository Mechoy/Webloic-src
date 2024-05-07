package weblogic.wsee.policy.deployment;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyStatement;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.policy.util.PolicyHelper;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlUtils;
import weblogic.wsee.wsdl.WsdlWriter;

public class PolicyReferenceWsdlExtension implements WsdlExtension {
   private ArrayList URIs = new ArrayList();

   public void addURI(URI var1) {
      this.URIs.add(var1);
   }

   public void removeURI(URI var1) {
      this.URIs.remove(var1);
   }

   public ArrayList getURIs() {
      return this.URIs;
   }

   public void setURIs(ArrayList var1) {
      this.URIs = var1;
   }

   public String getKey() {
      return "PolicyReference";
   }

   public void write(Element var1, WsdlWriter var2) {
      String var3;
      if (PolicyHelper.hasWsp15NamespaceUri(var1)) {
         var3 = "http://www.w3.org/ns/ws-policy";
      } else {
         var3 = "http://schemas.xmlsoap.org/ws/2004/09/policy";
      }

      Element var4 = var2.addChild(var1, "Policy", var3);
      Iterator var5 = this.URIs.iterator();

      while(var5.hasNext()) {
         URI var6 = (URI)var5.next();
         if (null != var6 && var6.toString() != null && var6.toString().trim().length() != 0) {
            Element var7 = var2.addChild(var4, "PolicyReference", var3);
            var2.setAttribute(var7, "URI", (String)null, (String)PolicyURIs.replaceURIWithId(var6, var2));
         }
      }

   }

   public NormalizedExpression getEffectivePolicy(Map var1) throws PolicyException {
      return this.getEffectivePolicy((PolicyServer)null, var1);
   }

   public NormalizedExpression getEffectivePolicy(PolicyServer var1, Map var2) throws PolicyException {
      PolicyRefFactory var3 = new PolicyRefFactory(var2);
      LinkedHashMap var4 = new LinkedHashMap();
      Iterator var5 = this.URIs.iterator();

      while(var5.hasNext()) {
         URI var6 = (URI)var5.next();
         if (null != var6 && var6.toString() != null && var6.toString().trim().length() != 0) {
            PolicyStatement var7 = var3.createPolicyRef((String)null, var6).getPolicy(var1, false);
            WsdlUtils.addPolicyToMap(var4, var7);
         }
      }

      return PolicyWsdlExtension.getEffectivePolicy(var4);
   }
}
