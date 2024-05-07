package weblogic.wsee.jaxws.tubeline.standard;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.jws.jaxws.client.ClientIdentityFeature;
import weblogic.wsee.jaxws.WLSContainer;
import weblogic.wsee.jaxws.spi.ClientIdentityRegistry;
import weblogic.wsee.jaxws.spi.ClientInstanceIdentityFeature;

public class WseeClientTube extends AbstractFilterTubeImpl {
   private static final Logger LOGGER = Logger.getLogger(WseeClientTube.class.getName());
   private static boolean _loadedVerboseConfigOnce = false;
   private ClientIdentityFeature _feature;
   private ClientInstanceIdentityFeature _instanceIdFeature;
   private ClientTubeAssemblerContext _context;

   public WseeClientTube(ClientTubeAssemblerContext var1, Tube var2) {
      super(var2);
      if (!_loadedVerboseConfigOnce) {
         WLSContainer.setupLogging();
      }

      this._feature = (ClientIdentityFeature)var1.getBinding().getFeature(ClientIdentityFeature.class);
      this._instanceIdFeature = (ClientInstanceIdentityFeature)var1.getBinding().getFeature(ClientInstanceIdentityFeature.class);
      this._context = var1;
      this.register();
   }

   public WseeClientTube(WseeClientTube var1, TubeCloner var2) {
      super(var1, var2);
      this._feature = var1._feature;
      this._instanceIdFeature = var1._instanceIdFeature;
      this._context = var1._context;
      this.register();
   }

   public AbstractTubeImpl copy(TubeCloner var1) {
      return new WseeClientTube(this, var1);
   }

   private void register() {
      ClientIdentityRegistry.addClientRuntimeMBean(this._feature, this._context);
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("WseeClientTube " + this + " set client instance id: " + (this._instanceIdFeature != null ? this._instanceIdFeature.getClientInstanceId() : null));
      }

   }

   public void preDestroy() {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Destroying WseeClientTube " + this + " with client instance ID: " + (this._instanceIdFeature != null ? this._instanceIdFeature.getClientInstanceId() : null));
      }

      if (this._instanceIdFeature != null) {
         this._instanceIdFeature.dispose();
      }

      if (this.next != null) {
         this.next.preDestroy();
      }

   }

   @NotNull
   public NextAction processRequest(Packet var1) {
      return this.doInvoke(this.next, var1);
   }

   @NotNull
   public NextAction processResponse(Packet var1) {
      return this.doReturnWith(var1);
   }
}
