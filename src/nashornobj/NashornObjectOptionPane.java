
package nashornobj;
/**
 * @author Zigmantas Kryzius
 */
//{{{ Imports
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.*;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.gjt.sp.jedit.AbstractOptionPane;
import org.gjt.sp.jedit.browser.VFSBrowser;
import org.gjt.sp.jedit.browser.VFSFileChooserDialog;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.gui.*;
//}}}
public class NashornObjectOptionPane extends AbstractOptionPane {

	private HistoryTextField scriptPath, methodName, methodArg, dockableTitle;

	public NashornObjectOptionPane() {
		super("nashorn-object");
	}

	protected void _init() {

		// component A
		scriptPath = new HistoryTextField("nashorn.object.path");
		scriptPath.getModel().setMax(6);
		scriptPath.setText(jEdit.getProperty("options.nashorn-object.script-path"));
		JButton browse = new JButton(jEdit.getProperty("vfs.browser.browse.label"));
		browse.addActionListener(new BrowseHandler());

		JPanel componentA = new JPanel();
		componentA.setLayout(new BoxLayout(componentA, BoxLayout.X_AXIS));
		componentA.add(scriptPath);
		componentA.add(browse);
		addComponent("Script path:", componentA);

		// components B, C
		methodName = new HistoryTextField("nashorn.object.method");
		methodName.getModel().setMax(6);
		methodName.setText(jEdit.getProperty("options.nashorn-object.method-name"));
		addComponent("Method name:", methodName);
		methodArg = new HistoryTextField("nashorn.object.arg");
		methodArg.getModel().setMax(6);
		methodArg.setText(jEdit.getProperty("options.nashorn-object.method-arg"));
		addComponent("Method arg:", methodArg);

		// component D
		dockableTitle = new HistoryTextField("nashorn.object.dockable");
		dockableTitle.getModel().setMax(6);
		dockableTitle.setText(jEdit.getProperty("options.nashorn-object.dockable-title"));
		JButton defaultDockableTitle = new JButton("Set default title");
		defaultDockableTitle.addActionListener(new DefaultTitleHandler());
		defaultDockableTitle.setToolTipText("Default dockable title is: Nashorn Object");

		JPanel componentD = new JPanel();
		componentD.setLayout(new BoxLayout(componentD, BoxLayout.X_AXIS));
		componentD.add(dockableTitle);
		componentD.add(defaultDockableTitle);
		addComponent("Dockable title:", componentD);
	}

	protected void _save() {
		jEdit.setProperty("options.nashorn-object.script-path", scriptPath.getText());
		scriptPath.addCurrentToHistory();
		jEdit.setProperty("options.nashorn-object.method-name", methodName.getText());
		methodName.addCurrentToHistory();
		jEdit.setProperty("options.nashorn-object.method-arg", methodArg.getText());
		methodArg.addCurrentToHistory();
		jEdit.setProperty("options.nashorn-object.dockable-title", dockableTitle.getText());
		dockableTitle.addCurrentToHistory();
	}

	class BrowseHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			VFSFileChooserDialog dialog = new VFSFileChooserDialog(
				jEdit.getActiveView(),
				System.getProperty("user.dir") + FileSystems.getDefault().getSeparator(),
				VFSBrowser.OPEN_DIALOG, false, true);
			String[] files = dialog.getSelectedFiles();
			if (files != null) {
				scriptPath.setText(files[0]);
			}
		}
	}

	class DefaultTitleHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dockableTitle.setText("");
			jEdit.setProperty("nashornobject.title", "Nashorn Object");
		}
	}

}
