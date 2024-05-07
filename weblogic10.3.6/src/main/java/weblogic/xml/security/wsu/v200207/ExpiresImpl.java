package weblogic.xml.security.wsu.v200207;

import java.util.Calendar;
import weblogic.xml.security.utils.ValidationException;
import weblogic.xml.security.utils.XMLReader;
import weblogic.xml.security.wsu.Expires;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

public class ExpiresImpl extends AttributedDateTimeBase implements Expires {
   protected ExpiresImpl() {
   }

   public ExpiresImpl(Calendar var1) {
      super(var1);
   }

   public ExpiresImpl(String var1) {
      super(var1);
   }

   public ExpiresImpl(XMLInputStream var1) throws XMLStreamException {
      this.fromXMLInternal(var1);
   }

   public ExpiresImpl(XMLReader var1) throws ValidationException {
      this.fromXMLInternal(var1);
   }

   protected final String getLocalName() {
      return "Expires";
   }
}
