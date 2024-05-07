package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class WLECConnectionPoolMBeanBinder extends DeploymentMBeanBinder implements AttributeBinder {
   private WLECConnectionPoolMBeanImpl bean;

   protected WLECConnectionPoolMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (WLECConnectionPoolMBeanImpl)var1;
   }

   public WLECConnectionPoolMBeanBinder() {
      super(new WLECConnectionPoolMBeanImpl());
      this.bean = (WLECConnectionPoolMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("ApplicationPassword")) {
                  try {
                     if (this.bean.isApplicationPasswordEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to ApplicationPassword [ WLECConnectionPoolMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setApplicationPassword((String)var2);
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("ApplicationPasswordEncrypted")) {
                  if (this.bean.isApplicationPasswordEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to ApplicationPasswordEncrypted [ WLECConnectionPoolMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setApplicationPasswordEncryptedAsString((String)var2);
               } else if (var1.equals("FailoverAddresses")) {
                  try {
                     this.bean.setFailoverAddresses(this.parseStringArrayInitializer((String)var2));
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("MaximumEncryptionLevel")) {
                  try {
                     this.bean.setMaximumEncryptionLevel(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("MaximumPoolSize")) {
                  try {
                     this.bean.setMaximumPoolSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("MinimumEncryptionLevel")) {
                  try {
                     this.bean.setMinimumEncryptionLevel(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("MinimumPoolSize")) {
                  try {
                     this.bean.setMinimumPoolSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("PrimaryAddresses")) {
                  try {
                     this.bean.setPrimaryAddresses(this.parseStringArrayInitializer((String)var2));
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("UserName")) {
                  try {
                     this.bean.setUserName((String)var2);
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("UserPassword")) {
                  try {
                     if (this.bean.isUserPasswordEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to UserPassword [ WLECConnectionPoolMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setUserPassword((String)var2);
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("UserPasswordEncrypted")) {
                  if (this.bean.isUserPasswordEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to UserPasswordEncrypted [ WLECConnectionPoolMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setUserPasswordEncryptedAsString((String)var2);
               } else if (var1.equals("UserRole")) {
                  try {
                     this.bean.setUserRole((String)var2);
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("WLEDomain")) {
                  try {
                     this.bean.setWLEDomain((String)var2);
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("CertificateAuthenticationEnabled")) {
                  try {
                     this.bean.setCertificateAuthenticationEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("SecurityContextEnabled")) {
                  try {
                     this.bean.setSecurityContextEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var18) {
         System.out.println(var18 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var18;
      } catch (RuntimeException var19) {
         throw var19;
      } catch (Exception var20) {
         if (var20 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var20);
         } else if (var20 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var20.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var20);
         }
      }
   }
}
