package weblogic.auddi.uddi.soap;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.soap.SOAPWrapper;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.response.DispositionReportResponse;
import weblogic.auddi.uddi.response.DispositionReportResponseHandler;
import weblogic.auddi.uddi.response.ErrorDispositionReportResponse;
import weblogic.auddi.uddi.response.Result;
import weblogic.auddi.util.Logger;
import weblogic.auddi.xml.ParserFactory;
import weblogic.auddi.xml.SchemaException;
import weblogic.auddi.xml.XMLUtil;

public class FaultWrapper {
   private boolean m_isNode;
   private String m_soapFaultString;
   private Node m_Node;
   UDDIException m_exception;
   DispositionReportResponse m_disposition;

   public FaultWrapper(UDDIException var1) {
      this(var1, (String)null, "Client Error");
   }

   public FaultWrapper(Node var1) throws UDDIException {
      this.m_exception = null;
      this.m_disposition = null;
      Logger.trace("+FaultWrapper.CTOR(Node)");
      if (var1 == null) {
         throw new IllegalArgumentException("Node is null");
      } else {
         this.m_isNode = true;
         this.m_Node = var1;
         NodeList var2 = this.m_Node.getOwnerDocument().getElementsByTagNameNS("*", "dispositionReport");
         if (var2.getLength() == 0) {
            throw new IllegalArgumentException("Node is not a valid fault message");
         } else {
            Node var3 = var2.item(0);
            DispositionReportResponseHandler var4 = new DispositionReportResponseHandler();
            this.m_disposition = (DispositionReportResponse)var4.create(var3);
            Logger.trace("-FaultWrapper.CTOR(Node)");
         }
      }
   }

   public FaultWrapper(UDDIException var1, String var2, String var3) {
      this.m_exception = null;
      this.m_disposition = null;
      Logger.trace("+FaultWrapper.CTOR(UDDIException,String,String)");
      if (var1 == null) {
         throw new IllegalArgumentException("Exception cannot be null");
      } else {
         this.m_isNode = false;
         this.m_exception = var1;
         this.m_disposition = new ErrorDispositionReportResponse();
         Result var4 = new Result(this.m_exception);
         this.m_disposition.addResult(var4);
         this.m_soapFaultString = SOAPWrapper.createFaultMessage(var2, var3, var4);
         Logger.trace("-FaultWrapper.CTOR(UDDIException,String,String)");
      }
   }

   public DispositionReportResponse getDisposition() {
      return this.m_disposition;
   }

   public String toXML() {
      return this.m_isNode ? this.nodeToXML() : this.exceptionToXML();
   }

   private String exceptionToXML() {
      Logger.trace("+FaultWrapper.exceptionToXML()");

      try {
         Document var1 = (new ParserFactory()).createDOMParserNS().parseRequest(this.m_soapFaultString);
         NodeList var2 = var1.getElementsByTagNameNS("*", "Fault");
         Node var3 = var2.item(0);
         Logger.trace("-FaultWrapper.exceptionToXML()");
         return XMLUtil.nodeToString(var3);
      } catch (SchemaException var4) {
         var4.printStackTrace();
         Logger.trace("-FaultWrapper.exceptionToXML()");
         return "";
      }
   }

   private String nodeToXML() {
      Logger.trace("+FaultWrapper.nodeToXML()");
      NodeList var1 = this.m_Node.getOwnerDocument().getElementsByTagNameNS("*", "Fault");
      Logger.trace("-FaultWrapper.nodeToXML()");
      return XMLUtil.nodeToString(var1.item(0));
   }
}
