package weblogic.management.security;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import javax.management.JMException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.BeanRemoveRejectedException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.beangen.LegalChecks;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.AbstractSchemaHelper2;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.commo.AbstractCommoConfigurationBean;
import weblogic.management.commo.RequiredModelMBeanWrapper;
import weblogic.management.security.audit.AuditorMBean;
import weblogic.management.security.audit.AuditorMBeanImpl;
import weblogic.management.security.authentication.AuthenticationProviderMBean;
import weblogic.management.security.authentication.AuthenticationProviderMBeanImpl;
import weblogic.management.security.authentication.PasswordValidatorMBean;
import weblogic.management.security.authentication.PasswordValidatorMBeanImpl;
import weblogic.management.security.authentication.UserLockoutManagerMBean;
import weblogic.management.security.authentication.UserLockoutManagerMBeanImpl;
import weblogic.management.security.authorization.AdjudicatorMBean;
import weblogic.management.security.authorization.AdjudicatorMBeanImpl;
import weblogic.management.security.authorization.AuthorizerMBean;
import weblogic.management.security.authorization.AuthorizerMBeanImpl;
import weblogic.management.security.authorization.RoleMapperMBean;
import weblogic.management.security.authorization.RoleMapperMBeanImpl;
import weblogic.management.security.credentials.CredentialMapperMBean;
import weblogic.management.security.credentials.CredentialMapperMBeanImpl;
import weblogic.management.security.pk.CertPathBuilderMBean;
import weblogic.management.security.pk.CertPathProviderMBean;
import weblogic.management.security.pk.CertPathProviderMBeanImpl;
import weblogic.management.security.pk.KeyStoreMBean;
import weblogic.management.security.pk.KeyStoreMBeanImpl;
import weblogic.management.utils.ErrorCollectionException;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class RealmMBeanImpl extends AbstractCommoConfigurationBean implements RealmMBean, Serializable {
   private AdjudicatorMBean _Adjudicator;
   private String[] _AdjudicatorTypes;
   private String[] _AuditorTypes;
   private AuditorMBean[] _Auditors;
   private String _AuthMethods;
   private String[] _AuthenticationProviderTypes;
   private AuthenticationProviderMBean[] _AuthenticationProviders;
   private String[] _AuthorizerTypes;
   private AuthorizerMBean[] _Authorizers;
   private CertPathBuilderMBean _CertPathBuilder;
   private String[] _CertPathProviderTypes;
   private CertPathProviderMBean[] _CertPathProviders;
   private boolean _CombinedRoleMappingEnabled;
   private String _CompatibilityObjectName;
   private String[] _CredentialMapperTypes;
   private CredentialMapperMBean[] _CredentialMappers;
   private boolean _DefaultRealm;
   private boolean _DelegateMBeanAuthorization;
   private boolean _DeployCredentialMappingIgnored;
   private boolean _DeployPolicyIgnored;
   private boolean _DeployRoleIgnored;
   private boolean _DeployableProviderSynchronizationEnabled;
   private Integer _DeployableProviderSynchronizationTimeout;
   private boolean _EnableWebLogicPrincipalValidatorCache;
   private boolean _FullyDelegateAuthorization;
   private String[] _KeyStoreTypes;
   private KeyStoreMBean[] _KeyStores;
   private Integer _MaxWebLogicPrincipalsInCache;
   private String _Name;
   private String[] _PasswordValidatorTypes;
   private PasswordValidatorMBean[] _PasswordValidators;
   private RDBMSSecurityStoreMBean _RDBMSSecurityStore;
   private String[] _RoleMapperTypes;
   private RoleMapperMBean[] _RoleMappers;
   private String _SecurityDDModel;
   private UserLockoutManagerMBean _UserLockoutManager;
   private boolean _ValidateDDSecurityData;
   private RealmImpl _customizer;
   private static SchemaHelper2 _schemaHelper;

   public RealmMBeanImpl() {
      try {
         this._customizer = new RealmImpl(new RequiredModelMBeanWrapper(this));
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public RealmMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new RealmImpl(new RequiredModelMBeanWrapper(this));
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public void addAuditor(AuditorMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 2)) {
         AuditorMBean[] var2;
         if (this._isSet(2)) {
            var2 = (AuditorMBean[])((AuditorMBean[])this._getHelper()._extendArray(this.getAuditors(), AuditorMBean.class, var1));
         } else {
            var2 = new AuditorMBean[]{var1};
         }

         try {
            this.setAuditors(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public AuditorMBean[] getAuditors() {
      return this._Auditors;
   }

   public boolean isAuditorsSet() {
      return this._isSet(2);
   }

   public void removeAuditor(AuditorMBean var1) {
      this.destroyAuditor(var1);
   }

   public void setAuditors(AuditorMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new AuditorMBeanImpl[0] : var1;
      ProviderValidator.validateProviders((ProviderMBean[])var4);

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 2)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      AuditorMBean[] var5 = this._Auditors;
      this._Auditors = (AuditorMBean[])var4;
      this._postSet(2, var5, var4);
   }

   public String[] getAuditorTypes() {
      return this._customizer.getAuditorTypes();
   }

   public boolean isAuditorTypesSet() {
      return this._isSet(3);
   }

   public void setAuditorTypes(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      this._AuditorTypes = var1;
   }

   public AuditorMBean createAuditor(String var1, String var2) throws ClassNotFoundException, JMException {
      return this._customizer.createAuditor(var1, var2);
   }

   public AuditorMBean createAuditor(String var1) throws ClassNotFoundException, JMException {
      return this._customizer.createAuditor(var1);
   }

   public AuditorMBean createAuditor(Class var1) throws JMException {
      AuditorMBeanImpl var2 = (AuditorMBeanImpl)this._createChildBean(var1, -1);

      try {
         this.addAuditor(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else if (var4 instanceof JMException) {
            throw (JMException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public AuditorMBean createAuditor(Class var1, String var2) throws JMException {
      AuditorMBeanImpl var3 = (AuditorMBeanImpl)this._createChildBean(var1, -1);

      try {
         var3.setName(var2);
         this.addAuditor(var3);
         return var3;
      } catch (Exception var5) {
         if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         } else if (var5 instanceof JMException) {
            throw (JMException)var5;
         } else {
            throw new UndeclaredThrowableException(var5);
         }
      }
   }

   public void destroyAuditor(AuditorMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 2);
         AuditorMBean[] var2 = this.getAuditors();
         AuditorMBean[] var3 = (AuditorMBean[])((AuditorMBean[])this._getHelper()._removeElement(var2, AuditorMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setAuditors(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public AuditorMBean lookupAuditor(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._Auditors).iterator();

      AuditorMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (AuditorMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addAuthenticationProvider(AuthenticationProviderMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 4)) {
         AuthenticationProviderMBean[] var2;
         if (this._isSet(4)) {
            var2 = (AuthenticationProviderMBean[])((AuthenticationProviderMBean[])this._getHelper()._extendArray(this.getAuthenticationProviders(), AuthenticationProviderMBean.class, var1));
         } else {
            var2 = new AuthenticationProviderMBean[]{var1};
         }

         try {
            this.setAuthenticationProviders(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public AuthenticationProviderMBean[] getAuthenticationProviders() {
      return this._AuthenticationProviders;
   }

   public boolean isAuthenticationProvidersSet() {
      return this._isSet(4);
   }

   public void removeAuthenticationProvider(AuthenticationProviderMBean var1) {
      this.destroyAuthenticationProvider(var1);
   }

   public void setAuthenticationProviders(AuthenticationProviderMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new AuthenticationProviderMBeanImpl[0] : var1;
      ProviderValidator.validateProviders((ProviderMBean[])var4);

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 4)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      AuthenticationProviderMBean[] var5 = this._AuthenticationProviders;
      this._AuthenticationProviders = (AuthenticationProviderMBean[])var4;
      this._postSet(4, var5, var4);
   }

   public String[] getAuthenticationProviderTypes() {
      return this._customizer.getAuthenticationProviderTypes();
   }

   public boolean isAuthenticationProviderTypesSet() {
      return this._isSet(5);
   }

   public void setAuthenticationProviderTypes(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      this._AuthenticationProviderTypes = var1;
   }

   public AuthenticationProviderMBean createAuthenticationProvider(String var1, String var2) throws ClassNotFoundException, JMException {
      return this._customizer.createAuthenticationProvider(var1, var2);
   }

   public AuthenticationProviderMBean createAuthenticationProvider(String var1) throws ClassNotFoundException, JMException {
      return this._customizer.createAuthenticationProvider(var1);
   }

   public AuthenticationProviderMBean createAuthenticationProvider(Class var1) throws JMException {
      AuthenticationProviderMBeanImpl var2 = (AuthenticationProviderMBeanImpl)this._createChildBean(var1, -1);

      try {
         this.addAuthenticationProvider(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else if (var4 instanceof JMException) {
            throw (JMException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public AuthenticationProviderMBean createAuthenticationProvider(Class var1, String var2) throws JMException {
      AuthenticationProviderMBeanImpl var3 = (AuthenticationProviderMBeanImpl)this._createChildBean(var1, -1);

      try {
         var3.setName(var2);
         this.addAuthenticationProvider(var3);
         return var3;
      } catch (Exception var5) {
         if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         } else if (var5 instanceof JMException) {
            throw (JMException)var5;
         } else {
            throw new UndeclaredThrowableException(var5);
         }
      }
   }

   public void destroyAuthenticationProvider(AuthenticationProviderMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 4);
         AuthenticationProviderMBean[] var2 = this.getAuthenticationProviders();
         AuthenticationProviderMBean[] var3 = (AuthenticationProviderMBean[])((AuthenticationProviderMBean[])this._getHelper()._removeElement(var2, AuthenticationProviderMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setAuthenticationProviders(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public AuthenticationProviderMBean lookupAuthenticationProvider(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._AuthenticationProviders).iterator();

      AuthenticationProviderMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (AuthenticationProviderMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addRoleMapper(RoleMapperMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 6)) {
         RoleMapperMBean[] var2;
         if (this._isSet(6)) {
            var2 = (RoleMapperMBean[])((RoleMapperMBean[])this._getHelper()._extendArray(this.getRoleMappers(), RoleMapperMBean.class, var1));
         } else {
            var2 = new RoleMapperMBean[]{var1};
         }

         try {
            this.setRoleMappers(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public RoleMapperMBean[] getRoleMappers() {
      return this._RoleMappers;
   }

   public boolean isRoleMappersSet() {
      return this._isSet(6);
   }

   public void removeRoleMapper(RoleMapperMBean var1) {
      this.destroyRoleMapper(var1);
   }

   public void setRoleMappers(RoleMapperMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new RoleMapperMBeanImpl[0] : var1;
      ProviderValidator.validateProviders((ProviderMBean[])var4);

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 6)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      RoleMapperMBean[] var5 = this._RoleMappers;
      this._RoleMappers = (RoleMapperMBean[])var4;
      this._postSet(6, var5, var4);
   }

   public String[] getRoleMapperTypes() {
      return this._customizer.getRoleMapperTypes();
   }

   public boolean isRoleMapperTypesSet() {
      return this._isSet(7);
   }

   public void setRoleMapperTypes(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      this._RoleMapperTypes = var1;
   }

   public RoleMapperMBean createRoleMapper(String var1, String var2) throws ClassNotFoundException, JMException {
      return this._customizer.createRoleMapper(var1, var2);
   }

   public RoleMapperMBean createRoleMapper(String var1) throws ClassNotFoundException, JMException {
      return this._customizer.createRoleMapper(var1);
   }

   public RoleMapperMBean createRoleMapper(Class var1) throws JMException {
      RoleMapperMBeanImpl var2 = (RoleMapperMBeanImpl)this._createChildBean(var1, -1);

      try {
         this.addRoleMapper(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else if (var4 instanceof JMException) {
            throw (JMException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public RoleMapperMBean createRoleMapper(Class var1, String var2) throws JMException {
      RoleMapperMBeanImpl var3 = (RoleMapperMBeanImpl)this._createChildBean(var1, -1);

      try {
         var3.setName(var2);
         this.addRoleMapper(var3);
         return var3;
      } catch (Exception var5) {
         if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         } else if (var5 instanceof JMException) {
            throw (JMException)var5;
         } else {
            throw new UndeclaredThrowableException(var5);
         }
      }
   }

   public void destroyRoleMapper(RoleMapperMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 6);
         RoleMapperMBean[] var2 = this.getRoleMappers();
         RoleMapperMBean[] var3 = (RoleMapperMBean[])((RoleMapperMBean[])this._getHelper()._removeElement(var2, RoleMapperMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setRoleMappers(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public RoleMapperMBean lookupRoleMapper(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._RoleMappers).iterator();

      RoleMapperMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (RoleMapperMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addAuthorizer(AuthorizerMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 8)) {
         AuthorizerMBean[] var2;
         if (this._isSet(8)) {
            var2 = (AuthorizerMBean[])((AuthorizerMBean[])this._getHelper()._extendArray(this.getAuthorizers(), AuthorizerMBean.class, var1));
         } else {
            var2 = new AuthorizerMBean[]{var1};
         }

         try {
            this.setAuthorizers(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public AuthorizerMBean[] getAuthorizers() {
      return this._Authorizers;
   }

   public boolean isAuthorizersSet() {
      return this._isSet(8);
   }

   public void removeAuthorizer(AuthorizerMBean var1) {
      this.destroyAuthorizer(var1);
   }

   public void setAuthorizers(AuthorizerMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new AuthorizerMBeanImpl[0] : var1;
      ProviderValidator.validateProviders((ProviderMBean[])var4);

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 8)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      AuthorizerMBean[] var5 = this._Authorizers;
      this._Authorizers = (AuthorizerMBean[])var4;
      this._postSet(8, var5, var4);
   }

   public String[] getAuthorizerTypes() {
      return this._customizer.getAuthorizerTypes();
   }

   public boolean isAuthorizerTypesSet() {
      return this._isSet(9);
   }

   public void setAuthorizerTypes(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      this._AuthorizerTypes = var1;
   }

   public AuthorizerMBean createAuthorizer(String var1, String var2) throws ClassNotFoundException, JMException {
      return this._customizer.createAuthorizer(var1, var2);
   }

   public AuthorizerMBean createAuthorizer(String var1) throws ClassNotFoundException, JMException {
      return this._customizer.createAuthorizer(var1);
   }

   public AuthorizerMBean createAuthorizer(Class var1) throws JMException {
      AuthorizerMBeanImpl var2 = (AuthorizerMBeanImpl)this._createChildBean(var1, -1);

      try {
         this.addAuthorizer(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else if (var4 instanceof JMException) {
            throw (JMException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public AuthorizerMBean createAuthorizer(Class var1, String var2) throws JMException {
      AuthorizerMBeanImpl var3 = (AuthorizerMBeanImpl)this._createChildBean(var1, -1);

      try {
         var3.setName(var2);
         this.addAuthorizer(var3);
         return var3;
      } catch (Exception var5) {
         if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         } else if (var5 instanceof JMException) {
            throw (JMException)var5;
         } else {
            throw new UndeclaredThrowableException(var5);
         }
      }
   }

   public void destroyAuthorizer(AuthorizerMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 8);
         AuthorizerMBean[] var2 = this.getAuthorizers();
         AuthorizerMBean[] var3 = (AuthorizerMBean[])((AuthorizerMBean[])this._getHelper()._removeElement(var2, AuthorizerMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setAuthorizers(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public AuthorizerMBean lookupAuthorizer(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._Authorizers).iterator();

      AuthorizerMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (AuthorizerMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public AdjudicatorMBean getAdjudicator() {
      return this._Adjudicator;
   }

   public boolean isAdjudicatorSet() {
      return this._isSet(10);
   }

   public void setAdjudicator(AdjudicatorMBean var1) throws InvalidAttributeValueException {
      if (var1 != null && this.getAdjudicator() != null && var1 != this.getAdjudicator()) {
         throw new BeanAlreadyExistsException(this.getAdjudicator() + " has already been created");
      } else {
         if (var1 != null) {
            AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
            if (this._setParent(var2, this, 10)) {
               this._getReferenceManager().registerBean(var2, false);
               this._postCreate(var2);
            }
         }

         AdjudicatorMBean var3 = this._Adjudicator;
         this._Adjudicator = var1;
         this._postSet(10, var3, var1);
      }
   }

   public String[] getAdjudicatorTypes() {
      return this._customizer.getAdjudicatorTypes();
   }

   public boolean isAdjudicatorTypesSet() {
      return this._isSet(11);
   }

   public void setAdjudicatorTypes(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      this._AdjudicatorTypes = var1;
   }

   public AdjudicatorMBean createAdjudicator(String var1, String var2) throws ClassNotFoundException, JMException {
      return this._customizer.createAdjudicator(var1, var2);
   }

   public AdjudicatorMBean createAdjudicator(String var1) throws ClassNotFoundException, JMException {
      return this._customizer.createAdjudicator(var1);
   }

   public AdjudicatorMBean createAdjudicator(Class var1) throws JMException {
      AdjudicatorMBeanImpl var2 = (AdjudicatorMBeanImpl)this._createChildBean(var1, -1);

      try {
         this.setAdjudicator(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else if (var4 instanceof JMException) {
            throw (JMException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public AdjudicatorMBean createAdjudicator(Class var1, String var2) throws JMException {
      AdjudicatorMBeanImpl var3 = (AdjudicatorMBeanImpl)this._createChildBean(var1, -1);

      try {
         var3.setName(var2);
         this.setAdjudicator(var3);
         return var3;
      } catch (Exception var5) {
         if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         } else if (var5 instanceof JMException) {
            throw (JMException)var5;
         } else {
            throw new UndeclaredThrowableException(var5);
         }
      }
   }

   public void destroyAdjudicator() {
      try {
         AbstractDescriptorBean var1 = (AbstractDescriptorBean)this._Adjudicator;
         if (var1 != null) {
            List var2 = this._getReferenceManager().getResolvedReferences(var1);
            if (var2 != null && var2.size() > 0) {
               throw new BeanRemoveRejectedException(var1, var2);
            } else {
               this._getReferenceManager().unregisterBean(var1);
               this._markDestroyed(var1);
               this.setAdjudicator((AdjudicatorMBean)null);
               this._unSet(10);
            }
         }
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public void addCredentialMapper(CredentialMapperMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 12)) {
         CredentialMapperMBean[] var2;
         if (this._isSet(12)) {
            var2 = (CredentialMapperMBean[])((CredentialMapperMBean[])this._getHelper()._extendArray(this.getCredentialMappers(), CredentialMapperMBean.class, var1));
         } else {
            var2 = new CredentialMapperMBean[]{var1};
         }

         try {
            this.setCredentialMappers(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public CredentialMapperMBean[] getCredentialMappers() {
      return this._CredentialMappers;
   }

   public boolean isCredentialMappersSet() {
      return this._isSet(12);
   }

   public void removeCredentialMapper(CredentialMapperMBean var1) {
      this.destroyCredentialMapper(var1);
   }

   public void setCredentialMappers(CredentialMapperMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new CredentialMapperMBeanImpl[0] : var1;
      ProviderValidator.validateProviders((ProviderMBean[])var4);

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 12)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      CredentialMapperMBean[] var5 = this._CredentialMappers;
      this._CredentialMappers = (CredentialMapperMBean[])var4;
      this._postSet(12, var5, var4);
   }

   public String[] getCredentialMapperTypes() {
      return this._customizer.getCredentialMapperTypes();
   }

   public boolean isCredentialMapperTypesSet() {
      return this._isSet(13);
   }

   public void setCredentialMapperTypes(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      this._CredentialMapperTypes = var1;
   }

   public CredentialMapperMBean createCredentialMapper(String var1, String var2) throws ClassNotFoundException, JMException {
      return this._customizer.createCredentialMapper(var1, var2);
   }

   public CredentialMapperMBean createCredentialMapper(String var1) throws ClassNotFoundException, JMException {
      return this._customizer.createCredentialMapper(var1);
   }

   public CredentialMapperMBean createCredentialMapper(Class var1) throws JMException {
      CredentialMapperMBeanImpl var2 = (CredentialMapperMBeanImpl)this._createChildBean(var1, -1);

      try {
         this.addCredentialMapper(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else if (var4 instanceof JMException) {
            throw (JMException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public CredentialMapperMBean createCredentialMapper(Class var1, String var2) throws JMException {
      CredentialMapperMBeanImpl var3 = (CredentialMapperMBeanImpl)this._createChildBean(var1, -1);

      try {
         var3.setName(var2);
         this.addCredentialMapper(var3);
         return var3;
      } catch (Exception var5) {
         if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         } else if (var5 instanceof JMException) {
            throw (JMException)var5;
         } else {
            throw new UndeclaredThrowableException(var5);
         }
      }
   }

   public void destroyCredentialMapper(CredentialMapperMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 12);
         CredentialMapperMBean[] var2 = this.getCredentialMappers();
         CredentialMapperMBean[] var3 = (CredentialMapperMBean[])((CredentialMapperMBean[])this._getHelper()._removeElement(var2, CredentialMapperMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setCredentialMappers(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public CredentialMapperMBean lookupCredentialMapper(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._CredentialMappers).iterator();

      CredentialMapperMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (CredentialMapperMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addCertPathProvider(CertPathProviderMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 14)) {
         CertPathProviderMBean[] var2;
         if (this._isSet(14)) {
            var2 = (CertPathProviderMBean[])((CertPathProviderMBean[])this._getHelper()._extendArray(this.getCertPathProviders(), CertPathProviderMBean.class, var1));
         } else {
            var2 = new CertPathProviderMBean[]{var1};
         }

         try {
            this.setCertPathProviders(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public CertPathProviderMBean[] getCertPathProviders() {
      return this._CertPathProviders;
   }

   public boolean isCertPathProvidersSet() {
      return this._isSet(14);
   }

   public void removeCertPathProvider(CertPathProviderMBean var1) {
      this.destroyCertPathProvider(var1);
   }

   public void setCertPathProviders(CertPathProviderMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new CertPathProviderMBeanImpl[0] : var1;
      ProviderValidator.validateProviders((ProviderMBean[])var4);

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 14)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      CertPathProviderMBean[] var5 = this._CertPathProviders;
      this._CertPathProviders = (CertPathProviderMBean[])var4;
      this._postSet(14, var5, var4);
   }

   public String[] getCertPathProviderTypes() {
      return this._customizer.getCertPathProviderTypes();
   }

   public boolean isCertPathProviderTypesSet() {
      return this._isSet(15);
   }

   public void setCertPathProviderTypes(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      this._CertPathProviderTypes = var1;
   }

   public CertPathProviderMBean createCertPathProvider(String var1, String var2) throws ClassNotFoundException, JMException {
      return this._customizer.createCertPathProvider(var1, var2);
   }

   public CertPathProviderMBean createCertPathProvider(String var1) throws ClassNotFoundException, JMException {
      return this._customizer.createCertPathProvider(var1);
   }

   public CertPathProviderMBean createCertPathProvider(Class var1) throws JMException {
      CertPathProviderMBeanImpl var2 = (CertPathProviderMBeanImpl)this._createChildBean(var1, -1);

      try {
         this.addCertPathProvider(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else if (var4 instanceof JMException) {
            throw (JMException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public CertPathProviderMBean createCertPathProvider(Class var1, String var2) throws JMException {
      CertPathProviderMBeanImpl var3 = (CertPathProviderMBeanImpl)this._createChildBean(var1, -1);

      try {
         var3.setName(var2);
         this.addCertPathProvider(var3);
         return var3;
      } catch (Exception var5) {
         if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         } else if (var5 instanceof JMException) {
            throw (JMException)var5;
         } else {
            throw new UndeclaredThrowableException(var5);
         }
      }
   }

   public void destroyCertPathProvider(CertPathProviderMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 14);
         CertPathProviderMBean[] var2 = this.getCertPathProviders();
         CertPathProviderMBean[] var3 = (CertPathProviderMBean[])((CertPathProviderMBean[])this._getHelper()._removeElement(var2, CertPathProviderMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setCertPathProviders(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public CertPathProviderMBean lookupCertPathProvider(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._CertPathProviders).iterator();

      CertPathProviderMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (CertPathProviderMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public CertPathBuilderMBean getCertPathBuilder() {
      return this._CertPathBuilder;
   }

   public String getCertPathBuilderAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getCertPathBuilder();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isCertPathBuilderSet() {
      return this._isSet(16);
   }

   public void setCertPathBuilderAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, CertPathBuilderMBean.class, new ReferenceManager.Resolver(this, 16) {
            public void resolveReference(Object var1) {
               try {
                  RealmMBeanImpl.this.setCertPathBuilder((CertPathBuilderMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         CertPathBuilderMBean var2 = this._CertPathBuilder;
         this._initializeProperty(16);
         this._postSet(16, var2, this._CertPathBuilder);
      }

   }

   public void setCertPathBuilder(CertPathBuilderMBean var1) throws InvalidAttributeValueException {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 16, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return RealmMBeanImpl.this.getCertPathBuilder();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      CertPathBuilderMBean var3 = this._CertPathBuilder;
      this._CertPathBuilder = var1;
      this._postSet(16, var3, var1);
   }

   public void addKeyStore(KeyStoreMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 17)) {
         KeyStoreMBean[] var2;
         if (this._isSet(17)) {
            var2 = (KeyStoreMBean[])((KeyStoreMBean[])this._getHelper()._extendArray(this.getKeyStores(), KeyStoreMBean.class, var1));
         } else {
            var2 = new KeyStoreMBean[]{var1};
         }

         try {
            this.setKeyStores(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public KeyStoreMBean[] getKeyStores() {
      return this._KeyStores;
   }

   public boolean isKeyStoresSet() {
      return this._isSet(17);
   }

   public void removeKeyStore(KeyStoreMBean var1) {
      this.destroyKeyStore(var1);
   }

   public void setKeyStores(KeyStoreMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new KeyStoreMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 17)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      KeyStoreMBean[] var5 = this._KeyStores;
      this._KeyStores = (KeyStoreMBean[])var4;
      this._postSet(17, var5, var4);
   }

   public String[] getKeyStoreTypes() {
      return this._customizer.getKeyStoreTypes();
   }

   public boolean isKeyStoreTypesSet() {
      return this._isSet(18);
   }

   public void setKeyStoreTypes(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      this._KeyStoreTypes = var1;
   }

   public KeyStoreMBean createKeyStore(String var1, String var2) throws ClassNotFoundException, JMException {
      return this._customizer.createKeyStore(var1, var2);
   }

   public KeyStoreMBean createKeyStore(String var1) throws ClassNotFoundException, JMException {
      return this._customizer.createKeyStore(var1);
   }

   public KeyStoreMBean createKeyStore(Class var1) throws JMException {
      KeyStoreMBeanImpl var2 = (KeyStoreMBeanImpl)this._createChildBean(var1, -1);

      try {
         this.addKeyStore(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else if (var4 instanceof JMException) {
            throw (JMException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public KeyStoreMBean createKeyStore(Class var1, String var2) throws JMException {
      KeyStoreMBeanImpl var3 = (KeyStoreMBeanImpl)this._createChildBean(var1, -1);

      try {
         var3.setName(var2);
         this.addKeyStore(var3);
         return var3;
      } catch (Exception var5) {
         if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         } else if (var5 instanceof JMException) {
            throw (JMException)var5;
         } else {
            throw new UndeclaredThrowableException(var5);
         }
      }
   }

   public void destroyKeyStore(KeyStoreMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 17);
         KeyStoreMBean[] var2 = this.getKeyStores();
         KeyStoreMBean[] var3 = (KeyStoreMBean[])((KeyStoreMBean[])this._getHelper()._removeElement(var2, KeyStoreMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setKeyStores(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public KeyStoreMBean lookupKeyStore(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._KeyStores).iterator();

      KeyStoreMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (KeyStoreMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public UserLockoutManagerMBean getUserLockoutManager() {
      return this._UserLockoutManager;
   }

   public boolean isUserLockoutManagerSet() {
      return this._isSet(19) || this._isAnythingSet((AbstractDescriptorBean)this.getUserLockoutManager());
   }

   public void setUserLockoutManager(UserLockoutManagerMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 19)) {
         this._postCreate(var2);
      }

      UserLockoutManagerMBean var3 = this._UserLockoutManager;
      this._UserLockoutManager = var1;
      this._postSet(19, var3, var1);
   }

   public boolean isDeployRoleIgnored() {
      return this._DeployRoleIgnored;
   }

   public boolean isDeployRoleIgnoredSet() {
      return this._isSet(20);
   }

   public void setDeployRoleIgnored(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._DeployRoleIgnored;
      this._DeployRoleIgnored = var1;
      this._postSet(20, var2, var1);
   }

   public boolean isDeployPolicyIgnored() {
      return this._DeployPolicyIgnored;
   }

   public boolean isDeployPolicyIgnoredSet() {
      return this._isSet(21);
   }

   public void setDeployPolicyIgnored(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._DeployPolicyIgnored;
      this._DeployPolicyIgnored = var1;
      this._postSet(21, var2, var1);
   }

   public boolean isDeployCredentialMappingIgnored() {
      return this._DeployCredentialMappingIgnored;
   }

   public boolean isDeployCredentialMappingIgnoredSet() {
      return this._isSet(22);
   }

   public void setDeployCredentialMappingIgnored(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._DeployCredentialMappingIgnored;
      this._DeployCredentialMappingIgnored = var1;
      this._postSet(22, var2, var1);
   }

   public boolean isFullyDelegateAuthorization() {
      return this._FullyDelegateAuthorization;
   }

   public boolean isFullyDelegateAuthorizationSet() {
      return this._isSet(23);
   }

   public void setFullyDelegateAuthorization(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._FullyDelegateAuthorization;
      this._FullyDelegateAuthorization = var1;
      this._postSet(23, var2, var1);
   }

   public boolean isValidateDDSecurityData() {
      return this._ValidateDDSecurityData;
   }

   public boolean isValidateDDSecurityDataSet() {
      return this._isSet(24);
   }

   public void setValidateDDSecurityData(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._ValidateDDSecurityData;
      this._ValidateDDSecurityData = var1;
      this._postSet(24, var2, var1);
   }

   public String getSecurityDDModel() {
      return this._SecurityDDModel;
   }

   public boolean isSecurityDDModelSet() {
      return this._isSet(25);
   }

   public void setSecurityDDModel(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"DDOnly", "CustomRoles", "CustomRolesAndPolicies", "Advanced"};
      var1 = LegalChecks.checkInEnum("SecurityDDModel", var1, var2);
      String var3 = this._SecurityDDModel;
      this._SecurityDDModel = var1;
      this._postSet(25, var3, var1);
   }

   public boolean isCombinedRoleMappingEnabled() {
      return this._CombinedRoleMappingEnabled;
   }

   public boolean isCombinedRoleMappingEnabledSet() {
      return this._isSet(26);
   }

   public void setCombinedRoleMappingEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._CombinedRoleMappingEnabled;
      this._CombinedRoleMappingEnabled = var1;
      this._postSet(26, var2, var1);
   }

   public void validate() throws ErrorCollectionException {
      this._customizer.validate();
   }

   public boolean isDefaultRealm() {
      return this._customizer.isDefaultRealm();
   }

   public boolean isDefaultRealmSet() {
      return this._isSet(27);
   }

   public void setDefaultRealm(boolean var1) throws InvalidAttributeValueException {
      this._customizer.setDefaultRealm(var1);
   }

   public boolean isEnableWebLogicPrincipalValidatorCache() {
      return this._EnableWebLogicPrincipalValidatorCache;
   }

   public boolean isEnableWebLogicPrincipalValidatorCacheSet() {
      return this._isSet(28);
   }

   public void setEnableWebLogicPrincipalValidatorCache(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._EnableWebLogicPrincipalValidatorCache;
      this._EnableWebLogicPrincipalValidatorCache = var1;
      this._postSet(28, var2, var1);
   }

   public Integer getMaxWebLogicPrincipalsInCache() {
      return this._MaxWebLogicPrincipalsInCache;
   }

   public boolean isMaxWebLogicPrincipalsInCacheSet() {
      return this._isSet(29);
   }

   public void setMaxWebLogicPrincipalsInCache(Integer var1) throws InvalidAttributeValueException {
      RealmValidator.validateMaxWebLogicPrincipalsInCache(var1);
      Integer var2 = this._MaxWebLogicPrincipalsInCache;
      this._MaxWebLogicPrincipalsInCache = var1;
      this._postSet(29, var2, var1);
   }

   public String getName() {
      return this._Name;
   }

   public boolean isNameSet() {
      return this._isSet(30);
   }

   public void setName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Name;
      this._Name = var1;
      this._postSet(30, var2, var1);
   }

   public boolean isDelegateMBeanAuthorization() {
      return this._DelegateMBeanAuthorization;
   }

   public boolean isDelegateMBeanAuthorizationSet() {
      return this._isSet(31);
   }

   public void setDelegateMBeanAuthorization(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._DelegateMBeanAuthorization;
      this._DelegateMBeanAuthorization = var1;
      this._postSet(31, var2, var1);
   }

   public String getAuthMethods() {
      return this._AuthMethods;
   }

   public boolean isAuthMethodsSet() {
      return this._isSet(32);
   }

   public void setAuthMethods(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._AuthMethods;
      this._AuthMethods = var1;
      this._postSet(32, var2, var1);
   }

   public String getCompatibilityObjectName() {
      return this._customizer.getCompatibilityObjectName();
   }

   public boolean isCompatibilityObjectNameSet() {
      return this._isSet(33);
   }

   public void setCompatibilityObjectName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CompatibilityObjectName;
      this._CompatibilityObjectName = var1;
      this._postSet(33, var2, var1);
   }

   public RDBMSSecurityStoreMBean getRDBMSSecurityStore() {
      return this._RDBMSSecurityStore;
   }

   public boolean isRDBMSSecurityStoreSet() {
      return this._isSet(34);
   }

   public void setRDBMSSecurityStore(RDBMSSecurityStoreMBean var1) throws InvalidAttributeValueException {
      if (var1 != null && this.getRDBMSSecurityStore() != null && var1 != this.getRDBMSSecurityStore()) {
         throw new BeanAlreadyExistsException(this.getRDBMSSecurityStore() + " has already been created");
      } else {
         if (var1 != null) {
            AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
            if (this._setParent(var2, this, 34)) {
               this._getReferenceManager().registerBean(var2, false);
               this._postCreate(var2);
            }
         }

         RDBMSSecurityStoreMBean var3 = this._RDBMSSecurityStore;
         this._RDBMSSecurityStore = var1;
         this._postSet(34, var3, var1);
      }
   }

   public RDBMSSecurityStoreMBean createRDBMSSecurityStore() throws JMException {
      RDBMSSecurityStoreMBeanImpl var1 = new RDBMSSecurityStoreMBeanImpl(this, -1);

      try {
         this.setRDBMSSecurityStore(var1);
         return var1;
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else if (var3 instanceof JMException) {
            throw (JMException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public RDBMSSecurityStoreMBean createRDBMSSecurityStore(String var1) throws JMException {
      RDBMSSecurityStoreMBeanImpl var2 = new RDBMSSecurityStoreMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.setRDBMSSecurityStore(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else if (var4 instanceof JMException) {
            throw (JMException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyRDBMSSecurityStore() {
      try {
         AbstractDescriptorBean var1 = (AbstractDescriptorBean)this._RDBMSSecurityStore;
         if (var1 != null) {
            List var2 = this._getReferenceManager().getResolvedReferences(var1);
            if (var2 != null && var2.size() > 0) {
               throw new BeanRemoveRejectedException(var1, var2);
            } else {
               this._getReferenceManager().unregisterBean(var1);
               this._markDestroyed(var1);
               this.setRDBMSSecurityStore((RDBMSSecurityStoreMBean)null);
               this._unSet(34);
            }
         }
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public PasswordValidatorMBean createPasswordValidator(Class var1) throws JMException {
      PasswordValidatorMBeanImpl var2 = (PasswordValidatorMBeanImpl)this._createChildBean(var1, -1);

      try {
         this.addPasswordValidator(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else if (var4 instanceof JMException) {
            throw (JMException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public PasswordValidatorMBean createPasswordValidator(Class var1, String var2) throws JMException {
      PasswordValidatorMBeanImpl var3 = (PasswordValidatorMBeanImpl)this._createChildBean(var1, -1);

      try {
         var3.setName(var2);
         this.addPasswordValidator(var3);
         return var3;
      } catch (Exception var5) {
         if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         } else if (var5 instanceof JMException) {
            throw (JMException)var5;
         } else {
            throw new UndeclaredThrowableException(var5);
         }
      }
   }

   public PasswordValidatorMBean createPasswordValidator(String var1, String var2) throws ClassNotFoundException, JMException {
      return this._customizer.createPasswordValidator(var1, var2);
   }

   public PasswordValidatorMBean createPasswordValidator(String var1) throws ClassNotFoundException, JMException {
      return this._customizer.createPasswordValidator(var1);
   }

   public String[] getPasswordValidatorTypes() {
      return this._customizer.getPasswordValidatorTypes();
   }

   public boolean isPasswordValidatorTypesSet() {
      return this._isSet(35);
   }

   public void setPasswordValidatorTypes(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      this._PasswordValidatorTypes = var1;
   }

   public void addPasswordValidator(PasswordValidatorMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 36)) {
         PasswordValidatorMBean[] var2;
         if (this._isSet(36)) {
            var2 = (PasswordValidatorMBean[])((PasswordValidatorMBean[])this._getHelper()._extendArray(this.getPasswordValidators(), PasswordValidatorMBean.class, var1));
         } else {
            var2 = new PasswordValidatorMBean[]{var1};
         }

         try {
            this.setPasswordValidators(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public PasswordValidatorMBean[] getPasswordValidators() {
      return this._PasswordValidators;
   }

   public boolean isPasswordValidatorsSet() {
      return this._isSet(36);
   }

   public void removePasswordValidator(PasswordValidatorMBean var1) {
      this.destroyPasswordValidator(var1);
   }

   public void setPasswordValidators(PasswordValidatorMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new PasswordValidatorMBeanImpl[0] : var1;
      ProviderValidator.validateProviders((ProviderMBean[])var4);

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 36)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      PasswordValidatorMBean[] var5 = this._PasswordValidators;
      this._PasswordValidators = (PasswordValidatorMBean[])var4;
      this._postSet(36, var5, var4);
   }

   public PasswordValidatorMBean lookupPasswordValidator(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._PasswordValidators).iterator();

      PasswordValidatorMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (PasswordValidatorMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void destroyPasswordValidator(PasswordValidatorMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 36);
         PasswordValidatorMBean[] var2 = this.getPasswordValidators();
         PasswordValidatorMBean[] var3 = (PasswordValidatorMBean[])((PasswordValidatorMBean[])this._getHelper()._removeElement(var2, PasswordValidatorMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setPasswordValidators(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public boolean isDeployableProviderSynchronizationEnabled() {
      return this._DeployableProviderSynchronizationEnabled;
   }

   public boolean isDeployableProviderSynchronizationEnabledSet() {
      return this._isSet(37);
   }

   public void setDeployableProviderSynchronizationEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._DeployableProviderSynchronizationEnabled;
      this._DeployableProviderSynchronizationEnabled = var1;
      this._postSet(37, var2, var1);
   }

   public Integer getDeployableProviderSynchronizationTimeout() {
      return this._DeployableProviderSynchronizationTimeout;
   }

   public boolean isDeployableProviderSynchronizationTimeoutSet() {
      return this._isSet(38);
   }

   public void setDeployableProviderSynchronizationTimeout(Integer var1) throws InvalidAttributeValueException {
      Integer var2 = this._DeployableProviderSynchronizationTimeout;
      this._DeployableProviderSynchronizationTimeout = var1;
      this._postSet(38, var2, var1);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   protected void _postCreate() {
      this._customizer._postCreate();
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
      }

   }

   protected AbstractDescriptorBeanHelper _createHelper() {
      return new Helper(this);
   }

   public boolean _isAnythingSet() {
      return super._isAnythingSet() || this.isUserLockoutManagerSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 10;
      }

      try {
         switch (var1) {
            case 10:
               this._Adjudicator = null;
               if (var2) {
                  break;
               }
            case 11:
               this._AdjudicatorTypes = new String[0];
               if (var2) {
                  break;
               }
            case 3:
               this._AuditorTypes = new String[0];
               if (var2) {
                  break;
               }
            case 2:
               this._Auditors = new AuditorMBean[0];
               if (var2) {
                  break;
               }
            case 32:
               this._AuthMethods = null;
               if (var2) {
                  break;
               }
            case 5:
               this._AuthenticationProviderTypes = new String[0];
               if (var2) {
                  break;
               }
            case 4:
               this._AuthenticationProviders = new AuthenticationProviderMBean[0];
               if (var2) {
                  break;
               }
            case 9:
               this._AuthorizerTypes = new String[0];
               if (var2) {
                  break;
               }
            case 8:
               this._Authorizers = new AuthorizerMBean[0];
               if (var2) {
                  break;
               }
            case 16:
               this._CertPathBuilder = null;
               if (var2) {
                  break;
               }
            case 15:
               this._CertPathProviderTypes = new String[0];
               if (var2) {
                  break;
               }
            case 14:
               this._CertPathProviders = new CertPathProviderMBean[0];
               if (var2) {
                  break;
               }
            case 33:
               this._CompatibilityObjectName = null;
               if (var2) {
                  break;
               }
            case 13:
               this._CredentialMapperTypes = new String[0];
               if (var2) {
                  break;
               }
            case 12:
               this._CredentialMappers = new CredentialMapperMBean[0];
               if (var2) {
                  break;
               }
            case 38:
               this._DeployableProviderSynchronizationTimeout = new Integer(60000);
               if (var2) {
                  break;
               }
            case 18:
               this._KeyStoreTypes = new String[0];
               if (var2) {
                  break;
               }
            case 17:
               this._KeyStores = new KeyStoreMBean[0];
               if (var2) {
                  break;
               }
            case 29:
               this._MaxWebLogicPrincipalsInCache = new Integer(500);
               if (var2) {
                  break;
               }
            case 30:
               this._Name = "Realm";
               if (var2) {
                  break;
               }
            case 35:
               this._PasswordValidatorTypes = new String[0];
               if (var2) {
                  break;
               }
            case 36:
               this._PasswordValidators = new PasswordValidatorMBean[0];
               if (var2) {
                  break;
               }
            case 34:
               this._RDBMSSecurityStore = null;
               if (var2) {
                  break;
               }
            case 7:
               this._RoleMapperTypes = new String[0];
               if (var2) {
                  break;
               }
            case 6:
               this._RoleMappers = new RoleMapperMBean[0];
               if (var2) {
                  break;
               }
            case 25:
               this._SecurityDDModel = "DDOnly";
               if (var2) {
                  break;
               }
            case 19:
               this._UserLockoutManager = new UserLockoutManagerMBeanImpl(this, 19);
               this._postCreate((AbstractDescriptorBean)this._UserLockoutManager);
               if (var2) {
                  break;
               }
            case 26:
               this._CombinedRoleMappingEnabled = true;
               if (var2) {
                  break;
               }
            case 27:
               this._customizer.setDefaultRealm(false);
               if (var2) {
                  break;
               }
            case 31:
               this._DelegateMBeanAuthorization = false;
               if (var2) {
                  break;
               }
            case 22:
               this._DeployCredentialMappingIgnored = false;
               if (var2) {
                  break;
               }
            case 21:
               this._DeployPolicyIgnored = false;
               if (var2) {
                  break;
               }
            case 20:
               this._DeployRoleIgnored = false;
               if (var2) {
                  break;
               }
            case 37:
               this._DeployableProviderSynchronizationEnabled = false;
               if (var2) {
                  break;
               }
            case 28:
               this._EnableWebLogicPrincipalValidatorCache = true;
               if (var2) {
                  break;
               }
            case 23:
               this._FullyDelegateAuthorization = false;
               if (var2) {
                  break;
               }
            case 24:
               this._ValidateDDSecurityData = false;
               if (var2) {
                  break;
               }
            default:
               if (var2) {
                  return false;
               }
         }

         return true;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Exception var5) {
         throw (Error)(new AssertionError("Impossible Exception")).initCause(var5);
      }
   }

   public Munger.SchemaHelper _getSchemaHelper() {
      return null;
   }

   public String _getElementName(int var1) {
      return this._getSchemaHelper2().getElementName(var1);
   }

   protected String getSchemaLocation() {
      return "http://xmlns.oracle.com/weblogic/1.0/security.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/security";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public String wls_getInterfaceClassName() {
      return "weblogic.management.security.RealmMBean";
   }

   public static class SchemaHelper2 extends AbstractSchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 30;
               }
            case 5:
            case 6:
            case 8:
            case 26:
            case 27:
            case 30:
            case 31:
            case 32:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            default:
               break;
            case 7:
               if (var1.equals("auditor")) {
                  return 2;
               }
               break;
            case 9:
               if (var1.equals("key-store")) {
                  return 17;
               }
               break;
            case 10:
               if (var1.equals("authorizer")) {
                  return 8;
               }
               break;
            case 11:
               if (var1.equals("adjudicator")) {
                  return 10;
               }

               if (var1.equals("role-mapper")) {
                  return 6;
               }
               break;
            case 12:
               if (var1.equals("auditor-type")) {
                  return 3;
               }

               if (var1.equals("auth-methods")) {
                  return 32;
               }
               break;
            case 13:
               if (var1.equals("default-realm")) {
                  return 27;
               }
               break;
            case 14:
               if (var1.equals("key-store-type")) {
                  return 18;
               }
               break;
            case 15:
               if (var1.equals("authorizer-type")) {
                  return 9;
               }
               break;
            case 16:
               if (var1.equals("adjudicator-type")) {
                  return 11;
               }

               if (var1.equals("role-mapper-type")) {
                  return 7;
               }

               if (var1.equals("securitydd-model")) {
                  return 25;
               }
               break;
            case 17:
               if (var1.equals("cert-path-builder")) {
                  return 16;
               }

               if (var1.equals("credential-mapper")) {
                  return 12;
               }
               break;
            case 18:
               if (var1.equals("cert-path-provider")) {
                  return 14;
               }

               if (var1.equals("password-validator")) {
                  return 36;
               }
               break;
            case 19:
               if (var1.equals("deploy-role-ignored")) {
                  return 20;
               }
               break;
            case 20:
               if (var1.equals("rdbms-security-store")) {
                  return 34;
               }

               if (var1.equals("user-lockout-manager")) {
                  return 19;
               }
               break;
            case 21:
               if (var1.equals("deploy-policy-ignored")) {
                  return 21;
               }
               break;
            case 22:
               if (var1.equals("credential-mapper-type")) {
                  return 13;
               }
               break;
            case 23:
               if (var1.equals("authentication-provider")) {
                  return 4;
               }

               if (var1.equals("cert-path-provider-type")) {
                  return 15;
               }

               if (var1.equals("password-validator-type")) {
                  return 35;
               }
               break;
            case 24:
               if (var1.equals("validatedd-security-data")) {
                  return 24;
               }
               break;
            case 25:
               if (var1.equals("compatibility-object-name")) {
                  return 33;
               }
               break;
            case 28:
               if (var1.equals("authentication-provider-type")) {
                  return 5;
               }

               if (var1.equals("delegatem-bean-authorization")) {
                  return 31;
               }

               if (var1.equals("fully-delegate-authorization")) {
                  return 23;
               }
               break;
            case 29:
               if (var1.equals("combined-role-mapping-enabled")) {
                  return 26;
               }
               break;
            case 33:
               if (var1.equals("max-web-logic-principals-in-cache")) {
                  return 29;
               }

               if (var1.equals("deploy-credential-mapping-ignored")) {
                  return 22;
               }
               break;
            case 42:
               if (var1.equals("enable-web-logic-principal-validator-cache")) {
                  return 28;
               }
               break;
            case 43:
               if (var1.equals("deployable-provider-synchronization-timeout")) {
                  return 38;
               }

               if (var1.equals("deployable-provider-synchronization-enabled")) {
                  return 37;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 2:
               return new AuditorMBeanImpl.SchemaHelper2();
            case 3:
            case 5:
            case 7:
            case 9:
            case 11:
            case 13:
            case 15:
            case 16:
            case 18:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 35:
            default:
               return super.getSchemaHelper(var1);
            case 4:
               return new AuthenticationProviderMBeanImpl.SchemaHelper2();
            case 6:
               return new RoleMapperMBeanImpl.SchemaHelper2();
            case 8:
               return new AuthorizerMBeanImpl.SchemaHelper2();
            case 10:
               return new AdjudicatorMBeanImpl.SchemaHelper2();
            case 12:
               return new CredentialMapperMBeanImpl.SchemaHelper2();
            case 14:
               return new CertPathProviderMBeanImpl.SchemaHelper2();
            case 17:
               return new KeyStoreMBeanImpl.SchemaHelper2();
            case 19:
               return new UserLockoutManagerMBeanImpl.SchemaHelper2();
            case 34:
               return new RDBMSSecurityStoreMBeanImpl.SchemaHelper2();
            case 36:
               return new PasswordValidatorMBeanImpl.SchemaHelper2();
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 2:
               return "auditor";
            case 3:
               return "auditor-type";
            case 4:
               return "authentication-provider";
            case 5:
               return "authentication-provider-type";
            case 6:
               return "role-mapper";
            case 7:
               return "role-mapper-type";
            case 8:
               return "authorizer";
            case 9:
               return "authorizer-type";
            case 10:
               return "adjudicator";
            case 11:
               return "adjudicator-type";
            case 12:
               return "credential-mapper";
            case 13:
               return "credential-mapper-type";
            case 14:
               return "cert-path-provider";
            case 15:
               return "cert-path-provider-type";
            case 16:
               return "cert-path-builder";
            case 17:
               return "key-store";
            case 18:
               return "key-store-type";
            case 19:
               return "user-lockout-manager";
            case 20:
               return "deploy-role-ignored";
            case 21:
               return "deploy-policy-ignored";
            case 22:
               return "deploy-credential-mapping-ignored";
            case 23:
               return "fully-delegate-authorization";
            case 24:
               return "validatedd-security-data";
            case 25:
               return "securitydd-model";
            case 26:
               return "combined-role-mapping-enabled";
            case 27:
               return "default-realm";
            case 28:
               return "enable-web-logic-principal-validator-cache";
            case 29:
               return "max-web-logic-principals-in-cache";
            case 30:
               return "name";
            case 31:
               return "delegatem-bean-authorization";
            case 32:
               return "auth-methods";
            case 33:
               return "compatibility-object-name";
            case 34:
               return "rdbms-security-store";
            case 35:
               return "password-validator-type";
            case 36:
               return "password-validator";
            case 37:
               return "deployable-provider-synchronization-enabled";
            case 38:
               return "deployable-provider-synchronization-timeout";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 2:
               return true;
            case 3:
               return true;
            case 4:
               return true;
            case 5:
               return true;
            case 6:
               return true;
            case 7:
               return true;
            case 8:
               return true;
            case 9:
               return true;
            case 10:
            case 16:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            default:
               return super.isArray(var1);
            case 11:
               return true;
            case 12:
               return true;
            case 13:
               return true;
            case 14:
               return true;
            case 15:
               return true;
            case 17:
               return true;
            case 18:
               return true;
            case 35:
               return true;
            case 36:
               return true;
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 2:
               return true;
            case 3:
            case 5:
            case 7:
            case 9:
            case 11:
            case 13:
            case 15:
            case 16:
            case 18:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 35:
            default:
               return super.isBean(var1);
            case 4:
               return true;
            case 6:
               return true;
            case 8:
               return true;
            case 10:
               return true;
            case 12:
               return true;
            case 14:
               return true;
            case 17:
               return true;
            case 19:
               return true;
            case 34:
               return true;
            case 36:
               return true;
         }
      }
   }

   protected static class Helper extends AbstractCommoConfigurationBean.Helper {
      private RealmMBeanImpl bean;

      protected Helper(RealmMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "Auditors";
            case 3:
               return "AuditorTypes";
            case 4:
               return "AuthenticationProviders";
            case 5:
               return "AuthenticationProviderTypes";
            case 6:
               return "RoleMappers";
            case 7:
               return "RoleMapperTypes";
            case 8:
               return "Authorizers";
            case 9:
               return "AuthorizerTypes";
            case 10:
               return "Adjudicator";
            case 11:
               return "AdjudicatorTypes";
            case 12:
               return "CredentialMappers";
            case 13:
               return "CredentialMapperTypes";
            case 14:
               return "CertPathProviders";
            case 15:
               return "CertPathProviderTypes";
            case 16:
               return "CertPathBuilder";
            case 17:
               return "KeyStores";
            case 18:
               return "KeyStoreTypes";
            case 19:
               return "UserLockoutManager";
            case 20:
               return "DeployRoleIgnored";
            case 21:
               return "DeployPolicyIgnored";
            case 22:
               return "DeployCredentialMappingIgnored";
            case 23:
               return "FullyDelegateAuthorization";
            case 24:
               return "ValidateDDSecurityData";
            case 25:
               return "SecurityDDModel";
            case 26:
               return "CombinedRoleMappingEnabled";
            case 27:
               return "DefaultRealm";
            case 28:
               return "EnableWebLogicPrincipalValidatorCache";
            case 29:
               return "MaxWebLogicPrincipalsInCache";
            case 30:
               return "Name";
            case 31:
               return "DelegateMBeanAuthorization";
            case 32:
               return "AuthMethods";
            case 33:
               return "CompatibilityObjectName";
            case 34:
               return "RDBMSSecurityStore";
            case 35:
               return "PasswordValidatorTypes";
            case 36:
               return "PasswordValidators";
            case 37:
               return "DeployableProviderSynchronizationEnabled";
            case 38:
               return "DeployableProviderSynchronizationTimeout";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("Adjudicator")) {
            return 10;
         } else if (var1.equals("AdjudicatorTypes")) {
            return 11;
         } else if (var1.equals("AuditorTypes")) {
            return 3;
         } else if (var1.equals("Auditors")) {
            return 2;
         } else if (var1.equals("AuthMethods")) {
            return 32;
         } else if (var1.equals("AuthenticationProviderTypes")) {
            return 5;
         } else if (var1.equals("AuthenticationProviders")) {
            return 4;
         } else if (var1.equals("AuthorizerTypes")) {
            return 9;
         } else if (var1.equals("Authorizers")) {
            return 8;
         } else if (var1.equals("CertPathBuilder")) {
            return 16;
         } else if (var1.equals("CertPathProviderTypes")) {
            return 15;
         } else if (var1.equals("CertPathProviders")) {
            return 14;
         } else if (var1.equals("CompatibilityObjectName")) {
            return 33;
         } else if (var1.equals("CredentialMapperTypes")) {
            return 13;
         } else if (var1.equals("CredentialMappers")) {
            return 12;
         } else if (var1.equals("DeployableProviderSynchronizationTimeout")) {
            return 38;
         } else if (var1.equals("KeyStoreTypes")) {
            return 18;
         } else if (var1.equals("KeyStores")) {
            return 17;
         } else if (var1.equals("MaxWebLogicPrincipalsInCache")) {
            return 29;
         } else if (var1.equals("Name")) {
            return 30;
         } else if (var1.equals("PasswordValidatorTypes")) {
            return 35;
         } else if (var1.equals("PasswordValidators")) {
            return 36;
         } else if (var1.equals("RDBMSSecurityStore")) {
            return 34;
         } else if (var1.equals("RoleMapperTypes")) {
            return 7;
         } else if (var1.equals("RoleMappers")) {
            return 6;
         } else if (var1.equals("SecurityDDModel")) {
            return 25;
         } else if (var1.equals("UserLockoutManager")) {
            return 19;
         } else if (var1.equals("CombinedRoleMappingEnabled")) {
            return 26;
         } else if (var1.equals("DefaultRealm")) {
            return 27;
         } else if (var1.equals("DelegateMBeanAuthorization")) {
            return 31;
         } else if (var1.equals("DeployCredentialMappingIgnored")) {
            return 22;
         } else if (var1.equals("DeployPolicyIgnored")) {
            return 21;
         } else if (var1.equals("DeployRoleIgnored")) {
            return 20;
         } else if (var1.equals("DeployableProviderSynchronizationEnabled")) {
            return 37;
         } else if (var1.equals("EnableWebLogicPrincipalValidatorCache")) {
            return 28;
         } else if (var1.equals("FullyDelegateAuthorization")) {
            return 23;
         } else {
            return var1.equals("ValidateDDSecurityData") ? 24 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         if (this.bean.getAdjudicator() != null) {
            var1.add(new ArrayIterator(new AdjudicatorMBean[]{this.bean.getAdjudicator()}));
         }

         var1.add(new ArrayIterator(this.bean.getAuditors()));
         var1.add(new ArrayIterator(this.bean.getAuthenticationProviders()));
         var1.add(new ArrayIterator(this.bean.getAuthorizers()));
         var1.add(new ArrayIterator(this.bean.getCertPathProviders()));
         var1.add(new ArrayIterator(this.bean.getCredentialMappers()));
         var1.add(new ArrayIterator(this.bean.getKeyStores()));
         var1.add(new ArrayIterator(this.bean.getPasswordValidators()));
         if (this.bean.getRDBMSSecurityStore() != null) {
            var1.add(new ArrayIterator(new RDBMSSecurityStoreMBean[]{this.bean.getRDBMSSecurityStore()}));
         }

         var1.add(new ArrayIterator(this.bean.getRoleMappers()));
         if (this.bean.getUserLockoutManager() != null) {
            var1.add(new ArrayIterator(new UserLockoutManagerMBean[]{this.bean.getUserLockoutManager()}));
         }

         return new CombinedIterator(var1);
      }

      protected long computeHashValue(CRC32 var1) {
         try {
            StringBuffer var2 = new StringBuffer();
            long var3 = super.computeHashValue(var1);
            if (var3 != 0L) {
               var2.append(String.valueOf(var3));
            }

            long var5 = 0L;
            var5 = this.computeChildHashValue(this.bean.getAdjudicator());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isAdjudicatorTypesSet()) {
               var2.append("AdjudicatorTypes");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getAdjudicatorTypes())));
            }

            if (this.bean.isAuditorTypesSet()) {
               var2.append("AuditorTypes");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getAuditorTypes())));
            }

            var5 = 0L;

            int var7;
            for(var7 = 0; var7 < this.bean.getAuditors().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getAuditors()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isAuthMethodsSet()) {
               var2.append("AuthMethods");
               var2.append(String.valueOf(this.bean.getAuthMethods()));
            }

            if (this.bean.isAuthenticationProviderTypesSet()) {
               var2.append("AuthenticationProviderTypes");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getAuthenticationProviderTypes())));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getAuthenticationProviders().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getAuthenticationProviders()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isAuthorizerTypesSet()) {
               var2.append("AuthorizerTypes");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getAuthorizerTypes())));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getAuthorizers().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getAuthorizers()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isCertPathBuilderSet()) {
               var2.append("CertPathBuilder");
               var2.append(String.valueOf(this.bean.getCertPathBuilder()));
            }

            if (this.bean.isCertPathProviderTypesSet()) {
               var2.append("CertPathProviderTypes");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getCertPathProviderTypes())));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getCertPathProviders().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getCertPathProviders()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isCompatibilityObjectNameSet()) {
               var2.append("CompatibilityObjectName");
               var2.append(String.valueOf(this.bean.getCompatibilityObjectName()));
            }

            if (this.bean.isCredentialMapperTypesSet()) {
               var2.append("CredentialMapperTypes");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getCredentialMapperTypes())));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getCredentialMappers().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getCredentialMappers()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isDeployableProviderSynchronizationTimeoutSet()) {
               var2.append("DeployableProviderSynchronizationTimeout");
               var2.append(String.valueOf(this.bean.getDeployableProviderSynchronizationTimeout()));
            }

            if (this.bean.isKeyStoreTypesSet()) {
               var2.append("KeyStoreTypes");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getKeyStoreTypes())));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getKeyStores().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getKeyStores()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isMaxWebLogicPrincipalsInCacheSet()) {
               var2.append("MaxWebLogicPrincipalsInCache");
               var2.append(String.valueOf(this.bean.getMaxWebLogicPrincipalsInCache()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isPasswordValidatorTypesSet()) {
               var2.append("PasswordValidatorTypes");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getPasswordValidatorTypes())));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getPasswordValidators().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getPasswordValidators()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getRDBMSSecurityStore());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isRoleMapperTypesSet()) {
               var2.append("RoleMapperTypes");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getRoleMapperTypes())));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getRoleMappers().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getRoleMappers()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isSecurityDDModelSet()) {
               var2.append("SecurityDDModel");
               var2.append(String.valueOf(this.bean.getSecurityDDModel()));
            }

            var5 = this.computeChildHashValue(this.bean.getUserLockoutManager());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isCombinedRoleMappingEnabledSet()) {
               var2.append("CombinedRoleMappingEnabled");
               var2.append(String.valueOf(this.bean.isCombinedRoleMappingEnabled()));
            }

            if (this.bean.isDefaultRealmSet()) {
               var2.append("DefaultRealm");
               var2.append(String.valueOf(this.bean.isDefaultRealm()));
            }

            if (this.bean.isDelegateMBeanAuthorizationSet()) {
               var2.append("DelegateMBeanAuthorization");
               var2.append(String.valueOf(this.bean.isDelegateMBeanAuthorization()));
            }

            if (this.bean.isDeployCredentialMappingIgnoredSet()) {
               var2.append("DeployCredentialMappingIgnored");
               var2.append(String.valueOf(this.bean.isDeployCredentialMappingIgnored()));
            }

            if (this.bean.isDeployPolicyIgnoredSet()) {
               var2.append("DeployPolicyIgnored");
               var2.append(String.valueOf(this.bean.isDeployPolicyIgnored()));
            }

            if (this.bean.isDeployRoleIgnoredSet()) {
               var2.append("DeployRoleIgnored");
               var2.append(String.valueOf(this.bean.isDeployRoleIgnored()));
            }

            if (this.bean.isDeployableProviderSynchronizationEnabledSet()) {
               var2.append("DeployableProviderSynchronizationEnabled");
               var2.append(String.valueOf(this.bean.isDeployableProviderSynchronizationEnabled()));
            }

            if (this.bean.isEnableWebLogicPrincipalValidatorCacheSet()) {
               var2.append("EnableWebLogicPrincipalValidatorCache");
               var2.append(String.valueOf(this.bean.isEnableWebLogicPrincipalValidatorCache()));
            }

            if (this.bean.isFullyDelegateAuthorizationSet()) {
               var2.append("FullyDelegateAuthorization");
               var2.append(String.valueOf(this.bean.isFullyDelegateAuthorization()));
            }

            if (this.bean.isValidateDDSecurityDataSet()) {
               var2.append("ValidateDDSecurityData");
               var2.append(String.valueOf(this.bean.isValidateDDSecurityData()));
            }

            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            RealmMBeanImpl var2 = (RealmMBeanImpl)var1;
            this.computeChildDiff("Adjudicator", this.bean.getAdjudicator(), var2.getAdjudicator(), false);
            this.computeChildDiff("Auditors", this.bean.getAuditors(), var2.getAuditors(), false, true);
            this.computeDiff("AuthMethods", this.bean.getAuthMethods(), var2.getAuthMethods(), false);
            this.computeChildDiff("AuthenticationProviders", this.bean.getAuthenticationProviders(), var2.getAuthenticationProviders(), false, true);
            this.computeChildDiff("Authorizers", this.bean.getAuthorizers(), var2.getAuthorizers(), false, true);
            this.computeDiff("CertPathBuilder", this.bean.getCertPathBuilder(), var2.getCertPathBuilder(), false);
            this.computeChildDiff("CertPathProviders", this.bean.getCertPathProviders(), var2.getCertPathProviders(), false, true);
            this.computeDiff("CompatibilityObjectName", this.bean.getCompatibilityObjectName(), var2.getCompatibilityObjectName(), false);
            this.computeChildDiff("CredentialMappers", this.bean.getCredentialMappers(), var2.getCredentialMappers(), false, true);
            this.computeDiff("DeployableProviderSynchronizationTimeout", this.bean.getDeployableProviderSynchronizationTimeout(), var2.getDeployableProviderSynchronizationTimeout(), false);
            this.computeChildDiff("KeyStores", this.bean.getKeyStores(), var2.getKeyStores(), false, true);
            this.computeDiff("MaxWebLogicPrincipalsInCache", this.bean.getMaxWebLogicPrincipalsInCache(), var2.getMaxWebLogicPrincipalsInCache(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeChildDiff("PasswordValidators", this.bean.getPasswordValidators(), var2.getPasswordValidators(), false, true);
            this.computeChildDiff("RDBMSSecurityStore", this.bean.getRDBMSSecurityStore(), var2.getRDBMSSecurityStore(), false);
            this.computeChildDiff("RoleMappers", this.bean.getRoleMappers(), var2.getRoleMappers(), false, true);
            this.computeDiff("SecurityDDModel", this.bean.getSecurityDDModel(), var2.getSecurityDDModel(), true);
            this.computeSubDiff("UserLockoutManager", this.bean.getUserLockoutManager(), var2.getUserLockoutManager());
            this.computeDiff("CombinedRoleMappingEnabled", this.bean.isCombinedRoleMappingEnabled(), var2.isCombinedRoleMappingEnabled(), true);
            this.computeDiff("DelegateMBeanAuthorization", this.bean.isDelegateMBeanAuthorization(), var2.isDelegateMBeanAuthorization(), false);
            this.computeDiff("DeployCredentialMappingIgnored", this.bean.isDeployCredentialMappingIgnored(), var2.isDeployCredentialMappingIgnored(), true);
            this.computeDiff("DeployPolicyIgnored", this.bean.isDeployPolicyIgnored(), var2.isDeployPolicyIgnored(), true);
            this.computeDiff("DeployRoleIgnored", this.bean.isDeployRoleIgnored(), var2.isDeployRoleIgnored(), true);
            this.computeDiff("DeployableProviderSynchronizationEnabled", this.bean.isDeployableProviderSynchronizationEnabled(), var2.isDeployableProviderSynchronizationEnabled(), false);
            this.computeDiff("EnableWebLogicPrincipalValidatorCache", this.bean.isEnableWebLogicPrincipalValidatorCache(), var2.isEnableWebLogicPrincipalValidatorCache(), false);
            this.computeDiff("FullyDelegateAuthorization", this.bean.isFullyDelegateAuthorization(), var2.isFullyDelegateAuthorization(), false);
            this.computeDiff("ValidateDDSecurityData", this.bean.isValidateDDSecurityData(), var2.isValidateDDSecurityData(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            RealmMBeanImpl var3 = (RealmMBeanImpl)var1.getSourceBean();
            RealmMBeanImpl var4 = (RealmMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("Adjudicator")) {
                  if (var6 == 2) {
                     var3.setAdjudicator((AdjudicatorMBean)this.createCopy((AbstractDescriptorBean)var4.getAdjudicator()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("Adjudicator", var3.getAdjudicator());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (!var5.equals("AdjudicatorTypes") && !var5.equals("AuditorTypes")) {
                  if (var5.equals("Auditors")) {
                     if (var6 == 2) {
                        var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                        var3.addAuditor((AuditorMBean)var2.getAddedObject());
                        this.reorderArrayObjects(var3.getAuditors(), var4.getAuditors());
                     } else if (var6 == 3) {
                        var3.removeAuditor((AuditorMBean)var2.getRemovedObject());
                     } else {
                        this.reorderArrayObjects(var3.getAuditors(), var4.getAuditors());
                     }

                     if (var3.getAuditors() == null || var3.getAuditors().length == 0) {
                        var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                     }
                  } else if (var5.equals("AuthMethods")) {
                     var3.setAuthMethods(var4.getAuthMethods());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 32);
                  } else if (!var5.equals("AuthenticationProviderTypes")) {
                     if (var5.equals("AuthenticationProviders")) {
                        if (var6 == 2) {
                           var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                           var3.addAuthenticationProvider((AuthenticationProviderMBean)var2.getAddedObject());
                           this.reorderArrayObjects(var3.getAuthenticationProviders(), var4.getAuthenticationProviders());
                        } else if (var6 == 3) {
                           var3.removeAuthenticationProvider((AuthenticationProviderMBean)var2.getRemovedObject());
                        } else {
                           this.reorderArrayObjects(var3.getAuthenticationProviders(), var4.getAuthenticationProviders());
                        }

                        if (var3.getAuthenticationProviders() == null || var3.getAuthenticationProviders().length == 0) {
                           var3._conditionalUnset(var2.isUnsetUpdate(), 4);
                        }
                     } else if (!var5.equals("AuthorizerTypes")) {
                        if (var5.equals("Authorizers")) {
                           if (var6 == 2) {
                              var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                              var3.addAuthorizer((AuthorizerMBean)var2.getAddedObject());
                              this.reorderArrayObjects(var3.getAuthorizers(), var4.getAuthorizers());
                           } else if (var6 == 3) {
                              var3.removeAuthorizer((AuthorizerMBean)var2.getRemovedObject());
                           } else {
                              this.reorderArrayObjects(var3.getAuthorizers(), var4.getAuthorizers());
                           }

                           if (var3.getAuthorizers() == null || var3.getAuthorizers().length == 0) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 8);
                           }
                        } else if (var5.equals("CertPathBuilder")) {
                           var3.setCertPathBuilderAsString(var4.getCertPathBuilderAsString());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 16);
                        } else if (!var5.equals("CertPathProviderTypes")) {
                           if (var5.equals("CertPathProviders")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addCertPathProvider((CertPathProviderMBean)var2.getAddedObject());
                                 this.reorderArrayObjects(var3.getCertPathProviders(), var4.getCertPathProviders());
                              } else if (var6 == 3) {
                                 var3.removeCertPathProvider((CertPathProviderMBean)var2.getRemovedObject());
                              } else {
                                 this.reorderArrayObjects(var3.getCertPathProviders(), var4.getCertPathProviders());
                              }

                              if (var3.getCertPathProviders() == null || var3.getCertPathProviders().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 14);
                              }
                           } else if (var5.equals("CompatibilityObjectName")) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 33);
                           } else if (!var5.equals("CredentialMapperTypes")) {
                              if (var5.equals("CredentialMappers")) {
                                 if (var6 == 2) {
                                    var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                    var3.addCredentialMapper((CredentialMapperMBean)var2.getAddedObject());
                                    this.reorderArrayObjects(var3.getCredentialMappers(), var4.getCredentialMappers());
                                 } else if (var6 == 3) {
                                    var3.removeCredentialMapper((CredentialMapperMBean)var2.getRemovedObject());
                                 } else {
                                    this.reorderArrayObjects(var3.getCredentialMappers(), var4.getCredentialMappers());
                                 }

                                 if (var3.getCredentialMappers() == null || var3.getCredentialMappers().length == 0) {
                                    var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                                 }
                              } else if (var5.equals("DeployableProviderSynchronizationTimeout")) {
                                 var3.setDeployableProviderSynchronizationTimeout(var4.getDeployableProviderSynchronizationTimeout());
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 38);
                              } else if (!var5.equals("KeyStoreTypes")) {
                                 if (var5.equals("KeyStores")) {
                                    if (var6 == 2) {
                                       var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                       var3.addKeyStore((KeyStoreMBean)var2.getAddedObject());
                                       this.reorderArrayObjects(var3.getKeyStores(), var4.getKeyStores());
                                    } else if (var6 == 3) {
                                       var3.removeKeyStore((KeyStoreMBean)var2.getRemovedObject());
                                    } else {
                                       this.reorderArrayObjects(var3.getKeyStores(), var4.getKeyStores());
                                    }

                                    if (var3.getKeyStores() == null || var3.getKeyStores().length == 0) {
                                       var3._conditionalUnset(var2.isUnsetUpdate(), 17);
                                    }
                                 } else if (var5.equals("MaxWebLogicPrincipalsInCache")) {
                                    var3.setMaxWebLogicPrincipalsInCache(var4.getMaxWebLogicPrincipalsInCache());
                                    var3._conditionalUnset(var2.isUnsetUpdate(), 29);
                                 } else if (var5.equals("Name")) {
                                    var3.setName(var4.getName());
                                    var3._conditionalUnset(var2.isUnsetUpdate(), 30);
                                 } else if (!var5.equals("PasswordValidatorTypes")) {
                                    if (var5.equals("PasswordValidators")) {
                                       if (var6 == 2) {
                                          var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                          var3.addPasswordValidator((PasswordValidatorMBean)var2.getAddedObject());
                                          this.reorderArrayObjects(var3.getPasswordValidators(), var4.getPasswordValidators());
                                       } else if (var6 == 3) {
                                          var3.removePasswordValidator((PasswordValidatorMBean)var2.getRemovedObject());
                                       } else {
                                          this.reorderArrayObjects(var3.getPasswordValidators(), var4.getPasswordValidators());
                                       }

                                       if (var3.getPasswordValidators() == null || var3.getPasswordValidators().length == 0) {
                                          var3._conditionalUnset(var2.isUnsetUpdate(), 36);
                                       }
                                    } else if (var5.equals("RDBMSSecurityStore")) {
                                       if (var6 == 2) {
                                          var3.setRDBMSSecurityStore((RDBMSSecurityStoreMBean)this.createCopy((AbstractDescriptorBean)var4.getRDBMSSecurityStore()));
                                       } else {
                                          if (var6 != 3) {
                                             throw new AssertionError("Invalid type: " + var6);
                                          }

                                          var3._destroySingleton("RDBMSSecurityStore", var3.getRDBMSSecurityStore());
                                       }

                                       var3._conditionalUnset(var2.isUnsetUpdate(), 34);
                                    } else if (!var5.equals("RoleMapperTypes")) {
                                       if (var5.equals("RoleMappers")) {
                                          if (var6 == 2) {
                                             var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                             var3.addRoleMapper((RoleMapperMBean)var2.getAddedObject());
                                             this.reorderArrayObjects(var3.getRoleMappers(), var4.getRoleMappers());
                                          } else if (var6 == 3) {
                                             var3.removeRoleMapper((RoleMapperMBean)var2.getRemovedObject());
                                          } else {
                                             this.reorderArrayObjects(var3.getRoleMappers(), var4.getRoleMappers());
                                          }

                                          if (var3.getRoleMappers() == null || var3.getRoleMappers().length == 0) {
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 6);
                                          }
                                       } else if (var5.equals("SecurityDDModel")) {
                                          var3.setSecurityDDModel(var4.getSecurityDDModel());
                                          var3._conditionalUnset(var2.isUnsetUpdate(), 25);
                                       } else if (var5.equals("UserLockoutManager")) {
                                          if (var6 == 2) {
                                             var3.setUserLockoutManager((UserLockoutManagerMBean)this.createCopy((AbstractDescriptorBean)var4.getUserLockoutManager()));
                                          } else {
                                             if (var6 != 3) {
                                                throw new AssertionError("Invalid type: " + var6);
                                             }

                                             var3._destroySingleton("UserLockoutManager", var3.getUserLockoutManager());
                                          }

                                          var3._conditionalUnset(var2.isUnsetUpdate(), 19);
                                       } else if (var5.equals("CombinedRoleMappingEnabled")) {
                                          var3.setCombinedRoleMappingEnabled(var4.isCombinedRoleMappingEnabled());
                                          var3._conditionalUnset(var2.isUnsetUpdate(), 26);
                                       } else if (!var5.equals("DefaultRealm")) {
                                          if (var5.equals("DelegateMBeanAuthorization")) {
                                             var3.setDelegateMBeanAuthorization(var4.isDelegateMBeanAuthorization());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 31);
                                          } else if (var5.equals("DeployCredentialMappingIgnored")) {
                                             var3.setDeployCredentialMappingIgnored(var4.isDeployCredentialMappingIgnored());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 22);
                                          } else if (var5.equals("DeployPolicyIgnored")) {
                                             var3.setDeployPolicyIgnored(var4.isDeployPolicyIgnored());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 21);
                                          } else if (var5.equals("DeployRoleIgnored")) {
                                             var3.setDeployRoleIgnored(var4.isDeployRoleIgnored());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 20);
                                          } else if (var5.equals("DeployableProviderSynchronizationEnabled")) {
                                             var3.setDeployableProviderSynchronizationEnabled(var4.isDeployableProviderSynchronizationEnabled());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 37);
                                          } else if (var5.equals("EnableWebLogicPrincipalValidatorCache")) {
                                             var3.setEnableWebLogicPrincipalValidatorCache(var4.isEnableWebLogicPrincipalValidatorCache());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 28);
                                          } else if (var5.equals("FullyDelegateAuthorization")) {
                                             var3.setFullyDelegateAuthorization(var4.isFullyDelegateAuthorization());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 23);
                                          } else if (var5.equals("ValidateDDSecurityData")) {
                                             var3.setValidateDDSecurityData(var4.isValidateDDSecurityData());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 24);
                                          } else {
                                             super.applyPropertyUpdate(var1, var2);
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }

            }
         } catch (RuntimeException var7) {
            throw var7;
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected AbstractDescriptorBean finishCopy(AbstractDescriptorBean var1, boolean var2, List var3) {
         try {
            RealmMBeanImpl var5 = (RealmMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("Adjudicator")) && this.bean.isAdjudicatorSet() && !var5._isSet(10)) {
               AdjudicatorMBean var4 = this.bean.getAdjudicator();
               var5.setAdjudicator((AdjudicatorMBean)null);
               var5.setAdjudicator(var4 == null ? null : (AdjudicatorMBean)this.createCopy((AbstractDescriptorBean)var4, var2));
            }

            int var8;
            if ((var3 == null || !var3.contains("Auditors")) && this.bean.isAuditorsSet() && !var5._isSet(2)) {
               AuditorMBean[] var6 = this.bean.getAuditors();
               AuditorMBean[] var7 = new AuditorMBean[var6.length];

               for(var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (AuditorMBean)((AuditorMBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setAuditors(var7);
            }

            if ((var3 == null || !var3.contains("AuthMethods")) && this.bean.isAuthMethodsSet()) {
               var5.setAuthMethods(this.bean.getAuthMethods());
            }

            if ((var3 == null || !var3.contains("AuthenticationProviders")) && this.bean.isAuthenticationProvidersSet() && !var5._isSet(4)) {
               AuthenticationProviderMBean[] var13 = this.bean.getAuthenticationProviders();
               AuthenticationProviderMBean[] var16 = new AuthenticationProviderMBean[var13.length];

               for(var8 = 0; var8 < var16.length; ++var8) {
                  var16[var8] = (AuthenticationProviderMBean)((AuthenticationProviderMBean)this.createCopy((AbstractDescriptorBean)var13[var8], var2));
               }

               var5.setAuthenticationProviders(var16);
            }

            if ((var3 == null || !var3.contains("Authorizers")) && this.bean.isAuthorizersSet() && !var5._isSet(8)) {
               AuthorizerMBean[] var14 = this.bean.getAuthorizers();
               AuthorizerMBean[] var18 = new AuthorizerMBean[var14.length];

               for(var8 = 0; var8 < var18.length; ++var8) {
                  var18[var8] = (AuthorizerMBean)((AuthorizerMBean)this.createCopy((AbstractDescriptorBean)var14[var8], var2));
               }

               var5.setAuthorizers(var18);
            }

            if ((var3 == null || !var3.contains("CertPathBuilder")) && this.bean.isCertPathBuilderSet()) {
               var5._unSet(var5, 16);
               var5.setCertPathBuilderAsString(this.bean.getCertPathBuilderAsString());
            }

            if ((var3 == null || !var3.contains("CertPathProviders")) && this.bean.isCertPathProvidersSet() && !var5._isSet(14)) {
               CertPathProviderMBean[] var15 = this.bean.getCertPathProviders();
               CertPathProviderMBean[] var20 = new CertPathProviderMBean[var15.length];

               for(var8 = 0; var8 < var20.length; ++var8) {
                  var20[var8] = (CertPathProviderMBean)((CertPathProviderMBean)this.createCopy((AbstractDescriptorBean)var15[var8], var2));
               }

               var5.setCertPathProviders(var20);
            }

            if ((var3 == null || !var3.contains("CompatibilityObjectName")) && this.bean.isCompatibilityObjectNameSet()) {
            }

            if ((var3 == null || !var3.contains("CredentialMappers")) && this.bean.isCredentialMappersSet() && !var5._isSet(12)) {
               CredentialMapperMBean[] var17 = this.bean.getCredentialMappers();
               CredentialMapperMBean[] var22 = new CredentialMapperMBean[var17.length];

               for(var8 = 0; var8 < var22.length; ++var8) {
                  var22[var8] = (CredentialMapperMBean)((CredentialMapperMBean)this.createCopy((AbstractDescriptorBean)var17[var8], var2));
               }

               var5.setCredentialMappers(var22);
            }

            if ((var3 == null || !var3.contains("DeployableProviderSynchronizationTimeout")) && this.bean.isDeployableProviderSynchronizationTimeoutSet()) {
               var5.setDeployableProviderSynchronizationTimeout(this.bean.getDeployableProviderSynchronizationTimeout());
            }

            if ((var3 == null || !var3.contains("KeyStores")) && this.bean.isKeyStoresSet() && !var5._isSet(17)) {
               KeyStoreMBean[] var19 = this.bean.getKeyStores();
               KeyStoreMBean[] var24 = new KeyStoreMBean[var19.length];

               for(var8 = 0; var8 < var24.length; ++var8) {
                  var24[var8] = (KeyStoreMBean)((KeyStoreMBean)this.createCopy((AbstractDescriptorBean)var19[var8], var2));
               }

               var5.setKeyStores(var24);
            }

            if ((var3 == null || !var3.contains("MaxWebLogicPrincipalsInCache")) && this.bean.isMaxWebLogicPrincipalsInCacheSet()) {
               var5.setMaxWebLogicPrincipalsInCache(this.bean.getMaxWebLogicPrincipalsInCache());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("PasswordValidators")) && this.bean.isPasswordValidatorsSet() && !var5._isSet(36)) {
               PasswordValidatorMBean[] var21 = this.bean.getPasswordValidators();
               PasswordValidatorMBean[] var25 = new PasswordValidatorMBean[var21.length];

               for(var8 = 0; var8 < var25.length; ++var8) {
                  var25[var8] = (PasswordValidatorMBean)((PasswordValidatorMBean)this.createCopy((AbstractDescriptorBean)var21[var8], var2));
               }

               var5.setPasswordValidators(var25);
            }

            if ((var3 == null || !var3.contains("RDBMSSecurityStore")) && this.bean.isRDBMSSecurityStoreSet() && !var5._isSet(34)) {
               RDBMSSecurityStoreMBean var11 = this.bean.getRDBMSSecurityStore();
               var5.setRDBMSSecurityStore((RDBMSSecurityStoreMBean)null);
               var5.setRDBMSSecurityStore(var11 == null ? null : (RDBMSSecurityStoreMBean)this.createCopy((AbstractDescriptorBean)var11, var2));
            }

            if ((var3 == null || !var3.contains("RoleMappers")) && this.bean.isRoleMappersSet() && !var5._isSet(6)) {
               RoleMapperMBean[] var23 = this.bean.getRoleMappers();
               RoleMapperMBean[] var26 = new RoleMapperMBean[var23.length];

               for(var8 = 0; var8 < var26.length; ++var8) {
                  var26[var8] = (RoleMapperMBean)((RoleMapperMBean)this.createCopy((AbstractDescriptorBean)var23[var8], var2));
               }

               var5.setRoleMappers(var26);
            }

            if ((var3 == null || !var3.contains("SecurityDDModel")) && this.bean.isSecurityDDModelSet()) {
               var5.setSecurityDDModel(this.bean.getSecurityDDModel());
            }

            if ((var3 == null || !var3.contains("UserLockoutManager")) && this.bean.isUserLockoutManagerSet() && !var5._isSet(19)) {
               UserLockoutManagerMBean var12 = this.bean.getUserLockoutManager();
               var5.setUserLockoutManager((UserLockoutManagerMBean)null);
               var5.setUserLockoutManager(var12 == null ? null : (UserLockoutManagerMBean)this.createCopy((AbstractDescriptorBean)var12, var2));
            }

            if ((var3 == null || !var3.contains("CombinedRoleMappingEnabled")) && this.bean.isCombinedRoleMappingEnabledSet()) {
               var5.setCombinedRoleMappingEnabled(this.bean.isCombinedRoleMappingEnabled());
            }

            if ((var3 == null || !var3.contains("DelegateMBeanAuthorization")) && this.bean.isDelegateMBeanAuthorizationSet()) {
               var5.setDelegateMBeanAuthorization(this.bean.isDelegateMBeanAuthorization());
            }

            if ((var3 == null || !var3.contains("DeployCredentialMappingIgnored")) && this.bean.isDeployCredentialMappingIgnoredSet()) {
               var5.setDeployCredentialMappingIgnored(this.bean.isDeployCredentialMappingIgnored());
            }

            if ((var3 == null || !var3.contains("DeployPolicyIgnored")) && this.bean.isDeployPolicyIgnoredSet()) {
               var5.setDeployPolicyIgnored(this.bean.isDeployPolicyIgnored());
            }

            if ((var3 == null || !var3.contains("DeployRoleIgnored")) && this.bean.isDeployRoleIgnoredSet()) {
               var5.setDeployRoleIgnored(this.bean.isDeployRoleIgnored());
            }

            if ((var3 == null || !var3.contains("DeployableProviderSynchronizationEnabled")) && this.bean.isDeployableProviderSynchronizationEnabledSet()) {
               var5.setDeployableProviderSynchronizationEnabled(this.bean.isDeployableProviderSynchronizationEnabled());
            }

            if ((var3 == null || !var3.contains("EnableWebLogicPrincipalValidatorCache")) && this.bean.isEnableWebLogicPrincipalValidatorCacheSet()) {
               var5.setEnableWebLogicPrincipalValidatorCache(this.bean.isEnableWebLogicPrincipalValidatorCache());
            }

            if ((var3 == null || !var3.contains("FullyDelegateAuthorization")) && this.bean.isFullyDelegateAuthorizationSet()) {
               var5.setFullyDelegateAuthorization(this.bean.isFullyDelegateAuthorization());
            }

            if ((var3 == null || !var3.contains("ValidateDDSecurityData")) && this.bean.isValidateDDSecurityDataSet()) {
               var5.setValidateDDSecurityData(this.bean.isValidateDDSecurityData());
            }

            return var5;
         } catch (RuntimeException var9) {
            throw var9;
         } catch (Exception var10) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var10);
         }
      }

      protected void inferSubTree(Class var1, Object var2) {
         super.inferSubTree(var1, var2);
         Object var3 = null;
         this.inferSubTree(this.bean.getAdjudicator(), var1, var2);
         this.inferSubTree(this.bean.getAuditors(), var1, var2);
         this.inferSubTree(this.bean.getAuthenticationProviders(), var1, var2);
         this.inferSubTree(this.bean.getAuthorizers(), var1, var2);
         this.inferSubTree(this.bean.getCertPathBuilder(), var1, var2);
         this.inferSubTree(this.bean.getCertPathProviders(), var1, var2);
         this.inferSubTree(this.bean.getCredentialMappers(), var1, var2);
         this.inferSubTree(this.bean.getKeyStores(), var1, var2);
         this.inferSubTree(this.bean.getPasswordValidators(), var1, var2);
         this.inferSubTree(this.bean.getRDBMSSecurityStore(), var1, var2);
         this.inferSubTree(this.bean.getRoleMappers(), var1, var2);
         this.inferSubTree(this.bean.getUserLockoutManager(), var1, var2);
      }
   }
}
