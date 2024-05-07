package weblogic.xml.security.wsu.v200207;

import java.util.Calendar;
import weblogic.xml.security.utils.TestUtils;
import weblogic.xml.security.utils.ValidationException;
import weblogic.xml.security.utils.XMLReader;
import weblogic.xml.security.utils.XMLWriter;
import weblogic.xml.security.wsu.Received;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class ReceivedImpl extends AttributedDateTimeBase implements Received {
   private String delay;

   public ReceivedImpl() {
      this.delay = null;
   }

   public ReceivedImpl(Calendar var1) {
      super(var1);
      this.delay = null;
   }

   public ReceivedImpl(String var1) {
      super(var1);
      this.delay = null;
   }

   public ReceivedImpl(String var1, int var2) {
      this(var1);
      this.delay = Integer.toString(var2);
   }

   public ReceivedImpl(XMLInputStream var1) throws XMLStreamException {
      this.delay = null;
      this.fromXMLInternal(var1);
   }

   public ReceivedImpl(XMLReader var1) throws ValidationException {
      this.delay = null;
      this.fromXMLInternal(var1);
   }

   protected final String getLocalName() {
      return "Received";
   }

   public int getDelay() {
      return this.delay != null ? Integer.decode(this.delay) : 0;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(super.toString());
      var1.append("\n   Delay:     ").append(this.delay);
      return var1.toString();
   }

   protected final void readAttributes(XMLReader var1) {
      super.readAttributes(var1);
      this.delay = var1.getAttribute(WSU_URI, "Delay");
   }

   protected void writeAttributes(XMLWriter var1) {
      super.writeAttributes(var1);
      if (this.delay != null) {
         var1.writeAttribute(WSU_URI, "Delay", this.delay);
      }

   }

   public static void main(String[] var0) throws Exception {
      XMLInputStream var1 = var0.length > 0 ? TestUtils.createXMLInputStreamFromFile(var0[0]) : TestUtils.createXMLInputStreamFromString("<wsu:Received xmlns:wsu=\"" + WSU_URI + "\"\n" + "      wsu:Id=\"myId\"" + "      wsu:ValueType=\"myBadQName\"" + "      wsu:Delay=\"1231\" >" + "2002-09-26T16:13:15Z" + "</wsu:Received>");
      ReceivedImpl var2 = new ReceivedImpl(var1);
      System.out.println(var2);
      XMLOutputStream var3 = TestUtils.createXMLOutputStream(System.out);
      var2.toXML(var3);
      var3.flush();
   }
}
