package weblogic.xml.security.utils;

import weblogic.xml.stream.EndElement;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public abstract class PreprocessorOutputStream extends XMLOutputStreamBase {
   protected final Preprocessor preprocessor;

   public PreprocessorOutputStream(XMLOutputStream var1, Preprocessor var2) {
      super(var1);
      this.preprocessor = var2;
   }

   protected void addXMLEvent(XMLEvent var1) throws XMLStreamException {
      if (var1.isStartElement()) {
         this.begin((StartElement)var1);
      } else if (var1.isEndElement()) {
         this.end((EndElement)var1);
      } else {
         this.dest.add(var1);
      }

   }

   protected void begin(StartElement var1) throws XMLStreamException {
      this.preprocessor.begin(var1, this.dest);
   }

   protected void end(EndElement var1) throws XMLStreamException {
      this.preprocessor.end(var1, this.dest);
   }
}
