package weblogic.xml.security.wsu.v200207;

import java.util.Calendar;
import weblogic.xml.security.utils.ValidationException;
import weblogic.xml.security.utils.XMLReader;
import weblogic.xml.security.wsu.Created;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

public class CreatedImpl extends AttributedDateTimeBase implements Created {
   public CreatedImpl() {
   }

   public CreatedImpl(Calendar var1) {
      super(var1);
   }

   public CreatedImpl(String var1) {
      super(var1);
   }

   public CreatedImpl(XMLInputStream var1) throws XMLStreamException {
      this.fromXMLInternal(var1);
   }

   public CreatedImpl(XMLReader var1) throws ValidationException {
      this.fromXMLInternal(var1);
   }

   protected final String getLocalName() {
      return "Created";
   }
}
