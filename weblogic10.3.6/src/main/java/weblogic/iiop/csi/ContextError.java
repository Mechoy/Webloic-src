package weblogic.iiop.csi;

import weblogic.diagnostics.debug.DebugLogger;
import weblogic.iiop.IIOPInputStream;
import weblogic.iiop.IIOPLogger;
import weblogic.iiop.IIOPOutputStream;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.Hex;

public class ContextError implements ContextBody {
   private static final DebugLogger debugIIOPSecurity = DebugLogger.getDebugLogger("DebugIIOPSecurity");
   private static final DebugCategory debugSecurity = Debug.getCategory("weblogic.iiop.security");
   private long clientContextId;
   private int majorStatus;
   private int minorStatus;
   private byte[] errorToken;

   public ContextError() {
   }

   public ContextError(long var1, int var3, int var4, byte[] var5) {
      this.clientContextId = var1;
      this.majorStatus = var3;
      this.minorStatus = var4;
      this.errorToken = var5;
   }

   protected ContextError(IIOPInputStream var1) {
      this.clientContextId = var1.read_longlong();
      this.majorStatus = var1.read_long();
      this.minorStatus = var1.read_long();
      this.errorToken = var1.read_octet_sequence();
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("read " + this);
      }

   }

   public long getClientContextId() {
      return this.clientContextId;
   }

   public int getMajorStatus() {
      return this.majorStatus;
   }

   public int getMinorStatus() {
      return this.minorStatus;
   }

   public byte[] getErrorToken() {
      return this.errorToken;
   }

   public void write(IIOPOutputStream var1) {
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("writing " + this);
      }

      var1.write_longlong(this.clientContextId);
      var1.write_long(this.majorStatus);
      var1.write_long(this.minorStatus);
      var1.write_octet_sequence(this.errorToken);
   }

   public String toString() {
      return "ContextError (clientContext = " + this.clientContextId + ", major = " + this.majorStatus + ", minor = " + this.minorStatus + ", errorToken = " + Hex.dump(this.errorToken) + ")";
   }

   private static void p(String var0) {
      IIOPLogger.logDebugSecurity("<ContextError>: " + var0);
   }
}
