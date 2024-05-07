package weblogic.wsee.jaxws.cluster;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;
import com.sun.xml.ws.api.server.TransportBackChannel;
import com.sun.xml.ws.api.server.WSEndpoint;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClusterRoutingServerTube extends AbstractFilterTubeImpl {
   private static final Logger LOGGER = Logger.getLogger(ClusterRoutingServerTube.class.getName());
   private ClusterRoutingTubeUtils _util;
   private ServerTubeAssemblerContext _context;
   private WSEndpoint _endpoint;
   private TransportBackChannel _backChannel;

   public ClusterRoutingServerTube(Tube var1, ServerTubeAssemblerContext var2, WSEndpoint var3) {
      super(var1);
      this._context = var2;
      this._endpoint = var3;
      this.commonConstructorCode(var2, var3);
   }

   private void commonConstructorCode(ServerTubeAssemblerContext var1, WSEndpoint var2) {
      this._util = new ClusterRoutingTubeUtils(var1.getEndpoint().getBinding(), var2);
   }

   public ClusterRoutingServerTube(ClusterRoutingServerTube var1, TubeCloner var2) {
      super(var1, var2);
      this._context = var1._context;
      this._endpoint = var1._endpoint;
      this.commonConstructorCode(this._context, this._endpoint);
   }

   public AbstractTubeImpl copy(TubeCloner var1) {
      return new ClusterRoutingServerTube(this, var1);
   }

   @NotNull
   public NextAction processRequest(@NotNull Packet var1) {
      this._backChannel = var1.keepTransportBackChannelOpen();

      try {
         Packet var2 = this._util.handleInboundMessage(var1);
         return var2 != null ? this.doReturnWith(var2) : this.doInvoke(this.next, var1);
      } catch (Exception var3) {
         if (LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.log(Level.WARNING, var3.toString(), var3);
         }

         return this.doThrow(var3);
      }
   }

   @NotNull
   public NextAction processResponse(@NotNull Packet var1) {
      var1.transportBackChannel = this._backChannel;
      this._util.processOutboundMessage(var1);
      return this.doReturnWith(var1);
   }

   @NotNull
   public NextAction processException(@NotNull Throwable var1) {
      return this.doThrow(var1);
   }
}
