package weblogic.wsee.jaxws.workcontext;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;
import java.io.IOException;
import weblogic.workarea.WorkContextHelper;
import weblogic.workarea.WorkContextInput;
import weblogic.workarea.WorkContextOutput;
import weblogic.workarea.spi.WorkContextMapInterceptor;
import weblogic.wsee.workarea.WorkAreaConstants;

public class WorkContextClientTube extends WorkContextTube {
   public WorkContextClientTube(Tube var1) {
      super(var1);
   }

   public WorkContextClientTube(WorkContextTube var1, TubeCloner var2) {
      super(var1, var2);
   }

   public AbstractTubeImpl copy(TubeCloner var1) {
      return new WorkContextClientTube(this, var1);
   }

   public NextAction processRequest(Packet var1) {
      WorkContextMapInterceptor var2 = getContext();
      if (var2 != null && this.hasContext(64, false)) {
         this.writeHeader(var2, var1.getMessage().getHeaders());
      }

      return super.processRequest(var1);
   }

   public NextAction processResponse(Packet var1) {
      if (var1.getMessage() != null) {
         HeaderList var2 = var1.getMessage().getHeaders();
         Header var3 = var2.get(WorkAreaConstants.WORK_AREA_HEADER, true);
         if (var3 != null) {
            this.readHeaderOld(var3);
         }

         Header var4 = var2.get(this.JAX_WS_WORK_AREA_HEADER, true);
         if (var4 != null) {
            this.readHeader(var4);
         }
      }

      return super.processResponse(var1);
   }

   protected void receive(WorkContextInput var1) throws IOException {
      WorkContextMapInterceptor var2 = WorkContextHelper.getWorkContextHelper().getInterceptor();
      var2.receiveResponse(var1);
   }

   protected void send(WorkContextMapInterceptor var1, WorkContextOutput var2, int var3) throws IOException {
      var1.sendRequest(var2, var3);
   }
}
