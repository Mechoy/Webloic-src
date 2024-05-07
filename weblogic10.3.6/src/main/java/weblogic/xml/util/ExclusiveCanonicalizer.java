package weblogic.xml.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import weblogic.xml.babel.stream.ExclusiveCanonicalWriter;
import weblogic.xml.babel.stream.XMLOutputStreamBase;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLInputStreamFactory;

public final class ExclusiveCanonicalizer {
   public static void main(String[] var0) throws Exception {
      String var1 = var0[0];
      String var2 = var0[1];
      XMLInputStream var3 = XMLInputStreamFactory.newInstance().newInputStream(new File(var1));
      var3.skip(new weblogic.xml.stream.events.Name(var2), 2);
      ExclusiveCanonicalWriter var4 = new ExclusiveCanonicalWriter(new OutputStreamWriter(new FileOutputStream("out.xml"), "utf-8"));
      XMLOutputStreamBase var5 = new XMLOutputStreamBase(var4);
      var5.add(var3.getSubStream());
      var5.flush();
   }
}
