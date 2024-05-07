package weblogic.iiop.csi;

import weblogic.diagnostics.debug.DebugLogger;
import weblogic.iiop.IIOPInputStream;
import weblogic.iiop.IIOPLogger;
import weblogic.iiop.IIOPOutputStream;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.Hex;

public class CompleteEstablishContext implements ContextBody {
   private static final DebugLogger debugIIOPSecurity = DebugLogger.getDebugLogger("DebugIIOPSecurity");
   private static final DebugCategory debugSecurity = Debug.getCategory("weblogic.iiop.security");
   private long clientContextId;
   private boolean contextStateful;
   private byte[] finalContextToken;

   public CompleteEstablishContext() {
   }

   public CompleteEstablishContext(long var1, boolean var3, byte[] var4) {
      this.clientContextId = var1;
      this.contextStateful = var3;
      this.finalContextToken = var4;
   }

   protected CompleteEstablishContext(IIOPInputStream var1) {
      this.clientContextId = var1.read_longlong();
      this.contextStateful = var1.read_boolean();
      this.finalContextToken = var1.read_octet_sequence();
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("read " + this);
      }

   }

   public long getClientContextId() {
      return this.clientContextId;
   }

   public boolean getContextStateful() {
      return this.contextStateful;
   }

   public byte[] getFinalContextToken() {
      return this.finalContextToken;
   }

   public void write(IIOPOutputStream var1) {
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("writing " + this);
      }

      var1.write_longlong(this.clientContextId);
      var1.write_boolean(this.contextStateful);
      var1.write_octet_sequence(this.finalContextToken);
   }

   public String toString() {
      return "CompleteEstablishContext (clientContext = " + this.clientContextId + ", contextStateful = " + this.contextStateful + ", finalContextToken = " + Hex.dump(this.finalContextToken) + ")";
   }

   private static void p(String var0) {
      IIOPLogger.logDebugSecurity("<CompleteEstablishContext>: " + var0);
   }
}
