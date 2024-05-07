package weblogic.xml.util;

import java.io.File;
import java.io.FileInputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import weblogic.xml.stax.ReaderToWriter;

public class PrettyPrint {
   public static void main(String[] var0) throws Exception {
      if (var0.length != 1) {
         System.out.println("Usage: java weblogic.xml.util.PrettyPrint [file]");
      } else {
         XMLStreamWriter var1 = XMLOutputFactory.newInstance().createXMLStreamWriter(System.out);
         PrettyXMLStreamWriter var3 = new PrettyXMLStreamWriter(var1, 2);
         File var4 = new File(var0[0]);
         FileInputStream var5 = new FileInputStream(var4);
         javax.xml.stream.XMLStreamReader var6 = XMLInputFactory.newInstance().createXMLStreamReader(var5);
         ReaderToWriter var7 = new ReaderToWriter(var3);
         var7.writeAll(var6);
         var3.flush();
         var5.close();
      }
   }
}
