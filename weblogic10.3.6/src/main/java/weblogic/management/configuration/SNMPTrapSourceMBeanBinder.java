package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class SNMPTrapSourceMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private SNMPTrapSourceMBeanImpl bean;

   protected SNMPTrapSourceMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (SNMPTrapSourceMBeanImpl)var1;
   }

   public SNMPTrapSourceMBeanBinder() {
      super(new SNMPTrapSourceMBeanImpl());
      this.bean = (SNMPTrapSourceMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("EnabledServers")) {
                  this.bean.setEnabledServersAsString((String)var2);
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
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