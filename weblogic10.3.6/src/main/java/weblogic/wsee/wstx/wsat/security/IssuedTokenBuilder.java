package weblogic.wsee.wstx.wsat.security;

import com.sun.xml.ws.util.DOMUtil;
import java.io.InputStream;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.ws.WebServiceException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyFinder;
import weblogic.wsee.security.wssp.ProtectionTokenAssertion;
import weblogic.wsee.security.wssp.SecureConversationTokenAssertion;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfo;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfoFactory;
import weblogic.wsee.security.wssp.SymmetricBindingInfo;
import weblogic.wsee.security.wst.framework.TrustProcessor;
import weblogic.wsee.security.wst.framework.TrustProcessorFactory;
import weblogic.wsee.security.wst.framework.TrustRequestor;
import weblogic.wsee.security.wst.framework.TrustRequestorFactory;
import weblogic.wsee.security.wst.framework.WSTContext;
import weblogic.wsee.wstx.wsc.common.types.BaseIdentifier;
import weblogic.wsee.wstx.wsc.common.types.CoordinationContextIF;

public class IssuedTokenBuilder {
   private static final DebugLogger logger = DebugLogger.getDebugLogger("DebugWSAT");
   private String trustVersion;
   private String tokenType;
   private String binarySecretType = "none";
   private int keySize = 256;
   private WebServiceException cachedException;

   public IssuedTokenBuilder(String var1, String var2, int var3) {
      this.trustVersion = var1;
      this.tokenType = var2;
      this.keySize = var3;
   }

   public IssuedTokenBuilder(String var1) {
      try {
         InputStream var2 = Thread.currentThread().getContextClassLoader().getResourceAsStream(var1);
         if (var2 == null) {
            throw new WebServiceException("fail to load policy file:\t" + var1);
         }

         NormalizedExpression var3 = null;

         try {
            var3 = PolicyFinder.readPolicyFromStream(var1, var2).normalize();
         } catch (PolicyException var13) {
            if (logger.isDebugEnabled()) {
               logger.debug("fail to load policy file:\t" + var1, var13);
            }

            throw new WebServiceException("fail to load policy file:\t" + var1);
         }

         if (var3 != null && var3.getPolicyAlternative() != null) {
            SecurityPolicyAssertionInfo var4 = SecurityPolicyAssertionInfoFactory.getSecurityPolicyAssertionInfo(var3.getPolicyAlternative());
            if (var4 != null && var4.getSymmetricBindingInfo() != null) {
               SymmetricBindingInfo var5 = var4.getSymmetricBindingInfo();
               ProtectionTokenAssertion var6 = var5.getProtectionTokenAssertion();
               SecureConversationTokenAssertion var7 = var6.getSecureConversationTokenAssertion();
               if (var7 != null) {
                  String var8 = var7.getSctTokenType();
                  String var9 = var7.isSC200502SecurityContextToken() ? "http://schemas.xmlsoap.org/ws/2005/02/trust" : "http://docs.oasis-open.org/ws-sx/ws-trust/200512";
                  NormalizedExpression var10 = var7.getNormalizedBootstrapPolicy();
                  if (var10 == null || var10.getPolicyAlternative() == null) {
                     throw new WebServiceException("no Bootstrap Policy for SecureConversationToken Assertion found in:\t" + var1);
                  }

                  SecurityPolicyAssertionInfo var11 = SecurityPolicyAssertionInfoFactory.getSecurityPolicyAssertionInfo(var10.getPolicyAlternative());
                  int var12 = var11.getAlgorithmSuiteInfo().getMinSymKeyLength();
                  this.tokenType = var8;
                  this.trustVersion = var9;
                  this.keySize = var12;
                  return;
               }

               throw new WebServiceException("no SecureConversationToken Assertion found in:\t" + var1);
            }

            throw new WebServiceException("no SymmetricBindingInfo found in:\t" + var1);
         }

         throw new WebServiceException("no policy alternative found in:\t" + var1);
      } catch (Throwable var14) {
         if (var14 instanceof WebServiceException) {
            this.cachedException = (WebServiceException)var14;
         } else {
            this.cachedException = new WebServiceException(var14);
         }
      }

   }

   public static IssuedTokenBuilder v12() {
      if (IssuedTokenBuilder.V12BuilderLazyHolder.builder.cachedException != null) {
         throw IssuedTokenBuilder.V12BuilderLazyHolder.builder.cachedException;
      } else {
         return IssuedTokenBuilder.V12BuilderLazyHolder.builder;
      }
   }

   public static IssuedTokenBuilder v13() {
      if (IssuedTokenBuilder.V13BuilderLazyHolder.builder.cachedException != null) {
         throw IssuedTokenBuilder.V13BuilderLazyHolder.builder.cachedException;
      } else {
         return IssuedTokenBuilder.V13BuilderLazyHolder.builder;
      }
   }

   IssuedTokenBuilder keySize(int var1) {
      this.keySize = var1;
      return this;
   }

   public Element buildFromContext(CoordinationContextIF var1) {
      Element var2 = DOMUtil.createDom().createElement("dumy");

      try {
         BaseIdentifier var3 = var1.getIdentifier();
         JAXBElement var4 = new JAXBElement(var3.getQName(), var3.getDelegate().getClass(), var3.getDelegate());
         var1.getJAXBRIContext().createMarshaller().marshal(var4, var2);
      } catch (JAXBException var5) {
         throw new WebServiceException(var5);
      }

      return this.buildFromAppliesToElement((Element)var2.getFirstChild());
   }

   public Element buildFromAppliesToElement(Element var1) {
      try {
         TrustProcessorFactory var2 = TrustProcessorFactory.getInstance();
         TrustProcessor var3 = var2.getProcessor("/Issue");
         WSTContext var4 = new WSTContext();
         SoapMessageContext var5 = new SoapMessageContext();
         var5.setProperty("weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.JAX_WS_RUNTIME", "true");
         var4.setMessageContext(var5);
         var4.setTrustVersion(this.trustVersion);
         var4.setTokenType(this.tokenType);
         var4.setAppliesToElement(var1);
         var4.setKeySize(this.keySize);
         var4.setBinarySecretType(this.binarySecretType);
         TrustRequestorFactory var6 = TrustRequestorFactory.getInstance();
         TrustRequestor var7 = var6.createTrustRequestor(var4.getTrustVersion());
         Node var8 = var7.newRequestSecurityToken(var4);
         Node var9 = var3.processRequestSecurityToken(var8, var4);
         Element var10 = DOMUtil.createDom().createElementNS(this.trustVersion, "IssuedTokens");
         var10.appendChild(var10.getOwnerDocument().importNode(var9, true));
         return var10;
      } catch (Exception var11) {
         if (logger.isDebugEnabled()) {
            logger.debug("fail to create IssuedToken!", var11);
         }

         throw new WebServiceException(var11);
      }
   }

   private static class V13BuilderLazyHolder {
      private static final IssuedTokenBuilder builder = new IssuedTokenBuilder(ClientPolicyFeatureBuilder.V11().getIssuedTokenPolicy());
   }

   private static class V12BuilderLazyHolder {
      private static final IssuedTokenBuilder builder = new IssuedTokenBuilder(ClientPolicyFeatureBuilder.V10().getIssuedTokenPolicy());
   }
}
