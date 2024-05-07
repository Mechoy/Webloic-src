package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.management.DistributedManagementException;

public interface JMSInteropModuleMBean extends JMSSystemResourceMBean, DomainTargetedMBean {
   TargetMBean[] getTargets();

   void setTargets(TargetMBean[] var1) throws InvalidAttributeValueException, DistributedManagementException;

   void addTarget(TargetMBean var1) throws InvalidAttributeValueException, DistributedManagementException;

   void removeTarget(TargetMBean var1) throws InvalidAttributeValueException, DistributedManagementException;
}
