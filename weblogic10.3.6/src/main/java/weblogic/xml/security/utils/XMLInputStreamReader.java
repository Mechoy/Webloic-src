package weblogic.xml.security.utils;

import java.util.HashMap;
import java.util.Map;
import weblogic.utils.collections.Stack;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.CharacterData;
import weblogic.xml.stream.EndElement;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLInputStreamFactory;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLStreamException;
import weblogic.xml.stream.events.Name;
import weblogic.xml.stream.util.TypeFilter;

public class XMLInputStreamReader extends AbstractXMLReader {
   private static TypeFilter filter = new TypeFilter(22);
   private static XMLInputStreamFactory streamFactory = XMLInputStreamFactory.newInstance();
   private final Stack startStack = new Stack();
   private final XMLInputStream source;
   private XMLEvent current;
   private StartElement currentStart = null;
   private boolean endReached;
   private int currentType = 0;

   public XMLInputStreamReader(XMLInputStream var1) throws XMLStreamException {
      this.source = streamFactory.newInputStream(var1, filter);
      this.endReached = false;
      this.next();
   }

   public int next() {
      try {
         if (!this.endReached && this.source.hasNext()) {
            XMLEvent var1 = this.source.next();
            switch (var1.getType()) {
               case 2:
                  return this.process((StartElement)var1);
               case 4:
                  return this.process((EndElement)var1);
               case 16:
                  return this.process((CharacterData)var1);
               default:
                  throw new AssertionError("received " + var1 + "; should have been filtered");
            }
         } else {
            this.endReached = true;
            return 0;
         }
      } catch (XMLStreamException var2) {
         throw new RuntimeException("problem parsing document", var2);
      }
   }

   private int process(StartElement var1) {
      this.startStack.push(this.currentStart);
      this.current = this.currentStart = var1;
      return this.currentType = 2;
   }

   private int process(EndElement var1) throws XMLStreamException {
      if (this.startStack.isEmpty()) {
         throw new XMLStreamException("End tag without corresponding start");
      } else {
         this.current = var1;
         this.currentStart = (StartElement)this.startStack.pop();
         if (this.startStack.isEmpty()) {
            this.endReached = true;
         }

         return this.currentType = 4;
      }
   }

   private int process(CharacterData var1) {
      this.current = var1;
      return this.currentType = 16;
   }

   public boolean hasName() {
      return this.currentType == 2 || this.currentType == 4;
   }

   public String getNamespaceURI() throws IllegalStateException {
      if (!this.hasName()) {
         throw new IllegalStateException("Current node has no name");
      } else {
         return this.current.getName().getNamespaceUri();
      }
   }

   public String getLocalName() throws IllegalStateException {
      if (!this.hasName()) {
         throw new IllegalStateException("Current node has no name");
      } else {
         return this.current.getName().getLocalName();
      }
   }

   public void require(int var1, String var2, String var3) throws ValidationException {
      XMLName var4;
      switch (var1) {
         case 2:
            if (this.currentType != 2) {
               throw new ValidationException("expected start element " + var2 + ":" + var3 + ", received " + this.current);
            } else {
               var4 = this.current.getName();
               if (var4.getLocalName().equals(var3) && (var2 == null || var2.equals(var4.getNamespaceUri()))) {
                  return;
               }

               throw new ValidationException("exptected <" + var2 + ":" + var3 + ">, got " + this.current);
            }
         case 4:
            if (this.currentType != 4) {
               throw new ValidationException("expected start element " + var2 + ":" + var3 + ", received " + this.current);
            } else {
               var4 = this.current.getName();
               if (var4.getLocalName().equals(var3) && (var2 == null || var2.equals(var4.getNamespaceUri()))) {
                  return;
               }

               throw new ValidationException("exptected </" + var2 + ":" + var3 + ">, got " + this.current);
            }
         case 16:
            if (this.currentType == 16) {
               return;
            }

            throw new ValidationException("received " + this.current + " instead of expected " + "character data");
         default:
            throw new IllegalArgumentException("unknown type");
      }
   }

   public void close() {
      this.startStack.clear();
      this.current = null;
      this.currentStart = null;
      this.endReached = true;
   }

   public int getEventType() {
      return this.currentType;
   }

   public boolean isStartElement() {
      return this.currentType == 2;
   }

   public boolean isEndElement() {
      return this.currentType == 4;
   }

   public boolean isCharacters() {
      return this.currentType == 16;
   }

   public String getAttribute(String var1, String var2) throws IllegalStateException {
      if (this.currentType != 2) {
         throw new IllegalStateException("current event is not a start event");
      } else {
         Attribute var3 = this.currentStart.getAttributeByName(new Name(var1, var2, (String)null));
         return var3 != null ? var3.getValue() : null;
      }
   }

   public String getNamespaceURI(String var1) {
      return null;
   }

   public Map getNamespaceMap() {
      HashMap var1 = new HashMap();
      var1.putAll(this.currentStart.getNamespaceMap());
      return var1;
   }

   public String getText() throws IllegalStateException {
      if (this.currentType != 16) {
         throw new IllegalStateException("expected CDATA, got " + this.current);
      } else {
         return ((CharacterData)this.current).getContent();
      }
   }
}
