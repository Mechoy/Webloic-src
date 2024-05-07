package weblogic.iiop;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.SystemException;
import weblogic.corba.utils.RepositoryId;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.Kernel;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.ServerChannelManager;
import weblogic.rmi.internal.RuntimeDescriptor;
import weblogic.utils.AssertionError;
import weblogic.utils.collections.NumericKeyHashMap;
import weblogic.utils.io.Chunk;

public final class IOR implements Externalizable {
   private static final long serialVersionUID = 1952182103399381650L;
   static String localHost;
   static int localPort = -1;
   public static final IOR NULL = new IOR();
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");
   protected boolean iorIsLocal;
   protected boolean iorIsForeign;
   private RepositoryId typeId;
   private String applicationName;
   private Profile[] profiles;
   private IOPProfile iopProfile;

   public IOR() {
      this.iorIsForeign = false;
      this.typeId = RepositoryId.EMPTY;
      this.profiles = new Profile[0];
   }

   IOR(IIOPInputStream var1) {
      this.iorIsForeign = false;
      this.read(var1, false);
   }

   IOR(IIOPInputStream var1, boolean var2) {
      this.iorIsForeign = false;
      this.read(var1, true);
   }

   public IOR(String var1, String var2, int var3, ObjectKey var4) {
      this(var1, var2, var3, var4, (String)null);
   }

   public IOR(String var1, String var2, int var3, ObjectKey var4, byte var5, byte var6) {
      this(var1, var2, var3, var4, var5, var6, (String)null, (ClusterComponent)null, (RuntimeDescriptor)null);
   }

   public IOR(String var1, String var2, int var3, ObjectKey var4, byte var5, byte var6, int var7) {
      this(var1, var2, var3, var4, var5, var6, (String)null, (ClusterComponent)null, (RuntimeDescriptor)null);
      this.iopProfile.securePort = var7;
   }

   public IOR(String var1, String var2, int var3, ObjectKey var4, String var5) {
      this(var1, var2, var3, var4, (byte)1, IIOPClientService.defaultGIOPMinorVersion, var5, (ClusterComponent)null, (RuntimeDescriptor)null);
   }

   public IOR(String var1, ObjectKey var2) throws SystemException {
      this(var1, getLocalHost(), getLocalPort(), var2);
   }

   public IOR(String var1, int var2) {
      this(var1, ServerChannelManager.findLocalServerAddress(ProtocolHandlerIIOP.PROTOCOL_IIOP), ServerChannelManager.findLocalServerPort(ProtocolHandlerIIOP.PROTOCOL_IIOP), new ObjectKey(var1, var2, LocalServerIdentity.getIdentity()), (byte)1, IIOPClientService.defaultGIOPMinorVersion);
   }

   private IOR(String var1, String var2, IOPProfile var3) {
      this.iorIsForeign = false;
      if (var1 == null) {
         this.typeId = RepositoryId.OBJECT;
      } else {
         this.typeId = new RepositoryId(var1);
      }

      this.applicationName = var2;
      this.iopProfile = var3;
      this.profiles = new Profile[]{this.iopProfile};
   }

   IOR(String var1, String var2, int var3, ObjectKey var4, byte var5, byte var6, String var7, ClusterComponent var8, RuntimeDescriptor var9) {
      this(var1, var7, new IOPProfile(var2, var3, var4, var7, var5, var6, var8, var9));
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("building ior for typeId = " + var1 + ", and host = " + var2 + ", and port = " + var3);
      }

   }

   public IOR(String var1, String var2, int var3, ObjectKey var4, String var5, ClusterComponent var6, RuntimeDescriptor var7) {
      this(var1, var2, var3, var4, (byte)1, IIOPClientService.defaultGIOPMinorVersion, var5, var6, var7);
   }

   public IOR(String var1, String var2, int var3, ObjectKey var4, String var5, ClusterComponent var6, NumericKeyHashMap var7) {
      this(var1, var5, new IOPProfile(var2, var3, var4, var5, (byte)1, IIOPClientService.defaultGIOPMinorVersion, var6, (RuntimeDescriptor)null, var7));
   }

   IOR(IOR var1, ClusterComponent var2) {
      this.iorIsForeign = false;
      this.typeId = var1.typeId;
      this.applicationName = var1.applicationName;
      this.profiles = new Profile[1];
      this.iopProfile = new IOPProfile(var1.iopProfile);
      this.iopProfile.setClusterComponent(var2);
      this.profiles[0] = this.iopProfile;
   }

   public final boolean isRemote() {
      return this.iorIsForeign;
   }

   public String toString() {
      return this.getClass().getName().toString() + "[" + this.typeId.toString() + "] @" + this.iopProfile.getHost() + ":" + this.iopProfile.getPort() + ", <" + this.iopProfile.getObjectKey().getObjectID() + ", " + this.iopProfile.getObjectKey().getActivationID() + ">";
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      byte[] var2 = this.getEncapsulation();
      var1.writeInt(var2.length);
      var1.write(var2);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      byte[] var3 = new byte[var2];
      var1.readFully(var3);
      IIOPInputStream var4 = new IIOPInputStream(var3);
      var4.consumeEndian();
      this.read(var4, false);
      var4.close();
   }

   public final String stringify() {
      byte[] var1 = this.getEncapsulation();
      char[] var2 = new char[4 + var1.length * 2];
      var2[0] = 'I';
      var2[1] = 'O';
      var2[2] = 'R';
      var2[3] = ':';

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[4 + var3 * 2] = this.toHexChar(var1[var3] >> 4 & 15);
         var2[4 + var3 * 2 + 1] = this.toHexChar(var1[var3] & 15);
      }

      return new String(var2);
   }

   public static IOR destringify(String var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("Null not a valid argument");
      } else if (var0.startsWith("IOR:") && (var0.length() & 1) != 1) {
         Chunk var1 = Chunk.getChunk();
         Chunk var2 = var1;
         int var3 = 0;

         for(int var4 = 4; var4 < var0.length(); ++var3) {
            var2.buf[var3] = (byte)(fromHexChar(var0.charAt(var4)) << 4 & 240);
            byte[] var10000 = var2.buf;
            var10000[var3] |= (byte)(fromHexChar(var0.charAt(var4 + 1)) << 0 & 15);
            if (var3 == Chunk.CHUNK_SIZE) {
               var2.next = Chunk.getChunk();
               var2.end = Chunk.CHUNK_SIZE;
               var2 = var2.next;
               var3 = 0;
            }

            var4 += 2;
         }

         var2.end = var3;
         IIOPInputStream var6 = new IIOPInputStream(var1, false, (EndPoint)null);
         var6.consumeEndian();
         IOR var5 = new IOR(var6);
         var6.close();
         return var5;
      } else {
         throw new IllegalArgumentException("String not valid");
      }
   }

   private final char toHexChar(int var1) {
      switch (var1) {
         case 0:
            return '0';
         case 1:
            return '1';
         case 2:
            return '2';
         case 3:
            return '3';
         case 4:
            return '4';
         case 5:
            return '5';
         case 6:
            return '6';
         case 7:
            return '7';
         case 8:
            return '8';
         case 9:
            return '9';
         case 10:
            return 'A';
         case 11:
            return 'B';
         case 12:
            return 'C';
         case 13:
            return 'D';
         case 14:
            return 'E';
         case 15:
            return 'F';
         default:
            throw new AssertionError("Unknown char: " + var1);
      }
   }

   private static int fromHexChar(char var0) {
      int var1 = var0 - 48;
      if (var1 >= 0 && var1 <= 9) {
         return var1;
      } else {
         var1 = var0 - 97 + 10;
         if (var1 >= 10 && var1 <= 15) {
            return var1;
         } else {
            var1 = var0 - 65 + 10;
            if (var1 >= 10 && var1 <= 15) {
               return var1;
            } else {
               throw new IllegalArgumentException("String not hex");
            }
         }
      }
   }

   private final byte[] getEncapsulation() {
      IIOPOutputStream var1 = new IIOPOutputStream();
      var1.putEndian();
      this.write(var1);
      byte[] var2 = var1.getBuffer();
      var1.close();
      return var2;
   }

   public final RepositoryId getTypeId() {
      return this.typeId;
   }

   public final String getCodebase() {
      CodebaseComponent var1 = (CodebaseComponent)this.getProfile().getComponent(25);
      return var1 != null ? var1.getCodebase() : null;
   }

   private static String getLocalHost() {
      if (localHost == null) {
         Class var0 = IOR.class;
         synchronized(IOR.class) {
            localHost = ServerChannelManager.findLocalServerAddress(ProtocolHandlerIIOP.PROTOCOL_IIOP);
         }
      }

      return localHost;
   }

   private static int getLocalPort() {
      if (localPort < 0) {
         Class var0 = IOR.class;
         synchronized(IOR.class) {
            localPort = ServerChannelManager.findLocalServerPort(ProtocolHandlerIIOP.PROTOCOL_IIOP);
         }
      }

      return localPort;
   }

   public final IOPProfile getProfile() {
      if (this.iopProfile == null) {
         throw new NO_IMPLEMENT(1330446339, CompletionStatus.COMPLETED_NO);
      } else {
         return this.iopProfile;
      }
   }

   public final ConnectionKey getConnectionKey() {
      return this.getProfile().getConnectionKey();
   }

   final boolean isSecure() {
      return this.getProfile().isSecure();
   }

   final boolean isNull() {
      return (this.typeId == null || this.typeId.length() == 0) && (this.profiles == null || this.profiles.length == 0);
   }

   public final boolean isLocal() {
      if (this.iopProfile != null) {
         if (this.iopProfile.getObjectKey().isLocalKey()) {
            return true;
         } else {
            ClusterComponent var1 = (ClusterComponent)this.iopProfile.getComponent(1111834883);
            return var1 != null && var1.findLocalIOR() != null;
         }
      } else {
         return false;
      }
   }

   private void read(IIOPInputStream var1, boolean var2) {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("read()");
      }

      this.typeId = var1.read_repository_id();
      if ((this.typeId == null || this.typeId.equals(RepositoryId.EMPTY)) && !var2) {
         var1.mark(0);
         if (var1.read_long() != 0) {
            var1.reset();
         }

      } else {
         int var3 = var1.read_long();
         this.profiles = new Profile[var3];

         for(int var4 = 0; var4 < var3; ++var4) {
            int var5 = var1.read_long();
            switch (var5) {
               case 0:
                  this.iopProfile = new IOPProfile();
                  this.iopProfile.read(var1);
                  this.profiles[var4] = this.iopProfile;
                  break;
               default:
                  this.profiles[var4] = new Profile(var4);
                  this.profiles[var4].read(var1);
            }
         }

         this.iorIsForeign = true;
      }
   }

   public void write(IIOPOutputStream var1) {
      var1.write_repository_id(this.typeId);
      if (this.profiles == null) {
         var1.write_long(0);
      } else {
         var1.write_long(this.profiles.length);

         for(int var2 = 0; var2 < this.profiles.length; ++var2) {
            this.profiles[var2].write(var1);
         }

      }
   }

   public final boolean readSecurely() {
      return this.getProfile().readSecurely();
   }

   public final byte[] getGSSUPTarget() {
      return this.getProfile().getGSSUPTarget();
   }

   public final boolean isGSSUPTargetStateful() {
      return this.getProfile().isGSSUPTargetStateful();
   }

   public final byte getMaxStreamFormatVersion() {
      return this.getProfile().getMaxStreamFormatVersion();
   }

   public final int hashCode() {
      return this.iopProfile == null ? this.typeId.hashCode() : this.typeId.hashCode() ^ this.iopProfile.hashCode();
   }

   public final boolean equals(Object var1) {
      if (!(var1 instanceof IOR)) {
         return false;
      } else {
         IOR var2 = (IOR)var1;
         return this.typeId.equals(var2.typeId) && (this.iopProfile == var2.iopProfile || (this.iopProfile == null || this.iopProfile.equals(var2.iopProfile)) && this.iopProfile != null);
      }
   }

   static void p(String var0) {
      System.err.println("<IOR> " + var0);
   }
}
