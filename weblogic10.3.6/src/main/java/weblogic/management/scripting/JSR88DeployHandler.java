package weblogic.management.scripting;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;
import javax.enterprise.deploy.spi.exceptions.InvalidModuleException;
import javax.enterprise.deploy.spi.exceptions.TargetException;
import javax.enterprise.deploy.spi.status.ProgressObject;
import org.python.core.ArgParser;
import org.python.core.PyDictionary;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.api.spi.WebLogicDeploymentConfiguration;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.deploy.api.tools.SessionHelper;
import weblogic.deploy.api.tools.deployer.ModuleTargetInfo;
import weblogic.deploy.api.tools.deployer.SubModuleTargetInfo;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.scripting.jsr88.WLSTPlan;
import weblogic.management.scripting.jsr88.WLSTPlanImpl;
import weblogic.management.scripting.jsr88.WLSTProgress;
import weblogic.management.scripting.jsr88.WLSTProgressImpl;
import weblogic.management.scripting.utils.WLSTMsgTextFormatter;
import weblogic.management.scripting.utils.WLSTUtil;
import weblogic.utils.StringUtils;

public class JSR88DeployHandler extends JSR88DeploymentConstants {
   WLScriptContext ctx = null;
   private WLSTMsgTextFormatter txtFmt;
   private static String NONE = "None";
   private static String FALSE = "false";
   private static String TRUE = "true";
   private WebLogicDeploymentManager dm = null;
   private SessionHelper helper = null;

   public JSR88DeployHandler(WLScriptContext ctx) {
      this.ctx = ctx;
      this.txtFmt = ctx.getWLSTMsgFormatter();
   }

   public WebLogicDeploymentManager getWLDM(boolean remote) throws ScriptException {
      this.init(remote);
      return this.dm;
   }

   public WebLogicDeploymentManager getWLDM() throws ScriptException {
      this.init();
      return this.dm;
   }

   private void init() throws ScriptException {
      this.init(false);
   }

   private void init(boolean remote) throws ScriptException {
      if (this.dm != null) {
         this.dm.release();
         this.dm = null;
      }

      if (this.ctx.isConnected() && !this.ctx.isAdminServer) {
         throw new ScriptException(this.txtFmt.getNotConnectedAdminServer(), this.ctx.commandType);
      } else {
         try {
            if (!this.ctx.isConnected()) {
               this.dm = SessionHelper.getDisconnectedDeploymentManager();
            } else {
               String host = this.ctx.getListenAddress(this.ctx.url);
               String port = this.ctx.getListenPort(this.ctx.url);
               String protocol = this.ctx.getProtocol(this.ctx.url);
               if (remote) {
                  this.dm = SessionHelper.getRemoteDeploymentManager(protocol, host, port, new String(this.ctx.username_bytes), new String(this.ctx.password_bytes));
               } else {
                  this.dm = SessionHelper.getDeploymentManager(protocol, host, port, new String(this.ctx.username_bytes), new String(this.ctx.password_bytes));
               }
            }

            if (this.helper != null) {
               this.helper.close();
               this.helper = null;
            }

            this.helper = SessionHelper.getInstance(this.dm);
         } catch (DeploymentManagerCreationException var5) {
            this.ctx.throwWLSTException(this.txtFmt.getCouldNotCreateDM(), var5);
         } catch (Throwable var6) {
            this.ctx.throwWLSTException(this.txtFmt.getCouldNotCreateDM(), var6);
         }

      }
   }

   public WLSTPlan loadApplication(String appPath, String planPath, String createPlan) throws ScriptException {
      this.ctx.println(this.txtFmt.getLoadingAppFrom(appPath));
      this.init();
      WLSTPlanImpl wlstPlan = null;
      boolean isInstallDir = false;
      boolean planExists = false;
      File moduleObject = new File(appPath);
      this.ctx.printDebug(this.txtFmt.getEnsureSubdirOfApp());
      if (moduleObject.getParentFile().getName().equals("app")) {
         isInstallDir = true;
      }

      File planFile = null;
      if (planPath == null) {
         planPath = this.getPlanPath(moduleObject.getAbsolutePath(), isInstallDir);
         planFile = new File(planPath);
         if (!planFile.exists()) {
            if (this.ctx.getBoolean(createPlan)) {
               this.ctx.println(this.txtFmt.getCreatePlan(planFile.getAbsolutePath()));
            } else {
               this.ctx.throwWLSTException(this.txtFmt.getCouldNotFindPlan());
            }
         } else {
            planExists = true;
         }
      } else {
         planFile = new File(planPath);
         if (!planFile.exists()) {
            if (this.ctx.getBoolean(createPlan)) {
               this.ctx.println(this.txtFmt.getCreatePlan(planFile.getAbsolutePath()));
            } else {
               this.ctx.throwWLSTException(this.txtFmt.getCouldNotFindPlan());
            }
         } else {
            planExists = true;
         }
      }

      try {
         if (this.ctx.getBoolean(createPlan) && !planExists) {
            this.helper.initializeConfiguration(moduleObject, (File)null);
         } else {
            this.helper.initializeConfiguration(moduleObject, planFile);
         }

         WebLogicDeploymentConfiguration wdc = this.helper.getConfiguration();
         wlstPlan = new WLSTPlanImpl(wdc, this.ctx, planFile.getAbsolutePath());
         if (this.ctx.getBoolean(createPlan) && !planExists) {
            wlstPlan.save();
         }

         String msg = this.txtFmt.getLoadedAppAndPlan(moduleObject.getAbsolutePath(), planFile.getAbsolutePath());
         this.ctx.println(msg);
         if (!WLSTUtil.runningWLSTAsModule()) {
            String planObjectName = "wlstPlan_" + wlstPlan.getDeploymentPlan().getApplicationName();
            planObjectName = planObjectName.replace('.', '_');
            WLSTUtil.getWLSTInterpreter().set(planObjectName, wlstPlan);
            this.ctx.println(this.txtFmt.getPlanVariableAssigned(planObjectName));
         }
      } catch (ConfigurationException var12) {
         this.ctx.throwWLSTException(this.txtFmt.getCouldNotInitConfig(), var12);
      } catch (IOException var13) {
         this.ctx.throwWLSTException(this.txtFmt.getCouldNotReadConfig(), var13);
      } catch (InvalidModuleException var14) {
         this.ctx.throwWLSTException(this.txtFmt.getInvalidModule(), var14);
      }

      return wlstPlan;
   }

   private String getExistingPlanPath(String appPath) {
      File file = new File(appPath);
      String planPath = file.getParentFile().getParentFile().getAbsolutePath() + "/plan/plan.xml";
      File f = new File(planPath);
      return f.exists() ? f.getAbsolutePath() : null;
   }

   private String getPlanPath(String appPath, boolean isInstallDir) {
      File file = new File(appPath);
      String planPath = null;
      if (isInstallDir) {
         this.ctx.printDebug(this.txtFmt.getAppPathIsDir(appPath));
         planPath = file.getParentFile().getParentFile().getAbsolutePath() + "/plan/plan.xml";
         File f = new File(planPath);
         if (!f.exists()) {
            this.ctx.printDebug(this.txtFmt.getPlanDoesNotExist(planPath));
            f = new File(file.getParentFile().getAbsolutePath() + "/plan.xml");
         }

         f.getParentFile().mkdirs();
         planPath = f.getAbsolutePath();
         this.ctx.printDebug(this.txtFmt.getPlanPathEvaluated(planPath));
         return planPath;
      } else {
         planPath = file.getParentFile().getAbsolutePath() + File.separator + "plan.xml";
         this.ctx.printDebug(this.txtFmt.getPlanPathEvaluated(planPath));
         return planPath;
      }
   }

   private TargetModuleID[] createTmidsFromApp(String appName) {
      if (appName == null) {
         return new TargetModuleID[0];
      } else {
         TargetModuleID[] tmids = null;
         AppDeploymentMBean mbean = ApplicationVersionUtils.getAppDeployment(this.dm.getHelper().getDomain(), appName, (String)null);
         if (mbean != null) {
            List l = this.dm.getServerConnection().getModules(mbean);
            tmids = (TargetModuleID[])((TargetModuleID[])l.toArray(new TargetModuleID[0]));
            return tmids;
         } else {
            tmids = new TargetModuleID[0];
            return this.createTmidForAdminServer(tmids, appName);
         }
      }
   }

   private TargetModuleID[] createTmidForAdminServer(TargetModuleID[] tmids, String appName) {
      if (appName == null) {
         return new TargetModuleID[0];
      } else {
         TargetModuleID[] newTmids = new TargetModuleID[tmids.length + 1];
         if (0 != tmids.length) {
            System.arraycopy(tmids, 0, newTmids, 1, tmids.length);
         }

         String admin = this.dm.getHelper().getAdminServerName();
         Target[] allt = this.dm.getTargets();

         for(int i = 0; i < allt.length; ++i) {
            Target target = allt[i];
            if (target.getName().equals(admin)) {
               newTmids[0] = this.dm.createTargetModuleID((String)appName, (ModuleType)WebLogicModuleType.UNKNOWN, (Target)target);
               return newTmids;
            }
         }

         return tmids;
      }
   }

   private TargetModuleID[] getTMIDs(String tgtNames, WebLogicDeploymentManager dm, String name, String subModuleTargets) throws ScriptException {
      List tl = new ArrayList();
      Target[] allt = dm.getTargets();
      Target[] targets = new Target[0];
      if (tgtNames != null && tgtNames.length() != 0 || subModuleTargets != null && subModuleTargets.length() != 0) {
         if (tgtNames == null) {
            tgtNames = "";
         }

         String[] targetNames = StringUtils.splitCompletely(tgtNames, ",");
         String[] smTargetNames = null;
         if (subModuleTargets != null) {
            smTargetNames = StringUtils.splitCompletely(subModuleTargets, ",");
         }

         Set targetInfos = this.getTargetInfos(targetNames, smTargetNames);

         Target targ;
         for(Iterator ti = targetInfos.iterator(); ti.hasNext(); tl.add(targ)) {
            ModuleTargetInfo mti = (ModuleTargetInfo)ti.next();
            targ = this.findTarget(mti, allt);
            if (targ == null) {
               String msg = this.txtFmt.getCouldNotFindMatchingTargets(mti.getTarget(), subModuleTargets);
               this.ctx.throwWLSTException(msg);
            }
         }

         targets = (Target[])((Target[])tl.toArray(new Target[0]));
         List list = this.prepareTmids(targetInfos, targets, dm, name);
         return this.getTmids(list);
      } else {
         return this.createTmidsFromApp(name);
      }
   }

   private Target findTarget(ModuleTargetInfo mti, Target[] allt) {
      for(int i = 0; i < allt.length; ++i) {
         Target target = allt[i];
         if (target.getName().equals(mti.getTarget())) {
            return target;
         }
      }

      return null;
   }

   private Set getTargetInfos(String[] modlist, String[] submodlist) {
      Set targs = new HashSet();

      int i;
      for(i = 0; i < modlist.length; ++i) {
         targs.add(new ModuleTargetInfo(modlist[i]));
      }

      if (submodlist != null) {
         for(i = 0; i < submodlist.length; ++i) {
            targs.add(new SubModuleTargetInfo(submodlist[i]));
         }
      }

      return targs;
   }

   private List prepareTmids(Set targetInfos, Target[] targets, WebLogicDeploymentManager dm, String name) {
      List tmids = new ArrayList();
      if (name == null) {
         return tmids;
      } else {
         Iterator infos = targetInfos.iterator();

         while(infos.hasNext()) {
            ModuleTargetInfo mti = (ModuleTargetInfo)infos.next();
            tmids.add(mti.createTmid(name, this.findTarget(mti, targets), dm));
         }

         return tmids;
      }
   }

   private TargetModuleID[] getTmids(List tmids) {
      return (TargetModuleID[])((TargetModuleID[])tmids.toArray(new TargetModuleID[0]));
   }

   public Object distributeApplication(PyObject[] args, String[] kw) throws ScriptException {
      ArgParser ap = new ArgParser("distributeApplication", args, kw, "appPath", "planPath", "targets");
      WLSTProgress progress = null;

      try {
         String appPath = ap.getString(0);
         File app = new File(appPath);
         String planPath = ap.getString(1);
         File plan = null;
         if (planPath != null) {
            plan = new File(planPath);
         }

         String targets = ap.getString(2);
         if (targets == null) {
            targets = this.ctx.serverName;
         }

         PyDictionary pyOptions = (PyDictionary)ap.getPyObject(3);
         String upload = pyOptions.get(DEPLOY_UPLOAD).toString();
         boolean doUpload = new Boolean(upload);
         WebLogicDeploymentManager dMgr = this.getWLDM(doUpload);
         DeploymentOptions depOptions = this.getDeploymentOptions();
         String subModuleTargets = this.getSubModuleTargets(pyOptions);
         TargetModuleID[] actualTargets = this.getTMIDs(targets, dMgr, app.getName(), subModuleTargets);
         if (pyOptions.items().__len__() > 0) {
            depOptions = this.getDeploymentOptions(pyOptions);
         }

         this.ctx.println(this.txtFmt.getDistributingApplication(app.getAbsolutePath(), targets));
         ProgressObject po = dMgr.distribute(actualTargets, app, plan, depOptions);
         String block = pyOptions.get(BLOCK).toString();
         progress = new WLSTProgressImpl(po, this.ctx);
         if (block == null || block.equals(NONE)) {
            block = TRUE;
         }

         block = this.doBlock("distribution", block);
         if (block.toLowerCase(Locale.US).toString().equals(TRUE)) {
            this.isDoneOrTimedOut(progress, pyOptions);
            if (progress.isFailed()) {
               this.ctx.println(this.txtFmt.getFailedToDistributeApp(progress.getState()));
               progress.printStatus();
               this.ctx.throwWLSTException(this.txtFmt.getFailedToDistributeApp(progress.getState()));
               return progress;
            }

            this.ctx.println(this.txtFmt.getAppDistributionComplete(progress.getState()));
            progress.printStatus();
            return progress;
         }

         this.ctx.println(this.txtFmt.getDistributionStarted());
      } catch (TargetException var19) {
         this.ctx.throwWLSTException(this.txtFmt.getErrorDistributingApp(var19.getMessage()), var19);
      }

      return progress;
   }

   private String doBlock(String action, String block) {
      if (this.ctx.isEditSessionInProgress) {
         this.ctx.println(this.txtFmt.getEditSessionInProgress(action));
         return FALSE;
      } else {
         return block;
      }
   }

   public Object deploy(PyObject[] args, String[] kw) throws ScriptException {
      this.ctx.commandType = "deploy";
      boolean createdPlan = false;
      boolean doCreatePlan = false;
      boolean doUpload = false;
      String block = FALSE;
      File plan = null;

      try {
         ArgParser ap = new ArgParser("deploy", args, kw, "appName", "path", "targets");
         String appName = ap.getString(0);
         String appPath = ap.getString(1);
         File app = new File(appPath);
         String planPath = ap.getString(4);
         if (planPath != null) {
            plan = new File(planPath);
         }

         String targets = ap.getString(2);
         String stageMode = ap.getString(3);
         PyDictionary pyOptions = (PyDictionary)ap.getPyObject(5);
         DeploymentOptions depOptions = this.getDeploymentOptions();
         ProgressObject po = null;
         String createPlan = pyOptions.get(CREATE_PLAN).toString();
         if (createPlan != null && !createPlan.equals(NONE)) {
            doCreatePlan = this.ctx.getBoolean(createPlan);
         } else {
            doCreatePlan = false;
         }

         String upload = pyOptions.get(DEPLOY_UPLOAD).toString();
         doUpload = new Boolean(upload);
         String remote = pyOptions.get(REMOTE).toString();
         if (!TRUE.equalsIgnoreCase(remote) && !app.exists()) {
            throw new ScriptException(this.txtFmt.getApplicationPathNotFound(app.getAbsolutePath()), this.ctx.commandType);
         } else {
            WebLogicDeploymentManager dMgr = this.getWLDM(doUpload);
            boolean isInstallDir = false;
            if (app.getParentFile() != null && app.getParentFile().getName().equals("app")) {
               isInstallDir = true;
            }

            try {
               if (doCreatePlan) {
                  this.ctx.printDebug(this.txtFmt.getCreatePlanTrue());
                  if (plan == null) {
                     planPath = this.getPlanPath(app.getAbsolutePath(), isInstallDir);
                     plan = new File(planPath);
                  }

                  this.helper.initializeConfiguration(app, (File)null);
                  WebLogicDeploymentConfiguration wdc = this.helper.getConfiguration();
                  WLSTPlan wlstPlan = new WLSTPlanImpl(wdc, this.ctx, plan.getAbsolutePath());
                  wlstPlan.getDeploymentPlan().setApplicationName(appName);
                  wlstPlan.save();
               }

               String _path;
               if (isInstallDir && plan == null) {
                  _path = this.getExistingPlanPath(app.getAbsolutePath());
                  if (_path != null) {
                     plan = new File(_path);
                  }
               }

               _path = this.getSubModuleTargets(pyOptions);
               TargetModuleID[] tmids = this.getTMIDs(targets, dMgr, appName, _path);
               if (pyOptions.items().__len__() > 0) {
                  depOptions = this.getDeploymentOptions(pyOptions);
               }

               depOptions.setStageMode(stageMode);
               depOptions.setName(appName);
               this.ctx.println(this.txtFmt.getDeployingApplication(app.getAbsolutePath(), targets, doUpload));
               if (plan != null) {
                  String msg = this.txtFmt.getDeployingApplicationWithPlan(app.getAbsolutePath(), plan.getAbsolutePath());
                  this.ctx.printDebug(msg);
               }

               po = dMgr.deploy(tmids, app, plan, depOptions);
            } catch (TargetException var26) {
               this.ctx.throwWLSTException(this.txtFmt.getDeploymentFailed(), var26);
            } catch (Throwable var27) {
               if (this.ctx.debug) {
                  var27.printStackTrace();
               }

               this.ctx.throwWLSTException(this.txtFmt.getUnexpectedError(var27.getMessage()), var27);
            }

            block = pyOptions.get(BLOCK).toString();
            if (block == null || block.equals(NONE)) {
               block = TRUE;
            }

            block = this.doBlock("deployment", block);
            WLSTProgress progress = new WLSTProgressImpl(po, this.ctx);
            if (block.toLowerCase(Locale.US).toString().equals(TRUE)) {
               this.isDoneOrTimedOut(progress, pyOptions);
               if (progress.isFailed()) {
                  this.ctx.println(this.txtFmt.getFailedToDeployApp(progress.getState()));
                  progress.printStatus();
                  this.ctx.throwWLSTException(this.txtFmt.getDeploymentFailed());
                  return progress;
               } else {
                  this.ctx.println(this.txtFmt.getAppDeploymentComplete(progress.getState()));
                  progress.printStatus();
                  return progress;
               }
            } else {
               this.ctx.println(this.txtFmt.getDeploymentStarted());
               return progress;
            }
         }
      } catch (Throwable var28) {
         this.ctx.throwWLSTException(this.txtFmt.getErrorDeployingApp(var28.getMessage()), var28);
         return null;
      }
   }

   private String getSubModuleTargets(PyDictionary pyOptions) {
      String tgts = pyOptions.get(SUB_MODULE_TARGETS).toString();
      return tgts != null && !tgts.equals(NONE) ? tgts : null;
   }

   private String getTargets(PyDictionary pyOptions) {
      String tgts = pyOptions.get(TARGETS).toString();
      return tgts != null && !tgts.equals(NONE) ? tgts : null;
   }

   private String[] getDeltas(PyDictionary pyOptions) {
      String deltaVal = pyOptions.get(DELTA).toString();
      if (deltaVal != null && !deltaVal.equals(NONE)) {
         String[] deltas = StringUtils.splitCompletely(deltaVal, ",");
         return deltas;
      } else {
         return null;
      }
   }

   private String getAppPath(String appname) {
      AppDeploymentMBean appMBean = this.ctx.editServiceMBean.getDomainConfiguration().lookupAppDeployment(appname);
      return appMBean == null ? null : appMBean.getAbsoluteSourcePath();
   }

   private void isDoneOrTimedOut(WLSTProgress progress, PyDictionary pyOptions) throws ScriptException {
      PyObject _timeout = pyOptions.get(TIME_OUT);
      int timeout = 300000;
      if (!_timeout.toString().equals(NONE) && _timeout != null) {
         timeout = (Integer)_timeout.__tojava__(Integer.class);
      }

      String msg = progress.getMessage();
      if (msg != null) {
         this.ctx.print(msg);
      }

      if (timeout == 0) {
         try {
            while(progress.isRunning()) {
               this.ctx.print(".");
               Thread.sleep(3000L);
            }
         } catch (InterruptedException var11) {
         }
      } else {
         Integer in = new Integer(timeout);
         long quitTime = System.currentTimeMillis() + in.longValue();

         do {
            try {
               if (progress.isRunning()) {
                  this.ctx.print(".");
                  Thread.sleep(3000L);
               }
            } catch (InterruptedException var10) {
            }

            if (quitTime <= System.currentTimeMillis()) {
               this.ctx.throwWLSTException(this.txtFmt.getActionTimedOut((long)timeout));
            }
         } while((timeout == 0 || quitTime > System.currentTimeMillis()) && progress.isRunning());
      }

   }

   public Object redeploy(PyObject[] args, String[] kw) throws ScriptException {
      ArgParser ap = new ArgParser("redeploy", args, kw, "appName");
      String appName = ap.getString(0);
      String planPath = ap.getString(1);
      File plan = null;
      if (planPath != null) {
         plan = new File(planPath);
      }

      PyDictionary pyOptions = (PyDictionary)ap.getPyObject(2);
      String upload = pyOptions.get(DEPLOY_UPLOAD).toString();
      boolean doUpload = new Boolean(upload);
      doUpload = new Boolean(upload);
      WebLogicDeploymentManager dMgr = this.getWLDM(doUpload);
      DeploymentOptions depOptions = this.getDeploymentOptions();
      if (pyOptions.items().__len__() > 0) {
         depOptions = this.getDeploymentOptions(pyOptions);
      }

      this.ctx.println(this.txtFmt.getRedeployingApp(appName));
      String subModuleTargets = this.getSubModuleTargets(pyOptions);
      String[] deltas = this.getDeltas(pyOptions);
      TargetModuleID[] modIds = this.getTMIDs((String)null, dMgr, appName, subModuleTargets);
      String appPath = pyOptions.get(APP_PATH).toString();
      File app = null;
      if (appPath != null && !appPath.equals(NONE)) {
         app = new File(appPath);
      }

      ProgressObject po = null;
      if (deltas != null) {
         po = dMgr.redeploy(modIds, app, deltas, depOptions);
      } else {
         po = dMgr.redeploy(modIds, app, plan, depOptions);
      }

      String block = pyOptions.get(BLOCK).toString();
      WLSTProgress progress = new WLSTProgressImpl(po, this.ctx);
      if (block == null || block.equals(NONE)) {
         block = TRUE;
      }

      block = this.doBlock("redeployment", block);
      if (block.toLowerCase(Locale.US).toString().equals(TRUE)) {
         this.isDoneOrTimedOut(progress, pyOptions);
         if (progress.isFailed()) {
            this.ctx.println(this.txtFmt.getFailedToRedeployApp(progress.getState()));
            progress.printStatus();
            this.ctx.throwWLSTException(this.txtFmt.getFailedToRedeployApp(progress.getState()));
            return progress;
         } else {
            this.ctx.println(this.txtFmt.getCompletedAppRedeploy(progress.getState()));
            progress.printStatus();
            return progress;
         }
      } else {
         this.ctx.println(this.txtFmt.getRedeploymentStarted());
         return progress;
      }
   }

   public Object undeploy(PyObject[] args, String[] kw) throws ScriptException {
      ArgParser ap = new ArgParser("undeploy", args, kw, "targets");
      String appName = ap.getString(0);
      String targets = ap.getString(1);
      PyDictionary pyOptions = (PyDictionary)ap.getPyObject(2);
      WebLogicDeploymentManager dMgr = this.getWLDM();
      this.ctx.println(this.txtFmt.getUndeployingApp(appName));
      DeploymentOptions depOptions = this.getDeploymentOptions();
      if (pyOptions.items().__len__() > 0) {
         depOptions = this.getDeploymentOptions(pyOptions);
      }

      String subModuleTargets = this.getSubModuleTargets(pyOptions);
      TargetModuleID[] modIds = this.getTMIDs(targets, dMgr, appName, subModuleTargets);
      String[] deltas = this.getDeltas(pyOptions);
      ProgressObject po = null;
      if (deltas != null) {
         po = dMgr.undeploy(modIds, (File)null, deltas, depOptions);
      } else {
         po = dMgr.undeploy(modIds, depOptions);
      }

      WLSTProgress progress = new WLSTProgressImpl(po, this.ctx);
      String block = pyOptions.get(BLOCK).toString();
      if (block == null || block.equals(NONE)) {
         block = TRUE;
      }

      block = this.doBlock("undeployment", block);
      if (block.toLowerCase(Locale.US).toString().equals(TRUE)) {
         this.isDoneOrTimedOut(progress, pyOptions);
         if (progress.isFailed()) {
            this.ctx.println(this.txtFmt.getFailedToUndeployApp(progress.getState()));
            progress.printStatus();
            this.ctx.throwWLSTException(this.txtFmt.getFailedToUndeployApp(progress.getState()));
            return progress;
         } else {
            this.ctx.println(this.txtFmt.getCompletedAppUndeploy(progress.getState()));
            progress.printStatus();
            return progress;
         }
      } else {
         this.ctx.println(this.txtFmt.getUndeploymentStarted());
         return progress;
      }
   }

   public Object startApplication(PyObject[] args, String[] kw) throws ScriptException {
      ArgParser ap = new ArgParser("startApplication", args, kw, "appName");
      String appName = ap.getString(0);
      PyDictionary pyOptions = (PyDictionary)ap.getPyObject(1);
      WebLogicDeploymentManager dMgr = this.getWLDM();
      DeploymentOptions depOptions = this.getDeploymentOptions();
      if (pyOptions.items().__len__() > 0) {
         depOptions = this.getDeploymentOptions(pyOptions);
      }

      this.ctx.println(this.txtFmt.getStartingApplication(appName));
      String subModuleTargets = this.getSubModuleTargets(pyOptions);
      String targets = this.getTargets(pyOptions);
      TargetModuleID[] modIds = this.getTMIDs(targets, dMgr, appName, subModuleTargets);
      ProgressObject po = dMgr.start(modIds, depOptions);
      String block = pyOptions.get(BLOCK).toString();
      WLSTProgress progress = new WLSTProgressImpl(po, this.ctx);
      if (block == null || block.equals(NONE)) {
         block = TRUE;
      }

      block = this.doBlock("startApplication", block);
      if (block.toLowerCase(Locale.US).toString().equals(TRUE)) {
         this.isDoneOrTimedOut(progress, pyOptions);
         if (progress.isFailed()) {
            this.ctx.println(this.txtFmt.getFailedToStartApp(progress.getState()));
            progress.printStatus();
            this.ctx.throwWLSTException(this.txtFmt.getFailedToStartApp(progress.getState()));
            return progress;
         } else {
            this.ctx.println(this.txtFmt.getCompletedAppStart(progress.getState()));
            progress.printStatus();
            return progress;
         }
      } else {
         this.ctx.println(this.txtFmt.getApplicationStarted());
         return progress;
      }
   }

   public Object stopApplication(PyObject[] args, String[] kw) throws ScriptException {
      ArgParser ap = new ArgParser("stopApplication", args, kw, "appName");
      String appName = ap.getString(0);
      PyDictionary pyOptions = (PyDictionary)ap.getPyObject(1);
      DeploymentOptions depOptions = this.getDeploymentOptions();
      if (pyOptions.items().__len__() > 0) {
         depOptions = this.getDeploymentOptions(pyOptions);
      }

      WebLogicDeploymentManager dMgr = this.getWLDM();
      this.ctx.println(this.txtFmt.getStoppingApplication(appName));
      String targets = this.getTargets(pyOptions);
      String subModuleTargets = this.getSubModuleTargets(pyOptions);
      TargetModuleID[] modIds = this.getTMIDs(targets, dMgr, appName, subModuleTargets);
      ProgressObject po = dMgr.stop(modIds, depOptions);
      WLSTProgress progress = new WLSTProgressImpl(po, this.ctx);
      String block = pyOptions.get(BLOCK).toString();
      if (block == null || block.equals(NONE)) {
         block = TRUE;
      }

      block = this.doBlock("stopApplication", block);
      if (block.toLowerCase(Locale.US).toString().equals(TRUE)) {
         this.isDoneOrTimedOut(progress, pyOptions);
         if (progress.isFailed()) {
            this.ctx.println(this.txtFmt.getFailedToStopApp(progress.getState()));
            progress.printStatus();
            this.ctx.throwWLSTException(this.txtFmt.getFailedToStopApp(progress.getState()));
            return progress;
         } else {
            this.ctx.println(this.txtFmt.getCompletedAppStop(progress.getState()));
            progress.printStatus();
            return progress;
         }
      } else {
         this.ctx.println(this.txtFmt.getStopStarted());
         return progress;
      }
   }

   public Object updateApplication(PyObject[] args, String[] kw) throws ScriptException {
      ArgParser ap = new ArgParser("updateApplication", args, kw, "appName", "planPath");
      String appName = ap.getString(0);
      String planPath = ap.getString(1);
      File plan = null;
      if (planPath != null) {
         plan = new File(planPath);
      } else {
         this.ctx.throwWLSTException(this.txtFmt.getPlanPathNeededToUpdate());
      }

      PyDictionary pyOptions = (PyDictionary)ap.getPyObject(2);
      String upload = pyOptions.get(DEPLOY_UPLOAD).toString();
      boolean doUpload = new Boolean(upload);
      WebLogicDeploymentManager dMgr = this.getWLDM(doUpload);
      DeploymentOptions depOptions = this.getDeploymentOptions();
      if (pyOptions.items().__len__() > 0) {
         depOptions = this.getDeploymentOptions(pyOptions);
      }

      this.ctx.println(this.txtFmt.getUpdatingApp(appName));
      String subModuleTargets = this.getSubModuleTargets(pyOptions);
      TargetModuleID[] modIds = this.getTMIDs((String)null, dMgr, appName, subModuleTargets);
      ProgressObject po = dMgr.update(modIds, plan, depOptions);
      String block = pyOptions.get(BLOCK).toString();
      WLSTProgress progress = new WLSTProgressImpl(po, this.ctx);
      if (block == null || block.equals(NONE)) {
         block = TRUE;
      }

      block = this.doBlock("updateApplication", block);
      if (block.toLowerCase(Locale.US).toString().equals(TRUE)) {
         this.isDoneOrTimedOut(progress, pyOptions);
         if (progress.isFailed()) {
            this.ctx.println(this.txtFmt.getFailedToUpdateApp(progress.getState()));
            progress.printStatus();
            this.ctx.throwWLSTException(this.txtFmt.getFailedToUpdateApp(progress.getState()));
            return progress;
         } else {
            this.ctx.println(this.txtFmt.getCompletedAppUpdate(progress.getState()));
            progress.printStatus();
            return progress;
         }
      } else {
         this.ctx.println(this.txtFmt.getUpdateStarted());
         return progress;
      }
   }

   public void listApplications() throws ScriptException {
      try {
         WebLogicDeploymentManager dMgr = this.getWLDM();
         HashSet apps = new HashSet();
         TargetModuleID[] tmids = null;
         Target[] targets = dMgr.getTargets();

         for(int i = 0; i < WebLogicModuleType.getModuleTypes(); ++i) {
            ModuleType mt = WebLogicModuleType.getModuleType(i);
            if (mt != null) {
               tmids = dMgr.getAvailableModules(mt, targets);
               if (tmids != null) {
                  for(int j = 0; j < tmids.length; ++j) {
                     if (tmids[j].getParentTargetModuleID() == null) {
                        apps.add(tmids[j].getModuleID());
                     }
                  }
               }
            }
         }

         Iterator it = apps.iterator();

         while(it.hasNext()) {
            String tmid = (String)it.next();
            String str = " " + ApplicationVersionUtils.getDisplayName(tmid);
            this.ctx.println(str);
         }
      } catch (TargetException var8) {
         this.ctx.throwWLSTException(this.txtFmt.getDeploymentFailed(), var8);
      } catch (Throwable var9) {
         if (this.ctx.debug) {
            var9.printStackTrace();
         }

         this.ctx.throwWLSTException(this.txtFmt.getUnexpectedError(var9.getMessage()), var9);
      }

   }

   private boolean checkForUnrecognizedOptions(PyDictionary pyDic) {
      try {
         Set<String> unrecognizedOptions = this.getUnrecognizedOptions(pyDic);
         if (unrecognizedOptions != null) {
            Iterator i$ = unrecognizedOptions.iterator();

            while(i$.hasNext()) {
               String unrecognized = (String)i$.next();
               this.ctx.println(this.txtFmt.unrecognizedOption(unrecognized));
            }

            return true;
         }
      } catch (Throwable var5) {
         var5.printStackTrace();
      }

      return false;
   }

   private DeploymentOptions getDeploymentOptions() {
      DeploymentOptions depOptions = new DeploymentOptions();
      if (this.ctx.isEditSessionInProgress) {
         depOptions.setUseExpiredLock(true);
      }

      return depOptions;
   }

   private DeploymentOptions getDeploymentOptions(PyDictionary pyDic) {
      DeploymentOptions depOptions = this.getDeploymentOptions();
      String option = pyDic.get(CLUSTER_DEPLOYMENT_TIMEOUT).toString();
      if (option != null && !option.equals(NONE)) {
         depOptions.setClusterDeploymentTimeout(Integer.parseInt(option));
      }

      option = pyDic.get(GRACEFUL_IGNORE_SESSIONS).toString();
      if (option != null && !option.equals(NONE)) {
         if (option.toLowerCase(Locale.US).equals(TRUE)) {
            depOptions.setGracefulIgnoreSessions(true);
         } else if (option.toLowerCase(Locale.US).equals(FALSE)) {
            depOptions.setGracefulIgnoreSessions(false);
         }
      }

      option = pyDic.get(GRACEFUL_PRODUCTION_TO_ADMIN).toString();
      if (option != null && !option.equals(NONE)) {
         if (option.toLowerCase(Locale.US).equals(TRUE)) {
            depOptions.setGracefulProductionToAdmin(true);
         } else if (option.toLowerCase(Locale.US).equals(FALSE)) {
            depOptions.setGracefulProductionToAdmin(false);
         }
      }

      option = pyDic.get(IS_LIBRARY_MODULE).toString();
      if (option != null && !option.equals(NONE)) {
         if (option.toLowerCase(Locale.US).equals(TRUE)) {
            depOptions.setLibrary(true);
         } else if (option.toLowerCase(Locale.US).equals(FALSE)) {
            depOptions.setLibrary(false);
         }
      }

      option = pyDic.get(RETIRE_GRACEFULLY).toString();
      if (option != null && !option.equals(NONE)) {
         if (option.toLowerCase(Locale.US).equals(TRUE)) {
            depOptions.setRetireGracefully(true);
         } else if (option.toLowerCase(Locale.US).equals(FALSE)) {
            depOptions.setRetireGracefully(false);
         }
      }

      option = pyDic.get(RETIRE_TIMEOUT).toString();
      if (option != null && !option.equals(NONE)) {
         depOptions.setRetireTime(Integer.parseInt(option));
      }

      option = pyDic.get(RMI_GRACE_PERIOD).toString();
      if (option != null && !option.equals(NONE)) {
         depOptions.setRMIGracePeriodSecs(Integer.parseInt(option));
      }

      option = pyDic.get(SECURITY_MODEL).toString();
      if (option != null && !option.equals(NONE)) {
         depOptions.setSecurityModel(option);
      }

      option = pyDic.get(SECURITY_VALIDATION_ENABLED).toString();
      if (option != null && !option.equals(NONE)) {
         if (option.toLowerCase(Locale.US).equals(TRUE)) {
            depOptions.setSecurityValidationEnabled(true);
         } else if (option.toLowerCase(Locale.US).equals(FALSE)) {
            depOptions.setSecurityValidationEnabled(false);
         }
      }

      option = pyDic.get(STAGE_MODE).toString();
      if (option != null && !option.equals(NONE)) {
         depOptions.setStageMode(option);
      }

      option = pyDic.get(TEST_MODE).toString();
      if (option != null && !option.equals(NONE)) {
         if (option.toLowerCase(Locale.US).equals(TRUE)) {
            depOptions.setTestMode(true);
         } else if (option.toLowerCase(Locale.US).equals(FALSE)) {
            depOptions.setTestMode(false);
         }
      }

      option = pyDic.get(ARCHIVE_VERSION).toString();
      if (option != null && !option.equals(NONE)) {
         depOptions.setArchiveVersion(option);
      }

      option = pyDic.get(PLAN_VERSION).toString();
      if (option != null && !option.equals(NONE)) {
         depOptions.setPlanVersion(option);
      }

      option = pyDic.get(LIBRARY_SPEC_VERSION).toString();
      if (option != null && !option.equals(NONE)) {
         depOptions.setLibSpecVersion(option);
      }

      option = pyDic.get(LIBRARY_IMPL_VERSION).toString();
      if (option != null && !option.equals(NONE)) {
         depOptions.setLibImplVersion(option);
      }

      option = pyDic.get(ALT_DD).toString();
      if (option != null && !option.equals(NONE)) {
         depOptions.setAltDD(option);
      }

      option = pyDic.get(ALT_WLS_DD).toString();
      if (option != null && !option.equals(NONE)) {
         depOptions.setAltWlsDD(option);
      }

      option = pyDic.get(VERSION_IDENTIFIER).toString();
      if (option != null && !option.equals(NONE)) {
         depOptions.setVersionIdentifier(option);
      }

      option = pyDic.get(FORCE_UNDEPLOYMENT_TIMEOUT).toString();
      if (option != null && !option.equals(NONE)) {
         depOptions.setForceUndeployTimeout(Long.parseLong(option));
      }

      option = pyDic.get(DEFAULT_SUBMODULE_TARGETS).toString();
      if (option != null && !option.equals(NONE)) {
         if (option.toLowerCase(Locale.US).equals(TRUE)) {
            depOptions.setDefaultSubmoduleTargets(true);
         } else if (option.toLowerCase(Locale.US).equals(FALSE)) {
            depOptions.setDefaultSubmoduleTargets(false);
         }
      }

      option = pyDic.get(DEPLOYMENT_PRINCIPAL_NAME).toString();
      if (option != null && !option.equals(NONE)) {
         depOptions.setDeploymentPrincipalName(option);
      }

      option = pyDic.get(REMOTE).toString();
      if (TRUE.equalsIgnoreCase(option)) {
         depOptions.setRemote(true);
      }

      PyObject deploymentOrder = pyDic.get(DEPLOYMENT_ORDER);
      if (deploymentOrder instanceof PyInteger) {
         depOptions.setDeploymentOrder(((PyInteger)deploymentOrder).getValue());
      }

      this.checkForUnrecognizedOptions(pyDic);
      return depOptions;
   }
}
