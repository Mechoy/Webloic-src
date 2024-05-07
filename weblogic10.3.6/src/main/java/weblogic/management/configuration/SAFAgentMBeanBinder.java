package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.ReadOnlyMBeanBinder;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class SAFAgentMBeanBinder extends DeploymentMBeanBinder implements AttributeBinder {
   private SAFAgentMBeanImpl bean;

   protected SAFAgentMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (SAFAgentMBeanImpl)var1;
   }

   public SAFAgentMBeanBinder() {
      super(new SAFAgentMBeanImpl());
      this.bean = (SAFAgentMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("AcknowledgeInterval")) {
                  try {
                     this.bean.setAcknowledgeInterval(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var28) {
                     System.out.println("Warning: multiple definitions with same name: " + var28.getMessage());
                  }
               } else if (var1.equals("BytesMaximum")) {
                  try {
                     this.bean.setBytesMaximum(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var27) {
                     System.out.println("Warning: multiple definitions with same name: " + var27.getMessage());
                  }
               } else if (var1.equals("BytesThresholdHigh")) {
                  try {
                     this.bean.setBytesThresholdHigh(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var26) {
                     System.out.println("Warning: multiple definitions with same name: " + var26.getMessage());
                  }
               } else if (var1.equals("BytesThresholdLow")) {
                  try {
                     this.bean.setBytesThresholdLow(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var25) {
                     System.out.println("Warning: multiple definitions with same name: " + var25.getMessage());
                  }
               } else if (var1.equals("ConversationIdleTimeMaximum")) {
                  try {
                     this.bean.setConversationIdleTimeMaximum(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var24) {
                     System.out.println("Warning: multiple definitions with same name: " + var24.getMessage());
                  }
               } else if (var1.equals("DefaultRetryDelayBase")) {
                  try {
                     this.bean.setDefaultRetryDelayBase(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var23) {
                     System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                  }
               } else if (var1.equals("DefaultRetryDelayMaximum")) {
                  try {
                     this.bean.setDefaultRetryDelayMaximum(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var22) {
                     System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                  }
               } else if (var1.equals("DefaultRetryDelayMultiplier")) {
                  try {
                     this.bean.setDefaultRetryDelayMultiplier(Double.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var21) {
                     System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                  }
               } else if (var1.equals("DefaultTimeToLive")) {
                  try {
                     this.bean.setDefaultTimeToLive(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var20) {
                     System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                  }
               } else if (var1.equals("JMSSAFMessageLogFile")) {
                  try {
                     this.bean.setJMSSAFMessageLogFile((JMSSAFMessageLogFileMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("MaximumMessageSize")) {
                  try {
                     this.bean.setMaximumMessageSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("MessageBufferSize")) {
                  try {
                     this.bean.setMessageBufferSize(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("MessagesMaximum")) {
                  try {
                     this.bean.setMessagesMaximum(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("MessagesThresholdHigh")) {
                  try {
                     this.bean.setMessagesThresholdHigh(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("MessagesThresholdLow")) {
                  try {
                     this.bean.setMessagesThresholdLow(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("PagingDirectory")) {
                  try {
                     this.bean.setPagingDirectory((String)var2);
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("ServiceType")) {
                  try {
                     this.bean.setServiceType((String)var2);
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("Store")) {
                  this.bean.setStoreAsString((String)var2);
               } else if (var1.equals("Targets")) {
                  this.bean.setTargetsAsString((String)var2);
               } else if (var1.equals("WindowInterval")) {
                  try {
                     this.bean.setWindowInterval(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("WindowSize")) {
                  try {
                     this.bean.setWindowSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("ForwardingPausedAtStartup")) {
                  try {
                     this.bean.setForwardingPausedAtStartup(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("IncomingPausedAtStartup")) {
                  try {
                     this.bean.setIncomingPausedAtStartup(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("LoggingEnabled")) {
                  try {
                     this.bean.setLoggingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("ReceivingPausedAtStartup")) {
                  try {
                     this.bean.setReceivingPausedAtStartup(Boolean.valueOf((String)var2));
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
