package weblogic.wsee.jws.conversation.database;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import weblogic.wsee.jws.conversation.WlwObjectInputStream;

class LoadedObject {
   long _timestamp;
   byte[] _bytes;

   LoadedObject(long var1, byte[] var3) {
      this._timestamp = var1;
      this._bytes = var3;
   }

   long getTimestamp() {
      return this._timestamp;
   }

   Object getObject() throws IOException, ClassNotFoundException {
      ByteArrayInputStream var1 = null;
      WlwObjectInputStream var2 = null;

      Object var4;
      try {
         var1 = new ByteArrayInputStream(this._bytes);
         var2 = new WlwObjectInputStream(var1, Thread.currentThread().getContextClassLoader());
         Object var3 = var2.readObject();
         var4 = var3;
      } finally {
         try {
            var2.close();
         } catch (IOException var11) {
         }

      }

      return var4;
   }
}
