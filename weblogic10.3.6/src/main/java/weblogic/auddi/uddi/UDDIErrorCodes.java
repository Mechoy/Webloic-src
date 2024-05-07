package weblogic.auddi.uddi;

public class UDDIErrorCodes {
   public static final int E_AUTH_TOKEN_EXPIRED = 10110;
   public static final int E_AUTH_TOKEN_REQUIRED = 10120;
   public static final int E_ACCOUNT_LIMIT_EXCEEDED = 10160;
   public static final int E_BUSY = 10400;
   public static final int E_FATAL_ERROR = 10500;
   public static final int E_INVALID_KEY_PASSED = 10210;
   public static final int E_LANGUAGE_ERROR = 10060;
   public static final int E_NAME_TOO_LONG = 10020;
   public static final int E_OPERATOR_MISMATCH = 10130;
   public static final int E_SUCCESS = 0;
   public static final int E_TOO_MANY_OPTIONS = 10030;
   public static final int E_UNRECOGNIZED_VERSION = 10040;
   public static final int E_UNKNOWN_USER = 10150;
   public static final int E_UNSUPPORTED = 10050;
   public static final int E_USER_MISMATCH = 10140;
   public static final int E_ASSERTION_NOT_FOUND = 30000;
   public static final int E_INVALID_PROJECTION = 20230;
   public static final int E_INVALID_COMPLETION_STATUS = 30100;
   public static final int E_INVALID_VALUE = 20200;
   public static final int E_MESSAGE_TOO_LARGE = 30110;
   public static final int E_PUBLISHER_CANCELLED = 30220;
   public static final int E_REQUEST_DENIED = 30210;
   public static final int E_SECRET_UNKNOWN = 30230;
   public static final int E_TRANSFER_ABORTED = 30200;
   public static final int E_VALUE_NOT_ALLOWED = 20210;
   public static final int E_EX_EMPTY_BODY = 90001;

   public static String getCode(int var0) {
      String var1 = null;
      switch (var0) {
         case 0:
            var1 = "E_success";
            break;
         case 10020:
            var1 = "E_nameTooLong";
            break;
         case 10030:
            var1 = "E_tooManyOptions";
            break;
         case 10040:
            var1 = "E_unrecognizedVersion";
            break;
         case 10050:
            var1 = "E_unsupported";
            break;
         case 10060:
            var1 = "E_languageError";
            break;
         case 10110:
            var1 = "E_authTokenExpired";
            break;
         case 10120:
            var1 = "E_authTokenRequired";
            break;
         case 10140:
            var1 = "E_userMismatch";
            break;
         case 10150:
            var1 = "E_unknownUser";
            break;
         case 10160:
            var1 = "E_accountLimitExceeded";
            break;
         case 10210:
            var1 = "E_invalidKeyPassed";
            break;
         case 10400:
            var1 = "E_busy";
            break;
         case 10500:
            var1 = "E_fatalError";
            break;
         case 20200:
            var1 = "E_invalidValue";
            break;
         case 20210:
            var1 = "E_valueNotAllowed";
            break;
         case 20230:
            var1 = "E_invalidProjection";
            break;
         case 30000:
            var1 = "E_assertionNotFound";
            break;
         case 30100:
            var1 = "E_invalidCompletionStatus";
            break;
         case 30110:
            var1 = "E_messageTooLarge";
            break;
         case 30200:
            var1 = "E_transferAborted";
            break;
         case 30210:
            var1 = "E_requestDenied";
            break;
         case 30220:
            var1 = "E_publisherCancelled";
            break;
         case 30230:
            var1 = "E_secretUnknown";
      }

      return var1;
   }

   public static String getMessage(int var0) {
      String var1 = null;
      switch (var0) {
         case 0:
            var1 = UDDIMessages.get("error.success.base");
            break;
         case 10020:
            var1 = UDDIMessages.get("error.nameTooLong.base");
            break;
         case 10030:
            var1 = UDDIMessages.get("error.tooManyOptions.base");
            break;
         case 10040:
            var1 = UDDIMessages.get("error.unrecognizedVersion.base");
            break;
         case 10050:
            var1 = UDDIMessages.get("error.unsupported.base");
            break;
         case 10060:
            var1 = UDDIMessages.get("error.languageError.base");
            break;
         case 10110:
            var1 = UDDIMessages.get("error.authTokenExpired.base");
            break;
         case 10120:
            var1 = UDDIMessages.get("error.authTokenRequired.base");
            break;
         case 10140:
            var1 = UDDIMessages.get("error.userMismatch.base");
            break;
         case 10150:
            var1 = UDDIMessages.get("error.unknownUser.base");
            break;
         case 10160:
            var1 = UDDIMessages.get("error.accountLimitExceeded.base");
            break;
         case 10210:
            var1 = UDDIMessages.get("error.invalidKeyPassed.base");
            break;
         case 10400:
            var1 = UDDIMessages.get("error.busy.base");
            break;
         case 10500:
            var1 = UDDIMessages.get("error.fatalError.base");
            break;
         case 20200:
            var1 = UDDIMessages.get("error.invalidValue.base");
            break;
         case 20210:
            var1 = UDDIMessages.get("error.valueNotAllowed.base");
            break;
         case 20230:
            var1 = UDDIMessages.get("error.invalidProjection.base");
            break;
         case 30000:
            var1 = UDDIMessages.get("error.assertionNotFound.base");
            break;
         case 30100:
            var1 = UDDIMessages.get("error.invalidCompletionStatus.base");
            break;
         case 30110:
            var1 = UDDIMessages.get("error.messageTooLarge.base");
            break;
         case 30200:
            var1 = UDDIMessages.get("error.transferAborted.base");
            break;
         case 30210:
            var1 = UDDIMessages.get("error.requestDenied.base");
            break;
         case 30220:
            var1 = UDDIMessages.get("error.publisherCancelled.base");
            break;
         case 30230:
            var1 = UDDIMessages.get("error.secretUnknown.base");
            break;
         case 90001:
            var1 = UDDIMessages.get("error.emptyBody.base");
      }

      return var1;
   }
}
