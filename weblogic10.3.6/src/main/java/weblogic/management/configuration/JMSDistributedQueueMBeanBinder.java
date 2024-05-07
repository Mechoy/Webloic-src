package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.ReadOnlyMBeanBinder;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class JMSDistributedQueueMBeanBinder extends JMSDistributedDestinationMBeanBinder implements AttributeBinder {
   private JMSDistributedQueueMBeanImpl bean;

   protected JMSDistributedQueueMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (JMSDistributedQueueMBeanImpl)var1;
   }

   public JMSDistributedQueueMBeanBinder() {
      super(new JMSDistributedQueueMBeanImpl());
      this.bean = (JMSDistributedQueueMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("ForwardDelay")) {
                  try {
                     this.bean.setForwardDelay(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("JMSDistributedQueueMember")) {
                  try {
                     this.bean.addJMSDistributedQueueMember((JMSDistributedQueueMemberMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                     this.bean.removeJMSDistributedQueueMember((JMSDistributedQueueMemberMBean)var11.getExistingBean());
                     this.bean.addJMSDistributedQueueMember((JMSDistributedQueueMemberMBean)((AbstractDescriptorBean)((JMSDistributedQueueMemberMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JMSTemplate")) {
                  try {
                     this.bean.setJMSTemplate((JMSTemplateMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("JNDIName")) {
                  try {
                     this.bean.setJNDIName((String)var2);
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("LoadBalancingPolicy")) {
                  try {
                     this.bean.setLoadBalancingPolicy((String)var2);
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("Members")) {
                  this.bean.setMembersAsString((String)var2);
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("Notes")) {
                  try {
                     this.bean.setNotes((String)var2);
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("ResetDeliveryCountOnForward")) {
                  try {
                     this.bean.setResetDeliveryCountOnForward(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else if (var1.equals("Targets")) {
                  this.bean.setTargetsAsString((String)var2);
               } else if (var1.equals("Template")) {
                  this.bean.setTemplateAsString((String)var2);
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var13) {
         System.out.println(var13 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var13;
      } catch (RuntimeException var14) {
         throw var14;
      } catch (Exception var15) {
         if (var15 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var15);
         } else if (var15 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var15.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var15);
         }
      }
   }
}
