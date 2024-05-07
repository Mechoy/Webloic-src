package weblogic.management.mbeans.custom;

import javax.management.InvalidAttributeValueException;
import weblogic.j2ee.descriptor.wl.DestinationBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.TemplateBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSTemplateMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;

public abstract class JMSDestination extends JMSDestCommon {
   private transient DestinationBean delegate;
   private String localJNDIName;
   private boolean localJNDIReplicated = true;

   public JMSDestination(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void useDelegates(DomainMBean var1, JMSBean var2, DestinationBean var3) {
      this.delegate = var3;
      super.useDelegates(var1, var2, this.delegate);
   }

   public JMSTemplateMBean getTemplate() {
      if (this.delegate == null) {
         Object var3 = this.getValue("Template");
         return var3 != null && var3 instanceof JMSTemplateMBean ? (JMSTemplateMBean)var3 : null;
      } else {
         TemplateBean var1 = this.delegate.getTemplate();
         String var2 = var1 == null ? null : var1.getName();
         return var2 == null ? null : this.domain.lookupJMSTemplate(var2);
      }
   }

   public void setTemplate(JMSTemplateMBean var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("Template", var1);
      } else {
         String var2 = var1 == null ? null : var1.getName();
         if (var2 == null) {
            this.delegate.setTemplate((TemplateBean)null);
         } else {
            TemplateBean var3 = this.interopModule.lookupTemplate(var2);
            if (var3 == null) {
               throw new InvalidAttributeValueException("Could find the template " + var2 + " in the interop module while setting the template attribute of a destination");
            }

            this.delegate.setTemplate(var3);
         }
      }

   }

   public String getJNDIName() {
      if (this.delegate == null) {
         return this.localJNDIName;
      } else {
         return this.localJNDIReplicated ? this.delegate.getJNDIName() : this.delegate.getLocalJNDIName();
      }
   }

   public void setJNDIName(String var1) {
      this.localJNDIName = var1;
      if (this.delegate != null) {
         if (this.localJNDIReplicated) {
            this.delegate.setJNDIName(this.localJNDIName);
         } else {
            this.delegate.setLocalJNDIName(this.localJNDIName);
         }

      }
   }

   public boolean isJNDINameReplicated() {
      return this.localJNDIReplicated;
   }

   public void setJNDINameReplicated(boolean var1) {
      boolean var2 = this.localJNDIReplicated;
      this.localJNDIReplicated = var1;
      if (this.delegate != null && this.localJNDIReplicated != var2) {
         if (this.localJNDIReplicated) {
            this.delegate.setLocalJNDIName((String)null);
            this.delegate.setJNDIName(this.localJNDIName);
         } else {
            this.delegate.setJNDIName((String)null);
            this.delegate.setLocalJNDIName(this.localJNDIName);
         }

      }
   }

   public String getStoreEnabled() {
      if (this.delegate == null) {
         Object var2 = this.getValue("StoreEnabled");
         return var2 != null && var2 instanceof String ? (String)var2 : "default";
      } else {
         String var1 = this.delegate.getDeliveryParamsOverrides().getDeliveryMode();
         if (var1.equals("No-Delivery")) {
            return "default";
         } else {
            return var1.equals("Persistent") ? "true" : "false";
         }
      }
   }

   public void setStoreEnabled(String var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("StoreEnabled", var1);
      }

   }
}
