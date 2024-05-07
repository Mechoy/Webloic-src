package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.ReadOnlyMBeanBinder;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class JMSServerMBeanBinder extends DeploymentMBeanBinder implements AttributeBinder {
   private JMSServerMBeanImpl bean;

   protected JMSServerMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (JMSServerMBeanImpl)var1;
   }

   public JMSServerMBeanBinder() {
      super(new JMSServerMBeanImpl());
      this.bean = (JMSServerMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("BlockingSendPolicy")) {
                  try {
                     this.bean.setBlockingSendPolicy((String)var2);
                  } catch (BeanAlreadyExistsException var37) {
                     System.out.println("Warning: multiple definitions with same name: " + var37.getMessage());
                  }
               } else if (var1.equals("BytesMaximum")) {
                  try {
                     this.bean.setBytesMaximum(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var36) {
                     System.out.println("Warning: multiple definitions with same name: " + var36.getMessage());
                  }
               } else if (var1.equals("BytesThresholdHigh")) {
                  try {
                     this.bean.setBytesThresholdHigh(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var35) {
                     System.out.println("Warning: multiple definitions with same name: " + var35.getMessage());
                  }
               } else if (var1.equals("BytesThresholdLow")) {
                  try {
                     this.bean.setBytesThresholdLow(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var34) {
                     System.out.println("Warning: multiple definitions with same name: " + var34.getMessage());
                  }
               } else if (var1.equals("ConsumptionPausedAtStartup")) {
                  try {
                     this.bean.setConsumptionPausedAtStartup((String)var2);
                  } catch (BeanAlreadyExistsException var33) {
                     System.out.println("Warning: multiple definitions with same name: " + var33.getMessage());
                  }
               } else if (var1.equals("ExpirationScanInterval")) {
                  try {
                     this.bean.setExpirationScanInterval(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var32) {
                     System.out.println("Warning: multiple definitions with same name: " + var32.getMessage());
                  }
               } else if (var1.equals("InsertionPausedAtStartup")) {
                  try {
                     this.bean.setInsertionPausedAtStartup((String)var2);
                  } catch (BeanAlreadyExistsException var31) {
                     System.out.println("Warning: multiple definitions with same name: " + var31.getMessage());
                  }
               } else if (var1.equals("JMSMessageLogFile")) {
                  try {
                     this.bean.setJMSMessageLogFile((JMSMessageLogFileMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var30) {
                     System.out.println("Warning: multiple definitions with same name: " + var30.getMessage());
                  }
               } else if (var1.equals("JMSQueue")) {
                  try {
                     this.bean.addJMSQueue((JMSQueueMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var29) {
                     System.out.println("Warning: multiple definitions with same name: " + var29.getMessage());
                     this.bean.removeJMSQueue((JMSQueueMBean)var29.getExistingBean());
                     this.bean.addJMSQueue((JMSQueueMBean)((AbstractDescriptorBean)((JMSQueueMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JMSSessionPool")) {
                  try {
                     this.bean.addJMSSessionPool((JMSSessionPoolMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var28) {
                     System.out.println("Warning: multiple definitions with same name: " + var28.getMessage());
                     this.bean.removeJMSSessionPool((JMSSessionPoolMBean)var28.getExistingBean());
                     this.bean.addJMSSessionPool((JMSSessionPoolMBean)((AbstractDescriptorBean)((JMSSessionPoolMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JMSTopic")) {
                  try {
                     this.bean.addJMSTopic((JMSTopicMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var27) {
                     System.out.println("Warning: multiple definitions with same name: " + var27.getMessage());
                     this.bean.removeJMSTopic((JMSTopicMBean)var27.getExistingBean());
                     this.bean.addJMSTopic((JMSTopicMBean)((AbstractDescriptorBean)((JMSTopicMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("MaximumMessageSize")) {
                  try {
                     this.bean.setMaximumMessageSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var26) {
                     System.out.println("Warning: multiple definitions with same name: " + var26.getMessage());
                  }
               } else if (var1.equals("MessageBufferSize")) {
                  try {
                     this.bean.setMessageBufferSize(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var25) {
                     System.out.println("Warning: multiple definitions with same name: " + var25.getMessage());
                  }
               } else if (var1.equals("MessagesMaximum")) {
                  try {
                     this.bean.setMessagesMaximum(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var24) {
                     System.out.println("Warning: multiple definitions with same name: " + var24.getMessage());
                  }
               } else if (var1.equals("MessagesThresholdHigh")) {
                  try {
                     this.bean.setMessagesThresholdHigh(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var23) {
                     System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                  }
               } else if (var1.equals("MessagesThresholdLow")) {
                  try {
                     this.bean.setMessagesThresholdLow(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var22) {
                     System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var21) {
                     System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                  }
               } else if (var1.equals("PagingBlockSize")) {
                  try {
                     this.bean.setPagingBlockSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var20) {
                     System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                  }
               } else if (var1.equals("PagingDirectory")) {
                  try {
                     this.bean.setPagingDirectory((String)var2);
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("PagingIoBufferSize")) {
                  try {
                     this.bean.setPagingIoBufferSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("PagingMaxFileSize")) {
                  try {
                     this.bean.setPagingMaxFileSize(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("PagingMaxWindowBufferSize")) {
                  try {
                     this.bean.setPagingMaxWindowBufferSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("PagingMinWindowBufferSize")) {
                  try {
                     this.bean.setPagingMinWindowBufferSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("PagingStore")) {
                  this.handleDeprecatedProperty("PagingStore", "9.0.0.0 Replaced with the PagingDirectory attribute.");
                  this.bean.setPagingStoreAsString((String)var2);
               } else if (var1.equals("PersistentStore")) {
                  this.bean.setPersistentStoreAsString((String)var2);
               } else if (var1.equals("ProductionPausedAtStartup")) {
                  try {
                     this.bean.setProductionPausedAtStartup((String)var2);
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("Store")) {
                  this.handleDeprecatedProperty("Store", "9.0.0.0 Replaced with the PersistentStore attribute.");
                  this.bean.setStoreAsString((String)var2);
               } else if (var1.equals("StoreEnabled")) {
                  try {
                     this.bean.setStoreEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("Targets")) {
                  this.bean.setTargetsAsString((String)var2);
               } else if (var1.equals("TemporaryTemplate")) {
                  this.handleDeprecatedProperty("TemporaryTemplate", "9.0.0.0 Replaced with the TemporaryTemplateName and TemporaryTemplateResource attributes.");
                  this.bean.setTemporaryTemplateAsString((String)var2);
               } else if (var1.equals("TemporaryTemplateName")) {
                  try {
                     this.bean.setTemporaryTemplateName((String)var2);
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("TemporaryTemplateResource")) {
                  try {
                     this.bean.setTemporaryTemplateResource((String)var2);
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("AllowsPersistentDowngrade")) {
                  try {
                     this.bean.setAllowsPersistentDowngrade(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("BytesPagingEnabled")) {
                  this.handleDeprecatedProperty("BytesPagingEnabled", "9.0.0.0 Replaced by defaulting the paging to always be enabled.");

                  try {
                     this.bean.setBytesPagingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("HostingTemporaryDestinations")) {
                  try {
                     this.bean.setHostingTemporaryDestinations(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("JDBCStoreUpgradeEnabled")) {
                  this.handleDeprecatedProperty("JDBCStoreUpgradeEnabled", "9.0.0.0 Replaced with the new upgrade functionality.");

                  try {
                     this.bean.setJDBCStoreUpgradeEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("MessagesPagingEnabled")) {
                  this.handleDeprecatedProperty("MessagesPagingEnabled", "9.0.0.0 Replaced by defaulting the paging to always be enabled.");

                  try {
                     this.bean.setMessagesPagingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("PagingFileLockingEnabled")) {
                  try {
                     this.bean.setPagingFileLockingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var38) {
         System.out.println(var38 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var38;
      } catch (RuntimeException var39) {
         throw var39;
      } catch (Exception var40) {
         if (var40 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var40);
         } else if (var40 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var40.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var40);
         }
      }
   }
}
