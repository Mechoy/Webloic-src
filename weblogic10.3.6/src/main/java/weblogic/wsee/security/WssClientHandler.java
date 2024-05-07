package weblogic.wsee.security;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;
import weblogic.kernel.KernelStatus;
import weblogic.security.SSL.TrustManager;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.security.bst.PolicyBSTCredentialProvider;
import weblogic.wsee.security.bst.StubPropertyBSTCredProv;
import weblogic.wsee.security.policy.SecurityPolicyAssertionHelper;
import weblogic.wsee.security.saml.SAML2CredentialProvider;
import weblogic.wsee.security.saml.SAMLCredentialProvider;
import weblogic.wsee.security.serviceref.ServiceRefBSTCredProv;
import weblogic.wsee.security.serviceref.ServiceRefTrustManager;
import weblogic.wsee.security.serviceref.ServiceRefUNTCredProv;
import weblogic.wsee.security.wss.SecurityPolicyDriver;
import weblogic.wsee.security.wss.SecurityPolicyException;
import weblogic.wsee.security.wss.SecurityPolicyValidator;
import weblogic.wsee.security.wssc.WSSCCredentialProviderFactory;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.api.WSSecurityFactory;
import weblogic.xml.crypto.wss.provider.CredentialProvider;

public class WssClientHandler extends WssHandler {
   private boolean autoReset = true;

   public WssClientHandler() {
   }

   public WssClientHandler(boolean var1) {
      this.autoReset = var1;
   }

   protected boolean processRequest(SOAPMessageContext var1) throws SOAPException, SecurityPolicyException, PolicyException, WSSecurityException {
      PolicyAlternative var2 = getRequestPolicyAlternative(var1);
      PolicyAlternative var3 = getResponsePolicyAlternative(var1);
      SecurityPolicyDriver var4 = null;
      var4 = this.getSecurityPolicyDriver(var1, var2);
      processOutbound(var2, var3, var4, var1);
      if (this.autoReset) {
         WSSecurityContext.getSecurityContext(var1).reset();
      }

      return true;
   }

   protected static void processOutbound(PolicyAlternative var0, PolicyAlternative var1, SecurityPolicyDriver var2, SOAPMessageContext var3) throws PolicyException, WSSecurityException, SecurityPolicyException {
      if (var0 != null || var1 != null) {
         try {
            var2.processOutbound(var0, var1, var3);
         } catch (MarshalException var5) {
            throw new WSSecurityException(var5);
         } catch (XMLEncryptionException var6) {
            throw new WSSecurityException(var6);
         }
      }
   }

   protected boolean processResponse(SOAPMessageContext var1) throws PolicyException, SOAPException, SecurityPolicyException, WSSecurityException {
      this.copyEndpointAddress(var1);
      PolicyAlternative var2 = getResponsePolicyAlternative(var1);
      SecurityPolicyValidator var3 = null;
      var3 = this.getSecurityPolicyValidator(var1);
      this.processInbound(var2, var3, var1);
      return true;
   }

   protected void fillCredentialProviders(SOAPMessageContext var1, WSSecurityContext var2, PolicyAlternative var3) throws WSSecurityException {
      TrustManager var4 = (TrustManager)var1.getProperty("weblogic.wsee.security.wss.TrustManager");
      if (var4 != null) {
         var2.setProperty("weblogic.wsee.security.wss.TrustManager", var4);
      } else if (KernelStatus.isServer()) {
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

      if (KernelStatus.isServer()) {
         List var7 = this.getServiceRefClientCredProvs();
         if (var7 != null) {
            var2.addCredentialProviders(var7);
         }
      }

      if (var6 == null) {
         var2.addCredentialProviders(this.getServerCredProvs(var3, var2));
      }

      addWSSCCredProviders(var2, var3);
   }

   private static void addWSSCCredProviders(WSSecurityContext var0, PolicyAlternative var1) {
      WSSCCredentialProviderFactory var2 = WSSCCredentialProviderFactory.getInstance();
      String[] var3 = (String[])SecurityPolicyAssertionHelper.getAllSupportedTokenTypes(var1).toArray(new String[0]);
      String[] var4 = var3;
      int var5 = var3.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String var7 = var4[var6];
         CredentialProvider var8 = var2.getCredentialProvider(var7);
         if (var8 != null) {
            var0.addCredentialProvider(var8);
         }
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
      var1.add(new SAMLCredentialProvider());
      var1.add(new SAML2CredentialProvider());
      return var1;
   }

   private List getServerCredProvs(PolicyAlternative var1, WSSecurityContext var2) throws WSSecurityException {
      PolicyBSTCredentialProvider var3 = null;

      try {
         var3 = new PolicyBSTCredentialProvider(var1, var2);
      } catch (Exception var5) {
         throw new WSSecurityException("Failed to setup server side credential provider: " + var5.getMessage(), var5);
      }

      ArrayList var4 = new ArrayList();
      var4.add(var3);
      return var4;
   }

   protected void processInbound(PolicyAlternative var1, SecurityPolicyValidator var2, SOAPMessageContext var3) throws WSSecurityException, SOAPException, SecurityPolicyException, PolicyException {
      try {
         if (hasSecurityHeader(var3)) {
            this.setupSecurityContext(var3, (PolicyAlternative)null);
            WSSecurityFactory.getInstance();
            WSSecurityFactory.unmarshalAndProcessSecurity(var3);
         }

         if (var1 != null) {
            var2.processInbound(var1, var3);
         }

      } catch (weblogic.xml.dom.marshal.MarshalException var5) {
         throw new WSSecurityException(var5, WSSConstants.FAILURE_INVALID);
      } catch (MarshalException var6) {
         throw new WSSecurityException(var6, WSSConstants.FAILURE_INVALID);
      } catch (XMLEncryptionException var7) {
         throw new WSSecurityException(var7, WSSConstants.FAILURE_VERIFY_OR_DECRYPT);
      }
   }
}
