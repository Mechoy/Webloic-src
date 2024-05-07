package weblogic.wsee.policy.deployment;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.PolicyConstants;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyStatement;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtensible;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlExtensionParser;
import weblogic.wsee.wsdl.WsdlMessage;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlReader;
import weblogic.wsee.wsdl.WsdlService;

public class PolicyWsdlExtensionParser implements WsdlExtensionParser {
   private PolicyLoader policyLoader = this.createPolicyLoader();

   public WsdlExtension parseMessage(WsdlMessage var1, Element var2) throws WsdlException {
      Element[] var3 = null;
      if ((var3 = getPolicyReferenceExtensionElement(var2)) != null) {
         return this.addPolicyReferenceWsdlExtension(var1, var3);
      } else {
         return isPolicyExtensionElement(var2) ? this.addPolicyWsdlExtension(var1, var2) : null;
      }
   }

   public WsdlExtension parseOperation(WsdlOperation var1, Element var2) throws WsdlException {
      Element[] var3 = null;
      if ((var3 = getPolicyReferenceExtensionElement(var2)) != null) {
         return this.addPolicyReferenceWsdlExtension(var1, var3);
      } else {
         return isPolicyExtensionElement(var2) ? this.addPolicyWsdlExtension(var1, var2) : null;
      }
   }

   public WsdlExtension parseBinding(WsdlBinding var1, Element var2) throws WsdlException {
      Element[] var3 = null;
      if ((var3 = getPolicyReferenceExtensionElement(var2)) != null) {
         return this.addPolicyReferenceWsdlExtension(var1, var3);
      } else {
         return isPolicyExtensionElement(var2) ? this.addPolicyWsdlExtension(var1, var2) : null;
      }
   }

   public WsdlExtension parseBindingOperation(WsdlBindingOperation var1, Element var2) throws WsdlException {
      Element[] var3 = null;
      if ((var3 = getPolicyReferenceExtensionElement(var2)) != null) {
         return this.addPolicyReferenceWsdlExtension(var1, var3);
      } else {
         return isPolicyExtensionElement(var2) ? this.addPolicyWsdlExtension(var1, var2) : null;
      }
   }

   public WsdlExtension parseBindingMessage(WsdlBindingMessage var1, Element var2) throws WsdlException {
      Element[] var3 = null;
      if ((var3 = getPolicyReferenceExtensionElement(var2)) != null) {
         return this.addPolicyReferenceWsdlExtension(var1, var3);
      } else {
         return isPolicyExtensionElement(var2) ? this.addPolicyWsdlExtension(var1, var2) : null;
      }
   }

   public WsdlExtension parseService(WsdlService var1, Element var2) throws WsdlException {
      Element[] var3 = null;
      if ((var3 = getPolicyReferenceExtensionElement(var2)) != null) {
         return this.addPolicyReferenceWsdlExtension(var1, var3);
      } else {
         return isPolicyExtensionElement(var2) ? this.addPolicyWsdlExtension(var1, var2) : null;
      }
   }

   public WsdlExtension parsePort(WsdlPort var1, Element var2) throws WsdlException {
      Element[] var3 = null;
      if ((var3 = getPolicyReferenceExtensionElement(var2)) != null) {
         return this.addPolicyReferenceWsdlExtension(var1, var3);
      } else {
         return isPolicyExtensionElement(var2) ? this.addPolicyWsdlExtension(var1, var2) : null;
      }
   }

   public WsdlExtension parseDefinitions(WsdlDefinitions var1, Element var2) throws WsdlException {
      if (!WsdlReader.tagEquals(var2, "UsingPolicy", "http://schemas.xmlsoap.org/ws/2004/09/policy") && !WsdlReader.tagEquals(var2, "UsingPolicy", "http://www.w3.org/ns/ws-policy")) {
         return isPolicyExtensionElement(var2) ? this.addPolicyWsdlExtension(var1, var2) : null;
      } else {
         String var3 = WsdlReader.getAttribute(var2, PolicyConstants.WSDL_NAMESPACE_URI, "Required");
         if (var3 == null) {
            var3 = "true";
         }

         boolean var4 = "true".equalsIgnoreCase(var3);
         return new UsingPolicy(var4);
      }
   }

   public void cleanUp() {
      this.policyLoader = this.createPolicyLoader();
   }

   private PolicyLoader createPolicyLoader() {
      try {
         return new PolicyLoader(ProviderRegistry.getTheRegistry());
      } catch (PolicyException var2) {
         throw new AssertionError(var2.getMessage());
      }
   }

   private PolicyStatement parsePolicyStatement(Element var1) throws WsdlException {
      try {
         PolicyStatement var2 = this.policyLoader.load((Node)var1);
         return var2;
      } catch (PolicyException var3) {
         var3.printStackTrace();
         throw new WsdlException("Fail to handle " + var1.getLocalName());
      }
   }

   private PolicyWsdlExtension addPolicyWsdlExtension(WsdlExtensible var1, Element var2) throws WsdlException {
      PolicyWsdlExtension var3 = (PolicyWsdlExtension)var1.getExtension("Policy");
      if (var3 == null) {
         var3 = new PolicyWsdlExtension();
         var1.putExtension(var3);
      }

      var3.addPolicy(this.parsePolicyStatement(var2));
      return var3;
   }

   private PolicyReferenceWsdlExtension addPolicyReferenceWsdlExtension(WsdlExtensible var1, Element[] var2) throws WsdlException {
      PolicyReferenceWsdlExtension var3 = (PolicyReferenceWsdlExtension)var1.getExtension("PolicyReference");
      if (var3 == null) {
         var3 = new PolicyReferenceWsdlExtension();
      }

      try {
         for(int var4 = 0; var4 < var2.length; ++var4) {
            URI var5 = DOMUtils.getAttributeValueAsURI(var2[var4], PolicyConstants.POLICY_INCLUDE_URI_REF_ATTRIBUTE);
            var3.addURI(var5);
         }

         return var3;
      } catch (URISyntaxException var6) {
         throw new WsdlException(var6.getMessage());
      }
   }

   private static boolean isPolicyExtensionElement(Element var0) {
      return WsdlReader.tagEquals(var0, "Policy", "http://schemas.xmlsoap.org/ws/2004/09/policy") || WsdlReader.tagEquals(var0, "Policy", "http://www.w3.org/ns/ws-policy");
   }

   private static Element[] getPolicyReferenceExtensionElement(Element var0) {
      if (!WsdlReader.tagEquals(var0, "PolicyReference", "http://schemas.xmlsoap.org/ws/2004/09/policy") && !WsdlReader.tagEquals(var0, "PolicyReference", "http://www.w3.org/ns/ws-policy")) {
         if (WsdlReader.tagEquals(var0, "Policy", "http://schemas.xmlsoap.org/ws/2004/09/policy") || WsdlReader.tagEquals(var0, "Policy", "http://www.w3.org/ns/ws-policy")) {
            ArrayList var5 = new ArrayList();
            NodeList var2 = var0.getChildNodes();

            for(int var3 = 0; var3 < var2.getLength(); ++var3) {
               Node var4 = var2.item(var3);
               if (var4.getNodeType() == 1) {
                  if (!DOMUtils.equalsQName((Element)var4, PolicyConstants.POLICY_INCLUDE) && !DOMUtils.equalsQName((Element)var4, PolicyConstants.POLICY_INCLUDE_15)) {
                     return null;
                  }

                  var5.add(var4);
               }
            }

            if (var5.size() > 0) {
               return (Element[])((Element[])var5.toArray(new Element[0]));
            }
         }

         return null;
      } else {
         Element[] var1 = new Element[]{var0};
         return var1;
      }
   }
}
