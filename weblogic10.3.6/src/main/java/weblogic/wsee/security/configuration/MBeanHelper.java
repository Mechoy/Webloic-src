package weblogic.wsee.security.configuration;

import java.security.AccessController;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import weblogic.management.configuration.ConfigurationPropertyMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.WebserviceCredentialProviderMBean;
import weblogic.management.configuration.WebserviceSecurityConfigurationMBean;
import weblogic.management.configuration.WebserviceSecurityMBean;
import weblogic.management.configuration.WebserviceSecurityTokenMBean;
import weblogic.management.configuration.WebserviceTimestampMBean;
import weblogic.management.configuration.WebserviceTokenHandlerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.xml.crypto.wss.WssPolicyContextHandler;

public final class MBeanHelper {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static WebserviceSecurityMBean createDefaultWSMBean(DomainMBean var0) {
      WebserviceSecurityMBean var1 = var0.createWebserviceSecurity("default_wss");
      WebserviceTokenHandlerMBean var2 = createTokenHandlerMBean(var1, "default_ut_handler", "weblogic.xml.crypto.wss.UsernameTokenHandler", "ut", 1);
      WebserviceCredentialProviderMBean var3 = createCredentialProviderMBean(var1, "default_ut_cp", "weblogic.xml.crypto.wss.UNTCredentialProvider", "ut");
      WebserviceTokenHandlerMBean var4 = createTokenHandlerMBean(var1, "default_x509_handler", "weblogic.xml.crypto.wss.BinarySecurityTokenHandler", "x509", 2);
      ConfigurationPropertyMBean var5 = createConfigPropertyMBean(var4, "UseX509ForIdentity", "true", false);
      WebserviceCredentialProviderMBean var6 = createCredentialProviderMBean(var1, "default_x509_cp", "weblogic.wsee.security.bst.ServerBSTCredentialProvider", "x509");
      return var1;
   }

   public static WebserviceTokenHandlerMBean createTokenHandlerMBean(WebserviceSecurityMBean var0, String var1, String var2, String var3, int var4) {
      WebserviceTokenHandlerMBean var5 = var0.createWebserviceTokenHandler(var1);
      var5.setClassName(var2);
      var5.setTokenType(var3);
      var5.setHandlingOrder(var4);
      return var5;
   }

   public static WebserviceCredentialProviderMBean createCredentialProviderMBean(WebserviceSecurityMBean var0, String var1, String var2, String var3) {
      WebserviceCredentialProviderMBean var4 = var0.createWebserviceCredentialProvider(var1);
      var4.setClassName(var2);
      var4.setTokenType(var3);
      return var4;
   }

   public static ConfigurationPropertyMBean createConfigPropertyMBean(WebserviceSecurityConfigurationMBean var0, String var1, String var2, boolean var3) {
      ConfigurationPropertyMBean var4 = var0.createConfigurationProperty(var1);
      if (var3) {
         var4.setEncryptValueRequired(true);
         var4.setEncryptedValue(var2);
      } else {
         var4.setValue(var2);
      }

      return var4;
   }

   public static WebserviceSecurityMBean lookupWebserviceSecurityMBean(DomainMBean var0, String var1) {
      return var0 == null ? null : var0.lookupWebserviceSecurity(var1);
   }

   public static WebserviceSecurityMBean lookupWebserviceSecurityMBean(String var0) {
      return lookupWebserviceSecurityMBean(getDomainMBean(), var0);
   }

   public static String[] getTokenHandlerClassNames(WebserviceSecurityMBean var0) {
      OrderedList var1 = new OrderedList(MBeanConstants.BUILTIN_TOKEN_TYPES, MBeanConstants.BUILTIN_TOKEN_HANDLERS);
      if (var0 != null) {
         WebserviceTokenHandlerMBean[] var2 = var0.getWebserviceTokenHandlers();
         Arrays.sort(var2, new Comparator() {
            public int compare(Object var1, Object var2) {
               return ((WebserviceTokenHandlerMBean)var1).getHandlingOrder() - ((WebserviceTokenHandlerMBean)var2).getHandlingOrder();
            }
         });

         for(int var3 = 0; var3 < var2.length; ++var3) {
            var1.add(var2[var3].getTokenType(), var2[var3].getClassName());
         }
      }

      return var1.getValues();
   }

   public static String[] getTokenHandlerClassNames(DomainMBean var0, String var1) {
      return getTokenHandlerClassNames(lookupWebserviceSecurityMBean(var0, var1));
   }

   public static String[] getTokenHandlerClassNames(String var0) {
      return getTokenHandlerClassNames(getDomainMBean(), var0);
   }

   public static String[] getCredentialProviderClassNames(WebserviceSecurityMBean var0) {
      OrderedList var1 = new OrderedList(MBeanConstants.BUILTIN_TOKEN_TYPES, MBeanConstants.BUILTIN_CREDENTIAL_PROVIDERS);
      if (var0 != null) {
         WebserviceCredentialProviderMBean[] var2 = var0.getWebserviceCredentialProviders();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            var1.add(var2[var3].getTokenType(), var2[var3].getClassName());
         }
      }

      return var1.getValues();
   }

   public static String[] getCredentialProviderClassNames(DomainMBean var0, String var1) {
      return getCredentialProviderClassNames(lookupWebserviceSecurityMBean(var0, var1));
   }

   public static String[] getCredentialProviderClassNames(String var0) {
      return getCredentialProviderClassNames(getDomainMBean(), var0);
   }

   public static String[] getSecurityTokenClassNames(WebserviceSecurityMBean var0) {
      OrderedList var1 = new OrderedList(MBeanConstants.BUILTIN_TOKEN_TYPES, MBeanConstants.BUILTIN_TOKENS);
      if (var0 != null) {
         WebserviceSecurityTokenMBean[] var2 = var0.getWebserviceSecurityTokens();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            var1.add(var2[var3].getTokenType(), var2[var3].getClassName());
         }
      }

      return var1.getValues();
   }

   public static String getCredentialProviderClass(WebserviceSecurityMBean var0, String var1, String var2) {
      String var3 = var2;
      if (var0 != null) {
         WebserviceCredentialProviderMBean[] var4 = var0.getWebserviceCredentialProviders();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            WebserviceCredentialProviderMBean var6 = var4[var5];
            if (var6.getTokenType().equals(var1)) {
               var3 = var6.getClassName();
               break;
            }
         }
      }

      return var3;
   }

   public static String getTokenHandlerClass(WebserviceSecurityMBean var0, String var1, String var2) {
      String var3 = null;
      if (var0 != null) {
         WebserviceTokenHandlerMBean[] var4 = var0.getWebserviceTokenHandlers();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            WebserviceTokenHandlerMBean var6 = var4[var5];
            if (var6.getTokenType().equals(var1)) {
               var3 = var6.getClassName();
               break;
            }
         }
      }

      if (var2 != null && var3 == null) {
         var3 = var2;
      }

      if (var3 == null) {
         for(int var7 = 0; var7 < MBeanConstants.BUILTIN_TOKEN_TYPES.length; ++var7) {
            if (MBeanConstants.BUILTIN_TOKEN_TYPES[var7].equals(var1)) {
               var3 = MBeanConstants.BUILTIN_TOKEN_HANDLERS[var7];
            }
         }
      }

      return var3;
   }

   public static String getSecurityTokenClass(WebserviceSecurityMBean var0, String var1, String var2) {
      String var3 = var2;
      WebserviceSecurityTokenMBean[] var4 = var0.getWebserviceSecurityTokens();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         WebserviceSecurityTokenMBean var6 = var4[var5];
         if (var6.getTokenType().equals(var1)) {
            var3 = var6.getClassName();
            break;
         }
      }

      return var3;
   }

   public static Object getConfigPropertyValue(WebserviceSecurityConfigurationMBean var0, String var1) {
      ConfigurationPropertyMBean var2 = var0.lookupConfigurationProperty(var1);
      return var2 == null ? null : getConfigPropertyValue(var2);
   }

   public static Object getTokenHandlerProperty(WebserviceSecurityMBean var0, String var1, Object var2) {
      Object var3 = null;
      if (var0 != null) {
         WebserviceTokenHandlerMBean[] var4 = var0.getWebserviceTokenHandlers();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            var3 = getConfigPropertyValue(var4[var5], var1);
            if (var3 != null) {
               break;
            }
         }
      }

      if (var3 == null) {
         var3 = var2;
      }

      return var3;
   }

   public static boolean getTokenHandlerPropertyBoolean(WebserviceSecurityMBean var0, String var1, boolean var2) {
      Object var3 = getTokenHandlerProperty(var0, var1, (Object)null);
      return var3 == null ? var2 : Boolean.parseBoolean((String)var3);
   }

   public static Object getCredentialProviderProperty(WebserviceSecurityMBean var0, String var1, Object var2) {
      Object var3 = null;
      if (var0 != null) {
         WebserviceCredentialProviderMBean[] var4 = var0.getWebserviceCredentialProviders();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            var3 = getConfigPropertyValue(var4[var5], var1);
            if (var3 != null) {
               break;
            }
         }
      }

      if (var3 == null) {
         var3 = var2;
      }

      return var3;
   }

   public static Object getCredentialProviderProperty(WebserviceSecurityMBean var0, String var1, String var2, Object var3) {
      Object var4 = null;
      if (var0 != null) {
         WebserviceCredentialProviderMBean[] var5 = var0.getWebserviceCredentialProviders();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            if (var5[var6].getClassName().equals(var1)) {
               var4 = getConfigPropertyValue(var5[var6], var2);
               break;
            }
         }
      }

      if (var4 == null) {
         var4 = var3;
      }

      return var4;
   }

   public static WebserviceTimestampMBean getTimestampConfig(WebserviceSecurityMBean var0) {
      return var0 != null ? var0.getWebserviceTimestamp() : null;
   }

   public static String getDefaultCredentialProviderSTSURI(WebserviceSecurityMBean var0) {
      return var0 != null ? var0.getDefaultCredentialProviderSTSURI() : null;
   }

   static DomainMBean getDomainMBean() {
      RuntimeAccess var0 = ManagementService.getRuntimeAccess(kernelId);
      return var0 == null ? null : var0.getDomain();
   }

   static void fillConfigProperties(WebserviceSecurityMBean var0, WssPolicyContextHandler var1) {
      if (var0 != null) {
         fillConfigProperties((WebserviceSecurityConfigurationMBean[])var0.getWebserviceTokenHandlers(), var1);
         fillConfigProperties((WebserviceSecurityConfigurationMBean[])var0.getWebserviceCredentialProviders(), var1);
      }
   }

   private static void fillConfigProperties(WebserviceSecurityConfigurationMBean[] var0, WssPolicyContextHandler var1) {
      for(int var2 = 0; var2 < var0.length; ++var2) {
         ConfigurationPropertyMBean[] var3 = var0[var2].getConfigurationProperties();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            ConfigurationPropertyMBean var5 = var3[var4];
            var1.addContextElement(var5.getName(), getConfigPropertyValue(var5));
         }
      }

   }

   private static Object getConfigPropertyValue(ConfigurationPropertyMBean var0) {
      if (var0.isEncryptValueRequired()) {
         String var1 = var0.getEncryptedValue();
         return var1 != null && var1.length() > 0 ? var1.toCharArray() : null;
      } else {
         return var0.getValue();
      }
   }

   private static class OrderedList {
      private LinkedHashMap customVals = new LinkedHashMap();
      private String[] tokenTypes;
      private String[] defaultVals;

      public OrderedList(String[] var1, String[] var2) {
         this.tokenTypes = var1;
         this.defaultVals = var2;
      }

      public void add(String var1, String var2) {
         if (this.customVals.get(var1) != null) {
            throw new IllegalArgumentException("duplicate tokenType: " + var1 + " found.");
         } else {
            this.customVals.put(var1, var2);
         }
      }

      public String[] getValues() {
         LinkedHashMap var1 = new LinkedHashMap();
         var1.putAll(this.customVals);

         for(int var2 = 0; var2 < this.tokenTypes.length; ++var2) {
            if (this.customVals.get(this.tokenTypes[var2]) == null) {
               var1.put(this.tokenTypes[var2], this.defaultVals[var2]);
            }
         }

         return (String[])((String[])var1.values().toArray(new String[0]));
      }
   }
}
