package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;

public interface VirtualHostMBean extends WebServerMBean, TargetMBean {
   String[] getVirtualHostNames();

   void setVirtualHostNames(String[] var1) throws InvalidAttributeValueException;

   String getNetworkAccessPoint();

   void setNetworkAccessPoint(String var1);
}
