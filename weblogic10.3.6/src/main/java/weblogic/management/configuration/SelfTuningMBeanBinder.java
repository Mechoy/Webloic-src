package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.ReadOnlyMBeanBinder;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class SelfTuningMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private SelfTuningMBeanImpl bean;

   protected SelfTuningMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (SelfTuningMBeanImpl)var1;
   }

   public SelfTuningMBeanBinder() {
      super(new SelfTuningMBeanImpl());
      this.bean = (SelfTuningMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("Capacity")) {
                  try {
                     this.bean.addCapacity((CapacityMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                     this.bean.removeCapacity((CapacityMBean)var11.getExistingBean());
                     this.bean.addCapacity((CapacityMBean)((AbstractDescriptorBean)((CapacityMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("ContextRequestClass")) {
                  try {
                     this.bean.addContextRequestClass((ContextRequestClassMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                     this.bean.removeContextRequestClass((ContextRequestClassMBean)var10.getExistingBean());
                     this.bean.addContextRequestClass((ContextRequestClassMBean)((AbstractDescriptorBean)((ContextRequestClassMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("FairShareRequestClass")) {
                  try {
                     this.bean.addFairShareRequestClass((FairShareRequestClassMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                     this.bean.removeFairShareRequestClass((FairShareRequestClassMBean)var9.getExistingBean());
                     this.bean.addFairShareRequestClass((FairShareRequestClassMBean)((AbstractDescriptorBean)((FairShareRequestClassMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("MaxThreadsConstraint")) {
                  try {
                     this.bean.addMaxThreadsConstraint((MaxThreadsConstraintMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                     this.bean.removeMaxThreadsConstraint((MaxThreadsConstraintMBean)var8.getExistingBean());
                     this.bean.addMaxThreadsConstraint((MaxThreadsConstraintMBean)((AbstractDescriptorBean)((MaxThreadsConstraintMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("MinThreadsConstraint")) {
                  try {
                     this.bean.addMinThreadsConstraint((MinThreadsConstraintMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                     this.bean.removeMinThreadsConstraint((MinThreadsConstraintMBean)var7.getExistingBean());
                     this.bean.addMinThreadsConstraint((MinThreadsConstraintMBean)((AbstractDescriptorBean)((MinThreadsConstraintMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("ResponseTimeRequestClass")) {
                  try {
                     this.bean.addResponseTimeRequestClass((ResponseTimeRequestClassMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                     this.bean.removeResponseTimeRequestClass((ResponseTimeRequestClassMBean)var6.getExistingBean());
                     this.bean.addResponseTimeRequestClass((ResponseTimeRequestClassMBean)((AbstractDescriptorBean)((ResponseTimeRequestClassMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("WorkManager")) {
                  try {
                     this.bean.addWorkManager((WorkManagerMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                     this.bean.removeWorkManager((WorkManagerMBean)var5.getExistingBean());
                     this.bean.addWorkManager((WorkManagerMBean)((AbstractDescriptorBean)((WorkManagerMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var12) {
         System.out.println(var12 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var12;
      } catch (RuntimeException var13) {
         throw var13;
      } catch (Exception var14) {
         if (var14 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var14);
         } else if (var14 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var14.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var14);
         }
      }
   }
}
