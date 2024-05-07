package weblogic.xml.security.signature;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import weblogic.xml.babel.stream.XMLInputStreamBase;
import weblogic.xml.security.utils.XMLStreamObserver;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

public class VerifyXMLInputStream extends XMLInputStreamBase implements XMLInputStream, DSIGConstants {
   private final XMLInputStream source;
   private boolean firstPass = true;
   private XMLEvent firstElement;
   private Stack nesting = new Stack();
   private List nodes = new LinkedList();
   private XMLStreamObserver unresolvedReferences = new XMLStreamObserver("http://www.w3.org/2000/09/xmldsig#");
   private List signatures = new LinkedList();

   public VerifyXMLInputStream(XMLInputStream source) throws XMLStreamException {
      this.source = source;
   }

   public Iterator getSignatures() throws XMLStreamException {
      if (this.firstPass) {
         this.firstPass();
      }

      return this.signatures.iterator();
   }

   public XMLEvent next() throws XMLStreamException {
      if (this.firstPass) {
         this.firstPass();
      }

      XMLEvent ret = (XMLEvent)this.nodes.remove(0);
      this.unresolvedReferences.observe(ret);
      return ret;
   }

   private void firstPass() throws XMLStreamException {
      label61:
      while(this.source.hasNext()) {
         XMLEvent next = this.source.peek();
         switch (next.getType()) {
            case 2:
               if (this.firstElement == null) {
                  this.firstElement = next;
               }

               if (next.getName().getLocalName().equals("Signature")) {
                  Signature signature = (Signature)DSIGReader.read(this.source, 8);
                  if (signature == null) {
                     break;
                  }

                  this.signatures.add(signature);
                  XMLInputStream obj = signature.getEmbeddedObject();
                  if (obj != null) {
                     while(obj.hasNext()) {
                        XMLEvent o = obj.next();
                        this.nodes.add(o);
                     }
                  }

                  Iterator references = signature.getReferences();

                  while(true) {
                     while(true) {
                        Reference ref;
                        do {
                           if (!references.hasNext()) {
                              continue label61;
                           }

                           ref = (Reference)references.next();
                        } while(!(ref instanceof InternalReference));

                        InternalReference iref = (InternalReference)ref;
                        if (iref instanceof EnvelopingReference) {
                           this.unresolvedReferences.add((XMLEvent)((XMLEvent)this.nesting.peek()), iref);
                        } else {
                           String uri = iref.getURI();
                           if (uri != null && !uri.equals("")) {
                              this.unresolvedReferences.add((String)iref.getURI(), iref);
                           } else {
                              this.unresolvedReferences.add((XMLEvent)this.firstElement, iref);
                           }
                        }
                     }
                  }
               }

               next = this.source.next();
               this.nesting.push(next);
               this.nodes.add(next);
               break;
            case 4:
               this.nesting.pop();
            default:
               next = this.source.next();
               this.nodes.add(next);
         }
      }

      this.firstPass = false;
   }

   public boolean hasNext() throws XMLStreamException {
      if (this.firstPass) {
         this.firstPass();
      }

      return !this.nodes.isEmpty();
   }

   public void skip() throws XMLStreamException {
      this.next();
   }

   public XMLEvent peek() throws XMLStreamException {
      if (this.firstPass) {
         this.firstPass();
      }

      return this.nodes.isEmpty() ? null : (XMLEvent)this.nodes.get(0);
   }

   public void close() throws XMLStreamException {
      this.source.close();
   }
}
