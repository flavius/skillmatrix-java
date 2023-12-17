
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class Bootstrap {
    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println("Hello bootstrap");
        for(var arg: args) {
        	System.out.println(arg);
        }
        var mainclsname = args[0];
        var moduleLayer = ModuleLayer.boot();
        var conf = moduleLayer.configuration().toString();
        for(var mod: moduleLayer.modules()) {
            var clsloader = mod.getClassLoader();
            if(clsloader == null) {
                continue;
            }
            //var pkg = mod.getClassLoader().getDefinedPackages()[0];
            //var layer = mod.getLayer().toString();
            var modname = mod.getName();
            if(modname.startsWith("jdk.") || modname.startsWith("java.")) {
            	continue;
            }
            System.out.println(String.format("loader %s mod %s", clsloader, modname));
        }
        Object o = new Bootstrap();
        var pd = o.getClass().getProtectionDomain();
        var loc = pd.getCodeSource().getLocation();
        var loc_domain = new URL(loc.toString() + "/domain");
        var urls = new URL[1];
        urls[0] = loc_domain;
        var loader = new ModularClassLoader("domain", urls, null);
        try {
        	var maincls = loader.loadClass(mainclsname);
        	System.out.println("main class found: " + maincls.getName());
        } catch (ClassNotFoundException ex) {
        	System.err.println("main class not found " + mainclsname);
        }
        var clsldr2 = o.getClass().getClassLoader();
        System.out.println(clsldr2.getClass().getName());
        //System.out.println(loc_domain);
        //var clsldr = o.getClass().getClassLoader();
        //clsldr.getParent();
        //System.out.println(clsldr);
        //var jarp = Path.of(loc.toURI());
        //var jar = new JarFile(jarp.toFile());
        //var entries = jar.entries();
        //while(entries.hasMoreElements()) {
        //    ZipEntry current = entries.nextElement();
        //    System.out.println(String.format("zip entry: %s", current.getName()));
        //}
        //System.out.println(String.format("loc: %s", loc.getPath()));
    }
}
