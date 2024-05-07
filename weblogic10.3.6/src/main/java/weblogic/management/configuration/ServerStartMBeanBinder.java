package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class ServerStartMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private ServerStartMBeanImpl bean;

   protected ServerStartMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (ServerStartMBeanImpl)var1;
   }

   public ServerStartMBeanBinder() {
      super(new ServerStartMBeanImpl());
      this.bean = (ServerStartMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("Arguments")) {
                  try {
                     this.bean.setArguments((String)var2);
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("BeaHome")) {
                  try {
                     this.bean.setBeaHome((String)var2);
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("ClassPath")) {
                  try {
                     this.bean.setClassPath((String)var2);
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("JavaHome")) {
                  try {
                     this.bean.setJavaHome((String)var2);
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("JavaVendor")) {
                  try {
                     this.bean.setJavaVendor((String)var2);
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("MaxRestartCount")) {
                  this.handleDeprecatedProperty("MaxRestartCount", "10.0.0.0 replaced by ServerMBean.getRestartMax");

                  try {
                     this.bean.setMaxRestartCount(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("OutputFile")) {
                  try {
                     this.bean.setOutputFile((String)var2);
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("Password")) {
                  try {
                     if (this.bean.isPasswordEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to Password [ ServerStartMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setPassword((String)var2);
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("PasswordEncrypted")) {
                  if (this.bean.isPasswordEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to PasswordEncrypted [ ServerStartMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setPasswordEncryptedAsString((String)var2);
               } else if (var1.equals("RootDirectory")) {
                  try {
                     this.bean.setRootDirectory((String)var2);
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("SecurityPolicyFile")) {
                  try {
                     this.bean.setSecurityPolicyFile((String)var2);
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("Username")) {
                  try {
                     this.bean.setUsername((String)var2);
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var17) {
         System.out.println(var17 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var17;
      } catch (RuntimeException var18) {
         throw var18;
      } catch (Exception var19) {
         if (var19 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var19);
         } else if (var19 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var19.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var19);
         }
      }
   }
}
