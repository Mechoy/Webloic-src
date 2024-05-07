package weblogic.xml.crypto.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import weblogic.xml.babel.baseparser.PrefixMapping;
import weblogic.xml.babel.baseparser.SymbolTable;
import weblogic.xml.babel.stream.XMLWriter;
import weblogic.xml.crypto.dsig.DsigConstants;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.ElementFactory;
import weblogic.xml.stream.XMLStreamException;
import weblogic.xml.stream.events.AttributeImpl;
import weblogic.xml.stream.events.ChangePrefixMappingEvent;
import weblogic.xml.stream.events.CharacterDataEvent;
import weblogic.xml.stream.events.CommentEvent;
import weblogic.xml.stream.events.EndDocumentEvent;
import weblogic.xml.stream.events.EndElementEvent;
import weblogic.xml.stream.events.EndPrefixMappingEvent;
import weblogic.xml.stream.events.Name;
import weblogic.xml.stream.events.ProcessingInstructionEvent;
import weblogic.xml.stream.events.SpaceEvent;
import weblogic.xml.stream.events.StartDocumentEvent;
import weblogic.xml.stream.events.StartElementEvent;
import weblogic.xml.stream.events.StartPrefixMappingEvent;

public class C14NDOMAdapter {
   private static final String PREFIX_SEP = ":";
   private static final String EMPTY_STRING = "";
   private static final String NEW_LINE = "\n";
   private XMLWriter writer;
   private SymbolTable nameSpaceTable = new SymbolTable();
   private StartElementEvent currentStartElement;
   private List nonVisiblyUsed = new ArrayList();
   private Map externalNamespaces;

   public C14NDOMAdapter(XMLWriter var1, Map var2) {
      this.writer = var1;
      this.externalNamespaces = var2;
   }

   public String[] getNonVisiblyUsed() {
      String[] var1 = new String[this.nonVisiblyUsed.size()];
      this.nonVisiblyUsed.toArray(var1);
      return var1;
   }

   public void adaptNodeStart(Node var1) throws XMLStreamException {
      this.adapt(var1, true);
   }

   public void adaptNodeEnd(Node var1) throws XMLStreamException {
      this.adapt(var1, false);
   }

   private void writeCurrentStartElement() throws XMLStreamException {
      if (this.currentStartElement != null) {
         this.writer.write(this.currentStartElement);
         this.currentStartElement = null;
      }

   }

   private void setCurrentStartElement(StartElementEvent var1) {
      this.currentStartElement = var1;
   }

   public void adapt(Node var1, boolean var2) throws XMLStreamException {
      String var3 = var1.getLocalName();
      if (var3 == null) {
         var3 = var1.getNodeName();
      }

      switch (var1.getNodeType()) {
         case 1:
            this.adaptElement(var1, var2);
            break;
         case 2:
            if (var2) {
               this.adaptAttribute(var1);
            }
            break;
         case 3:
            if (var2) {
               this.adaptText(var1);
            }
            break;
         case 4:
            if (var2) {
               this.adaptCDATA(var1);
            }
            break;
         case 5:
         case 6:
         default:
            throw new XMLStreamException("Unknown DOM Node Type");
         case 7:
            if (var2) {
               this.adaptProcessingInstruction((ProcessingInstruction)var1);
            }
            break;
         case 8:
            if (var2) {
               this.adaptComment((Comment)var1);
            }
            break;
         case 9:
            Document var4 = (Document)var1;
            this.adaptDocument(var4, var2);
         case 10:
      }

   }

   public void adaptDocument(Document var1, boolean var2) throws XMLStreamException {
      if (var2) {
         this.writer.write(new StartDocumentEvent());
      } else {
         this.writer.write(new EndDocumentEvent());
      }

   }

   private void adaptNamespace(StartElementEvent var1, String var2, String var3) throws XMLStreamException {
      Attribute var4 = ElementFactory.createNamespaceAttribute(var2, var3);
      var1.addNamespace(var4);
      if (var2 == null) {
         var2 = "";
      }

      this.nameSpaceTable.put(var2, var3);
      this.writer.write(new StartPrefixMappingEvent(var2, var3));
   }

   public void adaptAttribute(Node var1) throws XMLStreamException {
      String var2 = var1.getLocalName();
      if (var2 == null) {
         var2 = var1.getNodeName();
      }

      if (var2 == null) {
         throw new XMLStreamException("The local name of an attribute cannot be null");
      } else if (var2.equals("xmlns")) {
         this.adaptNamespace(this.currentStartElement, (String)null, var1.getNodeValue());
      } else {
         String var3 = var1.getPrefix();
         if (var3 != null && var3.equals("xmlns")) {
            this.adaptNamespace(this.currentStartElement, var1.getLocalName(), var1.getNodeValue());
         } else {
            if (DsigConstants.QNAME_VALUE_ATTRIBUTES.contains(new QName(var1.getNamespaceURI(), var2))) {
               String var4 = parsePrefix(var1.getNodeValue());
               if (var4 != null) {
                  String var5 = this.nameSpaceTable.get(var4);
                  if (var5 == null) {
                     var5 = (String)this.externalNamespaces.get(var4);
                  }

                  if (var5 != null) {
                     this.nonVisiblyUsed.add(var4);
                     this.adaptNamespace(this.currentStartElement, var4, var5);
                  }
               }
            }

            this.currentStartElement.addAttribute(new AttributeImpl(new Name(var1.getNamespaceURI(), var2, var1.getPrefix()), var1.getNodeValue(), "CDATA"));
         }
      }
   }

   private static String parsePrefix(String var0) {
      int var1 = var0.indexOf(":");
      if (var1 < 0) {
         return null;
      } else {
         return var1 == 0 ? "" : var0.substring(0, var1);
      }
   }

   public void adaptElement(Node var1, boolean var2) throws XMLStreamException {
      this.writeCurrentStartElement();
      String var3 = var1.getLocalName();
      if (var3 == null) {
         var3 = var1.getNodeName();
      }

      if (var2) {
         this.nameSpaceTable.openScope();
         if (var3 == null) {
            var3 = var1.getNodeName();
         }

         StartElementEvent var4 = new StartElementEvent(new Name(var1.getNamespaceURI(), var3, var1.getPrefix()));
         this.setCurrentStartElement(var4);
      } else {
         this.writer.write(new EndElementEvent(new Name(var1.getNamespaceURI(), var3, var1.getPrefix())));
         List var8 = this.nameSpaceTable.closeScope();
         int var5 = 0;

         for(int var6 = var8.size(); var5 < var6; ++var5) {
            PrefixMapping var7 = (PrefixMapping)var8.get(var5);
            if (var7.getUri() == null) {
               this.writer.write(new EndPrefixMappingEvent(var7.getPrefix()));
            } else {
               this.writer.write(new ChangePrefixMappingEvent(var7.getOldUri(), var7.getUri(), var7.getPrefix()));
            }
         }
      }

   }

   protected boolean isSpace(char var1) {
      return var1 == ' ' || var1 == '\t' || var1 == '\r' || var1 == '\n';
   }

   public void adaptComment(Comment var1) throws XMLStreamException {
      this.writeCurrentStartElement();
      this.writer.write(new CommentEvent(var1.getData()));
   }

   public void adaptText(Node var1) throws XMLStreamException {
      this.writeCurrentStartElement();
      String var2 = var1.getNodeValue();
      this.writer.write(new CharacterDataEvent(var2));
   }

   public void adaptCDATA(Node var1) throws XMLStreamException {
      this.writeCurrentStartElement();
      boolean var2 = true;
      String var3 = var1.getNodeValue();
      char var4 = 'a';
      int var5 = 1;

      for(int var6 = 0; var6 < var3.length(); ++var6) {
         if (!this.isSpace(var3.charAt(var6))) {
            var2 = false;
         }

         if (var4 == '\r') {
            if (var3.charAt(var6) == '\n') {
               ((CDATASection)var1).replaceData(var6 - var5, 1, "");
               ++var5;
            }

            if (var3.charAt(var6) != '\n') {
               ((CDATASection)var1).replaceData(var6 - var5, 1, "\n");
            }
         }

         var4 = var3.charAt(var6);
      }

      if (var3.charAt(var3.length() - 1) == '\r') {
         ((CDATASection)var1).replaceData(var3.length() - var5, 1, "\n");
      }

      if (var2) {
         this.writer.write(new SpaceEvent(var1.getNodeValue()));
      } else {
         this.writer.write(new CharacterDataEvent(var1.getNodeValue()));
      }

   }

   public void adaptProcessingInstruction(ProcessingInstruction var1) throws XMLStreamException {
      this.writer.write(new ProcessingInstructionEvent(new Name(var1.getNodeName()), var1.getData()));
   }
}
