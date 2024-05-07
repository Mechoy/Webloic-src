package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;

/** @deprecated */
public interface FileRealmMBean extends ConfigurationMBean {
   int getMaxUsers();

   void setMaxUsers(int var1) throws InvalidAttributeValueException;

   int getMaxGroups();

   void setMaxGroups(int var1) throws InvalidAttributeValueException;

   int getMaxACLs();

   void setMaxACLs(int var1) throws InvalidAttributeValueException;
}
