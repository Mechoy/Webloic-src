package weblogic.wsee.security.wssc.dk;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.rpc.handler.MessageContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.security.service.ContextHandler;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.security.wss.policy.GeneralPolicy;
import weblogic.wsee.security.wss.policy.SecurityPolicyArchitectureException;
import weblogic.wsee.security.wssp.AlgorithmSuiteInfo;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.wss.SecurityTokenContextHandler;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityInfo;
import weblogic.xml.crypto.wss.policy.Claims;
import weblogic.xml.crypto.wss.policy.ClaimsBuilder;

public class DKClaims implements Claims {
   private static final boolean verbose = Verbose.isVerbose(DKClaims.class);
   private static final String POLICY_URI = "http://www.bea.com/wls90/security/policy";
   private static final QName CLAIMS = new QName("http://www.bea.com/wls90/security/policy", "Claims");
   private static final QName LABEL = new QName("http://www.bea.com/wls90/security/policy", "Label");
   private static final QName LENGTH = new QName("http://www.bea.com/wls90/security/policy", "Length");

   public static Element makeClaimsNode() throws SecurityPolicyArchitectureException {
      try {
         DocumentBuilderFactory var0 = DocumentBuilderFactory.newInstance();
         DocumentBuilder var1 = var0.newDocumentBuilder();
         Document var2 = var1.newDocument();
         if (verbose) {
            Verbose.log((Object)"makeDKClaimsNode: got document");
         }

         Element var3 = var2.createElementNS(CLAIMS.getNamespaceURI(), "wssp:" + CLAIMS.getLocalPart());
         if (verbose) {
            Verbose.log((Object)"makeDKClaimsNode: got CLAIMS element");
         }

         return var3;
      } catch (Exception var4) {
         if (verbose) {
            Verbose.log((Object)("makeDKClaimsNode: caught exception: " + var4.toString()));
         }

         var4.printStackTrace();
         throw new SecurityPolicyArchitectureException(var4);
      }
   }

   public static Node makeDKClaimsNode(GeneralPolicy var0, String var1, AlgorithmSuiteInfo var2) throws SecurityPolicyArchitectureException {
      if (verbose) {
         Verbose.banner("makeDKClaimsNode");
         Verbose.log((Object)("label is '" + var1 + "'"));
         if (var2 == null) {
            Verbose.log((Object)"AlgorithmSuiteInfo is null!");
         } else {
            Verbose.log((Object)("MinSKL from ASI is '" + Integer.toString(var2.getMinSymKeyLength()) + "'"));
         }
      }

      Element var3 = makeClaimsNode();
      Element var4 = DOMUtils.createAndAddElement(var3, LABEL, var3.getPrefix());
      if (verbose) {
         Verbose.log((Object)"makeDKClaimsNode: got LABEL element");
      }

      String var5 = var1 != null ? var1 : "WS-SecureConversationWS-SecureConversation";
      if (verbose) {
         Verbose.log((Object)("makeDKClaimsNode: label string is " + var5));
      }

      DOMUtils.addText(var4, var5);
      if (verbose) {
         Verbose.log((Object)"makeDKClaimsNode: set label string");
      }

      Element var6 = DOMUtils.createAndAddElement(var3, LENGTH, var3.getPrefix());
      if (verbose) {
         Verbose.log((Object)"makeDKClaimsNode: got LENGTH element");
      }

      int var7 = var2 != null && var2.getMinSymKeyLength() > 0 ? var2.getMinSymKeyLength() / 8 : 32;
      String var8 = Integer.toString(var7);
      if (verbose) {
         Verbose.log((Object)("makeDKClaimsNode: length string is " + var8));
      }

      DOMUtils.addText(var6, var8);
      if (verbose) {
         Verbose.log((Object)"makeDKClaimsNode: set length string");
      }

      if (verbose) {
         Verbose.log((Object)"makeDKClaimsNode: returning claims");
      }

      return var3;
   }

   public static String getLabelFromContextHandler(ContextHandler var0) {
      if (verbose) {
         Verbose.banner("DKClaims.getLabelFromContextHandler");
      }

      String var1 = (String)var0.getValue(SecurityTokenContextHandler.DK_LABEL);
      if (var1 != null) {
         if (verbose) {
            Verbose.log((Object)("DKLabel: returning label from context handler: " + var1));
         }

         return var1;
      } else {
         Node var2 = (Node)var0.getValue("weblogic.xml.crypto.wss.policy.Claims");
         if (var2 != null) {
            if (verbose) {
               Verbose.log((Object)"DKLabel: claims not null");
            }

            var1 = ClaimsBuilder.getClaimFromElt(var2, LABEL);
            if (var1 != null) {
               if (verbose) {
                  Verbose.log((Object)("DKLabel: label from claims is " + var1));
               }

               return var1;
            }
         }

         String var3 = getDKlabelFromWSSConfig(var0);
         if (var3 != null) {
            if (verbose) {
               Verbose.log((Object)("DKLabel: returning label configured via mbean from context handler: " + var3));
            }

            return var3;
         } else {
            if (verbose) {
               Verbose.log((Object)"DKLabel: returning default label: WS-SecureConversation");
            }

            return "WS-SecureConversation";
         }
      }
   }

   public static int getLengthFromContextHandler(ContextHandler var0) {
      if (verbose) {
         Verbose.banner("DKClaims.getLengthFromContextHandler");
      }

      Integer var1 = (Integer)var0.getValue("weblogic.wsee.wssc.dk.length");
      if (var1 != null) {
         if (verbose) {
            Verbose.log((Object)("DKLength: returning length from context handler: " + var1));
         }

         return var1;
      } else {
         Node var2 = (Node)var0.getValue("weblogic.xml.crypto.wss.policy.Claims");
         if (var2 != null) {
            if (verbose) {
               Verbose.log((Object)"DKLength: claims not null");
            }

            String var3 = ClaimsBuilder.getClaimFromElt(var2, LENGTH);
            if (var3 != null) {
               if (verbose) {
                  Verbose.log((Object)("DKLength: length from claims is " + var3));
               }

               return Integer.parseInt(var3);
            }

            if (var2 instanceof Element && "http://schemas.xmlsoap.org/ws/2005/02/sc/dk".equals(((Element)var2).getAttribute("TokenType"))) {
               if (verbose) {
                  Verbose.log((Object)"DKLength: returning 9.2 default length 16");
               }

               return 16;
            }
         }

         Integer var4 = getDKLengthFromWSSConfig(var0);
         if (var4 != null && var4 != -1) {
            if (verbose) {
               Verbose.log((Object)("DKLength: returning length configured via mbeanfrom context handler: " + var4));
            }

            return var4;
         } else {
            if (verbose) {
               Verbose.log((Object)"DKLength: returning default length: 32");
            }

            return 32;
         }
      }
   }

   private static Integer getDKLengthFromWSSConfig(ContextHandler var0) {
      WssPolicyContext var1 = getWSSPolicyContext(var0);
      return var1 != null ? var1.getWssConfiguration().getDKLength() : null;
   }

   private static String getDKlabelFromWSSConfig(ContextHandler var0) {
      WssPolicyContext var1 = getWSSPolicyContext(var0);
      return var1 != null ? var1.getWssConfiguration().getDKLabel() : null;
   }

   private static WssPolicyContext getWSSPolicyContext(ContextHandler var0) {
      WSSecurityInfo var1 = (WSSecurityInfo)var0.getValue("com.bea.contextelement.xml.SecurityInfo");
      if (var1 instanceof WSSecurityContext) {
         WSSecurityContext var2 = (WSSecurityContext)var1;
         MessageContext var3 = var2.getMessageContext();
         if (var3 != null) {
            return (WssPolicyContext)var3.getProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx");
         }
      }

      return null;
   }
}
