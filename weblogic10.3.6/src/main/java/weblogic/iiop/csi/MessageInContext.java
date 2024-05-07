package weblogic.iiop.csi;

import weblogic.diagnostics.debug.DebugLogger;
import weblogic.iiop.IIOPInputStream;
import weblogic.iiop.IIOPLogger;
import weblogic.iiop.IIOPOutputStream;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public class MessageInContext implements ContextBody {
   private static final DebugLogger debugIIOPSecurity = DebugLogger.getDebugLogger("DebugIIOPSecurity");
   private static final DebugCategory debugSecurity = Debug.getCategory("weblogic.iiop.security");
   private long clientContextId;
   private boolean discardContext;

   public MessageInContext() {
   }

   public MessageInContext(long var1, boolean var3) {
      this.clientContextId = var1;
      this.discardContext = var3;
   }

   protected MessageInContext(IIOPInputStream var1) {
      this.clientContextId = var1.read_longlong();
      this.discardContext = var1.read_boolean();
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("read " + this);
      }

   }

   public long getClientContextId() {
      return this.clientContextId;
   }

   public boolean getDiscardContext() {
      return this.discardContext;
   }

   public void write(IIOPOutputStream var1) {
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("writing " + this);
      }

      var1.write_longlong(this.clientContextId);
      var1.write_boolean(this.discardContext);
   }

   public String toString() {
      return "MessageInContext (clientContext = " + this.clientContextId + ", discardContext = " + this.discardContext + ")";
   }

   private static void p(String var0) {
      IIOPLogger.logDebugSecurity("<MessageInContext>: " + var0);
   }
}
