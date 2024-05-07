package weblogic.xml.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import weblogic.xml.babel.stream.CanonicalInputStream;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLInputStreamFactory;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLOutputStreamFactory;
import weblogic.xml.stream.XMLStreamException;

public class CanonicalDiff {
   private static final boolean debug = false;

   public static byte[] getXMLAsArray(String var0) throws XMLStreamException, IOException {
      XMLInputStreamFactory var1 = XMLInputStreamFactory.newInstance();
      XMLInputStream var2 = var1.newDTDAwareInputStream(new FileInputStream(var0));
      XMLOutputStreamFactory var3 = XMLOutputStreamFactory.newInstance();
      ByteArrayOutputStream var4 = new ByteArrayOutputStream();
      XMLOutputStream var5 = var3.newCanonicalOutputStream(var4);
      var5.add(var2);
      var5.flush();
      return var4.toByteArray();
   }

   public static XMLInputStream getXMLAsStream(String var0) throws XMLStreamException, IOException {
      XMLInputStreamFactory var1 = XMLInputStreamFactory.newInstance();
      XMLInputStream var2 = var1.newDTDAwareInputStream(new FileInputStream(var0));
      return new CanonicalInputStream(var2);
   }

   public static boolean equals(XMLInputStream var0, XMLInputStream var1) throws XMLStreamException, IOException {
      while(true) {
         if (var0.hasNext()) {
            if (!var1.hasNext()) {
               return false;
            }

            if (var0.next().equals(var1.next())) {
               continue;
            }

            return false;
         }

         return !var1.hasNext();
      }
   }

   public static boolean equals(byte[] var0, byte[] var1) {
      if (var0.length != var1.length) {
         return false;
      } else {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            if (var0[var2] != var1[var2]) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean compare(String var0, String var1) throws XMLStreamException, IOException {
      byte[] var2 = getXMLAsArray(var0);
      byte[] var3 = getXMLAsArray(var1);
      return equals(var2, var3);
   }

   public static boolean compare2(String var0, String var1) throws XMLStreamException, IOException {
      XMLInputStream var2 = getXMLAsStream(var0);
      XMLInputStream var3 = getXMLAsStream(var1);
      if (equals(var2, var3)) {
         return true;
      } else {
         debug2(var0, var1);
         return false;
      }
   }

   public static void debug2(String var0, String var1) throws XMLStreamException, IOException {
      XMLInputStream var2 = getXMLAsStream(var0);

      XMLInputStream var3;
      XMLEvent var5;
      for(var3 = getXMLAsStream(var1); var2.hasNext(); System.out.println("e2[" + var5 + "]")) {
         if (!var3.hasNext()) {
            System.out.println("Stream 2 has no more elements");
            return;
         }

         XMLEvent var4 = var2.next();
         var5 = var3.next();
         System.out.print("e1[" + var4 + "]");
         if (!var4.equals(var5)) {
            System.out.print(" != ");
         } else {
            System.out.print("  = ");
         }
      }

      if (var3.hasNext()) {
         System.out.println("Stream 2 has more elements");
      }

   }

   public static void debug(byte[] var0, byte[] var1) {
      for(int var2 = 0; var2 < var0.length; ++var2) {
         if (var0[var2] == var1[var2]) {
            System.out.print("[equal]");
         } else {
            System.out.print("[diff ]");
         }

         System.out.println("byte[" + var2 + "]:\t\t" + var0[var2] + ":" + var1[var2] + "\t--\t" + (char)var0[var2] + ":" + (char)var1[var2]);
         if (var0[var2] != var1[var2]) {
            break;
         }
      }

   }

   public static void print(String var0) throws XMLStreamException, IOException {
      byte[] var1 = getXMLAsArray(var0);
      System.out.println("---------------- [ As String ] -------------");
      System.out.print(new String(var1));
      System.out.println("---------------- [ As Bytes  ] -------------");

      for(int var2 = 0; var2 < var1.length; ++var2) {
         System.out.print(var1[var2]);
      }

   }

   public static void main(String[] var0) throws Exception {
      if (compare(var0[0], var0[1])) {
         System.out.println(var0[0] + " = " + var0[1]);
      } else {
         System.out.println(var0[0] + " != " + var0[1]);
      }

      if (compare2(var0[0], var0[1])) {
         System.out.println(var0[0] + " s= " + var0[1]);
      } else {
         System.out.println(var0[0] + " s!= " + var0[1]);
      }

      if (var0.length == 3) {
         System.out.println("---------------- [ Debug ] -------------");
         debug(getXMLAsArray(var0[0]), getXMLAsArray(var0[1]));
      }

   }
}
