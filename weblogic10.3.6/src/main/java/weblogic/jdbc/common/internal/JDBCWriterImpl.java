package weblogic.jdbc.common.internal;

import java.io.IOException;
import java.io.Writer;
import java.rmi.NoSuchObjectException;
import weblogic.rmi.extensions.server.SmartStubInfo;
import weblogic.rmi.server.UnicastRemoteObject;

public class JDBCWriterImpl extends Writer implements JDBCWriter, SmartStubInfo {
   Writer t2_wtr;
   boolean verbose = false;
   boolean closed = false;
   int block_size;
   private transient JDBCWriterStub wtrstub = null;

   public JDBCWriterImpl(Writer var1, boolean var2, int var3) {
      this.block_size = var3;
      this.verbose = var2;
      this.t2_wtr = var1;
   }

   public void writeBlock(char[] var1) throws IOException {
      if (this.closed) {
         throw new IOException("ERROR Writer has been closed and cannot be used");
      } else {
         this.t2_wtr.write(var1);
      }
   }

   public void write(char[] var1, int var2, int var3) throws IOException {
      if (this.closed) {
         throw new IOException("ERROR Writer has been closed and cannot be used");
      } else {
         this.t2_wtr.write(var1, var2, var3);
      }
   }

   public void flush() throws IOException {
      if (this.closed) {
         throw new IOException("ERROR Writer has been closed and cannot be used");
      } else {
         this.t2_wtr.flush();
      }
   }

   public void close() throws IOException {
      try {
         UnicastRemoteObject.unexportObject(this, true);
      } catch (NoSuchObjectException var2) {
      }

      if (!this.closed) {
         this.t2_wtr.close();
         this.closed = true;
         this.t2_wtr = null;
         this.wtrstub = null;
      }
   }

   public Object getSmartStub(Object var1) {
      if (this.wtrstub == null) {
         this.wtrstub = new JDBCWriterStub((JDBCWriter)var1, this.verbose, this.block_size);
      }

      return this.wtrstub;
   }
}
