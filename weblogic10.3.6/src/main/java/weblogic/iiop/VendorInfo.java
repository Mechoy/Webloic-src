package weblogic.iiop;

import java.io.Serializable;
import weblogic.common.internal.PackageInfo;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.VersionInfo;
import weblogic.corba.client.Version;
import weblogic.corba.j2ee.workarea.WorkAreaContext;
import weblogic.kernel.Kernel;

public final class VendorInfo extends ServiceContext implements Version {
   public static final int VendorInfo = 1111834880;
   public static final int VendorInfoTx = 1111834881;
   public static final int VendorInfoSecurity = 1111834882;
   public static final int VendorInfoCluster = 1111834883;
   public static final int VendorInfoBEA04 = 1111834884;
   public static final int VendorInfoBEA05 = 1111834885;
   public static final int VendorInfoBEA06 = 1111834886;
   public static final int VendorInfoBEA07 = 1111834887;
   public static final int VendorInfoRoutingContext = 1111834888;
   public static final int VendorInfoForwardingContext = 1111834889;
   public static final int VendorInfoTrace = 1111834890;
   public static final int VendorRuntimeContext = 1111834891;
   public static final int VendorInfoEnd = 1111834943;
   protected byte majorVersion = 10;
   protected byte minorVersion = 3;
   protected byte minorServicePack = 6;
   protected static PackageInfo pkgInfo;
   protected PeerInfo peerinfo;
   static final VendorInfo VENDOR_INFO;

   private VendorInfo() {
      super(1111834880);
      if (Kernel.isServer()) {
         this.majorVersion = (byte)pkgInfo.getMajor();
         this.minorVersion = (byte)pkgInfo.getMinor();
         this.minorServicePack = (byte)pkgInfo.getServicePack();
      }

   }

   public VendorInfo(byte var1, byte var2, byte var3) {
      super(1111834880);
      this.majorVersion = var1;
      this.minorVersion = var2;
      this.minorServicePack = var3;
   }

   protected VendorInfo(IIOPInputStream var1) {
      super(1111834880);
      this.readEncapsulatedContext(var1);
   }

   public int getMajorVersion() {
      return this.majorVersion;
   }

   public int getMinorVersion() {
      return this.minorVersion;
   }

   public int getServicePack() {
      return this.minorServicePack;
   }

   public PeerInfo getPeerInfo() {
      if (this.peerinfo == null) {
         this.peerinfo = new PeerInfo(this.majorVersion, this.minorVersion, this.minorServicePack, (Serializable)null);
      }

      return this.peerinfo;
   }

   static ServiceContext readServiceContext(int var0, IIOPInputStream var1) {
      switch (var0) {
         case 1111834880:
            return new VendorInfo(var1);
         case 1111834881:
            return new VendorInfoTx(var1);
         case 1111834882:
            return new VendorInfoSecurity(var1);
         case 1111834883:
            return new VendorInfoCluster(var1);
         case 1111834884:
         case 1111834885:
         case 1111834886:
         case 1111834887:
         default:
            return new ServiceContext(var0, var1);
         case 1111834888:
            return new RoutingContext(var1);
         case 1111834889:
            return new ForwardingContext(var1);
         case 1111834890:
            return new VendorInfoTrace(var1);
         case 1111834891:
            return new WorkAreaContext(var1);
      }
   }

   public void write(IIOPOutputStream var1) {
      this.writeEncapsulatedContext(var1);
   }

   protected void readEncapsulation(IIOPInputStream var1) {
      this.majorVersion = var1.read_octet();
      this.minorVersion = var1.read_octet();
      this.minorServicePack = var1.read_octet();
   }

   protected void writeEncapsulation(IIOPOutputStream var1) {
      var1.write_octet(this.majorVersion);
      var1.write_octet(this.minorVersion);
      var1.write_octet(this.minorServicePack);
   }

   public String toString() {
      return "VendorInfo Context for " + this.majorVersion + "." + this.minorVersion + "." + this.minorServicePack;
   }

   static {
      if (Kernel.isServer()) {
         pkgInfo = VersionInfo.theOne().getPackages()[0];
      }

      VENDOR_INFO = new VendorInfo();
   }
}
