package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class ConfigurationPropertyMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private ConfigurationPropertyMBeanImpl bean;

   protected ConfigurationPropertyMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (ConfigurationPropertyMBeanImpl)var1;
   }

   public ConfigurationPropertyMBeanBinder() {
      super(new ConfigurationPropertyMBeanImpl());
      this.bean = (ConfigurationPropertyMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("EncryptedValue")) {
                  try {
                     if (this.bean.isEncryptedValueEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to EncryptedValue [ ConfigurationPropertyMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setEncryptedValue((String)var2);
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else {
                  if (var1.equals("EncryptedValueEncrypted")) {
                     throw new AssertionError("can't set read-only property EncryptedValueEncrypted");
                  }

                  if (var1.equals("Value")) {
                     try {
                        this.bean.setValue((String)var2);
                     } catch (BeanAlreadyExistsException var6) {
                        System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                     }
                  } else if (var1.equals("EncryptValueRequired")) {
                     try {
                        this.bean.setEncryptValueRequired(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var5) {
                        System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                     }
                  } else {
                     var3 = super.bindAttribute(var1, var2);
                  }
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var8) {
         System.out.println(var8 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var8;
      } catch (RuntimeException var9) {
         throw var9;
      } catch (Exception var10) {
         if (var10 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var10);
         } else if (var10 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var10.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var10);
         }
      }
   }
}
