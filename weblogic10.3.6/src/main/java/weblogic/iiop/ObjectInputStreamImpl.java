package weblogic.iiop;

import java.io.IOException;
import java.io.NotActiveException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import weblogic.common.internal.ProxyClassResolver;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.Kernel;
import weblogic.utils.collections.NumericValueHashMap;
import weblogic.utils.collections.Stack;
import weblogic.utils.io.ObjectStreamClass;
import weblogic.utils.io.ObjectStreamField;

final class ObjectInputStreamImpl extends ObjectInputStream {
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");
   private IIOPInputStream delegate;
   private Object value;
   private ObjectStreamClass osc;
   private boolean dfwoCalled = false;
   private int streamFormatState;
   private static final int STREAM_VERSION_1 = 0;
   private static final int STREAM_VERSION_2 = 1;
   private static final int STREAM_VERSION_2_UNTERMINATED = 2;
   private GetFieldImpl getFields;
   private IIOPInputStream.Marker mark = new IIOPInputStream.Marker();
   private Stack streamStack;

   ObjectInputStreamImpl(IIOPInputStream var1, Object var2, ObjectStreamClass var3, boolean var4, byte var5) throws IOException {
      this.delegate = var1;
      this.value = var2;
      this.osc = var3;
      this.streamFormatState = var5 > 1 ? 1 : 0;
      this.delegate.mark(0);
      this.delegate.mark(this.mark);
      this.dfwoCalled = var4;
   }

   void pushCurrent(Object var1, ObjectStreamClass var2, boolean var3, byte var4) {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("pushCurrent(" + var2 + ")");
      }

      if (this.streamStack == null) {
         this.streamStack = new Stack();
      }

      this.streamStack.push(new StreamEntry());
      this.value = var1;
      this.osc = var2;
      this.dfwoCalled = var3;
      this.getFields = null;
      this.streamFormatState = var4 > 1 ? 1 : 0;
      this.delegate.mark(this.mark);
   }

   private void popCurrent() {
      if (this.streamStack != null && this.streamStack.size() > 0) {
         StreamEntry var1 = (StreamEntry)this.streamStack.pop();
         this.value = var1.value;
         this.osc = var1.osc;
         this.dfwoCalled = var1.dfwoCalled;
         this.getFields = var1.getFields;
         this.streamFormatState = var1.streamFormatState;
         this.mark.copy(var1.mark);
      } else {
         this.value = null;
         this.osc = null;
      }

      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("popCurrent(" + this.osc + ")");
      }

   }

   public String readLine() throws IOException {
      throw new UnsupportedOperationException("readLine");
   }

   public int readInt() throws IOException {
      return this.delegate.readInt();
   }

   public String readUTF() throws IOException {
      return this.delegate.readUTF();
   }

   public long readLong() throws IOException {
      return this.delegate.readLong();
   }

   public byte readByte() throws IOException {
      return this.delegate.readByte();
   }

   public short readShort() throws IOException {
      return this.delegate.readShort();
   }

   public float readFloat() throws IOException {
      return this.delegate.readFloat();
   }

   public char readChar() throws IOException {
      return this.delegate.readChar();
   }

   public void readFully(byte[] var1) throws IOException {
      this.delegate.readFully(var1);
   }

   public void readFully(byte[] var1, int var2, int var3) throws IOException {
      this.delegate.readFully(var1, var2, var3);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      return this.delegate.read(var1, var2, var3);
   }

   public int read() throws IOException {
      return this.delegate.read();
   }

   public int skipBytes(int var1) throws IOException {
      return this.delegate.skipBytes(var1);
   }

   public boolean readBoolean() throws IOException {
      return this.delegate.readBoolean();
   }

   public int readUnsignedByte() throws IOException {
      return this.delegate.readUnsignedByte();
   }

   public int readUnsignedShort() throws IOException {
      return this.delegate.readUnsignedShort();
   }

   public double readDouble() throws IOException {
      return this.delegate.readDouble();
   }

   public Object readObjectOverride() throws IOException, ClassNotFoundException {
      return this.delegate.readObject();
   }

   public Object readUnshared() throws IOException, ClassNotFoundException {
      return this.delegate.readObject();
   }

   public void defaultReadObject() throws IOException, ClassNotFoundException {
      if (this.value == null) {
         throw new NotActiveException("Not in writeObject()");
      } else if (!this.dfwoCalled) {
         throw new StreamCorruptedException("defaultWriteObject was not called by the sender.");
      } else {
         this.osc.readFields(this.value, this.delegate);
         if (this.streamFormatState == 1 && this.delegate.startValue()) {
            this.streamFormatState = 2;
         }

      }
   }

   public ObjectInputStream.GetField readFields() throws IOException, ClassNotFoundException {
      if (this.value == null) {
         throw new NotActiveException("Not in writeObject()");
      } else {
         if (this.getFields == null) {
            this.getFields = new GetFieldImpl();
         }

         this.getFields.read();
         return this.getFields;
      }
   }

   protected Class resolveProxyClass(String[] var1) throws IOException, ClassNotFoundException {
      return ProxyClassResolver.resolveProxyClass(var1);
   }

   public void reset() throws IOException {
      this.delegate.reset(this.mark);
   }

   public void close() throws IOException {
      if (this.streamFormatState == 2) {
         this.delegate.end_value();
      }

      this.popCurrent();
   }

   private static void p(String var0) {
      System.out.println("<ObjectInputStreamImpl>: " + var0);
   }

   private final class GetFieldImpl extends ObjectInputStream.GetField {
      private final HashMap fieldMap;
      private final NumericValueHashMap primitiveFieldMap;

      private GetFieldImpl() {
         this.fieldMap = new HashMap();
         this.primitiveFieldMap = new NumericValueHashMap();
      }

      public boolean get(String var1, boolean var2) {
         return this.primitiveFieldMap.get(var1, (long)(var2 ? 1 : 0)) == 1L;
      }

      public byte get(String var1, byte var2) {
         return (byte)((int)this.primitiveFieldMap.get(var1, (long)var2));
      }

      public char get(String var1, char var2) {
         return (char)((int)this.primitiveFieldMap.get(var1, (long)var2));
      }

      public short get(String var1, short var2) {
         return (short)((int)this.primitiveFieldMap.get(var1, (long)var2));
      }

      public int get(String var1, int var2) {
         return (int)this.primitiveFieldMap.get(var1, (long)var2);
      }

      public long get(String var1, long var2) {
         return this.primitiveFieldMap.get(var1, var2);
      }

      public float get(String var1, float var2) {
         return Float.intBitsToFloat((int)this.primitiveFieldMap.get(var1, (long)Float.floatToIntBits(var2)));
      }

      public double get(String var1, double var2) {
         return Double.longBitsToDouble(this.primitiveFieldMap.get(var1, Double.doubleToLongBits(var2)));
      }

      public Object get(String var1, Object var2) {
         return this.fieldMap.get(var1);
      }

      public boolean defaulted(String var1) {
         return !this.primitiveFieldMap.containsKey(var1) && !this.fieldMap.containsKey(var1);
      }

      public java.io.ObjectStreamClass getObjectStreamClass() {
         return ObjectInputStreamImpl.this.osc.getObjectStreamClass();
      }

      private void read() throws IOException, ClassNotFoundException {
         ObjectStreamField[] var1 = ObjectInputStreamImpl.this.osc.getFields();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            ObjectStreamField var3 = var1[var2];
            if (Kernel.DEBUG && ObjectInputStreamImpl.debugIIOPDetail.isDebugEnabled()) {
               ObjectInputStreamImpl.p("Reading: " + var3);
            }

            switch (var3.getTypeCode()) {
               case 'B':
               case 'Z':
                  this.primitiveFieldMap.put(var3.getName(), (long)ObjectInputStreamImpl.this.delegate.readByte());
                  break;
               case 'C':
                  this.primitiveFieldMap.put(var3.getName(), (long)ObjectInputStreamImpl.this.delegate.readChar());
                  break;
               case 'D':
               case 'J':
                  this.primitiveFieldMap.put(var3.getName(), ObjectInputStreamImpl.this.delegate.readLong());
                  break;
               case 'E':
               case 'G':
               case 'H':
               case 'K':
               case 'M':
               case 'N':
               case 'O':
               case 'P':
               case 'Q':
               case 'R':
               case 'T':
               case 'U':
               case 'V':
               case 'W':
               case 'X':
               case 'Y':
               default:
                  throw new IOException("Bad typecode: " + var3.getTypeCode());
               case 'F':
               case 'I':
                  this.primitiveFieldMap.put(var3.getName(), (long)ObjectInputStreamImpl.this.delegate.readInt());
                  break;
               case 'L':
               case '[':
                  this.fieldMap.put(var3.getName(), ObjectInputStreamImpl.this.delegate.readObject(var3.getType()));
                  break;
               case 'S':
                  this.primitiveFieldMap.put(var3.getName(), (long)ObjectInputStreamImpl.this.delegate.readShort());
            }
         }

         if (ObjectInputStreamImpl.this.streamFormatState == 1 && ObjectInputStreamImpl.this.delegate.startValue()) {
            ObjectInputStreamImpl.this.streamFormatState = 2;
         }

      }

      // $FF: synthetic method
      GetFieldImpl(Object var2) {
         this();
      }
   }

   private final class StreamEntry {
      private final Object value;
      private final ObjectStreamClass osc;
      private final boolean dfwoCalled;
      private final GetFieldImpl getFields;
      private final int streamFormatState;
      private final IIOPInputStream.Marker mark;

      private StreamEntry() {
         this.mark = new IIOPInputStream.Marker();
         this.value = ObjectInputStreamImpl.this.value;
         this.osc = ObjectInputStreamImpl.this.osc;
         this.dfwoCalled = ObjectInputStreamImpl.this.dfwoCalled;
         this.getFields = ObjectInputStreamImpl.this.getFields;
         this.streamFormatState = ObjectInputStreamImpl.this.streamFormatState;
         this.mark.copy(ObjectInputStreamImpl.this.mark);
      }

      // $FF: synthetic method
      StreamEntry(Object var2) {
         this();
      }
   }
}
