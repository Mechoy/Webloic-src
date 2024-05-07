package weblogic.iiop.csi;

import weblogic.corba.cos.security.GSSUtil;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.iiop.IIOPInputStream;
import weblogic.iiop.IIOPLogger;
import weblogic.iiop.IIOPOutputStream;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.Hex;

public class ASContextSec {
   private static final DebugLogger debugIIOPSecurity = DebugLogger.getDebugLogger("DebugIIOPSecurity");
   private static final DebugCategory debugSecurity = Debug.getCategory("weblogic.iiop.security");
   private short supports;
   private short requires;
   private byte[] clientAuthenticationMech;
   private byte[] targetName;

   public ASContextSec(boolean var1, boolean var2) {
      this.supports = 0;
      if (var1) {
         this.supports = 64;
      }

      this.requires = 0;
      if (var2) {
         this.requires = (short)(this.requires | 64);
      }

      this.clientAuthenticationMech = GSSUtil.getGSSUPMech();
      String var3 = "weblogicDEFAULT";
      this.targetName = GSSUtil.createGSSUPGSSNTExportedName(var3);
   }

   ASContextSec(IIOPInputStream var1) {
      this.read(var1);
   }

   public final void read(IIOPInputStream var1) {
      this.supports = var1.read_short();
      this.requires = var1.read_short();
      this.clientAuthenticationMech = var1.read_octet_sequence();
      this.targetName = var1.read_octet_sequence();
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("read " + this);
      }

   }

   public final void write(IIOPOutputStream var1) {
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("writing " + this);
      }

      var1.write_short(this.supports);
      var1.write_short(this.requires);
      var1.write_octet_sequence(this.clientAuthenticationMech);
      var1.write_octet_sequence(this.targetName);
   }

   public final boolean hasGSSUP() {
      return this.supports != 0 && GSSUtil.isGSSUPMech(this.clientAuthenticationMech);
   }

   public final byte[] getGSSUPTarget() {
      return this.targetName;
   }

   public short getSupports() {
      return this.supports;
   }

   public short getRequires() {
      return this.requires;
   }

   public String toString() {
      return "ASContextSec (supports = " + this.supports + ",requires = " + this.requires + ",clientAuthMech = " + Hex.dump(this.clientAuthenticationMech) + ",targetName = " + Hex.dump(this.targetName) + ")";
   }

   private static void p(String var0) {
      IIOPLogger.logDebugSecurity("<ASContextSec>: " + var0);
   }
}
