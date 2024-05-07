package weblogic.xml.util;

import java.io.File;
import java.io.FileOutputStream;
import weblogic.xml.stream.ElementFactory;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLInputStreamFactory;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLOutputStreamFactory;

public class Merge {
   public static void main(String[] var0) throws Exception {
      if (var0.length != 3) {
         System.out.println("Usage: java weblogic.xml.util.Merge");
         System.out.println("[filename1] [filename2] [outputfile]");
         System.exit(0);
      }

      XMLInputStream var1 = XMLInputStreamFactory.newInstance().newInputStream(new File(var0[0]));
      var1.skip(2);
      StartElement var2 = (StartElement)var1.next();
      XMLInputStream var3 = XMLInputStreamFactory.newInstance().newInputStream(new File(var0[1]));
      var3.skip(2);
      var3.next();
      XMLOutputStream var4 = XMLOutputStreamFactory.newInstance().newDebugOutputStream(new FileOutputStream(new File(var0[2])));
      var4.add(var2);
      var4.add(var1.getSubStream());
      var4.add(var3.getSubStream());
      var4.add(ElementFactory.createEndElement(var2.getName()));
      var4.flush();
   }
}
