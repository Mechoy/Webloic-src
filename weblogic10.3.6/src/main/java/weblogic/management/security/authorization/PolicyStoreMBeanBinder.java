package weblogic.management.security.authorization;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.management.utils.PropertiesListerMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class PolicyStoreMBeanBinder extends PropertiesListerMBeanBinder implements AttributeBinder {
   private PolicyStoreMBeanImpl bean;

   protected PolicyStoreMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (PolicyStoreMBeanImpl)var1;
   }

   public PolicyStoreMBeanBinder() {
      super(new PolicyStoreMBeanImpl());
      this.bean = (PolicyStoreMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               var3 = super.bindAttribute(var1, var2);
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var4) {
         System.out.println(var4 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var4;
      } catch (RuntimeException var5) {
         throw var5;
      } catch (Exception var6) {
         if (var6 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var6);
         } else if (var6 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var6.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var6);
         }
      }
   }
}
