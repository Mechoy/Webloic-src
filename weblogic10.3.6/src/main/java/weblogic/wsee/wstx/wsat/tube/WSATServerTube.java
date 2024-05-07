package weblogic.wsee.wstx.wsat.tube;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;
import java.util.Set;
import weblogic.wsee.wstx.wsat.TransactionalFeature;
import weblogic.wsee.wstx.wsat.WSATConstants;

public class WSATServerTube extends AbstractFilterTubeImpl implements WSATConstants {
   private static final String WSATATTRIBUTE = "weblogic.wsee.wsat.attribute";
   ServerTubeAssemblerContext m_context;
   boolean isTransactionalAnnotationPresent;
   private WSDLPort m_port;
   private TransactionalFeature m_transactionalFeature;
   WSATServer m_wsatServerHelper = new WSATServerHelper();

   public WSATServerTube(Tube var1, ServerTubeAssemblerContext var2, TransactionalFeature var3) {
      super(var1);
      this.commonConstruct(var2, var3);
   }

   private void commonConstruct(ServerTubeAssemblerContext var1, TransactionalFeature var2) {
      this.m_context = var1;
      this.m_port = var1.getWsdlModel();
      this.m_transactionalFeature = var2;
   }

   public WSATServerTube(WSATServerTube var1, TubeCloner var2) {
      super(var1, var2);
      this.m_context = var1.m_context;
      this.m_port = var1.m_port;
      this.m_transactionalFeature = var1.m_transactionalFeature;
   }

   @NotNull
   public NextAction processRequest(Packet var1) {
      TransactionalAttribute var2 = WSATTubeHelper.getTransactionalAttribute(this.m_transactionalFeature, var1, this.m_port);
      var2.setSoapVersion(this.m_context.getEndpoint().getBinding().getSOAPVersion());
      var1.invocationProperties.put("weblogic.wsee.wsat.attribute", var2);
      HeaderList var3 = var1.getMessage().getHeaders();
      this.m_wsatServerHelper.doHandleRequest(var3, var2);
      if (!var2.isEnabled()) {
         Set var4 = MUHeaderHelper.getMUWSATHeaders(var3);
         if (var4 != null && !var4.isEmpty()) {
            return this.doReturnWith(var1.createResponse(MUHeaderHelper.createMUSOAPFaultMessage(var2.getSoapVersion(), var4)));
         }
      }

      return super.processRequest(var1);
   }

   @NotNull
   public NextAction processResponse(Packet var1) {
      TransactionalAttribute var2 = (TransactionalAttribute)var1.invocationProperties.get("weblogic.wsee.wsat.attribute");
      this.m_wsatServerHelper.doHandleResponse(var2);
      return super.processResponse(var1);
   }

   @NotNull
   public NextAction processException(Throwable var1) {
      this.m_wsatServerHelper.doHandleException(var1);
      return super.processException(var1);
   }

   public void preDestroy() {
      super.preDestroy();
   }

   public AbstractTubeImpl copy(TubeCloner var1) {
      return new WSATServerTube(this, var1);
   }

   NextAction doProcessResponse(Packet var1) {
      return super.processResponse(var1);
   }
}
