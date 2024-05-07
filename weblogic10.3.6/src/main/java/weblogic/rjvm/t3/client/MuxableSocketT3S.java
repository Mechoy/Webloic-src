package weblogic.rjvm.t3.client;

import javax.net.ssl.SSLSocketFactory;
import weblogic.protocol.ChannelHelperBase;
import weblogic.protocol.ServerChannel;
import weblogic.socket.SSLFilter;

public final class MuxableSocketT3S extends MuxableSocketT3 {
   private static final long serialVersionUID = -1499853227078510946L;

   MuxableSocketT3S(ServerChannel var1) {
      super(var1);
      this.setSocketFactory(new T3ClientWeblogicSocketFactory((SSLSocketFactory)SSLSocketFactory.getDefault(), var1));
      if (ChannelHelperBase.isAdminChannel(var1)) {
         this.connection.setAdminQOS();
      }

   }

   public void ensureForceClose() {
      ((SSLFilter)this.getSocketFilter()).ensureForceClose();
   }
}
