package weblogic.ejb.container.interfaces;

import javax.ejb.EJBContext;
import javax.transaction.Transaction;

public interface WLEnterpriseBean {
   int STATE_SET_CONTEXT = 1;
   int STATE_UNSET_CONTEXT = 2;
   int STATE_EJB_CREATE = 4;
   int STATE_EJB_POSTCREATE = 8;
   int STATE_EJB_REMOVE = 16;
   int STATE_EJB_ACTIVATE = 32;
   int STATE_EJB_PASSIVATE = 64;
   int STATE_BUSINESS_METHOD = 128;
   int STATE_AFTER_BEGIN = 256;
   int STATE_BEFORE_COMPLETION = 512;
   int STATE_AFTER_COMPLETION = 1024;
   int STATE_EJBFIND = 2048;
   int STATE_EJBSELECT = 4096;
   int STATE_EJBHOME = 8192;
   int STATE_EJBLOAD = 16384;
   int STATE_EJBSTORE = 32768;
   int STATE_EJBTIMEOUT = 65536;
   int STATE_WEBSERVICE_BUSINESS_METHOD = 131072;
   int ALLOWED_GET_EJB_HOME = 258047;
   int ALLOWED_SF_GET_CALLER_PRINCIPAL = 2036;
   int ALLOWED_SL_GET_CALLER_PRINCIPAL = 196736;
   int ALLOWED_EN_GET_CALLER_PRINCIPAL = 125084;
   int ALLOWED_SF_IS_CALLER_IN_ROLE = 2036;
   int ALLOWED_SL_IS_CALLER_IN_ROLE = 196736;
   int ALLOWED_EN_IS_CALLER_IN_ROLE = 125084;
   int ALLOWED_SESSION_GET_EJB_OBJECT = 198644;
   int ALLOWED_ENTITY_GET_EJB_OBJECT = 114936;
   int ALLOWED_SESSION_GET_ROLLBACK_ONLY = 197504;
   int ALLOWED_ENTITY_GET_ROLLBACK_ONLY = 125084;
   int ALLOWED_SESSION_SET_ROLLBACK_ONLY = 197504;
   int ALLOWED_ENTITY_SET_ROLLBACK_ONLY = 125084;
   int ALLOWED_GET_USER_TRANSACTION = 196852;
   int ALLOWED_GET_PRIMARY_KEY = 114936;
   int ALLOWED_GET_TIMER_SERVICE = 254204;
   int ALLOWED_INVOKE_TIMER_SERVICE = 245912;
   int ALLOWED_SL_GET_MESSAGE_CONTEXT = 131072;

   EJBContext __WL_getEJBContext();

   void __WL_setEJBContext(EJBContext var1);

   boolean __WL_isBusy();

   void __WL_setBusy(boolean var1);

   boolean __WL_getIsLocal();

   void __WL_setIsLocal(boolean var1);

   Transaction __WL_getBeanManagedTransaction();

   void __WL_setBeanManagedTransaction(Transaction var1);

   int __WL_getMethodState();

   void __WL_setMethodState(int var1);

   boolean __WL_needsRemove();

   void __WL_setNeedsRemove(boolean var1);

   void __WL_setLoadUser(Object var1);

   Object __WL_getLoadUser();

   void __WL_setCreatorOfTx(boolean var1);

   boolean __WL_isCreatorOfTx();
}
