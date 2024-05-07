package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class JMSTopicMBeanBinder extends JMSDestinationMBeanBinder implements AttributeBinder {
   private JMSTopicMBeanImpl bean;

   protected JMSTopicMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (JMSTopicMBeanImpl)var1;
   }

   public JMSTopicMBeanBinder() {
      super(new JMSTopicMBeanImpl());
      this.bean = (JMSTopicMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("BytesMaximum")) {
                  try {
                     this.bean.setBytesMaximum(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var28) {
                     System.out.println("Warning: multiple definitions with same name: " + var28.getMessage());
                  }
               } else if (var1.equals("BytesThresholdHigh")) {
                  try {
                     this.bean.setBytesThresholdHigh(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var27) {
                     System.out.println("Warning: multiple definitions with same name: " + var27.getMessage());
                  }
               } else if (var1.equals("BytesThresholdLow")) {
                  try {
                     this.bean.setBytesThresholdLow(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var26) {
                     System.out.println("Warning: multiple definitions with same name: " + var26.getMessage());
                  }
               } else if (var1.equals("CreationTime")) {
                  try {
                     this.bean.setCreationTime(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var25) {
                     System.out.println("Warning: multiple definitions with same name: " + var25.getMessage());
                  }
               } else if (var1.equals("DeliveryModeOverride")) {
                  try {
                     this.bean.setDeliveryModeOverride((String)var2);
                  } catch (BeanAlreadyExistsException var24) {
                     System.out.println("Warning: multiple definitions with same name: " + var24.getMessage());
                  }
               } else if (var1.equals("DestinationKeys")) {
                  this.bean.setDestinationKeysAsString((String)var2);
               } else if (var1.equals("ErrorDestination")) {
                  this.bean.setErrorDestinationAsString((String)var2);
               } else if (var1.equals("ExpirationLoggingPolicy")) {
                  try {
                     this.bean.setExpirationLoggingPolicy((String)var2);
                  } catch (BeanAlreadyExistsException var23) {
                     System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                  }
               } else if (var1.equals("ExpirationPolicy")) {
                  try {
                     this.bean.setExpirationPolicy((String)var2);
                  } catch (BeanAlreadyExistsException var22) {
                     System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                  }
               } else if (var1.equals("JNDIName")) {
                  try {
                     this.bean.setJNDIName((String)var2);
                  } catch (BeanAlreadyExistsException var21) {
                     System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                  }
               } else if (var1.equals("MaximumMessageSize")) {
                  try {
                     this.bean.setMaximumMessageSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var20) {
                     System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                  }
               } else if (var1.equals("MessagesMaximum")) {
                  try {
                     this.bean.setMessagesMaximum(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("MessagesThresholdHigh")) {
                  try {
                     this.bean.setMessagesThresholdHigh(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("MessagesThresholdLow")) {
                  try {
                     this.bean.setMessagesThresholdLow(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("MulticastAddress")) {
                  try {
                     this.bean.setMulticastAddress((String)var2);
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("MulticastPort")) {
                  try {
                     this.bean.setMulticastPort(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("MulticastTTL")) {
                  try {
                     this.bean.setMulticastTTL(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("Notes")) {
                  try {
                     this.bean.setNotes((String)var2);
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("PriorityOverride")) {
                  try {
                     this.bean.setPriorityOverride(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("RedeliveryDelayOverride")) {
                  try {
                     this.bean.setRedeliveryDelayOverride(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("RedeliveryLimit")) {
                  try {
                     this.bean.setRedeliveryLimit(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("StoreEnabled")) {
                  try {
                     this.bean.setStoreEnabled((String)var2);
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("Template")) {
                  this.bean.setTemplateAsString((String)var2);
               } else if (var1.equals("TimeToDeliverOverride")) {
                  try {
                     this.bean.setTimeToDeliverOverride((String)var2);
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("TimeToLiveOverride")) {
                  try {
                     this.bean.setTimeToLiveOverride(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("JNDINameReplicated")) {
                  try {
                     this.bean.setJNDINameReplicated(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var29) {
         System.out.println(var29 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var29;
      } catch (RuntimeException var30) {
         throw var30;
      } catch (Exception var31) {
         if (var31 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var31);
         } else if (var31 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var31.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var31);
         }
      }
   }
}
