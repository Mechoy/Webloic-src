package weblogic.management.mbeans.custom;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.Vector;
import javax.management.InvalidAttributeValueException;
import javax.management.JMException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.ManagementLogger;
import weblogic.management.commo.StandardInterface;
import weblogic.management.configuration.ConfigurationError;
import weblogic.management.configuration.ConfigurationValidator;
import weblogic.management.configuration.SecurityConfigurationMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;
import weblogic.management.security.ProviderMBean;
import weblogic.management.security.RealmMBean;
import weblogic.security.Salt;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.ClearOrEncryptedService;

public final class SecurityConfiguration extends ConfigurationMBeanCustomizer {
   private boolean _initialized = false;
   private byte[] _salt;
   private byte[] _encryptedSecretKey;
   private static ClearOrEncryptedService encryptionService;
   private static final boolean DEBUG = false;

   private void debug(String var1) {
   }

   private static ClearOrEncryptedService getEncryptionService() {
      if (encryptionService == null) {
         encryptionService = new ClearOrEncryptedService(SerializedSystemIni.getEncryptionService());
      }

      return encryptionService;
   }

   public SecurityConfiguration(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void _postCreate() {
      this._initialized = true;
   }

   private SecurityConfigurationMBean getMyMBean() {
      return (SecurityConfigurationMBean)this.getMbean();
   }

   public RealmMBean createRealm(String var1) throws JMException {
      ConfigurationValidator.validateName(var1);
      SecurityConfigurationMBean var2 = this.getMyMBean();
      RealmMBean var3 = null;

      try {
         Class var4 = Class.forName("weblogic.management.security.RealmMBeanImpl");
         Constructor var5 = var4.getConstructor(DescriptorBean.class, Integer.TYPE);
         var3 = (RealmMBean)var5.newInstance(var2, new Integer(-1));
         var3.setName(var1);
         Method var6 = var2.getClass().getMethod("addRealm", RealmMBean.class);
         var6.invoke(var2, var3);
         return var3;
      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else if (var7 instanceof JMException) {
            throw (JMException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public RealmMBean[] findRealms() {
      return this.getMyMBean().getRealms();
   }

   public RealmMBean findDefaultRealm() {
      return this.getMyMBean().getDefaultRealm();
   }

   public RealmMBean findRealm(String var1) {
      return this.getMyMBean().lookupRealm(var1);
   }

   public RealmMBean getDefaultRealmInternal() {
      return this.getMyMBean().getDefaultRealm();
   }

   public void setDefaultRealmInternal(RealmMBean var1) {
      try {
         this.getMyMBean().setDefaultRealm(var1);
      } catch (InvalidAttributeValueException var3) {
         throw new IllegalArgumentException(var3.toString());
      }
   }

   public synchronized byte[] getSalt() {
      if (this._salt == null) {
         try {
            byte[] var1 = SerializedSystemIni.getSalt();
            if (var1 == null || var1.length < 1) {
               throw new ConfigurationError("Empty salt: " + var1);
            }

            this._salt = var1;
         } catch (Exception var2) {
            ManagementLogger.logExceptionInCustomizer(var2);
         }
      }

      return this._salt;
   }

   public synchronized byte[] getEncryptedSecretKey() {
      if (this._encryptedSecretKey == null) {
         try {
            if (!this.isAdmin() && !(this.getMbean() instanceof DescriptorBean)) {
               throw new ConfigurationError("EncryptedSecretKey null in config");
            }

            byte[] var1 = SerializedSystemIni.getEncryptedSecretKey();
            if (var1 == null || var1.length < 1) {
               throw new ConfigurationError("Empty encryptedSecretKey: " + var1);
            }

            this._encryptedSecretKey = var1;
         } catch (Exception var2) {
            ManagementLogger.logExceptionInCustomizer(var2);
         }
      }

      return this._encryptedSecretKey;
   }

   public byte[] getEncryptedAESSecretKey() {
      byte[] var1 = null;

      try {
         var1 = SerializedSystemIni.getEncryptedAESSecretKey();
      } catch (Exception var3) {
         ManagementLogger.logExceptionInCustomizer(var3);
      }

      return var1;
   }

   public byte[] generateCredential() {
      byte[] var1 = Salt.getRandomBytes(32);

      byte[] var2;
      try {
         var2 = getEncryptionService().encryptBytes(var1);
      } finally {
         Arrays.fill(var1, (byte)0);
      }

      return var2;
   }

   public void setCredentialGenerated(boolean var1) {
      if (this._initialized && var1) {
         try {
            this.getMyMBean().setCredentialEncrypted(this.generateCredential());
         } catch (InvalidAttributeValueException var3) {
            throw new RuntimeException(var3);
         }
      }

   }

   private void addMBean(Vector var1, StandardInterface var2) {
      if (var2 != null) {
         var1.add(var2);
      }

   }

   private void addMBeans(Vector var1, StandardInterface[] var2) {
      for(int var3 = 0; var2 != null && var3 < var2.length; ++var3) {
         this.addMBean(var1, var2[var3]);
      }

   }

   public ProviderMBean[] pre90getProviders() {
      Vector var1 = new Vector();
      RealmMBean[] var2 = this.getMyMBean().getRealms();

      for(int var3 = 0; var2 != null && var3 < var2.length; ++var3) {
         RealmMBean var4 = var2[var3];
         this.addMBeans(var1, var4.getAuditors());
         this.addMBeans(var1, var4.getAuthenticationProviders());
         this.addMBeans(var1, var4.getRoleMappers());
         this.addMBeans(var1, var4.getAuthorizers());
         this.addMBeans(var1, var4.getCredentialMappers());
         this.addMBeans(var1, var4.getCertPathProviders());
         this.addMBeans(var1, var4.getKeyStores());
         this.addMBean(var1, var4.getAdjudicator());
      }

      return (ProviderMBean[])((ProviderMBean[])var1.toArray(new ProviderMBean[0]));
   }
}
