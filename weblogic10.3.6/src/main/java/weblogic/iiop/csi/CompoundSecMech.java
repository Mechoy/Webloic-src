package weblogic.iiop.csi;

import java.security.AccessController;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.iiop.Connection;
import weblogic.iiop.ConnectionKey;
import weblogic.iiop.IIOPInputStream;
import weblogic.iiop.IIOPLogger;
import weblogic.iiop.IIOPOutputStream;
import weblogic.iiop.TLSSecTransComponent;
import weblogic.iiop.TaggedComponent;
import weblogic.kernel.Kernel;
import weblogic.management.configuration.SSLMBean;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.ServerIdentity;
import weblogic.rmi.internal.RuntimeDescriptor;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public class CompoundSecMech {
   private static final DebugLogger debugIIOPSecurity = DebugLogger.getDebugLogger("DebugIIOPSecurity");
   private static final DebugCategory debugSecurity = Debug.getCategory("weblogic.iiop.security");
   private short requires;
   private TaggedComponent transportMech;
   private ASContextSec asContextMech;
   private SASContextSec sasContextMech;
   private boolean foreign = false;
   private boolean requiresSSL = false;

   public CompoundSecMech() {
   }

   CompoundSecMech(IIOPInputStream var1, ServerIdentity var2) {
      this.read(var1, var2);
      this.foreign = true;
   }

   public CompoundSecMech(boolean var1, String var2, ServerIdentity var3, RuntimeDescriptor var4) {
      if (var1) {
         this.transportMech = new TLSSecTransComponent(var2, var3, var4);
         this.requires = ((TLSSecTransComponent)this.transportMech).getRequires();
      } else {
         this.transportMech = null;
      }

      if (var4 != null && var4.getIntegrity() != null && "required".equals(var4.getIntegrity())) {
         this.requiresSSL = true;
      }

      SSLMBean var5 = Kernel.getConfig().getSSL();
      boolean var6 = var5.isClientCertificateEnforced();
      boolean var7 = false;

      try {
         var7 = Connection.isValidDefaultUser();
         if (!var7) {
            AuthenticatedSubject var8 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
            var7 = !ManagementService.getRuntimeAccess(var8).getDomain().getSecurity().isGuestDisabled();
         }
      } catch (Exception var13) {
      }

      boolean var14 = true;
      boolean var9 = false;
      if (!var7 && (!var1 || !var1 || !var6)) {
         var9 = true;
      }

      if (var4 != null) {
         String var10 = var4.getClientAuthentication();
         if (var10 != null) {
            if (var10.equals("none")) {
               var14 = false;
               var9 = false;
            } else if (var10.equals("supported")) {
               var14 = true;
               var9 = false;
            } else if (var10.equals("required")) {
               var14 = true;
               var9 = true;
            }
         }
      }

      boolean var15 = true;
      boolean var11 = false;
      if (var4 != null) {
         String var12 = var4.getIdentityAssertion();
         if (var12 != null) {
            if (var12.equals("none")) {
               var15 = false;
               var11 = false;
            } else if (var12.equals("supported")) {
               var15 = true;
               var11 = false;
            } else if (var12.equals("required")) {
               var15 = true;
               var11 = false;
            }
         }
      }

      this.asContextMech = new ASContextSec(var14, var9);
      this.requires |= this.asContextMech.getRequires();
      this.sasContextMech = new SASContextSec(var15, var11);
      this.requires |= this.sasContextMech.getRequires();
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("created " + this);
      }

   }

   public final TaggedComponent getTransportMech() {
      return this.transportMech;
   }

   public final ASContextSec getASContextMech() {
      return this.asContextMech;
   }

   public final SASContextSec getSASContextMech() {
      return this.sasContextMech;
   }

   public final boolean useSAS() {
      if (this.asContextMech != null && this.asContextMech.hasGSSUP()) {
         return true;
      } else {
         return this.sasContextMech != null && this.sasContextMech.hasGSSUPIdentity();
      }
   }

   public final boolean hasGSSUP() {
      return this.asContextMech != null && this.asContextMech.hasGSSUP();
   }

   public final boolean hasGSSUPIdentity() {
      return this.sasContextMech != null && this.sasContextMech.hasGSSUPIdentity();
   }

   public final byte[] getGSSUPTarget() {
      return this.asContextMech == null ? null : this.asContextMech.getGSSUPTarget();
   }

   public final String getSecureHost() {
      if (this.transportMech != null && this.transportMech instanceof TLSSecTransComponent) {
         TLSSecTransComponent var1 = (TLSSecTransComponent)this.transportMech;
         ConnectionKey[] var2 = var1.getAddresses();
         if (var2 != null) {
            return var2[0].getAddress();
         }
      }

      return null;
   }

   public final int getSecurePort() {
      if (this.transportMech != null && this.transportMech instanceof TLSSecTransComponent) {
         TLSSecTransComponent var1 = (TLSSecTransComponent)this.transportMech;
         ConnectionKey[] var2 = var1.getAddresses();
         if (var2 != null) {
            return var2[0].getPort();
         }
      }

      return -1;
   }

   public final void read(IIOPInputStream var1, ServerIdentity var2) {
      this.requires = var1.read_short();
      int var3 = var1.read_long();
      switch (var3) {
         case 34:
            new TaggedComponent(var3, var1);
            this.transportMech = null;
            break;
         case 36:
            this.transportMech = new TLSSecTransComponent(var1, var2);
            break;
         default:
            this.transportMech = new TaggedComponent(var3, var1);
      }

      this.asContextMech = new ASContextSec(var1);
      this.sasContextMech = new SASContextSec(var1);
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("read " + this);
      }

   }

   public final void write(IIOPOutputStream var1) {
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("writing " + this);
      }

      var1.write_short(this.requires);
      if (this.transportMech != null && (this.foreign || this.requiresSSL || var1.getServerChannel() == null || var1.getServerChannel().supportsTLS())) {
         this.transportMech.write(var1);
      } else {
         if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
            p("no TLS");
         }

         var1.write_long(34);
         var1.write_long(0);
      }

      this.asContextMech.write(var1);
      this.sasContextMech.write(var1);
   }

   public String toString() {
      return "CompoundSecMech (requires = " + this.requires + "\n  tranport = " + this.transportMech + "\n  ASContext = " + this.asContextMech + "\n  SASContext = " + this.sasContextMech + ")";
   }

   protected static void p(String var0) {
      IIOPLogger.logDebugSecurity("<CompoundSecMech>: " + var0);
   }
}
