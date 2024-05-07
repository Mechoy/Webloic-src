package weblogic.wsee.reliability;

import java.io.Serializable;
import javax.xml.rpc.JAXRPCException;
import weblogic.store.PersistentStoreException;
import weblogic.wsee.cluster.ClusterService;
import weblogic.wsee.cluster.ClusterServiceException;
import weblogic.wsee.conversation.ConversationInvokeState;
import weblogic.wsee.conversation.ConversationInvokeStateObjectHandler;
import weblogic.wsee.server.WsStorage;
import weblogic.wsee.server.WsStorageFactory;
import weblogic.wsee.util.Verbose;

public final class ReliableConversationMsgClusterService implements ClusterService {
   private static final boolean verbose = Verbose.isVerbose(ReliableConversationMsgClusterService.class);
   public static final String TARGET_URI = "weblogic.wsee.reliability.conversation.msg.cluster.service";

   public String getTargetURI() {
      return "weblogic.wsee.reliability.conversation.msg.cluster.service";
   }

   public Serializable dispatch(Serializable var1) throws ClusterServiceException {
      if (!(var1 instanceof ReliableConversationEPR)) {
         throw new ClusterServiceException("Invalid object type: " + var1);
      } else {
         ReliableConversationEPR var2 = (ReliableConversationEPR)var1;
         if (verbose) {
            Verbose.log((Object)"------------------------------");
            Verbose.log((Object)("Invoking reliable message cluster service for Reliable EPR: " + var2));
            Verbose.log((Object)"------------------------------");
         }

         WsStorage var3 = WsStorageFactory.getStorage("weblogic.wsee.conversation.store", new ConversationInvokeStateObjectHandler());
         ConversationInvokeState var4 = new ConversationInvokeState();
         var4.setRmState(true);
         var4.setSeqId(var2.getSeqId());
         var4.setEpr(var2.getEPR());

         try {
            var3.persistentPut(var2.getKey(), var4);
            return null;
         } catch (PersistentStoreException var6) {
            throw new JAXRPCException(var6);
         }
      }
   }
}
