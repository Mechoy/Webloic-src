package weblogic.wsee.jaxws;

import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;
import weblogic.jws.jaxws.client.ClientIdentityFeature;

public class WseeClientExecuteTube extends AbstractFilterTubeImpl {
   private ClientIdentityFeature _feature;

   public WseeClientExecuteTube(ClientIdentityFeature var1, Tube var2) {
      super(var2);
      this._feature = var1;
   }

   public WseeClientExecuteTube(WseeClientExecuteTube var1, TubeCloner var2) {
      super(var1, var2);
      this._feature = var1._feature;
   }

   public AbstractTubeImpl copy(TubeCloner var1) {
      return new WseeClientExecuteTube(this, var1);
   }

   public NextAction processRequest(Packet var1) {
      MonitoringPipe.MonitoringPropertySet var2 = (MonitoringPipe.MonitoringPropertySet)var1.getSatellite(MonitoringPipe.MonitoringPropertySet.class);
      if (var2 != null) {
         var2.setExecutionBegin(System.nanoTime());
      }

      return this.doInvoke(this.next, var1);
   }

   public NextAction processResponse(Packet var1) {
      MonitoringPipe.MonitoringPropertySet var2 = (MonitoringPipe.MonitoringPropertySet)var1.getSatellite(MonitoringPipe.MonitoringPropertySet.class);
      if (var2 != null) {
         var2.setExecutionEnd(System.nanoTime());
      }

      return this.doReturnWith(var1);
   }
}
