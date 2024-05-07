package weblogic.iiop;

import java.security.AccessController;
import weblogic.management.configuration.SSLMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class SSLSecTransComponent extends TaggedComponent {
   private int port;
   private short supports;
   private short requires;
   private static SSLSecTransComponent singleton;
   public static final int TAG_SSL_SEC_TRANS = 20;
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
   public static final short IOP_SIGNED_FLAGS = 314;
   public static final short IOP_SEALED_FLAGS = 318;

   public static SSLSecTransComponent getSingleton() {
      return singleton == null ? createSingleton() : singleton;
   }

   private static synchronized SSLSecTransComponent createSingleton() {
      if (singleton == null) {
         singleton = new SSLSecTransComponent();
      }

      return singleton;
   }

   private SSLSecTransComponent() {
      super(20);
      AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      SSLMBean var2 = ManagementService.getRuntimeAccess(var1).getServer().getSSL();
      this.port = var2.getListenPort();
      boolean var3 = false;
      boolean var4 = false;
      String[] var5 = var2.getCiphersuites();
      if (var5 != null && var5.length > 0) {
         for(int var6 = 0; var6 < var5.length; ++var6) {
            if (var5[var6] != null) {
               if (var5[var6].indexOf("WITH_NULL") != -1) {
                  var4 = true;
               } else {
                  var3 = true;
               }
            }
         }
      }

      this.supports = 318;
      this.requires = 314;
      if (!var3 && var5 != null) {
         this.supports = 314;
      }

      if (!var4 && var3) {
         this.requires = 318;
      }

      if (var2.isClientCertificateEnforced()) {
         this.supports = (short)(this.supports | 64);
         this.requires = (short)(this.requires | 64);
      }

   }

   public SSLSecTransComponent(IIOPInputStream var1) {
      super(20);
      this.read(var1);
   }

   public final int getPort() {
      return this.port;
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
      this.port = var1.read_unsigned_short();
      var1.endEncapsulation(var2);
   }

   public final void write(IIOPOutputStream var1) {
      var1.write_long(this.tag);
      long var2 = var1.startEncapsulation();
      var1.write_short(this.supports);
      var1.write_short(this.requires);
      var1.write_unsigned_short(this.port);
      var1.endEncapsulation(var2);
   }
}
