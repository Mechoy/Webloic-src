package weblogic.servlet;

import javax.servlet.http.HttpServletResponse;
import weblogic.servlet.internal.FileSenderImpl;

public final class SendFileUtility {
   public static FileSender getZeroCopyFileSender(HttpServletResponse var0) {
      return FileSenderImpl.getZeroCopyFileSender(var0);
   }

   public static FileSender getFileSender(HttpServletResponse var0) {
      return FileSenderImpl.getFileSender(var0);
   }
}
