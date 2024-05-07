package weblogic.iiop;

import java.io.IOException;
import org.omg.CORBA.SystemException;
import weblogic.corba.utils.RemoteInfo;
import weblogic.rmi.spi.MsgInput;

public abstract class AbstractMsgInput implements MsgInput {
   protected static final boolean DEBUG = false;
   protected final IIOPInputStream delegate;

   public AbstractMsgInput(IIOPInputStream var1) {
      this.delegate = var1;
   }

   public final void readFully(byte[] var1) throws IOException {
      try {
         this.delegate.readFully(var1);
      } catch (SystemException var3) {
         throw Utils.mapSystemException(var3);
      }
   }

   public final void readFully(byte[] var1, int var2, int var3) throws IOException {
      try {
         this.delegate.readFully(var1, var2, var3);
      } catch (SystemException var5) {
         throw Utils.mapSystemException(var5);
      }
   }

   public final int skipBytes(int var1) throws IOException {
      try {
         return this.delegate.skipBytes(var1);
      } catch (SystemException var3) {
         throw Utils.mapSystemException(var3);
      }
   }

   public final boolean readBoolean() throws IOException {
      try {
         return this.delegate.readBoolean();
      } catch (SystemException var2) {
         throw Utils.mapSystemException(var2);
      }
   }

   public final int readInt() throws IOException {
      try {
         return this.delegate.readInt();
      } catch (SystemException var2) {
         throw Utils.mapSystemException(var2);
      }
   }

   public final short readShort() throws IOException {
      try {
         return this.delegate.readShort();
      } catch (SystemException var2) {
         throw Utils.mapSystemException(var2);
      }
   }

   public final int readUnsignedShort() throws IOException {
      try {
         return this.delegate.readUnsignedShort();
      } catch (SystemException var2) {
         throw Utils.mapSystemException(var2);
      }
   }

   public final long readLong() throws IOException {
      try {
         return this.delegate.readLong();
      } catch (SystemException var2) {
         throw Utils.mapSystemException(var2);
      }
   }

   public final double readDouble() throws IOException {
      try {
         return this.delegate.readDouble();
      } catch (SystemException var2) {
         throw Utils.mapSystemException(var2);
      }
   }

   public final float readFloat() throws IOException {
      try {
         return this.delegate.readFloat();
      } catch (SystemException var2) {
         throw Utils.mapSystemException(var2);
      }
   }

   public final byte readByte() throws IOException {
      try {
         return this.delegate.readByte();
      } catch (SystemException var2) {
         throw Utils.mapSystemException(var2);
      }
   }

   public final int readUnsignedByte() throws IOException {
      try {
         return this.delegate.readUnsignedByte();
      } catch (SystemException var2) {
         throw Utils.mapSystemException(var2);
      }
   }

   final Object readRemote(Class var1) throws IOException {
      try {
         IOR var2 = new IOR(this.delegate);
         if (var1 != null && IDLUtils.isARemote(var1)) {
            RemoteInfo var3 = RemoteInfo.findRemoteInfo(var1);
            IIOPReplacer.getIIOPReplacer();
            return IIOPReplacer.resolveObject(var2, var3);
         } else {
            IIOPReplacer.getIIOPReplacer();
            return IIOPReplacer.resolveObject(var2);
         }
      } catch (SystemException var4) {
         throw Utils.mapSystemException(var4);
      }
   }

   public void close() throws IOException {
      try {
         this.delegate.close();
      } catch (SystemException var2) {
         throw Utils.mapSystemException(var2);
      }
   }

   public Object readObject() throws ClassNotFoundException, IOException {
      return this.delegate.readObject();
   }

   public final int available() throws IOException {
      return this.delegate.available();
   }

   public final long skip(long var1) {
      return this.delegate.skip(var1);
   }

   public final int read(byte[] var1, int var2, int var3) throws IOException {
      try {
         return this.delegate.read(var1, var2, var3);
      } catch (SystemException var5) {
         throw Utils.mapSystemException(var5);
      }
   }

   public final int read(byte[] var1) throws IOException {
      try {
         return this.delegate.read(var1);
      } catch (SystemException var3) {
         throw Utils.mapSystemException(var3);
      }
   }

   public final int read() throws IOException {
      try {
         return this.delegate.read();
      } catch (SystemException var2) {
         throw Utils.mapSystemException(var2);
      }
   }

   abstract AbstractMsgOutput createMsgOutput(IIOPOutputStream var1);

   protected final void p(String var1) {
      System.out.println("<AbstractMsgInput>: " + var1);
   }

   static {
      IIOPService.load();
   }
}
