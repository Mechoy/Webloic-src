package weblogic.iiop;

import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.Kernel;
import weblogic.protocol.Identity;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManagerFactory;

public final class MessageForwardingHandler extends WorkAdapter {
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");
   private final EndPoint endpoint;
   private final ConnectionKey finalDest;
   private final Message m;
   private Identity identity = null;

   static void p(String var0) {
      System.err.println("<MessageForwardingHandler at " + System.currentTimeMillis() + "> " + var0);
   }

   public MessageForwardingHandler(EndPoint var1, Message var2, Identity var3, ConnectionKey var4) {
      this.endpoint = var1;
      this.m = var2;
      this.identity = var3;
      this.finalDest = var4;
      WorkManagerFactory.getInstance().getSystem().schedule(this);
   }

   public void run() {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p(" sending...");
      }

      try {
         if (this.finalDest != null) {
            ForwardingContext var4 = new ForwardingContext(this.finalDest);
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
               p("adding forwarding context" + var4);
            }

            this.m.addServiceContext(var4);
         } else {
            this.m.removeServiceContext(1111834889);
         }

         IIOPOutputStream var5 = new IIOPOutputStream(this.m.isLittleEndian(), this.endpoint);
         var5.setCodeSets(this.endpoint.getCharCodeSet(), this.endpoint.getWcharCodeSet());
         var5.setMinorVersion(this.m.getMinorVersion());
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            p("re-write message");
         }

         this.m.getMessageHeader().setFragmented(false);
         this.m.write(var5);
         if (this.m instanceof ReplyMessage) {
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
               p("flushing pending data");
            }

            IIOPInputStream var2 = this.m.getInputStream();

            while(!var2.eof()) {
               var5.write_octet(var2.read_octet());
            }
         }

         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            p(" queue send...");
         }

         this.endpoint.send(var5);
         var5.close();
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            p("... queuing completes");
         }
      } catch (Throwable var3) {
         Object var1 = var3;
         if (this.identity != null) {
            EndPointManager.purgeRoutingTable(this.identity);
            var1 = new OBJECT_NOT_EXIST("Route to object has been lost due to : " + var3, 1330446337, CompletionStatus.COMPLETED_MAYBE);
         }

         this.endpoint.handleProtocolException(this.m, (Throwable)var1);
      }

   }
}
