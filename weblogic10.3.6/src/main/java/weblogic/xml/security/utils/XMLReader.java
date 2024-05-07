package weblogic.xml.security.utils;

import java.util.Map;
import javax.xml.namespace.QName;

public interface XMLReader {
   int END_DOCUMENT = 0;
   int START_ELEMENT = 2;
   int END_ELEMENT = 4;
   int CHARACTER_DATA = 16;

   int next();

   boolean hasName();

   String getNamespaceURI() throws IllegalStateException;

   String getLocalName() throws IllegalStateException;

   void require(int var1, String var2, String var3) throws ValidationException;

   void close();

   int getEventType();

   boolean isStartElement();

   boolean isEndElement();

   boolean isCharacters();

   String getAttribute(String var1, String var2) throws IllegalStateException;

   QName getQNameAttribute(String var1, String var2) throws IllegalStateException;

   String getNamespaceURI(String var1);

   Map getNamespaceMap();

   String getText() throws IllegalStateException;

   QName getQNameText() throws IllegalStateException;
}
