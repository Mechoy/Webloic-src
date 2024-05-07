package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class WSReliableDeliveryPolicyMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private WSReliableDeliveryPolicyMBeanImpl bean;

   protected WSReliableDeliveryPolicyMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (WSReliableDeliveryPolicyMBeanImpl)var1;
   }

   public WSReliableDeliveryPolicyMBeanBinder() {
      super(new WSReliableDeliveryPolicyMBeanImpl());
      this.bean = (WSReliableDeliveryPolicyMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("DefaultRetryCount")) {
                  try {
                     this.bean.setDefaultRetryCount(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("DefaultRetryInterval")) {
                  try {
                     this.bean.setDefaultRetryInterval(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("DefaultTimeToLive")) {
                  try {
                     this.bean.setDefaultTimeToLive(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else if (var1.equals("JMSServer")) {
                  this.bean.setJMSServerAsString((String)var2);
               } else if (var1.equals("Store")) {
                  this.handleDeprecatedProperty("Store", "9.0.0.0 use the JMSServer attribute instead");
                  this.bean.setStoreAsString((String)var2);
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
