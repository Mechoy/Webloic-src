package weblogic.servlet.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import weblogic.management.configuration.WebServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.ServletResponseImpl;
import weblogic.utils.PlatformConstants;
import weblogic.utils.http.HttpRequestParser;
import weblogic.utils.string.SimpleCachingDateFormat;

public final class CLFLogger implements Logger {
   private final byte[] LINE_SEP;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final CLFDateFormat format;
   private final CLFDateFormatWithMillis formatWithMillis;
   private final LogManagerHttp logManager;
   private boolean logMillis;

   public CLFLogger(LogManagerHttp var1, WebServerMBean var2) {
      this.LINE_SEP = PlatformConstants.EOL.getBytes();
      this.format = new CLFDateFormat();
      this.formatWithMillis = new CLFDateFormatWithMillis();
      this.logManager = var1;
      this.logMillis = var2.getWebServerLog().isLogMilliSeconds();
   }

   public int log(ServletRequestImpl var1, ServletResponseImpl var2) {
      FormatStringBuffer var3 = new FormatStringBuffer(128);
      if (ManagementService.getRuntimeAccess(kernelId).getServer().isReverseDNSAllowed()) {
         var3.appendValueOrDash(var1.getRemoteHost());
      } else {
         var3.appendValueOrDash(var1.getRemoteAddr());
      }

      var3.appendSpaceDashSpace();
      var3.appendValueOrDash(var1.getHttpAccountingInfo().getRemoteUser());
      var3.append(' ');
      if (this.logMillis) {
         var3.append(this.formatWithMillis.getDateAsBytes(var1));
         var3.append(' ');
      } else {
         var3.append(this.format.getDateAsBytes(var1));
         var3.append(' ');
      }

      var3.append('"');

      try {
         HttpRequestParser var4 = var1.getInputHelper().getRequestParser();
         ByteBuffer var5 = var4.getFullRequrestUriBytes();
         var3.append(var5.array(), var5.position(), var5.limit() - var5.position());
      } catch (Exception var7) {
         var3.append(var1.getRequestURI());
      }

      var3.append('"');
      var3.append(' ');
      var3.appendStatusCode(var2.getStatus()).append(' ');
      var3.append(var2.getContentLength());
      var3.append(' ');
      var3.append(this.LINE_SEP);
      OutputStream var8 = this.logManager.getLogStream();

      try {
         var8.write(var3.getBytes(), 0, var3.size());
      } catch (IOException var6) {
      }

      return var3.size();
   }

   public void markRotated() {
   }

   private static class CLFDateFormatWithMillis {
      private Date date = new Date();
      private SimpleDateFormat format = new SimpleDateFormat("[dd/MMM/yyyy:HH:mm:ss.SSS Z]");

      CLFDateFormatWithMillis() {
         this.format.setTimeZone(TimeZone.getDefault());
      }

      byte[] getDateAsBytes(ServletRequestImpl var1) {
         long var2 = var1.getHttpAccountingInfo().getInvokeTime();
         this.date.setTime(var2);
         return this.format.format(this.date).getBytes();
      }
   }

   private static class CLFDateFormat extends SimpleCachingDateFormat {
      private byte[] cachedBytes;
      private String cachedString;

      CLFDateFormat() {
         super("[dd/MMM/yyyy:HH:mm:ss Z]");
      }

      byte[] getDateAsBytes(ServletRequestImpl var1) {
         long var2 = var1.getHttpAccountingInfo().getInvokeTime();
         String var4 = super.getDate(var2);
         if (var4 != this.cachedString) {
            this.cachedBytes = var4.getBytes();
            this.cachedString = var4;
         }

         return this.cachedBytes;
      }
   }
}
