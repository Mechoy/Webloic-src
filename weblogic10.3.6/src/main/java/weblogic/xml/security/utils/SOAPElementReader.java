package weblogic.xml.security.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import weblogic.utils.collections.Stack;
import weblogic.webservice.core.soap.NameImpl;
import weblogic.webservice.core.soap.SOAPTextElement;

public class SOAPElementReader extends AbstractXMLReader {
   private final Stack stateStack = new Stack();
   private StateFrame currentFrame;
   private SOAPTextElement textElement;
   private int state;

   public SOAPElementReader(SOAPElement var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("element cannot be null");
      } else {
         this.currentFrame = new StateFrame(var1);
         this.state = 2;
         this.textElement = null;
      }
   }

   public int next() {
      switch (this.state) {
         case 0:
            return 0;
         case 4:
            if (this.stateStack.isEmpty()) {
               this.close();
               return this.state;
            } else {
               this.currentFrame = (StateFrame)this.stateStack.pop();
            }
         case 16:
            this.textElement = null;
         case 2:
            byte var1;
            if (!this.currentFrame.children.hasNext()) {
               var1 = 4;
            } else {
               Object var2 = this.currentFrame.children.next();
               if (var2 instanceof SOAPTextElement) {
                  this.textElement = (SOAPTextElement)var2;
                  var1 = 16;
               } else {
                  if (!(var2 instanceof SOAPElement)) {
                     throw new AssertionError("Unknown child type: " + var2);
                  }

                  this.stateStack.push(this.currentFrame);
                  this.currentFrame = new StateFrame((SOAPElement)var2);
                  var1 = 2;
               }
            }

            return this.state = var1;
         default:
            throw new IllegalStateException("Reader in illegal state");
      }
   }

   public boolean hasName() {
      return this.state == 2 || this.state == 4;
   }

   public QName getName() throws IllegalStateException {
      if (!this.hasName()) {
         throw new IllegalStateException("Current node has no name");
      } else {
         return qnameFactory.getQName(this.getNamespaceURI(), this.getLocalName());
      }
   }

   public String getLocalName() {
      if (!this.hasName()) {
         throw new IllegalStateException("Current node has no name");
      } else {
         return this.currentFrame.element.getElementName().getLocalName();
      }
   }

   public String getNamespaceURI() {
      if (!this.hasName()) {
         throw new IllegalStateException("Current node has no name");
      } else {
         return this.currentFrame.element.getElementName().getURI();
      }
   }

   public void require(int var1, String var2, String var3) throws ValidationException {
      String var4;
      String var5;
      switch (var1) {
         case 0:
            var4 = "end of document";
            switch (this.state) {
               case 0:
                  return;
               case 1:
               case 3:
               default:
                  var5 = "CDATA";
                  throw new ValidationException("expected " + var4 + ", got " + var5);
               case 2:
                  var5 = "<" + this.getName() + ">";
                  throw new ValidationException("expected " + var4 + ", got " + var5);
               case 4:
                  var5 = "</" + this.getName() + ">";
                  throw new ValidationException("expected " + var4 + ", got " + var5);
            }
         case 2:
            var4 = "<" + var2 + ":" + var3 + ">";
            switch (this.state) {
               case 2:
                  if (var3.equals(this.getLocalName()) && (var2 == null || var2.equals(this.getNamespaceURI()))) {
                     return;
                  }

                  var5 = "<" + this.getName() + ">";
                  throw new ValidationException("expected " + var4 + ", got " + var5);
               case 4:
                  var5 = "</" + this.getName() + ">";
                  throw new ValidationException("expected " + var4 + ", got " + var5);
               case 16:
                  var5 = "CDATA (" + this.textElement + ")";
                  throw new ValidationException("expected " + var4 + ", got " + var5);
               default:
                  var5 = "end of document";
                  throw new ValidationException("expected " + var4 + ", got " + var5);
            }
         case 4:
            var4 = "</" + var2 + ":" + var3 + ">";
            switch (this.state) {
               case 2:
                  var5 = "<" + this.getName() + ">";
                  throw new ValidationException("expected " + var4 + ", got " + var5);
               case 4:
                  if (var3.equals(this.getLocalName()) && (var2 == null || var2.equals(this.getNamespaceURI()))) {
                     return;
                  }

                  var5 = "</" + this.getName() + ">";
                  throw new ValidationException("expected " + var4 + ", got " + var5);
               case 16:
                  var5 = "CDATA (" + this.textElement + ")";
                  throw new ValidationException("expected " + var4 + ", got " + var5);
               default:
                  var5 = "end of document";
                  throw new ValidationException("expected " + var4 + ", got " + var5);
            }
         case 16:
            var4 = "CDATA";
            switch (this.state) {
               case 2:
                  var5 = "<" + this.getName() + ">";
                  throw new ValidationException("expected " + var4 + ", got " + var5);
               case 4:
                  var5 = "</" + this.getName() + ">";
                  throw new ValidationException("expected " + var4 + ", got " + var5);
               case 16:
                  return;
               default:
                  var5 = "end of document";
                  throw new ValidationException("expected " + var4 + ", got " + var5);
            }
         default:
            throw new IllegalArgumentException("illegal type provided - " + var1);
      }
   }

   public void close() {
      this.state = 0;
      this.currentFrame = null;
      this.stateStack.clear();
   }

   public int getEventType() {
      return this.state;
   }

   public boolean isStartElement() {
      return this.state == 2;
   }

   public boolean isEndElement() {
      return this.state == 4;
   }

   public boolean isCharacters() {
      return this.state == 16;
   }

   public String getAttribute(QName var1) {
      return this.getAttribute(var1.getNamespaceURI(), var1.getLocalPart());
   }

   public String getAttribute(String var1, String var2) throws IllegalStateException {
      if (this.state != 2 && this.state != 4) {
         throw new IllegalStateException("No attributes available for this type");
      } else {
         NameImpl var3 = new NameImpl(var2, (String)null, var1);
         return this.currentFrame.element.getAttributeValue(var3);
      }
   }

   public QName getQNameAttribute(QName var1) throws IllegalStateException {
      String var2 = this.getAttribute(var1);
      return this.getQName(var2);
   }

   public String getNamespaceURI(String var1) {
      return this.currentFrame.element.getNamespaceURI(var1);
   }

   public Map getNamespaceMap() {
      Iterator var1 = this.currentFrame.element.getNamespacePrefixes();
      HashMap var2 = new HashMap(5);

      while(var1.hasNext()) {
         String var3 = (String)var1.next();
         String var4 = this.currentFrame.element.getNamespaceURI(var3);
         var2.put(var3, var4);
      }

      return var2;
   }

   public String getText() throws IllegalStateException {
      if (this.state != 16) {
         throw new IllegalStateException();
      } else {
         String var1 = this.textElement.getValue();
         return var1;
      }
   }

   private static final class StateFrame {
      private final SOAPElement element;
      private final Iterator children;

      private StateFrame(SOAPElement var1) {
         this.element = var1;
         this.children = var1.getChildElements();
      }

      // $FF: synthetic method
      StateFrame(SOAPElement var1, Object var2) {
         this(var1);
      }
   }
}
