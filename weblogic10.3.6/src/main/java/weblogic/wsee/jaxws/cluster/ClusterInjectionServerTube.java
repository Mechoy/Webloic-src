package weblogic.wsee.jaxws.cluster;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;
import weblogic.wsee.jaxws.persistence.PersistenceConfig;

public class ClusterInjectionServerTube extends AbstractFilterTubeImpl {
   private ClusterInjectionTubeUtils _util;
   private ServerTubeAssemblerContext _context;

   public ClusterInjectionServerTube(Tube var1, ServerTubeAssemblerContext var2) {
      super(var1);
      this._context = var2;
      this.commonConstructorCode(var2);
   }

   private void commonConstructorCode(ServerTubeAssemblerContext var1) {
      PersistenceConfig.Service var2 = PersistenceConfig.getServiceConfig(var1);
      this._util = new ClusterInjectionTubeUtils(var1.getEndpoint().getBinding(), var2);
   }

   public ClusterInjectionServerTube(ClusterInjectionServerTube var1, TubeCloner var2) {
      super(var1, var2);
      this._context = var1._context;
      this.commonConstructorCode(this._context);
   }

   public AbstractTubeImpl copy(TubeCloner var1) {
      return new ClusterInjectionServerTube(this, var1);
   }

   @NotNull
   public NextAction processRequest(@NotNull Packet var1) {
      return this.doInvoke(this.next, var1);
   }

   @NotNull
   public NextAction processResponse(@NotNull Packet var1) {
      Packet var2 = this._util.processOutboundMessage(var1);
      if (var2 != null) {
         throw new IllegalStateException("Attempt to send new request from response side of cluster injection server tube");
      } else {
         return this.doReturnWith(var1);
      }
   }

   @NotNull
   public NextAction processException(@NotNull Throwable var1) {
      return this.doThrow(var1);
   }
}
