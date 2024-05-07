package weblogic.iiop.csi;

import weblogic.diagnostics.debug.DebugLogger;
import weblogic.iiop.IIOPInputStream;
import weblogic.iiop.IIOPLogger;
import weblogic.iiop.IIOPOutputStream;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public class EstablishContext implements ContextBody {
   private static final DebugLogger debugIIOPSecurity = DebugLogger.getDebugLogger("DebugIIOPSecurity");
   private static final DebugCategory debugSecurity = Debug.getCategory("weblogic.iiop.security");
   private long clientContextId;
   private IdentityToken identityToken;
   private byte[] clientAuthenticationToken;

   public EstablishContext() {
   }

   public EstablishContext(long var1, byte[] var3, IdentityToken var4) {
      this.clientContextId = var1;
      this.identityToken = var4;
      this.clientAuthenticationToken = var3;
   }

   protected EstablishContext(IIOPInputStream var1) {
      this.clientContextId = var1.read_longlong();
      this.skipAuthorizationToken(var1);
      this.identityToken = new IdentityToken(var1);
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("read " + this);
      }

      this.clientAuthenticationToken = var1.read_octet_sequence();
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("read " + this);
      }

   }

   public long getClientContextId() {
      return this.clientContextId;
   }

   public IdentityToken getIdentityToken() {
      return this.identityToken;
   }

   public byte[] getClientAuthenticationToken() {
      return this.clientAuthenticationToken;
   }

   public void write(IIOPOutputStream var1) {
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("writing " + this);
      }

      var1.write_longlong(this.clientContextId);
      var1.write_long(0);
      if (this.identityToken == null) {
         var1.write_long(0);
         var1.write_boolean(true);
      } else {
         this.identityToken.write(var1);
      }

      var1.write_octet_sequence(this.clientAuthenticationToken);
   }

   protected void skipAuthorizationToken(IIOPInputStream var1) {
      int var2 = var1.read_long();

      for(int var3 = 0; var3 < var2; ++var3) {
         var1.read_long();
         var1.read_octet_sequence();
      }

   }

   public String toString() {
      return "EstablishContext (clientContext = " + this.clientContextId + "\n   identityToken = " + this.identityToken + ")";
   }

   private static void p(String var0) {
      IIOPLogger.logDebugSecurity("<EstablishContext>: " + var0);
   }
}
