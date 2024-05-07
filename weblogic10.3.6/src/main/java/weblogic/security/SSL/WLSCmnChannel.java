package weblogic.security.SSL;

import com.bea.sslplus.SSLNioSocket;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;
import java.security.InvalidParameterException;
import javax.net.ssl.SSLSocket;

public class WLSCmnChannel {
   boolean isNio;
   private SocketChannel sockChan;
   private WLSSSLNioSocket nioSock;
   private Socket OrgSock;

   public WLSCmnChannel(Socket var1) {
      if (var1 == null) {
         throw new InvalidParameterException("The passed in SocketChannel instance is null");
      } else {
         if (var1 instanceof SSLNioSocket) {
            this.isNio = true;
            this.nioSock = (WLSSSLNioSocket)var1;
         } else if (var1 instanceof SSLSocket) {
            this.isNio = false;
            this.sockChan = var1.getChannel();
         } else {
            if (!(var1 instanceof Socket)) {
               throw new InvalidParameterException("The passed in SocketChannel is not a SSL socket");
            }

            this.isNio = false;
            this.sockChan = var1.getChannel();
         }

         this.OrgSock = var1;
      }
   }

   public WLSCmnChannel(WLSSSLNioSocket var1) {
      this((Socket)var1);
   }

   public SelectableChannel getSelectableChannel() {
      return (SelectableChannel)(this.isNio ? this.nioSock.getSelectableChannel() : this.sockChan);
   }

   public int write(ByteBuffer var1) throws IOException {
      return this.isNio ? this.nioSock.getWritableByteChannel().write(var1) : this.sockChan.write(var1);
   }

   public long write(ByteBuffer[] var1) throws IOException {
      if (this.isNio) {
         throw new UnsupportedOperationException("Not supported for the non-blocking SSL mode, use the other write method, write(ByteBuffer src)");
      } else {
         return this.sockChan.write(var1);
      }
   }

   public long write(ByteBuffer[] var1, int var2, int var3) throws IOException {
      if (this.isNio) {
         throw new UnsupportedOperationException("Not supported for the non-blocking SSL mode, use the other write method, write(ByteBuffer src)");
      } else {
         return this.sockChan.write(var1, var2, var3);
      }
   }

   public int read(ByteBuffer var1) throws IOException {
      return this.isNio ? this.nioSock.getReadableByteChannel().read(var1) : this.sockChan.read(var1);
   }

   public final long read(ByteBuffer[] var1) throws IOException {
      if (this.isNio) {
         throw new UnsupportedOperationException("Not supported for the non-blocking SSL mode, use the alternative read method, read(ByteBuffer dst)");
      } else {
         return this.sockChan.read(var1, 0, var1.length);
      }
   }

   public long read(ByteBuffer[] var1, int var2, int var3) throws IOException {
      if (this.isNio) {
         throw new UnsupportedOperationException("Not supported for the non-blocking SSL mode, use the alternative read method, read(ByteBuffer dst)");
      } else {
         return this.sockChan.read(var1, var2, var3);
      }
   }

   public void close() throws IOException {
      if (this.isNio) {
         this.OrgSock.close();
      } else {
         this.sockChan.close();
      }

   }
}
