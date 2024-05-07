package weblogic.wsee.wstx.wsat.tube;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Fiber;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import weblogic.wsee.wstx.wsat.TransactionalFeature;
import weblogic.wsee.wstx.wsat.WSATConstants;

public class WSATClientTube extends AbstractFilterTubeImpl implements WSATConstants {
   WSBinding m_wsbinding;
   WSATClient m_wsatClientHelper = new WSATClientHelper();
   private TransactionalFeature m_transactionalFeature;
   private WSDLPort m_port;

   public WSATClientTube(Tube var1, ClientTubeAssemblerContext var2, TransactionalFeature var3) {
      super(var1);
      this.m_wsbinding = var2.getBinding();
      this.m_transactionalFeature = var3;
      this.m_port = var2.getWsdlModel();
   }

   private WSATClientTube(WSATClientTube var1, TubeCloner var2) {
      super(var1, var2);
      this.m_wsbinding = var1.m_wsbinding;
      this.m_transactionalFeature = var1.m_transactionalFeature;
      this.m_port = var1.m_port;
   }

   public Set<QName> getHeaders() {
      return new HashSet();
   }

   @NotNull
   public NextAction processRequest(@NotNull Packet var1) {
      TransactionalAttribute var2 = WSATTubeHelper.getTransactionalAttribute(this.m_transactionalFeature, var1, this.m_port);
      var2.setSoapVersion(this.m_wsbinding.getSOAPVersion());
      List var3 = this.m_wsatClientHelper.doHandleRequest(var2, var1.invocationProperties);
      if (var3 != null) {
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            Header var5 = (Header)var4.next();
            var1.getMessage().getHeaders().add(var5);
         }
      }

      return super.processRequest(var1);
   }

   @NotNull
   public NextAction processResponse(@NotNull Packet var1) {
      this.m_wsatClientHelper.doHandleResponse(var1.invocationProperties);
      return super.processResponse(var1);
   }

   @NotNull
   public NextAction processException(Throwable var1) {
      Map var2 = Fiber.current().getPacket().invocationProperties;
      this.m_wsatClientHelper.doHandleResponse(var2);
      return super.processException(var1);
   }

   public AbstractTubeImpl copy(TubeCloner var1) {
      return new WSATClientTube(this, var1);
   }
}
