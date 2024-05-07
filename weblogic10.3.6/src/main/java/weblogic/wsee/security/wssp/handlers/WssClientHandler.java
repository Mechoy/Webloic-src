package weblogic.wsee.security.wssp.handlers;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;
import weblogic.kernel.KernelStatus;
import weblogic.security.SSL.TrustManager;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyContext;
import weblogic.wsee.security.bst.StubPropertyBSTCredProv;
import weblogic.wsee.security.saml.PKISAMLCredentialProvider;
import weblogic.wsee.security.saml.SAML2CredentialProvider;
import weblogic.wsee.security.saml.SAMLTrustCredentialProvider;
import weblogic.wsee.security.serviceref.ServiceRefBSTCredProv;
import weblogic.wsee.security.serviceref.ServiceRefTrustManager;
import weblogic.wsee.security.serviceref.ServiceRefUNTCredProv;
import weblogic.wsee.security.wss.SecurityPolicyArchitect;
import weblogic.wsee.security.wss.SecurityPolicyException;
import weblogic.wsee.security.wssc.WSSCCredentialProviderFactory;
import weblogic.wsee.security.wssc.base.sct.SCCredentialProactiveRequestor;
import weblogic.wsee.security.wssp.IssuedTokenAssertion;
import weblogic.wsee.security.wssp.ProtectionTokenAssertion;
import weblogic.wsee.security.wssp.SecureConversationTokenAssertion;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfo;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfoFactory;
import weblogic.wsee.security.wssp.SupportingTokensAssertion;
import weblogic.wsee.security.wssp.SymmetricBindingInfo;
import weblogic.wsee.security.wssp.TokenAssertion;
import weblogic.wsee.security.wssp.X509TokenAssertion;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss11.EncryptedKeyCredentialProviderFactory;
import weblogic.xml.crypto.wss11.internal.WSS11Context;
import weblogic.xml.crypto.wss11.internal.WSS11Factory;
import weblogic.xml.crypto.wss11.internal.enckey.EncryptedKeyToken;

public class WssClientHandler extends WssHandler {
   private static final boolean verbose = Verbose.isVerbose(WssClientHandler.class);
   private SCCredentialProactiveRequestor sccProactiveRequestor;
   private boolean autoReset = true;

   protected boolean processRequest(SOAPMessageContext var1) throws PolicyException, SOAPException, WSSecurityException {
      boolean var8;
      try {
         NormalizedExpression var2 = PolicyContext.getRequestEffectivePolicy(var1);
         if (var2 == null || null == var2.getPolicyAlternatives()) {
            boolean var13 = true;
            return var13;
         }

         SecurityPolicyArchitect var3 = this.getSecurityPolicyDriver(var1);
         processOutbound(var2, var3, var1);
         WSS11Context var4 = (WSS11Context)WSSecurityContext.getSecurityContext(var1);
         String[] var5 = var4.getSignatureValues();
         int var6 = var4.getRequestPolicyIdx();
         EncryptedKeyToken var7 = this.getEncryptedKeyToken(var4);
         if (this.autoReset) {
            WSSecurityContext.getSecurityContext(var1).reset();
         }

         var4.setRequestPolicyIdx(var6);
         var4.addPreviousMessageSignatureValues(var5);
         if (var7 != null) {
            var4.addKeyProvider(var7.getKeyProvider());
            var4.addSecurityToken(var7);
         }

         this.reportOutboundWSSSuccessToWsspStats(this.getWsspStats(var1), var1);
         var8 = true;
      } finally {
         this.populateSCCProactiveRequestor(var1);
      }

      return var8;
   }

   private EncryptedKeyToken getEncryptedKeyToken(WSS11Context var1) throws WSSecurityException {
      EncryptedKeyToken var2 = null;
      List var3 = var1.getSecurityTokens("http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey");
      if (var3.size() > 0) {
         var2 = (EncryptedKeyToken)var3.get(0);
      }

      return var2;
   }

   protected static void processOutbound(NormalizedExpression var0, SecurityPolicyArchitect var1, SOAPMessageContext var2) throws PolicyException, WSSecurityException {
      if (var0 != null) {
         try {
            var1.processRequestOutbound(var0, var2);
         } catch (MarshalException var4) {
            throw new WSSecurityException(var4);
         } catch (XMLEncryptionException var5) {
            throw new WSSecurityException(var5);
         } catch (SecurityPolicyException var6) {
            var6.printStackTrace();
            throw new WSSecurityException(var6);
         }
      }
   }

   protected boolean processResponse(SOAPMessageContext var1) throws PolicyException, SOAPException, WSSecurityException, SecurityPolicyException {
      this.copyEndpointAddress(var1);
      NormalizedExpression var2 = PolicyContext.getResponseEffectivePolicy(var1);
      this.processInbound(var2, var1);
      return true;
   }

   protected void processInbound(NormalizedExpression var1, SOAPMessageContext var2) throws WSSecurityException, SOAPException, SecurityPolicyException, PolicyException {
      try {
         if (hasSecurityHeader(var2)) {
            this.setupSecurityContext(var2);

            try {
               var2.setProperty("weblogic.wsee.security.wssc.needCheckSCTExpiration", "true");
               if (var2.getProperty("weblogic.wsee.security.wssc.checkingSCTExpiration") == null) {
                  var2.setProperty("weblogic.wsee.security.wssc.checkingSCTExpiration", "tolerantCheckingSCTExpiration");
               }

               WSS11Factory.getInstance();
               WSS11Factory.unmarshalAndProcessSecurity(var2);
            } finally {
               var2.setProperty("weblogic.wsee.security.wssc.needCheckSCTExpiration", "false");
            }
         }

      } catch (weblogic.xml.dom.marshal.MarshalException var8) {
         throw new WSSecurityException(var8, WSSConstants.FAILURE_INVALID);
      }
   }

   protected void fillCredentialProviders(SOAPMessageContext var1, WSSecurityContext var2) throws WSSecurityException {
      boolean var3 = KernelStatus.isServer();
      TrustManager var4 = (TrustManager)var1.getProperty("weblogic.wsee.security.wss.TrustManager");
      if (var4 != null) {
         var2.setProperty("weblogic.wsee.security.wss.TrustManager", var4);
      } else if (var3) {
         var2.setProperty("weblogic.wsee.security.wss.TrustManager", ServiceRefTrustManager.getInstance());
      }

      List var5 = (List)var1.getProperty("weblogic.wsee.security.wss.CredentialProviderList");
      if (var5 != null) {
         var2.setCredentialProviders(var5);
      }

      CredentialProvider var6 = this.getStubPropCredProv(var1);
      if (var6 != null) {
         var2.addCredentialProvider(var6);
      }

      if (var3) {
         List var7 = this.getServiceRefClientCredProvs();
         if (var7 != null) {
            var2.addCredentialProviders(var7);
         }
      }

      addWSSCCredProviders(var2, var1);
   }

   private static void addWSSCCredProviders(WSSecurityContext var0, SOAPMessageContext var1) throws WSSecurityException {
      if (verbose) {
         Verbose.log((Object)"Adding WSS cred providers");
      }

      try {
         NormalizedExpression var2 = PolicyContext.getRequestEffectivePolicy(var1);
         Set var3 = var2.getPolicyAlternatives();
         if (var3 != null) {
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
               PolicyAlternative var5 = (PolicyAlternative)var4.next();
               SecurityPolicyAssertionInfo var6 = SecurityPolicyAssertionInfoFactory.getSecurityPolicyAssertionInfo(var5);
               if (var6 == null) {
                  return;
               }

               SecureConversationTokenAssertion var7 = null;
               X509TokenAssertion var8 = null;
               IssuedTokenAssertion var9 = null;
               SymmetricBindingInfo var10 = var6.getSymmetricBindingInfo();
               if (var10 != null) {
                  ProtectionTokenAssertion var19 = var10.getProtectionTokenAssertion();
                  if (var19 == null) {
                     return;
                  }

                  var7 = var19.getSecureConversationTokenAssertion();
                  var8 = var19.getX509TokenAssertion();
               } else {
                  SupportingTokensAssertion var11 = var6.getSupportingTokensAssertion();
                  if (var11 == null) {
                     return;
                  }

                  ArrayList var12 = new ArrayList();
                  var12.addAll(var11.getSupportingTokens());
                  var12.addAll(var11.getSignedSupportingTokens());
                  var12.addAll(var11.getEncryptedSupportingTokens());
                  var12.addAll(var11.getSignedEncryptedSupportingTokens());
                  var12.addAll(var11.getEndorsingSupportingTokens());
                  var12.addAll(var11.getSignedEndorsingSupportingTokens());
                  Iterator var13 = var12.iterator();

                  while(var13.hasNext()) {
                     TokenAssertion var14 = (TokenAssertion)var13.next();
                     if (var14 instanceof SecureConversationTokenAssertion) {
                        var7 = (SecureConversationTokenAssertion)var14;
                     } else if (var14 instanceof X509TokenAssertion) {
                        var8 = (X509TokenAssertion)var14;
                     } else if (var14 instanceof IssuedTokenAssertion) {
                        var9 = (IssuedTokenAssertion)var14;
                     }
                  }
               }

               String[] var20 = null;
               if (var7 != null) {
                  var20 = var7.getTokenType();
               } else {
                  if (verbose) {
                     Verbose.log((Object)("X509TokenAssertion is: " + var8));
                  }

                  CredentialProvider var21 = null;
                  if (var8 != null) {
                     boolean var22 = false;
                     if (null != var6.getWsTrustOptions()) {
                        var22 = var6.getWsTrustOptions().isWst13();
                     }

                     var20 = var8.getDerivedKeyTokenType(var22);
                     var21 = EncryptedKeyCredentialProviderFactory.getEncryptedKeyCredentialProvider();
                  }

                  if (var9 != null) {
                     if (verbose) {
                        Verbose.log((Object)("IssuedTokenAssertion is: " + var9));
                     }

                     String var25 = var9.getDkTokenType();
                     if (verbose) {
                        Verbose.log((Object)("tokenType is: " + var25));
                     }

                     if (null != var20 && var20.length != 0) {
                        HashSet var26 = new HashSet(Arrays.asList(var20));
                        var26.add(var25);
                        var20 = (String[])((String[])var26.toArray());
                     } else {
                        var20 = new String[]{var25};
                     }

                     var21 = EncryptedKeyCredentialProviderFactory.getEncryptedKeyCredentialProvider();
                  }

                  if (verbose) {
                     Verbose.log((Object)("CP for EK is: " + var21));
                  }

                  if (var21 != null) {
                     var0.addCredentialProvider(var21);
                  }
               }

               if (var20 == null) {
                  return;
               }

               int var15;
               String var16;
               String[] var27;
               int var28;
               if (verbose) {
                  StringBuffer var23 = new StringBuffer("tokenTypes:");
                  var27 = var20;
                  var28 = var20.length;

                  for(var15 = 0; var15 < var28; ++var15) {
                     var16 = var27[var15];
                     var23.append(" " + var16);
                  }

                  Verbose.log((Object)("tokenTypes is: " + var23.toString()));
               }

               WSSCCredentialProviderFactory var24 = WSSCCredentialProviderFactory.getInstance();
               var27 = var20;
               var28 = var20.length;

               for(var15 = 0; var15 < var28; ++var15) {
                  var16 = var27[var15];
                  CredentialProvider var17 = var24.getCredentialProvider(var16);
                  if (var17 != null) {
                     var0.addCredentialProvider(var17);
                  }
               }
            }

         }
      } catch (PolicyException var18) {
         throw new WSSecurityException(var18);
      }
   }

   private CredentialProvider getStubPropCredProv(SOAPMessageContext var1) throws WSSecurityException {
      X509Certificate var2 = (X509Certificate)var1.getProperty("weblogic.wsee.security.bst.serverEncryptCert");
      X509Certificate var3 = (X509Certificate)var1.getProperty("weblogic.wsee.security.bst.serverVerifyCert");
      if (var2 == null) {
         if (var3 == null) {
            return null;
         } else {
            throw new WSSecurityException("Invalid to set server's verify certificate but no encryption certificate.");
         }
      } else {
         return new StubPropertyBSTCredProv(var2, var3);
      }
   }

   private List getServiceRefClientCredProvs() {
      ArrayList var1 = new ArrayList();
      var1.add(new ServiceRefUNTCredProv());
      var1.add(new ServiceRefBSTCredProv());
      var1.add(new PKISAMLCredentialProvider());
      var1.add(new SAML2CredentialProvider());
      var1.add(new SAMLTrustCredentialProvider());
      return var1;
   }

   private void populateSCCProactiveRequestor(SOAPMessageContext var1) {
      SCCredentialProactiveRequestor var2 = (SCCredentialProactiveRequestor)var1.getProperty("weblogic.wsee.security.wssc.sct.scCredentialProactiveRequestor");
      if (var2 != null && var2.verify(var1)) {
         this.sccProactiveRequestor = var2;
      }

   }

   public void destroy() {
      if (this.sccProactiveRequestor != null) {
         this.sccProactiveRequestor.dispose();
         this.sccProactiveRequestor = null;
      }

   }
}
