package weblogic.cluster.messaging.internal;

import java.io.IOException;

public interface Group {
   GroupMember[] getMembers();

   void send(Message var1) throws IOException;

   boolean isLocal();

   void start();

   void stop();

   ServerConfigurationInformation getConfigInformation(String var1);

   GroupMember findOrCreateGroupMember(ServerConfigurationInformation var1, long var2);

   void forward(Message var1, Connection var2);
}
