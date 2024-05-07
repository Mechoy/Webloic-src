package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;

/** @deprecated */
public interface NTRealmMBean extends BasicRealmMBean {
   String getPrimaryDomain();

   void setPrimaryDomain(String var1) throws InvalidAttributeValueException;

   int getPreferredMaxBytes();

   void setPreferredMaxBytes(int var1) throws InvalidAttributeValueException;

   boolean getIgnoreBadDomainName();

   void setIgnoreBadDomainName(boolean var1);
}
