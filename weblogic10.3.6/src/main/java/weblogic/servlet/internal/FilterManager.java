package weblogic.servlet.internal;

import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import weblogic.j2ee.descriptor.FilterBean;
import weblogic.j2ee.descriptor.FilterMappingBean;
import weblogic.j2ee.descriptor.ParamValueBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.security.SubjectUtils;
import weblogic.security.service.SecurityServiceManager;
import weblogic.servlet.FilterUnavailableException;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.security.internal.WebAppSecurity;
import weblogic.servlet.utils.StandardURLMapping;
import weblogic.servlet.utils.URLMapping;
import weblogic.servlet.utils.URLMappingFactory;

public final class FilterManager {
   public static final int REQUEST = 0;
   public static final int FORWARD = 1;
   public static final int INCLUDE = 2;
   public static final int ERROR = 3;
   public static final int UNKNOWN = -1;
   private final WebAppServletContext context;
   private HashMap filters = new HashMap();
   private FilterBean[] filterList;
   private final ArrayList filterPatternList = new ArrayList();
   private final ArrayList filterServletList = new ArrayList();
   private FilterWrapper reqEventsFilterWrapper;

   FilterManager(WebAppServletContext var1) {
      this.context = var1;
   }

   boolean hasFilters() {
      return !this.filterPatternList.isEmpty() || !this.filterServletList.isEmpty();
   }

   void preloadFilters() throws DeploymentException {
      this.preloadReqEventFilter();
      if (this.filterList != null && this.filterList.length >= 1) {
         for(int var1 = 0; var1 < this.filterList.length; ++var1) {
            HashMap var2 = this.getParamsMap(this.filterList[var1].getInitParams());
            Filter var3 = this.loadFilter(this.filterList[var1].getFilterName(), this.filterList[var1].getFilterClass(), var2);
            if (var3 != null) {
               this.filters.put(this.filterList[var1].getFilterName(), new FilterWrapper(var3, this.filterList[var1].getFilterName(), this.filterList[var1].getFilterClass(), var2, this.context));
            }
         }

      }
   }

   private void preloadReqEventFilter() throws DeploymentException {
      String var1 = RequestEventsFilter.class.getName();
      this.reqEventsFilterWrapper = new FilterWrapper(this.loadFilter(var1, var1, (Map)null), var1, var1, (Map)null, this.context);
      this.reqEventsFilterWrapper.setHeadFilter(true);
   }

   private HashMap getParamsMap(ParamValueBean[] var1) {
      HashMap var2 = new HashMap();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2.put(var1[var3].getParamName(), var1[var3].getParamValue());
         }
      }

      return var2;
   }

   synchronized Filter loadFilter(String var1, String var2, Map var3) throws DeploymentException {
      try {
         Filter var4 = this.context.getComponentCreator().createFilterInstance(var2);
         FilterConfigImpl var5 = new FilterConfigImpl(var1, this.context, var3);
         FilterInitAction var6 = new FilterInitAction(var4, var5);
         Throwable var7 = (Throwable)SecurityServiceManager.runAs(WebAppConfigManager.KERNEL_ID, SubjectUtils.getAnonymousSubject(), var6);
         if (var7 != null) {
            if (!(var7 instanceof FilterUnavailableException)) {
               HTTPLogger.logCouldNotLoadFilter(var2, var7);
            }

            this.filters.remove(var1);
            return null;
         } else {
            return var4;
         }
      } catch (ClassNotFoundException var8) {
         HTTPLogger.logCouldNotLoadFilter(this.context.getLogContext() + " " + var2, var8);
         throw new DeploymentException(var8);
      } catch (InstantiationException var9) {
         HTTPLogger.logCouldNotLoadFilter(this.context.getLogContext() + " " + var2, var9);
         throw new DeploymentException(var9);
      } catch (IllegalAccessException var10) {
         HTTPLogger.logCouldNotLoadFilter(this.context.getLogContext() + " " + var2, var10);
         throw new DeploymentException(var10);
      } catch (ClassCastException var11) {
         HTTPLogger.logCouldNotLoadFilter(this.context.getLogContext() + " " + var2, var11);
         throw new DeploymentException(var11);
      }
   }

   void registerFilter(String var1, String var2, String[] var3, String[] var4, Map var5, String[] var6) throws DeploymentException {
      Filter var7 = this.loadFilter(var1, var2, var5);
      int var8;
      if (var3 != null) {
         for(var8 = 0; var8 < var3.length; ++var8) {
            this.registerFilterMapping(var1, (String)null, var3[var8], var6);
         }
      }

      if (var4 != null) {
         for(var8 = 0; var8 < var4.length; ++var8) {
            this.registerFilterMapping(var1, var4[var8], (String)null, var6);
         }
      }

      this.filters.put(var1, new FilterWrapper(var7, var1, var2, var5, this.context));
   }

   void registerFilter(String var1, String var2, String[] var3, String[] var4, Map var5) throws DeploymentException {
      this.registerFilter(var1, var2, var3, var4, var5, (String[])null);
   }

   private synchronized void registerFilterMapping(String var1, String var2, String var3, String[] var4) throws DeploymentException {
      if (var3 != null) {
         StandardURLMapping var5 = URLMappingFactory.createCompatibleURLMapping(this.context.getUrlMatchMap(), this.context.getServletClassLoader(), WebAppConfigManager.isCaseInsensitive(), WebAppSecurity.getEnforceStrictURLPattern());
         if (URLMappingFactory.isInvalidUrlPattern(this.context.getUrlMatchMap(), var3)) {
            throw new DeploymentException("The url-pattern, '" + var3 + "' is not valid");
         }

         var5.put(var3, var1);
         this.filterPatternList.add(new FilterInfo(var1, var5, this.context, var4));
      }

      if (var2 != null) {
         this.filterServletList.add(new FilterInfo(var1, var2, this.context, var4));
      }

   }

   private synchronized void registerFilterMapping(FilterMappingBean var1) throws DeploymentException {
      String var2 = var1.getFilterName();
      String[] var3 = var1.getServletNames();
      String[] var4 = var1.getUrlPatterns();
      String var5 = null;
      int var7;
      if (var4 != null && var4.length > 0) {
         StandardURLMapping var6 = URLMappingFactory.createCompatibleURLMapping(this.context.getUrlMatchMap(), this.context.getServletClassLoader(), WebAppConfigManager.isCaseInsensitive(), WebAppSecurity.getEnforceStrictURLPattern());

         for(var7 = 0; var7 < var4.length; ++var7) {
            if (URLMappingFactory.isInvalidUrlPattern(this.context.getUrlMatchMap(), var4[var7])) {
               throw new DeploymentException("The url-pattern, '" + var4[var7] + "' is not valid");
            }

            var5 = WebAppSecurity.fixupURLPattern(var4[var7]);
            var6.put(var5, var2);
         }

         this.filterPatternList.add(new FilterInfo(var6, var1, this.context));
      }

      HashSet var8 = new HashSet();
      if (var3 != null && var3.length > 0) {
         for(var7 = 0; var7 < var3.length; ++var7) {
            if (var3[var7] != null && !var8.contains(var3[var7])) {
               this.filterServletList.add(new FilterInfo(var2, var3[var7], var1, this.context));
               var8.add(var3[var7]);
            }
         }
      }

   }

   void registerServletFilters(WebAppBean var1) throws DeploymentException {
      this.filterList = var1.getFilters();
      FilterMappingBean[] var2 = var1.getFilterMappings();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            this.registerFilterMapping(var2[var3]);
         }
      }

   }

   FilterChainImpl getFilterChain(ServletStubImpl var1, ServletRequest var2, ServletResponse var3, boolean var4, int var5) throws ServletException {
      ServletRequestImpl var6 = ServletRequestImpl.getOriginalRequest(var2);
      FilterChainImpl var7 = null;
      if (var4) {
         var7 = new FilterChainImpl();
         var7.add(this.reqEventsFilterWrapper);
      }

      String var9 = (String)var2.getAttribute("javax.servlet.include.request_uri");
      String var8;
      if (var9 != null) {
         var8 = var9.substring(((String)var2.getAttribute("javax.servlet.include.context_path")).length());
      } else if (var2.getAttribute("weblogic.servlet.errorPage") != null) {
         var8 = (String)var2.getAttribute("weblogic.servlet.errorPage");
      } else {
         var8 = var6.getRelativeUri();
      }

      if (this.filters.size() > 0) {
         Iterator var10 = this.filterPatternList.iterator();

         String var13;
         while(var10.hasNext()) {
            FilterInfo var11 = (FilterInfo)var10.next();
            if (this.isApplicable(var5, var11)) {
               URLMapping var12 = var11.getMap();
               var13 = (String)var12.get(var8);
               if (var13 != null) {
                  FilterWrapper var14 = (FilterWrapper)this.filters.get(var13);
                  if (var14 != null) {
                     if (var7 == null) {
                        var7 = new FilterChainImpl();
                     }

                     var7.add(var14);
                  }
               }
            }
         }

         String var16 = var1.getServletName();
         Iterator var17 = this.filterServletList.iterator();

         label62:
         while(true) {
            String var19;
            do {
               FilterInfo var18;
               do {
                  if (!var17.hasNext()) {
                     break label62;
                  }

                  var18 = (FilterInfo)var17.next();
               } while(!this.isApplicable(var5, var18));

               var13 = var18.getFilterName();
               var19 = var18.getServletName();
            } while(!var19.equals(var16) && !"*".equals(var19));

            FilterWrapper var15 = (FilterWrapper)this.filters.get(var13);
            if (var15 != null) {
               if (var7 == null) {
                  var7 = new FilterChainImpl();
               }

               var7.add(var15);
            }
         }
      }

      if (var7 == null) {
         return null;
      } else {
         var7.add((Filter)(new TailFilter(var1)));
         var7.setOrigRequest(var6);
         var7.setOrigResponse(var6.getResponse());
         return var7;
      }
   }

   private boolean isApplicable(int var1, FilterInfo var2) {
      if (var1 == 0 && var2.applyOnRequest()) {
         return true;
      } else if (var1 == 1 && var2.applyOnForward()) {
         return true;
      } else if (var1 == 2 && var2.applyOnInclude()) {
         return true;
      } else {
         return var1 == 3 && var2.applyOnError();
      }
   }

   synchronized void destroyFilters() {
      if (this.filters.size() >= 1) {
         String var2;
         if (this.filterList != null) {
            for(int var1 = this.filterList.length - 1; var1 >= 0; --var1) {
               var2 = this.filterList[var1].getFilterName();
               this.destroyFilter(var2);
               this.filters.remove(var2);
            }
         }

         Iterator var3 = this.filters.keySet().iterator();

         while(var3.hasNext()) {
            var2 = (String)var3.next();
            this.destroyFilter(var2);
         }

         this.filters.clear();
         this.filterPatternList.clear();
         this.filterServletList.clear();
      }
   }

   private void destroyFilter(String var1) {
      FilterWrapper var2 = (FilterWrapper)this.filters.get(var1);
      if (var2 != null) {
         Filter var3 = var2.getFilter();
         if (var3 != null) {
            this.destroyFilter(var3, var1);
            this.context.getComponentCreator().notifyPreDestroy(var3);
         }
      }
   }

   synchronized void destroyFilter(Filter var1, String var2) {
      FilterDestroyAction var3 = new FilterDestroyAction(var1);
      Throwable var4 = (Throwable)SecurityServiceManager.runAs(WebAppConfigManager.KERNEL_ID, SubjectUtils.getAnonymousSubject(), var3);
      if (var4 != null) {
         HTTPLogger.logFailedWhileDestroyingFilter(var2, var4);
      }

   }

   public boolean isFilterRegistered(String var1) {
      return this.filters.containsKey(var1);
   }

   private static class FilterInfo {
      private String filterName;
      private String servletName;
      private URLMapping map;
      private boolean applyOnRequest;
      private boolean applyOnForward;
      private boolean applyOnInclude;
      private boolean applyOnError;

      private FilterInfo(String var1, String var2) {
         this.filterName = var1;
         this.servletName = var2;
      }

      private FilterInfo(URLMapping var1) {
         this.map = var1;
      }

      private FilterInfo(String var1, String var2, FilterMappingBean var3, WebAppServletContext var4) throws DeploymentException {
         this(var1, var2);
         this.initDispatcher(var3, var4);
      }

      private FilterInfo(URLMapping var1, FilterMappingBean var2, WebAppServletContext var3) throws DeploymentException {
         this(var1);
         this.initDispatcher(var2, var3);
      }

      private FilterInfo(String var1, String var2, WebAppServletContext var3, String[] var4) throws DeploymentException {
         this(var1, var2);
         this.initDispatcher(var3, var4, var1);
      }

      private FilterInfo(String var1, URLMapping var2, WebAppServletContext var3, String[] var4) throws DeploymentException {
         this(var2);
         this.initDispatcher(var3, var4, var1);
      }

      private String getFilterName() {
         return this.filterName;
      }

      private String getServletName() {
         return this.servletName;
      }

      private URLMapping getMap() {
         return this.map;
      }

      private boolean applyOnRequest() {
         return this.applyOnRequest;
      }

      private boolean applyOnForward() {
         return this.applyOnForward;
      }

      private boolean applyOnInclude() {
         return this.applyOnInclude;
      }

      private boolean applyOnError() {
         return this.applyOnError;
      }

      private void initDispatcher(FilterMappingBean var1, WebAppServletContext var2) throws DeploymentException {
         String[] var3 = var1.getDispatchers();
         this.initDispatcher(var2, var3, var1.getFilterName());
      }

      private void initDispatcher(WebAppServletContext var1, String[] var2, String var3) throws DeploymentException {
         if (var2 != null && var2.length >= 1) {
            for(int var4 = 0; var4 < var2.length; ++var4) {
               if (var2[var4].equals("REQUEST")) {
                  this.applyOnRequest = true;
               } else if (var2[var4].equals("FORWARD")) {
                  this.applyOnForward = true;
               } else if (var2[var4].equals("INCLUDE")) {
                  this.applyOnInclude = true;
               } else {
                  if (!var2[var4].equals("ERROR")) {
                     Loggable var5 = HTTPLogger.logInvalidFilterDispatcherLoggable(var1.getLogContext(), var3, var2[var4]);
                     throw new DeploymentException(var5.getMessage());
                  }

                  this.applyOnError = true;
               }
            }
         } else {
            this.applyOnRequest = true;
            if (var1.getConfigManager().isFilterDispatchedRequestsEnabled()) {
               this.applyOnForward = this.applyOnInclude = true;
            }
         }

      }

      // $FF: synthetic method
      FilterInfo(String var1, URLMapping var2, WebAppServletContext var3, String[] var4, Object var5) throws DeploymentException {
         this(var1, var2, var3, var4);
      }

      // $FF: synthetic method
      FilterInfo(String var1, String var2, WebAppServletContext var3, String[] var4, Object var5) throws DeploymentException {
         this(var1, var2, var3, var4);
      }

      // $FF: synthetic method
      FilterInfo(URLMapping var1, FilterMappingBean var2, WebAppServletContext var3, Object var4) throws DeploymentException {
         this(var1, var2, var3);
      }

      // $FF: synthetic method
      FilterInfo(String var1, String var2, FilterMappingBean var3, WebAppServletContext var4, Object var5) throws DeploymentException {
         this(var1, var2, var3, var4);
      }
   }

   private final class FilterDestroyAction implements PrivilegedAction {
      private final Filter filter;

      FilterDestroyAction(Filter var2) {
         this.filter = var2;
      }

      public Object run() {
         try {
            this.filter.destroy();
            return null;
         } catch (Throwable var2) {
            return var2;
         }
      }
   }

   private final class FilterInitAction implements PrivilegedAction {
      private final Filter filter;
      private final FilterConfig cfg;

      FilterInitAction(Filter var2, FilterConfig var3) {
         this.filter = var2;
         this.cfg = var3;
      }

      public Object run() {
         try {
            this.filter.init(this.cfg);
            return null;
         } catch (Throwable var2) {
            return var2;
         }
      }
   }
}
