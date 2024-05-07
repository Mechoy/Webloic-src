package weblogic.wsee.jaxws.cluster;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;
import weblogic.wsee.jaxws.persistence.PersistenceConfig;

public class ClusterInjectionClientTube extends AbstractFilterTubeImpl {
   private ClusterInjectionTubeUtils _util;
   private ClientTubeAssemblerContext _context;

   public ClusterInjectionClientTube(Tube var1, ClientTubeAssemblerContext var2) {
      super(var1);
      this._context = var2;
      this.commonConstructorCode(var2);
   }

   private void commonConstructorCode(ClientTubeAssemblerContext var1) {
      PersistenceConfig.Client var2 = PersistenceConfig.getClientConfig(var1);
      this._util = new ClusterInjectionTubeUtils(var1.getBinding(), var2);
   }

   public ClusterInjectionClientTube(ClusterInjectionClientTube var1, TubeCloner var2) {
      super(var1, var2);
      this._context = var1._context;
      this.commonConstructorCode(this._context);
   }

   public AbstractTubeImpl copy(TubeCloner var1) {
      return new ClusterInjectionClientTube(this, var1);
   }

   @NotNull
   public NextAction processRequest(@NotNull Packet var1) {
      Packet var2 = this._util.processOutboundMessage(var1);
      return var2 != null ? this.doReturnWith(var2) : this.doInvoke(this.next, var1);
   }

   @NotNull
   public NextAction processResponse(@NotNull Packet var1) {
      return this.doReturnWith(var1);
   }

   @NotNull
   public NextAction processException(@NotNull Throwable var1) {
      return this.doThrow(var1);
   }
}
