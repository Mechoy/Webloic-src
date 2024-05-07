package weblogic.management.scripting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.TreeMap;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ObjectName;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.management.scripting.utils.ErrorInformation;
import weblogic.management.scripting.utils.WLSTMsgTextFormatter;
import weblogic.utils.StringUtils;

public class BrowseHandler extends BrowseHelper {
   private boolean currentlyResetting = false;
   private WLSTMsgTextFormatter txtFmt;
   private static final String DOTDOT = "..";
   private static final String NAME_KEY = "Name";
   private static final String SLASH = "/";
   private static final String BACKSLASH = "\\";
   private static final String EMPTY_STRING = "";
   int slipCdCount = 0;

   public BrowseHandler(WLScriptContext ctx) {
      this.ctx = ctx;
      this.txtFmt = ctx.getWLSTMsgFormatter();
   }

   void pop() throws ScriptException {
      try {
         WLScriptContext var10001 = this.ctx;
         if (this.ctx.domainType.equals("JNDI")) {
            this.handleJndiPop();
            return;
         }

         var10001 = this.ctx;
         if (this.ctx.domainType.equals("DomainRuntime") && !this.ctx.isAdminServer) {
            if (this.ctx.prompts.size() == 2) {
               return;
            }
         } else if (this.ctx.prompts.size() == 0 || this.ctx.beans.size() == 0) {
            return;
         }

         var10001 = this.ctx;
         if (this.ctx.domainType.equals("Custom_Domain")) {
            this.handleCustomDomainPop();
            return;
         }

         var10001 = this.ctx;
         if (this.ctx.domainType.equals("DomainCustom_Domain")) {
            this.handleDomainCustomDomainPop();
            return;
         }

         if (this.ctx.inNewTree()) {
            if (this.delegateToDomainRuntimeHandler("..")) {
               this.ctx.domainRuntimeHandler.pop();
               return;
            }

            if (this.delegateToServerRuntimeHandler("..")) {
               this.ctx.serverRuntimeHandler.pop();
               return;
            }

            this.ctx.newBrowseHandler.pop();
            return;
         }

         if (!this.ctx.inMBeanType && !this.ctx.inMBeanTypes) {
            if (this.ctx.atBeanLevel) {
               this.popAndPeek();
               String prevName = (String)this.ctx.prompts.peek();
               Object obj = this.ctx.mbs.getAttribute(this.ctx.getObjectName(this.ctx.beans.peek()), prevName);
               if (obj instanceof ObjectName || obj instanceof ObjectName[]) {
                  if (obj instanceof ObjectName) {
                     this.ctx.wlInstanceObjName = (ObjectName)obj;
                     this.changeToMBeanTypeLevel();
                     this.populateNames(obj);
                  } else {
                     this.ctx.wlInstanceObjNames = (ObjectName[])((ObjectName[])obj);
                     this.changeToMBeanTypesLevel();
                     this.populateNames(obj);
                  }

                  this.ctx.prompt = this.ctx.evaluatePrompt();
               }
            }
         } else {
            this.changeToBeanLevel();
            this.popAndPeek();
         }

         this.ctx.prompt = this.ctx.evaluatePrompt();
      } catch (Throwable var3) {
         this.ctx.throwWLSTException(this.txtFmt.getErrorCdingToBean(), var3);
      }

   }

   private void splitPush(String[] strs, String delimiter) throws ScriptException {
      String curPrompt = this.ctx.prompt;

      for(int i = 0; i < strs.length; ++i) {
         try {
            if (this.slipCdCount > 0) {
               for(int j = 0; j < this.slipCdCount; ++j) {
                  ++i;
               }

               this.slipCdCount = 0;
            }

            if (i >= strs.length) {
               this.slipCdCount = 0;
            } else if (incrementCdCount) {
               incrementCdCount = false;
            } else {
               String restCdArgument = strs[i];
               if (i < strs.length - 1) {
                  nextCdValue = strs[i + 1];

                  for(int j = i + 1; j < strs.length; ++j) {
                     restCdArgument = restCdArgument + delimiter + strs[j];
                  }
               }

               this.regularPush(strs[i], restCdArgument);
               nextCdValue = "";
            }
         } catch (Throwable var7) {
            this.resetCD(curPrompt);
            throw new ScriptException(this.txtFmt.getErrorCdingToBean(), var7, "cd");
         }
      }

   }

   private void regularPush(String mname, String restCdArgument) throws ScriptException {
      if (!this.cdDone) {
         String currentPrompt = this.ctx.prompt;

         try {
            WLScriptContext var10001;
            if (mname.equals("..")) {
               var10001 = this.ctx;
               if (this.ctx.domainType.equals("JNDI")) {
                  this.handleJndiPop();
               } else {
                  this.pop();
               }

               return;
            }

            String tree = this.ctx.getTreeFromArgument(mname + "/");
            if (tree != null) {
               this.jumpTree(tree);
               this.takeBackToRoot();
               return;
            }

            var10001 = this.ctx;
            if (this.ctx.domainType.equals("JNDI")) {
               this.handleJndiCd(mname);
               return;
            }

            var10001 = this.ctx;
            if (this.ctx.domainType.equals("Custom_Domain")) {
               this.cdToCustomBeanInstance(mname, restCdArgument);
               return;
            }

            var10001 = this.ctx;
            if (this.ctx.domainType.equals("DomainCustom_Domain")) {
               this.cdToDomainCustomBeanInstance(mname, restCdArgument);
               return;
            }

            boolean fnd;
            if (this.ctx.inMBeanType) {
               fnd = false;
               String name;
               if (this.ctx.wlInstanceObjName == null) {
                  this.ctx.errorMsg = this.txtFmt.getAttributeNotFound(mname);
                  this.ctx.errorInfo = new ErrorInformation(this.ctx.errorMsg);
                  this.ctx.exceptionHandler.handleException(this.ctx.errorInfo);
               } else if (!this.ctx.wlInstanceObjName.getKeyProperty("Name").equals(mname) && !this.ctx.wlInstanceObjName_name.equals(mname)) {
                  if (this.ctx.wlInstanceObjName.getKeyProperty("Name").indexOf("/") != -1 || this.ctx.wlInstanceObjName.getKeyProperty("Name").indexOf("\\") != -1 || this.ctx.wlInstanceObjName_name.indexOf("/") != -1 || this.ctx.wlInstanceObjName_name.indexOf("\\") != -1) {
                     this.ctx.printDebug(this.txtFmt.getMBeanHasSlash());
                     name = this.ctx.wlInstanceObjName.getKeyProperty("Name");
                     if (name.equals(this.cdArgument) || this.ctx.wlInstanceObjName_name.equals(this.cdArgument)) {
                        this.ctx.prompts.add(this.ctx.wlInstanceObjName_name);
                        this.ctx.prompt = this.ctx.evaluatePrompt();
                        this.changeToBeanLevel();
                        this.ctx.wlcmo = this.ctx.getMBeanFromObjectName(this.ctx.wlInstanceObjName);
                        this.ctx.beans.push(this.ctx.wlcmo);
                        this.cdDone = true;
                        fnd = true;
                     }
                  }
               } else {
                  this.ctx.prompts.add(mname);
                  this.ctx.prompt = this.ctx.evaluatePrompt();
                  this.changeToBeanLevel();
                  this.ctx.wlcmo = this.ctx.getMBeanFromObjectName(this.ctx.wlInstanceObjName);
                  this.ctx.beans.push(this.ctx.wlcmo);
                  fnd = true;
               }

               if (!fnd && (this.ctx.wlInstanceObjName.getKeyProperty("Name").indexOf("/") != -1 || this.ctx.wlInstanceObjName.getKeyProperty("Name").indexOf("\\") != -1 || this.ctx.wlInstanceObjName_name.indexOf("/") != -1 || this.ctx.wlInstanceObjName_name.indexOf("\\") != -1)) {
                  this.ctx.printDebug(this.txtFmt.getMBeanHasSlash());
                  name = this.ctx.wlInstanceObjName.getKeyProperty("Name");
                  if (this.ctx.wlInstanceObjName_name.equals(mname + "/" + nextCdValue) || restCdArgument != null && restCdArgument.indexOf(this.ctx.wlInstanceObjName_name) != -1) {
                     this.ctx.prompts.add(this.ctx.wlInstanceObjName_name);
                     this.ctx.prompt = this.ctx.evaluatePrompt();
                     this.changeToBeanLevel();
                     this.ctx.wlcmo = this.ctx.getMBeanFromObjectName(this.ctx.wlInstanceObjName);
                     this.ctx.beans.push(this.ctx.wlcmo);
                     fnd = true;
                     this.slipCdCount = this.determineSlipCount(this.ctx.wlInstanceObjName_name);
                     if (this.slipCdCount != 0 && this.slipCdCount != 1) {
                        --this.slipCdCount;
                     } else {
                        this.slipCdCount = 0;
                     }
                  }
               }

               if (!fnd) {
                  this.ctx.throwWLSTException(this.txtFmt.getAttributeNotFound(mname));
               }
            } else if (!this.ctx.inMBeanTypes) {
               if (this.ctx.atBeanLevel) {
                  Object obj = null;
                  if (this.ctx.inNewTree()) {
                     if (this.delegateToDomainRuntimeHandler(mname)) {
                        this.ctx.domainRuntimeHandler.cd(mname);
                        return;
                     }

                     if (this.delegateToServerRuntimeHandler(mname)) {
                        this.ctx.serverRuntimeHandler.cd(mname);
                        return;
                     }

                     obj = this.ctx.getMBSConnection(this.ctx.domainType).getAttribute(this.ctx.getObjectName(), mname);
                  } else {
                     obj = this.ctx.mbs.getAttribute(this.ctx.getObjectName(), mname);
                  }

                  if (obj != null && !(obj instanceof ObjectName) && !(obj instanceof ObjectName[])) {
                     this.ctx.throwWLSTException(this.txtFmt.getCannotCDToAttribute(mname));
                  } else {
                     if (obj instanceof ObjectName) {
                        if (this.ctx.skipSingletons) {
                           this.ctx.prompts.add(mname);
                           this.ctx.prompt = this.ctx.evaluatePrompt();
                           this.changeToBeanLevel();
                           this.ctx.wlcmo = this.ctx.getMBeanFromObjectName((ObjectName)obj);
                           this.ctx.beans.push(this.ctx.wlcmo);
                           return;
                        }

                        this.ctx.wlInstanceObjName = (ObjectName)obj;
                        this.changeToMBeanTypeLevel();
                        this.populateNames(obj);
                     } else if (obj instanceof ObjectName[]) {
                        this.ctx.wlInstanceObjNames = (ObjectName[])((ObjectName[])obj);
                        this.changeToMBeanTypesLevel();
                        this.populateNames(obj);
                     } else if (this.ctx.isPlural(mname)) {
                        this.ctx.wlInstanceObjNames = (ObjectName[])((ObjectName[])obj);
                        this.changeToMBeanTypesLevel();
                     } else {
                        this.ctx.wlInstanceObjName = (ObjectName)obj;
                        this.changeToMBeanTypeLevel();
                     }

                     this.ctx.prompts.add(mname);
                     this.ctx.beans.push(this.ctx.wlcmo);
                     this.ctx.prompt = this.ctx.evaluatePrompt();
                  }
               }
            } else {
               if (this.delegateToDomainRuntimeHandler(mname)) {
                  this.ctx.domainRuntimeHandler.cd(mname);
                  return;
               }

               if (this.ctx.wlInstanceObjNames == null) {
                  this.ctx.throwWLSTException(this.txtFmt.getAttributeNotFound(mname));
               }

               fnd = false;

               int k;
               ObjectName objName;
               for(k = 0; k < this.ctx.wlInstanceObjNames.length; ++k) {
                  if (this.ctx.wlInstanceObjNames[k] != null) {
                     objName = this.ctx.wlInstanceObjNames[k];
                     if (objName.getKeyProperty("Name").equals(this.cdArgument) || this.ctx.wlInstanceObjNames_names[k].equals(this.cdArgument)) {
                        this.ctx.prompts.add(this.cdArgument);
                        this.ctx.prompt = this.ctx.evaluatePrompt();
                        this.changeToBeanLevel();
                        this.ctx.wlcmo = this.ctx.getMBeanFromObjectName(objName);
                        this.ctx.beans.push(this.ctx.wlcmo);
                        fnd = true;
                        this.cdDone = true;
                        break;
                     }
                  }
               }

               if (!fnd) {
                  for(k = 0; k < this.ctx.wlInstanceObjNames.length; ++k) {
                     objName = this.ctx.wlInstanceObjNames[k];
                     if (this.ctx.wlInstanceObjNames[k] != null && (objName.getKeyProperty("Name").equals(mname) || this.ctx.wlInstanceObjNames_names[k].equals(mname))) {
                        for(int k = 0; k < this.ctx.wlInstanceObjNames.length; ++k) {
                           ObjectName _oname = this.ctx.wlInstanceObjNames[k];
                           if (this.ctx.wlInstanceObjNames[k] != null && (_oname.getKeyProperty("Name").equals(mname + "/" + nextCdValue) || this.ctx.wlInstanceObjNames_names[k].equals(mname + "/" + nextCdValue))) {
                              this.ctx.prompts.add(mname + "/" + nextCdValue);
                              this.ctx.prompt = this.ctx.evaluatePrompt();
                              this.changeToBeanLevel();
                              this.ctx.wlcmo = this.ctx.getMBeanFromObjectName(_oname);
                              this.ctx.beans.push(this.ctx.wlcmo);
                              fnd = true;
                              incrementCdCount = true;
                              break;
                           }
                        }

                        if (!fnd) {
                           this.ctx.prompts.add(mname);
                           this.ctx.prompt = this.ctx.evaluatePrompt();
                           this.changeToBeanLevel();
                           this.ctx.wlcmo = this.ctx.getMBeanFromObjectName(objName);
                           this.ctx.beans.push(this.ctx.wlcmo);
                           fnd = true;
                           break;
                        }
                     }
                  }
               }

               String oname;
               String _oname;
               if (!fnd) {
                  for(k = 0; k < this.ctx.wlInstanceObjNames.length; ++k) {
                     objName = this.ctx.wlInstanceObjNames[k];
                     oname = objName.getKeyProperty("Name");
                     _oname = this.ctx.wlInstanceObjNames_names[k];
                     if ((oname.indexOf("/") != -1 || oname.indexOf("\\") != -1 || _oname.indexOf("/") != -1 || _oname.indexOf("\\") != -1) && (oname.equals(this.cdArgument) || _oname.equals(this.cdArgument))) {
                        this.ctx.prompts.add(_oname);
                        this.ctx.prompt = this.ctx.evaluatePrompt();
                        this.changeToBeanLevel();
                        this.ctx.wlcmo = this.ctx.getMBeanFromObjectName(objName);
                        this.ctx.beans.push(this.ctx.wlcmo);
                        fnd = true;
                        this.cdDone = true;
                        break;
                     }
                  }
               }

               if (!fnd) {
                  for(k = 0; k < this.ctx.wlInstanceObjNames.length; ++k) {
                     objName = this.ctx.wlInstanceObjNames[k];
                     oname = objName.getKeyProperty("Name");
                     _oname = this.ctx.wlInstanceObjNames_names[k];
                     if ((oname.indexOf("/") != -1 || oname.indexOf("\\") != -1 || _oname.indexOf("/") != -1 || _oname.indexOf("\\") != -1) && (oname.indexOf(mname) != -1 || _oname.indexOf(mname) != -1) && (this.cdArgument.indexOf(oname) != -1 || this.cdArgument.indexOf(_oname) != -1)) {
                        this.ctx.prompts.add(_oname);
                        this.ctx.prompt = this.ctx.evaluatePrompt();
                        this.changeToBeanLevel();
                        this.ctx.wlcmo = this.ctx.getMBeanFromObjectName(objName);
                        this.ctx.beans.push(this.ctx.wlcmo);
                        fnd = true;
                        this.slipCdCount = this.determineSlipCount(_oname);
                        if (this.slipCdCount != 0 && this.slipCdCount != 1) {
                           --this.slipCdCount;
                        } else {
                           this.slipCdCount = 0;
                        }
                        break;
                     }
                  }
               }

               if (!fnd) {
                  this.ctx.throwWLSTException(this.txtFmt.getAttributeNotFound(mname));
               }
            }
         } catch (MBeanException var10) {
            if (var10.getTargetException() instanceof AttributeNotFoundException) {
               this.resetCD(currentPrompt);
               this.ctx.throwWLSTException(this.txtFmt.getAttributeNotFound(mname), var10.getTargetException());
            } else {
               this.resetCD(currentPrompt);
               this.ctx.throwWLSTException(this.txtFmt.getMBeanExceptionOccurred(), var10);
            }
         } catch (Throwable var11) {
            this.resetCD(currentPrompt);
            if (var11 instanceof ScriptException) {
               throw (ScriptException)var11;
            }

            this.ctx.throwWLSTException(this.txtFmt.getErrorBrowsingBeans(), var11);
         }

      }
   }

   private synchronized void resetCD(String curPrompt) throws ScriptException {
      if (!this.currentlyResetting) {
         try {
            this.currentlyResetting = true;
            this.takeBackToRoot();
            this.cd(curPrompt);
         } finally {
            this.currentlyResetting = false;
         }

      }
   }

   void jumpTree(String tree) throws ScriptException {
      if (!this.ctx.getCurrentTree().equals(tree)) {
         WLScriptContext var10001 = this.ctx;
         if (tree.equals("config")) {
            this.cdToConfig();
         }

         var10001 = this.ctx;
         if (tree.equals("runtime")) {
            this.cdToRuntime();
         }

         var10001 = this.ctx;
         if (tree.equals("adminConfig")) {
            this.adminConfig();
         }

         var10001 = this.ctx;
         if (tree.equals("custom")) {
            this.custom((String)null);
         }

         var10001 = this.ctx;
         if (tree.equals("domainCustom")) {
            this.domainCustom((String)null);
         }

         var10001 = this.ctx;
         if (tree.equals("jndi")) {
            this.jndi((String)null);
         }

         var10001 = this.ctx;
         if (tree.equals("serverConfig")) {
            this.ctx.newBrowseHandler.configRuntime();
         }

         var10001 = this.ctx;
         if (tree.equals("serverRuntime")) {
            this.ctx.newBrowseHandler.runtimeRuntime();
         }

         var10001 = this.ctx;
         if (tree.equals("domainConfig")) {
            this.ctx.newBrowseHandler.configDomainRuntime();
         }

         var10001 = this.ctx;
         if (tree.equals("domainRuntime")) {
            this.ctx.newBrowseHandler.runtimeDomainRuntime();
         }

         var10001 = this.ctx;
         if (tree.equals("edit")) {
            this.ctx.newBrowseHandler.configEdit();
         }

      }
   }

   void custom(String pattern) throws ScriptException {
      try {
         WLScriptContext var10001 = this.ctx;
         if (!this.ctx.domainType.equals("Custom_Domain")) {
            this.ctx.println(this.txtFmt.getLocationChangedToCustomTree());
            this.ctx.println(this.txtFmt.getUseCustomHelp());
            this.ctx.newBrowseHandler.saveLastPlaceInPreviousTree();
            this.ctx.wlcmo = this.txtFmt.getNoStubAvailable();
            var10001 = this.ctx;
            this.ctx.domainType = "Custom_Domain";
            this.initCommonVariables();
            this.ctx.atDomainLevel = true;
            this.ctx.customMBeanDomainObjNameMap = new TreeMap();
            this.ctx.getCustomMBeans(pattern);
            this.ctx.newBrowseHandler.goToLastPlaceInCurrentTree();
            this.ctx.mbs = this.ctx.getMBSConnection((String)null);
         } else {
            this.ctx.println(this.txtFmt.getAlreadyInTree("custom"));
         }
      } catch (Throwable var3) {
         if (var3 instanceof ScriptException) {
            throw (ScriptException)var3;
         }

         this.ctx.throwWLSTException(this.txtFmt.getErrorBrowsingTree("custom"), var3);
      }

   }

   void domainCustom(String pattern) throws ScriptException {
      if (!this.ctx.isAdminServer) {
         this.ctx.println(this.txtFmt.getDomainCustomCommandNotOnMS());
      } else if (!this.ctx.isDomainRuntimeServerEnabled) {
         this.ctx.println(this.txtFmt.getDomainRuntimeServerNotEnabled());
      } else {
         try {
            WLScriptContext var10001 = this.ctx;
            if (!this.ctx.domainType.equals("DomainCustom_Domain")) {
               this.ctx.println(this.txtFmt.getLocationChangedToDomainCustomTree());
               this.ctx.println(this.txtFmt.getUseDomainCustomHelp());
               this.ctx.newBrowseHandler.saveLastPlaceInPreviousTree();
               this.ctx.wlcmo = this.txtFmt.getNoStubAvailable();
               var10001 = this.ctx;
               this.ctx.domainType = "DomainCustom_Domain";
               this.initCommonVariables();
               this.ctx.atDomainLevel = true;
               this.ctx.domainCustomMBeanDomainObjNameMap = new TreeMap();
               this.ctx.getDomainCustomMBeans(pattern);
               this.ctx.newBrowseHandler.goToLastPlaceInCurrentTree();
               this.ctx.mbs = this.ctx.getMBSConnection((String)null);
            } else {
               this.ctx.println(this.txtFmt.getAlreadyInTree("domainCustom"));
            }
         } catch (Throwable var3) {
            if (var3 instanceof ScriptException) {
               throw (ScriptException)var3;
            }

            this.ctx.throwWLSTException(this.txtFmt.getErrorBrowsingTree("domainCustom"), var3);
         }

      }
   }

   void takeBackToRoot() throws ScriptException {
      if (this.ctx.debug) {
         this.ctx.printDebug(this.txtFmt.getBrowsingBackToRoot());
      }

      this.reset();
   }

   private void handleJndiCd(String mname) throws ScriptException {
      try {
         if (this.ctx.prompts.size() == 0) {
            ServerRuntimeMBean[] beans = this.ctx.domainRuntimeServiceMBean.getServerRuntimes();
            int i = 0;

            while(true) {
               if (i >= beans.length) {
                  this.ctx.throwWLSTException(this.txtFmt.getCannotFindJndiEntry(mname));
                  break;
               }

               if (beans[i].getName().equals(mname)) {
                  this.ctx.prompts.add(mname);
                  this.ctx.prompt = this.ctx.evaluatePrompt();
                  return;
               }

               ++i;
            }
         }

         if (this.ctx.jndiNames.size() == 0) {
            this.ctx.infoHandler.handleJNDIls(false, "c");
         }

         if (this.ctx.jndiNames.size() == 0) {
            return;
         }

         Iterator iter = this.ctx.jndiNames.iterator();

         while(iter.hasNext()) {
            String name = (String)iter.next();
            if (name.equals(mname)) {
               this.ctx.prompts.add(mname);
               this.ctx.prompt = this.ctx.evaluatePrompt();
               this.ctx.jndiNames = new ArrayList();
               return;
            }
         }

         this.ctx.jndiNames = new ArrayList();
      } catch (Throwable var4) {
         this.ctx.throwWLSTException(this.txtFmt.getErrorBrowsingTree("jndi"), var4);
      }

   }

   void cd(String mname) throws ScriptException {
      this.cdArgument = mname;
      int slipCdCount = false;
      this.cdDone = false;
      if (mname.startsWith("/")) {
         if (this.checkNameStartsWithSlash(mname)) {
            this.ctx.nameHasSlash = false;
            return;
         }

         String currentPrompt = this.ctx.prompt;

         try {
            this.takeBackToRoot();
            mname = mname.substring(1, mname.length());
            this.cd(mname);
         } catch (ScriptException var5) {
            this.resetCD(currentPrompt);
            throw var5;
         }
      } else {
         String[] strs;
         if (mname.indexOf("\\") != -1) {
            strs = StringUtils.splitCompletely(mname, "\\");
            if (strs.length == 0) {
               this.takeBackToRoot();
            }

            this.splitPush(strs, "\\");
         } else if (mname.indexOf("/") != -1) {
            strs = StringUtils.splitCompletely(mname, "/");
            if (strs.length == 0) {
               this.takeBackToRoot();
            }

            this.splitPush(strs, "/");
         } else {
            if (mname.length() == 0) {
               return;
            }

            this.regularPush(mname, (String)null);
         }
      }

   }

   void cdToConfig() throws ScriptException {
      if (!this.ctx.isCompatabilityServerEnabled) {
         this.ctx.println(this.txtFmt.getCannotChangeToConfigTree());
      } else {
         try {
            WLScriptContext var10001 = this.ctx;
            if (!this.ctx.domainType.equals("DomainConfig")) {
               if (this.ctx.isAdminServer) {
                  this.ctx.newBrowseHandler.saveLastPlaceInPreviousTree();
                  var10001 = this.ctx;
                  this.ctx.domainType = "Domain";
                  this.ctx.wlcmo = this.ctx.home.getAdminMBean(this.ctx.domainName, "Domain");
                  if (!this.configNavigatedBefore) {
                     this.ctx.addCompatChangeListener();
                     this.ctx.println(this.txtFmt.getLocationChangedToConfigTree("Domain"));
                     this.configNavigatedBefore = true;
                  }
               } else {
                  this.ctx.newBrowseHandler.saveLastPlaceInPreviousTree();
                  var10001 = this.ctx;
                  this.ctx.domainType = "DomainConfig";
                  this.ctx.wlcmo = this.ctx.home.getConfigurationMBean(this.ctx.domainName, "DomainConfig");
                  if (!this.configNavigatedBefore) {
                     this.ctx.println(this.txtFmt.getLocationChangedToConfigTree("DomainConfig"));
                     this.configNavigatedBefore = true;
                  }
               }

               this.initCommonVariables();
               this.ctx.newBrowseHandler.goToLastPlaceInCurrentTree();
               this.ctx.mbs = this.ctx.getMBSConnection((String)null);
            } else {
               this.ctx.print(this.txtFmt.getAlreadyInTree("domainConfig"));
            }
         } catch (Throwable var2) {
            if (var2 instanceof ScriptException) {
               throw (ScriptException)var2;
            }

            this.ctx.throwWLSTException(this.txtFmt.getErrorBrowsingTree("config"), var2);
         }

      }
   }

   void reset() throws ScriptException {
      this.ctx.lastPlaceInConfigRuntime = "";
      this.ctx.lastPlaceInRuntimeRuntime = "";
      this.ctx.lastPlaceInConfigDomainRuntime = "";
      this.ctx.lastPlaceInRuntimeDomainRuntime = "";
      this.ctx.lastPlaceInJNDI = "";
      this.ctx.lastPlaceInCustom = "";
      this.ctx.lastPlaceInDomainCustom = "";
      this.ctx.lastPlaceInEdit = "";
      this.ctx.lastPlaceInJSR77 = "";

      WLScriptContext var10001;
      try {
         var10001 = this.ctx;
         if (this.ctx.domainType.equals("DomainRuntime")) {
            if (this.ctx.isAdminServer) {
               this.ctx.wlcmo = this.ctx.home.getRuntimeMBean(this.ctx.domainName, "DomainRuntime");
               var10001 = this.ctx;
               this.ctx.domainType = "DomainRuntime";
               this.initCommonVariables();
            } else {
               this.ctx.wlcmo = this.ctx.home.getRuntimeMBean(this.ctx.serverName, "ServerRuntime");
               this.ctx.prompts = new Stack();
               this.ctx.prompts.add("ServerRuntime");
               this.ctx.prompts.add(this.ctx.serverName);
               this.ctx.beans = new Stack();
               this.ctx.beans.add(this.ctx.wlcmo);
               this.ctx.prompt = this.ctx.evaluatePrompt();
               var10001 = this.ctx;
               this.ctx.domainType = "DomainRuntime";
               this.changeToBeanLevel();
            }
         } else {
            var10001 = this.ctx;
            if (this.ctx.domainType.equals("Domain")) {
               this.ctx.wlcmo = this.ctx.home.getAdminMBean(this.ctx.domainName, "Domain");
               var10001 = this.ctx;
               this.ctx.domainType = "Domain";
               this.initCommonVariables();
            } else {
               var10001 = this.ctx;
               if (this.ctx.domainType.equals("Custom_Domain")) {
                  this.ctx.wlcmo = this.txtFmt.getNoStubAvailable();
                  this.initCommonVariables();
                  this.ctx.atDomainLevel = true;
               } else {
                  var10001 = this.ctx;
                  if (this.ctx.domainType.equals("DomainCustom_Domain")) {
                     this.ctx.wlcmo = this.txtFmt.getNoStubAvailable();
                     this.initCommonVariables();
                     this.ctx.atDomainLevel = true;
                  } else if (this.ctx.inNewTree()) {
                     var10001 = this.ctx;
                     if (this.ctx.domainType.equals("RuntimeConfigServerDomain")) {
                        this.ctx.wlcmo = this.ctx.runtimeDomainMBean;
                        var10001 = this.ctx;
                        this.ctx.domainType = "RuntimeConfigServerDomain";
                        this.initCommonVariables();
                     } else {
                        var10001 = this.ctx;
                        if (this.ctx.domainType.equals("RuntimeRuntimeServerDomain")) {
                           this.ctx.wlcmo = this.ctx.runtimeServerRuntimeMBean;
                           var10001 = this.ctx;
                           this.ctx.domainType = "RuntimeRuntimeServerDomain";
                           this.initCommonVariables();
                        } else {
                           var10001 = this.ctx;
                           if (this.ctx.domainType.equals("RuntimeDomainRuntime")) {
                              this.ctx.wlcmo = this.ctx.runtimeDomainRuntimeDRMBean;
                              var10001 = this.ctx;
                              this.ctx.domainType = "RuntimeDomainRuntime";
                              this.initCommonVariables();
                           } else {
                              var10001 = this.ctx;
                              if (this.ctx.domainType.equals("ConfigDomainRuntime")) {
                                 this.ctx.wlcmo = this.ctx.configDomainRuntimeDRMBean;
                                 var10001 = this.ctx;
                                 this.ctx.domainType = "ConfigDomainRuntime";
                                 this.initCommonVariables();
                              } else {
                                 var10001 = this.ctx;
                                 if (this.ctx.domainType.equals("JNDI")) {
                                    this.ctx.wlcmo = this.txtFmt.getNoStubAvailable();
                                    var10001 = this.ctx;
                                    this.ctx.domainType = "JNDI";
                                    this.initCommonVariables();
                                 } else {
                                    var10001 = this.ctx;
                                    if (this.ctx.domainType.equals("ConfigEdit")) {
                                       this.ctx.wlcmo = this.ctx.editDomainMBean;
                                       var10001 = this.ctx;
                                       this.ctx.domainType = "ConfigEdit";
                                       this.initCommonVariables();
                                    } else {
                                       var10001 = this.ctx;
                                       if (this.ctx.domainType.equals("JSR77")) {
                                          this.ctx.wlcmo = null;
                                          var10001 = this.ctx;
                                          this.ctx.domainType = "ConfigEdit";
                                          this.initCommonVariables();
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      } catch (InstanceNotFoundException var2) {
         this.ctx.throwWLSTException(this.txtFmt.getCouldNotFindDomainRuntimeMBean(), var2);
      } catch (Throwable var3) {
         var10001 = this.ctx;
         this.ctx.throwWLSTException("Unexpected Error: ", var3);
      }

   }

   void cdToRuntime() throws ScriptException {
      if (!this.ctx.isCompatabilityServerEnabled) {
         this.ctx.println(this.txtFmt.getCannotChangeToRuntimeTree());
      } else {
         WLScriptContext var10001;
         try {
            var10001 = this.ctx;
            if (!this.ctx.domainType.equals("DomainRuntime")) {
               if (!this.runtimeNavigatedBefore) {
                  this.ctx.addCompatChangeListener();
                  this.ctx.println(this.txtFmt.getLocationChangedToRuntimeTree("DomainRuntime"));
               }

               this.runtimeNavigatedBefore = true;
               this.ctx.newBrowseHandler.saveLastPlaceInPreviousTree();
               var10001 = this.ctx;
               this.ctx.domainType = "DomainRuntime";
               this.ctx.mbs = this.ctx.getMBSConnection((String)null);
               if (this.ctx.isAdminServer) {
                  this.ctx.wlcmo = this.ctx.home.getRuntimeMBean(this.ctx.domainName, "DomainRuntime");
                  this.initCommonVariables();
                  this.ctx.newBrowseHandler.goToLastPlaceInCurrentTree();
               } else {
                  if (!this.runtimeNavigatedBefore) {
                     this.ctx.println(this.txtFmt.getLocationChangedToRuntimeTree("ServerRuntime"));
                  }

                  this.runtimeNavigatedBefore = true;
                  this.ctx.wlcmo = this.ctx.home.getRuntimeMBean(this.ctx.serverName, "ServerRuntime");
                  this.ctx.prompts = new Stack();
                  this.ctx.prompts.add("ServerRuntime");
                  this.ctx.prompts.add(this.ctx.serverName);
                  this.ctx.beans = new Stack();
                  this.ctx.beans.add(this.ctx.wlcmo);
                  this.ctx.prompt = this.ctx.evaluatePrompt();
                  this.changeToBeanLevel();
                  this.ctx.newBrowseHandler.goToLastPlaceInCurrentTree();
               }
            } else {
               this.ctx.println(this.txtFmt.getAlreadyInTree("runtime"));
            }
         } catch (InstanceNotFoundException var2) {
            this.ctx.throwWLSTException("Could not find the RuntimeMBean instance", var2);
         } catch (Throwable var3) {
            if (var3 instanceof ScriptException) {
               throw (ScriptException)var3;
            }

            var10001 = this.ctx;
            this.ctx.throwWLSTException("Unexpected Error: ", var3);
         }

      }
   }

   private void cdToCustomBeanInstance(String mname, String restCdArgument) throws ScriptException {
      if (this.ctx.inMBeanType) {
         String inDomain = (String)this.ctx.prompts.peek();
         List oNames = (List)this.ctx.customMBeanDomainObjNameMap.get(inDomain);
         Iterator itt = oNames.iterator();

         while(itt.hasNext()) {
            String on = (String)itt.next();
            if (on.equals(mname) || on.equals(restCdArgument)) {
               if (!on.equals(mname)) {
                  this.ctx.prompts.add(restCdArgument);
                  this.slipCdCount = this.determineSlipCount(on);
                  if (this.slipCdCount != 0 && this.slipCdCount != 1) {
                     --this.slipCdCount;
                  } else {
                     this.slipCdCount = 0;
                  }
               } else {
                  this.ctx.prompts.add(mname);
               }

               this.ctx.prompt = this.ctx.evaluatePrompt();
               this.changeToBeanLevel();
               this.ctx.atDomainLevel = false;

               try {
                  this.ctx.wlcmo = this.ctx.getMBeanFromObjectName(new ObjectName(on));
               } catch (Throwable var8) {
                  this.ctx.wlcmo = this.txtFmt.getNoStubAvailable();
               }

               this.ctx.beans.push(this.ctx.wlcmo);
               return;
            }
         }
      }

      if (this.ctx.atDomainLevel) {
         new TreeMap();
         Iterator iter = this.ctx.customMBeanDomainObjNameMap.keySet().iterator();

         while(iter.hasNext()) {
            String dn = (String)iter.next();
            if (dn.equals(mname)) {
               this.ctx.prompts.add(mname);
               this.ctx.prompt = this.ctx.evaluatePrompt();
               this.ctx.atDomainLevel = false;
               this.changeToMBeanTypeLevel();
               this.ctx.wlcmo = this.txtFmt.getNoStubAvailable();
               this.ctx.beans.push(this.ctx.wlcmo);
               return;
            }
         }
      }

      this.ctx.throwWLSTException(this.txtFmt.getAttributeNotFound(mname));
   }

   private void cdToDomainCustomBeanInstance(String mname, String restCdArgument) throws ScriptException {
      if (this.ctx.inMBeanType) {
         String inDomain = (String)this.ctx.prompts.peek();
         List oNames = (List)this.ctx.domainCustomMBeanDomainObjNameMap.get(inDomain);
         Iterator itt = oNames.iterator();

         while(itt.hasNext()) {
            String on = (String)itt.next();
            if (on.equals(mname) || on.equals(restCdArgument)) {
               if (!on.equals(mname)) {
                  this.ctx.prompts.add(restCdArgument);
                  this.slipCdCount = this.determineSlipCount(on);
                  if (this.slipCdCount != 0 && this.slipCdCount != 1) {
                     --this.slipCdCount;
                  } else {
                     this.slipCdCount = 0;
                  }
               } else {
                  this.ctx.prompts.add(mname);
               }

               this.ctx.prompt = this.ctx.evaluatePrompt();
               this.changeToBeanLevel();
               this.ctx.atDomainLevel = false;

               try {
                  this.ctx.wlcmo = this.ctx.getMBeanFromObjectName(new ObjectName(on));
               } catch (Throwable var8) {
                  this.ctx.wlcmo = this.txtFmt.getNoStubAvailable();
               }

               this.ctx.beans.push(this.ctx.wlcmo);
               return;
            }
         }
      }

      if (this.ctx.atDomainLevel) {
         new TreeMap();
         Iterator iter = this.ctx.domainCustomMBeanDomainObjNameMap.keySet().iterator();

         while(iter.hasNext()) {
            String dn = (String)iter.next();
            if (dn.equals(mname)) {
               this.ctx.prompts.add(mname);
               this.ctx.prompt = this.ctx.evaluatePrompt();
               this.ctx.atDomainLevel = false;
               this.changeToMBeanTypeLevel();
               this.ctx.wlcmo = this.txtFmt.getNoStubAvailable();
               this.ctx.beans.push(this.ctx.wlcmo);
               return;
            }
         }
      }

      this.ctx.throwWLSTException(this.txtFmt.getAttributeNotFound(mname));
   }

   void jndi(String serverName) throws ScriptException {
      if (serverName != null && !serverName.equals(this.ctx.serverName)) {
         if (!this.ctx.isAdminServer && !serverName.equals(this.ctx.serverName)) {
            this.ctx.println(this.txtFmt.getCannotBrowsJNDIOfOtherServer());
            return;
         }
      } else {
         try {
            WLScriptContext var10001 = this.ctx;
            if (!this.ctx.domainType.equals("JNDI")) {
               this.ctx.println(this.txtFmt.getLocationChangedToJndiTree());
               this.ctx.newBrowseHandler.saveLastPlaceInPreviousTree();
               this.ctx.wlcmo = this.txtFmt.getNoStubAvailable();
               var10001 = this.ctx;
               this.ctx.domainType = "JNDI";
               this.initCommonVariables();
               this.ctx.newBrowseHandler.goToLastPlaceInCurrentTree();
            } else {
               this.ctx.println(this.txtFmt.getAlreadyInTree("jndi"));
               this.ctx.jndiNames = new ArrayList();
            }
         } catch (Throwable var3) {
            if (var3 instanceof ScriptException) {
               throw (ScriptException)var3;
            }

            this.ctx.throwWLSTException(this.txtFmt.getErrorBrowsingTree("jndi"), var3);
         }
      }

   }
}
