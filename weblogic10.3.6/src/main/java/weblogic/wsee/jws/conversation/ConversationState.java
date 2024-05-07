package weblogic.wsee.jws.conversation;

import weblogic.security.acl.internal.AuthenticatedSubject;

public interface ConversationState {
   String getId();

   long getTimeStamp();

   AuthenticatedSubject getAltAuthenticatedSubject();
}
