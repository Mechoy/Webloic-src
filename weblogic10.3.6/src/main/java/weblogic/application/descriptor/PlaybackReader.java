package weblogic.application.descriptor;

import java.io.File;
import java.io.FileInputStream;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.descriptor.DescriptorManager;
import weblogic.utils.Debug;

public class PlaybackReader implements XMLStreamReader {
   private boolean debug = Debug.getCategory("weblogic.descriptor.playback").isEnabled();
   protected ReaderEvent2 root;
   protected ReaderEvent2 currentEvent;
   protected PlaybackIterator iterator;
   protected int currentState;
   protected ReaderEventInfo.Namespaces namespaces;
   protected ReaderEventInfo.Attributes attributes;
   private String absolutePath;
   String dtdNamespaceURI = null;

   public PlaybackReader(ReaderEvent2 var1, String var2) throws XMLStreamException {
      this.root = var1;
      this.absolutePath = var2;
      this.currentEvent = var1;
      this.currentState = var1.getReaderEventInfo().getEventType();
      this.iterator = new PlaybackIterator(var1);
   }

   String getAbsolutePath() {
      return this.absolutePath;
   }

   public String getLocalName() {
      return this.currentEvent.getReaderEventInfo().getElementName();
   }

   public char[] getTextCharacters() {
      return this.currentEvent.getReaderEventInfo().getCharacters();
   }

   public int next() throws XMLStreamException {
      this.currentState = this.iterator.next();
      this.currentEvent = this.iterator.event();
      if (this.debug) {
         System.out.println("-> next: " + Utils.type2Str(this.currentState, this));
      }

      if (this.currentEvent.isDiscarded()) {
         if (this.debug) {
            System.out.println("-> skiped: " + Utils.type2Str(this.currentState, this));
         }

         return this.next();
      } else {
         return this.currentState;
      }
   }

   public int nextTag() throws XMLStreamException {
      if (this.debug) {
         System.out.println("->nextTag");
      }

      return this.next();
   }

   public String getElementText() throws XMLStreamException {
      if (this.debug) {
         System.out.println("->getElementText: " + new String(this.getTextCharacters()));
      }

      return new String(this.getTextCharacters());
   }

   public void require(int var1, String var2, String var3) throws XMLStreamException {
      if (this.debug) {
         System.out.println("->require");
      }

   }

   public boolean hasNext() throws XMLStreamException {
      if (this.debug) {
         System.out.println("->hasNext " + (this.currentState != 8));
      }

      return this.currentState != 8;
   }

   public void close() throws XMLStreamException {
   }

   ReaderEventInfo.Namespaces getNamespaces() {
      return this.namespaces == null ? this.currentEvent.getReaderEventInfo().getNamespaces() : this.namespaces;
   }

   boolean hasDTD() {
      return this.dtdNamespaceURI != null;
   }

   public void setDtdNamespaceURI(String var1) {
      this.dtdNamespaceURI = var1;
   }

   public String getDtdNamespaceURI() {
      return this.dtdNamespaceURI;
   }

   public String getNamespaceURI() {
      if (this.debug) {
         System.out.println("->getNamespaceURI: usingDTD() =" + this.hasDTD());
      }

      if (this.hasDTD() && this.currentEvent.getReaderEventInfo().getNamespaceCount() == 0) {
         return this.getDtdNamespaceURI();
      } else {
         String var1 = this.currentEvent.getReaderEventInfo().getPrefix();
         return this.getNamespaceURI(var1);
      }
   }

   public String getNamespaceURI(String var1) {
      if (this.debug) {
         System.out.println("->getNamespaceURI(" + var1 + ") " + this.getNamespaces().getNamespaceURI(var1));
      }

      String var2 = this.currentEvent.getReaderEventInfo().getNamespaces().getNamespaceURI(var1);
      if (var2 == null) {
         var2 = this.getNamespaces().getNamespaceURI(var1);
         if (var2 == null || var2.trim().length() == 0) {
            var2 = this.getNamespaces().getNamespaceURI();
         }
      }

      return var2;
   }

   public NamespaceContext getNamespaceContext() {
      if (this.debug) {
         System.out.println("->getNamespaceContext(): " + this.currentEvent.getReaderEventInfo().getNamespaceContext());
      }

      return this.currentEvent.getReaderEventInfo().getNamespaceContext();
   }

   public boolean isStartElement() {
      if (this.debug) {
         System.out.println("->isStartElement " + (this.currentState == 1));
      }

      return this.currentState == 1;
   }

   public boolean isEndElement() {
      if (this.debug) {
         System.out.println("->isEndElement: " + (this.currentState == 2));
      }

      return this.currentState == 2;
   }

   public boolean isCharacters() {
      if (this.debug) {
         System.out.println("->isCharacters");
      }

      return this.currentState == 4;
   }

   private static boolean isSpace(char var0) {
      return var0 == ' ' || var0 == '\t' || var0 == '\n' || var0 == '\r';
   }

   public boolean isWhiteSpace() {
      if (this.currentState == 4) {
         char[] var1 = (char[])this.currentEvent.getReaderEventInfo().getCharacters();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (!isSpace(var1[var2])) {
               if (this.debug) {
                  System.out.println("->isWhiteSpace: false");
               }

               return false;
            }
         }

         if (this.debug) {
            System.out.println("->isWhiteSpace: true");
         }

         return true;
      } else {
         throw new IllegalStateException("isWhiteSpace on type " + Utils.type2Str(this.currentState, this));
      }
   }

   public String getAttributeValue(String var1, String var2) {
      String var3 = this.currentEvent.getReaderEventInfo().getAttributeValue(var1, var2);
      if (this.debug) {
         System.out.println("->getAttributeValue(" + var1 + ", " + var2 + ") returns: " + var3);
      }

      return var3;
   }

   public int getAttributeCount() {
      int var1 = this.currentEvent.getReaderEventInfo().getAttributeCount();
      if (this.debug) {
         System.out.println("->getAttributeCount() returns " + var1);
      }

      return var1;
   }

   public QName getAttributeName(int var1) {
      QName var2 = this.currentEvent.getReaderEventInfo().getAttributeName(var1);
      if (this.debug) {
         System.out.println("->getAttributeName(" + var1 + ") returns: " + var2);
      }

      return var2;
   }

   public String getAttributePrefix(int var1) {
      String var2 = this.currentEvent.getReaderEventInfo().getAttributePrefix(var1);
      if (this.debug) {
         System.out.println("->getAttributePrefix(" + var1 + ") return " + var2);
      }

      return var2;
   }

   public String getAttributeNamespace(int var1) {
      String var2 = this.currentEvent.getReaderEventInfo().getAttributeNamespace(var1);
      if (this.debug) {
         System.out.println("->getAttributeNamespace(" + var1 + ") returns " + var2);
      }

      return var2;
   }

   public String getAttributeLocalName(int var1) {
      String var2 = this.currentEvent.getReaderEventInfo().getAttributeLocalName(var1);
      if (this.debug) {
         System.out.println("->getAttributeLocalName(" + var1 + ") returns " + var2);
      }

      return var2;
   }

   public String getAttributeType(int var1) {
      if (this.debug) {
         System.out.println("->getAttributeType returns CDATA");
      }

      return "CDATA";
   }

   public String getAttributeValue(int var1) {
      String var2 = this.currentEvent.getReaderEventInfo().getAttributeValue(var1);
      if (this.debug) {
         System.out.println("->getAttributeValue(" + var1 + ") returns: " + var2);
      }

      return var2;
   }

   public boolean isAttributeSpecified(int var1) {
      boolean var2 = this.currentEvent.getReaderEventInfo().isAttributeSpecified(var1);
      if (this.debug) {
         System.out.println("->isAttributeSpecified(" + var1 + ") returns " + var2);
      }

      return var2;
   }

   public int getNamespaceCount() {
      int var1 = this.currentEvent.getReaderEventInfo().getNamespaceCount();
      if (this.debug) {
         System.out.println("->getNamespaceCount return " + var1);
      }

      return var1;
   }

   public String getNamespacePrefix(int var1) {
      String var2 = this.currentEvent.getReaderEventInfo().getNamespacePrefix(var1);
      if (this.debug) {
         System.out.println("->getNamespacePrefix(" + var1 + ") return " + var2);
      }

      return var2;
   }

   public String getNamespaceURI(int var1) {
      if (this.debug) {
         System.out.println("->getNamespaceURI(int) " + var1);
      }

      return this.currentEvent.getReaderEventInfo().getNamespaceURI(var1);
   }

   public int getEventType() {
      int var1 = this.currentState;
      if (this.debug) {
         System.out.println("->getEventType: " + Utils.type2Str(var1, this));
      }

      return var1;
   }

   public String getText() {
      if (this.debug) {
         System.out.println("->getText");
      }

      return new String(this.getTextCharacters());
   }

   public int getTextCharacters(int var1, char[] var2, int var3, int var4) throws XMLStreamException {
      throw new UnsupportedOperationException();
   }

   public int getTextStart() {
      if (this.debug) {
         System.out.println("->getTextStart");
      }

      return 0;
   }

   public int getTextLength() {
      return this.getTextCharacters().length;
   }

   public String getEncoding() {
      if (this.debug) {
         System.out.println("->getEncoding");
      }

      return this.currentEvent.getReaderEventInfo().getEncoding();
   }

   public boolean hasText() {
      if (this.debug) {
         System.out.println("->hasText");
      }

      switch (this.currentState) {
         case 4:
         case 5:
         case 6:
         case 9:
         case 11:
            return true;
         case 7:
         case 8:
         case 10:
         default:
            return false;
      }
   }

   public Location getLocation() {
      Location var1 = this.currentEvent.getReaderEventInfo().getLocation();
      if (this.debug) {
         System.out.println("->getLocation: " + var1);
      }

      return var1;
   }

   public QName getName() {
      if (this.debug) {
         System.out.println("->getName");
      }

      ReaderEventInfo var1 = this.currentEvent.getReaderEventInfo();
      return var1.getNamespacePrefix(0) == null ? new QName(var1.getNamespaceURI(0), var1.getElementName()) : new QName(var1.getNamespaceURI(0), var1.getElementName(), var1.getNamespacePrefix(0));
   }

   public boolean hasName() {
      if (this.debug) {
         System.out.println("->hasName");
      }

      return this.currentState == 1 || this.currentState == 2;
   }

   public String getPrefix() {
      if (this.debug) {
         System.out.println("->getPrefix " + this.currentEvent.getReaderEventInfo().getNamespacePrefix(0));
      }

      return this.currentEvent.getReaderEventInfo().getPrefix();
   }

   public String getVersion() {
      if (this.debug) {
         System.out.println("->getVersion");
      }

      return null;
   }

   public boolean isStandalone() {
      if (this.debug) {
         System.out.println("->isStandalone");
      }

      return false;
   }

   public boolean standaloneSet() {
      if (this.debug) {
         System.out.println("->standaloneSet");
      }

      return false;
   }

   public String getCharacterEncodingScheme() {
      if (this.debug) {
         System.out.println("->getCharacterEncodingScheme");
      }

      return this.currentEvent.getReaderEventInfo().getCharacterEncodingScheme();
   }

   public String getPITarget() {
      if (this.debug) {
         System.out.println("->getPITarget");
      }

      throw new IllegalStateException();
   }

   public String getPIData() {
      if (this.debug) {
         System.out.println("->getPIData");
      }

      throw new IllegalStateException();
   }

   public Object getProperty(String var1) {
      if (this.debug) {
         System.out.println("->getProperty");
      }

      throw new UnsupportedOperationException("UNIMPLEMENTED");
   }

   public static void main(String[] var0) throws Exception {
      FileInputStream var1 = null;
      String var2 = null;
      if (var0.length == 0) {
         usage();
      } else {
         File var3 = new File(var0[0]);
         var1 = new FileInputStream(var3);
         var2 = var0[0];
      }

      System.out.println("stamp out munger...");
      System.out.flush();
      BasicMunger2 var4 = new BasicMunger2(var1, var2);
      System.out.println("hand munger to descriptor manger...");
      System.out.flush();
      (new DescriptorManager()).createDescriptor(var4).toXML(System.out);
      System.out.print("\n dump the bean from the playback reader:\n");
      System.out.flush();
      (new DescriptorManager()).createDescriptor(new PlaybackReader(var4.root, var4.getAbsolutePath())).toXML(System.out);
   }

   private static void usage() {
      System.err.println("usage: java weblogic.application.descriptor.PlaybackReader <descriptor file name>");
      System.exit(0);
   }

   private class MyLocation implements Location {
      Location l;

      MyLocation(Location var2) {
         this.l = var2;
      }

      public int getLineNumber() {
         return this.l.getLineNumber();
      }

      public int getColumnNumber() {
         return this.l.getColumnNumber();
      }

      public int getCharacterOffset() {
         return this.l.getCharacterOffset();
      }

      public String getPublicId() {
         return PlaybackReader.this.getAbsolutePath() + ":" + this.l.getLineNumber() + ":" + this.l.getColumnNumber();
      }

      public String getSystemId() {
         return this.l.getSystemId();
      }
   }

   class PlaybackIterator {
      int index = 0;
      int state = -1;
      ReaderEvent2 event;
      PlaybackIterator parent;

      PlaybackIterator(ReaderEvent2 var2) {
         this.event = var2;
      }

      PlaybackIterator(ReaderEvent2 var2, PlaybackIterator var3) {
         this.event = var2;
         this.parent = var3;
      }

      int next() throws XMLStreamException {
         int var1 = this.state();
         if (var1 == -2 || var1 == 7) {
            if (this.index < this.event.getChildren().size()) {
               PlaybackReader.this.iterator = PlaybackReader.this.new PlaybackIterator((ReaderEvent2)this.event.getChildren().elementAt(this.index), this);
               ++this.index;
               return PlaybackReader.this.iterator.next();
            }

            if (this.index == this.event.getChildren().size()) {
               if (this.parent == null) {
                  throw new XMLStreamException("unexpected end of xml stream");
               }

               PlaybackReader.this.iterator = this.parent;
               return PlaybackReader.this.iterator.next();
            }
         }

         return var1;
      }

      ReaderEvent2 event() {
         return this.event;
      }

      int state() throws XMLStreamException {
         switch (this.state) {
            case -1:
               this.state = this.event.getReaderEventInfo().getEventType();
               if (this.state == 1) {
                  if (this.event.getReaderEventInfo().getNamespaceCount() > 0) {
                     if (PlaybackReader.this.namespaces == null) {
                        PlaybackReader.this.namespaces = this.event.getReaderEventInfo().getNamespaces();
                     } else {
                        ReaderEventInfo.Namespaces var1 = this.event.getReaderEventInfo().getNamespaces();

                        for(int var2 = 0; var2 < var1.getNamespaceCount(); ++var2) {
                           PlaybackReader.this.namespaces.setNamespaceURI(var1.getNamespacePrefix(var2), var1.getNamespaceURI(var2));
                        }
                     }

                     if (PlaybackReader.this.debug) {
                        for(int var3 = 0; var3 < PlaybackReader.this.namespaces.getNamespaceCount(); ++var3) {
                           System.out.println("===== namespace prefix = " + PlaybackReader.this.namespaces.getNamespacePrefix(var3));
                           System.out.println("===== namespaceURI = " + PlaybackReader.this.namespaces.getNamespaceURI(var3));
                        }
                     }
                  }

                  if (this.event.getReaderEventInfo().getAttributeCount() > 0) {
                     PlaybackReader.this.attributes = this.event.getReaderEventInfo().getAttributes();
                  }
               }
            case 0:
            case 3:
            default:
               break;
            case 1:
               if (this.event.getReaderEventInfo().getCharacters() != null) {
                  this.state = 4;
               } else {
                  if (this.index < this.event.getChildren().size()) {
                     PlaybackReader.this.iterator = PlaybackReader.this.new PlaybackIterator((ReaderEvent2)this.event.getChildren().elementAt(this.index), this);
                     ++this.index;
                     return PlaybackReader.this.iterator.next();
                  }

                  this.state = 2;
               }
               break;
            case 2:
               this.state = -2;
               break;
            case 4:
               if (this.index < this.event.getChildren().size()) {
                  PlaybackReader.this.iterator = PlaybackReader.this.new PlaybackIterator((ReaderEvent2)this.event.getChildren().elementAt(this.index), this);
                  ++this.index;
                  return PlaybackReader.this.iterator.next();
               }

               this.state = 2;
         }

         return this.state;
      }
   }
}
