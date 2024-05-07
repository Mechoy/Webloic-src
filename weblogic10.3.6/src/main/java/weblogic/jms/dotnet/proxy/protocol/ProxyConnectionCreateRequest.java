package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public final class ProxyConnectionCreateRequest extends ProxyRequest {
   private static final int EXTVERSION = 1;
   private static final int _IS_XA = 1;
   private static final int _HAS_USER_NAME = 2;
   private static final int _HAS_PASSWORD = 3;
   private long listenerServiceId;
   private String userName;
   private String password;
   private boolean createXAConnection;

   public ProxyConnectionCreateRequest() {
   }

   public ProxyConnectionCreateRequest(long var1, String var3, String var4, boolean var5) {
      this.listenerServiceId = var1;
      this.userName = var3;
      this.password = var4;
      this.createXAConnection = var5;
   }

   public String getUserName() {
      return this.userName;
   }

   public String getPassword() {
      return this.password;
   }

   public boolean isCreateXAConnection() {
      return this.createXAConnection;
   }

   public long getListenerServiceId() {
      return this.listenerServiceId;
   }

   public int getMarshalTypeCode() {
      return 9;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      if (this.userName != null) {
         this.versionFlags.setBit(2);
      }

      if (this.password != null) {
         this.versionFlags.setBit(3);
      }

      if (this.createXAConnection) {
         this.versionFlags.setBit(1);
      }

      this.versionFlags.marshal(var1);
      var1.writeLong(this.listenerServiceId);
      if (this.userName != null) {
         var1.writeString(this.userName);
      }

      if (this.password != null) {
         var1.writeString(this.password);
      }

   }

   public void unmarshal(MarshalReader var1) {
      this.versionFlags = new MarshalBitMask();
      this.versionFlags.unmarshal(var1);
      ProxyUtil.checkVersion(this.versionFlags.getVersion(), 1, 1);
      this.listenerServiceId = var1.readLong();
      if (this.versionFlags.isSet(2)) {
         this.userName = var1.readString();
      }

      if (this.versionFlags.isSet(3)) {
         this.password = var1.readString();
      }

      if (this.versionFlags.isSet(1)) {
         this.createXAConnection = true;
      }

   }
}
