package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.management.DistributedManagementException;

/** @deprecated */
public interface DomainLogFilterMBean extends ConfigurationMBean {
   /** @deprecated */
   int INFO = 64;
   /** @deprecated */
   int WARNING = 16;
   /** @deprecated */
   int ERROR = 8;
   /** @deprecated */
   int NOTICE = 32;
   /** @deprecated */
   int CRITICAL = 4;
   /** @deprecated */
   int ALERT = 2;
   /** @deprecated */
   int EMERGENCY = 1;

   /** @deprecated */
   int getSeverityLevel();

   void setSeverityLevel(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   String[] getSubsystemNames();

   /** @deprecated */
   void setSubsystemNames(String[] var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   String[] getUserIds();

   /** @deprecated */
   void setUserIds(String[] var1) throws InvalidAttributeValueException, DistributedManagementException;

   LogFilterMBean getDelegate();

   void setDelegate(LogFilterMBean var1);
}
