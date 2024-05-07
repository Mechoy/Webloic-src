package weblogic.common;

import weblogic.common.internal.AdminMsg;

final class AdminServicesImpl implements AdminServicesDef {
   T3Client t3;

   AdminServicesImpl(T3Client var1) {
      this.t3 = var1;
   }

   static Object create(T3Client var0) {
      return new AdminServicesImpl(var0);
   }

   public String ping(byte[] var1) throws Exception {
      return (String)this.t3.sendRecv("weblogic.common.internal.AdminProxy", (new AdminMsg((byte)1)).setEcho(var1));
   }

   public String ping() throws Exception {
      return this.ping((byte[])null);
   }

   public String shut(int var1) throws Exception {
      return this.shut((String)null, var1);
   }

   public String shut() throws Exception {
      return this.shut(-1);
   }

   public String shut(String var1, int var2) throws Exception {
      AdminMsg var3 = new AdminMsg((byte)2);
      var3.setIntervalSecs(var2);
      var3.setArgString(var1);
      return (String)this.t3.sendRecv("weblogic.common.internal.AdminProxy", var3);
   }

   public String cancelShut() throws Exception {
      return (String)this.t3.sendRecv("weblogic.common.internal.AdminProxy", new AdminMsg((byte)12));
   }

   public String lockServer(String var1) throws Exception {
      return (String)this.t3.sendRecv("weblogic.common.internal.AdminProxy", (new AdminMsg((byte)10)).setArgString(var1));
   }

   public String unlockServer() throws Exception {
      return (String)this.t3.sendRecv("weblogic.common.internal.AdminProxy", new AdminMsg((byte)11));
   }

   public String version() throws Exception {
      return (String)this.t3.sendRecv("weblogic.common.internal.AdminProxy", new AdminMsg((byte)5));
   }

   public void enableWatchDog(int var1) throws Exception {
      this.t3.sendRecv("weblogic.common.internal.AdminProxy", (new AdminMsg((byte)6)).setIntervalSecs(var1));
   }

   public void disableWatchDog() throws Exception {
      this.t3.sendRecv("weblogic.common.internal.AdminProxy", new AdminMsg((byte)7));
   }

   public void threadDump() throws Exception {
      this.t3.sendRecv("weblogic.common.internal.AdminProxy", new AdminMsg((byte)13));
   }
}
