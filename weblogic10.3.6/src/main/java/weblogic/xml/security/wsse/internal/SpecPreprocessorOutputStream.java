package weblogic.xml.security.wsse.internal;

import java.util.HashSet;
import java.util.Set;
import weblogic.xml.security.specs.OperationSpec;
import weblogic.xml.security.utils.Preprocessor;
import weblogic.xml.security.utils.PreprocessorOutputStream;
import weblogic.xml.stream.EndElement;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class SpecPreprocessorOutputStream extends PreprocessorOutputStream {
   private final Set headerTypes = new HashSet();
   private final Set bodyTypes = new HashSet();
   private final Set unrestrictedTypes = new HashSet();
   private Set typeRegistrations;
   private final boolean entireBody;
   private final SoapStreamState state = new SoapStreamState();

   public SpecPreprocessorOutputStream(XMLOutputStream var1, OperationSpec var2, Preprocessor var3) {
      super(var1, var3);
      XMLName[] var4 = var2.getHeaderElementNames();

      int var5;
      XMLName var6;
      for(var5 = 0; var5 < var4.length; ++var5) {
         var6 = var4[var5];
         this.headerTypes.add(var6);
      }

      var4 = var2.getUnrestrictedElementNames();

      for(var5 = 0; var5 < var4.length; ++var5) {
         var6 = var4[var5];
         this.unrestrictedTypes.add(var6);
      }

      this.entireBody = var2.entireBody();
      if (!this.entireBody) {
         var4 = var2.getBodyElementNames();

         for(var5 = 0; var5 < var4.length; ++var5) {
            var6 = var4[var5];
            this.bodyTypes.add(var6);
         }
      }

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
      if (this.state.update(var1)) {
         if (this.state.inHeader()) {
            this.typeRegistrations = this.headerTypes;
         } else if (this.state.inBody()) {
            this.typeRegistrations = this.bodyTypes;
         }
      }

      if (this.entireBody && this.state.inBody()) {
         if (this.state.atPartLevel()) {
            this.preprocessor.begin(var1, this.dest);
            return;
         }
      } else if (this.state.atTypeLevel() && this.typeRegistrations != null && !this.typeRegistrations.isEmpty() && this.typeRegistrations.contains(var1.getName())) {
         this.preprocessor.begin(var1, this.dest);
         return;
      }

      if (this.unrestrictedTypes.contains(var1.getName())) {
         this.preprocessor.begin(var1, this.dest);
      } else {
         this.dest.add(var1);
      }

   }

   protected void end(EndElement var1) throws XMLStreamException {
      if (this.unrestrictedTypes.contains(var1.getName())) {
         this.preprocessor.end(var1, this.dest);
         this.state.update(var1);
      } else {
         if (this.entireBody && this.state.inBody()) {
            if (this.state.atPartLevel()) {
               this.preprocessor.end(var1, this.dest);
               this.state.update(var1);
               return;
            }
         } else if (this.state.atTypeLevel() && this.typeRegistrations != null && !this.typeRegistrations.isEmpty() && this.typeRegistrations.contains(var1.getName())) {
            this.preprocessor.end(var1, this.dest);
            this.state.update(var1);
            return;
         }

         this.dest.add(var1);
         this.state.update(var1);
      }
   }
}
