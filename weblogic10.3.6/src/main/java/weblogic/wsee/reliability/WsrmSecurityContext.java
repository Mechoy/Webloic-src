package weblogic.wsee.reliability;

import com.sun.xml.ws.api.message.Packet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.connection.Connection;
import weblogic.wsee.deploy.WsdlAddressInfo;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.policy.deployment.PolicyURIs;
import weblogic.wsee.policy.deployment.ProviderRegistry;
import weblogic.wsee.policy.deployment.WsdlPolicySubject;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyContext;
import weblogic.wsee.reliability.policy.ReliabilityPolicyAssertionsFactory;
import weblogic.wsee.reliability2.sequence.CreateSequencePostSecurityTokenCallback;
import weblogic.wsee.security.policy.WssPolicyUtils;
import weblogic.wsee.security.policy.assertions.SecurityPolicyAssertionFactory;
import weblogic.wsee.security.wss.SecurityPolicySelectionHelper;
import weblogic.wsee.security.wssc.sct.SCCredential;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfoFactory;
import weblogic.wsee.security.wst.framework.WSTContext;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.dispatch.Dispatcher;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlDocumentation;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlPortType;
import weblogic.wsee.wsdl.WsdlService;

public class WsrmSecurityContext implements Externalizable {
   private static final long serialVersionUID = 662768244501161879L;
   private static final Logger LOGGER = Logger.getLogger(WsrmSecurityContext.class.getName());
   private static final String X509_POLICY = "Sign.xml";
   private static final String WSSC_POLICY = "WsscRmBootstrap.xml";
   private static final int HAS_SERVER_ENCRYPT_CERT = 1;
   private static final int HAS_SERVER_VERIFY_CERT = 2;
   private static final int HAS_SC_CREDENTIAL = 4;
   private static final int HAS_SERVICE_POLICY = 8;
   private static final int HAS_STS_ENDPOINT = 16;
   private static final int HAS_SAML_STS_ENDPOINT = 32;
   private static final int HAS_STS_ENCRYPT_CERT = 64;
   private static final int HAS_WST_CONTEXT = 128;
   private boolean secureWithX509 = false;
   private boolean secureWithWssp10Wssc = false;
   private boolean secureWithWssp12Wssc = false;
   private boolean secureWithWssp12Wssc13 = false;
   private boolean secureWithSSL = false;
   private X509Certificate serverEncryptCert = null;
   private X509Certificate serverVerifyCert = null;
   private X509Certificate stsEncryptCert = null;
   private SCCredential scCredential;
   private WSTContext wstCtx;
   private byte[] sslSessionId;
   private X509Certificate[] sslCertChain;
   private boolean forcedSslSessionId;
   private NormalizedExpression servicePolicy = null;
   private String stsEndpoint = null;
   private String samlStsEndpoint = null;
   private Object samlAttributes = null;
   private Object samlAttributesOnly = null;
   private transient NormalizedExpression cachedExpression = null;
   private transient CreateSequencePostSecurityTokenCallback jaxWsSecurityTokenCallback;
   private static final String[] SERIALIABLE_WST_PROPERTY_NAMES = new String[]{"weblogic.wsee.addressing.From", "weblogic.wsee.addressing.version", "weblogic.wsee.policy.compat.preference", "weblogic.weblogic.wsee.security.policy.WssPolicyCtx", "weblogic.wsee.security.wst_bootstrap_policy", "weblogic.wsee.wst.saml.sts_endpoint_uri", "weblogic.wsee.security.bst.stsEncryptCert", "weblogic.wsee.security.trust_soap_version", "weblogic.wsee.security.wst_onbehalfof_user", "weblogic.wsee.security.trust_version", "weblogic.wsee.security.message_age", "weblogic.wsee.wssc.sct.lifetime", "weblogic.wsee.security.bst.serverEncryptCert", "weblogic.wsee.security.bst.serverVerifyCert", "javax.xml.rpc.session.maintain", "weblogic.wsee.transport.jms.url", "javax.xml.rpc.security.auth.username", "javax.xml.rpc.security.auth.password", "weblogic.wsee.transport.jms.messagetype", "weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.JAX_WS_RUNTIME", "weblogic.wsee.security.wst.enforceAsyncTrustExchange", "javax.xml.rpc.service.endpoint.address"};
   private static final String[] NON_SERIALIABLE_WST_PROPERTY_NAMES = new String[]{"weblogic.wsee.security.wss.TrustManager", "weblogic.wsee.security.wst_transportinfo", "weblogic.wsee.security.wst_ssladapter", "weblogic.wsee.security.wssc.sct.scCredentialProactiveRequestor", "weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.ASYNC_CLIENT_FEATURE", "weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.SERVICE"};
   private boolean requireUpdateWSTCtx = false;

   public WsrmSecurityContext() {
   }

   public WsrmSecurityContext(WsrmSecurityContext var1) {
      this.secureWithX509 = var1.secureWithX509;
      this.secureWithWssp10Wssc = var1.secureWithWssp10Wssc;
      this.secureWithWssp12Wssc = var1.secureWithWssp12Wssc;
      this.secureWithWssp12Wssc13 = var1.secureWithWssp12Wssc13;
      this.secureWithSSL = var1.secureWithSSL;
      this.serverEncryptCert = var1.serverEncryptCert;
      this.serverVerifyCert = var1.serverVerifyCert;
      this.scCredential = var1.scCredential;
      this.wstCtx = var1.wstCtx;
      this.sslSessionId = var1.sslSessionId;
      this.sslCertChain = var1.sslCertChain;
      this.cachedExpression = var1.cachedExpression;
      this.servicePolicy = var1.servicePolicy;
      this.forcedSslSessionId = var1.forcedSslSessionId;
      this.jaxWsSecurityTokenCallback = var1.jaxWsSecurityTokenCallback;
      this.stsEndpoint = var1.stsEndpoint;
      this.stsEncryptCert = var1.stsEncryptCert;
      this.samlStsEndpoint = var1.samlStsEndpoint;
   }

   public WsrmSecurityContext(MessageContext var1) {
      try {
         this.setSecurityPolicyProperties(var1);
      } catch (PolicyException var3) {
         throw new JAXRPCException(var3);
      }

      this.setMessageContextProperties(var1);
      var1.setProperty("weblogic.wsee.wsrm.security.context", this);
   }

   public WsrmSecurityContext(NormalizedExpression var1, boolean var2) {
      try {
         this.setSecurityPolicyProperties(var1, var2);
      } catch (PolicyException var4) {
         throw new JAXRPCException(var4);
      }
   }

   private void setSecurityPolicyProperties(MessageContext var1) throws PolicyException {
      WsdlPolicySubject var2 = WssPolicyUtils.getWsdlPolicySubject(var1);
      if (var2 != null) {
         NormalizedExpression var3 = PolicyContext.getEndpointPolicy(var1);
         boolean var4 = var1.containsProperty("weblogic.wsee.wsrm.SequenceTransportSecurity");
         this.setSecurityPolicyProperties(var3, var4);
      }

   }

   private void setSecurityPolicyProperties(NormalizedExpression var1, boolean var2) throws PolicyException {
      SecurityPolicySelectionHelper var3;
      try {
         var3 = new SecurityPolicySelectionHelper(var1, true);
      } catch (Exception var5) {
         throw new PolicyException(var5.toString(), var5);
      }

      if (SecurityPolicyAssertionFactory.isWSTEnabled(var1)) {
         this.servicePolicy = var1;
         this.secureWithWssp10Wssc = true;
      } else if (var3.secureConversation13Requirement() > 0) {
         this.servicePolicy = var1;
         this.secureWithWssp12Wssc13 = true;
      } else if (var3.secureConversationRequirement() > 0) {
         this.servicePolicy = var1;
         this.secureWithWssp12Wssc = true;
      } else if (SecurityPolicyAssertionInfoFactory.hasTransportSecurityPolicy(var1) && (ReliabilityPolicyAssertionsFactory.hasSSLTLSPolicy(var1) || var2)) {
         this.servicePolicy = var1;
         this.secureWithSSL = true;
      } else if (SecurityPolicyAssertionFactory.hasSecurityPolicy(var1)) {
         this.secureWithX509 = true;
      }

   }

   private void setMessageContextProperties(MessageContext var1) {
      Object var2 = var1.getProperty("weblogic.wsee.security.bst.serverEncryptCert");
      if (var2 != null) {
         if (!(var2 instanceof X509Certificate)) {
            throw new JAXRPCException("weblogic.wsee.security.bst.serverEncryptCert must be an instanceof X509Certificate");
         }

         this.setServerEncryptCert((X509Certificate)var2);
      }

      Object var3 = var1.getProperty("weblogic.wsee.security.bst.serverVerifyCert");
      if (var3 != null) {
         if (!(var3 instanceof X509Certificate)) {
            throw new JAXRPCException("weblogic.wsee.security.bst.serverVerifyCert must be an instanceof X509Certificate");
         }

         this.setServerVerifyCert((X509Certificate)var3);
      }

      Object var4 = var1.getProperty("weblogic.wsee.security.bst.stsEncryptCert");
      if (var4 != null) {
         if (!(this.stsEncryptCert instanceof X509Certificate)) {
            throw new JAXRPCException("weblogic.wsee.security.bst.stsEncryptCert must be an instanceof X509Certificate");
         }

         this.setStsEncryptCert(this.stsEncryptCert);
      }

      Object var5 = var1.getProperty("weblogic.wsee.wst.sts_endpoint_uri");
   }

   public void setStsEndpoint(String var1) {
      this.stsEndpoint = var1;
   }

   public String getStsEndpoint() {
      return this.stsEndpoint;
   }

   public void setJaxWsSecurityTokenCallback(CreateSequencePostSecurityTokenCallback var1) {
      this.jaxWsSecurityTokenCallback = var1;
   }

   public void removeCreateSequencePostSecurityTokenCallback() {
      this.jaxWsSecurityTokenCallback = null;
   }

   public Map newInitializedMap() throws PolicyException {
      HashMap var1 = new HashMap();
      this.setMap(var1);
      return var1;
   }

   public void setMap(Map var1) throws PolicyException {
      var1.put("weblogic.weblogic.wsee.security.policy.WssPolicyCtx", WssPolicyUtils.getContext());
      if (this.isSecureWithWssc()) {
         if (this.getSCCredential() != null) {
            var1.put("weblogic.wsee.wssc.sct", this.getSCCredential());
         }

         if (this.getWSTContext() != null) {
            var1.put("weblogic.wsee.security.wst.framework.WSTContext", this.getWSTContext());
         }
      }

      if (this.getServerEncryptCert() != null) {
         var1.put("weblogic.wsee.security.bst.serverEncryptCert", this.getServerEncryptCert());
      }

      if (this.getStsEncryptCert() != null) {
         var1.put("weblogic.wsee.security.bst.stsEncryptCert", this.getStsEncryptCert());
      }

      if (this.getServerVerifyCert() != null) {
         var1.put("weblogic.wsee.security.bst.serverVerifyCert", this.getServerVerifyCert());
      }

      if (this.jaxWsSecurityTokenCallback != null) {
         var1.put(CreateSequencePostSecurityTokenCallback.PROPERTY_NAME, this.jaxWsSecurityTokenCallback);
      }

      if (this.stsEndpoint != null) {
         var1.put("weblogic.wsee.wst.sts_endpoint_uri", this.stsEndpoint);
      }

      if (this.samlStsEndpoint != null) {
         var1.put("weblogic.wsee.wst.saml.sts_endpoint_uri", this.samlStsEndpoint);
      }

      if (this.samlAttributes != null) {
         var1.put("weblogic.wsee.security.saml.attributies", this.samlAttributes);
      }

      if (this.samlAttributesOnly != null) {
         var1.put("oracle.contextelement.saml2.AttributeOnly", this.samlAttributesOnly);
      }

   }

   public boolean update(Map var1) {
      boolean var2 = false;
      if (var1 == null) {
         return var2;
      } else {
         if (this.isSecureWithWssc()) {
            SCCredential var3 = (SCCredential)var1.get("weblogic.wsee.wssc.sct");
            if (this.getSCCredential() == null) {
               this.setSCCredential(var3);
               var2 = true;
            } else if (!this.getSCCredential().getExpires().equals(var3.getExpires())) {
               this.setSCCredential(var3);
               var2 = true;
            }

            WSTContext var4 = (WSTContext)var1.get("weblogic.wsee.security.wst.framework.WSTContext");
            if (var4 != null && !var4.equals(this.getWSTContext())) {
               this.setWSTContext(var4);
               var2 = true;
            }

            this.updateWSTContext(var1);
         }

         X509Certificate var5 = (X509Certificate)var1.get("weblogic.wsee.security.bst.serverEncryptCert");
         if (var5 != null && this.getServerEncryptCert() == null) {
            this.setServerEncryptCert(var5);
            var2 = true;
         }

         var5 = (X509Certificate)var1.get("weblogic.wsee.security.bst.stsEncryptCert");
         if (var5 != null && this.getStsEncryptCert() == null) {
            this.setServerEncryptCert(var5);
            var2 = true;
         }

         var5 = (X509Certificate)var1.get("weblogic.wsee.security.bst.serverVerifyCert");
         if (var5 != null && this.getServerVerifyCert() == null) {
            this.setServerVerifyCert(var5);
            var2 = true;
         }

         String var6 = (String)var1.get("weblogic.wsee.wst.sts_endpoint_uri");
         if (var6 != null && (this.stsEndpoint == null || !var6.equals(this.stsEndpoint))) {
            this.stsEndpoint = var6;
            var2 = true;
         }

         var6 = (String)var1.get("weblogic.wsee.wst.sts_endpoint_uri");
         if (var6 != null && (this.samlStsEndpoint == null || !var6.equals(this.stsEndpoint))) {
            this.samlStsEndpoint = var6;
            var2 = true;
         }

         return var2;
      }
   }

   public boolean update(Packet var1) {
      if (var1 == null) {
         return false;
      } else {
         Map var2 = var1.invocationProperties;
         return this.update(var2);
      }
   }

   public NormalizedExpression getSecurityPolicy() throws PolicyException {
      if (this.servicePolicy != null) {
         return this.servicePolicy;
      } else {
         if (this.cachedExpression == null) {
            if (this.secureWithX509) {
               this.cachedExpression = WssPolicyUtils.getPolicy("Sign.xml");
            } else if (this.secureWithWssp10Wssc) {
               this.cachedExpression = WssPolicyUtils.getPolicy("WsscRmBootstrap.xml");
            }
         }

         return this.cachedExpression;
      }
   }

   public boolean isSecure() {
      return this.secureWithWssp10Wssc || this.secureWithX509 || this.secureWithWssp12Wssc || this.secureWithWssp12Wssc13;
   }

   public SCCredential getSCCredential() {
      return this.scCredential;
   }

   public void setSCCredential(SCCredential var1) {
      this.scCredential = var1;
   }

   public WSTContext getWSTContext() {
      return this.wstCtx;
   }

   public void setWSTContext(WSTContext var1) {
      this.wstCtx = var1;
   }

   public byte[] getSSLSessionId() {
      return this.sslSessionId;
   }

   public void setSSLSessionId(byte[] var1) {
      this.sslSessionId = var1;
   }

   public void setSSLCertChain(X509Certificate[] var1) {
      this.sslCertChain = var1;
   }

   public X509Certificate[] getSSLCertChain() {
      return this.sslCertChain;
   }

   public boolean isForcedSSLSessionId() {
      return this.forcedSslSessionId;
   }

   public void setForcedSSLSessionId(boolean var1) {
      this.forcedSslSessionId = var1;
   }

   /** @deprecated */
   public boolean isSecureWithX509() {
      return this.secureWithX509;
   }

   public void setSecureWithX509(boolean var1) {
      this.secureWithX509 = var1;
   }

   public boolean isSecureWithWssc() {
      return this.secureWithWssp10Wssc || this.secureWithWssp12Wssc || this.secureWithWssp12Wssc13;
   }

   public boolean isSecureWithWssp10Wssc() {
      return this.secureWithWssp10Wssc;
   }

   public boolean isSecureWithWssp12Wssc() {
      return this.secureWithWssp12Wssc;
   }

   public boolean isSecureWithWssp12Wssc13() {
      return this.secureWithWssp12Wssc13;
   }

   public boolean isSecureWithSSL() {
      return this.secureWithSSL;
   }

   public void setSecureWithSSL(boolean var1) {
      this.secureWithSSL = var1;
   }

   public void setServerEncryptCert(X509Certificate var1) {
      this.serverEncryptCert = var1;
   }

   public X509Certificate getServerEncryptCert() {
      return this.serverEncryptCert;
   }

   public void setStsEncryptCert(X509Certificate var1) {
      this.stsEncryptCert = var1;
   }

   public X509Certificate getStsEncryptCert() {
      return this.stsEncryptCert;
   }

   public void setServerVerifyCert(X509Certificate var1) {
      this.serverVerifyCert = var1;
   }

   public X509Certificate getServerVerifyCert() {
      return this.serverVerifyCert;
   }

   public String getSamlStsEndpoint() {
      return this.samlStsEndpoint;
   }

   public void setSamlStsEndpoint(String var1) {
      this.samlStsEndpoint = var1;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeUTF("10.3+");
      var1.writeBoolean(this.secureWithX509);
      var1.writeBoolean(this.secureWithWssp10Wssc);
      var1.writeBoolean(this.secureWithWssp12Wssc);
      var1.writeBoolean(this.secureWithWssp12Wssc13);
      var1.writeBoolean(this.secureWithSSL);
      if (this.sslSessionId != null) {
         var1.writeInt(this.sslSessionId.length);
         var1.write(this.sslSessionId);
      } else {
         var1.writeInt(0);
      }

      var1.writeBoolean(this.forcedSslSessionId);
      var1.writeObject(this.sslCertChain);
      int var2 = 0;
      if (this.serverEncryptCert != null) {
         var2 |= 1;
      }

      if (this.serverVerifyCert != null) {
         var2 |= 2;
      }

      if (this.scCredential != null) {
         var2 |= 4;
      }

      if (this.wstCtx != null) {
         var2 |= 128;
      }

      if (this.servicePolicy != null) {
         var2 |= 8;
      }

      if (this.stsEndpoint != null) {
         var2 |= 16;
      }

      if (this.samlStsEndpoint != null) {
         var2 |= 32;
      }

      if (this.stsEncryptCert != null) {
         var2 |= 64;
      }

      var1.writeInt(var2);
      if (this.serverEncryptCert != null) {
         var1.writeObject(this.serverEncryptCert);
      }

      if (this.serverVerifyCert != null) {
         var1.writeObject(this.serverVerifyCert);
      }

      if (this.scCredential != null) {
         var1.writeObject(this.scCredential);
      }

      if (this.wstCtx != null) {
         this.writeWSTContext(var1, this.wstCtx);
      }

      if (this.servicePolicy != null) {
         var1.writeObject(this.servicePolicy);
      }

      if (this.stsEndpoint != null) {
         var1.writeObject(this.stsEndpoint);
      }

      if (this.stsEncryptCert != null) {
         var1.writeObject(this.stsEncryptCert);
      }

      if (this.samlStsEndpoint != null) {
         var1.writeObject(this.samlStsEndpoint);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      String var2 = var1.readUTF();
      boolean var3 = false;
      boolean var4 = false;
      boolean var5 = false;
      if (!"9.2".equals(var2)) {
         if ("9.5".equals(var2)) {
            var3 = true;
         } else if ("10.3".equals(var2)) {
            var4 = true;
         } else {
            if (!"10.3+".equals(var2)) {
               throw new IOException("Unknown/unsupported version found: " + var2);
            }

            var5 = true;
         }
      }

      this.secureWithX509 = var1.readBoolean();
      this.secureWithWssp10Wssc = var1.readBoolean();
      if (!var3 && !var4 && !var5) {
         this.secureWithWssp12Wssc = false;
      } else {
         this.secureWithWssp12Wssc = var1.readBoolean();
      }

      int var6;
      if (var4 || var5) {
         this.secureWithWssp12Wssc13 = var1.readBoolean();
         this.secureWithSSL = var1.readBoolean();
         var6 = var1.readInt();
         if (var6 > 0) {
            this.sslSessionId = new byte[var6];
            var1.readFully(this.sslSessionId);
         }

         this.forcedSslSessionId = var1.readBoolean();
      }

      if (var5) {
         this.sslCertChain = (X509Certificate[])((X509Certificate[])var1.readObject());
      }

      var6 = var1.readInt();
      if ((var6 & 1) != 0) {
         this.serverEncryptCert = (X509Certificate)var1.readObject();
      }

      if ((var6 & 2) != 0) {
         this.serverVerifyCert = (X509Certificate)var1.readObject();
      }

      if ((var6 & 4) != 0) {
         this.scCredential = (SCCredential)var1.readObject();
      }

      if ((var6 & 128) != 0) {
         this.wstCtx = this.readWSTContext(var1);
      }

      if ((var6 & 8) != 0) {
         if (this.secureWithWssp12Wssc13) {
            try {
               ProviderRegistry.getTheRegistry();
            } catch (Exception var8) {
               throw new IOException("Error doing policy assertion registration: " + var8.toString());
            }
         }

         this.servicePolicy = (NormalizedExpression)var1.readObject();
      }

      if ((var6 & 16) != 0) {
         this.stsEndpoint = (String)var1.readObject();
      }

      if ((var6 & 64) != 0) {
         this.stsEncryptCert = (X509Certificate)var1.readObject();
      }

      if ((var6 & 32) != 0) {
         this.samlStsEndpoint = (String)var1.readObject();
      }

   }

   private void writeWSTContext(ObjectOutput var1, WSTContext var2) throws IOException {
      var1.writeObject(var2);
      MessageContext var3 = var2.getMessageContext();
      SoapMessageContext var4 = (SoapMessageContext)var3;
      var1.writeBoolean(var4.isSoap12());
      List var5 = (List)var3.getProperty("weblogic.wsee.security.wss.CredentialProviderList");
      this.writeCredentialProviders(var1, var5);
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.log(Level.FINE, "Writes credential provider list while serializing WST context: " + var5);
      }

      Object var6 = var3.getProperty("weblogic.wsee.security.trust_claim");
      var1.writeBoolean(var6 != null);
      if (var6 != null) {
         if (var6 instanceof Serializable) {
            var1.writeObject(var6);
         } else {
            var1.writeObject(new Object());
         }
      }

      Dispatcher var7 = var4.getDispatcher();
      QName var8 = null;
      if (var7 != null && var7.getWsdlPort() != null) {
         var8 = var7.getWsdlPort().getName();
      }

      var1.writeBoolean(var8 != null);
      if (var8 != null) {
         var1.writeObject(var8);
      }

      for(int var9 = 0; var9 < SERIALIABLE_WST_PROPERTY_NAMES.length; ++var9) {
         Object var10 = var3.getProperty(SERIALIABLE_WST_PROPERTY_NAMES[var9]);
         var1.writeBoolean(var10 != null);
         if (var10 != null) {
            var1.writeObject(var10);
         }
      }

   }

   private void writeCredentialProviders(ObjectOutput var1, List var2) throws IOException {
      ArrayList var3 = new ArrayList();
      if (var2 != null && !var2.isEmpty()) {
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            Object var5 = var4.next();
            if (var5 instanceof Serializable) {
               var3.add(var5);
               if (LOGGER.isLoggable(Level.FINEST)) {
                  LOGGER.log(Level.FINEST, "Detects a serializable credential provider while serializing WST context: " + var5);
               }
            }
         }
      }

      var1.writeBoolean(!var3.isEmpty());
      if (!var3.isEmpty()) {
         var1.writeObject(var3);
      }

   }

   private WSTContext readWSTContext(ObjectInput var1) throws ClassNotFoundException, IOException {
      WSTContext var2 = (WSTContext)var1.readObject();
      boolean var3 = var1.readBoolean();
      SoapMessageContext var4 = new SoapMessageContext(var3);
      var2.setMessageContext(var4);
      if (var1.readBoolean()) {
         var4.setProperty("weblogic.wsee.security.wss.CredentialProviderList", var1.readObject());
      }

      if (var1.readBoolean()) {
         var4.setProperty("weblogic.wsee.security.trust_claim", var1.readObject());
      }

      if (var1.readBoolean()) {
         Dispatcher var5 = this.readDispatcher(var1);
         var4.setDispatcher(var5);
      }

      for(int var7 = 0; var7 < SERIALIABLE_WST_PROPERTY_NAMES.length; ++var7) {
         if (var1.readBoolean()) {
            Object var6 = var1.readObject();
            var4.setProperty(SERIALIABLE_WST_PROPERTY_NAMES[var7], var6);
         }
      }

      this.requireUpdateWSTCtx = true;
      return var2;
   }

   private Dispatcher readDispatcher(ObjectInput var1) throws ClassNotFoundException, IOException {
      final QName var2 = (QName)var1.readObject();
      return new Dispatcher() {
         public WsdlBindingOperation getBindingOperation() {
            return null;
         }

         public Connection getConnection() {
            return null;
         }

         public WlMessageContext getContext() {
            return null;
         }

         public Map getInParams() {
            return null;
         }

         public WsdlOperation getOperation() {
            return null;
         }

         public QName getOperationName() {
            return null;
         }

         public Map getOutParams() {
            return null;
         }

         public QName getPortName() {
            return var2;
         }

         public QName getServiceName() {
            return null;
         }

         public WsMethod getWsMethod() {
            return null;
         }

         public WsPort getWsPort() {
            return null;
         }

         public WsdlPort getWsdlPort() {
            return new WsdlPort() {
               public WsdlBinding getBinding() {
                  return null;
               }

               public WsdlDefinitions getDefinitions() {
                  return null;
               }

               public QName getName() {
                  return var2;
               }

               public PolicyURIs getPolicyUris() {
                  return null;
               }

               public WsdlAddressInfo.PortAddress getPortAddress() {
                  return null;
               }

               public WsdlPortType getPortType() {
                  return null;
               }

               public WsdlService getService() {
                  return null;
               }

               public String getTransport() {
                  return null;
               }

               public void setPolicyUris(PolicyURIs var1) {
               }

               public void setPortAddress(WsdlAddressInfo.PortAddress var1) {
               }

               public WsdlExtension getExtension(String var1) {
                  return null;
               }

               public List<WsdlExtension> getExtensionList(String var1) {
                  return null;
               }

               public Map<String, List<WsdlExtension>> getExtensions() {
                  return null;
               }

               public void putExtension(WsdlExtension var1) {
               }

               public WsdlDocumentation getDocumentation() {
                  return null;
               }
            };
         }

         public boolean isSOAP12() {
            return false;
         }

         public void setInParams(Map var1) {
         }

         public void setOutParams(Map var1) {
         }

         public void setWsPort(WsPort var1) {
         }
      };
   }

   private void updateWSTContext(Map var1) {
      if (this.requireUpdateWSTCtx) {
         if (this.wstCtx != null && this.wstCtx.getMessageContext() != null) {
            MessageContext var2 = this.wstCtx.getMessageContext();

            for(int var3 = 0; var3 < NON_SERIALIABLE_WST_PROPERTY_NAMES.length; ++var3) {
               if (var1.containsKey(NON_SERIALIABLE_WST_PROPERTY_NAMES[var3])) {
                  var2.setProperty(NON_SERIALIABLE_WST_PROPERTY_NAMES[var3], var1.get(NON_SERIALIABLE_WST_PROPERTY_NAMES[var3]));
               }

               if (LOGGER.isLoggable(Level.FINEST)) {
                  LOGGER.log(Level.FINEST, "Updates a non-serializable property for WST original message context after un-serializing WST context: " + NON_SERIALIABLE_WST_PROPERTY_NAMES[var3] + " = [" + var1.get(NON_SERIALIABLE_WST_PROPERTY_NAMES[var3]) + "]");
               }
            }

            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.log(Level.FINE, "Updates all non-serializable properties for original WST message context after un-serializing WST context");
            }

            this.requireUpdateWSTCtx = false;
         }
      }
   }
}
