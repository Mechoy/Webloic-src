package weblogic.iiop;

import java.io.IOException;
import java.io.NotActiveException;
import java.io.NotSerializableException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import weblogic.corba.utils.ValueHandlerImpl;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.Kernel;
import weblogic.utils.UnsyncStringBuffer;
import weblogic.utils.collections.NumericValueHashMap;
import weblogic.utils.collections.Stack;
import weblogic.utils.io.ObjectStreamClass;
import weblogic.utils.io.ObjectStreamField;

final class ObjectOutputStreamImpl extends ObjectOutputStream {
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");
   private final IIOPOutputStream delegate;
   private Object value;
   private ObjectStreamClass osc;
   private boolean dfwoHandled = false;
   private int streamFormatState;
   private static final int STREAM_VERSION_1 = 0;
   private static final int STREAM_VERSION_2 = 1;
   private static final int STREAM_VERSION_2_UNTERMINATED = 2;
   private PutFieldImpl putFields;
   private IIOPOutputStream.Marker mark = new IIOPOutputStream.Marker();
   private Stack streamStack;

   ObjectOutputStreamImpl(IIOPOutputStream var1, Object var2, ObjectStreamClass var3, byte var4) throws IOException {
      this.delegate = var1;
      this.value = var2;
      this.osc = var3;
      this.streamFormatState = var4 > 1 ? 1 : 0;
      this.delegate.setMark(this.mark);
   }

   void pushCurrent(Object var1, ObjectStreamClass var2, byte var3) {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("pushCurrent(" + var2 + ")");
      }

      if (this.streamStack == null) {
         this.streamStack = new Stack();
      }

      this.streamStack.push(new StreamEntry());
      this.value = var1;
      this.osc = var2;
      this.dfwoHandled = false;
      this.putFields = null;
      this.streamFormatState = var3 > 1 ? 1 : 0;
      this.delegate.setMark(this.mark);
   }

   private void popCurrent() {
      if (this.streamStack != null && this.streamStack.size() > 0) {
         StreamEntry var1 = (StreamEntry)this.streamStack.pop();
         this.value = var1.value;
         this.osc = var1.osc;
         this.dfwoHandled = var1.dfwoHandled;
         this.putFields = var1.putFields;
         this.streamFormatState = var1.streamFormatState;
         this.mark = var1.mark;
      } else {
         this.value = null;
         this.osc = null;
      }

      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("popCurrent(" + this.osc + ")");
      }

   }

   public void write(byte[] var1) throws IOException {
      this.handleOptionalData();
      this.delegate.write(var1);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.handleOptionalData();
      this.delegate.write(var1, var2, var3);
   }

   public void write(int var1) throws IOException {
      this.handleOptionalData();
      this.delegate.write(var1);
   }

   public void writeInt(int var1) throws IOException {
      this.handleOptionalData();
      this.delegate.writeInt(var1);
   }

   public void writeUTF(String var1) throws IOException {
      this.handleOptionalData();
      this.delegate.writeUTF(var1);
   }

   public void writeLong(long var1) throws IOException {
      this.handleOptionalData();
      this.delegate.writeLong(var1);
   }

   public void writeByte(int var1) throws IOException {
      this.handleOptionalData();
      this.delegate.writeByte(var1);
   }

   public void writeShort(int var1) throws IOException {
      this.handleOptionalData();
      this.delegate.writeShort(var1);
   }

   public void writeBytes(String var1) throws IOException {
      this.handleOptionalData();
      this.delegate.writeBytes(var1);
   }

   public void writeFloat(float var1) throws IOException {
      this.handleOptionalData();
      this.delegate.writeFloat(var1);
   }

   public void writeChar(int var1) throws IOException {
      this.handleOptionalData();
      this.delegate.writeChar(var1);
   }

   public void writeBoolean(boolean var1) throws IOException {
      this.handleOptionalData();
      this.delegate.writeBoolean(var1);
   }

   public void writeDouble(double var1) throws IOException {
      this.handleOptionalData();
      this.delegate.writeDouble(var1);
   }

   public void writeChars(String var1) throws IOException {
      this.handleOptionalData();
      this.delegate.writeChars(var1);
   }

   protected final void writeObjectOverride(Object var1) throws IOException {
      this.handleOptionalData();
      this.delegate.writeObject(var1);
   }

   public void writeUnshared(Object var1) throws IOException {
      this.handleOptionalData();
      this.delegate.writeObject(var1);
   }

   public void defaultWriteObject() throws IOException {
      if (this.value == null) {
         throw new NotActiveException("Not in writeObject()");
      } else if (this.dfwoHandled) {
         throw new IOException("Called defaultWriteObject()/writeFields() twice or after writing optional data.");
      } else {
         this.delegate.write_octet((byte)1);
         this.dfwoHandled = true;
         this.osc.writeFields(this.value, this.delegate);
      }
   }

   public ObjectOutputStream.PutField putFields() throws IOException {
      if (this.value == null) {
         throw new NotActiveException("Not in writeObject()");
      } else {
         if (this.putFields == null) {
            this.putFields = new PutFieldImpl();
         }

         return this.putFields;
      }
   }

   public void writeFields() throws IOException {
      if (this.value != null && this.putFields != null) {
         this.putFields.write();
      } else {
         throw new NotActiveException("Not in writeObject()");
      }
   }

   private void handleOptionalData() {
      if (!this.dfwoHandled) {
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            p("handleOptionalData() at " + this.delegate.pos());
         }

         this.delegate.write_octet((byte)0);
         this.dfwoHandled = true;
      }

      if (this.streamFormatState == 1) {
         String var1 = (new UnsyncStringBuffer("RMI:org.omg.custom.")).append(ValueHandlerImpl.getRepositoryID(this.osc.forClass()).substring(4)).toString();
         this.delegate.start_value(var1);
         this.streamFormatState = 2;
      }

   }

   public void reset() throws IOException {
      this.delegate.restoreMark(this.mark);
   }

   public void flush() throws IOException {
   }

   public void drain() throws IOException {
   }

   public void close() throws IOException {
      if (!this.dfwoHandled) {
         this.delegate.write_octet((byte)0);
      }

      if (this.streamFormatState == 1) {
         this.delegate.write_long(0);
      } else if (this.streamFormatState == 2) {
         this.delegate.end_value();
      }

      this.popCurrent();
   }

   private static void p(String var0) {
      System.out.println("<ObjectOutputStreamImpl>: " + var0);
   }

   private final class PutFieldImpl extends ObjectOutputStream.PutField {
      private final HashMap fieldMap;
      private final NumericValueHashMap primitiveFieldMap;

      private PutFieldImpl() {
         this.fieldMap = new HashMap();
         this.primitiveFieldMap = new NumericValueHashMap();
      }

      public void put(String var1, boolean var2) {
         this.primitiveFieldMap.put(var1, (long)(var2 ? 1 : 0));
      }

      public void put(String var1, byte var2) {
         this.primitiveFieldMap.put(var1, (long)var2);
      }

      public void put(String var1, char var2) {
         this.primitiveFieldMap.put(var1, (long)var2);
      }

      public void put(String var1, short var2) {
         this.primitiveFieldMap.put(var1, (long)var2);
      }

      public void put(String var1, int var2) {
         this.primitiveFieldMap.put(var1, (long)var2);
      }

      public void put(String var1, long var2) {
         this.primitiveFieldMap.put(var1, var2);
      }

      public void put(String var1, float var2) {
         this.primitiveFieldMap.put(var1, (long)Float.floatToIntBits(var2));
      }

      public void put(String var1, double var2) {
         this.primitiveFieldMap.put(var1, Double.doubleToLongBits(var2));
      }

      public void put(String var1, Object var2) {
         this.fieldMap.put(var1, var2);
      }

      public void write(ObjectOutput var1) throws IOException {
         throw new NotSerializableException("PutField.write() is not supported");
      }

      private void write() throws IOException {
         if (ObjectOutputStreamImpl.this.dfwoHandled) {
            throw new IOException("Called defaultWriteObject()/writeFields() twice or after writing optional data.");
         } else {
            if (Kernel.DEBUG && ObjectOutputStreamImpl.debugIIOPDetail.isDebugEnabled()) {
               ObjectOutputStreamImpl.p("PutField.write() writing defaultWriteObject at " + ObjectOutputStreamImpl.this.delegate.pos());
            }

            ObjectOutputStreamImpl.this.delegate.write_octet((byte)1);
            ObjectOutputStreamImpl.this.dfwoHandled = true;
            ObjectStreamField[] var1 = ObjectOutputStreamImpl.this.osc.getFields();

            for(int var2 = 0; var2 < var1.length; ++var2) {
               ObjectStreamField var3 = var1[var2];
               if (Kernel.DEBUG && ObjectOutputStreamImpl.debugIIOPDetail.isDebugEnabled()) {
                  ObjectOutputStreamImpl.p("Writing: " + var3);
               }

               switch (var3.getTypeCode()) {
                  case 'B':
                  case 'Z':
                     ObjectOutputStreamImpl.this.delegate.writeByte((byte)((int)this.primitiveFieldMap.get(var3.getName())));
                     break;
                  case 'C':
                     ObjectOutputStreamImpl.this.delegate.writeChar((char)((int)this.primitiveFieldMap.get(var3.getName())));
                     break;
                  case 'D':
                  case 'J':
                     ObjectOutputStreamImpl.this.delegate.writeLong(this.primitiveFieldMap.get(var3.getName()));
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
                     ObjectOutputStreamImpl.this.delegate.writeInt((int)this.primitiveFieldMap.get(var3.getName()));
                     break;
                  case 'L':
                  case '[':
                     ObjectOutputStreamImpl.this.delegate.writeObject(this.fieldMap.get(var3.getName()), var3.getType());
                     break;
                  case 'S':
                     ObjectOutputStreamImpl.this.delegate.writeShort((short)((int)this.primitiveFieldMap.get(var3.getName())));
               }
            }

         }
      }

      // $FF: synthetic method
      PutFieldImpl(Object var2) {
         this();
      }
   }

   private final class StreamEntry {
      private final Object value;
      private final ObjectStreamClass osc;
      private final boolean dfwoHandled;
      private final PutFieldImpl putFields;
      private final int streamFormatState;
      private final IIOPOutputStream.Marker mark;

      private StreamEntry() {
         this.mark = new IIOPOutputStream.Marker();
         this.value = ObjectOutputStreamImpl.this.value;
         this.osc = ObjectOutputStreamImpl.this.osc;
         this.dfwoHandled = ObjectOutputStreamImpl.this.dfwoHandled;
         this.putFields = ObjectOutputStreamImpl.this.putFields;
         this.streamFormatState = ObjectOutputStreamImpl.this.streamFormatState;
         this.mark.chunk = ObjectOutputStreamImpl.this.mark.chunk;
         this.mark.pos = ObjectOutputStreamImpl.this.mark.pos;
      }

      // $FF: synthetic method
      StreamEntry(Object var2) {
         this();
      }
   }
}
