package weblogic.management.mbeans.custom;

import javax.management.InvalidAttributeValueException;
import weblogic.j2ee.descriptor.wl.DistributedDestinationBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.JMSTemplateMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;

public class JMSDistributedDestination extends ConfigurationMBeanCustomizer {
   private static final String TARGETS = "Targets";
   private static final long serialVersionUID = 5429833394456540954L;
   private transient DistributedDestinationBean delegate;
   private transient SubDeploymentMBean subDeployment;

   public JMSDistributedDestination(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void useDelegates(DistributedDestinationBean var1, SubDeploymentMBean var2) {
      this.delegate = var1;
      this.subDeployment = var2;
   }

   public TargetMBean[] getTargets() {
      if (this.subDeployment == null) {
         Object var1 = this.getValue("Targets");
         if (var1 == null) {
            return new TargetMBean[0];
         } else {
            if (!(var1 instanceof TargetMBean)) {
               if (!(var1 instanceof WebLogicMBean[])) {
                  return new TargetMBean[0];
               }

               WebLogicMBean[] var2 = (WebLogicMBean[])((WebLogicMBean[])var1);
               TargetMBean[] var3 = new TargetMBean[var2.length];

               for(int var4 = 0; var4 < var2.length; ++var4) {
                  WebLogicMBean var5 = var2[var4];
                  if (!(var5 instanceof TargetMBean)) {
                     return new TargetMBean[0];
                  }

                  var3[var4] = (TargetMBean)var5;
               }

               var1 = var3;
            }

            return (TargetMBean[])((TargetMBean[])var1);
         }
      } else {
         return this.subDeployment.getTargets();
      }
   }

   public void setTargets(TargetMBean[] var1) throws InvalidAttributeValueException, DistributedManagementException {
      if (this.subDeployment == null) {
         this.putValueNotify("Targets", var1);
      } else {
         this.subDeployment.setTargets(var1);
      }

   }

   public String getLoadBalancingPolicy() {
      if (this.delegate == null) {
         Object var1 = this.getValue("LoadBalancingPolicy");
         return var1 != null && var1 instanceof String ? (String)var1 : "Round-Robin";
      } else {
         return this.delegate.getLoadBalancingPolicy();
      }
   }

   public void setLoadBalancingPolicy(String var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("LoadBalancingPolicy", var1);
      } else {
         this.delegate.setLoadBalancingPolicy(var1);
      }

   }

   public JMSTemplateMBean getTemplate() {
      Object var1 = this.getValue("Template");
      return (JMSTemplateMBean)var1;
   }

   public void setTemplate(JMSTemplateMBean var1) throws InvalidAttributeValueException {
      this.putValue("Template", var1);
   }

   public JMSTemplateMBean getJMSTemplate() {
      Object var1 = this.getValue("JMSTemplate");
      return (JMSTemplateMBean)var1;
   }

   public void setJMSTemplate(JMSTemplateMBean var1) throws InvalidAttributeValueException {
      this.putValue("JMSTemplate", var1);
   }

   public String getNotes() {
      if (this.delegate == null) {
         Object var1 = this.getValue("Notes");
         return var1 != null && var1 instanceof String ? (String)var1 : null;
      } else {
         return this.delegate.getNotes();
      }
   }

   public void setNotes(String var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("Notes", var1);
      } else {
         this.delegate.setNotes(var1);
      }

   }

   public String getJNDIName() {
      if (this.delegate == null) {
         Object var1 = this.getValue("JNDIName");
         return var1 != null && var1 instanceof String ? (String)var1 : null;
      } else {
         return this.delegate.getJNDIName();
      }
   }

   public void setJNDIName(String var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("JNDIName", var1);
      } else {
         this.delegate.setJNDIName(var1);
      }

   }
}
