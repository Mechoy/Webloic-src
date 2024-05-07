package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.ReadOnlyMBeanBinder;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class WorkManagerMBeanBinder extends DeploymentMBeanBinder implements AttributeBinder {
   private WorkManagerMBeanImpl bean;

   protected WorkManagerMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (WorkManagerMBeanImpl)var1;
   }

   public WorkManagerMBeanBinder() {
      super(new WorkManagerMBeanImpl());
      this.bean = (WorkManagerMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("Capacity")) {
                  this.bean.setCapacityAsString((String)var2);
               } else if (var1.equals("ContextRequestClass")) {
                  this.bean.setContextRequestClassAsString((String)var2);
               } else if (var1.equals("FairShareRequestClass")) {
                  this.bean.setFairShareRequestClassAsString((String)var2);
               } else if (var1.equals("IgnoreStuckThreads")) {
                  try {
                     this.bean.setIgnoreStuckThreads(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("MaxThreadsConstraint")) {
                  this.bean.setMaxThreadsConstraintAsString((String)var2);
               } else if (var1.equals("MinThreadsConstraint")) {
                  this.bean.setMinThreadsConstraintAsString((String)var2);
               } else if (var1.equals("ResponseTimeRequestClass")) {
                  this.bean.setResponseTimeRequestClassAsString((String)var2);
               } else if (var1.equals("WorkManagerShutdownTrigger")) {
                  try {
                     this.bean.setWorkManagerShutdownTrigger((WorkManagerShutdownTriggerMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var7) {
         System.out.println(var7 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var7;
      } catch (RuntimeException var8) {
         throw var8;
      } catch (Exception var9) {
         if (var9 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var9);
         } else if (var9 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var9.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var9);
         }
      }
   }
}
