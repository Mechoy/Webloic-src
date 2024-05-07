package weblogic.jms.multicast;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public final class JMSTDMSocketIPM implements JMSTDMSocket {
   private MulticastSocket sock;
   private ArrayList groups;
   private int timeout = 0;

   public JMSTDMSocketIPM(int var1) throws IOException, UnknownHostException {
      this.sock = new MulticastSocket(var1);
      this.groups = new ArrayList();
   }

   public JMSTDMSocketIPM() throws IOException, UnknownHostException {
      this.sock = new MulticastSocket();
      this.groups = new ArrayList();
   }

   public void send(byte[] var1, int var2, InetAddress var3, int var4, byte var5) throws IOException {
      DatagramPacket var6 = new DatagramPacket(var1, var2, var3, var4);
      this.sock.send(var6, var5);
   }

   public void setSoTimeout(int var1) throws IOException {
      try {
         this.sock.setSoTimeout(var1);
      } catch (SocketException var3) {
         throw new IOException(var3.toString());
      }
   }

   public void setTTL(byte var1) throws IOException {
      this.sock.setTTL(var1);
   }

   public int receive(byte[] var1) throws IOException {
      DatagramPacket var2 = new DatagramPacket(var1, var1.length);

      try {
         this.sock.receive(var2);
      } catch (InterruptedIOException var4) {
         return 0;
      }

      return var2.getLength();
   }

   public void close() {
      if (this.sock != null) {
         for(int var1 = 0; var1 < this.groups.size(); ++var1) {
            try {
               this.sock.leaveGroup((InetAddress)this.groups.get(var1));
            } catch (IOException var3) {
            }
         }

         this.sock.close();
         this.sock = null;
         this.groups = null;
      }
   }

   public void joinGroup(InetAddress var1) throws IOException {
      if (this.sock == null) {
         throw new IOException("Socket is closed");
      } else if (this.groups.indexOf(var1) >= 0) {
         throw new IOException("Group exists");
      } else {
         this.sock.joinGroup(var1);
         this.groups.add(var1);
      }
   }

   public void leaveGroup(InetAddress var1) throws IOException {
      if (this.sock == null) {
         throw new IOException("Socket is closed");
      } else if (this.groups.indexOf(var1) == -1) {
         throw new IOException("Group not found");
      } else {
         this.sock.leaveGroup(var1);
         this.groups.remove(var1);
      }
   }

   void setTimeout(int var1) {
      this.timeout = var1;
   }
}
