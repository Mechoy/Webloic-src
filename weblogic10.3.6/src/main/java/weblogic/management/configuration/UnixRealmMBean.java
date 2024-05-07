package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;

/** @deprecated */
public interface UnixRealmMBean extends BasicRealmMBean {
   String getAuthProgram();

   void setAuthProgram(String var1) throws InvalidAttributeValueException;
}
