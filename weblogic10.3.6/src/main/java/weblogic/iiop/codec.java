package weblogic.iiop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;
import weblogic.utils.Getopt2;
import weblogic.utils.Hex;

public class codec {
   private static final String PROGRAM = "weblogic.iiop.codec";
   private static int fpos = 0;

   public static void main(String[] var0) throws Exception {
      Class var1 = null;
      fpos = 0;
      Getopt2 var2 = new Getopt2();
      var2.addFlag("sun", "Decode/encode using Sun ORB");
      var2.addFlag("wls", "Decode/encode using WLS ORB");
      var2.addFlag("rewrite", "Rewrite message using WLS ORB");
      var2.addFlag("value", "Write valuetype");
      var2.addOption("o", "outfile", "Output file");
      var2.addOption("f", "infile", "Input file");
      var2.setUsageArgs("<filename> [<return type>]");
      if (var0.length == 0) {
         var2.usageAndExit("weblogic.iiop.codec");
      }

      var2.grok(var0);
      String var3 = var2.getOption("f");
      String var4 = var2.getOption("o");
      if (var3 == null && var2.args().length > 0) {
         var3 = var2.args()[0];
      }

      if (var2.args().length > 1) {
         var1 = Class.forName(var2.args()[1]);
      }

      boolean var5 = var2.hasOption("sun");
      boolean var6 = var2.hasOption("wls");
      if (var2.hasOption("value")) {
         Serializable var7 = (Serializable)var1.newInstance();
         FileWriter var8 = new FileWriter(var3);
         Object var9 = null;
         if (var5) {
            var9 = (OutputStream)ORB.init(new String[0], (Properties)null).create_output_stream();
         } else {
            IIOPClient.initialize();
            var9 = new IIOPOutputStream();
         }

         writeValue((OutputStream)var9, var7, var8);
         var8.close();
         System.exit(0);
      }

      File var13 = new File(var3);
      if (!var13.exists()) {
         fatal("file " + var3 + " does not exist");
      }

      FileReader var14 = new FileReader(var13);
      int var15 = 0;

      while(skipTo(var14, "<BEA-002031>") > 0) {
         ByteArrayOutputStream var10 = new ByteArrayOutputStream();
         skipTo(var14, "\n");

         while(readHexLine(var14, var10) > 0) {
            skipTo(var14, "\n");
         }

         byte[] var11 = var10.toByteArray();
         if (var2.hasOption("rewrite")) {
            if (var4 == null) {
               var4 = "message" + var15++ + ".msg";
            }

            FileWriter var12 = new FileWriter(var4 == null ? "message" + var15++ + ".msg" : var4);
            if (var5) {
               rewriteWithCDR(var11, var1, var12);
            } else {
               rewriteWithWLS(var11, var1, var12);
            }

            var12.close();
         } else if (var6) {
            readWithWLS(var11, var1);
         } else if (var5) {
            readWithCDR(var11, var1);
         }
      }

      var14.close();
   }

   private static void readWithWLS(byte[] var0, Class var1) {
      IIOPInputStream var2 = new IIOPInputStream(var0);
      Message var3 = createMessage(var2);
      if (var1 != null && var3 instanceof ReplyMessage) {
         Serializable var4 = var3.getInputStream().read_value(var1);
         debug("Read return value: " + var4);
      }

      var2.close();
   }

   private static void rewriteWithWLS(byte[] var0, Class var1, FileWriter var2) throws IOException {
      IIOPInputStream var3 = new IIOPInputStream(var0);
      IIOPOutputStream var4 = new IIOPOutputStream();
      rewrite(var3, var1, var4, var2);
   }

   private static void rewriteWithCDR(byte[] var0, Class var1, FileWriter var2) throws IOException {
      IIOPInputStream var3 = new IIOPInputStream(var0);
      OutputStream var4 = createCDROutputStream();
      rewrite(var3, var1, var4, var2);
   }

   private static void rewrite(InputStream var0, Class var1, OutputStream var2, FileWriter var3) throws IOException {
      byte[] var4 = readMessage(var0);
      var2.write_octet_array(var4, 0, var4.length);
      if (var4.length % 8 > 0) {
         for(int var5 = 0; var5 < 8 - var4.length % 8; ++var5) {
            var0.read_octet();
            var2.write_octet((byte)0);
         }
      }

      if (var1 != null) {
         Serializable var6 = var0.read_value(var1);
         debug("Read return value: " + var6);
         var2.write_value(var6);
      }

      dumpBuffer(var2, var3);
      var0.close();
   }

   private static byte[] readMessage(InputStream var0) throws IOException {
      OutputStream var1 = (OutputStream)var0.orb().create_output_stream();

      int var2;
      for(var2 = 0; var2 < 5; ++var2) {
         var1.write_long(var0.read_long());
      }

      var2 = var0.read_long();
      var1.write_long(var2);

      for(int var3 = 0; var3 < var2; ++var3) {
         var1.write_long(var0.read_long());
         int var4 = var0.read_long();
         byte[] var5 = new byte[var4];
         var0.read_octet_array(var5, 0, var4);
         var1.write_long(var4);
         var1.write_octet_array(var5, 0, var4);
      }

      byte[] var6 = streamToBuf(var1);
      var1.close();
      return var6;
   }

   private static void writeValue(OutputStream var0, Serializable var1, FileWriter var2) throws IOException {
      var0.write_value(var1);
      dumpBuffer(var0, var2);
   }

   private static void dumpBuffer(OutputStream var0, FileWriter var1) throws IOException {
      String var2 = "<BEA-002031>\n";
      var1.write(var2, 0, var2.length());
      byte[] var3 = streamToBuf(var0);
      String var4 = Hex.dump(var3, 0, var3.length);
      var1.write(var4, 0, var4.length());
      var1.write(">\n", 0, 2);
      var0.close();
   }

   public static byte[] streamToBuf(OutputStream var0) throws IOException {
      InputStream var1 = (InputStream)var0.create_input_stream();
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();

      try {
         while(true) {
            var2.write(var1.read_octet() & 255);
         }
      } catch (MARSHAL var4) {
         byte[] var3 = var2.toByteArray();
         var2.close();
         var1.close();
         return var3;
      }
   }

   public static void readWithCDR(byte[] var0, Class var1) throws IOException {
      InputStream var2 = createCDRStream(var0);
      readMessage(var2);
      debug("Reading value");
      Serializable var3 = var2.read_value(var1);
      debug("Read return value: " + var3);
   }

   public static InputStream createCDRStream(byte[] var0) throws IOException {
      Properties var1 = new Properties();
      var1.put("org.omg.CORBA.ORBClass", "com.sun.corba.se.internal.POA.POAORB");
      ORB var2 = ORB.init(new String[0], var1);
      OutputStream var3 = (OutputStream)var2.create_output_stream();
      var3.write_octet_array(var0, 0, var0.length);
      InputStream var4 = (InputStream)var3.create_input_stream();
      var3.close();
      return var4;
   }

   public static OutputStream createCDROutputStream() throws IOException {
      Properties var0 = new Properties();
      ORB var1 = ORB.init(new String[0], var0);
      return (OutputStream)var1.create_output_stream();
   }

   private static int readHexLine(FileReader var0, java.io.OutputStream var1) throws IOException {
      if (var0.ready() && var0.read() != 62) {
         var0.skip(6L);

         for(int var2 = 0; var2 < 8; ++var2) {
            byte[] var3 = new byte[4];

            for(int var4 = 0; var4 < 4; ++var4) {
               var3[var4] = (byte)(255 & var0.read());
            }

            byte[] var5 = Hex.fromHexString(var3, 4);
            var1.write(var5);
            var0.skip(1L);
         }

         return 1;
      } else {
         return -1;
      }
   }

   private static int skipTo(FileReader var0, String var1) throws IOException {
      label30:
      while(var0.ready()) {
         while(var0.ready() && var0.read() != var1.charAt(0)) {
            ++fpos;
         }

         for(int var2 = 1; var2 < var1.length(); ++var2) {
            ++fpos;
            if (var0.read() != var1.charAt(var2)) {
               continue label30;
            }
         }

         return fpos;
      }

      return -1;
   }

   public static Message createMessage(IIOPInputStream var0) {
      MessageHeader var1 = new MessageHeader(var0);
      var0.setSupportsJDK13Chunking(var1.getMinorVersion() < 2);
      switch (var1.getMsgType()) {
         case 0:
            return new RequestMessage((EndPoint)null, var1, var0);
         case 1:
            return new ReplyMessage((EndPoint)null, var1, var0);
         case 7:
            return new FragmentMessage((EndPoint)null, var1, var0);
         default:
            fatal("Unknown message type: " + var1.getMsgType());
            return null;
      }
   }

   private static void fatal(String var0) {
      System.out.println(var0);
      System.exit(-1);
   }

   private static void debug(String var0) {
      System.out.println(var0);
   }

   private static void skip(InputStream var0, long var1) {
      for(int var3 = 0; (long)var3 < var1; ++var3) {
         var0.read_octet();
      }

   }
}
