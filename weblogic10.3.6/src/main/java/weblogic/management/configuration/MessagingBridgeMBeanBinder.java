package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class MessagingBridgeMBeanBinder extends DeploymentMBeanBinder implements AttributeBinder {
   private MessagingBridgeMBeanImpl bean;

   protected MessagingBridgeMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (MessagingBridgeMBeanImpl)var1;
   }

   public MessagingBridgeMBeanBinder() {
      super(new MessagingBridgeMBeanImpl());
      this.bean = (MessagingBridgeMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("BatchInterval")) {
                  try {
                     this.bean.setBatchInterval(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("BatchSize")) {
                  try {
                     this.bean.setBatchSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("IdleTimeMaximum")) {
                  try {
                     this.bean.setIdleTimeMaximum(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("PreserveMsgProperty")) {
                  try {
                     this.bean.setPreserveMsgProperty(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("QualityOfService")) {
                  try {
                     this.bean.setQualityOfService((String)var2);
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("ReconnectDelayIncrease")) {
                  try {
                     this.bean.setReconnectDelayIncrease(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("ReconnectDelayMaximum")) {
                  try {
                     this.bean.setReconnectDelayMaximum(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("ReconnectDelayMinimum")) {
                  try {
                     this.bean.setReconnectDelayMinimum(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("Selector")) {
                  try {
                     this.bean.setSelector((String)var2);
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("SourceDestination")) {
                  this.bean.setSourceDestinationAsString((String)var2);
               } else if (var1.equals("TargetDestination")) {
                  this.bean.setTargetDestinationAsString((String)var2);
               } else if (var1.equals("TransactionTimeout")) {
                  try {
                     this.bean.setTransactionTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("AsyncEnabled")) {
                  try {
                     this.bean.setAsyncEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("DurabilityEnabled")) {
                  try {
                     this.bean.setDurabilityEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("QOSDegradationAllowed")) {
                  try {
                     this.bean.setQOSDegradationAllowed(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("Started")) {
                  try {
                     this.bean.setStarted(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var19) {
         System.out.println(var19 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var19;
      } catch (RuntimeException var20) {
         throw var20;
      } catch (Exception var21) {
         if (var21 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var21);
         } else if (var21 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var21.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var21);
         }
      }
   }
}
