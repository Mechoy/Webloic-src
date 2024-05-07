package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class CustomRealmMBeanBinder extends BasicRealmMBeanBinder implements AttributeBinder {
   private CustomRealmMBeanImpl bean;

   protected CustomRealmMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (CustomRealmMBeanImpl)var1;
   }

   public CustomRealmMBeanBinder() {
      super(new CustomRealmMBeanImpl());
      this.bean = (CustomRealmMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("ConfigurationData")) {
                  this.bean.setConfigurationDataAsString((String)var2);
               } else if (var1.equals("Password")) {
                  try {
                     if (this.bean.isPasswordEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to Password [ CustomRealmMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setPassword((String)var2);
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("PasswordEncrypted")) {
                  if (this.bean.isPasswordEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to PasswordEncrypted [ CustomRealmMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setPasswordEncryptedAsString((String)var2);
               } else if (var1.equals("RealmClassName")) {
                  try {
                     this.bean.setRealmClassName((String)var2);
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var7) {
         System.out.println(var7 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var7;
      } catch (RuntimeException var8) {
         throw var8;
      } catch (Exception var9) {
         if (var9 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var9);
         } else if (var9 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var9.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var9);
         }
      }
   }
}
