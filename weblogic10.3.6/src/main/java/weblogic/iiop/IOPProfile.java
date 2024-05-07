package weblogic.iiop;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import weblogic.corba.cos.transactions.TransactionPolicyComponent;
import weblogic.corba.idl.poa.PolicyImpl;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.iiop.csi.CompoundSecMechList;
import weblogic.kernel.Kernel;
import weblogic.kernel.KernelStatus;
import weblogic.rmi.internal.RuntimeDescriptor;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.collections.NumericKeyHashMap;

public final class IOPProfile extends Profile {
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");
   public static final int PORT_DISABLED = 0;
   private byte major;
   private byte minor;
   private String host;
   private InetAddress canonicalHost;
   private transient ConnectionKey address;
   private int port;
   private boolean readSecurely;
   private boolean clusterable;
   int securePort;
   private ObjectKey key;
   private TargetAddress targetAddress;
   private TaggedComponent[] taggedComponents;
   private int ncomps;
   private static final int MAX_COMPONENTS = 16;

   public IOPProfile(String var1, int var2, ObjectKey var3) {
      this(var1, var2, var3, (String)null);
   }

   public IOPProfile(String var1, int var2, ObjectKey var3, String var4) {
      this(var1, var2, var3, var4, (byte)1, IIOPClientService.defaultGIOPMinorVersion, (ClusterComponent)null, (RuntimeDescriptor)null);
   }

   IOPProfile(String var1, int var2, ObjectKey var3, String var4, byte var5, byte var6, ClusterComponent var7, RuntimeDescriptor var8) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, (NumericKeyHashMap)null);
   }

   IOPProfile(String var1, int var2, ObjectKey var3, String var4, byte var5, byte var6, ClusterComponent var7, RuntimeDescriptor var8, NumericKeyHashMap var9) {
      super(0);
      this.address = null;
      this.readSecurely = false;
      this.clusterable = false;
      this.securePort = -1;
      this.host = var1;
      this.port = var2;
      this.key = var3;
      this.targetAddress = new TargetAddress(var3);
      this.major = var5;
      this.minor = var6;
      this.ncomps = 0;
      if (var5 >= 1 && var6 >= 1) {
         this.taggedComponents = new TaggedComponent[16];
         this.taggedComponents[this.ncomps++] = CodeSetsComponent.getDefault();
         if (IIOPClientService.useSerialFormatVersion2) {
            this.taggedComponents[this.ncomps++] = SFVComponent.VERSION_2;
         }

         if (Kernel.isServer() && var3.isWLSKey()) {
            this.taggedComponents[this.ncomps++] = new CodebaseComponent(var3.getTarget(), var4);
            if (var8 != null && var8.getMethodsAreTransactional()) {
               this.taggedComponents[this.ncomps++] = TransactionPolicyComponent.EJB_OTS_POLICY;
            } else if (var9 != null && var9.get(55L) != null) {
               this.taggedComponents[this.ncomps++] = TransactionPolicyComponent.getInvocationPolicy(((PolicyImpl)var9.get(55L)).policy_value());
            }

            if (var9 == null) {
               this.taggedComponents[this.ncomps++] = TransactionPolicyComponent.EJB_INV_POLICY;
            } else if (var9.get(56L) != null) {
               this.taggedComponents[this.ncomps++] = TransactionPolicyComponent.getOTSPolicy(((PolicyImpl)var9.get(56L)).policy_value());
            }

            if (!KernelStatus.isServer() || SecurityServiceManager.isSecurityServiceInitialized()) {
               if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
                  p("create compound secmechlist for IOR: " + var4);
               }

               this.taggedComponents[this.ncomps++] = new CompoundSecMechList(var1, var3.getTarget(), var8);
            }

            if (var8 != null && var8.getIntegrity() != null && "required".equals(var8.getIntegrity())) {
               this.port = 0;
            }

            if (var3.isLocalKey()) {
               this.securePort = SSLSecTransComponent.getSingleton().getPort();
            }
         }

         if (var7 != null) {
            this.taggedComponents[this.ncomps++] = var7;
         }
      }

   }

   IOPProfile() {
      super(0);
      this.address = null;
      this.readSecurely = false;
      this.clusterable = false;
      this.securePort = -1;
   }

   IOPProfile(IOPProfile var1) {
      super(0);
      this.address = null;
      this.readSecurely = false;
      this.clusterable = false;
      this.securePort = -1;
      this.major = var1.major;
      this.minor = var1.minor;
      this.host = var1.host;
      this.port = var1.port;
      this.readSecurely = var1.readSecurely;
      this.securePort = var1.securePort;
      this.key = var1.key;
      this.targetAddress = var1.targetAddress;
      this.ncomps = var1.ncomps;
      this.taggedComponents = new TaggedComponent[var1.taggedComponents.length];

      for(int var2 = 0; var2 < this.taggedComponents.length; ++var2) {
         this.taggedComponents[var2] = var1.taggedComponents[var2];
      }

   }

   void setClusterComponent(ClusterComponent var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < this.ncomps; ++var3) {
         if (!(this.taggedComponents[var3] instanceof ClusterComponent)) {
            this.taggedComponents[var2++] = this.taggedComponents[var3];
         }
      }

      if (var1 != null) {
         if (var2 + 1 >= this.taggedComponents.length) {
            TaggedComponent[] var4 = new TaggedComponent[var2 + 1];
            System.arraycopy(this.taggedComponents, 0, var4, 0, var2);
            this.taggedComponents = var4;
         }

         this.taggedComponents[var2++] = var1;
      }

   }

   public final String getHost() {
      return this.host;
   }

   public final InetAddress getHostAddress() throws UnknownHostException {
      if (this.canonicalHost == null) {
         this.canonicalHost = InetAddress.getByName(this.host);
      }

      return this.canonicalHost;
   }

   final boolean isSecure() {
      return (this.readSecurely() || this.getPort() <= 0) && this.getSecurePort() > 0;
   }

   public final void makeSecure() {
      if (this.getSecurePort() > 0 && this.getPort() > 0) {
         this.port = 0;
      }

   }

   public final ConnectionKey getConnectionKey() {
      if (this.address == null) {
         try {
            if (this.isSecure()) {
               String var1 = this.getSecureHost() == null ? this.getHost() : this.getSecureHost();
               this.address = new ConnectionKey(InetAddress.getByName(var1).getHostAddress(), this.getSecurePort());
            } else {
               this.address = new ConnectionKey(this.getHostAddress().getHostAddress(), this.getPort());
            }
         } catch (UnknownHostException var3) {
            if (this.isSecure()) {
               String var2 = this.getSecureHost() == null ? this.getHost() : this.getSecureHost();
               this.address = new ConnectionKey(var2, this.getSecurePort());
            } else {
               this.address = new ConnectionKey(this.getHost(), this.getPort());
            }
         }
      }

      return this.address;
   }

   public final int getPort() {
      return this.port;
   }

   public final int getSecurePort() {
      if (this.securePort < 0) {
         TaggedComponent var1 = this.getComponent(33);
         if (var1 != null) {
            this.securePort = ((CompoundSecMechList)var1).getSecurePort();
         }

         if (this.securePort < 0) {
            SSLSecTransComponent var2 = (SSLSecTransComponent)this.getComponent(20);
            if (var2 != null) {
               this.securePort = var2.getPort();
            }
         }
      }

      return this.securePort;
   }

   public final String getSecureHost() {
      TaggedComponent var1 = this.getComponent(33);
      return var1 != null ? ((CompoundSecMechList)var1).getSecureHost() : null;
   }

   public final boolean readSecurely() {
      return this.readSecurely;
   }

   public final byte getMinorVersion() {
      return this.minor;
   }

   public final byte getMajorVersion() {
      return this.major;
   }

   public final ObjectKey getObjectKey() {
      return this.key;
   }

   public final boolean isTransactional() {
      TransactionPolicyComponent var1 = (TransactionPolicyComponent)this.getComponent(31);
      return var1 != null && var1.getPolicy() != 2;
   }

   public final boolean isClusterable() {
      if (!this.clusterable && this.getComponent(1111834883) != null) {
         this.clusterable = true;
      }

      return this.clusterable;
   }

   public final TaggedComponent getComponent(int var1) {
      if (this.taggedComponents == null) {
         return null;
      } else {
         for(int var2 = 0; var2 < this.ncomps; ++var2) {
            if (this.taggedComponents[var2].tag == var1) {
               return this.taggedComponents[var2];
            }
         }

         return null;
      }
   }

   public final void removeComponent(int var1) {
      TaggedComponent var2 = this.getComponent(var1);
      if (var2 != null) {
         List var3 = Arrays.asList((Object[])this.taggedComponents);
         var3.remove(var2);
         this.taggedComponents = (TaggedComponent[])((TaggedComponent[])var3.toArray(this.taggedComponents));
         --this.ncomps;
      }

   }

   final TargetAddress getTargetAddress() {
      return this.targetAddress;
   }

   public final boolean useSAS() {
      TaggedComponent var1 = this.getComponent(33);
      return var1 != null ? ((CompoundSecMechList)var1).useSAS() : false;
   }

   public final byte[] getGSSUPTarget() {
      TaggedComponent var1 = this.getComponent(33);
      return var1 != null ? ((CompoundSecMechList)var1).getGSSUPTarget() : null;
   }

   public final boolean isGSSUPTargetStateful() {
      TaggedComponent var1 = this.getComponent(33);
      return var1 != null ? ((CompoundSecMechList)var1).isGSSUPTargetStateful() : false;
   }

   public byte getMaxStreamFormatVersion() {
      SFVComponent var1 = (SFVComponent)this.getComponent(38);
      return var1 != null ? var1.getMaxFormatVersion() : 1;
   }

   public void read(IIOPInputStream var1) {
      long var2 = var1.startEncapsulation();
      if (var1.isSecure()) {
         this.readSecurely = true;
      }

      this.major = var1.read_octet();
      this.minor = var1.read_octet();
      ConnectionKey var4 = new ConnectionKey(var1);
      this.key = new ObjectKey(var1);
      this.targetAddress = new TargetAddress(this.key);
      if (this.key.isLocalKey()) {
         var4 = var4.readResolve(var1);
      }

      this.host = var4.getAddress();
      this.port = var4.getPort();
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("read() preamble: " + this.major + "." + this.minor + " profile to " + this.host + ":" + this.port + " @" + var1.pos());
      }

      if (this.major >= 1 && this.minor >= 1) {
         this.ncomps = var1.read_long();
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            p("read() " + this.ncomps + " components @" + var1.pos());
         }

         this.taggedComponents = new TaggedComponent[this.ncomps];
         int var5 = 0;

         while(var5 < this.ncomps) {
            this.taggedComponents[var5] = TaggedComponent.readComponent(var1, this.key.getTarget());
            switch (this.taggedComponents[var5].getTag()) {
               case 1111834883:
                  this.clusterable = true;
               default:
                  ++var5;
            }
         }
      }

      var1.endEncapsulation(var2);
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("read " + this);
      }

   }

   public void write(IIOPOutputStream var1) {
      var1.write_long(0);
      long var2 = var1.startEncapsulation();
      var1.write_octet(this.major);
      var1.write_octet(this.minor);
      ConnectionKey var4 = new ConnectionKey(this.host, this.port);
      if (this.key.getTarget() != null) {
         var4.writeForChannel(var1, this.key.getTarget());
      } else {
         var4.write(var1);
      }

      this.key.write(var1);
      if (this.major >= 1 && this.minor >= 1) {
         if (var1.isSecure() && this.key.isLocalKey() && Kernel.isServer()) {
            var1.write_long(this.ncomps + 1);
         } else {
            var1.write_long(this.ncomps);
         }

         for(int var5 = 0; var5 < this.ncomps; ++var5) {
            this.taggedComponents[var5].write(var1);
         }

         if (var1.isSecure() && this.key.isLocalKey() && Kernel.isServer()) {
            SSLSecTransComponent.getSingleton().write(var1);
         }
      }

      var1.endEncapsulation(var2);
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("wrote " + this);
      }

   }

   public final int hashCode() {
      return this.port ^ this.host.hashCode() ^ this.key.hashCode();
   }

   public final boolean equals(Object var1) {
      try {
         IOPProfile var2 = (IOPProfile)var1;
         if (this.key.isLocalKey() && !Kernel.isServer() && this.key.equals(var2.key)) {
            return true;
         } else {
            return this.port == var2.port && (this.host == var2.host || this.host.hashCode() == var2.host.hashCode() || this.host.equals(var2.host)) && (this.key == var2.key || this.key.equals(var2.key));
         }
      } catch (ClassCastException var3) {
         return false;
      }
   }

   public String toString() {
      String var1 = "IOP Profile (ver = " + this.major + "." + this.minor + ", host = " + this.host + ",port = " + this.port + ",key = " + this.key + ", taggedComponents = ";
      if (this.taggedComponents != null) {
         for(int var2 = 0; var2 < this.taggedComponents.length; ++var2) {
            var1 = var1 + this.taggedComponents[var2];
         }
      }

      return var1 + ")";
   }

   private static void p(String var0) {
      System.err.println("<IOPProfile> " + var0);
   }
}
