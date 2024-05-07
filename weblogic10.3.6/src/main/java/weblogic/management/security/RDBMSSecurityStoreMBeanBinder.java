package weblogic.management.security;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class RDBMSSecurityStoreMBeanBinder extends SecurityReadOnlyMBeanBinder implements AttributeBinder {
   private RDBMSSecurityStoreMBeanImpl bean;

   protected RDBMSSecurityStoreMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (RDBMSSecurityStoreMBeanImpl)var1;
   }

   public RDBMSSecurityStoreMBeanBinder() {
      super(new RDBMSSecurityStoreMBeanImpl());
      this.bean = (RDBMSSecurityStoreMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("CompatibilityObjectName")) {
                  throw new AssertionError("can't set read-only property CompatibilityObjectName");
               }

               if (var1.equals("ConnectionProperties")) {
                  try {
                     this.bean.setConnectionProperties((String)var2);
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("ConnectionURL")) {
                  try {
                     this.bean.setConnectionURL((String)var2);
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("DriverName")) {
                  try {
                     this.bean.setDriverName((String)var2);
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("JMSExceptionReconnectAttempts")) {
                  try {
                     this.bean.setJMSExceptionReconnectAttempts(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("JMSTopic")) {
                  try {
                     this.bean.setJMSTopic((String)var2);
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("JMSTopicConnectionFactory")) {
                  try {
                     this.bean.setJMSTopicConnectionFactory((String)var2);
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("JNDIPassword")) {
                  try {
                     if (this.bean.isJNDIPasswordEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to JNDIPassword [ RDBMSSecurityStoreMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setJNDIPassword((String)var2);
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("JNDIPasswordEncrypted")) {
                  if (this.bean.isJNDIPasswordEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to JNDIPasswordEncrypted [ RDBMSSecurityStoreMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setJNDIPasswordEncryptedAsString((String)var2);
               } else if (var1.equals("JNDIUsername")) {
                  try {
                     this.bean.setJNDIUsername((String)var2);
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("NotificationProperties")) {
                  try {
                     this.bean.setNotificationProperties((String)var2);
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("Password")) {
                  try {
                     if (this.bean.isPasswordEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to Password [ RDBMSSecurityStoreMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setPassword((String)var2);
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("PasswordEncrypted")) {
                  if (this.bean.isPasswordEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to PasswordEncrypted [ RDBMSSecurityStoreMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setPasswordEncryptedAsString((String)var2);
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
