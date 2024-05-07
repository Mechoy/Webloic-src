package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class PasswordPolicyMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private PasswordPolicyMBeanImpl bean;

   protected PasswordPolicyMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (PasswordPolicyMBeanImpl)var1;
   }

   public PasswordPolicyMBeanBinder() {
      super(new PasswordPolicyMBeanImpl());
      this.bean = (PasswordPolicyMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("LockoutCacheSize")) {
                  try {
                     this.bean.setLockoutCacheSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("LockoutDuration")) {
                  try {
                     this.bean.setLockoutDuration(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("LockoutGCThreshold")) {
                  try {
                     this.bean.setLockoutGCThreshold(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("LockoutResetDuration")) {
                  try {
                     this.bean.setLockoutResetDuration(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("LockoutThreshold")) {
                  try {
                     this.bean.setLockoutThreshold(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("MinimumPasswordLength")) {
                  try {
                     this.bean.setMinimumPasswordLength(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("LockoutEnabled")) {
                  try {
                     this.bean.setLockoutEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var12) {
         System.out.println(var12 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var12;
      } catch (RuntimeException var13) {
         throw var13;
      } catch (Exception var14) {
         if (var14 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var14);
         } else if (var14 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var14.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var14);
         }
      }
   }
}
