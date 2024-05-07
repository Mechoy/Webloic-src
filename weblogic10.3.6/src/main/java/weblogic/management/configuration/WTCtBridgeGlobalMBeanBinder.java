package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class WTCtBridgeGlobalMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private WTCtBridgeGlobalMBeanImpl bean;

   protected WTCtBridgeGlobalMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (WTCtBridgeGlobalMBeanImpl)var1;
   }

   public WTCtBridgeGlobalMBeanBinder() {
      super(new WTCtBridgeGlobalMBeanImpl());
      this.bean = (WTCtBridgeGlobalMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("AllowNonStandardTypes")) {
                  try {
                     this.bean.setAllowNonStandardTypes((String)var2);
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("DefaultReplyDeliveryMode")) {
                  try {
                     this.bean.setDefaultReplyDeliveryMode((String)var2);
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("DeliveryModeOverride")) {
                  try {
                     this.bean.setDeliveryModeOverride((String)var2);
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("JmsFactory")) {
                  try {
                     this.bean.setJmsFactory((String)var2);
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("JmsToTuxPriorityMap")) {
                  try {
                     this.bean.setJmsToTuxPriorityMap((String)var2);
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("JndiFactory")) {
                  try {
                     this.bean.setJndiFactory((String)var2);
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("Retries")) {
                  try {
                     this.bean.setRetries(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("RetryDelay")) {
                  try {
                     this.bean.setRetryDelay(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("Timeout")) {
                  try {
                     this.bean.setTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("Transactional")) {
                  try {
                     this.bean.setTransactional((String)var2);
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("TuxErrorQueue")) {
                  try {
                     this.bean.setTuxErrorQueue((String)var2);
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("TuxFactory")) {
                  try {
                     this.bean.setTuxFactory((String)var2);
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("TuxToJmsPriorityMap")) {
                  try {
                     this.bean.setTuxToJmsPriorityMap((String)var2);
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("UserId")) {
                  try {
                     this.bean.setUserId((String)var2);
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("WlsErrorDestination")) {
                  try {
                     this.bean.setWlsErrorDestination((String)var2);
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var20) {
         System.out.println(var20 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var20;
      } catch (RuntimeException var21) {
         throw var21;
      } catch (Exception var22) {
         if (var22 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var22);
         } else if (var22 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var22.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var22);
         }
      }
   }
}
