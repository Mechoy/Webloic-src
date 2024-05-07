package weblogic.servlet.internal.session;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Iterator;
import weblogic.application.AppClassLoaderManager;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.provider.ManagementService;
import weblogic.rmi.utils.io.RemoteObjectReplacer;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.io.ChunkInputStreamAccess;
import weblogic.utils.io.ChunkOutput;
import weblogic.utils.io.ChunkedObjectOutputStream;
import weblogic.utils.io.StringInput;
import weblogic.utils.io.StringOutput;

public class ReplicatedSessionChange implements Externalizable {
   private static final long serialVersionUID = -2864760712171499570L;
   protected final HashMap attributeChanges = new HashMap();
   protected final HashMap internalChanges = new HashMap();
   protected int maxInactiveInterval;
   protected long lastAccessedTime;
   protected long timeOnPrimaryAtUpdate;
   protected boolean modified = false;
   protected String annotation;
   protected static final DebugLogger DEBUG_SESSIONS = DebugLogger.getDebugLogger("DebugHttpSessions");
   private transient boolean useLazyDeserialization = false;
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final ClusterMBean clusterMbean;

   synchronized void init(int var1, long var2) {
      this.maxInactiveInterval = var1;
      this.lastAccessedTime = var2;
      this.modified = true;
      String var4 = ReplicatedSessionData.getAnnotation();
      this.annotation = var4 == null ? "" : var4;
   }

   void clear() {
      this.attributeChanges.clear();
      this.internalChanges.clear();
      this.modified = false;
   }

   boolean getModified() {
      return this.modified;
   }

   void setTimeOnPrimaryAtUpdate(long var1) {
      this.timeOnPrimaryAtUpdate = var1;
   }

   long getTimeOnPrimaryAtUpdate() {
      return this.timeOnPrimaryAtUpdate;
   }

   long getLastAccessTime() {
      return this.lastAccessedTime;
   }

   int getMaxInActiveInterval() {
      return this.maxInactiveInterval;
   }

   HashMap getAttributeChanges() {
      return this.attributeChanges;
   }

   HashMap getInternalAttributeChanges() {
      return this.internalChanges;
   }

   public void addAttributeChange(String var1, Object var2) {
      this.getAttributeChanges().put(var1, var2);
   }

   public void addInternalAttributeChange(String var1, Object var2) {
      this.getInternalAttributeChanges().put(var1, var2);
   }

   public String toString() {
      return "[Changes:" + this.attributeChanges.size() + " internalChanges:" + this.internalChanges.size() + " maxInactiveInterval: " + this.maxInactiveInterval + " lastAccessedTime: " + this.lastAccessedTime + "]";
   }

   private void writeString(ObjectOutput var1, String var2) throws IOException {
      if (var1 instanceof StringOutput) {
         StringOutput var3 = (StringOutput)var1;
         var3.writeUTF8(var2);
      } else {
         var1.writeUTF(var2);
      }

   }

   private String readString(ObjectInput var1) throws IOException {
      if (var1 instanceof StringInput) {
         StringInput var2 = (StringInput)var1;
         return var2.readUTF8();
      } else {
         return var1.readUTF();
      }
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      this.writeString(var1, this.annotation);
      int var2 = this.attributeChanges.size();
      var1.writeInt(var2);
      Iterator var3;
      String var11;
      if (var2 > 0) {
         var3 = this.attributeChanges.keySet().iterator();
         if (clusterMbean.isSessionLazyDeserializationEnabled() && var1 instanceof PeerInfoable) {
            PeerInfo var4 = ((PeerInfoable)var1).getPeerInfo();
            if (var4.compareTo(PeerInfo.VERSION_1033) > 0) {
               this.useLazyDeserialization = true;
            }
         }

         if (!this.useLazyDeserialization) {
            while(var3.hasNext()) {
               var11 = (String)var3.next();
               this.writeString(var1, var11);
               var1.writeObject(this.attributeChanges.get(var11));
            }
         } else {
            ChunkedObjectOutputStream var10 = new ChunkedObjectOutputStream();
            var10.setReplacer(RemoteObjectReplacer.getReplacer());

            while(var3.hasNext()) {
               try {
                  String var5 = (String)var3.next();
                  this.writeString(var1, var5);
                  var10.writeObject(this.attributeChanges.get(var5));
                  ((ChunkOutput)var1).writeChunks(var10.getChunks());
               } finally {
                  var10.reset();
               }
            }
         }
      }

      var2 = this.internalChanges.size();
      var1.writeInt(var2);
      if (var2 > 0) {
         var3 = this.internalChanges.keySet().iterator();

         while(var3.hasNext()) {
            var11 = (String)var3.next();
            this.writeString(var1, var11);
            var1.writeObject(this.internalChanges.get(var11));
         }
      }

      var1.writeInt(this.maxInactiveInterval);
      var1.writeLong(this.lastAccessedTime);
      var1.writeLong(this.timeOnPrimaryAtUpdate);
      var1.writeBoolean(this.modified);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      GenericClassLoader var2 = null;
      String var3 = this.readString(var1);
      var2 = AppClassLoaderManager.getAppClassLoaderManager().findLoader(new Annotation(var3));
      Thread var4 = Thread.currentThread();
      ClassLoader var5 = var4.getContextClassLoader();
      if (var2 != null) {
         var4.setContextClassLoader(var2);
      }

      if (clusterMbean.isSessionLazyDeserializationEnabled() && var1 instanceof PeerInfoable) {
         PeerInfo var6 = ((PeerInfoable)var1).getPeerInfo();
         if (var6.compareTo(PeerInfo.VERSION_1033) > 0) {
            this.useLazyDeserialization = true;
         }
      }

      try {
         this.clear();
         int var15 = var1.readInt();

         int var7;
         String var8;
         for(var7 = 0; var7 < var15; ++var7) {
            var8 = this.readString(var1);
            if (!this.useLazyDeserialization) {
               this.attributeChanges.put(var8, var1.readObject());
            } else {
               int var9 = ((ChunkInputStreamAccess)var1).readChunkLength();
               byte[] var10 = new byte[var9];
               ((ChunkInputStreamAccess)var1).readByteArray(var10, 0, var9);
               if (var10.length == 1 && var10[0] == 112) {
                  this.attributeChanges.put(var8, (Object)null);
               } else {
                  this.attributeChanges.put(var8, var10);
               }
            }
         }

         var15 = var1.readInt();

         for(var7 = 0; var7 < var15; ++var7) {
            var8 = this.readString(var1);
            this.internalChanges.put(var8, var1.readObject());
         }

         this.maxInactiveInterval = var1.readInt();
         this.lastAccessedTime = var1.readLong();
         this.timeOnPrimaryAtUpdate = var1.readLong();
         this.modified = var1.readBoolean();
      } finally {
         if (var2 != null) {
            var4.setContextClassLoader(var5);
         }

      }
   }

   public boolean isUseLazyDeserialization() {
      return this.useLazyDeserialization;
   }

   static {
      clusterMbean = ManagementService.getRuntimeAccess(KERNEL_ID).getServer().getCluster();
   }
}
