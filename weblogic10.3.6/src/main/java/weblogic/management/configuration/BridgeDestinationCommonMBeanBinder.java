package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class BridgeDestinationCommonMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private BridgeDestinationCommonMBeanImpl bean;

   protected BridgeDestinationCommonMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (BridgeDestinationCommonMBeanImpl)var1;
   }

   public BridgeDestinationCommonMBeanBinder() {
      super(new BridgeDestinationCommonMBeanImpl());
      this.bean = (BridgeDestinationCommonMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("AdapterJNDIName")) {
                  try {
                     this.bean.setAdapterJNDIName((String)var2);
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("Classpath")) {
                  try {
                     this.bean.setClasspath((String)var2);
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("UserName")) {
                  try {
                     this.bean.setUserName((String)var2);
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("UserPassword")) {
                  try {
                     if (this.bean.isUserPasswordEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to UserPassword [ BridgeDestinationCommonMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setUserPassword((String)var2);
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else if (var1.equals("UserPasswordEncrypted")) {
                  if (this.bean.isUserPasswordEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to UserPasswordEncrypted [ BridgeDestinationCommonMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setUserPasswordEncryptedAsString((String)var2);
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var9) {
         System.out.println(var9 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var9;
      } catch (RuntimeException var10) {
         throw var10;
      } catch (Exception var11) {
         if (var11 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var11);
         } else if (var11 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var11.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var11);
         }
      }
   }
}
