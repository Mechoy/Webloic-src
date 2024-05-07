package weblogic.management.scripting;

import java.util.Iterator;
import java.util.Stack;
import javax.management.AttributeNotFoundException;
import javax.management.ObjectName;
import weblogic.management.scripting.utils.WLSTMsgTextFormatter;

public class NewBrowseHandler {
   WLScriptContext ctx = null;
   private static WLSTMsgTextFormatter txtFmt;
   boolean runtimeRuntimeNavigatedBefore = false;
   boolean configRuntimeNavigatedBefore = false;
   boolean configDomainRuntimeNavigatedBefore = false;
   boolean runtimeDomainRuntimeNavigatedBefore = false;
   boolean jndiNavigatedBefore = false;
   boolean editNavigatedBefore = false;

   public NewBrowseHandler(WLScriptContext ctx) {
      this.ctx = ctx;
      txtFmt = ctx.getWLSTMsgFormatter();
   }

   public void configRuntime() throws ScriptException {
      if (!this.ctx.isRuntimeServerEnabled) {
         this.ctx.println(txtFmt.getConfigRuntimeServerNotEnabled());
      } else {
         try {
            WLScriptContext var10001 = this.ctx;
            if (!this.ctx.domainType.equals("RuntimeConfigServerDomain")) {
               if (!this.configRuntimeNavigatedBefore) {
                  this.ctx.println(txtFmt.getLocationChangedToConfigRuntime());
               }

               this.configRuntimeNavigatedBefore = true;
               this.saveLastPlaceInPreviousTree();
               this.ctx.wlcmo = this.ctx.runtimeDomainMBean;
               this.ctx.prompts = new Stack();
               this.ctx.beans = new Stack();
               this.ctx.beans.add(this.ctx.wlcmo);
               this.ctx.prompt = "";
               var10001 = this.ctx;
               this.ctx.domainType = "RuntimeConfigServerDomain";
               this.ctx.browseHandler.changeToBeanLevel();
               this.goToLastPlaceInCurrentTree();
               this.ctx.mbs = this.ctx.getMBSConnection((String)null);
            } else {
               this.ctx.println(txtFmt.getAlreadyInConfigRuntime());
            }
         } catch (Throwable var2) {
            if (var2 instanceof ScriptException) {
               throw (ScriptException)var2;
            }

            this.ctx.throwWLSTException(txtFmt.getErrorTraversingToConfigRuntime(), var2);
         }

      }
   }

   void goToLastPlaceInCurrentTree() throws ScriptException {
      WLScriptContext var10001 = this.ctx;
      if (this.ctx.domainType.equals("Domain") && this.ctx.lastPlaceInConfig.length() != 0) {
         this.ctx.browseHandler.cd(this.ctx.lastPlaceInConfig);
      } else {
         var10001 = this.ctx;
         if (this.ctx.domainType.equals("DomainConfig") && this.ctx.lastPlaceInAdminConfig.length() != 0) {
            this.ctx.browseHandler.cd(this.ctx.lastPlaceInAdminConfig);
         } else {
            var10001 = this.ctx;
            if (this.ctx.domainType.equals("DomainRuntime") && this.ctx.lastPlaceInRuntime.length() != 0) {
               this.ctx.browseHandler.cd(this.ctx.lastPlaceInRuntime);
            } else {
               var10001 = this.ctx;
               if (this.ctx.domainType.equals("RuntimeConfigServerDomain") && this.ctx.lastPlaceInConfigRuntime.length() != 0) {
                  this.ctx.browseHandler.cd(this.ctx.lastPlaceInConfigRuntime);
               } else {
                  var10001 = this.ctx;
                  if (this.ctx.domainType.equals("RuntimeRuntimeServerDomain") && this.ctx.lastPlaceInRuntimeRuntime.length() != 0) {
                     this.ctx.browseHandler.cd(this.ctx.lastPlaceInRuntimeRuntime);
                  } else {
                     var10001 = this.ctx;
                     if (this.ctx.domainType.equals("RuntimeDomainRuntime") && this.ctx.lastPlaceInRuntimeDomainRuntime.length() != 0) {
                        this.ctx.browseHandler.cd(this.ctx.lastPlaceInRuntimeDomainRuntime);
                     } else {
                        var10001 = this.ctx;
                        if (this.ctx.domainType.equals("ConfigDomainRuntime") && this.ctx.lastPlaceInConfigDomainRuntime.length() != 0) {
                           this.ctx.browseHandler.cd(this.ctx.lastPlaceInConfigDomainRuntime);
                        } else {
                           var10001 = this.ctx;
                           if (this.ctx.domainType.equals("JNDI") && this.ctx.lastPlaceInJNDI.length() != 0) {
                              this.ctx.browseHandler.cd(this.ctx.lastPlaceInJNDI);
                           } else {
                              var10001 = this.ctx;
                              if (this.ctx.domainType.equals("Custom_Domain") && this.ctx.lastPlaceInCustom.length() != 0) {
                                 this.ctx.browseHandler.cd(this.ctx.lastPlaceInCustom);
                              } else {
                                 var10001 = this.ctx;
                                 if (this.ctx.domainType.equals("DomainCustom_Domain") && this.ctx.lastPlaceInDomainCustom.length() != 0) {
                                    this.ctx.browseHandler.cd(this.ctx.lastPlaceInDomainCustom);
                                 } else {
                                    var10001 = this.ctx;
                                    if (this.ctx.domainType.equals("ConfigEdit") && this.ctx.lastPlaceInEdit.length() != 0) {
                                       this.ctx.browseHandler.cd(this.ctx.lastPlaceInEdit);
                                    } else {
                                       var10001 = this.ctx;
                                       if (this.ctx.domainType.equals("JSR77") && this.ctx.lastPlaceInJSR77.length() != 0) {
                                          this.ctx.browseHandler.cd(this.ctx.lastPlaceInJSR77);
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
      }
   }

   void saveLastPlaceInPreviousTree() throws ScriptException {
      String currentPrompt = this.ctx.getPrompt();
      WLScriptContext var10001 = this.ctx;
      if (this.ctx.domainType.equals("Domain")) {
         this.ctx.lastPlaceInConfig = currentPrompt;
      } else {
         var10001 = this.ctx;
         if (this.ctx.domainType.equals("DomainConfig")) {
            this.ctx.lastPlaceInAdminConfig = currentPrompt;
         } else {
            var10001 = this.ctx;
            if (!this.ctx.domainType.equals("DomainRuntime")) {
               var10001 = this.ctx;
               if (this.ctx.domainType.equals("RuntimeConfigServerDomain")) {
                  this.ctx.lastPlaceInConfigRuntime = currentPrompt;
               } else {
                  var10001 = this.ctx;
                  if (this.ctx.domainType.equals("RuntimeRuntimeServerDomain")) {
                     this.ctx.lastPlaceInRuntimeRuntime = currentPrompt;
                  } else {
                     var10001 = this.ctx;
                     if (this.ctx.domainType.equals("RuntimeDomainRuntime")) {
                        this.ctx.lastPlaceInRuntimeDomainRuntime = currentPrompt;
                     } else {
                        var10001 = this.ctx;
                        if (this.ctx.domainType.equals("ConfigDomainRuntime")) {
                           this.ctx.lastPlaceInConfigDomainRuntime = currentPrompt;
                        } else {
                           var10001 = this.ctx;
                           if (this.ctx.domainType.equals("ConfigEdit")) {
                              this.ctx.lastPlaceInEdit = currentPrompt;
                           } else {
                              var10001 = this.ctx;
                              if (this.ctx.domainType.equals("JSR77")) {
                                 this.ctx.lastPlaceInJSR77 = currentPrompt;
                              } else {
                                 var10001 = this.ctx;
                                 if (this.ctx.domainType.equals("Custom_Domain")) {
                                    this.ctx.lastPlaceInCustom = currentPrompt;
                                 } else {
                                    var10001 = this.ctx;
                                    if (this.ctx.domainType.equals("DomainCustom_Domain")) {
                                       this.ctx.lastPlaceInDomainCustom = currentPrompt;
                                    } else {
                                       var10001 = this.ctx;
                                       if (this.ctx.domainType.equals("JNDI")) {
                                          this.ctx.lastPlaceInJNDI = currentPrompt;
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            } else {
               if (this.ctx.isAdminServer) {
                  this.ctx.lastPlaceInRuntime = currentPrompt;
               } else if (this.ctx.prompts.size() == 2) {
                  this.ctx.lastPlaceInRuntime = "/";
               } else {
                  this.ctx.prompts.remove(0);
                  this.ctx.prompts.remove(0);
                  currentPrompt = "";

                  for(Iterator i = this.ctx.prompts.iterator(); i.hasNext(); currentPrompt = currentPrompt + "/" + (String)i.next() + "/") {
                  }

                  this.ctx.lastPlaceInRuntime = currentPrompt;
               }

            }
         }
      }
   }

   public void runtimeRuntime() throws ScriptException {
      if (!this.ctx.isRuntimeServerEnabled) {
         this.ctx.println(txtFmt.getRuntimeServerNotEnabled());
      } else {
         try {
            WLScriptContext var10001 = this.ctx;
            if (!this.ctx.domainType.equals("RuntimeRuntimeServerDomain")) {
               if (!this.runtimeRuntimeNavigatedBefore) {
                  this.ctx.println(txtFmt.getLocationChangedToServerRuntime());
               }

               this.runtimeRuntimeNavigatedBefore = true;
               this.saveLastPlaceInPreviousTree();
               this.ctx.wlcmo = this.ctx.runtimeServerRuntimeMBean;
               this.ctx.prompts = new Stack();
               this.ctx.beans = new Stack();
               this.ctx.beans.add(this.ctx.wlcmo);
               this.ctx.prompt = "";
               var10001 = this.ctx;
               this.ctx.domainType = "RuntimeRuntimeServerDomain";
               this.ctx.browseHandler.changeToBeanLevel();
               this.goToLastPlaceInCurrentTree();
               this.ctx.mbs = this.ctx.getMBSConnection((String)null);
            } else {
               this.ctx.println(txtFmt.getAlreadyInServerRuntime());
            }
         } catch (Throwable var2) {
            if (var2 instanceof ScriptException) {
               throw (ScriptException)var2;
            }

            this.ctx.throwWLSTException(txtFmt.getErrorTraversingToServerRuntime(), var2);
         }

      }
   }

   public void configDomainRuntime() throws ScriptException {
      if (!this.ctx.isAdminServer) {
         this.ctx.println(txtFmt.getDomainRuntimeNotAvailableOnMS());
      } else if (!this.ctx.isDomainRuntimeServerEnabled) {
         this.ctx.println(txtFmt.getDomainRuntimeServerNotEnabled());
      } else {
         try {
            WLScriptContext var10001 = this.ctx;
            if (!this.ctx.domainType.equals("ConfigDomainRuntime")) {
               if (!this.configDomainRuntimeNavigatedBefore) {
                  this.ctx.println(txtFmt.getLocationChangedToDomainConfig());
               }

               this.configDomainRuntimeNavigatedBefore = true;
               this.saveLastPlaceInPreviousTree();
               this.ctx.wlcmo = this.ctx.configDomainRuntimeDRMBean;
               var10001 = this.ctx;
               this.ctx.domainType = "ConfigDomainRuntime";
               this.ctx.browseHandler.initCommonVariables();
               this.goToLastPlaceInCurrentTree();
               this.ctx.mbs = this.ctx.getMBSConnection((String)null);
            } else {
               this.ctx.println(txtFmt.getAlreadyInDomainConfig());
            }
         } catch (Throwable var2) {
            if (var2 instanceof ScriptException) {
               throw (ScriptException)var2;
            }

            this.ctx.throwWLSTException(txtFmt.getErrorTraversingToDomainConfig(), var2);
         }

      }
   }

   public void configEdit() throws ScriptException {
      if (!this.ctx.isAdminServer) {
         this.ctx.println(txtFmt.getEditNotAvailableOnMS());
      } else if (!this.ctx.isEditServerEnabled) {
         this.ctx.println(txtFmt.getEditServerNotEnabled());
      } else {
         try {
            WLScriptContext var10001 = this.ctx;
            if (!this.ctx.domainType.equals("ConfigEdit")) {
               if (!this.editNavigatedBefore) {
                  this.ctx.addEditChangeListener();
                  this.ctx.println(txtFmt.getLocationChangedToEdit());
               }

               this.editNavigatedBefore = true;
               this.saveLastPlaceInPreviousTree();
               this.ctx.wlcmo = this.ctx.editDomainMBean;
               var10001 = this.ctx;
               this.ctx.domainType = "ConfigEdit";
               this.ctx.browseHandler.initCommonVariables();
               if (this.doesUserHasLock()) {
                  this.ctx.isEditSessionInProgress = true;
                  this.ctx.println(txtFmt.getEditSessionInProgress());
               }

               this.goToLastPlaceInCurrentTree();
               this.ctx.mbs = this.ctx.getMBSConnection((String)null);
            } else {
               this.ctx.println(txtFmt.getAlreadyInEdit());
            }
         } catch (Throwable var2) {
            if (var2 instanceof ScriptException) {
               throw (ScriptException)var2;
            }

            this.ctx.throwWLSTException(txtFmt.getErrorTraversingToEdit(), var2);
         }

      }
   }

   boolean doesUserHasLock() {
      return this.ctx.configurationManager.isEditor() && (!this.ctx.configurationManager.isCurrentEditorExclusive() || this.ctx.isEditSessionExclusive);
   }

   public void jsr77() throws ScriptException {
      if (!this.ctx.isAdminServer) {
         this.ctx.println(txtFmt.getJSR77NotAvailableOnMS());
      } else if (!this.ctx.isEditServerEnabled) {
         this.ctx.println(txtFmt.getJSR77ServerNotEnabled());
      } else {
         this.ctx.println("Not implemented yet!");
      }
   }

   public void runtimeDomainRuntime() throws ScriptException {
      if (!this.ctx.isAdminServer) {
         this.ctx.println(txtFmt.getDomainRuntimeNotAvailableOnMS());
      } else if (!this.ctx.isDomainRuntimeServerEnabled) {
         this.ctx.println(txtFmt.getDomainRuntimeServerNotEnabled());
      } else {
         try {
            WLScriptContext var10001 = this.ctx;
            if (!this.ctx.domainType.equals("RuntimeDomainRuntime")) {
               if (!this.runtimeDomainRuntimeNavigatedBefore) {
                  this.ctx.println(txtFmt.getLocationChangedToDomainRuntime());
               }

               this.runtimeDomainRuntimeNavigatedBefore = true;
               this.saveLastPlaceInPreviousTree();
               this.ctx.wlcmo = this.ctx.runtimeDomainRuntimeDRMBean;
               var10001 = this.ctx;
               this.ctx.domainType = "RuntimeDomainRuntime";
               this.ctx.browseHandler.initCommonVariables();
               this.goToLastPlaceInCurrentTree();
               this.ctx.mbs = this.ctx.getMBSConnection((String)null);
            } else {
               this.ctx.println(txtFmt.getAlreadyInDomainRuntime());
            }
         } catch (Throwable var2) {
            if (var2 instanceof ScriptException) {
               throw (ScriptException)var2;
            }

            this.ctx.throwWLSTException(txtFmt.getErrorTraversingToDomainRuntime(), var2);
         }

      }
   }

   void pop() throws ScriptException {
      if (this.ctx.inMBeanType) {
         this.ctx.browseHandler.changeToBeanLevel();
         this.ctx.browseHandler.popAndPeek();
      } else if (this.ctx.inMBeanTypes) {
         this.ctx.browseHandler.changeToBeanLevel();
         this.ctx.browseHandler.popAndPeek();
      } else if (this.ctx.atBeanLevel) {
         this.ctx.browseHandler.popAndPeek();
         Object obj = null;
         String prevName = null;
         if (this.ctx.prompts.size() == 0) {
            this.ctx.prompt = this.ctx.evaluatePrompt();
            this.ctx.browseHandler.changeToBeanLevel();
            return;
         }

         prevName = (String)this.ctx.prompts.peek();

         try {
            obj = this.ctx.getMBSConnection(this.ctx.domainType).getAttribute(this.ctx.getObjectName(this.ctx.beans.peek()), prevName);
         } catch (AttributeNotFoundException var4) {
            this.ctx.prompt = this.ctx.evaluatePrompt();
            this.ctx.browseHandler.changeToBeanLevel();
            return;
         } catch (Throwable var5) {
            this.ctx.throwWLSTException(txtFmt.getErrorCdingToBean(), var5);
         }

         if (obj instanceof ObjectName || obj instanceof ObjectName[]) {
            if (obj instanceof ObjectName) {
               this.ctx.wlInstanceObjName = (ObjectName)obj;
               this.ctx.browseHandler.populateNames(obj);
               this.ctx.browseHandler.changeToMBeanTypeLevel();
            } else {
               this.ctx.wlInstanceObjNames = (ObjectName[])((ObjectName[])obj);
               this.ctx.browseHandler.populateNames(obj);
               this.ctx.browseHandler.changeToMBeanTypesLevel();
            }

            this.ctx.prompt = this.ctx.evaluatePrompt();
         }
      }

      this.ctx.prompt = this.ctx.evaluatePrompt();
   }
}
