package weblogic.wsee.jaxws.tubeline.standard;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.jws.jaxws.client.ClientIdentityFeature;
import weblogic.jws.jaxws.client.ClientIdentityFeature.Type;
import weblogic.jws.jaxws.client.async.AsyncClientFeatureListFeature;
import weblogic.wsee.jaxws.WLSContainer;
import weblogic.wsee.jaxws.spi.ClientIdentityRegistry;

public class WseeServerTube extends AbstractFilterTubeImpl {
   private static final Logger LOGGER = Logger.getLogger(WseeServerTube.class.getName());
   private static boolean _loadedVerboseConfigOnce = false;
   private ClientIdentityFeature _feature;
   private ServerTubeAssemblerContext _context;

   public WseeServerTube(ServerTubeAssemblerContext var1, Tube var2) {
      super(var2);
      if (!_loadedVerboseConfigOnce) {
         WLSContainer.setupLogging();
      }

      ClientIdentityFeature var3 = (ClientIdentityFeature)var1.getEndpoint().getBinding().getFeature(ClientIdentityFeature.class);
      if (var3 == null) {
         AsyncClientFeatureListFeature var4 = (AsyncClientFeatureListFeature)var1.getEndpoint().getBinding().getFeature(AsyncClientFeatureListFeature.class);
         String var6;
         if (var4 != null) {
            ClientIdentityFeature var5 = (ClientIdentityFeature)var4.getClientFeatures().get(ClientIdentityFeature.class);
            if (var5 != null) {
               var6 = var5.getRawClientId() + "-SystemClient";
               var3 = new ClientIdentityFeature();
               var3.setClientId(var6, true);
               ((WebServiceFeatureList)var1.getEndpoint().getBinding().getFeatures()).add(var3);
            }
         }

         if (var3 == null) {
            String var7 = var1.getEndpoint().getEndpointId();
            var6 = var7 + "-SystemClient";
            var3 = new ClientIdentityFeature();
            var3.setClientId(var6, false);
            ((WebServiceFeatureList)var1.getEndpoint().getBinding().getFeatures()).add(var3);
         }

         var3.setType(Type.SYSTEM);
      }

      ClientIdentityRegistry.initClientInstanceIdentityFeature(var1);
      this._feature = var3;
      this._context = var1;
      this.register();
   }

   public WseeServerTube(WseeServerTube var1, TubeCloner var2) {
      super(var1, var2);
      this._feature = var1._feature;
      this._context = var1._context;
      this.register();
   }

   public AbstractTubeImpl copy(TubeCloner var1) {
      return new WseeServerTube(this, var1);
   }

   private void register() {
      ClientIdentityRegistry.registerClientIdentity(this._feature);
      ClientIdentityRegistry.addClientRuntimeMBean(this._feature, this._context);
   }

   public void preDestroy() {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Destroying WseeServerTube with system client ID: " + (this._feature != null ? this._feature.getClientId() : null));
      }

      if (this._feature != null) {
         this._feature.dispose();
         this._feature = null;
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
