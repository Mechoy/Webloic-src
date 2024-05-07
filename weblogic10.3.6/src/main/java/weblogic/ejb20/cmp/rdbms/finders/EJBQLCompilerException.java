package weblogic.ejb20.cmp.rdbms.finders;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.xml.sax.SAXParseException;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.j2ee.validation.IDescriptorError;
import weblogic.j2ee.validation.IDescriptorErrorInfo;
import weblogic.logging.Loggable;
import weblogic.utils.AssertionError;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.StackTraceUtils;
import weblogic.xml.process.XMLParsingException;
import weblogic.xml.process.XMLProcessingException;

public class EJBQLCompilerException extends Exception implements IDescriptorError {
   private static final long serialVersionUID = 5756198563339201332L;
   private IDescriptorErrorInfo errorInfo;
   private ErrorCollectionException errorCollectionEx;
   private boolean ejbqlRewritten = false;
   private String ejbqlRewrittenReasonsString = "Unknown ?";
   private String originalEjbqlText;
   private List ejbqlTokenList;
   private String header = "";
   private String errorIndicatorLeft = " =>> ";
   private String errorIndicatorRight = " <<=  ";
   private static Class[] bugs = new Class[]{Error.class, NullPointerException.class, AssertionError.class};

   public EJBQLCompilerException(ErrorCollectionException var1, boolean var2, String var3, String var4, List var5, String var6, IDescriptorErrorInfo var7) {
      this.errorCollectionEx = var1;
      this.ejbqlTokenList = var5;
      this.originalEjbqlText = var4;
      this.ejbqlRewrittenReasonsString = var3;
      this.ejbqlRewritten = var2;
      this.errorInfo = var7;
      if (var6 != null) {
         this.header = var6;
      }

   }

   public ErrorCollectionException getErrorCollectionException() {
      return this.errorCollectionEx;
   }

   public Collection getExceptions() {
      return this.getErrorCollectionException().getExceptions();
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.getErrorHeader()).append("\n\n");
      if (this.ejbqlRewritten) {
         var1.append(this.getEjbqlHasBeenRewrittenMessage()).append("\n\n");
         var1.append("    ").append(this.originalEjbqlText).append("\n\n\n\n");
         var1.append(" The rewritten query is:\n\n");
      }

      var1.append(this.getDefaultEjbqlString()).append("\n\n");
      var1.append(this.getExceptionMessagesOnly());
      return var1.toString();
   }

   public String getMessage() {
      return this.toString();
   }

   public List getEjbqlTokenList() {
      return this.ejbqlTokenList;
   }

   public String getErrorHeader() {
      return this.header;
   }

   public String getEjbqlHasBeenRewrittenMessage() {
      Loggable var1 = EJBLogger.logEjbqlHasBeenRewrittenLoggable(this.ejbqlRewrittenReasonsString);
      return var1.getMessage();
   }

   public boolean isEmpty() {
      return this.getErrorCollectionException().isEmpty();
   }

   public String getExceptionMessagesOnly() {
      StringBuffer var1 = new StringBuffer();
      if (super.getMessage() != null) {
         var1.append(super.getMessage());
      }

      Iterator var2 = this.getExceptions().iterator();

      while(var2.hasNext()) {
         Throwable var3 = (Throwable)var2.next();
         String var4 = this.formatExceptionMessage(var3);
         var1.append(var4);
      }

      return var1.toString();
   }

   private String formatExceptionMessage(Throwable var1) {
      String var2 = var1.getMessage();
      StringBuffer var3 = new StringBuffer();
      if (var1 instanceof ClassNotFoundException) {
         var3.append("Unable to load class: " + var2 + "\n");
      } else if (var1 instanceof XMLParsingException) {
         XMLParsingException var4 = (XMLParsingException)var1;
         Throwable var5 = var4.getNestedException();
         if (var5 instanceof SAXParseException) {
            SAXParseException var6 = (SAXParseException)var5;
            var3.append("Error parsing '" + var4.getFileName() + "' line " + var6.getLineNumber() + ": " + var2 + "\n");
         } else {
            var3.append("Error parsing '" + var4.getFileName() + "': " + var2 + "\n");
         }
      } else if (var1 instanceof XMLProcessingException) {
         XMLProcessingException var7 = (XMLProcessingException)var1;
         var3.append("Error processing '" + var7.getFileName() + "': " + var2 + "\n");
      } else if (var2 != null) {
         var3.append(var2 + "\n");
      }

      boolean var8 = EJBDebugService.compilationLogger.isDebugEnabled() || EJBDebugService.deploymentLogger.isDebugEnabled();
      if (var8 || var2 == null || this.isBug(var1)) {
         var3.append(StackTraceUtils.throwable2StackTrace(var1));
      }

      return var3.toString();
   }

   private boolean isBug(Throwable var1) {
      for(int var2 = 0; var2 < bugs.length; ++var2) {
         if (bugs[var2].isAssignableFrom(var1.getClass())) {
            return true;
         }
      }

      return false;
   }

   public String getDefaultEjbqlString() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.ejbqlTokenList.iterator();

      while(var2.hasNext()) {
         EJBQLToken var3 = (EJBQLToken)var2.next();
         if (var3.getHadException()) {
            var1.append(this.errorIndicatorLeft);
            var1.append(var3.getTokenText());
            var1.deleteCharAt(var1.length() - 1);
            var1.append(this.errorIndicatorRight);
         } else {
            var1.append(var3.getTokenText());
         }
      }

      return var1.toString();
   }

   public boolean hasErrorInfo() {
      return this.errorInfo != null;
   }

   public IDescriptorErrorInfo getErrorInfo() {
      return this.errorInfo;
   }

   public void setDescriptorErrorInfo(IDescriptorErrorInfo var1) {
      this.errorInfo = var1;
   }
}
