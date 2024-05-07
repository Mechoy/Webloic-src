package weblogic.wsee.reliability2.sequence;

import com.sun.xml.ws.api.message.Packet;
import weblogic.wsee.reliability2.saf.WsrmSAFDispatchFactory;

public interface SequenceIdFactory {
   Info getSequenceId(String var1, boolean var2, Packet var3);

   public static class Info {
      public String id;
      public boolean preExisting;
      public WsrmSAFDispatchFactory.Key safDispatchKey;
   }
}
