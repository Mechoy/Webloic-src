package weblogic.ejb.container.cmp11.rdbms;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import weblogic.ejb.container.cmp11.rdbms.finders.Finder;

public final class BeanWriter {
   public static final boolean debug = false;
   public static final boolean verbose = false;
   private PrintWriter writer = null;
   private int indent = 0;

   public void putRDBMSBean(RDBMSBean var1, OutputStream var2) {
      this.writer = new PrintWriter(var2, true);
      this.docType("weblogic-rdbms-bean", "-//BEA Systems, Inc.//DTD WebLogic 5.1.0 EJB RDBMS Persistence//EN", "http://www.bea.com/servers/wls510/dtd/weblogic-rdbms-persistence.dtd");
      this.openTag("weblogic-rdbms-bean");
      this.stringValue("pool-name", var1.getPoolName(), true);
      this.stringValue("schema-name", var1.getSchemaName(), false);
      this.stringValue("table-name", var1.getTableName(), true);
      this.openTag("attribute-map");
      Iterator var3 = var1.getObjectLinks();

      while(var3.hasNext()) {
         RDBMSBean.ObjectLink var4 = (RDBMSBean.ObjectLink)var3.next();
         this.openTag("object-link");
         this.stringValue("bean-field", var4.getBeanField(), true);
         this.stringValue("dbms-column", var4.getDBMSColumn(), true);
         this.closeTag("object-link");
      }

      this.closeTag("attribute-map");
      this.openTag("finder-list");

      for(Iterator var9 = var1.getFinders(); var9.hasNext(); this.closeTag("finder")) {
         Finder var5 = (Finder)var9.next();
         this.openTag("finder");
         this.stringValue("method-name", var5.getName(), true);
         this.openTag("method-params");
         Iterator var6 = var5.getParameterTypes();

         while(var6.hasNext()) {
            String var7 = (String)var6.next();
            this.stringValue("method-param", var7, false);
         }

         this.closeTag("method-params");
         this.cDataValue("finder-query", var5.getWeblogicQuery(), true);
         Iterator var10 = var5.getFinderExpressions();

         while(var10.hasNext()) {
            Finder.FinderExpression var8 = (Finder.FinderExpression)var10.next();
            this.openTag("finder-expression");
            this.intValue("expression-number", var8.getNumber());
            this.cDataValue("expression-text", var8.getExpressionText(), false);
            this.stringValue("expression-type", var8.getExpressionType(), true);
            this.closeTag("finder-expression");
         }

         if (var5.getFinderOptions() != null) {
            this.openTag("finder-options");
            Finder.FinderOptions var11 = var5.getFinderOptions();
            this.booleanValue("find-for-update", var11.getFindForUpdate());
            this.closeTag("finder-options");
         }
      }

      this.closeTag("finder-list");
      this.openTag("options");
      this.booleanValue("use-quoted-names", var1.getUseQuotedNames());
      if (var1.getTransactionIsolation() != null) {
         this.stringValue("transaction-isolation", RDBMSUtils.isolationLevelToString(var1.getTransactionIsolation()), false);
      }

      this.closeTag("options");
      this.closeTag("weblogic-rdbms-bean");
   }

   private void openTag(String var1) {
      this.indentCurrentLevel();
      this.printOpenTag(var1);
      this.writer.println();
      ++this.indent;
   }

   private void closeTag(String var1) {
      --this.indent;
      this.indentCurrentLevel();
      this.printCloseTag(var1);
      this.writer.println();
   }

   private void stringValue(String var1, String var2, boolean var3) {
      if (var3 || var2 != null) {
         this.indentCurrentLevel();
         this.printOpenTag(var1);
         if (var2 != null) {
            this.writer.print(var2);
         }

         this.printCloseTag(var1);
         this.writer.println();
      }

   }

   private void cDataValue(String var1, String var2, boolean var3) {
      this.stringValue(var1, "<![CDATA[" + var2 + "]]>", var3);
   }

   private void booleanValue(String var1, boolean var2) {
      this.indentCurrentLevel();
      this.printOpenTag(var1);
      this.writer.print((new Boolean(var2)).toString());
      this.printCloseTag(var1);
      this.writer.println();
   }

   private void intValue(String var1, int var2) {
      this.indentCurrentLevel();
      this.printOpenTag(var1);
      this.writer.print(var2);
      this.printCloseTag(var1);
      this.writer.println();
   }

   private void indentCurrentLevel() {
      for(int var1 = 0; var1 < this.indent * 2; ++var1) {
         this.writer.print(' ');
      }

   }

   private void printOpenTag(String var1) {
      this.writer.print("<");
      this.writer.print(var1);
      this.writer.print(">");
   }

   private void printCloseTag(String var1) {
      this.writer.print("</");
      this.writer.print(var1);
      this.writer.print(">");
   }

   private void docType(String var1, String var2, String var3) {
      this.writer.print("<!DOCTYPE ");
      this.writer.print(var1);
      this.writer.println(" PUBLIC ");
      this.writer.print(" \"");
      this.writer.print(var2);
      this.writer.println("\"");
      this.writer.print(" \"");
      this.writer.print(var3);
      this.writer.print("\">");
      this.writer.println();
   }
}
