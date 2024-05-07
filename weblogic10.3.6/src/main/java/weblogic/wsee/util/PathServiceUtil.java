package weblogic.wsee.util;

import java.security.AccessController;
import weblogic.common.CompletionRequest;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.messaging.path.Member;
import weblogic.messaging.path.helper.KeyString;
import weblogic.messaging.path.helper.MemberString;
import weblogic.messaging.path.helper.PathHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class PathServiceUtil {
   private static final ServerMBean server = ManagementService.getRuntimeAccess((AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction())).getServer();
   private static final boolean isStandAloneServer;

   public static void saveConversationIdMapping(String var0) throws Throwable {
      if (!isStandAloneServer) {
         String var1 = makeSureValidConversationId(var0);
         CompletionRequest var2 = new CompletionRequest();
         PathHelper.manager().cachedPutIfAbsent(PathHelper.DEFAULT_PATH_SERVICE_JNDI, new KeyString((byte)4, var1, var1), new MemberString(server.getName(), server.getName()), 32768, var2);
         synchronized(var2) {
            var2.getResult();
         }
      }
   }

   public static String getServerNameFromPathService(String var0) throws Throwable {
      if (isStandAloneServer) {
         return null;
      } else {
         String var1 = makeSureValidConversationId(var0);
         CompletionRequest var2 = new CompletionRequest();
         PathHelper.manager().cachedGet(PathHelper.DEFAULT_PATH_SERVICE_JNDI, new KeyString((byte)4, var1, var1), 32768, var2);
         Member var3 = (Member)var2.getResult();
         return var3 == null ? null : var3.getWLServerName();
      }
   }

   public static void removeConversationIdMapping(String var0) throws Throwable {
      if (!isStandAloneServer) {
         String var1 = makeSureValidConversationId(var0);
         PathHelper.manager().cachedRemove(PathHelper.DEFAULT_PATH_SERVICE_JNDI, new KeyString((byte)4, var1, var1), new MemberString(server.getName(), server.getName()), 33280);
      }
   }

   private static String makeSureValidConversationId(String var0) {
      String var1 = var0.replace(":", ";");
      var1 = var1.replace(",", ".");
      var1 = var1.replace("=", "-");
      var1 = var1.replace("*", "8");
      var1 = var1.replace("?", "-");
      return var1;
   }

   static {
      isStandAloneServer = server.getCluster() == null;
   }
}
