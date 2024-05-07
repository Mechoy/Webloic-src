package weblogic.management.mbeans.custom;

import javax.management.InvalidAttributeValueException;
import weblogic.management.configuration.LogFilterMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;

public final class DomainLogFilter extends ConfigurationMBeanCustomizer {
   private int severityLevel = 16;
   private String[] userIds;
   private String[] subSystems;
   LogFilterMBean delegate = null;

   public DomainLogFilter(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public int getSeverityLevel() {
      return this.delegate != null ? this.delegate.getSeverityLevel() : this.severityLevel;
   }

   public void setSeverityLevel(int var1) {
      this.severityLevel = var1;
      if (this.delegate != null) {
         this.delegate.setSeverityLevel(var1);
      }

   }

   public String[] getSubsystemNames() {
      return this.delegate != null ? this.delegate.getSubsystemNames() : this.subSystems;
   }

   public void setSubsystemNames(String[] var1) throws InvalidAttributeValueException {
      this.subSystems = var1;
      if (this.delegate != null) {
         this.delegate.setSubsystemNames(this.subSystems);
      }

   }

   public String[] getUserIds() {
      return this.delegate != null ? this.delegate.getUserIds() : this.userIds;
   }

   public void setUserIds(String[] var1) throws InvalidAttributeValueException {
      this.userIds = var1;
      if (this.delegate != null) {
         this.delegate.setUserIds(this.userIds);
      }

   }

   public LogFilterMBean getDelegate() {
      return this.delegate;
   }

   public void setDelegate(LogFilterMBean var1) {
      this.delegate = var1;
   }
}
