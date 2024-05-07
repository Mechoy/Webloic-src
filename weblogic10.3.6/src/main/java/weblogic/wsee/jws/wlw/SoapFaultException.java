package weblogic.wsee.jws.wlw;

import org.apache.xmlbeans.XmlObject;

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
      try {
         Class var2 = getContextClassLoader().loadClass("weblogic.wsee.jws.wlw.schemas.soap11.Fault");
         Class var3 = getContextClassLoader().loadClass("weblogic.wsee.jws.wlw.schemas.soap12.Fault");
         if (var2.isAssignableFrom(var1.getClass())) {
            this._faultContent = var1;
            this._soapFaultVersion = 1;
         } else if (var3.isAssignableFrom(var1.getClass())) {
            this._faultContent = var1;
            this._soapFaultVersion = 2;
         } else {
            this._detailContent = new XmlObject[]{var1};
            this._soapFaultVersion = 0;
         }

      } catch (ClassNotFoundException var4) {
         throw new RuntimeException("Unbale to load wlw soap fault types from context classloader", var4);
      }
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

   private static final ClassLoader getContextClassLoader() {
      return Thread.currentThread().getContextClassLoader();
   }
}
