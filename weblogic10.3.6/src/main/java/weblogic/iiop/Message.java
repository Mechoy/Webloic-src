package weblogic.iiop;

import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.SystemException;
import weblogic.common.internal.PeerInfo;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.Kernel;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.SyncKeyTable;

public abstract class Message implements weblogic.iiop.spi.Message {
   protected MessageHeader msgHdr;
   protected int request_id;
   protected ServiceContextList serviceContexts;
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");
   private static final DebugCategory debugTransport = Debug.getCategory("weblogic.iiop.transport");
   private static final DebugLogger debugIIOPTransport = DebugLogger.getDebugLogger("DebugIIOPTransport");
   protected EndPoint endPoint;
   protected IIOPInputStream inputStream;
   protected IIOPOutputStream outputStream;
   private boolean marshaled = false;
   private byte maxFormatVersion = 1;

   public Message() {
      this.serviceContexts = new ServiceContextList();
   }

   public Message(ServiceContextList var1) {
      this.serviceContexts = var1;
   }

   public void close() {
      this.endPoint = null;
      this.outputStream = null;
      this.inputStream = null;
      this.msgHdr.close();
      this.msgHdr = null;
      this.serviceContexts.reset();
   }

   public final EndPoint getEndPoint() {
      return this.endPoint;
   }

   public final MessageHeader getMessageHeader() {
      return this.msgHdr;
   }

   public final int getRequestID() {
      return this.request_id;
   }

   public final int hashCode() {
      return this.request_id;
   }

   public final boolean equals(Object var1) {
      if (var1 instanceof Message) {
         return var1.hashCode() == this.hashCode();
      } else {
         return false;
      }
   }

   public final int getMsgType() {
      return this.msgHdr.getMsgType();
   }

   public final int getMsgSize() {
      return this.msgHdr.getMsgSize();
   }

   public final boolean isLittleEndian() {
      return this.msgHdr.isLittleEndian();
   }

   public final boolean isFragmented() {
      return this.msgHdr.isFragmented();
   }

   public final int getMinorVersion() {
      int var1 = this.msgHdr.getMinorVersion();
      switch (var1) {
         case 0:
         case 1:
         case 2:
            return var1;
         default:
            throw new MARSHAL("Unsupported GIOP minor version.");
      }
   }

   public final byte getMaxStreamFormatVersion() {
      return this.maxFormatVersion;
   }

   protected final void setMaxStreamFormatVersion(byte var1) {
      this.maxFormatVersion = var1;
   }

   public final void alignOnEightByteBoundry(IIOPInputStream var1) {
      switch (this.getMinorVersion()) {
         case 2:
            var1.setNeedEightByteAlignment();
         case 0:
         case 1:
         default:
      }
   }

   public final void alignOnEightByteBoundry(IIOPOutputStream var1) {
      switch (this.getMinorVersion()) {
         case 2:
            if (var1.getPeerInfo().compareTo(PeerInfo.VERSION_701) <= 0 && var1.getPeerInfo() != PeerInfo.FOREIGN) {
               var1.align8();
            } else {
               var1.setNeedEightByteAlignment();
            }
         case 0:
         case 1:
         default:
      }
   }

   public final IIOPOutputStream getOutputStream() {
      if (this.outputStream == null) {
         this.outputStream = new IIOPOutputStream(this.isLittleEndian(), this.endPoint, this);
      }

      return this.outputStream;
   }

   public abstract void write(IIOPOutputStream var1) throws SystemException;

   public void read(IIOPInputStream var1) throws SystemException {
   }

   public void flush() {
      if (!this.marshaled) {
         if (this.inputStream != null) {
            this.read(this.getInputStream());
         } else {
            this.write(this.getOutputStream());
         }

         this.marshaled = true;
      }

   }

   public final IIOPInputStream getInputStream() {
      Debug.assertion(this.inputStream != null);
      return this.inputStream;
   }

   public final ServiceContext getServiceContext(int var1) {
      return this.serviceContexts.getServiceContext(var1);
   }

   public ServiceContextList getServiceContexts() {
      return this.serviceContexts;
   }

   public final void removeServiceContext(int var1) {
      this.serviceContexts.removeServiceContext(var1);
   }

   protected final void addServiceContext(ServiceContext var1) {
      this.serviceContexts.addServiceContext(var1);
   }

   protected void writeServiceContexts(IIOPOutputStream var1) {
      this.serviceContexts.write(var1);
   }

   protected void readServiceContexts(IIOPInputStream var1) {
      this.serviceContexts.read(var1);
      CodeSet var2 = (CodeSet)this.serviceContexts.getServiceContext(1);
      if (var2 != null) {
         int var3 = var2.getCharCodeSet();
         int var4 = var2.getWcharCodeSet();
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled() || debugTransport.isEnabled() || debugIIOPTransport.isDebugEnabled()) {
            IIOPLogger.logDebugTransport("client selected char codeset = " + Integer.toHexString(var3) + ", wchar codeset = " + Integer.toHexString(var4));
         }

         var1.setCodeSets(var3, var4);
         if (this.endPoint != null && !this.endPoint.getFlag(16)) {
            this.endPoint.setCodeSets(var3, var4);
            this.endPoint.setFlag(32);
         }
      }

      SendingContextRunTime var9 = (SendingContextRunTime)this.serviceContexts.getServiceContext(6);
      if (var9 != null) {
         this.endPoint.setRemoteCodeBase(var9.getCodeBase());
      }

      SFVContext var10 = (SFVContext)this.serviceContexts.getServiceContext(17);
      if (var10 != null) {
         this.setMaxStreamFormatVersion(var10.getMaxFormatVersion());
      }

      if (this.endPoint != null && this.endPoint.supportsForwarding()) {
         BiDirIIOPContextImpl var5 = (BiDirIIOPContextImpl)this.serviceContexts.getServiceContext(5);
         if (var5 != null) {
            this.endPoint.setFlag(64);
            SyncKeyTable var6 = (SyncKeyTable)this.endPoint.getConnection().getProperty("weblogic.iiop.BiDirKeys");
            if (var6 == null) {
               var6 = new SyncKeyTable();
               this.endPoint.getConnection().setProperty("weblogic.iiop.BiDirKeys", var6);
            }

            ConnectionKey[] var7 = var5.getListenPoints();

            for(int var8 = 0; var8 < var7.length; ++var8) {
               var6.put(var7[var8]);
            }

            if (var7.length > 0) {
               EndPointManager.updateConnection(this.endPoint.getConnection(), var7[0]);
            }
         }

         RoutingContext var12 = (RoutingContext)this.serviceContexts.getServiceContext(1111834888);
         if (var12 != null) {
            EndPointManager.updateRoutingTable(var12.getIdentity(), var12.getConnectionKey());
         }
      }

      if (this.endPoint != null && this.endPoint.getPeerInfo() == null) {
         VendorInfo var11 = (VendorInfo)this.serviceContexts.getServiceContext(1111834880);
         if (var11 != null) {
            this.endPoint.setPeerInfo(var11.getPeerInfo());
         }
      }

   }

   protected static void p(String var0) {
      System.err.println("<Message> " + var0);
   }
}
