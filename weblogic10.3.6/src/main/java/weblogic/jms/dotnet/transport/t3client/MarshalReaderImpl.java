package weblogic.jms.dotnet.transport.t3client;

import java.io.DataInput;
import java.io.IOException;
import weblogic.jms.dotnet.transport.MarshalReadable;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.Transport;
import weblogic.utils.io.ChunkedDataInputStream;

public class MarshalReaderImpl implements MarshalReader {
   private final ChunkedDataInputStream cdis;
   private final Transport transport;

   MarshalReaderImpl(ChunkedDataInputStream var1, Transport var2) {
      this.cdis = var1;
      this.transport = var2;
   }

   public Transport getTransport() {
      return this.transport;
   }

   public MarshalReadable readMarshalable() {
      int var1 = this.readInt();
      int var2 = this.readInt();
      int var3 = this.cdis.pos() + var2;
      if (this.cdis.peek(var2) == -1) {
         throw new RuntimeException("EOF detected. Stream does not have enough bytes for reading entire MarshalReadable object(Marshal type code=" + var1 + ")");
      } else {
         MarshalReadable var4 = this.transport.createMarshalReadable(var1);
         var4.unmarshal(this);
         if (this.cdis.pos() < var3) {
            try {
               this.cdis.skip((long)(var3 - this.cdis.pos()));
            } catch (IOException var6) {
               throw new RuntimeException(var6);
            }
         }

         return var4;
      }
   }

   public int read() {
      try {
         return this.cdis.read();
      } catch (IOException var2) {
         throw new RuntimeException(var2);
      }
   }

   public byte readByte() {
      try {
         return this.cdis.readByte();
      } catch (IOException var2) {
         throw new RuntimeException(var2);
      }
   }

   public int read(byte[] var1, int var2, int var3) {
      try {
         return this.cdis.read(var1, var2, var3);
      } catch (IOException var5) {
         throw new RuntimeException(var5);
      }
   }

   public boolean readBoolean() {
      try {
         return this.cdis.readBoolean();
      } catch (IOException var2) {
         throw new RuntimeException(var2);
      }
   }

   public short readShort() {
      try {
         return this.cdis.readShort();
      } catch (IOException var2) {
         throw new RuntimeException(var2);
      }
   }

   public char readChar() {
      try {
         return this.cdis.readChar();
      } catch (IOException var2) {
         throw new RuntimeException(var2);
      }
   }

   public int readInt() {
      try {
         return this.cdis.readInt();
      } catch (IOException var2) {
         throw new RuntimeException(var2);
      }
   }

   public long readLong() {
      try {
         return this.cdis.readLong();
      } catch (IOException var2) {
         throw new RuntimeException(var2);
      }
   }

   public float readFloat() {
      try {
         return this.cdis.readFloat();
      } catch (IOException var2) {
         throw new RuntimeException(var2);
      }
   }

   public double readDouble() {
      try {
         return this.cdis.readDouble();
      } catch (IOException var2) {
         throw new RuntimeException(var2);
      }
   }

   public String readString() {
      try {
         return this.cdis.readUTF8();
      } catch (IOException var2) {
         throw new RuntimeException(var2);
      }
   }

   public byte[] readStringAsBytes() {
      try {
         int var1 = this.cdis.peekInt();
         byte[] var2 = new byte[var1 + 4];
         this.cdis.read(var2, 0, var2.length);
         return var2;
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void internalClose() {
      this.cdis.close();
   }

   public DataInput getDataInputStream() {
      return this.cdis;
   }
}
