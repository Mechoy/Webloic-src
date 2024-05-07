package weblogic.iiop.csi;

import java.security.AccessController;
import weblogic.corba.cos.security.GSSUtil;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.iiop.IIOPInputStream;
import weblogic.iiop.IIOPLogger;
import weblogic.iiop.IIOPOutputStream;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public class SASContextSec {
   private static final DebugLogger debugIIOPSecurity = DebugLogger.getDebugLogger("DebugIIOPSecurity");
   private static final DebugCategory debugSecurity = Debug.getCategory("weblogic.iiop.security");
   private static final int SupportedTypes = 15;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private short supports = 0;
   private short requires = 0;
   private int supportedIdentityTypes = 0;
   private int numAuthorities = 0;
   private int numOIDs = 0;
   private byte[][] oidSequences;

   public SASContextSec(boolean var1, boolean var2) {
      this.supportedIdentityTypes = this.getSupportedTypes();
      this.supports = 0;
      if (var1 && this.supportedIdentityTypes != 0) {
         this.supports = 1024;
      }

      this.requires = 0;
      if (var2 && this.supportedIdentityTypes != 0) {
         this.requires = 1024;
      }

      this.numOIDs = 1;
      this.oidSequences = new byte[this.numOIDs][];
      this.oidSequences[0] = GSSUtil.getGSSUPMech();
   }

   SASContextSec(IIOPInputStream var1) {
      this.read(var1);
   }

   public final void read(IIOPInputStream var1) {
      this.supports = var1.read_short();
      this.requires = var1.read_short();
      this.numAuthorities = var1.read_long();

      int var2;
      for(var2 = 0; var2 < this.numAuthorities; ++var2) {
         var1.read_long();
         var1.read_octet_sequence();
      }

      this.numOIDs = var1.read_long();
      if (this.numOIDs > 0) {
         this.oidSequences = new byte[this.numOIDs][];
      }

      for(var2 = 0; var2 < this.numOIDs; ++var2) {
         this.oidSequences[var2] = var1.read_octet_sequence();
      }

      this.supportedIdentityTypes = var1.read_long();
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
      var1.write_long(0);
      if (this.supports != 0) {
         var1.write_long(this.numOIDs);

         for(int var2 = 0; var2 < this.numOIDs; ++var2) {
            var1.write_octet_sequence(this.oidSequences[var2]);
         }

         var1.write_long(this.supportedIdentityTypes);
      } else {
         var1.write_long(0);
         var1.write_long(0);
      }

   }

   public short getSupports() {
      return this.supports;
   }

   public short getRequires() {
      return this.requires;
   }

   public final boolean hasGSSUPIdentity() {
      if ((this.supports & 1024) == 0) {
         return false;
      } else if ((this.supportedIdentityTypes & 15) == 0) {
         return false;
      } else {
         for(int var1 = 0; var1 < this.numOIDs; ++var1) {
            if (GSSUtil.isGSSUPMech(this.oidSequences[var1])) {
               return true;
            }
         }

         return false;
      }
   }

   private int getSupportedTypes() {
      int var1 = 0;
      String var2 = "weblogicDEFAULT";
      PrincipalAuthenticator var3 = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(kernelId, var2, ServiceType.AUTHENTICATION);
      if (var3.isTokenTypeSupported("CSI.ITTAnonymous")) {
         var1 |= 1;
      }

      if (var3.isTokenTypeSupported("CSI.PrincipalName")) {
         var1 |= 2;
      }

      if (var3.isTokenTypeSupported("CSI.X509CertChain")) {
         var1 |= 4;
      }

      if (var3.isTokenTypeSupported("CSI.DistinguishedName")) {
         var1 |= 8;
      }

      return var1;
   }

   public String toString() {
      return "SASContextSec (supports = " + this.supports + ",requires = " + this.requires + ")";
   }

   private static void p(String var0) {
      IIOPLogger.logDebugSecurity("<SASContextSec>: " + var0);
   }
}
