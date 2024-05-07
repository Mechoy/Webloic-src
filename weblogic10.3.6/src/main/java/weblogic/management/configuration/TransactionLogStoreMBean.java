package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.management.DistributedManagementException;

public interface TransactionLogStoreMBean extends PersistentStoreMBean {
   /** @deprecated */
   TargetMBean[] getTargets();

   /** @deprecated */
   void setTargets(TargetMBean[] var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   boolean addTarget(TargetMBean var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   boolean removeTarget(TargetMBean var1) throws InvalidAttributeValueException, DistributedManagementException;
}
