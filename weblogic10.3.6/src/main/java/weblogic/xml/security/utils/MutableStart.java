package weblogic.xml.security.utils;

import java.util.List;
import java.util.Map;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.AttributeIterator;
import weblogic.xml.stream.Location;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.events.StartElementEvent;

public final class MutableStart implements StartElement {
   private final StartElementEvent start;
   private boolean namespaced;
   private boolean attributesNamespaced;

   public MutableStart(StartElementEvent var1) {
      this.namespaced = false;
      this.attributesNamespaced = false;
      this.start = var1;
   }

   public MutableStart(StartElement var1) {
      this(new StartElementEvent(var1));
   }

   public MutableStart(XMLName var1) {
      this(new StartElementEvent(var1));
   }

   void markNamespaced() {
      this.namespaced = true;
   }

   void markAttributesNamespaced() {
      this.attributesNamespaced = true;
   }

   boolean namespaced() {
      return this.namespaced;
   }

   boolean attributesNamespaced() {
      return this.attributesNamespaced;
   }

   void setName(XMLName var1) {
      this.namespaced = false;
      this.start.setName(var1);
   }

   public void addAttribute(Attribute var1) {
      this.attributesNamespaced = false;
      this.start.addAttribute(var1);
   }

   void setAttributes(List var1) {
      this.attributesNamespaced = false;
      this.start.setAttributes(var1);
   }

   void addNamespace(Attribute var1) {
      this.start.addNamespace(var1);
   }

   void setNamespaceMap(Map var1) {
      this.start.setNamespaceMap(var1);
   }

   public Attribute getAttributeByName(XMLName var1) {
      return this.start.getAttributeByName(var1);
   }

   public final AttributeIterator getAttributesAndNamespaces() {
      return this.start.getAttributesAndNamespaces();
   }

   public final String getNamespaceUri(String var1) {
      return this.start.getNamespaceUri(var1);
   }

   public final int getType() {
      return this.start.getType();
   }

   public final XMLName getSchemaType() {
      return this.start.getSchemaType();
   }

   public final String getTypeAsString() {
      return this.start.getTypeAsString();
   }

   public final XMLName getName() {
      return this.start.getName();
   }

   public final boolean hasName() {
      return this.start.hasName();
   }

   public final Location getLocation() {
      return this.start.getLocation();
   }

   public final boolean isStartElement() {
      return true;
   }

   public final boolean isEndElement() {
      return false;
   }

   public final boolean isEntityReference() {
      return false;
   }

   public final boolean isStartPrefixMapping() {
      return false;
   }

   public final boolean isEndPrefixMapping() {
      return false;
   }

   public final boolean isChangePrefixMapping() {
      return false;
   }

   public final boolean isProcessingInstruction() {
      return false;
   }

   public final boolean isCharacterData() {
      return false;
   }

   public final boolean isSpace() {
      return false;
   }

   public final boolean isNull() {
      return false;
   }

   public final boolean isStartDocument() {
      return false;
   }

   public final boolean isEndDocument() {
      return false;
   }

   public final AttributeIterator getAttributes() {
      return this.start.getAttributes();
   }

   public final AttributeIterator getNamespaces() {
      return this.start.getNamespaces();
   }

   public final Map getNamespaceMap() {
      return this.start.getNamespaceMap();
   }

   public final boolean equals(Object var1) {
      return this.start.equals(var1);
   }

   public String toString() {
      return this.start.toString();
   }
}
