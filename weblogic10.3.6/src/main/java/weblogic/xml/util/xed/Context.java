package weblogic.xml.util.xed;

import java.util.ArrayList;
import java.util.List;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.AttributeIterator;
import weblogic.xml.stream.CharacterData;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.events.AttributeImpl;
import weblogic.xml.stream.events.CharacterDataEvent;
import weblogic.xml.stream.events.ElementEvent;
import weblogic.xml.stream.events.Name;
import weblogic.xml.stream.events.StartElementEvent;

public class Context {
   private Name name;
   private Name attName;
   private ElementEvent currentEvent;
   private AttributeImpl att;

   public void set(XMLEvent var1) {
      this.currentEvent = (ElementEvent)var1;
      this.name = (Name)var1.getName();
   }

   public void set(XMLEvent var1, Attribute var2) {
      this.currentEvent = (ElementEvent)var1;
      this.name = (Name)var1.getName();
      this.att = (AttributeImpl)var2;
      this.attName = (Name)var2.getName();
   }

   public String getPrefix() {
      return this.name.getPrefix();
   }

   public String getLocalName() {
      return this.name.getLocalName();
   }

   public String getUri() {
      return this.name.getNamespaceUri();
   }

   public String getText() {
      return ((CharacterData)this.currentEvent).getContent();
   }

   public String getAttLocalName() {
      return this.attName.getLocalName();
   }

   public String getAttPrefix() {
      return this.attName.getPrefix();
   }

   public String getAttValue() {
      return this.att.getValue();
   }

   public void setPrefix(String var1) {
      this.name.setPrefix(var1);
   }

   public void setUri(String var1) {
      this.name.setNamespaceUri(var1);
   }

   public void setLocalName(String var1) {
      this.name.setLocalName(var1);
   }

   public List getAttributes() {
      ArrayList var1 = new ArrayList();
      AttributeIterator var2 = ((StartElement)this.currentEvent).getAttributesAndNamespaces();

      while(var2.hasNext()) {
         var1.add(var2.next());
      }

      return var1;
   }

   public void setAttributes(List var1) {
      ((StartElementEvent)this.currentEvent).setAttributes(var1);
   }

   public void setText(String var1) {
      ((CharacterDataEvent)this.currentEvent).setContent(var1);
   }

   public void setAttLocalName(String var1) {
      this.attName.setLocalName(var1);
   }

   public void setAttPrefix(String var1) {
      this.attName.setPrefix(var1);
   }

   public void setAttValue(String var1) {
      this.att.setValue(var1);
   }

   public int getEventType() {
      return this.currentEvent.getType();
   }

   public Object lookup(Variable var1) throws StreamEditorException {
      if ("prefix".equals(var1.getName())) {
         return this.getPrefix();
      } else if ("localName".equals(var1.getName())) {
         return this.getLocalName();
      } else if ("uri".equals(var1.getName())) {
         return this.getUri();
      } else if ("text".equals(var1.getName())) {
         return this.getText();
      } else if ("@prefix".equals(var1.getName())) {
         return this.getAttPrefix();
      } else if ("@localName".equals(var1.getName())) {
         return this.getAttLocalName();
      } else if ("@value".equals(var1.getName())) {
         return this.getAttValue();
      } else if ("@attributes".equals(var1.getName())) {
         return this.getAttributes();
      } else {
         throw new StreamEditorException("Unknown variable:" + var1.getName());
      }
   }

   public void assign(Variable var1, Object var2) throws StreamEditorException {
      if ("prefix".equals(var1.getName())) {
         this.setPrefix((String)var2);
      } else if ("localName".equals(var1.getName())) {
         this.setLocalName((String)var2);
      } else if ("uri".equals(var1.getName())) {
         this.setUri((String)var2);
      } else if ("text".equals(var1.getName())) {
         this.setText((String)var2);
      } else if ("@prefix".equals(var1.getName())) {
         this.setAttPrefix((String)var2);
      } else if ("@localName".equals(var1.getName())) {
         this.setAttLocalName((String)var2);
      } else if ("@value".equals(var1.getName())) {
         this.setAttValue((String)var2);
      } else {
         if (!"@attributes".equals(var1.getName())) {
            throw new StreamEditorException("Unknown variable:" + var1.getName());
         }

         this.setAttributes((List)var2);
      }

   }
}
