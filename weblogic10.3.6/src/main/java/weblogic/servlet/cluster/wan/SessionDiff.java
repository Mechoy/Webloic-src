package weblogic.servlet.cluster.wan;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import weblogic.servlet.utils.ServletObjectOutputStream;

public final class SessionDiff implements Externalizable {
   static final long serialVersionUID = 8310551219025541051L;
   private HashMap newAttributes = new HashMap(11);
   private HashMap updateAttributes = new HashMap(11);
   private HashMap newInternalAttributes = new HashMap(3);
   private HashMap updateInternalAttributes = new HashMap(3);
   private int versionCounter;
   private boolean serialized;

   public final synchronized SessionDiff cloneAndClear() {
      SessionDiff var1 = new SessionDiff();
      var1.updateAttributes = (HashMap)this.updateAttributes.clone();
      var1.updateInternalAttributes = (HashMap)this.updateInternalAttributes.clone();
      var1.newAttributes = (HashMap)this.newAttributes.clone();
      var1.newInternalAttributes = (HashMap)this.newInternalAttributes.clone();
      ++this.versionCounter;
      var1.versionCounter = this.versionCounter;
      this.updateAttributes.clear();
      this.updateInternalAttributes.clear();
      this.newAttributes.clear();
      this.newInternalAttributes.clear();
      return var1;
   }

   public final int getVersionCount() {
      return this.versionCounter;
   }

   public final synchronized void setAttribute(String var1, Object var2, boolean var3, boolean var4) {
      HashMap var5;
      if (!var3 && !this.newAttributes.containsKey(var1)) {
         var5 = var4 ? this.updateInternalAttributes : this.updateAttributes;
         var5.put(var1, var2);
      } else {
         var5 = var4 ? this.newInternalAttributes : this.newAttributes;
         var5.put(var1, var2);
      }
   }

   HashMap getNewAttributes() {
      return this.newAttributes;
   }

   HashMap getUpdateAttributes() {
      return this.updateAttributes;
   }

   HashMap getNewInternalAttributes() {
      return this.newInternalAttributes;
   }

   HashMap getUpdateInternalAttributes() {
      return this.updateInternalAttributes;
   }

   public HashMap getAttributes() {
      HashMap var1 = new HashMap();
      HashMap var2 = null;
      HashMap var3 = null;
      synchronized(this) {
         if (this.newAttributes.size() > 0) {
            var2 = (HashMap)this.newAttributes.clone();
         }

         if (this.updateAttributes.size() > 0) {
            var3 = (HashMap)this.updateAttributes.clone();
         }
      }

      Iterator var4;
      Object var5;
      if (var2 != null) {
         var4 = var2.keySet().iterator();

         while(var4.hasNext()) {
            var5 = var4.next();
            var1.put(var5, var2.get(var5));
         }
      }

      if (var3 != null) {
         var4 = var3.keySet().iterator();

         while(var4.hasNext()) {
            var5 = var4.next();
            var1.put(var5, var3.get(var5));
         }
      }

      return var1;
   }

   public HashMap getInternalAttributes() {
      HashMap var1 = new HashMap();
      HashMap var2 = null;
      HashMap var3 = null;
      synchronized(this) {
         if (this.newInternalAttributes.size() > 0) {
            var2 = (HashMap)var1.clone();
         }

         if (this.updateInternalAttributes.size() > 0) {
            var3 = (HashMap)this.updateInternalAttributes.clone();
         }
      }

      Iterator var4;
      Object var5;
      if (var2 != null) {
         var4 = var2.keySet().iterator();

         while(var4.hasNext()) {
            var5 = var4.next();
            var1.put(var5, var2.get(var5));
         }
      }

      if (var3 != null) {
         var4 = var3.keySet().iterator();

         while(var4.hasNext()) {
            var5 = var4.next();
            var1.put(var5, var3.get(var5));
         }
      }

      return var1;
   }

   public void setVersionCounter(int var1) {
      this.versionCounter = var1;
   }

   byte[] getBytesForDB(Object var1) throws IOException {
      byte[] var2;
      if (this.serialized) {
         ByteBuffer var3 = (ByteBuffer)var1;
         var3.rewind();
         var2 = new byte[var3.remaining()];
         var3.get(var2);
      } else {
         var2 = (byte[])passivateObject(var1);
      }

      return var2;
   }

   static byte[] passivateObject(Object var0) throws IOException {
      ServletObjectOutputStream var1 = null;
      var1 = ServletObjectOutputStream.getOutputStream();
      var1.writeObject(var0);
      var1.flush();
      return var1.toByteArray();
   }

   private synchronized void passivateMap(ObjectOutput var1, HashMap var2) throws IOException {
      int var3 = var2.size();
      var1.writeInt(var3);
      if (var3 > 0) {
         Iterator var4 = var2.keySet().iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            byte[] var6 = passivateObject(var2.get(var5));
            var1.writeUTF(var5);
            var1.writeInt(var6.length);
            var1.write(var6);
         }
      }

   }

   private void readMap(ObjectInput var1, HashMap var2) throws IOException {
      int var3 = var1.readInt();
      if (var3 > 0) {
         int var4 = 0;

         while(var4++ < var3) {
            String var5 = var1.readUTF();
            int var6 = var1.readInt();
            byte[] var7 = new byte[var6];
            ByteBuffer var8 = ByteBuffer.allocateDirect(var6);

            int var10;
            for(int var9 = 0; var9 < var6; var9 += var10) {
               var10 = var1.read(var7, var9, var6 - var9);
               if (var10 == -1) {
                  throw new IOException("Encountered EOF during deserialization");
               }
            }

            var8.put(var7, 0, var6);
            var2.put(var5, var8);
         }
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeInt(this.versionCounter);
      this.passivateMap(var1, this.newAttributes);
      this.passivateMap(var1, this.updateAttributes);
      this.passivateMap(var1, this.newInternalAttributes);
      this.passivateMap(var1, this.updateInternalAttributes);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.versionCounter = var1.readInt();
      this.readMap(var1, this.newAttributes);
      this.readMap(var1, this.updateAttributes);
      this.readMap(var1, this.newInternalAttributes);
      this.readMap(var1, this.updateInternalAttributes);
      this.serialized = true;
   }
}
