package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class EmbeddedLDAPMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private EmbeddedLDAPMBeanImpl bean;

   protected EmbeddedLDAPMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (EmbeddedLDAPMBeanImpl)var1;
   }

   public EmbeddedLDAPMBeanBinder() {
      super(new EmbeddedLDAPMBeanImpl());
      this.bean = (EmbeddedLDAPMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("BackupCopies")) {
                  try {
                     this.bean.setBackupCopies(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("BackupHour")) {
                  try {
                     this.bean.setBackupHour(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("BackupMinute")) {
                  try {
                     this.bean.setBackupMinute(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("CacheSize")) {
                  try {
                     this.bean.setCacheSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("CacheTTL")) {
                  try {
                     this.bean.setCacheTTL(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("Credential")) {
                  try {
                     if (this.bean.isCredentialEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to Credential [ EmbeddedLDAPMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setCredential((String)var2);
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("CredentialEncrypted")) {
                  if (this.bean.isCredentialEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to CredentialEncrypted [ EmbeddedLDAPMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setCredentialEncryptedAsString((String)var2);
               } else if (var1.equals("Timeout")) {
                  try {
                     this.bean.setTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("AnonymousBindAllowed")) {
                  try {
                     this.bean.setAnonymousBindAllowed(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("CacheEnabled")) {
                  try {
                     this.bean.setCacheEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("MasterFirst")) {
                  try {
                     this.bean.setMasterFirst(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("RefreshReplicaAtStartup")) {
                  try {
                     this.bean.setRefreshReplicaAtStartup(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var16) {
         System.out.println(var16 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var16;
      } catch (RuntimeException var17) {
         throw var17;
      } catch (Exception var18) {
         if (var18 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var18);
         } else if (var18 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var18.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var18);
         }
      }
   }
}
