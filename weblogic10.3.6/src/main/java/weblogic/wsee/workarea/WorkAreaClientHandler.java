package weblogic.wsee.workarea;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import weblogic.workarea.WorkContextHelper;
import weblogic.workarea.spi.WorkContextMapInterceptor;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.Verbose;

public class WorkAreaClientHandler extends WorkAreaHandler {
   private static final boolean verbose = Verbose.isVerbose(WorkAreaClientHandler.class);

   public boolean handleRequest(MessageContext var1) {
      try {
         WlMessageContext var2 = WlMessageContext.narrow(var1);
         WorkContextMapInterceptor var3 = getContext();
         if (var3 != null && this.hasContext(64)) {
            WorkAreaHeader var4 = new WorkAreaHeader();
            ByteArrayOutputStream var5 = new ByteArrayOutputStream();
            WorkContextXmlOutputAdapter var6 = new WorkContextXmlOutputAdapter(var5);
            var3.sendRequest(var6, 64);
            var6.close();
            var4.parse(new ByteArrayInputStream(var5.toByteArray()));
            if (verbose) {
               Verbose.log((Object)("Preparing to Send -> [" + var4 + "]"));
            }

            var2.getHeaders().addHeader(var4);
         }

         return true;
      } catch (IOException var7) {
         throw new JAXRPCException("Unable to procees WorkContext:" + var7);
      }
   }

   public boolean handleResponse(MessageContext var1) {
      try {
         WlMessageContext var2 = WlMessageContext.narrow(var1);
         MsgHeaders var3 = var2.getHeaders();
         WorkAreaHeader var4 = (WorkAreaHeader)var3.getHeader(WorkAreaHeader.TYPE);
         if (verbose && var4 != null) {
            Verbose.log((Object)("Received WorkAreaHeader " + var4));
         }

         if (var4 != null) {
            WorkContextMapInterceptor var5 = WorkContextHelper.getWorkContextHelper().getInterceptor();
            var5.receiveResponse(new WorkContextXmlInputAdapter(var4.getInputStream()));
         }

         return true;
      } catch (IOException var6) {
         throw new JAXRPCException("Unable to procees WorkContext:" + var6);
      }
   }
}
