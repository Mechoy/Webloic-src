package weblogic.iiop;

import java.security.AccessController;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.Kernel;
import weblogic.management.configuration.SSLMBean;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.protocol.ServerIdentity;
import weblogic.rmi.internal.RuntimeDescriptor;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class TLSSecTransComponent extends TaggedComponent {
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");
   private ConnectionKey[] addrs;
   private short supports;
   private short requires;
   private ServerIdentity target;
   public static final int TAG_SSL_SEC_TRANS = 36;
   public static final short IOPSEC_NOPROTECTION = 1;
   public static final short IOPSEC_INTEGRITY = 2;
   public static final short IOPSEC_CONFIDENTIALITY = 4;
   public static final short IOPSEC_DETECTREPLAY = 8;
   public static final short IOPSEC_DETECTMISORDERING = 16;
   public static final short IOPSEC_ESTABLISHTRUSTINTARGET = 32;
   public static final short IOPSEC_ESTABLISHTRUSTINCLIENT = 64;
   public static final short IOPSEC_NODELEGATION = 128;
   public static final short IOPSEC_SIMPLEDELEGATION = 256;
   public static final short IOPSEC_COMPOSITEDELEGATION = 512;
   public static final short IOPSEC_IDENTITYASSERTION = 1024;
   public static final short IOPSEC_DELEGATIONBYCLIENT = 2048;
   public static final short IOP_SIGNED_FLAGS = 2;
   public static final short IOP_SEALED_FLAGS = 6;

   public TLSSecTransComponent(String var1, ServerIdentity var2, RuntimeDescriptor var3) {
      super(36);
      AuthenticatedSubject var4 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      SSLMBean var5 = ManagementService.getRuntimeAccess(var4).getServer().getSSL();
      ServerChannel var6 = ServerChannelManager.findLocalServerChannel(ProtocolHandlerIIOPS.PROTOCOL_IIOPS);
      if (var1 == null) {
         this.addrs = new ConnectionKey[]{new ConnectionKey(var6.getPublicAddress(), var6.getPublicPort())};
      } else {
         this.addrs = new ConnectionKey[]{new ConnectionKey(var1, var5.getListenPort())};
      }

      this.target = var2;
      boolean var7 = true;
      boolean var8 = false;
      String[] var9 = var5.getCiphersuites();
      if (var9 != null && var9.length > 0) {
         var7 = false;

         for(int var10 = 0; var10 < var9.length; ++var10) {
            if (var9[var10] != null) {
               if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
                  p("cipher suite " + var9[var10]);
               }

               if (var9[var10].indexOf("WITH_NULL") != -1) {
                  var8 = true;
               } else {
                  var7 = true;
               }
            }
         }
      }

      this.supports = 38;
      this.requires = 2;
      if (!var7 && var9 != null) {
         this.supports = 2;
      }

      if (!var8 && var7) {
         this.requires = 6;
      }

      if (var5.isClientCertificateEnforced()) {
         this.supports = (short)(this.supports | 64);
         this.requires = (short)(this.requires | 64);
      }

      if (var3 != null) {
         String var12 = var3.getClientCertAuthentication();
         if (var12 != null) {
            if (var12.equals("supported")) {
               this.supports = (short)(this.supports | 64);
            } else if (var12.equals("required")) {
               this.supports = (short)(this.supports | 64);
               this.requires = (short)(this.requires | 64);
            }
         }

         String var11 = var3.getConfidentiality();
         if (var11 != null && var11.equals("required") && var8) {
            this.requires = (short)(this.requires | 6);
         }
      }

      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("TLS sec supports = " + this.supports + " requires = " + this.requires);
      }

   }

   public TLSSecTransComponent(IIOPInputStream var1, ServerIdentity var2) {
      super(36);
      this.target = var2;
      this.read(var1);
   }

   public final ConnectionKey[] getAddresses() {
      return this.addrs;
   }

   public final short getSupports() {
      return this.supports;
   }

   public final short getRequires() {
      return this.requires;
   }

   public final void read(IIOPInputStream var1) {
      long var2 = var1.startEncapsulation();
      this.supports = var1.read_short();
      this.requires = var1.read_short();
      int var4 = var1.read_long();
      this.addrs = new ConnectionKey[var4];

      for(int var5 = 0; var5 < var4; ++var5) {
         this.addrs[var5] = new ConnectionKey(var1);
      }

      var1.endEncapsulation(var2);
   }

   public final void write(IIOPOutputStream var1) {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("write(" + this.toString() + ")");
      }

      var1.write_long(this.tag);
      long var2 = var1.startEncapsulation();
      var1.write_short(this.supports);
      var1.write_short(this.requires);
      int var4 = this.addrs != null ? this.addrs.length : 0;
      var1.write_long(var4);

      for(int var5 = 0; var5 < var4; ++var5) {
         if (var1.isSecure() && this.target != null) {
            this.addrs[var5].writeForChannel(var1, this.target);
         } else {
            this.addrs[var5].write(var1);
         }
      }

      var1.endEncapsulation(var2);
   }

   public String toString() {
      String var1 = "TLSSecTrans (supports = " + this.supports + ",requires = " + this.requires;
      if (this.addrs != null) {
         var1 = var1 + " addresses{ ";

         for(int var2 = 0; var2 < this.addrs.length; ++var2) {
            var1 = var1 + " " + this.addrs[var2].getAddress() + ":" + this.addrs[var2].getPort();
         }

         var1 = var1 + "} ";
      }

      return var1;
   }
}
