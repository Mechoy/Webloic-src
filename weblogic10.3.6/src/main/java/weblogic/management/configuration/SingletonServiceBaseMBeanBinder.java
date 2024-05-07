package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class SingletonServiceBaseMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private SingletonServiceBaseMBeanImpl bean;

   protected SingletonServiceBaseMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (SingletonServiceBaseMBeanImpl)var1;
   }

   public SingletonServiceBaseMBeanBinder() {
      super(new SingletonServiceBaseMBeanImpl());
      this.bean = (SingletonServiceBaseMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("AdditionalMigrationAttempts")) {
                  try {
                     this.bean.setAdditionalMigrationAttempts(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("HostingServer")) {
                  this.bean.setHostingServerAsString((String)var2);
               } else if (var1.equals("MillisToSleepBetweenAttempts")) {
                  try {
                     this.bean.setMillisToSleepBetweenAttempts(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else if (var1.equals("UserPreferredServer")) {
                  this.bean.setUserPreferredServerAsString((String)var2);
               } else {
                  var3 = super.bindAttribute(var1, var2);
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
