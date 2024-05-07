package weblogic.jms.forwarder.dd;

import java.io.Externalizable;
import weblogic.jms.common.DestinationImpl;

public interface DDMemberInfo extends Externalizable {
   String getJMSServerName();

   String getDDMemberConfigName();

   String getType();

   DDMemberRuntimeInformation getDDMemberRuntimeInformation();

   DestinationImpl getDestination();

   void setDestination(DestinationImpl var1);
}
