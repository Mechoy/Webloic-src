package weblogic.common;

/** @deprecated */
public interface AdminServicesDef {
   /** @deprecated */
   String ping(byte[] var1) throws Exception;

   /** @deprecated */
   String shut(int var1) throws Exception;

   /** @deprecated */
   String shut() throws Exception;

   /** @deprecated */
   String shut(String var1, int var2) throws Exception;

   /** @deprecated */
   String cancelShut() throws Exception;

   /** @deprecated */
   String lockServer(String var1) throws Exception;

   String unlockServer() throws Exception;

   /** @deprecated */
   String version() throws Exception;

   /** @deprecated */
   void enableWatchDog(int var1) throws Exception;

   /** @deprecated */
   void disableWatchDog() throws Exception;

   /** @deprecated */
   void threadDump() throws Exception;
}
