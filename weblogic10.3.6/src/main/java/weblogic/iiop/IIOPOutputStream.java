package weblogic.iiop;

import java.io.Externalizable;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.rmi.Remote;
import javax.management.MBeanInfo;
import javax.rmi.CORBA.Util;
import javax.rmi.CORBA.ValueHandler;
import org.omg.CORBA.Any;
import org.omg.CORBA.CODESET_INCOMPATIBLE;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Principal;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.BoxedValueHelper;
import org.omg.CORBA.portable.CustomValue;
import org.omg.CORBA.portable.IDLEntity;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.InvokeHandler;
import org.omg.CORBA.portable.Streamable;
import org.omg.CORBA.portable.StreamableValue;
import org.omg.CORBA.portable.ValueBase;
import org.omg.CORBA.portable.ValueOutputStream;
import org.omg.CORBA_2_3.portable.OutputStream;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.corba.idl.TypeCodeImpl;
import weblogic.corba.utils.ClassInfo;
import weblogic.corba.utils.IndirectionHashtable;
import weblogic.corba.utils.IndirectionValueHashtable;
import weblogic.corba.utils.MarshalledObject;
import weblogic.corba.utils.RepositoryId;
import weblogic.corba.utils.ValueHandlerImpl;
import weblogic.ejb.spi.EJBPortableReplacer;
import weblogic.iiop.spi.MessageStream;
import weblogic.protocol.AsyncOutgoingMessage;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelStream;
import weblogic.rmi.spi.MsgOutput;
import weblogic.rmi.utils.io.Codebase;
import weblogic.rmi.utils.io.RemoteObjectReplacer;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.acl.internal.AuthenticatedUser;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.Hex;
import weblogic.utils.collections.Pool;
import weblogic.utils.collections.StackPool;
import weblogic.utils.io.Chunk;
import weblogic.utils.io.ChunkOutput;
import weblogic.utils.io.ObjectOutput;
import weblogic.utils.io.ObjectStreamClass;
import weblogic.utils.io.StringOutput;

public final class IIOPOutputStream extends OutputStream implements AsyncOutgoingMessage, ObjectOutput, PeerInfoable, ServerChannelStream, StringOutput, ValueOutputStream, ChunkOutput, MsgOutput, MessageStream {
   private static final boolean ASSERT = false;
   private static final boolean debugValueTypes = false;
   private static boolean USE_JAVA_SERIALIZATION_FOR_JMX = true;
   private static final boolean DEBUG = false;
   private static final int VT_NON_CHUNKING = 2147483394;
   private static final int VT_CHUNKING = 2147483402;
   private static final int VT_INDIRECTION = -1;
   private static final boolean SUPPORT_CHUNKING = true;
   private static final String EXTRACT_METHOD = "extract";
   private static final byte TRUE = 1;
   private static final byte FALSE = 0;
   private Chunk head;
   private Chunk current;
   private int currentPos;
   private int chunkPos;
   private Chunk queued;
   private int queuedLength;
   private final Marker[] markers;
   private int minorVersion;
   private boolean SUPPORT_JDK_13_CHUNKING;
   private int nestingLevel;
   private boolean nesting;
   private ORB orb;
   private int needLongAlignment;
   private boolean needEightByteAlignment;
   static final long HANDLE_ENDIAN_MASK = -2147483648L;
   static final long HANDLE_ALIGN_MASK = 1073741824L;
   static final long HANDLE_CHUNK_MASK = 536870912L;
   static final long HANDLE_NESTING_MASK = 268435456L;
   private static final int CHAR_BUF_SIZE = 256;
   private final char[] cbuf;
   private static final ValueHandler valueHandler;
   private boolean littleEndian;
   private EndPoint endPoint;
   private int char_codeset;
   private int wchar_codeset;
   private java.io.ObjectOutput ooutput;
   private ObjectOutputStream objectStream;
   private IndirectionHashtable encapsulations;
   private IndirectionValueHashtable indirections;
   private IndirectionValueHashtable tcIndirections;
   private int tcNestingLevel;
   private weblogic.iiop.spi.Message message;
   private boolean useJavaSerialization;
   private byte maxFormatVersion;
   private static final int MAP_POOL_SIZE = 1024;
   private static final Pool mapPool;
   private boolean chunking;
   private int currentChunk;
   private int currentChunkPos;
   private Chunk currentChunkChunk;
   private int endTag;
   private int lastEnd;
   private boolean writingObjectKey;

   public IIOPOutputStream() {
      this(false, (EndPoint)null, (weblogic.iiop.spi.Message)null);
   }

   public IIOPOutputStream(ORB var1) {
      this();
      this.orb = var1;
   }

   public IIOPOutputStream(boolean var1, EndPoint var2) {
      this(var1, var2, (weblogic.iiop.spi.Message)null);
   }

   public IIOPOutputStream(boolean var1, EndPoint var2, weblogic.iiop.spi.Message var3) {
      this.currentPos = 0;
      this.chunkPos = 0;
      this.markers = new Marker[]{new Marker(), new Marker()};
      this.minorVersion = 2;
      this.SUPPORT_JDK_13_CHUNKING = false;
      this.nestingLevel = 0;
      this.nesting = true;
      this.orb = null;
      this.needLongAlignment = 0;
      this.needEightByteAlignment = false;
      this.cbuf = new char[256];
      this.char_codeset = CodeSet.getDefaultCharCodeSet();
      this.wchar_codeset = CodeSet.getDefaultWcharCodeSet();
      this.encapsulations = null;
      this.indirections = null;
      this.tcIndirections = null;
      this.tcNestingLevel = 0;
      this.useJavaSerialization = false;
      this.maxFormatVersion = 1;
      this.chunking = false;
      this.currentChunk = 0;
      this.currentChunkPos = 0;
      this.endTag = 0;
      this.lastEnd = 0;
      this.writingObjectKey = false;
      this.littleEndian = var1;
      this.endPoint = var2;
      this.message = var3;
      if (var2 != null) {
         this.wchar_codeset = var2.getWcharCodeSet();
         this.char_codeset = var2.getCharCodeSet();
         this.useJavaSerialization = var2.getServerChannel().getUseFastSerialization();
      }

      if (var3 != null) {
         this.minorVersion = var3.getMinorVersion();
         this.maxFormatVersion = var3.getMaxStreamFormatVersion();
      }

      this.orb = weblogic.corba.orb.ORB.getInstance();
      this.head = this.current = Chunk.getChunk();
   }

   public void setMaxStreamFormatVersion(byte var1) {
      this.maxFormatVersion = var1;
   }

   public byte getMaxStreamFormatVersion() {
      return this.maxFormatVersion;
   }

   public long startEncapsulation() {
      return this.startEncapsulation(false, true, this.nesting);
   }

   long startEncapsulationNoEndian() {
      return this.startEncapsulation(false, false, this.nesting);
   }

   long startEncapsulationNoNesting() {
      return this.startEncapsulation(false, true, false);
   }

   private long startEncapsulation(boolean var1, boolean var2, boolean var3) {
      this.write_long(0);
      int var4 = this.pos();
      long var5 = (long)var4;
      Debug.assertion((var5 & -1073741824L) == 0L);
      if (this.littleEndian) {
         var5 |= -2147483648L;
      }

      if (this.needLongAlignment > 0) {
         var5 |= 1073741824L;
      }

      if (this.nesting) {
         ++this.nestingLevel;
         var5 |= 268435456L;
      }

      this.needLongAlignment = this.chunkPos % 8 != 0 ? 4 : 0;
      if (this.chunking) {
         var5 |= 536870912L;
         if (this.encapsulations == null) {
            this.encapsulations = IIOPInputStream.getHashMap();
         }

         this.encapsulations.put(var4, new IIOPInputStream.EncapsulationWrapper(this.lastEnd, this.chunking, this.currentChunk, this.endTag));
         this.chunking = false;
         this.currentChunk = 0;
         this.currentChunkChunk = null;
         this.currentChunkPos = 0;
         this.endTag = 0;
         this.lastEnd = 0;
      }

      this.littleEndian = var1;
      this.nesting = var3;
      if (var2) {
         this.putEndian();
      }

      return var5;
   }

   public void endEncapsulation(long var1) {
      this.littleEndian = (var1 & -2147483648L) != 0L;
      this.nesting = (var1 & 268435456L) != 0L;
      if (this.nesting) {
         --this.nestingLevel;
      }

      this.needLongAlignment = (var1 & 1073741824L) != 0L ? 4 : 0;
      boolean var3 = (var1 & 536870912L) != 0L;
      var1 &= 268435455L;
      if (var3) {
         IIOPInputStream.EncapsulationWrapper var4 = (IIOPInputStream.EncapsulationWrapper)this.encapsulations.remove((int)var1);
         if (var4 == null) {
            throw new MARSHAL("No encapsulation information at: " + this.pos());
         }

         this.chunking = var4.chunking;
         this.endTag = var4.endTag;
         this.currentChunk = var4.chunkLength;
         this.currentChunkChunk = null;
         this.currentChunkPos = 0;
         this.lastEnd = var4.encapLength;
      } else {
         this.chunking = false;
      }

      long var6 = (long)this.pos();
      this.setPosition((int)(var1 - 4L));
      this.write_long((int)(var6 - var1));
      this.setPosition((int)var6);
   }

   public final void write_octet(byte var1) {
      if (this.needEightByteAlignment) {
         this.checkEightByteAlignment();
      }

      if (this.chunking && this.currentChunk == 0) {
         this.startChunk();
      }

      this.advance();
      this.current.buf[this.chunkPos++] = var1;
   }

   private final void align(int var1) {
      if (this.needEightByteAlignment) {
         this.checkEightByteAlignment();
      }

      if (this.chunking && this.currentChunk == 0) {
         this.startChunk();
      }

      for(int var2 = this.chunkPos + this.needLongAlignment; var2 % var1 != 0; ++var2) {
         this.advance();
         this.current.buf[this.chunkPos++] = 0;
      }

   }

   public final void align8() {
      this.align(8);
   }

   public final void setNeedEightByteAlignment() {
      this.needEightByteAlignment = true;
   }

   public final void checkEightByteAlignment() {
      this.needEightByteAlignment = false;
      this.align(8);
   }

   public final void putEndian() {
      if (this.littleEndian) {
         this.write_octet((byte)1);
      } else {
         this.write_octet((byte)0);
      }

   }

   public void startUnboundedEncapsulation() {
      this.putEndian();
      ++this.nestingLevel;
   }

   void reset() {
      Chunk var1 = this.head;
      this.head = this.head.next;
      this.close();
      this.head = var1;
      this.head.next = null;
      this.head.end = 0;
      this.chunkPos = 0;
      this.current = this.head;
      this.currentPos = 0;
      this.nestingLevel = 0;
      this.nesting = true;
      this.needLongAlignment = 0;
      this.needEightByteAlignment = false;
   }

   public String dumpBuf() {
      byte[] var1 = this.getBuffer();
      return Hex.dump(var1, 0, var1.length);
   }

   public final int getMinorVersion() {
      return this.nestingLevel <= 0 || this.SUPPORT_JDK_13_CHUNKING && !this.writingObjectKey ? this.minorVersion : 2;
   }

   private final boolean useCompliantChunking() {
      return !this.SUPPORT_JDK_13_CHUNKING || this.nestingLevel != 0 && this.writingObjectKey;
   }

   public final void setMinorVersion(int var1) {
      this.minorVersion = var1;
      this.SUPPORT_JDK_13_CHUNKING = var1 < 2;
   }

   public java.io.ObjectOutput getObjectOutput(boolean var1) {
      if (!var1) {
         return this;
      } else {
         if (this.ooutput == null) {
            this.ooutput = new IDLMsgOutput(this);
         }

         return this.ooutput;
      }
   }

   public ObjectOutputStream getObjectOutputStream(Object var1, ObjectStreamClass var2, byte var3) throws IOException {
      if (this.objectStream == null) {
         this.objectStream = new ObjectOutputStreamImpl(this, var1, var2, var3);
      } else {
         ((ObjectOutputStreamImpl)this.objectStream).pushCurrent(var1, var2, var3);
      }

      return this.objectStream;
   }

   public PeerInfo getPeerInfo() {
      return this.endPoint != null && this.endPoint.getPeerInfo() != null ? this.endPoint.getPeerInfo() : PeerInfo.getPeerInfo();
   }

   public void setCodeSets(int var1, int var2) {
      this.char_codeset = var1;
      this.wchar_codeset = var2;
   }

   private final int getWcharCodeSet() {
      return this.nestingLevel > 0 && !this.SUPPORT_JDK_13_CHUNKING && !this.writingObjectKey ? 65801 : this.wchar_codeset;
   }

   private static final int min(int var0, int var1) {
      return var0 <= var1 ? var0 : var1;
   }

   private static final int max(int var0, int var1) {
      return var0 >= var1 ? var0 : var1;
   }

   private final void setMark(int var1) {
      this.markers[var1].chunk = this.current;
      this.markers[var1].pos = this.chunkPos;
      this.markers[var1].currentPos = this.currentPos;
   }

   final void setMark(Marker var1) {
      var1.chunk = this.current;
      var1.pos = this.chunkPos;
      var1.currentPos = this.currentPos;
   }

   private final void restoreMark(int var1) {
      this.current = this.markers[var1].chunk;
      this.chunkPos = this.markers[var1].pos;
      this.currentPos = this.markers[var1].currentPos;
   }

   final void restoreMark(Marker var1) {
      this.current = var1.chunk;
      this.chunkPos = var1.pos;
      this.currentPos = var1.currentPos;
   }

   private final void setPosition(int var1) {
      if (this.currentPos <= var1 && var1 - this.currentPos < Chunk.CHUNK_SIZE) {
         this.chunkPos = var1 - this.currentPos;
      } else {
         this.current = this.head;

         for(this.currentPos = 0; var1 > Chunk.CHUNK_SIZE; this.current = this.current.next) {
            if (this.current.next == null) {
               this.current.next = Chunk.getChunk();
            }

            var1 -= this.current.end;
            this.currentPos += this.current.end;
         }

         this.chunkPos = var1;
      }

   }

   private final int setLength() {
      this.current.end = this.chunkPos;
      int var1 = this.getSize();
      this.setPosition(8);
      this.write_long(var1 - 12);
      return var1;
   }

   public final void writeTo(java.io.OutputStream var1) throws IOException {
      if (this.queued == null) {
         if (this.current == null || this.head == null) {
            throw new IOException("writeTo() called on closed stream");
         }

         this.enqueue();
      }

      while(this.queued != null) {
         if (this.queued.end > 0) {
            var1.write(this.queued.buf, 0, this.queued.end);
         }

         Chunk var2 = this.queued;
         this.queued = this.queued.next;
         Chunk.releaseChunk(var2);
      }

      this.queuedLength = 0;
   }

   public Chunk getChunks() {
      return this.queued != null ? this.queued : this.head;
   }

   public void cleanup() {
      while(this.queued != null) {
         Chunk var1 = this.queued.next;
         Chunk.releaseChunk(this.queued);
         this.queued = var1;
      }

   }

   public final void enqueue() {
      if (this.queued == null) {
         this.queuedLength = this.setLength();
         this.queued = this.head;
         this.head = null;
         this.close();
      }

   }

   public final int getLength() throws IOException {
      return this.queued != null ? this.queuedLength : this.getSize();
   }

   public int pos() {
      return this.currentPos + this.chunkPos;
   }

   public byte[] getBuffer() {
      this.current.end = this.chunkPos;
      byte[] var1 = new byte[this.getSize()];
      Chunk var2 = this.head;

      for(int var3 = 0; var2 != null; var2 = var2.next) {
         System.arraycopy(var2.buf, 0, var1, var3, var2.end);
         var3 += var2.end;
      }

      return var1;
   }

   byte[] getBufferFromMark(Marker var1) {
      this.current.end = this.chunkPos;
      byte[] var2 = new byte[this.currentPos + this.chunkPos - (var1.currentPos + var1.pos)];
      int var3 = var1.chunk.end - var1.pos;
      System.arraycopy(var1.chunk.buf, var1.pos, var2, 0, var3);

      for(Chunk var4 = var1.chunk.next; var4 != null; var4 = var4.next) {
         System.arraycopy(var4.buf, 0, var2, var3, var4.end);
         var3 += var4.end;
      }

      return var2;
   }

   public byte[] getBufferToWrite() {
      if (this.queued == null) {
         this.enqueue();
      }

      byte[] var1 = new byte[this.queuedLength];
      int var2 = 0;

      while(this.queued != null) {
         System.arraycopy(this.queued.buf, 0, var1, var2, this.queued.end);
         var2 += this.queued.end;
         Chunk var3 = this.queued;
         this.queued = this.queued.next;
         Chunk.releaseChunk(var3);
      }

      this.queuedLength = 0;
      return var1;
   }

   public void close() {
      while(this.head != null) {
         Chunk var1 = this.head.next;
         Chunk.releaseChunk(this.head);
         this.head = var1;
      }

      releaseHashMap(this.indirections);
      this.indirections = null;
      releaseHashMap(this.tcIndirections);
      this.tcIndirections = null;
      IIOPInputStream.releaseHashMap(this.encapsulations);
      this.encapsulations = null;
      this.current = null;
      this.message = null;
   }

   private final int getSize() {
      Chunk var1 = this.head;

      int var2;
      for(var2 = 0; var1 != this.current; var1 = var1.next) {
         var2 += var1.end;
      }

      return var2 + this.chunkPos;
   }

   boolean isSecure() {
      return this.endPoint != null ? this.endPoint.isSecure() : false;
   }

   public EndPoint getEndPoint() {
      return this.endPoint;
   }

   public ServerChannel getServerChannel() {
      return this.endPoint != null ? this.endPoint.getServerChannel() : null;
   }

   public InputStream create_input_stream() {
      this.current.end = this.chunkPos;
      return this.createInputStream(this.head);
   }

   public IIOPInputStream createExactInputStream() {
      this.current.end = this.chunkPos;
      Chunk var1 = new Chunk(this.getSize());
      var1.end = var1.buf.length;
      int var2 = 0;

      while(this.head != null) {
         System.arraycopy(this.head.buf, 0, var1.buf, var2, this.head.end);
         var2 += this.head.end;
         Chunk var3 = this.head;
         this.head = this.head.next;
         Chunk.releaseChunk(var3);
      }

      return this.createInputStream(var1);
   }

   private IIOPInputStream createInputStream(Chunk var1) {
      IIOPInputStream var2 = new IIOPInputStream(var1, this.isSecure(), this.endPoint);
      var2.setCodeSets(this.char_codeset, this.wchar_codeset);
      var2.setEndian(this.littleEndian);
      var2.setORB(this.orb);
      var2.recordStart();
      this.head = this.current = null;
      this.close();
      return var2;
   }

   public void write_boolean(boolean var1) {
      this.write_octet((byte)(var1 ? 1 : 0));
   }

   public void write_char(char var1) {
      this.write_octet((byte)(var1 & 255));
   }

   public void write_wchar(char var1) {
      label28:
      switch (this.getWcharCodeSet()) {
         case 65792:
            switch (this.getMinorVersion()) {
               case 0:
               case 1:
                  this.align(2);
                  break label28;
               case 2:
                  this.write_octet((byte)2);
               default:
                  break label28;
            }
         case 65801:
            switch (this.getMinorVersion()) {
               case 0:
               case 1:
                  this.align(2);
                  break label28;
               case 2:
                  this.write_octet((byte)4);
                  if (this.littleEndian) {
                     this.write_octet((byte)-1);
                     this.write_octet((byte)-2);
                  } else {
                     this.write_octet((byte)-2);
                     this.write_octet((byte)-1);
                  }
               default:
                  break label28;
            }
         case 83951617:
            this.write_UTF8wchar(var1);
            return;
      }

      if (this.littleEndian) {
         this.write_octet((byte)var1);
         this.write_octet((byte)(var1 >>> 8));
      } else {
         this.write_octet((byte)(var1 >>> 8));
         this.write_octet((byte)var1);
      }

   }

   private final void advance() {
      if (this.chunkPos == Chunk.CHUNK_SIZE) {
         if (this.current.next == null) {
            this.current.next = Chunk.getChunk();
         }

         this.current.end = this.chunkPos;
         this.currentPos += this.chunkPos;
         this.current = this.current.next;
         this.chunkPos = 0;
      }

   }

   public void write_unsigned_short(int var1) {
      this.align(2);
      if (this.chunkPos + 2 <= Chunk.CHUNK_SIZE) {
         if (this.littleEndian) {
            this.current.buf[this.chunkPos++] = (byte)var1;
            this.current.buf[this.chunkPos++] = (byte)(var1 >>> 8);
         } else {
            this.current.buf[this.chunkPos++] = (byte)(var1 >>> 8);
            this.current.buf[this.chunkPos++] = (byte)var1;
         }
      } else if (this.littleEndian) {
         this.write_octet((byte)(var1 & 255));
         this.write_octet((byte)(var1 >>> 8));
      } else {
         this.write_octet((byte)(var1 >>> 8));
         this.write_octet((byte)var1);
      }

   }

   public void write_ushort(short var1) {
      this.write_unsigned_short(var1);
   }

   public void write_short(short var1) {
      this.write_unsigned_short(var1);
   }

   public final void write_long(int var1) {
      this.align(4);
      if (this.chunkPos + 4 <= Chunk.CHUNK_SIZE && !this.littleEndian) {
         this.current.buf[this.chunkPos++] = (byte)(var1 >>> 24);
         this.current.buf[this.chunkPos++] = (byte)(var1 >>> 16);
         this.current.buf[this.chunkPos++] = (byte)(var1 >>> 8);
         this.current.buf[this.chunkPos++] = (byte)var1;
      } else {
         this.write_slow_long(var1);
      }

   }

   public final void write_slow_long(int var1) {
      if (this.littleEndian) {
         if (this.chunkPos + 4 <= Chunk.CHUNK_SIZE) {
            this.current.buf[this.chunkPos++] = (byte)var1;
            this.current.buf[this.chunkPos++] = (byte)(var1 >>> 8);
            this.current.buf[this.chunkPos++] = (byte)(var1 >>> 16);
            this.current.buf[this.chunkPos++] = (byte)(var1 >>> 24);
         } else {
            this.write_octet((byte)var1);
            this.write_octet((byte)(var1 >>> 8));
            this.write_octet((byte)(var1 >>> 16));
            this.write_octet((byte)(var1 >>> 24));
         }
      } else {
         this.write_octet((byte)(var1 >>> 24));
         this.write_octet((byte)(var1 >>> 16));
         this.write_octet((byte)(var1 >>> 8));
         this.write_octet((byte)var1);
      }

   }

   public final void write_ulong(int var1) {
      this.write_long(var1);
   }

   public void write_longlong(long var1) {
      this.align(8);
      if (this.chunkPos + 8 <= Chunk.CHUNK_SIZE) {
         if (this.littleEndian) {
            this.current.buf[this.chunkPos++] = (byte)((int)var1);
            this.current.buf[this.chunkPos++] = (byte)((int)(var1 >>> 8));
            this.current.buf[this.chunkPos++] = (byte)((int)(var1 >>> 16));
            this.current.buf[this.chunkPos++] = (byte)((int)(var1 >>> 24));
            this.current.buf[this.chunkPos++] = (byte)((int)(var1 >>> 32));
            this.current.buf[this.chunkPos++] = (byte)((int)(var1 >>> 40));
            this.current.buf[this.chunkPos++] = (byte)((int)(var1 >>> 48));
            this.current.buf[this.chunkPos++] = (byte)((int)(var1 >>> 56));
         } else {
            this.current.buf[this.chunkPos++] = (byte)((int)(var1 >>> 56));
            this.current.buf[this.chunkPos++] = (byte)((int)(var1 >>> 48));
            this.current.buf[this.chunkPos++] = (byte)((int)(var1 >>> 40));
            this.current.buf[this.chunkPos++] = (byte)((int)(var1 >>> 32));
            this.current.buf[this.chunkPos++] = (byte)((int)(var1 >>> 24));
            this.current.buf[this.chunkPos++] = (byte)((int)(var1 >>> 16));
            this.current.buf[this.chunkPos++] = (byte)((int)(var1 >>> 8));
            this.current.buf[this.chunkPos++] = (byte)((int)var1);
         }
      } else if (this.littleEndian) {
         this.write_octet((byte)((int)var1));
         this.write_octet((byte)((int)(var1 >>> 8)));
         this.write_octet((byte)((int)(var1 >>> 16)));
         this.write_octet((byte)((int)(var1 >>> 24)));
         this.write_octet((byte)((int)(var1 >>> 32)));
         this.write_octet((byte)((int)(var1 >>> 40)));
         this.write_octet((byte)((int)(var1 >>> 48)));
         this.write_octet((byte)((int)(var1 >>> 56)));
      } else {
         this.write_octet((byte)((int)(var1 >>> 56)));
         this.write_octet((byte)((int)(var1 >>> 48)));
         this.write_octet((byte)((int)(var1 >>> 40)));
         this.write_octet((byte)((int)(var1 >>> 32)));
         this.write_octet((byte)((int)(var1 >>> 24)));
         this.write_octet((byte)((int)(var1 >>> 16)));
         this.write_octet((byte)((int)(var1 >>> 8)));
         this.write_octet((byte)((int)var1));
      }

   }

   public void write_ulonglong(long var1) {
      this.write_longlong(var1);
   }

   public void write_float(float var1) {
      this.write_long(Float.floatToIntBits(var1));
   }

   public void write_double(double var1) {
      this.write_longlong(Double.doubleToLongBits(var1));
   }

   public void write_string(String var1) {
      if (var1 == null) {
         throw new MARSHAL("null value passed to write_string(String)");
      } else {
         switch (this.char_codeset) {
            case 65537:
            case 65568:
               int var2 = var1.length();
               this.write_ulong(var1.length() + 1);
               this.writeASCII(var1);
               this.write_octet((byte)0);
               break;
            case 83951617:
               this.write_stringUTF8(var1);
               break;
            default:
               throw new CODESET_INCOMPATIBLE("Unknown char codeset");
         }

      }
   }

   public void write_stringUTF8(String var1) {
      int var2 = 0;
      int var3 = var1.length();

      boolean var4;
      int var5;
      for(var4 = true; var2 < var3; var2 += var5) {
         var5 = min(256, var3 - var2);
         var1.getChars(var2, var2 + var5, this.cbuf, 0);

         for(int var6 = 0; var6 < var5; ++var6) {
            char var7 = this.cbuf[var6];
            if ((var7 & 'ﾀ') != 0) {
               var4 = false;
               break;
            }
         }

         if (!var4) {
            break;
         }
      }

      if (!var4) {
         this.writeStringUTF8_1Pass(var1, true);
      } else {
         this.write_ulong(var1.length() + 1);
         this.writeASCII(var1);
         this.write_octet((byte)0);
      }

   }

   public final void write_repository_id(RepositoryId var1) {
      if (var1 == null) {
         this.write_ulong(1);
         this.write_octet((byte)0);
      } else {
         var1.write(this);
      }

   }

   public final void writeRepositoryIdList(RepositoryId[] var1) {
      if (!this.writeIndirectionMaybe(var1)) {
         this.write_long(var1.length);

         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (!this.writeIndirectionMaybe(var1[var2])) {
               this.write_repository_id(var1[var2]);
            }
         }
      }

   }

   private final void writeStringUTF16(String var1, boolean var2) {
      int var3 = var1.length();
      this.write_ulong(var3 == 0 ? var3 : var3 * 2 + (var2 ? 2 : 0));
      if (var3 != 0 && var2) {
         if (this.littleEndian) {
            this.write_octet((byte)-1);
            this.write_octet((byte)-2);
         } else {
            this.write_octet((byte)-2);
            this.write_octet((byte)-1);
         }
      }

      int var4 = 0;

      while(var4 < var3) {
         int var5 = min(var3 - var4, 256);
         var1.getChars(var4, var4 + var5, this.cbuf, 0);
         var4 += var5;
         this.writeCharsUTF16(this.cbuf, var5);
      }

   }

   private final void writeCharsUTF16(char[] var1, int var2) {
      this.align(1);
      int var3 = var2 * 2;
      int var4 = 0;
      byte var5 = 8;
      byte var6 = 0;
      if (this.littleEndian) {
         var6 = 8;
         var5 = 0;
      }

      while(var4 < var2) {
         int var7 = Chunk.CHUNK_SIZE - this.chunkPos;
         if (var7 == 0) {
            this.advance();
         } else if (var7 == 1) {
            this.current.buf[this.chunkPos++] = (byte)(var1[var4] >>> var5);
            this.advance();
            this.current.buf[this.chunkPos++] = (byte)(var1[var4++] >>> var6);
            var3 -= 2;
         }

         var7 = Chunk.CHUNK_SIZE - this.chunkPos;
         var7 -= var7 % 2;

         for(int var8 = min(var7, var3); var8 > 0; var3 -= 2) {
            this.current.buf[this.chunkPos++] = (byte)(var1[var4] >>> var5);
            this.current.buf[this.chunkPos++] = (byte)(var1[var4++] >>> var6);
            var8 -= 2;
         }
      }

   }

   public final void writeUTF8(String var1) {
      int var2 = var1.length();

      int var3;
      int var4;
      label27:
      for(var3 = 0; var3 < var2; var3 += var4) {
         var4 = min(256, var2 - var3);
         var1.getChars(var3, var3 + var4, this.cbuf, 0);

         for(int var5 = 0; var5 < var4; ++var5) {
            char var6 = this.cbuf[var5];
            if ((var6 & 'ﾀ') != 0) {
               break label27;
            }
         }
      }

      if (var3 == var2) {
         this.write_ulong(var2);
         this.writeASCII(var1);
      } else {
         this.writeStringUTF8_1Pass(var1, false);
      }

   }

   private final void writeStringUTF8_1Pass(String var1, boolean var2) {
      this.align(4);
      int var3 = this.pos();
      this.write_slow_long(0);
      int var4 = var1.length();
      int var5 = 0;
      int var6 = 0;

      int var7;
      while(var6 < var4) {
         while(var6 < var4 && this.chunkPos < Chunk.CHUNK_SIZE - 3) {
            var7 = min(256, var4 - var6);
            var1.getChars(var6, var6 + var7, this.cbuf, 0);
            int var8 = 0;

            int var9;
            for(var9 = this.chunkPos; var8 < var7 && var9 < Chunk.CHUNK_SIZE - 3; ++var8) {
               char var10 = this.cbuf[var8];
               if ((var10 & 'ﾀ') == 0) {
                  this.current.buf[var9++] = (byte)var10;
               } else if ((var10 & '\uf800') == 0) {
                  this.current.buf[var9++] = (byte)(192 | var10 >> 6 & 31);
                  this.current.buf[var9++] = (byte)(128 | var10 >> 0 & 63);
               } else {
                  this.current.buf[var9++] = (byte)(224 | var10 >> 12 & 15);
                  this.current.buf[var9++] = (byte)(128 | var10 >> 6 & 63);
                  this.current.buf[var9++] = (byte)(128 | var10 >> 0 & 63);
               }
            }

            var5 += var9 - this.chunkPos;
            this.chunkPos = var9;
            var6 += var8;
         }

         while(var6 < var4 && this.chunkPos >= Chunk.CHUNK_SIZE - 3) {
            var5 += this.writeUTF8Char(var1.charAt(var6++));
         }
      }

      var7 = this.pos();
      this.setPosition(var3);
      if (var2) {
         this.write_slow_long(var5 + 1);
         this.setPosition(var7);
         this.write_octet((byte)0);
      } else {
         this.write_slow_long(var5);
         this.setPosition(var7);
      }

   }

   public final void writeASCII(String var1) {
      if (this.chunking && this.currentChunk == 0) {
         this.startChunk();
      }

      int var2 = var1.length();

      int var4;
      for(int var3 = 0; var3 < var2; var3 += var4) {
         if (this.chunkPos == Chunk.CHUNK_SIZE) {
            this.advance();
         }

         var4 = min(Chunk.CHUNK_SIZE - this.chunkPos, var2 - var3);
         var1.getBytes(var3, var3 + var4, this.current.buf, this.chunkPos);
         this.chunkPos += var4;
      }

   }

   private final int writeUTF8Char(char var1) {
      if ((var1 & 'ﾀ') == 0) {
         this.write_octet((byte)var1);
         return 1;
      } else if ((var1 & '\uf800') == 0) {
         this.write_octet((byte)(192 | var1 >> 6 & 31));
         this.write_octet((byte)(128 | var1 >> 0 & 63));
         return 2;
      } else {
         this.write_octet((byte)(224 | var1 >> 12 & 15));
         this.write_octet((byte)(128 | var1 >> 6 & 63));
         this.write_octet((byte)(128 | var1 >> 0 & 63));
         return 3;
      }
   }

   protected void write_UTF8wchar(char var1) {
      this.align(1);
      if (this.getMinorVersion() >= 2) {
         if ((var1 & 'ﾀ') == 0) {
            this.write_octet((byte)1);
         } else if ((var1 & '\uf800') == 0) {
            this.write_octet((byte)2);
         } else {
            this.write_octet((byte)3);
         }
      }

      this.writeUTF8Char(var1);
   }

   public void write_wstring(String var1) {
      if (var1 == null) {
         throw new MARSHAL("null value passes to write_wstring(String)");
      } else {
         Object var2 = null;
         if (this.getMinorVersion() < 2) {
            GIOP10Helper.write_wstring(var1, this, this.getWcharCodeSet(), this.littleEndian);
         } else {
            switch (this.getWcharCodeSet()) {
               case 65792:
                  this.writeStringUTF16(var1, false);
                  break;
               case 65801:
                  this.writeStringUTF16(var1, true);
                  break;
               case 83951617:
                  this.writeUTF8(var1);
                  break;
               default:
                  throw new CODESET_INCOMPATIBLE();
            }

         }
      }
   }

   public final void writeChunks(Chunk var1) {
      this.current.end = Math.max(this.current.end, this.chunkPos);
      Chunk var2 = var1;

      int var3;
      for(var3 = 0; var1.next != null; var1 = var1.next) {
         var3 += var1.end;
      }

      var3 += var1.end;
      this.write_long(var3);
      this.current.next = var2;
      this.chunkPos = var1.end;
      this.current = var1;
      this.currentPos = this.currentPos + var3 - this.chunkPos;
   }

   public void write_boolean_array(boolean[] var1, int var2, int var3) {
      if (var1 == null) {
         throw new MARSHAL("null value passes to write_boolean_array");
      } else {
         for(int var4 = var2; var4 < var3; ++var4) {
            this.write_boolean(var1[var4]);
         }

      }
   }

   public void write_char_array(char[] var1, int var2, int var3) {
      if (var1 == null) {
         throw new MARSHAL("null value passes to write_char_array");
      } else {
         for(int var4 = var2; var4 < var3; ++var4) {
            this.write_char(var1[var4]);
         }

      }
   }

   public void write_wchar_array(char[] var1, int var2, int var3) {
      if (var1 == null) {
         throw new MARSHAL("null value passes to write_wchar_array");
      } else {
         for(int var4 = var2; var4 < var3; ++var4) {
            this.write_wchar(var1[var4]);
         }

      }
   }

   public void write_octet_array(byte[] var1, int var2, int var3) {
      if (var1 == null) {
         throw new MARSHAL("null array as parameter to write_octet_array");
      } else {
         if (this.needEightByteAlignment) {
            this.checkEightByteAlignment();
         }

         if (this.chunking && this.currentChunk == 0) {
            this.startChunk();
         }

         while(var3 > 0) {
            this.advance();
            int var4 = min(Chunk.CHUNK_SIZE - this.chunkPos, var3);
            System.arraycopy(var1, var2, this.current.buf, this.chunkPos, var4);
            var2 += var4;
            var3 -= var4;
            this.chunkPos += var4;
         }

      }
   }

   public void write_short_array(short[] var1, int var2, int var3) {
      if (var1 == null) {
         throw new MARSHAL("null value passes to write_short_array");
      } else {
         for(int var4 = var2; var4 < var3; ++var4) {
            this.write_short(var1[var4]);
         }

      }
   }

   public void write_ushort_array(short[] var1, int var2, int var3) {
      if (var1 == null) {
         throw new MARSHAL("null value passes to write_ushort_array");
      } else {
         for(int var4 = var2; var4 < var3; ++var4) {
            this.write_ushort(var1[var4]);
         }

      }
   }

   public void write_long_array(int[] var1, int var2, int var3) {
      if (var1 == null) {
         throw new MARSHAL("null value passes to write_long_array");
      } else {
         for(int var4 = var2; var4 < var3; ++var4) {
            this.write_long(var1[var4]);
         }

      }
   }

   public void write_ulong_array(int[] var1, int var2, int var3) {
      if (var1 == null) {
         throw new MARSHAL("null value passes to write_ulong_array");
      } else {
         for(int var4 = var2; var4 < var3; ++var4) {
            this.write_ulong(var1[var4]);
         }

      }
   }

   public void write_longlong_array(long[] var1, int var2, int var3) {
      if (var1 == null) {
         throw new MARSHAL("null value passes to write_longlong_array");
      } else {
         for(int var4 = var2; var4 < var3; ++var4) {
            this.write_longlong(var1[var4]);
         }

      }
   }

   public void write_ulonglong_array(long[] var1, int var2, int var3) {
      if (var1 == null) {
         throw new MARSHAL("null value passes to write_ulonglong_array");
      } else {
         for(int var4 = var2; var4 < var3; ++var4) {
            this.write_ulonglong(var1[var4]);
         }

      }
   }

   public void write_float_array(float[] var1, int var2, int var3) {
      if (var1 == null) {
         throw new MARSHAL("null value passes to write_float_array");
      } else {
         for(int var4 = var2; var4 < var3; ++var4) {
            this.write_float(var1[var4]);
         }

      }
   }

   public void write_double_array(double[] var1, int var2, int var3) {
      if (var1 == null) {
         throw new MARSHAL("null value passes to write_double_array");
      } else {
         for(int var4 = var2; var4 < var3; ++var4) {
            this.write_double(var1[var4]);
         }

      }
   }

   public void write_Object(org.omg.CORBA.Object var1) {
      this.writeRemote(var1);
   }

   private static IndirectionValueHashtable getHashMap() {
      IndirectionValueHashtable var0 = (IndirectionValueHashtable)mapPool.remove();
      if (var0 == null) {
         var0 = new IndirectionValueHashtable();
      }

      return var0;
   }

   private static void releaseHashMap(IndirectionValueHashtable var0) {
      if (var0 != null) {
         var0.clear();
         mapPool.add(var0);
      }

   }

   public void write_TypeCode(TypeCode var1) {
      if (var1 == null) {
         this.write_long(0);
      } else {
         if (this.tcNestingLevel == 0 && this.tcIndirections != null) {
            this.tcIndirections.clear();
         }

         if (var1.kind().value() > 13) {
            if (this.tcIndirections == null) {
               this.tcIndirections = getHashMap();
            }

            int var6;
            if ((var6 = this.tcIndirections.get(var1, 0)) >= 0) {
               this.write_ulong(-1);
               this.write_ulong(var6 - this.pos());
               return;
            }

            this.align(4);
            this.tcIndirections.put(var1, 0, this.pos(), var6);
         }

         ++this.tcNestingLevel;
         TypeCodeImpl.write(var1, this);
         --this.tcNestingLevel;
      }
   }

   void writeAny(Object var1) {
      try {
         var1 = IIOPReplacer.getIIOPReplacer().replaceObject(var1);
      } catch (IOException var3) {
         throw new MARSHAL("IOException writing Any " + var3.getMessage());
      }

      if (var1 == null && this.useCompliantChunking()) {
         this.write_TypeCode(TypeCodeImpl.NULL);
         this.write_boolean(false);
         this.write_long(0);
      } else if (var1 instanceof String) {
         String var2 = (String)var1;
         this.write_TypeCode(TypeCodeImpl.STRING);
         this.write_value(var2);
      } else if (var1 instanceof IOR) {
         this.write_TypeCode(TypeCodeImpl.OBJECT);
         ((IOR)var1).write(this);
      } else {
         this.write_TypeCode(new TypeCodeImpl(29, this.findRepositoryIdForValue((Serializable)var1), "", TypeCodeImpl.NULL_TC));
         this.write_value((Serializable)var1);
      }

   }

   public final void write_any(Any var1) {
      if (var1 == null) {
         this.write_TypeCode((TypeCode)null);
      } else {
         this.write_TypeCode(var1.type());
         var1.write_value(this);
      }

   }

   public final void write_any(Any var1, TypeCode var2) {
      var1.write_value(this);
   }

   public void write_Principal(Principal var1) {
      throw new NO_IMPLEMENT("write_Principal not implemented");
   }

   public void write(int var1) throws IOException {
      this.write_octet((byte)(var1 & 255));
   }

   public void write_fixed(BigDecimal var1) {
      throw new NO_IMPLEMENT("write_fixed() not implemented");
   }

   public void write_Context(Context var1, ContextList var2) {
      throw new NO_IMPLEMENT("write_Context() not implemented");
   }

   public ORB orb() {
      return this.orb;
   }

   public weblogic.iiop.spi.Message getMessage() {
      return this.message;
   }

   String getRepositoryID(Object var1) {
      return var1 == null ? null : valueHandler.getRMIRepositoryID(var1.getClass());
   }

   private final boolean writeIndirectionMaybe(Serializable var1) {
      int var2 = this.indirections.get(var1, this.nestingLevel);
      if (var2 > 0) {
         this.write_long(-1);
         this.write_long(var2 - this.pos());
         return true;
      } else {
         this.align(4);
         this.indirections.put(var1, this.nestingLevel, this.pos(), var2);
         return false;
      }
   }

   private void terminateChunk(int var1) {
      int var2 = var1 - this.currentChunk - 4;
      Debug.assertion(this.currentChunk > 0);
      if (this.currentChunkChunk != null) {
         this.currentChunkChunk.buf[this.currentChunkPos++] = (byte)(var2 >>> 24);
         this.currentChunkChunk.buf[this.currentChunkPos++] = (byte)(var2 >>> 16);
         this.currentChunkChunk.buf[this.currentChunkPos++] = (byte)(var2 >>> 8);
         this.currentChunkChunk.buf[this.currentChunkPos++] = (byte)var2;
         this.currentChunkChunk = null;
         this.currentChunkPos = 0;
      } else {
         this.setPosition(this.currentChunk);
         this.write_slow_long(var2);
         this.setPosition(var1);
      }

      this.currentChunk = 0;
   }

   private void startChunk() {
      this.chunking = false;
      this.align(4);
      this.currentChunk = this.pos();
      if (this.chunkPos + 4 <= Chunk.CHUNK_SIZE && !this.littleEndian) {
         this.currentChunkPos = this.chunkPos;
         this.currentChunkChunk = this.current;
         this.chunkPos += 4;
      } else {
         this.write_slow_long(0);
      }

      this.chunking = true;
   }

   private void endChunk() {
      int var1 = this.pos();
      this.chunking = false;
      if (this.lastEnd != var1) {
         if (this.currentChunk > 0) {
            this.terminateChunk(var1);
         }
      } else {
         this.setPosition(this.lastEnd - 4);
         this.currentChunk = 0;
         this.currentChunkChunk = null;
      }

      this.write_long(this.endTag);
      if (this.useCompliantChunking()) {
         ++this.endTag;
      }

      if (this.endTag > 0) {
         throw new AssertionError("Chunking error at " + this.pos() + ": end tags must not be +ve: " + this.endTag);
      } else {
         if (this.endTag == 0) {
            this.lastEnd = 0;
         } else {
            this.lastEnd = this.pos();
         }

      }
   }

   public void start_value(String var1) {
      Debug.assertion(var1 != null);
      this.chunking = false;
      if (this.currentChunk != 0) {
         this.terminateChunk(this.pos());
      }

      this.write_long(2147483402);
      RepositoryId var2 = ClassInfo.getRepositoryId(var1);
      if (!this.writeIndirectionMaybe(var2)) {
         this.write_repository_id(var2);
      }

      this.chunking = true;
      --this.endTag;
   }

   public void end_value() {
      if (this.chunking) {
         this.endChunk();
      }

      this.chunking = true;
   }

   public void write_value(Serializable var1) {
      if (var1 == null) {
         this.write_long(0);
      } else {
         if (this.indirections == null) {
            this.indirections = getHashMap();
         }

         int var2 = this.indirections.get(var1, this.nestingLevel);
         if (var2 > 0) {
            this.write_long(-1);
            this.write_long(var2 - this.pos());
         } else {
            Class var3 = var1.getClass();
            if (Proxy.class.isAssignableFrom(var3)) {
               var1 = new ProxyDesc((Proxy)var1);
               var3 = ProxyDesc.class;
            }

            ClassInfo var4 = ClassInfo.findClassInfo(var3);
            if (var4.isPortableReplaceable()) {
               try {
                  var1 = EJBPortableReplacer.getReplacer().replaceObject((Serializable)var1);
                  var3 = var1.getClass();
                  var4 = ClassInfo.findClassInfo(var3);
               } catch (IOException var15) {
                  throw new MARSHAL("IOException writing PortableReplaceable " + var15.getMessage());
               }
            }

            Serializable var16 = var4.writeReplace(var1);
            if (var16.getClass() != var3) {
               var3 = var16.getClass();
               var4 = ClassInfo.findClassInfo(var3);
            }

            boolean var6 = this.chunking;
            boolean var7 = this.chunking || var4.getDescriptor().isCustomMarshaled();
            this.chunking = false;
            if (var6 && this.currentChunk != 0) {
               this.terminateChunk(this.pos());
            }

            int var8 = var7 ? 2147483402 : 2147483394;
            RepositoryId[] var9 = var4.getRepositoryIdList();
            if (var9 != null) {
               var8 |= 6;
            }

            boolean var10 = false;
            String var11 = null;
            if (var10) {
               String var12 = Utils.getAnnotation(var4.forClass().getClassLoader());
               if (var12 != null) {
                  var11 = Codebase.getDefaultCodebase() + var12 + '/';
                  var8 |= 1;
               }
            }

            this.write_long(var8);
            this.indirections.put(var16, this.nestingLevel, this.pos() - 4, var2);
            if (var11 != null && !this.writeIndirectionMaybe(var11)) {
               this.write_string(var11);
            }

            RepositoryId var17 = var4.getRepositoryId();
            if (var9 != null) {
               this.writeRepositoryIdList(var9);
            } else {
               if (var4.isValueBase()) {
                  var17 = new RepositoryId(((ValueBase)var16)._truncatable_ids()[0]);
               }

               if (var17 == null || var17.length() == 0 || !this.writeIndirectionMaybe(var17)) {
                  this.write_repository_id(var17);
               }
            }

            this.chunking = var7;
            if (var7 || !this.useCompliantChunking()) {
               --this.endTag;
            }

            if (var4.isString()) {
               this.write_wstring((String)var16);
            } else if (var17.isClassDesc()) {
               this.write_value("");
               this.write_value(ClassInfo.findClassInfo((Class)var16).getRepositoryId().toString());
            } else if (var4.isIDLEntity() && !org.omg.CORBA.Object.class.isAssignableFrom(var3)) {
               this.write_IDLValue(var16, var3);
            } else if (var4.isStreamable()) {
               ((Streamable)var16)._write(this);
            } else if (!this.useJavaSerialization) {
               try {
                  ValueHandlerImpl.writeValue(this, var16, this.maxFormatVersion, var4);
               } catch (IOException var14) {
                  throw (MARSHAL)(new MARSHAL(var14.getMessage())).initCause(var14);
               }
            } else {
               valueHandler.writeValue(this, var16);
            }

            if (this.chunking) {
               this.endChunk();
            }

            if (!this.useCompliantChunking()) {
               ++this.endTag;
            }

            this.chunking = var6;
         }
      }
   }

   public void write_value(Serializable var1, Class var2) {
      if (var2.equals(AuthenticatedUser.class) && AuthenticatedSubject.class.isInstance(var1)) {
         var1 = SecurityServiceManager.convertToAuthenticatedUser((AuthenticatedUser)var1);
         if (var1 == null) {
            this.write_long(0);
            return;
         }
      }

      this.write_value((Serializable)var1);
   }

   public void write_value(Serializable var1, String var2) {
      this.write_value(var1);
   }

   public RepositoryId findRepositoryIdForValue(Serializable var1) {
      Class var2 = var1.getClass();
      ClassInfo var3 = ClassInfo.findClassInfo(var2);
      if (var3.isPortableReplaceable()) {
         try {
            var1 = EJBPortableReplacer.getReplacer().replaceObject(var1);
            var2 = var1.getClass();
            var3 = ClassInfo.findClassInfo(var2);
         } catch (IOException var5) {
            throw new MARSHAL("IOException writing value " + var5.getMessage());
         }
      }

      var1 = var3.writeReplace(var1);
      if (var1.getClass() != var2) {
         var2 = var1.getClass();
         var3 = ClassInfo.findClassInfo(var2);
      }

      return var3.isValueBase() ? new RepositoryId(((ValueBase)var1)._truncatable_ids()[0]) : var3.getRepositoryId();
   }

   public void write_value(Serializable var1, BoxedValueHelper var2) {
      this.write_value(var1);
   }

   public void write_abstract_interface(Object var1) {
      if ((var1 instanceof Remote || var1 instanceof org.omg.CORBA.Object || var1 instanceof InvokeHandler || var1 instanceof IOR) && !(var1 instanceof Proxy)) {
         this.write_boolean(true);
         this.writeRemote(var1);
      } else {
         this.write_boolean(false);
         this.write_value((Serializable)var1);
      }

   }

   private final void write_IDLValue(Serializable var1, Class var2) {
      try {
         if (var1 instanceof StreamableValue) {
            ((StreamableValue)var1)._write(this);
         } else {
            if (var1 instanceof CustomValue) {
               throw new MARSHAL("Custom marshalled valuetypes not supported");
            }

            if (var1 instanceof ValueBase) {
               BoxedValueHelper var3 = (BoxedValueHelper)Utils.getHelper(var2, "Helper").newInstance();
               var3.write_value(this, var1);
            } else {
               this.write_IDLEntity(var1, var2);
            }
         }

      } catch (InstantiationException var4) {
         throw new MARSHAL(var4.getMessage());
      } catch (IllegalAccessException var5) {
         throw new MARSHAL(var5.getMessage());
      }
   }

   final void write_IDLEntity(Object var1, Class var2) {
      write_IDLEntity(this, var1, var2);
   }

   public static final void write_IDLEntity(org.omg.CORBA.portable.OutputStream var0, Object var1, Class var2) {
      Debug.assertion(IDLEntity.class.isAssignableFrom(var2));
      Method var3 = Utils.getIDLWriter(var2);
      if (var3 == null) {
         throw new MARSHAL("Couldn't find helper for " + var2.getName());
      } else {
         writeWithHelper(var0, var1, var3);
      }
   }

   public static final void writeWithHelper(org.omg.CORBA.portable.OutputStream var0, Object var1, Method var2) {
      try {
         var2.invoke((Object)null, var0, var1);
      } catch (IllegalAccessException var5) {
         throw new MARSHAL(var5.getMessage());
      } catch (InvocationTargetException var6) {
         MARSHAL var4 = new MARSHAL(var6.getTargetException().getMessage());
         var4.initCause(var6.getTargetException());
         throw var4;
      }
   }

   public void writeRemote(Object var1) {
      if (var1 == null) {
         IOR.NULL.write(this);
      } else {
         IOR var2 = null;

         try {
            var2 = IIOPReplacer.getIIOPReplacer().replaceRemote(var1);
         } catch (IOException var4) {
            IIOPLogger.logFailedToExport(var1.getClass().getName(), var4);
            throw new MARSHAL("Couldn't export " + var1.getClass().getName(), 0, CompletionStatus.COMPLETED_MAYBE);
         }

         var2.write(this);
      }
   }

   public void write_octet_sequence(byte[] var1) {
      if (var1 == null) {
         this.write_long(0);
      } else {
         this.write_long(var1.length);
         this.write_octet_array(var1, 0, var1.length);
      }

   }

   public final void write(byte[] var1) throws IOException {
      if (var1 == null) {
         throw new MARSHAL("null array as parameter to readFully");
      } else {
         this.write(var1, 0, var1.length);
      }
   }

   public final void write(byte[] var1, int var2, int var3) throws IOException {
      this.write_octet_array(var1, var2, var3);
   }

   public final void writeBoolean(boolean var1) throws IOException {
      this.write_boolean(var1);
   }

   public final void writeByte(int var1) throws IOException {
      this.write_octet((byte)(var1 & 255));
   }

   public final void writeShort(int var1) throws IOException {
      this.write_short((short)(var1 & '\uffff'));
   }

   public final void writeChar(int var1) throws IOException {
      this.write_wchar((char)(var1 & '\uffff'));
   }

   public final void writeInt(int var1) throws IOException {
      this.write_long(var1);
   }

   public final void writeLong(long var1) throws IOException {
      this.write_longlong(var1);
   }

   public final void writeFloat(float var1) throws IOException {
      this.write_float(var1);
   }

   public final void writeDouble(double var1) throws IOException {
      this.write_double(var1);
   }

   public final void writeBytes(String var1) throws IOException {
      this.writeASCII(var1);
   }

   public final void writeChars(String var1) throws IOException {
      char[] var2 = var1.toCharArray();
      this.write_wchar_array(var2, 0, var2.length);
   }

   public final void writeUTF(String var1) throws IOException {
      this.write_wstring(var1);
   }

   public final void writeObject(Object var1) throws IOException {
      this.write_abstract_interface(var1);
   }

   public final void writeObject(Object var1, Class var2) throws IOException {
      try {
         if (!Remote.class.isAssignableFrom(var2) && !InvokeHandler.class.isAssignableFrom(var2) && !org.omg.CORBA.Object.class.isAssignableFrom(var2)) {
            if (!var2.equals(Object.class) && !var2.equals(Serializable.class) && !var2.equals(Externalizable.class)) {
               if (Utils.isIDLException(var2)) {
                  this.write_IDLEntity(var1, var2);
               } else if (Utils.isAbstractInterface(var2)) {
                  this.write_abstract_interface(var1);
               } else {
                  var1 = RemoteObjectReplacer.getReplacer().replaceObject(var1);
                  if (this.useJavaSerialization) {
                     try {
                        this.write_value(new MarshalledObject(var1), (Class)MarshalledObject.class);
                     } catch (NotSerializableException var4) {
                        this.write_value((Serializable)var1, var2);
                     }
                  } else {
                     if (USE_JAVA_SERIALIZATION_FOR_JMX && MBeanInfo.class.isAssignableFrom(var2)) {
                        var1 = new MarshalledObject(var1);
                        var2 = MarshalledObject.class;
                     }

                     this.write_value((Serializable)var1, var2);
                  }
               }
            } else {
               this.writeAny(var1);
            }
         } else {
            this.writeRemote(var1);
         }

      } catch (SystemException var5) {
         throw Utils.mapSystemException(var5);
      }
   }

   public final void flush() {
      this.current.end = this.chunkPos;
   }

   void p(String var1) {
      System.err.println("<IIOPOutputStream>: " + var1);
   }

   final boolean isWritingObjectKey() {
      return this.writingObjectKey;
   }

   final void setWritingObjectKey(boolean var1) {
      this.writingObjectKey = var1;
   }

   boolean getLittleEndian() {
      return this.littleEndian;
   }

   static {
      String var0 = System.getProperty("weblogic.iiop.useJavaSerializationForJMX", "true");
      USE_JAVA_SERIALIZATION_FOR_JMX = Boolean.valueOf(var0);
      IIOPService.load();
      valueHandler = Util.createValueHandler();
      mapPool = new StackPool(1024);
   }

   static class Marker {
      Chunk chunk;
      int pos;
      int currentPos;
   }
}
