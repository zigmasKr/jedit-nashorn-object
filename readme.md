<h1>NashornObject plugin</h1>

<h2>Overview</h2>
<p>The NashornObject plugin is a demo plugin showing how to render a Nashorn script
into a jEdit plugin. The principal functionality of the plugin is programmed in some
Nashorn script which evaluates to some object with its methods. Then, in the plugin's code,
the Nashorn script object is casted to ScriptObjectMirror type and
then callMember is applied.</p>

<h2>Installation of the plugin</h2>
<p>The JAR file NashornObject.jar should be copied the usual place of jEdit's
plugins. This place can be [jEdit application directory]/jars or
[jEdit settings directory]/jars. The accompanying Nashorn script
demo_script_object.js
is found in the repository subfolder /scripts. This script should be copied into
[jEdit settings directory]/scripts. This should be enough to start the plugin.
</p>

<h2>Configuring the plugin</h2>
<p>The plugin can be configured via the plugin options. The object in the plugin's accompanying Nashorn
script has two methods: demoBorderPanel and demoScrollerPanel. In the options panel,
each of them can be chosen to be invoked. The behaviour of the method demoScrollerPanel depends
on the presence of the argument. In the options panel, another place of the script
can be selected.
</p>

<h2>Extending the plugin</h2>
<p>To extend or change the functionality of the plugin, one has to include other (useful)
methods of the object in the script nashorn_object_demo.js, or write another (useful) Nashorn script,
and then to configure the plugin in the options panel accordingly.
</p>
