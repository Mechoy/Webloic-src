package weblogic.xml.security.utils;

import java.util.Map;
import javax.xml.namespace.QName;

public interface XMLWriter {
   void writeStartElement(String var1, String var2) throws XMLWriterRuntimeException;

   void writeAttribute(String var1, String var2, String var3) throws XMLWriterRuntimeException, IllegalStateException;

   void writeAttribute(String var1, String var2, QName var3) throws XMLWriterRuntimeException, IllegalStateException;

   void writeCharacters(String var1) throws XMLWriterRuntimeException;

   void writeCharacters(QName var1) throws XMLWriterRuntimeException, IllegalStateException;

   void writeEndElement() throws XMLWriterRuntimeException;

   void setPrefix(String var1, String var2) throws XMLWriterRuntimeException;

   void setDefaultNamespace(String var1) throws XMLWriterRuntimeException;

   void setDefaultPrefixes(Map var1);

   Map getNamespaceMap();

   void setNamespaceMap(Map var1);

   void flush() throws XMLWriterRuntimeException;

   void close() throws XMLWriterRuntimeException;
}
