package weblogic.wsee.security.wss.plan.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Node;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.security.policy.MessagePartsEvaluator;
import weblogic.wsee.security.policy.assertions.xbeans.MessagePartsType;
import weblogic.wsee.security.policy12.assertions.XPath;
import weblogic.wsee.security.wss.policy.SecurityPolicyArchitectureException;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.stax.util.NamespaceContextImpl;

public class XpathNodesHelper {
   private static final boolean verbose = Verbose.isVerbose(XpathNodesHelper.class);
   private static final boolean debug = false;

   public static List<Node> findNode(MessagePartsType var0, SOAPMessageContext var1, boolean var2) throws SecurityPolicyArchitectureException {
      if (null == var0) {
         if (verbose) {
            Verbose.log((Object)"Null MessagePartsType found.");
         }

         return null;
      } else {
         XPath var3 = new XPath();
         var3.setAssertion(var0.getDialect(), var0.getStringValue(), "");
         return findNode(var3, var1, var2);
      }
   }

   public static List<Node> findNode(XPath var0, SOAPMessageContext var1, boolean var2) throws SecurityPolicyArchitectureException {
      if (null == var0) {
         if (verbose) {
            Verbose.log((Object)"Null XPath object found.");
         }

         return null;
      } else {
         ArrayList var3 = new ArrayList(1);
         var3.add(var0);
         return findNode((List)var3, var1, var2);
      }
   }

   public static List<Node> findNode(List<XPath> var0, SOAPMessageContext var1, boolean var2) throws SecurityPolicyArchitectureException {
      if (null != var0 && var0.size() != 0) {
         if (null != var1 && null != var1.getMessage()) {
            Node var3 = null;

            try {
               if (var1.getMessage().getSOAPBody() == null) {
                  throw new SecurityPolicyArchitectureException("missing Soap Body");
               }

               var3 = var1.getMessage().getSOAPBody().getParentNode();
            } catch (Exception var11) {
               throw new SecurityPolicyArchitectureException("missing Soap Body", var11);
            }

            ArrayList var4 = new ArrayList();
            Map var5 = DOMUtils.getNamespaceMap(var3);
            Iterator var6 = var0.iterator();

            while(var6.hasNext()) {
               XPath var7 = (XPath)var6.next();
               MessagePartsEvaluator var8 = new MessagePartsEvaluator(var7, var1, var5);

               try {
                  List var9 = var8.getNodes();
                  if (null != var9 && var9.size() != 0) {
                     var4.addAll(var9);
                  } else {
                     if (var2) {
                        throw new SecurityPolicyArchitectureException("Missing required element with xpath=" + var7.getXPathExpr());
                     }

                     Verbose.log((Object)("Element in xpath not found in SOAP message (but not required)=" + var7.getXPathExpr()));
                  }
               } catch (PolicyException var10) {
                  if (var2) {
                     throw new SecurityPolicyArchitectureException("Missing required element with xpath=" + var7.getXPathExpr(), var10);
                  }

                  Verbose.log((Object)("Element in xpath not found in SOAP message (but not required)=" + var7.getXPathExpr()));
               }
            }

            return var4;
         } else {
            throw new IllegalArgumentException("Null Soap Message found");
         }
      } else {
         if (verbose) {
            Verbose.log((Object)"No XPath objects found.");
         }

         return null;
      }
   }

   public static javax.xml.xpath.XPath buildXPath(List var0) {
      javax.xml.xpath.XPath var1 = XPathFactory.newInstance().newXPath();
      NamespaceContextImpl var2 = new NamespaceContextImpl();
      var1.setNamespaceContext(var2);
      return var1;
   }

   public static String getElementPath(Node var0) {
      if (var0 == null) {
         return null;
      } else {
         String var1 = "";

         do {
            if (var0.getNodeType() != 1) {
               if (var1.length() == 0) {
                  return null;
               }

               return var1;
            }

            var1 = "/" + var0.getNamespaceURI() + ":" + var0.getLocalName() + var1;
            var0 = var0.getParentNode();
         } while(var0 != null);

         return null;
      }
   }
}
