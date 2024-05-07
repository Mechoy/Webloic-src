package weblogic.wsee.jaxws.framework.policy;

import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.api.streaming.XMLStreamWriterFactory;
import com.sun.xml.ws.wsdl.parser.WSDLConstants;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.jws.Policy;
import weblogic.jws.Policy.Direction;

class PatchFilter implements XMLStreamWriter, XMLStreamWriterFactory.RecycleAware {
   protected XMLStreamWriter inner;
   private PolicyMap policyMap;
   private QName currentElement = null;
   private List<QName> context = new ArrayList();
   private String defaultNamespace = "";
   private QName portName;
   private QName bindingName;
   private String targetNamespace = "";
   private QName currentOperation = null;
   private QName currentBinding = null;
   private boolean wssutilFlag = false;
   private boolean wspFlag = false;

   public PatchFilter(WSEndpoint var1, XMLStreamWriter var2) {
      this.inner = var2;
      this.portName = var1.getPort().getName();
      this.bindingName = var1.getPort().getBinding().getName();
      this.policyMap = new PolicyMap(var1.getContainer(), var1.getSEIModel(), var1.getBinding(), this.portName, var1.getImplementationClass());
   }

   public void close() throws XMLStreamException {
      this.onCurrentChange();
      this.inner.close();
   }

   public void flush() throws XMLStreamException {
      this.inner.flush();
   }

   public NamespaceContext getNamespaceContext() {
      return this.inner.getNamespaceContext();
   }

   public String getPrefix(String var1) throws XMLStreamException {
      return this.inner.getPrefix(var1);
   }

   public Object getProperty(String var1) throws IllegalArgumentException {
      return this.inner.getProperty(var1);
   }

   public void setDefaultNamespace(String var1) throws XMLStreamException {
      this.inner.setDefaultNamespace(var1);
      this.defaultNamespace = var1;
   }

   public void setNamespaceContext(NamespaceContext var1) throws XMLStreamException {
      this.inner.setNamespaceContext(var1);
   }

   public void setPrefix(String var1, String var2) throws XMLStreamException {
      this.inner.setPrefix(var1, var2);
   }

   public void writeAttribute(String var1, String var2) throws XMLStreamException {
      this.checkAttribute((String)null, var1, var2);
      this.inner.writeAttribute(var1, var2);
   }

   public void writeAttribute(String var1, String var2, String var3) throws XMLStreamException {
      this.checkAttribute(var1, var2, var3);
      this.inner.writeAttribute(var1, var2, var3);
   }

   public void writeAttribute(String var1, String var2, String var3, String var4) throws XMLStreamException {
      this.checkAttribute(var2, var3, var4);
      this.inner.writeAttribute(var1, var2, var3, var4);
   }

   public void writeCData(String var1) throws XMLStreamException {
      this.onCurrentChange();
      this.inner.writeCData(var1);
   }

   public void writeCharacters(char[] var1, int var2, int var3) throws XMLStreamException {
      this.onCurrentChange();
      this.inner.writeCharacters(var1, var2, var3);
   }

   public void writeCharacters(String var1) throws XMLStreamException {
      this.onCurrentChange();
      this.inner.writeCharacters(var1);
   }

   public void writeComment(String var1) throws XMLStreamException {
      this.inner.writeComment(var1);
   }

   public void writeDefaultNamespace(String var1) throws XMLStreamException {
      this.inner.writeDefaultNamespace(var1);
      this.defaultNamespace = var1;
   }

   public void writeDTD(String var1) throws XMLStreamException {
      this.onCurrentChange();
      this.inner.writeDTD(var1);
   }

   public void writeEmptyElement(String var1) throws XMLStreamException {
      this.onCurrentChange();
      this.inner.writeEmptyElement(var1);
   }

   public void writeEmptyElement(String var1, String var2) throws XMLStreamException {
      this.onCurrentChange();
      this.inner.writeEmptyElement(var1, var2);
   }

   public void writeEmptyElement(String var1, String var2, String var3) throws XMLStreamException {
      this.onCurrentChange();
      this.inner.writeEmptyElement(var1, var2, var3);
   }

   public void writeEndDocument() throws XMLStreamException {
      this.onCurrentChange();
      this.inner.writeEndDocument();
   }

   public void writeEndElement() throws XMLStreamException {
      this.onCurrentChange();
      this.context.remove(this.context.size() - 1);
      this.inner.writeEndElement();
   }

   public void writeEntityRef(String var1) throws XMLStreamException {
      this.onCurrentChange();
      this.inner.writeEntityRef(var1);
   }

   public void writeNamespace(String var1, String var2) throws XMLStreamException {
      if (!this.isIgnorablePrefix(var1)) {
         this.inner.writeNamespace(var1, var2);
      }
   }

   private boolean isIgnorablePrefix(String var1) {
      if (WSDLConstants.QNAME_DEFINITIONS.equals(this.currentElement)) {
         if ("wsp".equals(var1)) {
            if (this.wspFlag) {
               return true;
            }

            this.wspFlag = true;
         }

         if ("wssutil".equals(var1)) {
            if (this.wssutilFlag) {
               return true;
            }

            this.wssutilFlag = true;
         }
      }

      return false;
   }

   public void writeProcessingInstruction(String var1) throws XMLStreamException {
      this.onCurrentChange();
      this.inner.writeProcessingInstruction(var1);
   }

   public void writeProcessingInstruction(String var1, String var2) throws XMLStreamException {
      this.onCurrentChange();
      this.inner.writeProcessingInstruction(var1, var2);
   }

   public void writeStartDocument() throws XMLStreamException {
      this.inner.writeStartDocument();
   }

   public void writeStartDocument(String var1) throws XMLStreamException {
      this.inner.writeStartDocument(var1);
   }

   public void writeStartDocument(String var1, String var2) throws XMLStreamException {
      this.inner.writeStartDocument(var1, var2);
   }

   public void writeStartElement(String var1) throws XMLStreamException {
      this.onCurrentChange();
      this.inner.writeStartElement(var1);
      this.currentElement = new QName(this.defaultNamespace, var1);
   }

   public void writeStartElement(String var1, String var2) throws XMLStreamException {
      this.onCurrentChange();
      this.inner.writeStartElement(var1, var2);
      this.currentElement = new QName(var1, var2);
   }

   public void writeStartElement(String var1, String var2, String var3) throws XMLStreamException {
      this.onCurrentChange();
      this.inner.writeStartElement(var1, var2, var3);
      this.currentElement = new QName(var3, var2, var1);
   }

   private void checkAttribute(String var1, String var2, String var3) {
      if (this.currentElement != null) {
         if (var1 == null && "targetNamespace".equals(var2) && this.currentElement.equals(WSDLConstants.QNAME_DEFINITIONS)) {
            this.targetNamespace = var3;
         } else if (var1 == null && "name".equals(var2) && this.currentElement.equals(WSDLConstants.QNAME_OPERATION)) {
            this.currentOperation = new QName(this.targetNamespace, var3);
         } else if (var1 == null && "name".equals(var2) && this.currentElement.equals(WSDLConstants.QNAME_BINDING)) {
            this.currentBinding = new QName(this.targetNamespace, var3);
         }
      }

   }

   private void onCurrentChange() {
      if (this.currentElement != null) {
         if (this.currentElement.equals(WSDLConstants.QNAME_DEFINITIONS)) {
            this.policyMap.addDefinitionsExtension(this.inner, this.wssutilFlag, this.wspFlag);
            this.wspFlag = true;
            this.wssutilFlag = true;
         } else if (this.currentElement.equals(WSDLConstants.QNAME_BINDING) && this.bindingName.equals(this.currentBinding)) {
            this.policyMap.addApplicablePolicyReferences(this.portName, (Policy.Direction)null, (XMLStreamWriter)this.inner);
         }

         if (!this.context.isEmpty() && this.bindingName.equals(this.currentBinding)) {
            QName var1 = (QName)this.context.get(this.context.size() - 1);
            if (var1.equals(WSDLConstants.QNAME_BINDING)) {
               if (this.currentElement.equals(WSDLConstants.QNAME_OPERATION) && this.currentOperation != null) {
                  this.policyMap.addApplicablePolicyReferences(this.currentOperation, Direction.both, this.inner);
               }
            } else if (var1.equals(WSDLConstants.QNAME_OPERATION) && this.context.size() > 1) {
               QName var2 = (QName)this.context.get(this.context.size() - 2);
               if (var2.equals(WSDLConstants.QNAME_BINDING)) {
                  if (this.currentElement.equals(WSDLConstants.QNAME_INPUT)) {
                     if (this.currentOperation != null) {
                        this.policyMap.addApplicablePolicyReferences(this.currentOperation, Direction.inbound, this.inner);
                     }
                  } else if (this.currentElement.equals(WSDLConstants.QNAME_OUTPUT) && this.currentOperation != null) {
                     this.policyMap.addApplicablePolicyReferences(this.currentOperation, Direction.outbound, this.inner);
                  }
               }
            }
         }

         this.context.add(this.currentElement);
         this.currentElement = null;
      }

   }

   public void onRecycled() {
      XMLStreamWriterFactory.recycle(this.inner);
      this.inner = null;
   }
}
