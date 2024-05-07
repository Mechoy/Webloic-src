package weblogic.wsee.security.wssp.tube;

import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.Fiber;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;
import java.io.IOException;
import javax.xml.soap.SOAPException;
import weblogic.wsee.jaxws.framework.jaxrpc.EnvironmentFactory;
import weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.security.wssp.handlers.WSTHeuristicProcessor;

public class WSTHeuristicTube extends AbstractFilterTubeImpl {
   private EnvironmentFactory fac = null;
   private WSTHeuristicProcessor processor;

   public WSTHeuristicTube(WSTHeuristicTube var1, TubeCloner var2) {
      super(var1, var2);
      this.fac = var1.fac;
      this.processor = null;
   }

   public WSTHeuristicTube(EnvironmentFactory var1, Tube var2) {
      super(var2);
      this.fac = var1;
   }

   public AbstractTubeImpl copy(TubeCloner var1) {
      return new WSTHeuristicTube(this, var1);
   }

   public NextAction processRequest(Packet var1) {
      if (this.processor == null) {
         this.processor = new WSTHeuristicProcessor();
      }

      SOAPMessageContext var2 = this.fac.getContext(var1);
      this.processor.setReserve(new Reserve(var1));
      if (this.processor.processRequest(var2)) {
         var2.updatePacket();
      }

      return super.processRequest(var1);
   }

   public NextAction processResponse(Packet var1) {
      SOAPMessageContext var2 = this.fac.getContext(var1);
      if (this.processor.processResponse(var2)) {
         ((SOAPMessageContext)this.processor.getReserve().getSoapMessageContext()).updatePacket();
         return this.doInvoke(this.next, ((Reserve)this.processor.getReserve()).getOriginalRequest());
      } else {
         return super.processResponse(var1);
      }
   }

   public NextAction processException(Throwable var1) {
      Packet var2 = Fiber.current().getPacket();
      SOAPMessageContext var3 = this.fac.getContext(var2);
      if (this.processor.processResponse(var3)) {
         ((SOAPMessageContext)this.processor.getReserve().getSoapMessageContext()).updatePacket();
         return this.doInvoke(this.next, ((Reserve)this.processor.getReserve()).getOriginalRequest());
      } else {
         return super.processException(var1);
      }
   }

   private class Reserve extends WSTHeuristicProcessor.Reserve {
      private Packet originalRequest;

      public Reserve(Packet var2) {
         this.originalRequest = var2;
      }

      public Packet getOriginalRequest() {
         return this.originalRequest;
      }

      public SoapMessageContext getSoapMessageContext() {
         return WSTHeuristicTube.this.fac.getContext(this.originalRequest);
      }

      public void reserveContext() throws SOAPException, IOException {
         super.reserveContext();
         if (this.properties.containsKey("weblogic.wsee.jaxws.framework.jaxrpc.context")) {
            this.properties.remove("weblogic.wsee.jaxws.framework.jaxrpc.context");
         }

      }
   }
}
