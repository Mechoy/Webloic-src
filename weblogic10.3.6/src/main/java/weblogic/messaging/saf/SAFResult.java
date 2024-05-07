package weblogic.messaging.saf;

import java.util.List;

public interface SAFResult {
   int SUCCESSFUL = 0;
   int DUPLICATE = 1;
   int OUTOFORDER = 2;
   int ENDPOITNNOTAVAIL = 3;
   int NOTPERMITTED = 4;
   int CONVERSATIONTERMINATED = 5;
   int UNKNOWNCONVERSATION = 6;
   int CONVERSATIONREFUSED = 7;
   int CONVERSATIONTIMEOUT = 8;
   int ADMINPURGED = 9;
   int CONVERSATIONPOISENED = 10;
   int EXPIRED = 11;
   int SAFINTERNALERROR = 12;
   int SAFHRWRITEFAILURE = 13;
   int SAFNOCURRENTTX = 14;
   int SAFTXCOMMITFAILURE = 15;
   int SAFTXROLLBACKFAILURE = 16;
   int SAFTXNOTSTARTED = 17;
   int SAFNOTALLOWED = 18;
   int SAFSEENLASTMESSAGE = 19;
   int PERMANENTTRANSPORTERROR = 20;
   String[] description = new String[]{" Operation Successful.", " Duplcate Message in the Conversation.", " Out Of Order Messsage in the Conversation.", " Endpoint is not available.", " Operation not permitted.", " Conversation is terminated.", " Unknown Conversation.", " Conversation refused.", " Conversation timed out.", " Conversation is purged Adminstratively.", " Conversation is poisoned.", " Conversation is expired.", " Internal Error.", " Internal Error: History Record Write Failure.", " Internal Error: No Current Transaction available.", " Internal Error: Transaction Commit Failure.", " Internal Error: Transaction Rollback Failure.", " Internal Error: Transaction not started.", " Endpoint does not allow store-and-forward operation.", " Cannot send more messages after Last Message of a conversation/sequence.", " Transport indicated a permanent error, no retry allowed"};

   boolean isDuplicate();

   boolean isSuccessful();

   SAFConversationInfo getConversationInfo();

   List getSequenceNumbers();

   int getResultCode();

   void setSAFException(SAFException var1);

   SAFException getSAFException();

   String getDescription();
}
