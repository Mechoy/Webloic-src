package weblogic.xml.security.utils;

import weblogic.xml.stream.EndElement;
import weblogic.xml.stream.Location;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.events.EndElementEvent;

class MutableEnd implements XMLEvent, EndElement {
   private final EndElementEvent endElementEvent;
   private boolean namespaced;

   public MutableEnd(EndElementEvent var1) {
      this.namespaced = false;
      this.endElementEvent = var1;
   }

   public MutableEnd(EndElement var1) {
      this(new EndElementEvent(var1.getName(), var1.getLocation()));
      this.endElementEvent.setSchemaType(var1.getSchemaType());
   }

   public MutableEnd(XMLName var1) {
      this(new EndElementEvent(var1));
   }

   void markNamespaced() {
      this.namespaced = true;
   }

   boolean namespaced() {
      return this.namespaced;
   }

   void setName(XMLName var1) {
      this.namespaced = false;
      this.endElementEvent.setName(var1);
   }

   public final String getTypeAsString() {
      return this.endElementEvent.getTypeAsString();
   }

   public final int getType() {
      return this.endElementEvent.getType();
   }

   public final XMLName getName() {
      return this.endElementEvent.getName();
   }

   public final boolean hasName() {
      return this.endElementEvent.hasName();
   }

   public final Location getLocation() {
      return this.endElementEvent.getLocation();
   }

   public final boolean isEndElement() {
      return true;
   }

   public final boolean isStartElement() {
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

   public final XMLName getSchemaType() {
      return this.endElementEvent.getSchemaType();
   }

   public final boolean equals(Object var1) {
      return this.endElementEvent.equals(var1);
   }

   public final String toString() {
      return this.endElementEvent.toString();
   }
}
