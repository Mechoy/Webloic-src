package weblogic.wsee.jws;

import com.bea.xml.XmlObject;

/** @deprecated */
public class SoapFaultException extends RuntimeException {
   private XmlObject[] _detailContent;
   private XmlObject _faultContent;
   public static final int FAULT_UNKNOWN = 0;
   public static final int FAULT_SOAP11 = 1;
   public static final int FAULT_SOAP12 = 2;
   private int _soapFaultVersion = 0;
   private boolean _senderIsCause = false;

   /** @deprecated */
   public SoapFaultException(XmlObject var1, String var2) {
      this._detailContent = new XmlObject[]{var1};
      this._soapFaultVersion = 0;
   }

   public SoapFaultException(XmlObject[] var1) {
      this._detailContent = var1;
      this._soapFaultVersion = 0;
   }

   public SoapFaultException(XmlObject var1) {
      this._detailContent = new XmlObject[]{var1};
      this._soapFaultVersion = 0;
   }

   public boolean hasDetail() {
      return this._detailContent != null;
   }

   public boolean hasFault() {
      return this._faultContent != null;
   }

   public XmlObject[] getDetail() {
      return this._detailContent;
   }

   public boolean isCausedBySender() {
      return this._senderIsCause;
   }

   public XmlObject getFault() {
      return this._faultContent;
   }

   public int soapFaultVersion() {
      return this._soapFaultVersion;
   }

   public void setCausedBySender(boolean var1) {
      this._senderIsCause = var1;
   }
}
