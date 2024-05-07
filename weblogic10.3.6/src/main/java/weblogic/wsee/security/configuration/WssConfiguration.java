package weblogic.wsee.security.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.xml.rpc.handler.MessageContext;
import weblogic.kernel.KernelStatus;
import weblogic.management.configuration.WebserviceSecurityMBean;
import weblogic.wsee.policy.util.PolicySelectionPreference;
import weblogic.wsee.security.bst.ServerBSTCredentialProvider;
import weblogic.wsee.security.wssc.utils.WSSCCompatibilityUtil;
import weblogic.xml.crypto.wss.BinarySecurityTokenHandler;
import weblogic.xml.crypto.wss.ClientUNTHandler;
import weblogic.xml.crypto.wss.SecurityUtils;
import weblogic.xml.crypto.wss.UsernameTokenHandler;
import weblogic.xml.crypto.wss.WssPolicyContextHandler;
import weblogic.xml.crypto.wss.api.NonceValidator;
import weblogic.xml.crypto.wss.nonce.NonceValidatorFactory;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.crypto.wss11.internal.bst.ClientBSTHandler;

public class WssConfiguration {
   public static final String WSS_CONFIGURATION_CTX_PROP = "weblogic.wsee.security.wssconfig";
   private static final String CRED_PROVIDER_SYS_PROP = "weblogic.xml.crypto.wss.provider.CredentialProvider";
   private static final String TOKEN_HANDLER_SYS_PROP = "weblogic.xml.crypto.wss.provider.SecurityTokenHandler";
   public static final String NONCE_VALIDATOR = "weblogic.wsee.security.nonce.validator";
   private volatile boolean inited;
   private boolean isServer;
   private String mbeanName;
   private List tokenHandlers;
   private List credProviders;
   private List tokens;
   private WssPolicyContextHandler wssContextHandler;
   private Map classNameToInstanceMap;
   private WebserviceSecurityMBean wsm;
   private TimestampConfiguration timestampConfig;
   private static final boolean HEURISTIC_COMPATIBILITY = WSSCCompatibilityUtil.isHeuristicCompatibility();

   public WssConfiguration() {
      this((String)null, true);
   }

   public WssConfiguration(boolean var1) {
      this((String)null, var1);
   }

   public WssConfiguration(String var1) {
      this(var1, true);
   }

   public WssConfiguration(String var1, boolean var2) {
      this.inited = false;
      this.isServer = true;
      this.mbeanName = null;
      this.tokenHandlers = null;
      this.credProviders = null;
      this.tokens = null;
      this.wssContextHandler = new WssPolicyContextHandler();
      this.classNameToInstanceMap = new HashMap();
      if (var1 == null) {
         this.mbeanName = "default_wss";
      } else {
         this.mbeanName = var1;
      }

      if (var2) {
         this.isServer = true;
      } else {
         try {
            this.isServer = MBeanHelper.getDomainMBean() != null;
         } catch (AssertionError var4) {
            this.isServer = false;
         }
      }

   }

   public synchronized void init() throws WssConfigurationException {
      if (!this.inited) {
         this.tokenHandlers = this.createInstancesFromSysProp(System.getProperty("weblogic.xml.crypto.wss.provider.SecurityTokenHandler"));
         this.credProviders = this.createInstancesFromSysProp(System.getProperty("weblogic.xml.crypto.wss.provider.CredentialProvider"));
         if (this.isServer) {
            this.wsm = MBeanHelper.lookupWebserviceSecurityMBean(this.mbeanName);
            if (this.wsm == null && !this.mbeanName.equals("default_wss")) {
               throw new WssConfigurationException("WebserviceSecurityMBean \"" + this.mbeanName + "\" does not exist");
            }

            this.tokenHandlers.addAll(this.createInstancesFromMBean(MBeanHelper.getTokenHandlerClassNames(this.wsm)));
            this.credProviders.addAll(this.createInstancesFromMBean(MBeanHelper.getCredentialProviderClassNames(this.wsm)));
            this.fillContextHandler();
            this.initDefaultConfiguration();
            this.initCompatibility();
         } else {
            this.initClientBuiltInHandlers();
         }

         this.initTimestampConfiguration();
         NonceValidator var1 = NonceValidatorFactory.getInstance(System.getProperty("weblogic.xml.crypto.wss.provider.SecurityTokenHandler"), this.timestampConfig);
         this.inited = true;
      }

   }

   public void destroy() {
      if (this.wsm != null && this.timestampConfig != null) {
         this.wsm.getWebserviceTimestamp().removeBeanUpdateListener(this.timestampConfig);
      }

   }

   public TimestampConfiguration getTimestampConfig() {
      return this.timestampConfig;
   }

   public List getTokenHandlers() throws WssConfigurationException {
      this.init();
      return this.tokenHandlers;
   }

   public List getCredentialProviders() throws WssConfigurationException {
      this.init();
      return this.credProviders;
   }

   public List getSupprotedTokens() throws WssConfigurationException {
      this.init();
      if (this.tokens == null) {
         this.initTokens();
      }

      return this.tokens;
   }

   public CredentialProvider getCredentialProvider(String var1, String var2) throws WssConfigurationException {
      this.init();
      String var3 = MBeanHelper.getCredentialProviderClass(this.wsm, var1, var2);
      if (var3 != null) {
         Object var4 = createInstance(var3);
         if (var4 instanceof CredentialProvider) {
            return (CredentialProvider)var4;
         } else {
            throw new WssConfigurationException(var3 + " needs to implement weblogic.xml.crypto.wss.provider.CredentialProvider interface");
         }
      } else {
         return null;
      }
   }

   public SecurityTokenHandler getTokenHandler(String var1, String var2) throws WssConfigurationException {
      this.init();
      if (var2 != null) {
         Iterator var3 = this.tokenHandlers.iterator();

         while(var3.hasNext()) {
            SecurityTokenHandler var4 = (SecurityTokenHandler)var3.next();
            if (var4.getClass().getName().equals(var2)) {
               return var4;
            }
         }
      }

      String var11 = MBeanHelper.getTokenHandlerClass(this.wsm, var1, var2);
      if (var11 != null) {
         Object var13 = createInstance(var11);
         if (var13 instanceof SecurityTokenHandler) {
            return (SecurityTokenHandler)var13;
         } else {
            throw new WssConfigurationException(var11 + " needs to implement weblogic.xml.crypto.wss.provider.SecurityTokenHandler");
         }
      } else {
         if (var11 == null) {
            Iterator var12 = this.tokenHandlers.iterator();

            while(var12.hasNext()) {
               SecurityTokenHandler var5 = (SecurityTokenHandler)var12.next();
               String[] var6 = var5.getValueTypes();
               String[] var7 = var6;
               int var8 = var6.length;

               for(int var9 = 0; var9 < var8; ++var9) {
                  String var10 = var7[var9];
                  if (var10.equals(var1)) {
                     return var5;
                  }
               }
            }
         }

         return null;
      }
   }

   public void initTokens() throws WssConfigurationException {
      String[] var1 = MBeanHelper.getSecurityTokenClassNames(this.wsm);
      this.tokens = new ArrayList();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.tokens.add(createInstance(var1[var2]));
      }

   }

   public WssPolicyContextHandler getContextHandler() {
      return this.wssContextHandler;
   }

   public boolean isSignatureACLEnabled() {
      return MBeanHelper.getTokenHandlerPropertyBoolean(this.wsm, "UseX509ForIdentity", false);
   }

   public boolean validateHOKNeeded() {
      return MBeanHelper.getTokenHandlerPropertyBoolean(this.wsm, "EnableHoderOfKeyValidation", true);
   }

   public Long getSCTLifeTime() {
      return new Long((String)MBeanHelper.getCredentialProviderProperty(this.wsm, "TokenLifeTime", MBeanConstants.DEFAULT_SCT_TOKEN_LIFE_TIME));
   }

   public String getDKLabel() {
      return (String)MBeanHelper.getCredentialProviderProperty(this.wsm, "Label", (Object)null);
   }

   public Integer getDKLength() {
      return new Integer((String)MBeanHelper.getCredentialProviderProperty(this.wsm, "Length", "-1"));
   }

   public String getDefaultSTSURI() {
      return MBeanHelper.getDefaultCredentialProviderSTSURI(this.wsm);
   }

   public String getSTSURI(String var1) {
      return (String)MBeanHelper.getCredentialProviderProperty(this.wsm, var1, "StsUri", (Object)null);
   }

   public String getSTSPolicy(String var1) {
      return (String)MBeanHelper.getCredentialProviderProperty(this.wsm, var1, "StsPolicy", (Object)null);
   }

   public PolicySelectionPreference getPolicySelectionPreference() {
      PolicySelectionPreference var1 = null;
      if (this.wsm != null && this.wsm.getPolicySelectionPreference() != null) {
         var1 = new PolicySelectionPreference(this.wsm.getPolicySelectionPreference());
      }

      return var1;
   }

   public Boolean getSamlAttributesOnly(MessageContext var1) {
      String var2 = (String)var1.getProperty("oracle.contextelement.saml2.AttributeOnly");
      return Boolean.getBoolean(var2);
   }

   public String getCompatibilityPreference(MessageContext var1) {
      if (HEURISTIC_COMPATIBILITY) {
         return (String)var1.getProperty("weblogic.wsee.policy.compat.preference");
      } else {
         return this.wsm != null ? this.wsm.getCompatibilityPreference() : null;
      }
   }

   /** @deprecated */
   public String getCompatibilityPreference() {
      return this.wsm != null ? this.wsm.getCompatibilityPreference() : null;
   }

   public void setCompatibilityPreference(String var1, MessageContext var2) {
      if (HEURISTIC_COMPATIBILITY) {
         var2.setProperty("weblogic.wsee.policy.compat.preference", var1);
      } else if (this.wsm != null) {
         this.wsm.setCompatibilityPreference(var1);
      }

   }

   public String getCompatibilityOrderingPreference() {
      return this.wsm != null ? this.wsm.getCompatibilityOrderingPreference() : null;
   }

   public void setCompatibilityOrderingPreference(String var1) {
      if (this.wsm != null) {
         this.wsm.setCompatibilityOrderingPreference(var1);
      }

   }

   private void fillContextHandler() {
      this.wssContextHandler.addContextElement("com.bea.contextelement.wsee.credentialProviders", this.credProviders);
      this.wssContextHandler.addContextElement("com.bea.contextelement.wsee.tokenHandlers", this.tokenHandlers);
      MBeanHelper.fillConfigProperties(this.wsm, this.wssContextHandler);
   }

   private void initClientBuiltInHandlers() throws WssConfigurationException {
      this.tokenHandlers.add(ClientBSTHandler.getInstance());
      this.tokenHandlers.add(ClientUNTHandler.getInstance());
      this.tokenHandlers.add((SecurityTokenHandler)createInstance("weblogic.wsee.security.wssc.v200502.sct.SCTokenHandler"));
      this.tokenHandlers.add((SecurityTokenHandler)createInstance("weblogic.wsee.security.wssc.v200502.dk.DKTokenHandler"));
      this.tokenHandlers.add((SecurityTokenHandler)createInstance("weblogic.wsee.security.wssc.v13.sct.SCTokenHandler"));
      this.tokenHandlers.add((SecurityTokenHandler)createInstance("weblogic.wsee.security.wssc.v13.dk.DKTokenHandler"));
   }

   private void initDefaultConfiguration() throws WssConfigurationException {
      Object var1 = this.findInstance("weblogic.xml.crypto.wss.BinarySecurityTokenHandler");
      boolean var2;
      if (var1 != null && var1 instanceof BinarySecurityTokenHandler) {
         var2 = MBeanHelper.getTokenHandlerPropertyBoolean(this.wsm, "UseX509ForIdentity", false);
         this.wssContextHandler.addContextElement("UseX509ForIdentity", new Boolean(var2));
         if (var2 && !SecurityUtils.isX509Supported()) {
            throw new WssConfigurationException("Server is not configured to support assert x509 identity but property \" UseX509ForIdentity\" is true");
         }

         ((BinarySecurityTokenHandler)var1).setAuthorizationToken(var2);
      }

      var1 = this.findInstance("weblogic.xml.crypto.wss.UsernameTokenHandler");
      if (var1 != null && var1 instanceof UsernameTokenHandler) {
         var2 = MBeanHelper.getTokenHandlerPropertyBoolean(this.wsm, "UsePasswordDigest", false);
         if (var2 && !SecurityUtils.isPasswordDigestSupported()) {
            throw new WssConfigurationException("Server is not configured to support password digest but property \" UsePasswordDigest\" is true");
         }

         ((UsernameTokenHandler)var1).setPasswordDigestSupported(var2);
      }

      if (KernelStatus.isServer()) {
         Object var3 = this.findInstance("weblogic.wsee.security.bst.ServerBSTCredentialProvider");
         if (var3 != null && var3 instanceof ServerBSTCredentialProvider) {
            ((ServerBSTCredentialProvider)var3).initCredentials(this.wssContextHandler);
         }
      }

   }

   private void initTimestampConfiguration() {
      this.timestampConfig = new TimestampConfiguration(MBeanHelper.getTimestampConfig(this.wsm));
   }

   private void initCompatibility() {
   }

   private Object findInstance(String var1) {
      return this.classNameToInstanceMap.get(var1);
   }

   private List createInstancesFromSysProp(String var1) throws WssConfigurationException {
      ArrayList var2 = new ArrayList();
      if (var1 != null) {
         StringTokenizer var3 = new StringTokenizer(var1, ",");

         while(var3.hasMoreTokens()) {
            String var4 = var3.nextToken();
            this.addInstance(var2, var4, createInstance(var4));
         }
      }

      return var2;
   }

   private void addInstance(List var1, String var2, Object var3) {
      var1.add(var3);
      this.classNameToInstanceMap.put(var2, var3);
   }

   private List createInstancesFromMBean(String[] var1) throws WssConfigurationException {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         this.addInstance(var2, var1[var3], createInstance(var1[var3]));
      }

      return var2;
   }

   private static Object createInstance(String var0) throws WssConfigurationException {
      try {
         return Class.forName(var0).newInstance();
      } catch (InstantiationException var2) {
         throw new WssConfigurationException("Could not instantiate object of type " + var0, var2);
      } catch (IllegalAccessException var3) {
         throw new WssConfigurationException("Could not instantiate object of type " + var0, var3);
      } catch (ClassNotFoundException var4) {
         throw new WssConfigurationException("Could not instantiate object of type " + var0, var4);
      }
   }
}
