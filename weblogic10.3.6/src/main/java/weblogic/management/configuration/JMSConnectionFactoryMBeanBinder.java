package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class JMSConnectionFactoryMBeanBinder extends DeploymentMBeanBinder implements AttributeBinder {
   private JMSConnectionFactoryMBeanImpl bean;

   protected JMSConnectionFactoryMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (JMSConnectionFactoryMBeanImpl)var1;
   }

   public JMSConnectionFactoryMBeanBinder() {
      super(new JMSConnectionFactoryMBeanImpl());
      this.bean = (JMSConnectionFactoryMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("AcknowledgePolicy")) {
                  try {
                     this.bean.setAcknowledgePolicy((String)var2);
                  } catch (BeanAlreadyExistsException var29) {
                     System.out.println("Warning: multiple definitions with same name: " + var29.getMessage());
                  }
               } else if (var1.equals("AllowCloseInOnMessage")) {
                  try {
                     this.bean.setAllowCloseInOnMessage(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var28) {
                     System.out.println("Warning: multiple definitions with same name: " + var28.getMessage());
                  }
               } else if (var1.equals("ClientId")) {
                  try {
                     this.bean.setClientId((String)var2);
                  } catch (BeanAlreadyExistsException var27) {
                     System.out.println("Warning: multiple definitions with same name: " + var27.getMessage());
                  }
               } else if (var1.equals("DefaultDeliveryMode")) {
                  try {
                     this.bean.setDefaultDeliveryMode((String)var2);
                  } catch (BeanAlreadyExistsException var26) {
                     System.out.println("Warning: multiple definitions with same name: " + var26.getMessage());
                  }
               } else if (var1.equals("DefaultPriority")) {
                  try {
                     this.bean.setDefaultPriority(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var25) {
                     System.out.println("Warning: multiple definitions with same name: " + var25.getMessage());
                  }
               } else if (var1.equals("DefaultRedeliveryDelay")) {
                  try {
                     this.bean.setDefaultRedeliveryDelay(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var24) {
                     System.out.println("Warning: multiple definitions with same name: " + var24.getMessage());
                  }
               } else if (var1.equals("DefaultTimeToDeliver")) {
                  try {
                     this.bean.setDefaultTimeToDeliver(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var23) {
                     System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                  }
               } else if (var1.equals("DefaultTimeToLive")) {
                  try {
                     this.bean.setDefaultTimeToLive(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var22) {
                     System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                  }
               } else if (var1.equals("FlowInterval")) {
                  try {
                     this.bean.setFlowInterval(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var21) {
                     System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                  }
               } else if (var1.equals("FlowMaximum")) {
                  try {
                     this.bean.setFlowMaximum(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var20) {
                     System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                  }
               } else if (var1.equals("FlowMinimum")) {
                  try {
                     this.bean.setFlowMinimum(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("FlowSteps")) {
                  try {
                     this.bean.setFlowSteps(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("JNDIName")) {
                  try {
                     this.bean.setJNDIName((String)var2);
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("MessagesMaximum")) {
                  try {
                     this.bean.setMessagesMaximum(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("Notes")) {
                  try {
                     this.bean.setNotes((String)var2);
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("OverrunPolicy")) {
                  try {
                     this.bean.setOverrunPolicy((String)var2);
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("SendTimeout")) {
                  try {
                     this.bean.setSendTimeout(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("Targets")) {
                  this.bean.setTargetsAsString((String)var2);
               } else if (var1.equals("TransactionTimeout")) {
                  try {
                     this.bean.setTransactionTimeout(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("FlowControlEnabled")) {
                  try {
                     this.bean.setFlowControlEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("LoadBalancingEnabled")) {
                  try {
                     this.bean.setLoadBalancingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("ServerAffinityEnabled")) {
                  try {
                     this.bean.setServerAffinityEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("UserTransactionsEnabled")) {
                  this.handleDeprecatedProperty("UserTransactionsEnabled", "8.1.0.0 Replaced by {@link weblogic.management.configuration.JMSConnectionFactoryMBean#XAConnectionFactoryEnabled}");

                  try {
                     this.bean.setUserTransactionsEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("XAConnectionFactoryEnabled")) {
                  try {
                     this.bean.setXAConnectionFactoryEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("XAServerEnabled")) {
                  this.handleDeprecatedProperty("XAServerEnabled", "8.1.0.0 Replaced by {@link weblogic.management.configuration.JMSConnectionFactoryMBean#XAConnectionFactoryEnabled}");

                  try {
                     this.bean.setXAServerEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var30) {
         System.out.println(var30 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var30;
      } catch (RuntimeException var31) {
         throw var31;
      } catch (Exception var32) {
         if (var32 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var32);
         } else if (var32 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var32.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var32);
         }
      }
   }
}
