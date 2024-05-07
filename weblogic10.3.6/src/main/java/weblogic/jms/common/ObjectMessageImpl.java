package weblogic.jms.common;

import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import javax.jms.ObjectMessage;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.JMSClientExceptionLogger;
import weblogic.kernel.KernelStatus;
import weblogic.store.PersistentStoreException;
import weblogic.store.TestStoreException;
import weblogic.utils.io.Replacer;

public final class ObjectMessageImpl extends MessageImpl implements ObjectMessage, Externalizable, TestStoreException {
   private static final byte EXTVERSION = 1;
   private static final byte EXTVERSION2 = 2;
   private static final byte VERSIONMASK = 127;
   static final long serialVersionUID = -1035306457762201546L;
   private transient Serializable object;
   PayloadStream payload;
   private static final Replacer REPLACER = getReplacer();
   private static final String REMOTE_OBJECT_REPLACER_CLASS_NAME = "weblogic.rmi.utils.io.RemoteObjectReplacer";
   private static boolean testStoreExceptionEnabled;
   public static final String DEBUG_STORE_PROPERTY = "JMS_BEA_DEBUG_STORE_EXCEPTION";

   public ObjectMessageImpl() {
   }

   public ObjectMessageImpl(ObjectMessage var1) throws javax.jms.JMSException {
      this(var1, (javax.jms.Destination)null, (javax.jms.Destination)null);
   }

   public ObjectMessageImpl(ObjectMessage var1, javax.jms.Destination var2, javax.jms.Destination var3) throws javax.jms.JMSException {
      super(var1, var2, var3);
      this.setObject(var1.getObject());
   }

   public byte getType() {
      return 4;
   }

   public static boolean isTestStoreExceptionEnabled() {
      return testStoreExceptionEnabled;
   }

   public void setObject(Serializable var1) throws javax.jms.JMSException {
      this.setObject(var1, (PeerInfo)null);
   }

   public void setObject(Serializable var1, PeerInfo var2) throws javax.jms.JMSException {
      this.writeMode();

      try {
         BufferOutputStream var3 = PayloadFactoryImpl.createOutputStream();
         Object var4 = null;
         if (var2 == null) {
            var4 = new ObjectOutputStream2(var3);
         } else {
            var4 = new ObjectOutputStreamPeerInfoable(var3, var2);
         }

         if (!((ObjectOutputStream2)var4).canReplace()) {
            var1 = (Serializable)((ObjectOutputStream2)var4).replaceObject(var1);
         }

         ((ObjectOutputStream2)var4).writeObject(var1);
         ((ObjectOutputStream2)var4).flush();
         this.payload = (PayloadStream)var3.moveToPayload();
         this.object = null;
      } catch (Exception var5) {
         throw new JMSException(JMSClientExceptionLogger.logSerializationErrorLoggable().getMessage(), var5);
      }
   }

   public Serializable getObject() throws javax.jms.JMSException {
      this.decompressMessageBody();
      if (this.object == null && this.payload != null) {
         try {
            ObjectInputStream2 var1 = new ObjectInputStream2(this.payload.getInputStream());
            this.object = (Serializable)var1.readObject();
            if (!var1.canResolve()) {
               this.object = (Serializable)var1.resolveObject(this.object);
            }
         } catch (IOException var2) {
            throw new JMSException(JMSClientExceptionLogger.logDeserializeIOLoggable().getMessage(), var2);
         } catch (ClassNotFoundException var3) {
            throw new JMSException(JMSClientExceptionLogger.logDeserializeCNFELoggable().getMessage(), var3);
         }
      }

      return this.object;
   }

   public void nullBody() {
      this.object = null;
      this.payload = null;
   }

   public String toString() {
      try {
         return "ObjectMessage[" + this.getJMSMessageID() + "," + this.getObject() + "]";
      } catch (javax.jms.JMSException var2) {
         return "ObjectMessage[" + this.getJMSMessageID() + "]";
      }
   }

   private static Replacer getReplacer() {
      try {
         Class var0 = Class.forName("weblogic.rmi.utils.io.RemoteObjectReplacer");
         Method var1 = var0.getMethod("getReplacer");
         return (Replacer)var1.invoke((Object)null);
      } catch (ClassNotFoundException var2) {
         return null;
      } catch (NoSuchMethodException var3) {
         throw new AssertionError(var3);
      } catch (IllegalAccessException var4) {
         throw new AssertionError(var4);
      } catch (InvocationTargetException var5) {
         throw new AssertionError(var5);
      }
   }

   public PersistentStoreException getTestException() {
      try {
         if (isTestStoreExceptionEnabled() && this.propertyExists("JMS_BEA_DEBUG_STORE_EXCEPTION")) {
            Serializable var1 = this.getObject();
            if (var1 instanceof PersistentStoreException) {
               return (PersistentStoreException)var1;
            }
         }
      } catch (javax.jms.JMSException var2) {
      }

      return null;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      int var3 = Integer.MAX_VALUE;
      ObjectOutput var2;
      if (var1 instanceof MessageImpl.JMSObjectOutputWrapper) {
         var3 = ((MessageImpl.JMSObjectOutputWrapper)var1).getCompressionThreshold();
         var2 = ((MessageImpl.JMSObjectOutputWrapper)var1).getInnerObjectOutput();
      } else {
         var2 = var1;
      }

      if (!this.isCompressed() && this.payload == null) {
         BufferOutputStream var5 = PayloadFactoryImpl.createOutputStream();
         ObjectOutputStream2 var6 = new ObjectOutputStream2(var5);
         var6.writeObject(this.object);
         var6.flush();
         this.payload = (PayloadStream)var5.moveToPayload();
      }

      byte var4;
      if (this.getVersion(var2) >= 30) {
         var4 = (byte)(2 | (this.shouldCompress(var2, var3) ? -128 : 0));
      } else {
         var4 = 1;
      }

      var2.writeByte(var4);
      if (this.isCompressed()) {
         if (var4 == 1) {
            PayloadStream var7 = (PayloadStream)this.decompress();
            var7.writeLengthAndData(var2);
         } else {
            this.flushCompressedMessageBody(var2);
         }
      } else if ((var4 & -128) != 0) {
         this.writeExternalCompressPayload(var2, this.payload);
      } else {
         this.payload.writeLengthAndData(var2);
      }

   }

   public final void decompressMessageBody() throws javax.jms.JMSException {
      if (this.isCompressed()) {
         try {
            this.payload = (PayloadStream)this.decompress();
         } catch (IOException var6) {
            throw new JMSException(JMSClientExceptionLogger.logErrorDecompressMessageBodyLoggable().getMessage(), var6);
         } finally {
            this.cleanupCompressedMessageBody();
         }

      }
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      byte var2 = var1.readByte();
      byte var3 = (byte)(var2 & 127);
      if (var3 >= 1 && var3 <= 2) {
         if ((var2 & -128) != 0) {
            this.readExternalCompressedMessageBody(var1);
         } else {
            this.payload = (PayloadStream)PayloadFactoryImpl.createPayload((InputStream)var1);
         }

      } else {
         throw JMSUtilities.versionIOException(var3, 1, 2);
      }
   }

   public MessageImpl copy() throws javax.jms.JMSException {
      ObjectMessageImpl var1 = new ObjectMessageImpl();
      this.copy(var1);

      try {
         if (this.payload == null && this.object != null) {
            BufferOutputStream var2 = PayloadFactoryImpl.createOutputStream();
            ObjectOutputStream2 var3 = new ObjectOutputStream2(var2);
            var3.writeObject(this.object);
            var3.flush();
            var1.payload = (PayloadStream)var2.moveToPayload();
         } else if (this.payload != null) {
            var1.payload = this.payload.copyPayloadWithoutSharedStream();
            var1.payloadCopyOnWrite = this.payloadCopyOnWrite = true;
         } else {
            var1.payload = null;
         }

         var1.object = null;
      } catch (IOException var4) {
      }

      var1.setBodyWritable(false);
      var1.setPropertiesWritable(false);
      return var1;
   }

   public long getPayloadSize() {
      if (this.isCompressed()) {
         return (long)this.getCompressedMessageBodySize();
      } else if (super.bodySize != -1L) {
         return super.bodySize;
      } else {
         return this.payload != null ? (super.bodySize = (long)this.payload.getLength()) : (super.bodySize = 0L);
      }
   }

   public PayloadStream getPayload() {
      return this.payload;
   }

   public byte[] getBodyBytes() throws javax.jms.JMSException {
      if (this.payload != null) {
         PayloadStream var1 = this.payload;

         try {
            ByteArrayOutputStream var2 = new ByteArrayOutputStream();
            var1.writeTo(var2);
            var2.flush();
            return var2.toByteArray();
         } catch (IOException var3) {
            throw new JMSException(var3);
         }
      } else {
         return new byte[0];
      }
   }

   public void setBodyBytes(PayloadStream var1) {
      this.payload = var1;
      this.object = null;
   }

   public PayloadStream getMessageBody() throws javax.jms.JMSException {
      if (!this.isCompressed()) {
         return this.payload;
      } else {
         try {
            return (PayloadStream)this.decompress();
         } catch (IOException var2) {
            throw new JMSException(JMSClientExceptionLogger.logErrorDecompressMessageBodyLoggable().getMessage(), var2);
         }
      }
   }

   static {
      try {
         String var0 = System.getProperty("weblogic.store.qa.StoreTest");
         if (var0 != null) {
            String var1 = "true";
            testStoreExceptionEnabled = var1.equalsIgnoreCase(var0);
         }
      } catch (Throwable var2) {
         var2.printStackTrace();
      }

   }

   final class ObjectOutputStreamPeerInfoable extends ObjectOutputStream2 implements PeerInfoable {
      private PeerInfo peerInfo;

      ObjectOutputStreamPeerInfoable(OutputStream var2, PeerInfo var3) throws IOException, StreamCorruptedException {
         super(var2);
         this.peerInfo = var3;
      }

      public PeerInfo getPeerInfo() {
         return this.peerInfo;
      }
   }

   class ObjectOutputStream2 extends ObjectOutputStream implements WLObjectOutput {
      private boolean canReplace = true;

      ObjectOutputStream2(OutputStream var2) throws IOException, StreamCorruptedException {
         super(var2);
         if (KernelStatus.isApplet()) {
            this.canReplace = false;
         } else {
            this.enableReplaceObject(true);
         }

      }

      boolean canReplace() {
         return this.canReplace;
      }

      protected Object replaceObject(Object var1) throws IOException {
         return ObjectMessageImpl.REPLACER != null ? ObjectMessageImpl.REPLACER.replaceObject(var1) : var1;
      }

      public final void writeObjectWL(Object var1) throws IOException {
         this.writeObject(var1);
      }

      public final void writeString(String var1) throws IOException {
         this.writeObject(var1);
      }

      public final void writeDate(Date var1) throws IOException {
         this.writeObject(var1);
      }

      public final void writeArrayList(ArrayList var1) throws IOException {
         this.writeObject(var1);
      }

      public final void writeProperties(Properties var1) throws IOException {
         this.writeObject(var1);
      }

      public final void writeBytes(byte[] var1) throws IOException {
         this.writeObject(var1);
      }

      public final void writeBytes(byte[] var1, int var2, int var3) throws IOException {
         byte[] var4 = new byte[var3];
         System.arraycopy(var1, var2, var4, 0, var3);
         this.writeObject(var4);
      }

      public final void writeArrayOfObjects(Object[] var1) throws IOException {
         this.writeObject(var1);
      }

      public final void writeAbbrevString(String var1) throws IOException {
         this.writeObject(var1);
      }

      public final void writeImmutable(Object var1) throws IOException {
         this.writeObject(var1);
      }
   }

   final class ObjectInputStream2 extends ObjectInputStream implements WLObjectInput {
      private boolean canResolve = true;

      ObjectInputStream2(InputStream var2) throws IOException, StreamCorruptedException {
         super(var2);
         if (KernelStatus.isApplet()) {
            this.canResolve = false;
         } else {
            this.enableResolveObject(true);
         }

      }

      boolean canResolve() {
         return this.canResolve;
      }

      protected Class resolveClass(ObjectStreamClass var1) throws IOException, ClassNotFoundException {
         if (KernelStatus.isApplet()) {
            return Class.forName(var1.getName());
         } else {
            ClassLoader var2 = Thread.currentThread().getContextClassLoader();

            try {
               return Class.forName(var1.getName(), true, var2);
            } catch (ClassNotFoundException var4) {
               return super.resolveClass(var1);
            }
         }
      }

      protected Object resolveObject(Object var1) throws IOException {
         return ObjectMessageImpl.REPLACER != null ? ObjectMessageImpl.REPLACER.resolveObject(var1) : var1;
      }

      public final Object readObjectWL() throws IOException, ClassNotFoundException {
         return this.readObject();
      }

      public final String readString() throws IOException {
         try {
            return (String)this.readObject();
         } catch (ClassNotFoundException var2) {
            throw new IOException(var2.toString());
         }
      }

      public final Date readDate() throws IOException {
         try {
            return (Date)this.readObject();
         } catch (ClassNotFoundException var2) {
            throw new IOException(var2.toString());
         }
      }

      public final ArrayList readArrayList() throws IOException, ClassNotFoundException {
         return (ArrayList)this.readObject();
      }

      public final Properties readProperties() throws IOException {
         try {
            return (Properties)this.readObject();
         } catch (ClassNotFoundException var2) {
            throw new IOException(var2.toString());
         }
      }

      public final byte[] readBytes() throws IOException {
         try {
            return (byte[])((byte[])this.readObject());
         } catch (ClassNotFoundException var2) {
            throw new IOException(var2.toString());
         }
      }

      public final Object[] readArrayOfObjects() throws IOException, ClassNotFoundException {
         return (Object[])((Object[])this.readObject());
      }

      public final String readAbbrevString() throws IOException {
         try {
            return (String)this.readObject();
         } catch (ClassNotFoundException var2) {
            throw new IOException(var2.toString());
         }
      }

      public final Object readImmutable() throws IOException, ClassNotFoundException {
         return this.readObject();
      }
   }
}
