package weblogic.cluster.replication;

import java.util.ArrayList;
import weblogic.rmi.spi.HostID;

public interface SecondarySelector {
   HostID getSecondarySrvr();

   ArrayList getSecondaryCandidates();

   void removeDeadSecondarySrvr(HostID var1);
}
