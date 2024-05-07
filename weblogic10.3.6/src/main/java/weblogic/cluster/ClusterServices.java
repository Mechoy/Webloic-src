package weblogic.cluster;

import java.util.Collection;
import weblogic.cluster.singleton.LeaseManager;
import weblogic.cluster.singleton.LeasingBasis;

public interface ClusterServices {
   MulticastSession createMulticastSession(RecoverListener var1, int var2);

   MulticastSession createMulticastSession(RecoverListener var1, int var2, boolean var3);

   ClusterMemberInfo getLocalMember();

   Collection getRemoteMembers();

   Collection getClusterMasterMembers();

   void addClusterMembersListener(ClusterMembersChangeListener var1);

   void removeClusterMembersListener(ClusterMembersChangeListener var1);

   void addHeartbeatMessage(GroupMessage var1);

   void removeHeartbeatMessage(GroupMessage var1);

   void resendLocalAttributes();

   LeasingBasis getDefaultLeasingBasis();

   LeaseManager getDefaultLeaseManager(String var1);
}
