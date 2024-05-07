package weblogic.xml.util.xed;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLOutputStreamFactory;
import weblogic.xml.stream.XMLStreamException;
import weblogic.xml.xpath.XPathException;

public class StreamEditor {
   private static final boolean VERBOSE = true;

   public static void outputInsertions(Iterator var0, XMLOutputStream var1) throws XMLStreamException {
      while(var0.hasNext()) {
         XMLInputStream var2 = (XMLInputStream)var0.next();
         var1.add(var2);
      }

   }

   public static void operate(String var0, String var1, XMLOutputStream var2) throws XPathException, StreamEditorException, XMLStreamException, IOException {
      XEDParser var3 = new XEDParser(new FileReader(var0));
      Operation var4 = var3.parse();
      System.out.println("[Operation Loaded]");
      System.out.println(var4);
      var4.prepare();
      XMLInputStream var5 = var4.getStream(var1);

      while(var5.hasNext()) {
         XMLEvent var6 = var5.next();
         ArrayList var7 = var4.getPre();
         ArrayList var8 = var4.getPost();
         ArrayList var9 = var4.getChild();
         if (var6.isStartElement()) {
            outputInsertions(var7.iterator(), var2);
         }

         if (!var4.needToDelete()) {
            var2.add(var6);
         }

         if (var6.isStartElement()) {
            outputInsertions(var9.iterator(), var2);
         }

         if (var6.isEndElement()) {
            outputInsertions(var8.iterator(), var2);
         }
      }

   }

   public static void main(String[] var0) throws Exception {
      String var1 = var0[0];
      String var2 = var0[1];
      String var3 = var0[2];
      XMLOutputStreamFactory var4 = XMLOutputStreamFactory.newInstance();
      XMLOutputStream var5 = var4.newOutputStream(new FileOutputStream(var3));
      operate(var1, var2, var5);
      var5.flush();
   }
}
