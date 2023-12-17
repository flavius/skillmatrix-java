
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class Bootstrap {
    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println("Hello bootstrap");
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
        var clsldr = o.getClass().getClassLoader();
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
