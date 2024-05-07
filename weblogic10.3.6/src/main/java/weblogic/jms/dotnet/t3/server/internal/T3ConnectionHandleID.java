package weblogic.jms.dotnet.t3.server.internal;

import weblogic.jms.dotnet.t3.server.spi.T3Connection;
import weblogic.jms.dotnet.t3.server.spi.T3ConnectionHandle;

public class T3ConnectionHandleID {
   private long value;
   private T3ConnectionHandle handle;
   private T3Connection connection;

   public long getValue() {
      return this.value;
   }

   public T3ConnectionHandleID(long var1) {
      this.value = var1;
   }

   public int hashCode() {
      return (int)(this.value >> 32 ^ this.value & -1L);
   }

   public T3Connection getConnection() {
      return this.connection;
   }

   public void setHandle(T3ConnectionHandle var1) {
      this.handle = var1;
   }

   public void setConnection(T3Connection var1) {
      this.connection = var1;
   }

   public T3ConnectionHandle getHandle() {
      return this.handle;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof T3ConnectionHandleID)) {
         return false;
      } else {
         return ((T3ConnectionHandleID)var1).value == this.value;
      }
   }
}
