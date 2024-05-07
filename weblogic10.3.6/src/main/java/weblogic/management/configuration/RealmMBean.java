package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.management.DistributedManagementException;

/** @deprecated */
public interface RealmMBean extends ConfigurationMBean {
   CachingRealmMBean getCachingRealm();

   void setCachingRealm(CachingRealmMBean var1) throws InvalidAttributeValueException;

   FileRealmMBean getFileRealm();

   void setFileRealm(FileRealmMBean var1) throws InvalidAttributeValueException;

   void refresh() throws RealmException, DistributedManagementException;

   RealmManager manager();

   int getResultsBatchSize();

   void setResultsBatchSize(int var1) throws InvalidAttributeValueException;

   boolean isEnumerationAllowed();

   void setEnumerationAllowed(boolean var1);
}
