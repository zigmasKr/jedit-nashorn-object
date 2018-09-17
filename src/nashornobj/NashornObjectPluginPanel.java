
package nashornobj;

import java.io.*;
import java.nio.file.*;
import javax.script.*;

import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.gjt.sp.util.Log;
import org.gjt.sp.jedit.*;
import console.*;
import javascriptshell.*;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class NashornObjectPluginPanel extends JPanel {

	private String scriptNameDefault = "demo_script_object.js";
	private String methodDefault = "demoBorderPanel";
	private String argDefault = "";
	private String dockableTitleDefault = jEdit.getProperty("nashornobject.title");

	private String scriptPath;
	private String method;
	private String arg;
	private String dockableTitle;

	private class NashornObjectPanel
			extends JPanel
			implements WindowConstants {
		//
		private JavaScriptShell.RetVal scriptEval = null;
		private ScriptObjectMirror som = null;

		private NashornObjectPanel() {

			// script path
			String scriptRootDefault = jEdit.getSettingsDirectory() + FileSystems.getDefault().getSeparator() +
				"scripts";

			String pathDefault = scriptRootDefault + FileSystems.getDefault().getSeparator() +
				scriptNameDefault;
			String pathSelected = jEdit.getProperty("options.nashorn-object.script-path");

			if (pathSelected != null && !pathSelected.trim().isEmpty()) {
				scriptPath = pathSelected;
			}
			else {
				scriptPath = pathDefault;
				jEdit.setProperty("options.nashorn-object.script-path", pathDefault);
			}

			View currentView = jEdit.getActiveView();
			JPanel panel = null;
			boolean scriptFound = false;

			if (Files.exists(Paths.get(scriptPath), new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
				scriptFound = true;
			}
			else {
				Log.log(Log.DEBUG, NashornObjectPanel.class, "Script file |" + scriptPath + "| is NOT found");
				Macros.error(currentView, "Script file " + scriptPath + " is not found.\nPlease review the plugin options.\n");
			}

			// method name
			String methodSelected = jEdit.getProperty("options.nashorn-object.method-name");
			if (methodSelected != null && !methodSelected.trim().isEmpty()) {
				method = methodSelected;
			} else {
				method = methodDefault;
				jEdit.setProperty("options.nashorn-object.method-name", method);
			}

			// method arg
			String argSelected = jEdit.getProperty("options.nashorn-object.method-arg");
			if (argSelected != null) {
				arg = argSelected;
			} else {
				arg = argDefault;
				jEdit.setProperty("options.nashorn-object.method-arg", arg);
			}

			// dockable window title
			String dockableTitleSelected = jEdit.getProperty("options.nashorn-object.dockable-title");
			if (dockableTitleSelected != null && !dockableTitleSelected.trim().isEmpty()) {
				dockableTitle = dockableTitleSelected;
			} else {
				dockableTitle = dockableTitleDefault;
				jEdit.setProperty("options.nashorn-object.dockable-title", dockableTitle);
			}

			// getting to ScriptObjectMirror
			if (scriptFound) {
				try {
					FileReader scrFileReader = new FileReader(scriptPath);
					scriptEval = (JavaScriptShell.RetVal) JavaScriptShell.evaluateScript(currentView, scrFileReader, true);
					Log.log(Log.DEBUG, NashornObjectPanel.class, "scriptEval:        " + scriptEval.toString());
					Log.log(Log.DEBUG, NashornObjectPanel.class, "scriptEval.retVal: " + scriptEval.retVal.toString());
					if (!scriptEval.error) {
						// if no error occurrs
						som = (ScriptObjectMirror) scriptEval.retVal;
						panel = (JPanel) som.callMember(method, arg);
						add(panel);
					}
					else {
						Macros.error(currentView, "Script file " + scriptPath + ": error in JavaScriptShell.evaluateScript.");
					}
				}
				catch(FileNotFoundException fe)
				{
					Log.log(Log.ERROR, NashornObjectPanel.class, fe.toString());
					//errMessage = fe.toString();
					Macros.error(currentView, fe.toString());
					//new TextAreaDialog(view, "FileNotFoundException", fe);
				}
			}
		}

		/**
		* Sets the closeOperation attribute of this Panel object.
		* Borrowed from Calculator plugin (CalculatorPanel.java).
		*
		* @param operation  The new closeOperation value,
		* one of DISPOSE_ON_CLOSE, DO_NOTHING_ON_CLOSE, EXIT_ON_CLOSE, or HIDE_ON_CLOSE.
		*/
		private int close_operation = EXIT_ON_CLOSE;

		public void setCloseOperation(int operation) {
			switch (operation) {
				case DISPOSE_ON_CLOSE:
				case DO_NOTHING_ON_CLOSE:
				case EXIT_ON_CLOSE:
				case HIDE_ON_CLOSE:
					break;
				default:
					throw new IllegalArgumentException("Invalid close operation, see javax.swing.WindowConstants.");
			}
			close_operation = operation;
		}

		/**
		* @return   The closeOperation value,
		* one of DISPOSE_ON_CLOSE, DO_NOTHING_ON_CLOSE, EXIT_ON_CLOSE, or HIDE_ON_CLOSE.
		*/
		public int getCloseOperation() {
			return close_operation;
		}
	}

	public NashornObjectPluginPanel() {
		NashornObjectPanel nop = new NashornObjectPanel();
		nop.setCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		add(nop);
	}
}
