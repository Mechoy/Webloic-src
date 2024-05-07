package weblogic.wsee.reliability2.api;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public interface ReliabilityErrorContext {
   boolean isRequestSpecific();

   String getOperationName();

   XMLStreamReader getRequestStreamReader() throws XMLStreamException;

   <T> T getRequest(JAXBContext var1, Class<T> var2) throws JAXBException, XMLStreamException;

   List<Throwable> getFaults();

   String getFaultSummaryMessage();

   Map<String, Serializable> getUserRequestContextProperties();
}
