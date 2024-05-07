package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import javax.jms.Queue;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.Topic;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.JMSClientExceptionLogger;
import weblogic.jms.client.JMSConnection;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.jms.extensions.WLDestination;
import weblogic.messaging.dispatcher.DispatcherId;
import weblogic.store.common.PersistentStoreOutputStream;

public class DestinationImpl extends Destination implements Queue, Topic, TemporaryQueue, TemporaryTopic, Externalizable, WLDestination, Cloneable {
   private static final byte EXTVERSION1 = 1;
   private static final byte EXTVERSION2 = 2;
   private static final byte EXTVERSION3 = 3;
   private static final byte EXTVERSION4 = 4;
   private static final byte EXTVERSION5 = 5;
   private static final byte EXTVERSION6 = 6;
   private static final long serialVersionUID = 6099783323740404732L;
   public static final byte TYPE_ANONYMOUS = 0;
   public static final byte TYPE_QUEUE = 1;
   public static final byte TYPE_TOPIC = 2;
   public static final byte TYPE_TEMP_QUEUE = 4;
   public static final byte TYPE_TEMP_TOPIC = 8;
   private transient JMSConnection connection;
   private String name;
   private String serverName;
   private String applicationName;
   private String moduleName;
   JMSServerId backEndId;
   private String multicastAddress;
   private int port;
   private boolean pre90;
   private boolean pre10_3_4;
   JMSID destinationId;
   byte type;
   DispatcherId dispatcherId;
   private long generation;
   private static final int FIRSTGENERATION = 1;
   private String[] safExportAllowed;
   private String referenceName;
   private String persistentStoreName;
   private static final int _VERSIONMASK = 3840;
   private static final int _VERSIONSHIFT = 8;
   private static final int _TYPEMASK = 15;
   private static final int _TYPESHIFT = 0;
   private static final int _HASREFERENCENAME = 16;
   private static final int _HASGENERATION = 32;
   private static final int _ISPARTOFAPP = 64;
   private static final int _ISPARTOFEAR = 128;
   protected static final int _HASIDS = 4096;
   private static final int _ISNOTREPLYTO = 8192;
   private static final int _HASMULTICASTADDR = 16384;
   private static final int _HASDISPID = 32768;
   private static final int _HASSTORENAME = 1;
   private static final int _HASMOREFLAGS = 32768;

   public DestinationImpl() {
      this.pre90 = false;
      this.pre10_3_4 = false;
      this.type = 1;
      this.generation = 1L;
      this.persistentStoreName = null;
   }

   public DestinationImpl(byte var1, String var2, String var3, String var4, String var5) {
      this.pre90 = false;
      this.pre10_3_4 = false;
      this.type = 1;
      this.generation = 1L;
      this.persistentStoreName = null;
      this.applicationName = var4;
      this.moduleName = var5;
      this.destinationImplInternal(var1, var2, var3);
   }

   public void destinationImplInternal(byte var1, String var2, String var3) {
      this.name = var3;
      this.serverName = var2;
      this.type = var1;
   }

   public DestinationImpl(byte var1) {
      this.pre90 = false;
      this.pre10_3_4 = false;
      this.type = 1;
      this.generation = 1L;
      this.persistentStoreName = null;
      this.type = var1;
   }

   public DestinationImpl(int var1, String var2, String var3, String var4, String var5, String var6, JMSServerId var7, JMSID var8, long var9, String var11) {
      this.pre90 = false;
      this.pre10_3_4 = false;
      this.type = 1;
      this.generation = 1L;
      this.persistentStoreName = null;
      this.applicationName = var5;
      this.moduleName = var6;
      this.destinationImplInternalWithCreationTime(var1, var2, var4, var7, var8, var9);
      this.persistentStoreName = var3;
      this.setSafExportAllowedArray(var11);
   }

   private void destinationImplInternalWithCreationTime(int var1, String var2, String var3, JMSServerId var4, JMSID var5, long var6) {
      this.type = (byte)var1;
      this.serverName = var2;
      this.name = var3;
      this.backEndId = var4;
      this.destinationId = var5;
      this.generation = var6;
      this.dispatcherId = JMSDispatcherManager.getLocalDispatcher().getId();
   }

   public DestinationImpl(int var1, String var2, String var3, String var4, String var5, String var6, JMSServerId var7, JMSID var8, DispatcherId var9) {
      this.pre90 = false;
      this.pre10_3_4 = false;
      this.type = 1;
      this.generation = 1L;
      this.persistentStoreName = null;
      this.applicationName = var5;
      this.moduleName = var6;
      this.destinationImplInternalWithoutCreationTime(var1, var2, var4, var7, var8);
      this.dispatcherId = var9;
      this.persistentStoreName = var3;
   }

   public DestinationImpl(int var1, String var2, String var3, String var4, String var5, JMSServerId var6, JMSID var7) {
      this.pre90 = false;
      this.pre10_3_4 = false;
      this.type = 1;
      this.generation = 1L;
      this.persistentStoreName = null;
      this.applicationName = var4;
      this.moduleName = var5;
      this.destinationImplInternalWithoutCreationTime(var1, var2, var3, var6, var7);
      this.dispatcherId = JMSDispatcherManager.getLocalDispatcher().getId();
   }

   public DestinationImpl(int var1, String var2, String var3, String var4, String var5, JMSServerId var6, JMSID var7, String var8, String var9) {
      this(var1, var2, var3, var4, var5, var6, var7);
      this.setSafExportAllowedArray(var8);
      this.persistentStoreName = var9;
   }

   private void destinationImplInternalWithoutCreationTime(int var1, String var2, String var3, JMSServerId var4, JMSID var5) {
      this.type = (byte)var1;
      this.serverName = var2;
      this.name = var3;
      this.backEndId = var4;
      this.destinationId = var5;
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   public DestinationImpl getClone() {
      try {
         return (DestinationImpl)this.clone();
      } catch (CloneNotSupportedException var2) {
         throw new AssertionError(var2);
      }
   }

   public void setReferenceName(String var1) {
      this.referenceName = var1;
   }

   public String getReferenceName() {
      return this.referenceName;
   }

   void setSafExportAllowedArray(String var1) {
      if (var1 != null) {
         ArrayList var2 = new ArrayList();
         String var3 = new String(var1);

         int var6;
         for(boolean var4 = false; (var6 = var3.indexOf(",")) != -1; var3 = var3.substring(var6 + 1)) {
            var2.add(var3.substring(0, var6 - 1));
         }

         if (var3 != null) {
            var2.add(var3);
         }

         if (var2.size() > 0) {
            this.safExportAllowed = new String[var2.size()];

            for(int var5 = 0; var5 < var2.size(); ++var5) {
               this.safExportAllowed[var5] = (String)var2.get(var5);
            }
         }

      }
   }

   public String[] getSafAllowedArray() {
      return this.safExportAllowed;
   }

   public final boolean isQueue() {
      return this.type == 1 || this.type == 4;
   }

   public final boolean isTopic() {
      return this.type == 2 || this.type == 8;
   }

   public final boolean isAnonymous() {
      return this.type == 0;
   }

   String getDestinationName() {
      return this.name;
   }

   public String toString() {
      return this.getDestinationName();
   }

   public final String getQueueName() {
      return this.getDestinationName();
   }

   public final String getTopicName() {
      return this.getDestinationName();
   }

   public byte getDestinationInstanceType() {
      return 1;
   }

   public final int getType() {
      return this.type;
   }

   public final void setBackEndID(JMSServerId var1) {
      this.backEndId = var1;
   }

   public final synchronized JMSServerId getBackEndId() {
      return this.backEndId;
   }

   public final JMSID getId() {
      return this.destinationId;
   }

   public final synchronized JMSID getDestinationId() {
      return this.destinationId;
   }

   public final void setDestinationId(JMSID var1) {
      this.destinationId = var1;
   }

   public String getMemberName() {
      return this.name;
   }

   public String getCreateDestinationArgument() {
      String var1 = this.getServerName() + "/" + this.getMemberName();
      return var1.intern();
   }

   protected void setName(String var1) {
      this.name = var1;
   }

   public String getName() {
      return this.getDestinationName();
   }

   protected void setServerName(String var1) {
      this.serverName = var1;
   }

   public final String getServerName() {
      return this.serverName;
   }

   protected void setApplicationName(String var1) {
      this.applicationName = var1;
   }

   public final String getApplicationName() {
      return this.applicationName;
   }

   protected void setModuleName(String var1) {
      this.moduleName = var1;
   }

   public final String getModuleName() {
      return this.moduleName;
   }

   public final DispatcherId getDispatcherId() {
      return this.dispatcherId;
   }

   public final void setDispatcherId(DispatcherId var1) {
      this.dispatcherId = var1;
   }

   public synchronized boolean isStale() {
      return this.dispatcherId == null;
   }

   public synchronized void markStale() {
      this.dispatcherId = null;
   }

   public String getPersistentStoreName() {
      return this.persistentStoreName;
   }

   protected int getVersion(Object var1) throws IOException {
      if (var1 instanceof PeerInfoable) {
         PeerInfo var2 = ((PeerInfoable)var1).getPeerInfo();
         int var3 = var2.getMajor();
         if (var3 < 6) {
            throw new IOException(JMSClientExceptionLogger.logIncompatibleVersion9Loggable((byte)1, (byte)2, (byte)3, (byte)4, var2.toString()).getMessage());
         }

         switch (var3) {
            case 6:
               return 2;
            case 7:
            case 8:
               return 3;
            case 9:
               return 4;
            case 10:
               if (var2.compareTo(PeerInfo.VERSION_1034) >= 0) {
                  return 6;
               }

               if (var2.compareTo(PeerInfo.VERSION_1033) >= 0) {
                  return 5;
               }

               return 4;
         }
      }

      return 6;
   }

   public final boolean isPre90() {
      return this.pre90;
   }

   public final boolean isPre10_3_4() {
      return this.pre10_3_4;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      this.writeDestinationImpl(var1, this.name);
   }

   final void writeDestinationImpl(ObjectOutput var1) throws IOException {
      this.writeDestinationImpl(var1, this.name);
   }

   final void writeDestinationImpl(ObjectOutput var1, String var2) throws IOException {
      boolean var3 = var1 instanceof PersistentStoreOutputStream;
      boolean var4 = var1 instanceof WLObjectOutput;
      short var5 = 0;
      int var6 = this.getVersion(var1);
      var5 = (short)(var5 | var6 << 8);
      if (!var3 && var6 <= 3) {
         var5 &= -3841;
         var5 = (short)(var5 | 512);
      }

      var5 = (short)(var5 | this.type);
      if (var3) {
         var5 = (short)(var5 | 32);
         if (this.applicationName != null) {
            var5 = (short)(var5 | 64);
         }

         if (this.moduleName != null) {
            var5 = (short)(var5 | 128);
         }

         var1.writeShort(var5);
         var1.writeUTF(var2);
         var1.writeUTF(this.serverName);
         var1.writeLong(this.getGeneration());
         if (this.applicationName != null) {
            var1.writeUTF(this.applicationName);
         }

         if (this.moduleName != null) {
            var1.writeUTF(this.moduleName);
         }

      } else {
         var5 = (short)(var5 | 4096);
         if (this.multicastAddress != null) {
            var5 = (short)(var5 | 16384);
         }

         if (this.destinationId != null) {
            var5 = (short)(var5 | 8192);
         }

         JMSID var7 = this.destinationId;
         if (this.dispatcherId != null) {
            var5 = (short)(var5 | '耀');
         }

         if (this.applicationName != null) {
            var5 = (short)(var5 | 64);
         }

         if (this.moduleName != null) {
            var5 = (short)(var5 | 128);
         }

         if (var6 >= 4 && this.referenceName != null) {
            var5 = (short)(var5 | 16);
         }

         var1.writeShort(var5);
         if (var4) {
            ((WLObjectOutput)var1).writeAbbrevString(this.name);
            ((WLObjectOutput)var1).writeAbbrevString(this.serverName);
         } else {
            var1.writeUTF(this.name);
            var1.writeUTF(this.serverName);
         }

         if (var6 >= 4) {
            if (this.applicationName != null) {
               var1.writeUTF(this.applicationName);
            }

            if (this.moduleName != null) {
               var1.writeUTF(this.moduleName);
            }

            if (this.safExportAllowed == null) {
               var1.writeByte(0);
            } else {
               var1.writeByte(this.safExportAllowed.length);

               for(int var8 = 0; var8 < this.safExportAllowed.length; ++var8) {
                  var1.writeUTF(this.safExportAllowed[var8]);
               }
            }
         }

         if (var7 != null) {
            this.backEndId.writeExternal(var1);
            var7.writeExternal(var1);
         }

         if (this.multicastAddress != null) {
            var1.writeUTF(this.multicastAddress);
            var1.writeInt(this.port);
         }

         if (this.dispatcherId != null) {
            this.dispatcherId.writeExternal(var1);
         }

         if ((var5 & 16) != 0) {
            var1.writeUTF(this.referenceName);
         }

         short var9 = 0;
         if (var6 >= 5) {
            if (this.persistentStoreName != null && this.persistentStoreName.length() != 0) {
               var9 = (short)(var9 | 1);
            }

            var1.writeShort(var9);
         }

         if ((var9 & 1) != 0) {
            var1.writeUTF(this.persistentStoreName);
         }

      }
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      short var2 = var1.readShort();
      this.readDestinationImpl(var1, var2);
   }

   final void readDestinationImpl(ObjectInput var1, int var2) throws IOException, ClassNotFoundException {
      byte var3 = (byte)((var2 & 3840) >>> 8 & 255);
      if (var3 == 1) {
         this.readExternalVersion1(var1, (byte)(var2 & 255));
      } else if (var3 != 2 && var3 != 3 && var3 != 4 && var3 != 5 && var3 != 6) {
         throw JMSUtilities.versionIOException(var3, 1, 6);
      } else {
         if (var3 < 4) {
            this.pre90 = true;
         }

         if (var3 < 6) {
            this.pre10_3_4 = true;
         }

         this.type = (byte)((var2 & 15) >>> 0 & 255);
         if ((var2 & 4096) == 0) {
            this.name = var1.readUTF();
            this.serverName = var1.readUTF();
            if ((var2 & 32) != 0) {
               if (var3 == 2) {
                  this.generation = (long)var1.readInt();
               } else {
                  this.generation = var1.readLong();
               }
            }

            if ((var2 & 64) != 0) {
               this.applicationName = var1.readUTF();
            }

            if ((var2 & 128) != 0) {
               this.moduleName = var1.readUTF();
            }

         } else {
            if (var1 instanceof WLObjectInput) {
               this.name = ((WLObjectInput)var1).readAbbrevString();
               this.serverName = ((WLObjectInput)var1).readAbbrevString();
            } else {
               this.name = var1.readUTF();
               this.serverName = var1.readUTF();
            }

            short var4;
            if (var3 >= 4) {
               if ((var2 & 64) != 0) {
                  this.applicationName = var1.readUTF();
               }

               if ((var2 & 128) != 0) {
                  this.moduleName = var1.readUTF();
               }

               var4 = var1.readByte();
               if (var4 > 0) {
                  this.safExportAllowed = new String[var4];

                  for(int var5 = 0; var5 < var4; ++var5) {
                     this.safExportAllowed[var5] = var1.readUTF();
                  }
               }
            }

            if ((var2 & 8192) != 0) {
               this.backEndId = new JMSServerId();
               this.backEndId.readExternal(var1);
               this.destinationId = new JMSID();
               this.destinationId.readExternal(var1);
            }

            if ((var2 & 16384) != 0) {
               this.multicastAddress = var1.readUTF();
               this.port = var1.readInt();
            }

            if ((var2 & '耀') != 0) {
               this.dispatcherId = new DispatcherId();
               this.dispatcherId.readExternal(var1);
            }

            if ((var2 & 16) != 0) {
               this.referenceName = var1.readUTF();
            }

            var4 = 0;
            if (var3 >= 5) {
               var4 = var1.readShort();
            }

            if ((var4 & 1) != 0) {
               this.persistentStoreName = var1.readUTF();
            }

         }
      }
   }

   private void readExternalVersion1(ObjectInput var1, byte var2) throws IOException, ClassNotFoundException {
      this.type = var2;
      this.generation = 1L;
      this.name = var1.readUTF();
      this.serverName = var1.readUTF();
      if (var1.readBoolean()) {
         this.backEndId = new JMSServerId();
         this.backEndId.readExternal(var1);
      }

      if (var1.readBoolean()) {
         this.destinationId = new JMSID();
         this.destinationId.readExternal(var1);
      }

      if (var1.readBoolean()) {
         this.multicastAddress = var1.readUTF();
         this.port = var1.readInt();
      }

   }

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof DestinationImpl) {
         DestinationImpl var2 = (DestinationImpl)var1;
         if (var2 == this) {
            return true;
         } else if (this.type != var2.type) {
            return false;
         } else if (this.destinationId == null) {
            return false;
         } else {
            return !this.name.equals(var2.name) ? false : this.destinationId.equals(var2.destinationId);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.getId() != null ? this.getId().hashCode() : super.hashCode();
   }

   public final void delete() throws javax.jms.JMSException {
      if (this.connection == null) {
         throw new JMSException(JMSClientExceptionLogger.logInvalidTemporaryDestinationLoggable().getMessage());
      } else {
         this.connection.destroyTemporaryDestination(this.backEndId, this.destinationId);
      }
   }

   public final void setConnection(JMSConnection var1) {
      this.connection = var1;
   }

   public final JMSConnection getConnection() {
      return this.connection;
   }

   public final void setMulticastAddress(String var1) {
      this.multicastAddress = var1;
   }

   public final String getMulticastAddress() {
      return this.multicastAddress;
   }

   public final int getPort() {
      return this.port;
   }

   public final void setPort(int var1) {
      this.port = var1;
   }

   public final synchronized long getGeneration() {
      return this.generation;
   }
}
