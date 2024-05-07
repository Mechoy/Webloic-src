package weblogic.iiop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.HashMap;
import org.omg.CORBA.MARSHAL;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.CosTransactions.TransactionFactoryHelper;
import org.omg.SendingContext.CodeBaseHelper;
import weblogic.corba.utils.MarshaledString;
import weblogic.corba.utils.RepositoryId;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.Kernel;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.Identity;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.ServerIdentity;
import weblogic.protocol.ServerIdentityManager;
import weblogic.rmi.extensions.server.ActivatableServerReference;
import weblogic.rmi.internal.OIDManager;
import weblogic.rmi.internal.RemoteType;
import weblogic.rmi.internal.RuntimeDescriptor;
import weblogic.rmi.internal.ServerReference;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;
import weblogic.utils.Hex;
import weblogic.utils.StringUtils;

public class ObjectKey {
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");
   private int keyType;
   private int oid;
   private int keyLength;
   private transient int numComponents;
   private Object activationID;
   private IIOPInputStream activationData;
   private boolean foreignKey;
   private byte[] key_data;
   private ServerIdentity target;
   private Identity identity;
   private static final String BOOTSTRAP_NAME_SERVICE = "INIT";
   static final String NAME_SERVICE = "NameService";
   private static final String BEA_NAME_SERVICE = "BEA:NameService:Root";
   static final ObjectKey BOOTSTRAP_KEY = new ObjectKey("INIT");
   static final ObjectKey NAME_SERVICE_KEY = new ObjectKey("NameService");
   private byte[] preMarshalled;
   private static HashMap typeIdMap = new HashMap();
   private static final int ENCAPKEYHEADERSIZE = 8;
   private static final int KEYTYPE_ICEBERG = 8;
   private static final int TAG_OBJKEY_HASH = 1111834886;
   private static final int TAG_CLNT_ROUTE_INFO = 1111834884;
   private static final int TAG_WLS_INITIAL_REF = 1111834888;
   private static final int TAG_WLS_TRANSIENT_REF_51 = 1111834890;
   private static final int TAG_WLS_TRANSIENT_REF_61 = 1111834887;
   private static final int TAG_WLS_OBJ_INFO = 1111834891;
   private static final int TAG_WLS_ACTIVATABLE_REF = 1111834897;
   private static final int TAG_WLS_ACTIVATABLE_REF_81 = 1111834896;
   private static final int TAG_WLS_TRANSIENT_REF = 1111834922;
   private static final int KEY_MAJOR_VERSION = 1;
   private static final int KEY_MINOR_VERSION = 3;
   private static final int WLS_OA_ID = 0;
   private static final int MAX_KEY_SIZE = 1048576;
   private static MarshaledString localDomainId;
   private int wleVersionMajor;
   private int wleVersionMinor;
   private RepositoryId interfaceName;
   private MarshaledString wleDomainId;
   private int wleGroupId;
   private String wleObjectId;
   private int wleObjectAdapter;
   private int wleScaInterfaceBucket;
   private transient int numForeignComponents;
   private TaggedComponent[] foreignComponents;
   private String[] remoteInterfaces;
   private boolean isRepIdAnInterface;
   private transient boolean writeObjInfo;
   private transient boolean computeRepId;

   public ObjectKey() {
      this.keyType = 0;
      this.oid = 0;
      this.keyLength = 0;
      this.numComponents = 0;
      this.foreignKey = false;
      this.wleVersionMajor = 1;
      this.wleVersionMinor = 3;
      this.interfaceName = RepositoryId.EMPTY;
      this.wleGroupId = 0;
      this.wleObjectId = "";
      this.wleObjectAdapter = 0;
      this.wleScaInterfaceBucket = -1;
      this.numForeignComponents = 0;
      this.remoteInterfaces = null;
      this.isRepIdAnInterface = true;
      this.writeObjInfo = false;
      this.computeRepId = true;
   }

   ObjectKey(IIOPInputStream var1) {
      this.keyType = 0;
      this.oid = 0;
      this.keyLength = 0;
      this.numComponents = 0;
      this.foreignKey = false;
      this.wleVersionMajor = 1;
      this.wleVersionMinor = 3;
      this.interfaceName = RepositoryId.EMPTY;
      this.wleGroupId = 0;
      this.wleObjectId = "";
      this.wleObjectAdapter = 0;
      this.wleScaInterfaceBucket = -1;
      this.numForeignComponents = 0;
      this.remoteInterfaces = null;
      this.isRepIdAnInterface = true;
      this.writeObjInfo = false;
      this.computeRepId = true;
      this.read(var1);
   }

   ObjectKey(byte[] var1) {
      this.keyType = 0;
      this.oid = 0;
      this.keyLength = 0;
      this.numComponents = 0;
      this.foreignKey = false;
      this.wleVersionMajor = 1;
      this.wleVersionMinor = 3;
      this.interfaceName = RepositoryId.EMPTY;
      this.wleGroupId = 0;
      this.wleObjectId = "";
      this.wleObjectAdapter = 0;
      this.wleScaInterfaceBucket = -1;
      this.numForeignComponents = 0;
      this.remoteInterfaces = null;
      this.isRepIdAnInterface = true;
      this.writeObjInfo = false;
      this.computeRepId = true;
      this.key_data = var1;
      this.foreignKey = true;
      this.keyLength = this.key_data.length;
   }

   private ObjectKey(String var1) {
      this.keyType = 0;
      this.oid = 0;
      this.keyLength = 0;
      this.numComponents = 0;
      this.foreignKey = false;
      this.wleVersionMajor = 1;
      this.wleVersionMinor = 3;
      this.interfaceName = RepositoryId.EMPTY;
      this.wleGroupId = 0;
      this.wleObjectId = "";
      this.wleObjectAdapter = 0;
      this.wleScaInterfaceBucket = -1;
      this.numForeignComponents = 0;
      this.remoteInterfaces = null;
      this.isRepIdAnInterface = true;
      this.writeObjInfo = false;
      this.computeRepId = true;
      this.keyLength = var1.length();
      this.key_data = new byte[this.keyLength];
      var1.getBytes(0, this.keyLength, this.key_data, 0);
      this.foreignKey = true;
   }

   public ObjectKey(String var1, int var2, ServerIdentity var3) {
      this.keyType = 0;
      this.oid = 0;
      this.keyLength = 0;
      this.numComponents = 0;
      this.foreignKey = false;
      this.wleVersionMajor = 1;
      this.wleVersionMinor = 3;
      this.interfaceName = RepositoryId.EMPTY;
      this.wleGroupId = 0;
      this.wleObjectId = "";
      this.wleObjectAdapter = 0;
      this.wleScaInterfaceBucket = -1;
      this.numForeignComponents = 0;
      this.remoteInterfaces = null;
      this.isRepIdAnInterface = true;
      this.writeObjInfo = false;
      this.computeRepId = true;
      this.interfaceName = new RepositoryId(var1);
      this.oid = var2;
      this.keyType = 1111834922;
      this.target = var3;
      this.identity = var3.getTransientIdentity();
      if (var3.getServerName() == null) {
         this.wleDomainId = MarshaledString.EMPTY;
      } else if (var3.isLocal()) {
         this.wleDomainId = localDomainId;
      } else {
         this.wleDomainId = new MarshaledString(var3.getServerName());
      }

      this.wleGroupId = 0;
      this.numComponents = 1;
      this.checkObjInfoRequiredToBeMarshalled();
   }

   public ObjectKey(String var1, int var2) {
      this(var1, var2, LocalServerIdentity.getIdentity());
   }

   public ObjectKey(String var1, int var2, Object var3) {
      this.keyType = 0;
      this.oid = 0;
      this.keyLength = 0;
      this.numComponents = 0;
      this.foreignKey = false;
      this.wleVersionMajor = 1;
      this.wleVersionMinor = 3;
      this.interfaceName = RepositoryId.EMPTY;
      this.wleGroupId = 0;
      this.wleObjectId = "";
      this.wleObjectAdapter = 0;
      this.wleScaInterfaceBucket = -1;
      this.numForeignComponents = 0;
      this.remoteInterfaces = null;
      this.isRepIdAnInterface = true;
      this.writeObjInfo = false;
      this.computeRepId = true;
      this.interfaceName = new RepositoryId(var1);
      this.oid = var2;
      this.activationID = var3;
      Debug.assertion(var3 != null);
      this.keyType = 1111834897;
      this.target = LocalServerIdentity.getIdentity();
      this.identity = this.target.getPersistentIdentity();
      this.wleDomainId = localDomainId;
      this.wleGroupId = 0;
      this.numComponents = 1;
      this.checkObjInfoRequiredToBeMarshalled();
   }

   public static final MarshaledString getLocalDomainID() {
      return localDomainId;
   }

   private static final MarshaledString getLocalDomainIDInternal() {
      if (Kernel.isServer()) {
         AuthenticatedSubject var0 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         return new MarshaledString(ManagementService.getRuntimeAccess(var0).getServerName());
      } else {
         return MarshaledString.EMPTY;
      }
   }

   static void p(String var0) {
      System.err.println("<ObjectKey> " + var0);
   }

   protected final String getInterfaceName() {
      return this.interfaceName.toString();
   }

   protected final void setInterfaceName(String var1) {
      this.preMarshalled = null;
      this.interfaceName = new RepositoryId(var1);
   }

   public final int getObjectID() {
      return this.oid;
   }

   final MarshaledString getWLEDomainId() {
      return this.wleDomainId;
   }

   protected final void setWLEDomainId(String var1) {
      this.preMarshalled = null;
      this.wleDomainId = new MarshaledString(var1);
   }

   protected final void setWLEObjectId(String var1) {
      this.preMarshalled = null;
      this.wleObjectId = var1;
   }

   protected final void setWLEObjectAdapter(int var1) {
      this.preMarshalled = null;
      this.wleObjectAdapter = var1;
   }

   final boolean isWLEKey() {
      return this.target == null && !this.foreignKey && this.wleObjectAdapter >= 0;
   }

   final boolean isWLSKey() {
      return this.identity != null && this.target != null;
   }

   private final boolean isForeignKey() {
      return this.foreignKey || this.identity == null && this.wleObjectAdapter == 0;
   }

   final boolean isBEAKey() {
      return !this.foreignKey;
   }

   final boolean isBootstrapKey() {
      return this.foreignKey && this.key_data != null && this.key_data.length == 4 && (this.key_data[0] == 73 && this.key_data[1] == 78 && this.key_data[2] == 73 && this.key_data[3] == 84 || this.key_data[0] == 84 && this.key_data[1] == 73 && this.key_data[2] == 78 && this.key_data[3] == 73);
   }

   public final boolean isNamingKey() {
      return this.key_data != null && this.key_data.length < 128 && (new String(this.key_data)).equals("NameService") || this.oid == 8;
   }

   final IOR getInitialReference() {
      return this.key_data != null && this.key_data.length < 128 ? InitialReferences.getInitialReference(new String(this.key_data)) : null;
   }

   final boolean isLocalKey() {
      return this.isWLSKey() && this.target != null && this.target.isLocal();
   }

   public final Object getActivationID() {
      if (this.activationID == null && this.activationData != null) {
         synchronized(this.activationData) {
            if (this.activationData != null) {
               this.activationID = this.activationData.read_value();
               this.activationData.close();
               this.activationData = null;
            }
         }
      }

      return this.activationID;
   }

   final ServerIdentity getTarget() {
      return this.target;
   }

   public final Identity getIdentity() {
      return this.identity;
   }

   public static String getTypeId(Object var0) {
      String[] var1 = (String[])((String[])typeIdMap.get(var0));
      return var1 != null ? var1[0] : null;
   }

   protected final void setKeyType(int var1) {
      this.preMarshalled = null;
      this.keyType = var1;
   }

   public static ObjectKey getBootstrapKey(String var0) {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("getBootstrapKey(" + var0 + ")");
      }

      String[] var1 = (String[])((String[])typeIdMap.get(var0));
      byte[] var2;
      if (var1 != null) {
         var2 = new byte[var1[1].length()];
         var1[1].getBytes(0, var1[1].length(), var2, 0);
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            p("getBootstrapKey(" + var0 + ") = " + var1[1]);
         }

         return new ObjectKey(var2);
      } else {
         var2 = new byte[var0.length()];
         var0.getBytes(0, var0.length(), var2, 0);
         return new ObjectKey(var2);
      }
   }

   Identity getTransientIdentity() {
      return this.identity;
   }

   private final boolean readVendor(IIOPInputStream var1) {
      char var2 = (char)var1.read_octet();
      char var3 = (char)var1.read_octet();
      char var4 = (char)var1.read_octet();
      return var2 == 'B' && var3 == 'E' && var4 == 'A';
   }

   private final void writeVendor(IIOPOutputStream var1) {
      byte[] var2 = new byte[]{66, 69, 65};
      var1.write_octet_array(var2, 0, var2.length);
   }

   final void read(IIOPInputStream var1) {
      this.preMarshalled = null;
      boolean var2 = var1.isReadingObjectKey();
      var1.mark(0);
      int var3 = var1.read_long();
      if (var3 > 1048576) {
         throw new MARSHAL("Stream corrupted at " + var1.pos() + ": tried to read object key of length " + Integer.toHexString(var3));
      } else {
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            p("read(" + Integer.toHexString(var3) + "@" + var1.pos() + ")");
         }

         var1.reset();
         this.foreignKey = false;
         long var4 = var1.startEncapsulation(false);
         var1.mark(0);
         var1.consumeEndian();
         if (var3 < 8) {
            this.readForeignKey(var1, var3, var4);
         } else if (!this.readVendor(var1)) {
            this.readForeignKey(var1, var3, var4);
         } else if (var1.read_octet() != 8) {
            this.readForeignKey(var1, var3, var4);
         } else if ((this.wleVersionMajor = var1.read_octet()) > 1) {
            this.readForeignKey(var1, var3, var4);
         } else {
            var1.setReadingObjectKey(true);
            var1.clearMark();
            this.wleVersionMinor = var1.read_octet();
            this.wleObjectAdapter = var1.read_octet();
            this.wleDomainId = new MarshaledString(var1);
            this.wleGroupId = var1.read_long();
            this.interfaceName = var1.read_repository_id();

            try {
               this.oid = var1.read_numeric_string();
            } catch (NumberFormatException var22) {
               this.oid = 0;
               this.wleObjectId = var22.getMessage();
               String[] var7 = (String[])((String[])typeIdMap.get(this.wleObjectId));
               if (var7 != null && var7[1].equals("NameService")) {
                  this.oid = 8;
               } else {
                  this.oid = 0;
               }
            }

            if (this.wleVersionMinor > 1 && this.wleVersionMajor == 1) {
               this.numComponents = var1.read_long();

               for(int var6 = 0; var6 < this.numComponents; ++var6) {
                  int var23 = var1.read_long();
                  long var8;
                  switch (var23) {
                     case 1111834884:
                     case 1111834886:
                     default:
                        if (this.foreignComponents == null) {
                           this.foreignComponents = new TaggedComponent[this.numComponents];
                        }

                        this.foreignComponents[this.numForeignComponents++] = new TaggedComponent(var23, var1);
                        break;
                     case 1111834888:
                        this.keyType = 1111834888;
                        var1.read_long();
                        this.target = LocalServerIdentity.getIdentity();
                        break;
                     case 1111834891:
                        var8 = var1.startEncapsulation();
                        ByteArrayInputStream var10 = null;
                        DataInputStream var11 = null;

                        try {
                           byte[] var12 = var1.read_octet_sequence();
                           var10 = new ByteArrayInputStream(var12);
                           var11 = new DataInputStream(var10);
                           this.isRepIdAnInterface = var11.readBoolean();
                           String var13 = var11.readUTF();
                           if (var13 != null && var13.length() > 0) {
                              this.remoteInterfaces = StringUtils.splitCompletely(var13, ":");
                           }
                        } catch (Exception var20) {
                        } finally {
                           if (var11 != null) {
                              this.close(var11);
                           }

                        }

                        var1.endEncapsulation(var8);
                        this.writeObjInfo = true;
                        this.computeRepId = false;
                        break;
                     case 1111834897:
                        this.keyType = 1111834897;
                        this.activationData = new IIOPInputStream(var1);
                        this.identity = Identity.read(this.activationData);
                        this.target = ServerIdentityManager.findServerIdentityFromPersistent(this.identity);
                        break;
                     case 1111834922:
                        this.keyType = 1111834922;
                        var8 = var1.startEncapsulation();
                        this.identity = Identity.read(var1);
                        this.target = ServerIdentityManager.findServerIdentityFromTransient(this.identity);
                        var1.endEncapsulation(var8);
                  }
               }
            }

            var1.endEncapsulation(var4);
            var1.setReadingObjectKey(var2);
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
               p("read length: " + var3 + ", Vendor: " + (this.foreignKey ? "Foreign" : "BEA"));
               if (!this.foreignKey) {
                  p("Domain Id: " + this.wleDomainId.toString() + ", Interface: " + this.interfaceName + ", Target: " + this.target + ", " + this.identity + " (local: " + LocalServerIdentity.getIdentity().getTransientIdentity() + ")");
               } else {
                  try {
                     p("Key data: " + new String(this.key_data, "US-ASCII"));
                  } catch (UnsupportedEncodingException var19) {
                  }
               }
            }

         }
      }
   }

   private final void readForeignKey(IIOPInputStream var1, int var2, long var3) {
      var1.reset();
      this.key_data = new byte[var2];
      var1.read_octet_array((byte[])this.key_data, 0, this.key_data.length);
      this.foreignKey = true;
      var1.endEncapsulation(var3);
   }

   final void write(IIOPOutputStream var1) {
      long var2 = var1.startEncapsulationNoEndian();
      if (this.preMarshalled == null) {
         if (!var1.isWritingObjectKey()) {
            IIOPOutputStream var4 = new IIOPOutputStream(var1.getLittleEndian(), var1.getEndPoint());
            var4.setWritingObjectKey(true);
            this.writeEncapsulation(var4);
            this.preMarshalled = var4.getBuffer();
            var4.close();
         } else {
            IIOPOutputStream.Marker var5 = new IIOPOutputStream.Marker();
            var1.setMark(var5);
            this.writeEncapsulation(var1);
            this.preMarshalled = var1.getBufferFromMark(var5);
         }
      }

      var1.write_octet_array(this.preMarshalled, 0, this.preMarshalled.length);
      var1.endEncapsulation(var2);
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("wrote key length: " + this.keyLength + ", Vendor: " + (this.isForeignKey() ? "Foreign" : "BEA"));
         if (!this.isForeignKey()) {
            p("Domain Id: " + this.wleDomainId.toString() + ", Interface: " + this.interfaceName + " Target: " + this.target);
         }

         p("Key data: " + var1.dumpBuf());
      }

   }

   private final void writeEncapsulation(IIOPOutputStream var1) {
      if (this.foreignKey) {
         var1.write_octet_array(this.key_data, 0, this.key_data.length);
      } else {
         var1.putEndian();
         this.writeVendor(var1);
         var1.write_octet((byte)8);
         var1.write_octet((byte)this.wleVersionMajor);
         var1.write_octet((byte)this.wleVersionMinor);
         var1.write_octet((byte)this.wleObjectAdapter);
         this.wleDomainId.write(var1);
         var1.write_long(this.wleGroupId);
         var1.write_repository_id(this.interfaceName);
         if (this.oid == 0 || this.wleObjectId != null && this.wleObjectId.length() != 0) {
            var1.write_string(this.wleObjectId);
         } else {
            var1.write_string(Integer.toString(this.oid));
         }

         if (this.wleVersionMinor > 1 && this.wleVersionMajor == 1) {
            var1.write_long(this.numComponents);
            long var2;
            switch (this.keyType) {
               case 1111834888:
                  var1.write_long(1111834888);
                  var1.write_long(0);
                  break;
               case 1111834897:
                  var1.write_long(1111834897);
                  var2 = var1.startEncapsulationNoNesting();
                  this.identity.write(var1);
                  var1.write_value((Serializable)this.getActivationID());
                  var1.endEncapsulation(var2);
                  break;
               case 1111834922:
                  var1.write_long(1111834922);
                  var2 = var1.startEncapsulation();
                  this.identity.write(var1);
                  var1.endEncapsulation(var2);
            }

            if (this.writeObjInfo) {
               var1.write_long(1111834891);
               var2 = var1.startEncapsulation();
               var1.write_octet_sequence(this.getObjInfo());
               var1.endEncapsulation(var2);
            }

            for(int var4 = 0; var4 < this.numForeignComponents; ++var4) {
               this.foreignComponents[var4].write(var1);
            }
         } else {
            var1.write_long(0);
         }

      }
   }

   private void close(Closeable var1) {
      try {
         var1.close();
      } catch (IOException var3) {
      }

   }

   public final int hashCode() {
      int var1 = this.foreignKey ? 0 : -1;
      if (this.foreignKey) {
         if (this.key_data != null) {
            for(int var2 = 0; var2 < this.key_data.length; ++var2) {
               var1 ^= this.key_data[var2];
            }
         }
      } else {
         var1 ^= this.wleVersionMajor ^ this.wleVersionMinor ^ this.wleObjectAdapter ^ this.wleGroupId ^ this.oid ^ this.wleDomainId.hashCode();
      }

      return var1;
   }

   public final boolean equals(Object var1) {
      if (var1 instanceof ObjectKey) {
         ObjectKey var2 = (ObjectKey)var1;
         if (this.foreignKey) {
            if (!var2.foreignKey) {
               return false;
            } else if (this.key_data == null) {
               return var2.key_data == null;
            } else if (this.key_data.length != var2.key_data.length) {
               return false;
            } else {
               for(int var3 = 0; var3 < this.key_data.length; ++var3) {
                  if (this.key_data[var3] != var2.key_data[var3]) {
                     return false;
                  }
               }

               return true;
            }
         } else if (var2.foreignKey) {
            return false;
         } else if (this.wleVersionMajor != var2.wleVersionMajor) {
            return false;
         } else if (this.wleVersionMinor != var2.wleVersionMinor) {
            return false;
         } else if (this.wleObjectAdapter != var2.wleObjectAdapter) {
            return false;
         } else if (this.wleGroupId != var2.wleGroupId) {
            return false;
         } else if (this.oid != var2.oid) {
            return false;
         } else if (!this.wleDomainId.equals(var2.wleDomainId)) {
            return false;
         } else if (!this.interfaceName.equals(var2.interfaceName)) {
            return false;
         } else if (!this.wleObjectId.equals(var2.wleObjectId)) {
            return false;
         } else if (this.wleScaInterfaceBucket != var2.wleScaInterfaceBucket) {
            return false;
         } else if (this.target != null && this.identity != var2.identity && !this.identity.equals(var2.identity)) {
            return false;
         } else {
            return this.keyType == var2.keyType;
         }
      } else {
         return false;
      }
   }

   public final String toString() {
      return !this.foreignKey ? "type: " + ServiceContext.VMCIDToString(this.keyType) + ", interface: " + this.interfaceName + ", oid: " + this.oid + ", target: " + this.target + ", identity: " + this.identity : Hex.dump(this.key_data, 0, this.key_data.length);
   }

   public String[] getRemoteInterfaces() {
      return this.remoteInterfaces;
   }

   public boolean isRepositoryIdAnInterface() {
      return this.isRepIdAnInterface;
   }

   private String[] getRemoteInterfacesForOid(int var1) {
      String[] var2 = null;

      try {
         ServerReference var3 = OIDManager.getInstance().findServerReference(var1);
         if (var3 != null) {
            RuntimeDescriptor var4 = var3.getDescriptor();
            if (var4 != null) {
               RemoteType var5 = var4.getRemoteType();
               if (var5 != null) {
                  var2 = var5.getInterfaces();
               }
            }
         }
      } catch (Exception var6) {
      }

      return var2;
   }

   private boolean isRepositoryIdAnInterfaceForOid(int var1, RepositoryId var2) {
      boolean var3 = true;

      try {
         ServerReference var4 = OIDManager.getInstance().findServerReference(var1);
         if (var4 != null) {
            Object var5 = var4.getImplementation();
            if (var5 == null && var4 instanceof ActivatableServerReference) {
               try {
                  var5 = ((ActivatableServerReference)var4).getImplementation(this.activationID);
               } catch (RemoteException var7) {
               }
            }

            if (var5 != null && var2 != null && var5.getClass().getName().equals(var2.getClassName())) {
               var3 = false;
            }
         }
      } catch (Exception var8) {
      }

      return var3;
   }

   private byte[] getObjInfo() {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      DataOutputStream var2 = null;

      try {
         var2 = new DataOutputStream(var1);
         boolean var3 = true;
         if (this.computeRepId) {
            var3 = this.isRepositoryIdAnInterfaceForOid(this.oid, this.interfaceName);
         } else {
            var3 = this.isRepIdAnInterface;
         }

         var2.writeBoolean(var3);
         String var4 = "";
         if (this.remoteInterfaces != null) {
            var4 = StringUtils.join(this.remoteInterfaces, ":");
         }

         var2.writeUTF(var4);
         var2.flush();
      } catch (Exception var8) {
      } finally {
         if (var2 != null) {
            this.close(var2);
         }

      }

      return var1.toByteArray();
   }

   private boolean checkObjInfoRequiredToBeMarshalled() {
      boolean var1 = false;
      if (!this.interfaceName.isIDLType()) {
         this.remoteInterfaces = this.getRemoteInterfacesForOid(this.oid);
         if (this.remoteInterfaces != null && this.remoteInterfaces.length > 1) {
            this.writeObjInfo = true;
            ++this.numComponents;
            var1 = true;
         }
      }

      return var1;
   }

   static {
      Kernel.ensureInitialized();
      typeIdMap.put(new Integer(8), new String[]{NamingContextHelper.id(), "NameService"});
      typeIdMap.put("NameService", new String[]{NamingContextHelper.id(), "NameService"});
      typeIdMap.put("INIT", new String[]{NamingContextHelper.id(), "NameService"});
      typeIdMap.put("BEA:NameService:Root", new String[]{NamingContextHelper.id(), "NameService"});
      typeIdMap.put("TransactionFactory", new String[]{TransactionFactoryHelper.id(), "TransactionFactory"});
      typeIdMap.put("CodeBase", new String[]{CodeBaseHelper.id(), "CodeBase"});
      localDomainId = getLocalDomainIDInternal();
   }
}
