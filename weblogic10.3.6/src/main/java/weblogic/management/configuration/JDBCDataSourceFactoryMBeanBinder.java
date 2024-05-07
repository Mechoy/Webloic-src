package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class JDBCDataSourceFactoryMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private JDBCDataSourceFactoryMBeanImpl bean;

   protected JDBCDataSourceFactoryMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (JDBCDataSourceFactoryMBeanImpl)var1;
   }

   public JDBCDataSourceFactoryMBeanBinder() {
      super(new JDBCDataSourceFactoryMBeanImpl());
      this.bean = (JDBCDataSourceFactoryMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("DriverClassName")) {
                  try {
                     this.bean.setDriverClassName((String)var2);
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("FactoryName")) {
                  try {
                     this.bean.setFactoryName((String)var2);
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("Password")) {
                  try {
                     if (this.bean.isPasswordEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to Password [ JDBCDataSourceFactoryMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setPassword((String)var2);
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("PasswordEncrypted")) {
                  if (this.bean.isPasswordEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to PasswordEncrypted [ JDBCDataSourceFactoryMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setPasswordEncryptedAsString((String)var2);
               } else if (var1.equals("Properties")) {
                  this.bean.setPropertiesAsString((String)var2);
               } else if (var1.equals("URL")) {
                  try {
                     this.bean.setURL((String)var2);
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("UserName")) {
                  try {
                     this.bean.setUserName((String)var2);
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var11) {
         System.out.println(var11 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (Exception var13) {
         if (var13 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var13);
         } else if (var13 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var13.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var13);
         }
      }
   }
}
