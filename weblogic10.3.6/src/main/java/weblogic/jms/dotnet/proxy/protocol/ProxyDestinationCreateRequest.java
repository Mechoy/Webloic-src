package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public final class ProxyDestinationCreateRequest extends ProxyRequest {
   private String destinationName;
   private int destinationType;
   private boolean temporary;
   private static final int EXTVERSION = 1;
   private static final int _HAS_DESTINATION_TYPE = 1;
   private static final int _IS_TEMPORARY = 2;
   public static final int TYPE_QUEUE = 1;
   public static final int TYPE_TOPIC = 2;

   public ProxyDestinationCreateRequest(String var1, int var2, boolean var3) {
      this.destinationName = var1;
      this.destinationType = var2;
      this.temporary = var3;
   }

   public final String getDestinationName() {
      return this.destinationName;
   }

   public final void setDestinationName(String var1) {
      this.destinationName = var1;
   }

   public final int getDestinationType() {
      return this.destinationType;
   }

   public final boolean isTemporary() {
      return this.temporary;
   }

   public ProxyDestinationCreateRequest() {
   }

   public int getMarshalTypeCode() {
      return 20;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      if (this.destinationType != -1) {
         this.versionFlags.setBit(1);
      }

      if (this.temporary) {
         this.versionFlags.setBit(2);
      }

      this.versionFlags.marshal(var1);
      var1.writeString(this.destinationName);
      if (this.destinationType != -1) {
         var1.writeInt(this.destinationType);
      }

   }

   public void unmarshal(MarshalReader var1) {
      MarshalBitMask var2 = new MarshalBitMask();
      var2.unmarshal(var1);
      ProxyUtil.checkVersion(var2.getVersion(), 1, 1);
      this.destinationName = var1.readString();
      if (var2.isSet(1)) {
         this.destinationType = var1.readInt();
      }

      this.temporary = var2.isSet(2);
   }
}
