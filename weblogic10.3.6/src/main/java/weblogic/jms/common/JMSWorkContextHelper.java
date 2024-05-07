package weblogic.jms.common;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.workarea.WorkContextHelper;
import weblogic.workarea.WorkContextInput;
import weblogic.workarea.WorkContextOutput;
import weblogic.workarea.spi.WorkContextMapInterceptor;
import weblogic.workarea.utils.WorkContextInputAdapter;
import weblogic.workarea.utils.WorkContextOutputAdapter;

public class JMSWorkContextHelper {
   private static final boolean DEBUG = false;
   private static WorkContextHelper helper = WorkContextHelper.getWorkContextHelper();

   public static void infectMessage(MessageImpl var0) {
      WorkContextMapInterceptor var1 = WorkContextHelper.getWorkContextHelper().getLocalInterceptor();
      if (var1 != null) {
         var0.setWorkContext(var1.copyThreadContexts(48));
      }

   }

   public static void infectThread(MessageImpl var0) {
      Object var1 = var0.getWorkContext();
      if (var1 != null) {
         WorkContextHelper.getWorkContextHelper().setLocalInterceptor((WorkContextMapInterceptor)var1);
      }

   }

   public static void disinfectThread() {
      WorkContextHelper.getWorkContextHelper().setLocalInterceptor((WorkContextMapInterceptor)null);
   }

   static void writeWorkContext(Object var0, ObjectOutput var1) throws IOException {
      WorkContextMapInterceptor var2 = (WorkContextMapInterceptor)var0;
      if (var1 instanceof WorkContextOutput) {
         var2.sendRequest((WorkContextOutput)var1, 48);
      } else {
         var2.sendRequest(new WorkContextOutputAdapter(var1), 48);
      }

   }

   static Object readWorkContext(ObjectInput var0) throws IOException {
      WorkContextMapInterceptor var1 = WorkContextHelper.getWorkContextHelper().createInterceptor();
      if (var0 instanceof WorkContextInput) {
         var1.receiveRequest((WorkContextInput)var0);
      } else {
         var1.receiveRequest(new WorkContextInputAdapter(var0));
      }

      return var1;
   }

   private static void p(String var0) {
      System.out.println("<JMSWorkContextHelper>: " + var0);
   }
}
