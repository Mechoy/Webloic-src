package weblogic.cluster.messaging.internal;

import java.io.Serializable;
import java.net.InetAddress;

public interface ServerConfigurationInformation extends Comparable, Serializable {
   long serialVersionUID = -5667123813958435540L;

   InetAddress getAddress();

   int getPort();

   String getServerName();

   long getCreationTime();

   boolean isUsingSSL();
}
