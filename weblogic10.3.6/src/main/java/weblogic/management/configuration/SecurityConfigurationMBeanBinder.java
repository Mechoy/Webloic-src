package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.ReadOnlyMBeanBinder;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class SecurityConfigurationMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private SecurityConfigurationMBeanImpl bean;

   protected SecurityConfigurationMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (SecurityConfigurationMBeanImpl)var1;
   }

   public SecurityConfigurationMBeanBinder() {
      super(new SecurityConfigurationMBeanImpl());
      this.bean = (SecurityConfigurationMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("CertRevoc")) {
                  try {
                     this.bean.setCertRevoc((CertRevocMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var28) {
                     System.out.println("Warning: multiple definitions with same name: " + var28.getMessage());
                  }
               } else if (var1.equals("CompatibilityConnectionFiltersEnabled")) {
                  try {
                     this.bean.setCompatibilityConnectionFiltersEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var27) {
                     System.out.println("Warning: multiple definitions with same name: " + var27.getMessage());
                  }
               } else if (var1.equals("ConnectionFilter")) {
                  try {
                     this.bean.setConnectionFilter((String)var2);
                  } catch (BeanAlreadyExistsException var26) {
                     System.out.println("Warning: multiple definitions with same name: " + var26.getMessage());
                  }
               } else if (var1.equals("ConnectionFilterRules")) {
                  try {
                     this.bean.setConnectionFilterRules(this.parseStringArrayInitializer((String)var2));
                  } catch (BeanAlreadyExistsException var25) {
                     System.out.println("Warning: multiple definitions with same name: " + var25.getMessage());
                  }
               } else if (var1.equals("ConnectionLoggerEnabled")) {
                  try {
                     this.bean.setConnectionLoggerEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var24) {
                     System.out.println("Warning: multiple definitions with same name: " + var24.getMessage());
                  }
               } else if (var1.equals("Credential")) {
                  try {
                     if (this.bean.isCredentialEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to Credential [ SecurityConfigurationMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setCredential((String)var2);
                  } catch (BeanAlreadyExistsException var23) {
                     System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                  }
               } else if (var1.equals("CredentialEncrypted")) {
                  if (this.bean.isCredentialEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to CredentialEncrypted [ SecurityConfigurationMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setCredentialEncryptedAsString((String)var2);
               } else if (var1.equals("DefaultRealm")) {
                  this.bean.setDefaultRealmAsString((String)var2);
               } else if (var1.equals("DefaultRealmInternal")) {
                  this.bean.setDefaultRealmInternalAsString((String)var2);
               } else if (var1.equals("DowngradeUntrustedPrincipals")) {
                  try {
                     this.bean.setDowngradeUntrustedPrincipals(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var22) {
                     System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                  }
               } else if (var1.equals("EnforceStrictURLPattern")) {
                  try {
                     this.bean.setEnforceStrictURLPattern(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var21) {
                     System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                  }
               } else if (var1.equals("EnforceValidBasicAuthCredentials")) {
                  try {
                     this.bean.setEnforceValidBasicAuthCredentials(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var20) {
                     System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                  }
               } else if (var1.equals("ExcludedDomainNames")) {
                  try {
                     this.bean.setExcludedDomainNames(this.parseStringArrayInitializer((String)var2));
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("NodeManagerPassword")) {
                  try {
                     if (this.bean.isNodeManagerPasswordEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to NodeManagerPassword [ SecurityConfigurationMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setNodeManagerPassword((String)var2);
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("NodeManagerPasswordEncrypted")) {
                  if (this.bean.isNodeManagerPasswordEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to NodeManagerPasswordEncrypted [ SecurityConfigurationMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setNodeManagerPasswordEncryptedAsString((String)var2);
               } else if (var1.equals("NodeManagerUsername")) {
                  try {
                     this.bean.setNodeManagerUsername((String)var2);
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("RealmBootStrapVersion")) {
                  try {
                     this.bean.setRealmBootStrapVersion((String)var2);
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("Realm")) {
                  try {
                     this.bean.addRealm((weblogic.management.security.RealmMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                     this.bean.removeRealm((weblogic.management.security.RealmMBean)var14.getExistingBean());
                     this.bean.addRealm((weblogic.management.security.RealmMBean)((AbstractDescriptorBean)((weblogic.management.security.RealmMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("WebAppFilesCaseInsensitive")) {
                  try {
                     this.bean.setWebAppFilesCaseInsensitive((String)var2);
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("AnonymousAdminLookupEnabled")) {
                  try {
                     this.bean.setAnonymousAdminLookupEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("ClearTextCredentialAccessEnabled")) {
                  try {
                     this.bean.setClearTextCredentialAccessEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("ConsoleFullDelegationEnabled")) {
                  try {
                     this.bean.setConsoleFullDelegationEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("CredentialGenerated")) {
                  try {
                     this.bean.setCredentialGenerated(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("CrossDomainSecurityEnabled")) {
                  try {
                     this.bean.setCrossDomainSecurityEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("PrincipalEqualsCaseInsensitive")) {
                  try {
                     this.bean.setPrincipalEqualsCaseInsensitive(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("PrincipalEqualsCompareDnAndGuid")) {
                  try {
                     this.bean.setPrincipalEqualsCompareDnAndGuid(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (this.isInstance(weblogic.management.security.RealmMBean.class, var1)) {
                  try {
                     this.bean.addRealm((weblogic.management.security.RealmMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name : " + var5.getMessage());
                     this.bean.removeRealm((weblogic.management.security.RealmMBean)var5.getExistingBean());
                     this.bean.addRealm((weblogic.management.security.RealmMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var29) {
         System.out.println(var29 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var29;
      } catch (RuntimeException var30) {
         throw var30;
      } catch (Exception var31) {
         if (var31 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var31);
         } else if (var31 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var31.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var31);
         }
      }
   }
}
