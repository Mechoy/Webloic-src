package weblogic.xml.security.signature;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import weblogic.xml.babel.stream.XMLOutputStreamBase;
import weblogic.xml.security.utils.NSOutputStream;
import weblogic.xml.security.utils.NamespaceAwareXOS;
import weblogic.xml.security.utils.XMLStreamObserver;
import weblogic.xml.stream.CharacterData;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class SignXMLOutputStream extends XMLOutputStreamBase implements DSIGConstants, NSOutputStream {
   private NSOutputStream dest;
   private boolean firstPass;
   private XMLEvent firstElement;
   private XMLEvent lastSpace;
   private List nodes;
   private XMLStreamObserver unresolvedReferences;
   private Signature pendingSignature;
   private final boolean streamSignature;

   public SignXMLOutputStream(XMLOutputStream dest) {
      this(dest, true);
   }

   public SignXMLOutputStream(XMLOutputStream dest, boolean streamSignature) {
      this.firstPass = true;
      this.nodes = new LinkedList();
      this.unresolvedReferences = new XMLStreamObserver("http://www.w3.org/2000/09/xmldsig#");
      if (!(dest instanceof NSOutputStream)) {
         this.dest = new NamespaceAwareXOS(dest);
      } else {
         this.dest = (NSOutputStream)dest;
      }

      this.addPrefix("http://www.w3.org/2000/09/xmldsig#", "dsig");
      this.streamSignature = streamSignature;
   }

   public void sign(Signature signature) throws XMLStreamException {
      this.pendingSignature = signature;
   }

   protected void addXMLEvent(XMLEvent event) throws XMLStreamException {
      if (event != null) {
         if (this.firstElement == null && event.isStartElement()) {
            this.firstElement = event;
         }

         if (this.pendingSignature != null && event.isStartElement()) {
            Iterator references = this.pendingSignature.getReferences();

            while(true) {
               while(true) {
                  Reference ref;
                  do {
                     if (!references.hasNext()) {
                        boolean needToWrite = !this.unresolvedReferences.observe(event);
                        if (needToWrite) {
                           this.nodes.add(event);
                        }

                        this.nodes.add(this.pendingSignature);
                        this.pendingSignature = null;
                        return;
                     }

                     ref = (Reference)references.next();
                  } while(!(ref instanceof InternalReference));

                  InternalReference iref = (InternalReference)ref;
                  if (iref instanceof EnvelopingReference) {
                     this.unresolvedReferences.add((XMLEvent)event, iref);
                  } else {
                     String uri = ref.getURI();
                     if (uri != null && !uri.equals("")) {
                        this.unresolvedReferences.add((String)uri, iref);
                     } else {
                        this.unresolvedReferences.add((XMLEvent)this.firstElement, iref);
                     }
                  }
               }
            }
         } else {
            if (!this.unresolvedReferences.observe(event)) {
               this.nodes.add(event);
            }

         }
      }
   }

   private int calculateIndent() {
      if (this.lastSpace == null) {
         return 0;
      } else {
         String content = ((CharacterData)this.lastSpace).getContent();
         int i = content.lastIndexOf(10);
         return i == -1 ? content.length() : content.length() - (i + 1);
      }
   }

   public Signature process() throws XMLStreamException {
      if (this.firstPass) {
         this.lastStartElement = null;
         this.firstPass = false;
      }

      if (this.pendingSignature != null) {
         if (this.streamSignature) {
            this.pendingSignature.toXML(this.dest, "http://www.w3.org/2000/09/xmldsig#", this.calculateIndent());
         }

         this.pendingSignature = null;
      }

      Iterator i = this.nodes.iterator();

      Object o;
      XMLEvent next;
      while(i.hasNext()) {
         o = i.next();
         if (!(o instanceof Signature)) {
            next = (XMLEvent)o;
            if (this.unresolvedReferences.observe(next)) {
               i.remove();
            }
         }
      }

      for(; this.nodes.size() > 0; this.dest.add(next)) {
         o = this.nodes.remove(0);
         if (o instanceof Signature) {
            this.pendingSignature = (Signature)o;
            this.pendingSignature.setIndent(this.calculateIndent());
            this.pendingSignature.setNamespaces(this.dest.getNamespaces());
            return this.pendingSignature;
         }

         next = (XMLEvent)o;
         if (next.isSpace()) {
            this.lastSpace = next;
         }
      }

      return null;
   }

   public void flush() throws XMLStreamException {
      if (this.process() != null && this.streamSignature) {
         throw new XMLStreamException("attempt to flush unsigned Signatures");
      } else {
         this.dest.flush();
      }
   }

   public void close() throws XMLStreamException {
      this.flush();
      this.dest.close();
   }

   public void close(boolean flush) throws XMLStreamException {
      this.process();
      if (flush) {
         this.flush();
      }

      this.dest.close(flush);
   }

   public void addPrefix(String namespaceURI, String prefix) {
      this.dest.addPrefix(namespaceURI, prefix);
   }

   public Map getNamespaces() {
      return this.dest.getNamespaces();
   }
}
