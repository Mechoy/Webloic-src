package weblogic.xml.security.utils;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.Stack;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public final class DebugOutputStream implements NSOutputStream {
   private NSOutputStream delegate;
   private final String label;
   private final boolean verbose;
   private final Stack names;
   private int callCount;
   private static final DecimalFormat f = new DecimalFormat("0000");

   public DebugOutputStream(XMLOutputStream delegate) {
      this((String)null, delegate);
   }

   public DebugOutputStream(String label, XMLOutputStream delegate) {
      this.names = new Stack();
      this.callCount = 0;
      if (delegate instanceof NSOutputStream) {
         this.delegate = (NSOutputStream)delegate;
      } else {
         this.delegate = new NamespaceAwareXOS(delegate);
      }

      this.label = label;
      this.verbose = label != null;
   }

   private void debugOut(String call, Object param) {
      System.out.println("[" + this.label + ":" + f.format((long)this.callCount) + "/" + this.names.size() + "] " + call + "(" + param + ")");
      ++this.callCount;
   }

   public void add(XMLEvent event) throws XMLStreamException {
      if (this.verbose) {
         this.debugOut("add", event);
      }

      switch (event.getType()) {
         case 2:
            this.names.push(event.getName());
            break;
         case 4:
            if (this.names.isEmpty()) {
               throw new AssertionError("got end without start: " + this.names);
            }

            XMLName match = (XMLName)this.names.pop();
            if (!matches(match, event.getName())) {
               throw new AssertionError("Got " + event + ", expected </" + match + ">");
            }
      }

      this.delegate.add(event);
   }

   private static boolean matches(XMLName expected, XMLName got) {
      String expectedNS = expected.getNamespaceUri();
      boolean ns = expectedNS == null ? got.getNamespaceUri() == null : expectedNS != null && expectedNS.equals(got.getNamespaceUri());
      String expectedLN = expected.getLocalName();
      boolean ln = expectedLN == null ? got.getLocalName() == null : expectedLN.equals(got.getLocalName());
      return ns && ln;
   }

   public void add(XMLInputStream inputStream) throws XMLStreamException {
      if (this.verbose) {
         this.debugOut("add", inputStream);
      }

      this.delegate.add(inputStream);
   }

   public void add(String markup) throws XMLStreamException {
      if (this.verbose) {
         this.debugOut("add", markup);
      }

      this.delegate.add(markup);
   }

   public void add(Attribute attribute) throws XMLStreamException {
      if (this.verbose) {
         this.debugOut("add", attribute);
      }

      this.delegate.add(attribute);
   }

   public void close() throws XMLStreamException {
      if (this.verbose) {
         this.debugOut("close", "");
      }

      this.delegate.close();
   }

   public void close(boolean flush) throws XMLStreamException {
      if (this.verbose) {
         this.debugOut("close", flush ? "true" : "false");
      }

      this.delegate.close(flush);
   }

   public void flush() throws XMLStreamException {
      if (this.verbose) {
         this.debugOut("flush", "");
      }

      this.delegate.flush();
   }

   public void addPrefix(String namespaceURI, String prefix) {
      if (this.verbose) {
         this.debugOut("addPrefix", namespaceURI + ", " + prefix);
      }

      if (this.delegate instanceof NSOutputStream) {
         this.delegate.addPrefix(namespaceURI, prefix);
      }

   }

   public Map getNamespaces() {
      if (this.verbose) {
         this.debugOut("getNamespaces", "");
      }

      return this.delegate instanceof NSOutputStream ? this.delegate.getNamespaces() : null;
   }
}
