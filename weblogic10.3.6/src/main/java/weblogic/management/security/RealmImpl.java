package weblogic.management.security;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.util.Vector;
import javax.management.InvalidAttributeValueException;
import javax.management.JMException;
import javax.management.MBeanException;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.RequiredModelMBean;
import weblogic.descriptor.DescriptorClassLoader;
import weblogic.management.configuration.ConfigurationValidator;
import weblogic.management.provider.beaninfo.BeanInfoAccessFactory;
import weblogic.management.security.audit.AuditorMBean;
import weblogic.management.security.authentication.AuthenticationProviderMBean;
import weblogic.management.security.authentication.PasswordValidatorMBean;
import weblogic.management.security.authorization.AdjudicatorMBean;
import weblogic.management.security.authorization.AuthorizerMBean;
import weblogic.management.security.authorization.RoleMapperMBean;
import weblogic.management.security.credentials.CredentialMapperMBean;
import weblogic.management.security.pk.CertPathProviderMBean;
import weblogic.management.security.pk.KeyStoreMBean;
import weblogic.management.utils.ErrorCollectionException;
import weblogic.security.SecurityMessagesTextFormatter;
import weblogic.security.internal.RealmValidatorImpl;

public class RealmImpl extends BaseMBeanImpl {
   private boolean isInConstructor = true;
   private static final String MBEAN_SUFFIX = "MBean";

   public RealmImpl(ModelMBean var1) throws MBeanException {
      super(var1);
   }

   protected RealmImpl(RequiredModelMBean var1) throws MBeanException {
      super(var1);
   }

   public void validate() throws ErrorCollectionException {
      (new RealmValidatorImpl()).validate(this.getRealm());
   }

   private boolean constructed() {
      return !this.isInConstructor;
   }

   public void _postCreate() {
      this.isInConstructor = false;
   }

   private RealmMBean getRealm() {
      try {
         return (RealmMBean)this.getProxy();
      } catch (MBeanException var2) {
         throw new AssertionError(var2);
      }
   }

   private RealmContainer getRealmContainer() {
      return (RealmContainer)((RealmContainer)this.getRealm().getParentBean());
   }

   private boolean amDefaultRealm() {
      RealmMBean var1 = this.getRealm();
      if (var1 == null) {
         throw new AssertionError("Realm customizer cannot get its RealmMBean");
      } else {
         String var2 = var1.getName();
         if (var2 != null && var2.length() >= 1) {
            RealmMBean var3 = this.getRealmContainer().getDefaultRealmInternal();
            return var3 == null ? false : var2.equals(var3.getName());
         } else {
            throw new AssertionError("RealmMBean has a null or empty name");
         }
      }
   }

   public boolean isDefaultRealm() {
      return this.constructed() ? this.amDefaultRealm() : false;
   }

   public void setDefaultRealm(boolean var1) throws InvalidAttributeValueException {
      if (this.constructed()) {
         if (this.amDefaultRealm()) {
            if (!var1) {
               this.getRealmContainer().setDefaultRealmInternal((RealmMBean)null);
            }
         } else if (var1) {
            this.getRealmContainer().setDefaultRealmInternal(this.getRealm());
         }
      }

   }

   public String getCompatibilityObjectName() {
      String var1 = "Security:Name=";
      String var2 = var1 + this.getRealm().getName();
      return var2;
   }

   private String[] getProviderTypes(String var1) {
      String[] var2 = BeanInfoAccessFactory.getBeanInfoAccess().getSubtypes(var1 + "MBean");
      Vector var3 = new Vector();

      for(int var4 = 0; var2 != null && var4 < var2.length; ++var4) {
         String var5 = var2[var4];
         BeanInfo var6 = BeanInfoAccessFactory.getBeanInfoAccess().getBeanInfoForInterface(var5, false, (String)null);
         if (var6 != null) {
            BeanDescriptor var7 = var6.getBeanDescriptor();
            if (var7 == null) {
               throw new AssertionError("Could not get BeanDescriptor for provider type " + var5);
            }

            Boolean var8 = (Boolean)var7.getValue("abstract");
            boolean var9 = var8 != null ? var8 : false;
            if (!var9) {
               if (!var5.endsWith("MBean")) {
                  throw new AssertionError("Provider type " + var5 + " should have ended with " + "MBean");
               }

               String var10 = var5.substring(0, var5.length() - "MBean".length());
               if (!var10.startsWith("weblogic.security.providers.realmadapter.")) {
                  var3.add(var10);
               }
            }
         }
      }

      return var3.size() > 0 ? (String[])((String[])var3.toArray(new String[0])) : null;
   }

   public String[] getPasswordValidatorTypes() {
      return this.getProviderTypes("weblogic.management.security.authentication.PasswordValidator");
   }

   public String[] getAuditorTypes() {
      return this.getProviderTypes("weblogic.management.security.audit.Auditor");
   }

   public String[] getAuthenticationProviderTypes() {
      return this.getProviderTypes("weblogic.management.security.authentication.AuthenticationProvider");
   }

   public String[] getRoleMapperTypes() {
      return this.getProviderTypes("weblogic.management.security.authorization.RoleMapper");
   }

   public String[] getAuthorizerTypes() {
      return this.getProviderTypes("weblogic.management.security.authorization.Authorizer");
   }

   public String[] getAdjudicatorTypes() {
      return this.getProviderTypes("weblogic.management.security.authorization.Adjudicator");
   }

   public String[] getCredentialMapperTypes() {
      return this.getProviderTypes("weblogic.management.security.credentials.CredentialMapper");
   }

   public String[] getCertPathProviderTypes() {
      return this.getProviderTypes("weblogic.management.security.pk.CertPathProvider");
   }

   public String[] getKeyStoreTypes() {
      return this.getProviderTypes("weblogic.management.security.pk.KeyStore");
   }

   private ProviderMBean createProvider(String var1, String[] var2, String var3, boolean var4, String var5, ProviderCreator var6) throws JMException, ClassNotFoundException {
      boolean var7 = false;

      for(int var8 = 0; !var7 && var2 != null && var8 < var2.length; ++var8) {
         if (var2[var8].equals(var3)) {
            var7 = true;
         }
      }

      if (var7) {
         Class var16 = Class.forName(var3 + "MBean", true, DescriptorClassLoader.getClassLoader());
         ClassLoader var15 = Thread.currentThread().getContextClassLoader();

         ProviderMBean var10;
         try {
            Thread.currentThread().setContextClassLoader(DescriptorClassLoader.getClassLoader());
            var10 = var4 ? var6.createProvider(var16, var5) : var6.createProvider(var16);
         } finally {
            Thread.currentThread().setContextClassLoader(var15);
         }

         return var10;
      } else {
         String var14 = "";

         for(int var9 = 0; var2 != null && var9 < var2.length; ++var9) {
            if (var9 > 0) {
               var14 = var14 + ",";
            }

            var14 = var14 + var2[var9];
         }

         throw new IllegalArgumentException(SecurityMessagesTextFormatter.getInstance().getUnknownSecurityProviderTypeError(var3, var1, var14));
      }
   }

   private AuditorMBean createAuditor(String var1, boolean var2, String var3) throws JMException, ClassNotFoundException {
      return (AuditorMBean)this.createProvider("Auditor", this.getRealm().getAuditorTypes(), var1, var2, var3, new ProviderCreator() {
         public ProviderMBean createProvider(Class var1) throws JMException, ClassNotFoundException {
            return RealmImpl.this.getRealm().createAuditor(var1);
         }

         public ProviderMBean createProvider(Class var1, String var2) throws JMException, ClassNotFoundException {
            return RealmImpl.this.getRealm().createAuditor(var1, var2);
         }
      });
   }

   public AuthenticationProviderMBean createAuthenticationProvider(String var1, boolean var2, String var3) throws JMException, ClassNotFoundException {
      return (AuthenticationProviderMBean)this.createProvider("AutenticationProvider", this.getRealm().getAuthenticationProviderTypes(), var1, var2, var3, new ProviderCreator() {
         public ProviderMBean createProvider(Class var1) throws JMException, ClassNotFoundException {
            return RealmImpl.this.getRealm().createAuthenticationProvider(var1);
         }

         public ProviderMBean createProvider(Class var1, String var2) throws JMException, ClassNotFoundException {
            return RealmImpl.this.getRealm().createAuthenticationProvider(var1, var2);
         }
      });
   }

   public RoleMapperMBean createRoleMapper(String var1, boolean var2, String var3) throws JMException, ClassNotFoundException {
      return (RoleMapperMBean)this.createProvider("RoleMapper", this.getRealm().getRoleMapperTypes(), var1, var2, var3, new ProviderCreator() {
         public ProviderMBean createProvider(Class var1) throws JMException, ClassNotFoundException {
            return RealmImpl.this.getRealm().createRoleMapper(var1);
         }

         public ProviderMBean createProvider(Class var1, String var2) throws JMException, ClassNotFoundException {
            return RealmImpl.this.getRealm().createRoleMapper(var1, var2);
         }
      });
   }

   public AuthorizerMBean createAuthorizer(String var1, boolean var2, String var3) throws JMException, ClassNotFoundException {
      return (AuthorizerMBean)this.createProvider("Authorizer", this.getRealm().getAuthorizerTypes(), var1, var2, var3, new ProviderCreator() {
         public ProviderMBean createProvider(Class var1) throws JMException, ClassNotFoundException {
            return RealmImpl.this.getRealm().createAuthorizer(var1);
         }

         public ProviderMBean createProvider(Class var1, String var2) throws JMException, ClassNotFoundException {
            return RealmImpl.this.getRealm().createAuthorizer(var1, var2);
         }
      });
   }

   public AdjudicatorMBean createAdjudicator(String var1, boolean var2, String var3) throws JMException, ClassNotFoundException {
      return (AdjudicatorMBean)this.createProvider("Adjudicator", this.getRealm().getAdjudicatorTypes(), var1, var2, var3, new ProviderCreator() {
         public ProviderMBean createProvider(Class var1) throws JMException, ClassNotFoundException {
            return RealmImpl.this.getRealm().createAdjudicator(var1);
         }

         public ProviderMBean createProvider(Class var1, String var2) throws JMException, ClassNotFoundException {
            return RealmImpl.this.getRealm().createAdjudicator(var1, var2);
         }
      });
   }

   public CredentialMapperMBean createCredentialMapper(String var1, boolean var2, String var3) throws JMException, ClassNotFoundException {
      return (CredentialMapperMBean)this.createProvider("CredentialMapper", this.getRealm().getCredentialMapperTypes(), var1, var2, var3, new ProviderCreator() {
         public ProviderMBean createProvider(Class var1) throws JMException, ClassNotFoundException {
            return RealmImpl.this.getRealm().createCredentialMapper(var1);
         }

         public ProviderMBean createProvider(Class var1, String var2) throws JMException, ClassNotFoundException {
            return RealmImpl.this.getRealm().createCredentialMapper(var1, var2);
         }
      });
   }

   public CertPathProviderMBean createCertPathProvider(String var1, boolean var2, String var3) throws JMException, ClassNotFoundException {
      return (CertPathProviderMBean)this.createProvider("CertPathProvider", this.getRealm().getCertPathProviderTypes(), var1, var2, var3, new ProviderCreator() {
         public ProviderMBean createProvider(Class var1) throws JMException, ClassNotFoundException {
            return RealmImpl.this.getRealm().createCertPathProvider(var1);
         }

         public ProviderMBean createProvider(Class var1, String var2) throws JMException, ClassNotFoundException {
            return RealmImpl.this.getRealm().createCertPathProvider(var1, var2);
         }
      });
   }

   public KeyStoreMBean createKeyStore(String var1, boolean var2, String var3) throws JMException, ClassNotFoundException {
      return (KeyStoreMBean)this.createProvider("KeyStore", this.getRealm().getKeyStoreTypes(), var1, var2, var3, new ProviderCreator() {
         public ProviderMBean createProvider(Class var1) throws JMException, ClassNotFoundException {
            return RealmImpl.this.getRealm().createKeyStore(var1);
         }

         public ProviderMBean createProvider(Class var1, String var2) throws JMException, ClassNotFoundException {
            return RealmImpl.this.getRealm().createKeyStore(var1, var2);
         }
      });
   }

   public PasswordValidatorMBean createPasswordValidator(String var1, boolean var2, String var3) throws JMException, ClassNotFoundException {
      return (PasswordValidatorMBean)this.createProvider("PasswordValidator", this.getRealm().getPasswordValidatorTypes(), var1, var2, var3, new ProviderCreator() {
         public ProviderMBean createProvider(Class var1) throws JMException, ClassNotFoundException {
            return RealmImpl.this.getRealm().createPasswordValidator(var1);
         }

         public ProviderMBean createProvider(Class var1, String var2) throws JMException, ClassNotFoundException {
            return RealmImpl.this.getRealm().createPasswordValidator(var1, var2);
         }
      });
   }

   public AuditorMBean createAuditor(String var1, String var2) throws JMException, ClassNotFoundException {
      return this.createAuditor(var2, true, var1);
   }

   public AuthenticationProviderMBean createAuthenticationProvider(String var1, String var2) throws JMException, ClassNotFoundException {
      ConfigurationValidator.validateName(var1);
      return this.createAuthenticationProvider(var2, true, var1);
   }

   public RoleMapperMBean createRoleMapper(String var1, String var2) throws JMException, ClassNotFoundException {
      return this.createRoleMapper(var2, true, var1);
   }

   public AuthorizerMBean createAuthorizer(String var1, String var2) throws JMException, ClassNotFoundException {
      return this.createAuthorizer(var2, true, var1);
   }

   public AdjudicatorMBean createAdjudicator(String var1, String var2) throws JMException, ClassNotFoundException {
      return this.createAdjudicator(var2, true, var1);
   }

   public CredentialMapperMBean createCredentialMapper(String var1, String var2) throws JMException, ClassNotFoundException {
      return this.createCredentialMapper(var2, true, var1);
   }

   public CertPathProviderMBean createCertPathProvider(String var1, String var2) throws JMException, ClassNotFoundException {
      return this.createCertPathProvider(var2, true, var1);
   }

   public KeyStoreMBean createKeyStore(String var1, String var2) throws JMException, ClassNotFoundException {
      return this.createKeyStore(var2, true, var1);
   }

   public AuditorMBean createAuditor(String var1) throws JMException, ClassNotFoundException {
      return this.createAuditor(var1, false, (String)null);
   }

   public AuthenticationProviderMBean createAuthenticationProvider(String var1) throws JMException, ClassNotFoundException {
      return this.createAuthenticationProvider(var1, false, (String)null);
   }

   public RoleMapperMBean createRoleMapper(String var1) throws JMException, ClassNotFoundException {
      return this.createRoleMapper(var1, false, (String)null);
   }

   public AuthorizerMBean createAuthorizer(String var1) throws JMException, ClassNotFoundException {
      return this.createAuthorizer(var1, false, (String)null);
   }

   public AdjudicatorMBean createAdjudicator(String var1) throws JMException, ClassNotFoundException {
      return this.createAdjudicator(var1, false, (String)null);
   }

   public CredentialMapperMBean createCredentialMapper(String var1) throws JMException, ClassNotFoundException {
      return this.createCredentialMapper(var1, false, (String)null);
   }

   public CertPathProviderMBean createCertPathProvider(String var1) throws JMException, ClassNotFoundException {
      return this.createCertPathProvider(var1, false, (String)null);
   }

   public KeyStoreMBean createKeyStore(String var1) throws JMException, ClassNotFoundException {
      return this.createKeyStore(var1, false, (String)null);
   }

   public PasswordValidatorMBean createPasswordValidator(String var1, String var2) throws JMException, ClassNotFoundException {
      return this.createPasswordValidator(var2, true, var1);
   }

   public PasswordValidatorMBean createPasswordValidator(String var1) throws JMException, ClassNotFoundException {
      return this.createPasswordValidator(var1, false, (String)null);
   }

   private interface ProviderCreator {
      ProviderMBean createProvider(Class var1) throws JMException, ClassNotFoundException;

      ProviderMBean createProvider(Class var1, String var2) throws JMException, ClassNotFoundException;
   }
}
