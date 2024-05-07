package weblogic.wsee.security.wssc.base.sct;

import java.util.Iterator;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import org.w3c.dom.NodeList;
import weblogic.kernel.KernelStatus;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.security.wssc.sct.SCCredential;
import weblogic.wsee.security.wssc.sct.SCTStore;
import weblogic.wsee.security.wssp.ProtectionTokenAssertion;
import weblogic.wsee.security.wssp.SecureConversationTokenAssertion;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfo;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfoFactory;
import weblogic.wsee.security.wssp.SymmetricBindingInfo;
import weblogic.wsee.security.wst.faults.WSTFaultException;
import weblogic.wsee.security.wst.faults.WSTFaultUtil;
import weblogic.wsee.security.wst.framework.TrustSoapClient;
import weblogic.wsee.security.wst.framework.WSTContext;
import weblogic.wsee.server.WsLifeCycleEvent;
import weblogic.wsee.server.WsLifeCycleListenerRegistry;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class SCTHelperBase {
   public static final SCTokenBase performWSSCHandshake(SoapMessageContext var0, SCTokenHandlerBase var1) {
      NormalizedExpression var2 = (NormalizedExpression)var0.getProperty("weblogic.wsee.policy.effectiveRequestPolicy");
      NormalizedExpression var3 = (NormalizedExpression)var0.getProperty("weblogic.wsee.policy.effectiveResponsePolicy");

      try {
         if (var0.getProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx") == null) {
            WssPolicyContext var4 = new WssPolicyContext(KernelStatus.isServer());
            var0.setProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx", var4);
         }

         WSTContext var17 = WSTContext.getWSTContext(var0);
         var17.initEndpoints(var0);
         if (SecurityPolicyAssertionInfoFactory.hasWsTrustPolicy(var2)) {
            var17.setWssp(true);
            Iterator var5 = var2.getPolicyAlternatives().iterator();

            while(var5.hasNext()) {
               SecurityPolicyAssertionInfo var6 = SecurityPolicyAssertionInfoFactory.getSecurityPolicyAssertionInfo((PolicyAlternative)var5.next());
               if (var6 != null) {
                  SymmetricBindingInfo var7 = var6.getSymmetricBindingInfo();
                  if (var7 != null) {
                     ProtectionTokenAssertion var8 = var7.getProtectionTokenAssertion();
                     if (var8 != null) {
                        SecureConversationTokenAssertion var9 = var8.getSecureConversationTokenAssertion();
                        NormalizedExpression var10 = var9.getNormalizedBootstrapPolicy();
                        if (var10 != null) {
                           var17.setBootstrapPolicy(var10);
                        }
                     }
                  }
               }
            }
         }

         var17.setAction(var1.getSCT_RST_ACTION());
         var17.setTokenType(var1.getSCT_VALUE_TYPE());
         String var18 = var17.getWstNamespaceURI();
         if (var18 == null) {
            if (var1.getXMLNS_WSC().equals("http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512")) {
               var17.setWstNamespaceURI("http://docs.oasis-open.org/ws-sx/ws-trust/200512");
            } else {
               if (!var1.getXMLNS_WSC().equals("http://schemas.xmlsoap.org/ws/2005/02/sc")) {
                  throw new IllegalArgumentException("Unexpected WS-SecureConversation version namespace '" + var1.getXMLNS_WSC() + "'.  This needs to be fixed !");
               }

               var17.setWstNamespaceURI("http://schemas.xmlsoap.org/ws/2005/02/trust");
            }
         }

         TrustSoapClient var19 = new TrustSoapClient(var17);
         SCCredential var20 = ClientSCCredentialProviderBase.createSCCredential(var19, var17, var1);
         WsLifeCycleListenerRegistry.getInstance().onEvent(WsLifeCycleEvent.WSRM_SEND_RST_BEFORE_RSTR);
         String var21 = SCCredentialProviderBase.getPhysicalStoreNameFromMessageContext(var17.getMessageContext());
         SCTStore.addToClient(var20, false, var21);
         var0.setProperty("weblogic.wsee.wssc.sct", var20);
         SCTokenBase var22 = (SCTokenBase)var19.generateTrustToken(var0, var1.getCANNED_POLICY_INCLUDE_SCT_FOR_IDENTITY(), var1.getSCT_VALUE_TYPE());
         return var22;
      } catch (WSTFaultException var15) {
         var15.printStackTrace();
         WSTFaultUtil.raiseFault(var15);
      } finally {
         if (var2 != null) {
            var0.setProperty("weblogic.wsee.policy.effectiveRequestPolicy", var2);
         }

         if (var3 != null) {
            var0.setProperty("weblogic.wsee.policy.effectiveResponsePolicy", var3);
         }

      }

      return null;
   }

   public static final String getCredentialIdentifier(SOAPMessageContext var0, String var1, String var2) {
      SOAPHeader var3 = null;

      try {
         var3 = var0.getMessage().getSOAPHeader();
      } catch (SOAPException var7) {
         throw new JAXRPCException(var7);
      }

      if (var3 == null) {
         return null;
      } else {
         NodeList var4 = var3.getElementsByTagNameNS(var1, var2);
         if (var4.getLength() <= 0) {
            return null;
         } else {
            try {
               return DOMUtils.getTextData(var4.item(0));
            } catch (DOMProcessingException var6) {
               throw new JAXRPCException(var6);
            }
         }
      }
   }
}
