package weblogic.xml.crypto.wss.nonce;

import java.util.Calendar;
import javax.xml.rpc.soap.SOAPFaultException;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.wss.TimestampHandler;
import weblogic.xml.crypto.wss.api.NonceValidator;

public class DisableNonceValidatorImpl implements NonceValidator {
   private static final boolean debug = false;

   public DisableNonceValidatorImpl() {
      LogUtils.logWss("Nonce Validator is diabaled");
   }

   public void init(String var1, TimestampHandler var2) {
      LogUtils.logWss("No init due to Nonce Validator is diabaled");
   }

   public void checkNonceAndTime(String var1, Calendar var2) throws SOAPFaultException {
   }

   public void setExpirationTime(int var1) {
   }

   public void checkDuplicateNonce(String var1) throws SOAPFaultException {
   }
}
