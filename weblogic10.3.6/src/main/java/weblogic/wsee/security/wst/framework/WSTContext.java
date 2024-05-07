package weblogic.wsee.security.wst.framework;

import java.io.Serializable;
import java.security.Key;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.xml.rpc.handler.MessageContext;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.security.wst.helpers.TrustTokenHelper;
import weblogic.xml.crypto.wss.provider.SecurityToken;

public class WSTContext implements Serializable {
   private static final long serialVersionUID = -7901762602413666059L;
   private static final String WST_CONTEXT_PROP = "weblogic.wsee.security.wst.framework.WSTContext";
   private static final String WST_PREFIX = "wst";
   private static final String WSP_PREFIX = "wsp";
   private static final String WSA_PREFIX = "wsa";
   private static final String WSU_PREFIX = "wsu";
   private String action;
   private String appliesTo = null;
   private String tokenType = null;
   private Element appliesToElement = null;
   private String binarySecretType = null;
   private String context = null;
   private String computedKeyAlgorithm = null;
   private int keySize = 256;
   private long lifetimePeriod = 1800000L;
   private String symmetricKeyAlgorithm = "AES";
   private int symmetricKeyLength = 16;
   private String keyType = null;
   private SecurityToken onBehalfOfToken;
   private Element issuedTokenClaims = null;
   private boolean persistSession = false;
   private Calendar created;
   private Calendar expires;
   private transient MessageContext msgCtx;
   private HashMap<String, String> namespaces = new HashMap();
   private byte[] rstNonce;
   private transient Key symmetricKey = null;
   private String wsaNamespaceUri = null;
   private String wspNamespaceUri = null;
   private String wstNamespaceUri = null;
   private String wsuNamespaceUri = null;
   private boolean isWssp = false;
   private NormalizedExpression bootstrapPolicy = null;
   private NormalizedExpression outerPolicy = null;
   private String trustVersion = "http://docs.oasis-open.org/ws-sx/ws-trust/200512";
   private String stsUri = null;
   private String endpointUri = null;
   private String soapVersion = null;

   public HashMap<String, String> getNamespaces() {
      return this.namespaces;
   }

   public void setContext(String var1) {
      this.context = var1;
   }

   public String getContext() {
      return this.context;
   }

   public void setAppliesTo(String var1) {
      this.appliesTo = var1;
   }

   public String getAppliesTo() {
      return this.appliesTo;
   }

   public Element getAppliesToElement() {
      return this.appliesToElement;
   }

   public void setAppliesToElement(Element var1) {
      this.appliesToElement = var1;
   }

   public void setWstNamespaceURI(String var1) {
      this.wstNamespaceUri = var1;
      this.namespaces.put(var1, "wst");
   }

   public String getWstNamespaceURI() {
      return this.wstNamespaceUri;
   }

   public void setWspNamespaceURI(String var1) {
      this.wspNamespaceUri = var1;
      this.namespaces.put(var1, "wsp");
   }

   public String getWspNamespaceURI() {
      return this.wspNamespaceUri;
   }

   public void setWsaNamespaceURI(String var1) {
      this.wsaNamespaceUri = var1;
      this.namespaces.put(var1, "wsa");
   }

   public String getWsaNamespaceURI() {
      return this.wsaNamespaceUri;
   }

   public void setWsuNamespaceURI(String var1) {
      this.wsuNamespaceUri = var1;
      this.namespaces.put(var1, "wsu");
   }

   public String getWsuNamespaceURI() {
      return this.wsuNamespaceUri;
   }

   public void setTokenType(String var1) {
      this.tokenType = var1;
   }

   public String getTokenType() {
      return this.tokenType;
   }

   public void setComputedKeyAlgorithm(String var1) {
      this.computedKeyAlgorithm = var1;
   }

   public String getComputedKeyAlgorithm() {
      return this.computedKeyAlgorithm;
   }

   public void setBinarySecretType(String var1) {
      this.binarySecretType = var1;
   }

   public String getBinarySecretType() {
      return this.binarySecretType;
   }

   public void setSymmetricKeyAlgorithm(String var1) {
      this.symmetricKeyAlgorithm = var1;
   }

   public String getSymmetricKeyAlgorithm() {
      return this.symmetricKeyAlgorithm;
   }

   public void setAction(String var1) {
      this.action = var1;
   }

   public String getAction() {
      return this.action;
   }

   public void setSymmetricKeyLength(int var1) {
      this.symmetricKeyLength = var1;
   }

   public int getSymmetricKeyLength() {
      return this.symmetricKeyLength;
   }

   public void setLifetimePeriod(long var1) {
      this.lifetimePeriod = var1;
   }

   public long getLifetimePeriod() {
      return this.lifetimePeriod;
   }

   public void setKeySize(int var1) {
      this.keySize = var1;
   }

   public int getKeySize() {
      return this.keySize;
   }

   public void setSymmetricKey(Key var1) {
      this.symmetricKey = var1;
   }

   public Key getSymmetricKey() {
      return this.symmetricKey;
   }

   public void setPersistSession(boolean var1) {
      this.persistSession = var1;
   }

   public boolean isSessionPersisted() {
      return this.persistSession;
   }

   public void setCreated(Calendar var1) {
      this.created = var1;
   }

   public Calendar getCreated() {
      return this.created;
   }

   public void setExpires(Calendar var1) {
      this.expires = var1;
   }

   public Calendar getExpires() {
      return this.expires;
   }

   public void setRstNonce(byte[] var1) {
      this.rstNonce = var1;
   }

   public byte[] getRstNonce() {
      return this.rstNonce;
   }

   public void setMessageContext(MessageContext var1) {
      var1.setProperty("weblogic.wsee.security.wst.framework.WSTContext", this);
      this.msgCtx = var1;
   }

   public MessageContext getMessageContext() {
      return this.msgCtx;
   }

   public NormalizedExpression getBootstrapPolicy() {
      return this.bootstrapPolicy;
   }

   public void setBootstrapPolicy(NormalizedExpression var1) {
      this.bootstrapPolicy = var1;
   }

   public void setIssuedTokenClaims(Element var1) {
      this.issuedTokenClaims = var1;
   }

   public boolean hasIssuedTokenClaims() {
      return this.issuedTokenClaims != null;
   }

   public Element getIssuedTokenClaims() {
      return this.issuedTokenClaims;
   }

   public static WSTContext getWSTContext(MessageContext var0) {
      WSTContext var1 = (WSTContext)var0.getProperty("weblogic.wsee.security.wst.framework.WSTContext");
      if (var1 == null) {
         var1 = new WSTContext();
         var1.setMessageContext(var0);
         Map var2 = (Map)var0.getProperty("weblogic.wsee.invoke_properties");
         if (var2 != null) {
            var2.put("weblogic.wsee.security.wst.framework.WSTContext", var1);
         }
      }

      return var1;
   }

   public void setTrustVersion(String var1) {
      this.trustVersion = var1;
   }

   public String getTrustVersion() {
      return this.trustVersion;
   }

   public String getStsUri() {
      return this.stsUri;
   }

   public void setStsUri(String var1) {
      this.stsUri = var1;
   }

   public String getEndpointUri() {
      return this.endpointUri;
   }

   public void setEndpointUri(String var1) {
      this.endpointUri = var1;
   }

   public void initEndpoints(MessageContext var1) {
      String var2 = this.getAppliesTo();
      if (var2 == null) {
         var2 = (String)var1.getProperty("javax.xml.rpc.service.endpoint.address");
         this.setAppliesTo(var2);
      }

      this.setEndpointUri(var2);
      String var3 = null;
      if (!TrustTokenHelper.isWsscTokenType(this.tokenType)) {
         var3 = (String)var1.getProperty("weblogic.wsee.wst.saml.sts_endpoint_uri");
      }

      if (var3 == null) {
         var3 = (String)var1.getProperty("weblogic.wsee.wst.sts_endpoint_uri");
      }

      if (var3 == null) {
         var3 = var2;
      }

      this.setStsUri(var3);
   }

   public boolean isWssp() {
      return this.isWssp;
   }

   public void setWssp(boolean var1) {
      this.isWssp = var1;
   }

   public String getKeyType() {
      return this.keyType;
   }

   public void setKeyType(String var1) {
      this.keyType = var1;
   }

   public SecurityToken getOnBehalfOfToken() {
      return this.onBehalfOfToken;
   }

   public void setOnBehalfOfToken(SecurityToken var1) {
      this.onBehalfOfToken = var1;
   }

   public NormalizedExpression getOuterPolicy() {
      return this.outerPolicy;
   }

   public void setOuterPolicy(NormalizedExpression var1) {
      this.outerPolicy = var1;
   }

   public String getSoapVersion() {
      return this.soapVersion;
   }

   public void setSoapVersion(String var1) {
      this.soapVersion = var1;
   }
}
