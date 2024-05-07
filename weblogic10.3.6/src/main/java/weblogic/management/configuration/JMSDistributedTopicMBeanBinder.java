package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.ReadOnlyMBeanBinder;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class JMSDistributedTopicMBeanBinder extends JMSDistributedDestinationMBeanBinder implements AttributeBinder {
   private JMSDistributedTopicMBeanImpl bean;

   protected JMSDistributedTopicMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (JMSDistributedTopicMBeanImpl)var1;
   }

   public JMSDistributedTopicMBeanBinder() {
      super(new JMSDistributedTopicMBeanImpl());
      this.bean = (JMSDistributedTopicMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("JMSDistributedTopicMember")) {
                  try {
                     this.bean.addJMSDistributedTopicMember((JMSDistributedTopicMemberMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                     this.bean.removeJMSDistributedTopicMember((JMSDistributedTopicMemberMBean)var10.getExistingBean());
                     this.bean.addJMSDistributedTopicMember((JMSDistributedTopicMemberMBean)((AbstractDescriptorBean)((JMSDistributedTopicMemberMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JMSTemplate")) {
                  try {
                     this.bean.setJMSTemplate((JMSTemplateMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("JNDIName")) {
                  try {
                     this.bean.setJNDIName((String)var2);
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("LoadBalancingPolicy")) {
                  try {
                     this.bean.setLoadBalancingPolicy((String)var2);
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("Members")) {
                  this.bean.setMembersAsString((String)var2);
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("Notes")) {
                  try {
                     this.bean.setNotes((String)var2);
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
      } catch (ClassCastException var11) {
         System.out.println(var11 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (Exception var13) {
         if (var13 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var13);
         } else if (var13 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var13.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var13);
         }
      }
   }
}
