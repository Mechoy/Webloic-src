package weblogic.jdbc.common.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.rmi.NoSuchObjectException;
import weblogic.rmi.extensions.server.SmartStubInfo;
import weblogic.rmi.server.UnicastRemoteObject;

public class JDBCOutputStreamImpl extends OutputStream implements JDBCOutputStream, SmartStubInfo {
   OutputStream t2_os;
   boolean verbose = false;
   boolean closed = false;
   int block_size;
   private transient JDBCOutputStreamStub osstub = null;

   public JDBCOutputStreamImpl(OutputStream var1, boolean var2, int var3) {
      this.t2_os = var1;
      this.verbose = var2;
      this.block_size = var3;
   }

   public void write(int var1) throws IOException {
      if (this.closed) {
         throw new IOException("ERROR OutputStream has been closed and cannot be used");
      } else {
         this.t2_os.write(var1);
      }
   }

   public void write(byte[] var1) throws IOException {
      if (this.closed) {
         throw new IOException("ERROR OutputStream has been closed and cannot be used");
      } else {
         this.t2_os.write(var1);
      }
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (this.closed) {
         throw new IOException("ERROR OutputStream has been closed and cannot be used");
      } else {
         this.t2_os.write(var1, var2, var3);
      }
   }

   public void flush() throws IOException {
      if (this.closed) {
         throw new IOException("ERROR OutputStream has been closed and cannot be used");
      } else {
         this.t2_os.flush();
      }
   }

   public void close() throws IOException {
      try {
         UnicastRemoteObject.unexportObject(this, true);
      } catch (NoSuchObjectException var2) {
      }

      if (!this.closed) {
         this.t2_os.close();
         this.closed = true;
         this.t2_os = null;
         this.osstub = null;
      }
   }

   public void writeBlock(byte[] var1) throws IOException {
      if (this.closed) {
         throw new IOException("ERROR OutputStream has been closed and cannot be used");
      } else {
         this.t2_os.write(var1);
      }
   }

   public Object getSmartStub(Object var1) {
      if (this.osstub == null) {
         this.osstub = new JDBCOutputStreamStub((JDBCOutputStream)var1, this.verbose, this.block_size);
      }

      return this.osstub;
   }
}
