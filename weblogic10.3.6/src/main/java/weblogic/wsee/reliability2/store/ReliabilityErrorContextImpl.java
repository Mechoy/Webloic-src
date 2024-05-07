package weblogic.wsee.reliability2.store;

import com.sun.xml.ws.api.message.Message;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.wsee.reliability2.api.ReliabilityErrorContext;

public class ReliabilityErrorContextImpl implements ReliabilityErrorContext {
   private boolean _requestSpecific;
   private String _operationName;
   private Message _message;
   private List<Throwable> _faults;
   private String _faultSummaryMessage;
   private Map<String, Serializable> _userRequestContextProperties;

   public ReliabilityErrorContextImpl(boolean var1, String var2, Message var3, List<Throwable> var4, String var5, Map<String, Serializable> var6) {
      this._requestSpecific = var1;
      this._operationName = var2;
      this._message = var3;
      this._faults = var4;
      this._faultSummaryMessage = var5;
      this._userRequestContextProperties = var6;
   }

   public boolean isRequestSpecific() {
      return this._requestSpecific;
   }

   public String getOperationName() {
      this.verifyRequestSpecific();
      return this._operationName;
   }

   private void verifyRequestSpecific() {
      if (!this._requestSpecific) {
         throw new IllegalStateException("Not request specific");
      }
   }

   public XMLStreamReader getRequestStreamReader() throws XMLStreamException {
      this.verifyRequestSpecific();
      return this._message.readPayload();
   }

   public <T> T getRequest(JAXBContext var1, Class<T> var2) throws JAXBException, XMLStreamException {
      this.verifyRequestSpecific();
      Unmarshaller var3 = var1.createUnmarshaller();
      XMLStreamReader var4 = this._message.readPayload();
      return var3.unmarshal(var4, var2).getValue();
   }

   public List<Throwable> getFaults() {
      return this._faults;
   }

   public String getFaultSummaryMessage() {
      return this._faultSummaryMessage;
   }

   public Map<String, Serializable> getUserRequestContextProperties() {
      return this._userRequestContextProperties;
   }
}
