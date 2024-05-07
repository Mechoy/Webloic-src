package weblogic.auddi.uddi.util;

import weblogic.auddi.soap.SOAPWrapper;
import weblogic.auddi.uddi.AccountLimitExceededException;
import weblogic.auddi.uddi.AssertionNotFoundException;
import weblogic.auddi.uddi.AuthTokenExpiredException;
import weblogic.auddi.uddi.AuthTokenRequiredException;
import weblogic.auddi.uddi.BusyException;
import weblogic.auddi.uddi.EmptyBodyException;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.InvalidCompletionStatusException;
import weblogic.auddi.uddi.InvalidKeyPassedException;
import weblogic.auddi.uddi.InvalidProjectionException;
import weblogic.auddi.uddi.InvalidValueException;
import weblogic.auddi.uddi.LanguageErrorException;
import weblogic.auddi.uddi.MessageTooLargeException;
import weblogic.auddi.uddi.NameTooLongException;
import weblogic.auddi.uddi.PublisherCancelledException;
import weblogic.auddi.uddi.RequestDeniedException;
import weblogic.auddi.uddi.SecretUnknownException;
import weblogic.auddi.uddi.TooManyOptionsException;
import weblogic.auddi.uddi.TransferAbortedException;
import weblogic.auddi.uddi.UDDIErrorCodes;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UnknownUserException;
import weblogic.auddi.uddi.UnrecognizedVersionException;
import weblogic.auddi.uddi.UnsupportedException;
import weblogic.auddi.uddi.UserMismatchException;
import weblogic.auddi.uddi.ValueNotAllowedException;
import weblogic.auddi.uddi.response.ErrorDispositionReportResponse;
import weblogic.auddi.uddi.response.Result;
import weblogic.auddi.uddi.response.Results;
import weblogic.auddi.uddi.soap.FaultWrapper;

public class UDDIExceptionMapper {
   private UDDIExceptionMapper() {
   }

   public static ErrorDispositionReportResponse toDispositionReport(UDDIException var0) {
      FaultWrapper var1 = new FaultWrapper(var0);
      ErrorDispositionReportResponse var2 = (ErrorDispositionReportResponse)var1.getDisposition();
      return var2;
   }

   public static String toDispositionXML(UDDIException var0) {
      FaultWrapper var1 = new FaultWrapper(var0);
      String var2 = var1.toXML();
      return var2;
   }

   public static String toDispositionSOAP(UDDIException var0) {
      String var1 = toDispositionXML(var0);
      String var2 = SOAPWrapper.makeSOAPString(var1);
      return var2;
   }

   public static UDDIException toException(ErrorDispositionReportResponse var0) {
      Object var1 = null;
      Results var2 = var0.getResults();
      if (var2 != null && var2.size() > 0) {
         Result var3 = var2.getFirst();
         if (var3 != null) {
            int var4 = var3.getErrno();
            String var5 = var3.getErrMsg();
            String var6 = UDDIErrorCodes.getMessage(var4);
            if (var5.startsWith(var6)) {
               int var7 = var6.length();
               var5 = var5.substring(var7);
               if (var5.startsWith(" '") && var5.endsWith("'")) {
                  byte var8 = 2;
                  int var9 = var5.length() - 1;
                  var5 = var5.substring(var8, var9);
               }
            }

            switch (var4) {
               case 10020:
                  var1 = new NameTooLongException(var5);
                  break;
               case 10030:
                  var1 = new TooManyOptionsException(var5);
                  break;
               case 10040:
                  var1 = new UnrecognizedVersionException(var5);
                  break;
               case 10050:
                  var1 = new UnsupportedException(var5);
                  break;
               case 10060:
                  var1 = new LanguageErrorException(var5);
                  break;
               case 10110:
                  var1 = new AuthTokenExpiredException(var5);
                  break;
               case 10120:
                  var1 = new AuthTokenRequiredException(var5);
                  break;
               case 10140:
                  var1 = new UserMismatchException(var5);
                  break;
               case 10150:
                  var1 = new UnknownUserException(var5);
                  break;
               case 10160:
                  var1 = new AccountLimitExceededException(var5);
                  break;
               case 10210:
                  var1 = new InvalidKeyPassedException(var5);
                  break;
               case 10400:
                  var1 = new BusyException(var5);
                  break;
               case 10500:
                  var1 = new FatalErrorException(var5);
                  break;
               case 20200:
                  var1 = new InvalidValueException(var5);
                  break;
               case 20210:
                  var1 = new ValueNotAllowedException(var5);
                  break;
               case 20230:
                  var1 = new InvalidProjectionException(var5);
                  break;
               case 30000:
                  var1 = new AssertionNotFoundException(var5);
                  break;
               case 30100:
                  var1 = new InvalidCompletionStatusException(var5);
                  break;
               case 30110:
                  var1 = new MessageTooLargeException(var5);
                  break;
               case 30200:
                  var1 = new TransferAbortedException(var5);
                  break;
               case 30210:
                  var1 = new RequestDeniedException(var5);
                  break;
               case 30220:
                  var1 = new PublisherCancelledException(var5);
                  break;
               case 30230:
                  var1 = new SecretUnknownException(var5);
                  break;
               case 90001:
                  var1 = new EmptyBodyException(var5);
            }
         }
      }

      return (UDDIException)var1;
   }
}
