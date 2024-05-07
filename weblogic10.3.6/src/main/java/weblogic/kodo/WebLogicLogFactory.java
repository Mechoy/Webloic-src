package weblogic.kodo;

import kodo.bea.BEALogFactory;
import kodo.bea.BEALogImpl;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.i18n.logging.MessageDispatcher;
import weblogic.i18n.logging.MessageLogger;

public class WebLogicLogFactory extends BEALogFactory {
   private final BEALogImpl.TraceEnabledHook UNKNOWN = new BEALogImpl.TraceEnabledHook() {
      public boolean isTraceEnabled() {
         return false;
      }
   };
   private final BEALogImpl.TraceEnabledHook METADATA = new TraceEnabledHookImpl("DebugJpaMetaData");
   private final BEALogImpl.TraceEnabledHook ENHANCE = new TraceEnabledHookImpl("DebugJpaEnhance");
   private final BEALogImpl.TraceEnabledHook RUNTIME = new TraceEnabledHookImpl("DebugJpaRuntime");
   private final BEALogImpl.TraceEnabledHook QUERY = new TraceEnabledHookImpl("DebugJpaQuery");
   private final BEALogImpl.TraceEnabledHook DATACACHE = new TraceEnabledHookImpl("DebugJpaDataCache");
   private final BEALogImpl.TraceEnabledHook TOOL = new TraceEnabledHookImpl("DebugJpaTool");
   private final BEALogImpl.TraceEnabledHook MANAGE = new TraceEnabledHookImpl("DebugJpaManage");
   private final BEALogImpl.TraceEnabledHook PROFILE = new TraceEnabledHookImpl("DebugJpaProfile");
   private final BEALogImpl.TraceEnabledHook SQL = new TraceEnabledHookImpl("DebugJpaJdbcSql");
   private final BEALogImpl.TraceEnabledHook JDBC = new TraceEnabledHookImpl("DebugJpaJdbcJdbc");
   private final BEALogImpl.TraceEnabledHook SCHEMA = new TraceEnabledHookImpl("DebugJpaJdbcSchema");

   protected BEALogImpl newBEALogImpl(String var1, MessageLogger var2, MessageDispatcher var3, String var4, String var5) {
      BEALogImpl var6 = super.newBEALogImpl(var1, var2, var3, var4, var5);
      BEALogImpl.TraceEnabledHook var7;
      if (var1.endsWith("MetaData")) {
         var7 = this.METADATA;
      } else if (var1.endsWith("Enhance")) {
         var7 = this.ENHANCE;
      } else if (var1.endsWith("Runtime")) {
         var7 = this.RUNTIME;
      } else if (var1.endsWith("Query")) {
         var7 = this.QUERY;
      } else if (var1.endsWith("DataCache")) {
         var7 = this.DATACACHE;
      } else if (var1.endsWith("Tool")) {
         var7 = this.TOOL;
      } else if (var1.endsWith("Manage")) {
         var7 = this.MANAGE;
      } else if (var1.endsWith("Profile")) {
         var7 = this.PROFILE;
      } else if (var1.endsWith("SQL")) {
         var7 = this.SQL;
      } else if (var1.endsWith("JDBC")) {
         var7 = this.JDBC;
      } else if (var1.endsWith("Schema")) {
         var7 = this.SCHEMA;
      } else {
         var7 = this.UNKNOWN;
      }

      var6.setTraceEnabledHook(var7);
      return var6;
   }

   public class TraceEnabledHookImpl implements BEALogImpl.TraceEnabledHook {
      private final DebugLogger logger;

      public TraceEnabledHookImpl(String var2) {
         this.logger = DebugLogger.getDebugLogger(var2);
      }

      public boolean isTraceEnabled() {
         return this.logger.isDebugEnabled();
      }
   }
}
