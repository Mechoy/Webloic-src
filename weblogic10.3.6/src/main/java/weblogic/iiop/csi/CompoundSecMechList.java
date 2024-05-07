package weblogic.iiop.csi;

import java.security.AccessController;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.iiop.IIOPInputStream;
import weblogic.iiop.IIOPLogger;
import weblogic.iiop.IIOPOutputStream;
import weblogic.iiop.TaggedComponent;
import weblogic.kernel.Kernel;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.ServerIdentity;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.rmi.internal.RuntimeDescriptor;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public class CompoundSecMechList extends TaggedComponent {
   private static final DebugLogger debugIIOPSecurity = DebugLogger.getDebugLogger("DebugIIOPSecurity");
   private static final DebugCategory debugSecurity = Debug.getCategory("weblogic.iiop.security");
   private boolean statefulContext = true;
   private boolean validDefaultIIOPUser = false;
   private int numSecMechs = 0;
   private CompoundSecMech[] secMechs;

   public CompoundSecMechList(String var1, ServerIdentity var2, RuntimeDescriptor var3) {
      super(33);
      boolean var4 = false;
      if (Kernel.isServer()) {
         AuthenticatedSubject var5 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         var4 = ChannelHelper.isSSLChannelEnabled(ManagementService.getRuntimeAccess(var5).getServer());
      }

      if (var4 && var3 != null && var3.getIntegrity() != null && "none".equals(var3.getIntegrity())) {
         var4 = false;
      }

      boolean var7 = true;
      if (var3 != null && var3.getIntegrity() != null && "required".equals(var3.getIntegrity())) {
         var7 = false;
      }

      if (var3 != null) {
         this.statefulContext = var3.getStatefulAuthentication();
      } else {
         this.statefulContext = Kernel.getConfig().getIIOP().getUseStatefulAuthentication();
      }

      this.numSecMechs = 0;
      if (var4) {
         ++this.numSecMechs;
      }

      if (var7) {
         ++this.numSecMechs;
      }

      this.secMechs = new CompoundSecMech[this.numSecMechs];
      int var6 = 0;
      if (var4) {
         this.secMechs[var6++] = new CompoundSecMech(true, var1, var2, var3);
      }

      if (var7) {
         this.secMechs[var6++] = new CompoundSecMech(false, var1, var2, var3);
      }

      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("created " + this);
      }

   }

   public CompoundSecMechList(IIOPInputStream var1, ServerIdentity var2) {
      super(33);
      this.read(var1, var2);
   }

   public final CompoundSecMech[] getCompoundSecMechs() {
      return this.secMechs;
   }

   public final boolean useSAS() {
      for(int var1 = 0; var1 < this.numSecMechs; ++var1) {
         if (this.secMechs[var1].useSAS()) {
            if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
               p("useSAS returns true");
            }

            return true;
         }
      }

      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("useSAS returns false " + this);
      }

      return false;
   }

   public final boolean hasGSSUP() {
      for(int var1 = 0; var1 < this.numSecMechs; ++var1) {
         if (this.secMechs[var1].hasGSSUP()) {
            if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
               p("hasGSSUP returns true");
            }

            return true;
         }
      }

      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("hasGSSUP returns false " + this);
      }

      return false;
   }

   public final boolean hasGSSUPIdentity() {
      for(int var1 = 0; var1 < this.numSecMechs; ++var1) {
         if (this.secMechs[var1].hasGSSUPIdentity()) {
            if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
               p("hasGSSUPIdentity returns true");
            }

            return true;
         }
      }

      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("hasGSSUPIdentity returns false " + this);
      }

      return false;
   }

   public final byte[] getGSSUPTarget() {
      for(int var1 = 0; var1 < this.numSecMechs; ++var1) {
         if (this.secMechs[var1].hasGSSUP()) {
            byte[] var2 = this.secMechs[var1].getGSSUPTarget();
            if (var2 != null) {
               if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
                  p("getGSSUPTarget returns " + var2);
               }

               return var2;
            }
         }
      }

      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("getGSSUPTarget returns null");
      }

      return null;
   }

   public final boolean isGSSUPTargetStateful() {
      return this.statefulContext;
   }

   public final String getSecureHost() {
      for(int var1 = 0; var1 < this.numSecMechs; ++var1) {
         String var2 = this.secMechs[var1].getSecureHost();
         if (var2 != null) {
            if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
               p("getSecureHost returns " + var2);
            }

            return var2;
         }
      }

      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("getSecureHost returns null");
      }

      return null;
   }

   public final int getSecurePort() {
      for(int var1 = 0; var1 < this.numSecMechs; ++var1) {
         int var2 = this.secMechs[var1].getSecurePort();
         if (var2 != -1) {
            if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
               p("getSecurePort returns " + var2);
            }

            return var2;
         }
      }

      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("getSecurePort returns -1");
      }

      return -1;
   }

   public final void read(IIOPInputStream var1, ServerIdentity var2) {
      long var3 = var1.startEncapsulation();
      this.statefulContext = var1.read_boolean();
      this.numSecMechs = var1.read_long();
      this.secMechs = new CompoundSecMech[this.numSecMechs];

      for(int var5 = 0; var5 < this.numSecMechs; ++var5) {
         this.secMechs[var5] = new CompoundSecMech(var1, var2);
      }

      var1.endEncapsulation(var3);
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("read  " + this);
      }

   }

   public final void write(IIOPOutputStream var1) {
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("writing " + this);
      }

      var1.write_long(this.tag);
      long var2 = var1.startEncapsulation();
      var1.write_boolean(this.statefulContext);
      var1.write_long(this.numSecMechs);

      for(int var4 = 0; var4 < this.numSecMechs; ++var4) {
         this.secMechs[var4].write(var1);
      }

      var1.endEncapsulation(var2);
   }

   public String toString() {
      String var1 = "CompoundSecMechList (stateful = " + this.statefulContext + ",numSecMechs = " + this.numSecMechs + ", secMechs = ";

      for(int var2 = 0; var2 < this.numSecMechs; ++var2) {
         var1 = var1 + this.secMechs[var2];
      }

      return var1 + ")";
   }

   protected static void p(String var0) {
      IIOPLogger.logDebugSecurity("<CompoundSecMechList>: " + var0);
   }
}
