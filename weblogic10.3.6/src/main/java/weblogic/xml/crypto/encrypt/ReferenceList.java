package weblogic.xml.crypto.encrypt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.XMLStructure;
import weblogic.xml.crypto.encrypt.api.ReferenceType;
import weblogic.xml.crypto.utils.StaxUtils;

public class ReferenceList implements XMLStructure {
   public static final String TAG_REFERENCE_LIST = "ReferenceList";
   private List references;

   public ReferenceList() {
   }

   public ReferenceList(List var1) {
      this.references = var1;
   }

   public List getReferences() {
      return this.references;
   }

   public static String toString(List var0) {
      StringBuffer var1 = new StringBuffer();
      var1.append("ReferenceList:\n");
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         var1.append("  ").append(var2.next()).append("\n");
      }

      return var1.toString();
   }

   public static List newInstance(XMLStreamReader var0) throws MarshalException {
      List var1 = read(var0);
      return var1;
   }

   public static void write(XMLStreamWriter var0, List var1) throws MarshalException {
      try {
         var0.writeStartElement("http://www.w3.org/2001/04/xmlenc#", "ReferenceList");
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            WLReferenceType var3 = (WLReferenceType)var2.next();
            var3.write(var0);
         }

         var0.writeEndElement();
      } catch (XMLStreamException var4) {
         throw new MarshalException(var4);
      }
   }

   public static List read(XMLStreamReader var0, boolean var1) throws MarshalException {
      ArrayList var2 = new ArrayList();

      try {
         if (StaxUtils.findStart(var0, "http://www.w3.org/2001/04/xmlenc#", "ReferenceList", false)) {
            var0.next();

            while(!var0.isEndElement()) {
               ReferenceType var3 = WLReferenceType.newInstance(var0);
               var2.add(var3);
            }

            if (var1) {
               StaxUtils.readEnd(var0, "http://www.w3.org/2001/04/xmlenc#", "ReferenceList");
            }
         }
      } catch (XMLStreamException var4) {
         throw new MarshalException(var4);
      }

      return Collections.unmodifiableList(var2);
   }

   public static List read(XMLStreamReader var0) throws MarshalException {
      return read(var0, true);
   }

   public static void main(String[] var0) throws Exception {
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }
}
