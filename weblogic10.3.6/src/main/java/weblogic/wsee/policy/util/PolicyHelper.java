package weblogic.wsee.policy.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyConstants;
import weblogic.wsee.policy.framework.PolicyExpression;

public class PolicyHelper {
   private static final String VALUE_TRUE;
   private static final String VALUE_FALSE;

   public static String getOptionalPolicyNamespaceUri(Element var0) {
      if (null == var0) {
         return null;
      } else {
         String var1 = null;
         Boolean var2 = DOMUtils.getAttributeValueAsBoolean(var0, PolicyConstants.OPTIONAL);
         if (var2 == null) {
            var2 = DOMUtils.getAttributeValueAsBoolean(var0, PolicyConstants.OPTIONAL_15);
            if (null != var2) {
               var1 = "http://www.w3.org/ns/ws-policy";
            }
         } else {
            var1 = "http://schemas.xmlsoap.org/ws/2004/09/policy";
         }

         return var1;
      }
   }

   public static Boolean getOptionalBoolean(Element var0) {
      if (null == var0) {
         return null;
      } else {
         Boolean var1 = DOMUtils.getAttributeValueAsBoolean(var0, PolicyConstants.OPTIONAL);
         if (var1 == null) {
            var1 = DOMUtils.getAttributeValueAsBoolean(var0, PolicyConstants.OPTIONAL_15);
         }

         return var1 == null ? null : var1;
      }
   }

   public static void addOptionalAttribute(Element var0, String var1) {
      addOptionalAttribute(var0, true, var1);
   }

   public static void addOptionalAttribute(Element var0, boolean var1, String var2) {
      if (null != var0) {
         Boolean var3 = getOptionalBoolean(var0);
         if (var3 == null) {
            QName var4 = null;
            String var5 = null;
            if ("http://www.w3.org/ns/ws-policy".equals(var2)) {
               var4 = PolicyConstants.OPTIONAL_15;
            } else if ("http://schemas.xmlsoap.org/ws/2004/09/policy".equals(var2)) {
               var4 = PolicyConstants.OPTIONAL;
            } else {
               Node var6 = var0.getParentNode();
               if (var6 != null) {
                  if ("http://schemas.xmlsoap.org/ws/2004/09/policy".equals(var6.getBaseURI())) {
                     var4 = PolicyConstants.OPTIONAL;
                  } else if ("http://www.w3.org/ns/ws-policy".equals(var6.getBaseURI())) {
                     var4 = PolicyConstants.OPTIONAL_15;
                  }
               } else if ("http://schemas.xmlsoap.org/ws/2004/09/policy".equals(var0.getBaseURI())) {
                  var4 = PolicyConstants.OPTIONAL;
               } else if ("http://www.w3.org/ns/ws-policy".equals(var0.getBaseURI())) {
                  var4 = PolicyConstants.OPTIONAL_15;
               }
            }

            if (null == var4) {
               var4 = PolicyConstants.OPTIONAL;
            }

            if (var4 == PolicyConstants.OPTIONAL) {
               var5 = "wsp";
            } else {
               var5 = "wsp15";
            }

            if (var1) {
               DOMUtils.addPrefixedAttribute(var0, var4, var5, VALUE_TRUE);
            } else {
               DOMUtils.addPrefixedAttribute(var0, var4, var5, VALUE_FALSE);
            }

         }
      }
   }

   public static void checkOptionNamespaceUri(Element var0, String var1) {
      if (null == var1) {
         throw new IllegalArgumentException("nsUri cannot be null");
      } else {
         Boolean var2 = DOMUtils.getAttributeValueAsBoolean(var0, PolicyConstants.OPTIONAL);
         if (var2 != null) {
            if (!"http://schemas.xmlsoap.org/ws/2004/09/policy".equals(var1)) {
               String var3 = VALUE_TRUE;
               if (!var2) {
                  var3 = VALUE_FALSE;
               }

               var0.removeAttributeNS("http://schemas.xmlsoap.org/ws/2004/09/policy", PolicyConstants.OPTIONAL.getLocalPart());
               DOMUtils.addPrefixedAttribute(var0, PolicyConstants.OPTIONAL_15, "wsp15", var3);
            }
         }
      }
   }

   public static boolean hasWsp15NamespaceUri(Element var0) {
      Map var1 = DOMUtils.getNamespaceMapping(var0);
      return var1.containsValue("http://www.w3.org/ns/ws-policy");
   }

   public static String getPolicyNamespaceUri(PolicyAlternative var0) {
      if (null != var0 && !var0.isEmpty()) {
         Set var1 = var0.getAssertions();
         Iterator var2 = var1.iterator();

         PolicyAssertion var3;
         do {
            if (!var2.hasNext()) {
               return "http://schemas.xmlsoap.org/ws/2004/09/policy";
            }

            var3 = (PolicyAssertion)var2.next();
         } while(null == var3);

         return var3.getPolicyNamespaceUri();
      } else {
         return "http://schemas.xmlsoap.org/ws/2004/09/policy";
      }
   }

   public static String getPolicyNamespaceUri(Set var0) {
      if (null != var0 && !var0.isEmpty()) {
         Iterator var1 = var0.iterator();

         PolicyAlternative var2;
         do {
            if (!var1.hasNext()) {
               return "http://schemas.xmlsoap.org/ws/2004/09/policy";
            }

            var2 = (PolicyAlternative)var1.next();
         } while(var2 == null);

         return getPolicyNamespaceUri(var2);
      } else {
         return "http://schemas.xmlsoap.org/ws/2004/09/policy";
      }
   }

   public static void setPolicyExpressionNs(PolicyExpression var0, Node var1) {
      if (null != var1) {
         Node var2 = var1.getParentNode();
         if (null != var2 && "http://www.w3.org/ns/ws-policy".equals(var2.getNamespaceURI())) {
            var0.setPolicyNamespaceUri("http://www.w3.org/ns/ws-policy");
         }

      }
   }

   static {
      VALUE_TRUE = Boolean.TRUE.toString();
      VALUE_FALSE = Boolean.FALSE.toString();
   }
}
