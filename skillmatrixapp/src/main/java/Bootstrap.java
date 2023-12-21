
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.module.ModuleFinder;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class Bootstrap {
    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println("Hello bootstrap");
        for (var arg : args) {
            System.out.println(arg);
        }
        if (args.length < 1) {
            System.err.println("no main class provided");
            System.exit(1);
        }
        var mainclsname = args[0];
        var mf = ModuleFinder.of(Path.of("file://home/flav/learn/gradle/skillmatrix/skillmatrixapp/build/libs/skillmatrixapp.jar/"));
        for (var mod : mf.findAll()) {
            System.out.println("mod in jar: " + mod);
        }
        var bootLayer = ModuleLayer.boot();
        //System.out.println("boot layer " + bootLayer.toString());
        var conf = bootLayer.configuration().toString();
        for (var mod : bootLayer.modules()) {
            var clsloader = mod.getClassLoader();
            if (clsloader == null) {
                continue;
            }
            //var pkg = mod.getClassLoader().getDefinedPackages()[0];
            //var layer = mod.getLayer().toString();
            var modname = mod.getName();
            if (modname.startsWith("jdk.") || modname.startsWith("java.")) {
                continue;
            }
            System.out.println(String.format("loader %s mod %s", clsloader, modname));
        }
        Object o = new Bootstrap();
        var pd = o.getClass().getProtectionDomain();
        var loc = pd.getCodeSource().getLocation();
        var loc_domain = new URL(loc.toString() + "/domain/");
        var urls = new URL[1];
        urls[0] = loc_domain;
        System.out.println("domain model jar url " + loc_domain);
        var loader = new ModularClassLoader("domain", urls, java.lang.ClassLoader.getSystemClassLoader());
        for (Package pkg : loader.getDefinedPackages()) {
            System.out.println("pkg in domain: " + pkg.getName());
            var pkgName = pkg.getName();
            InputStream stream = loader.getResourceAsStream(pkgName);
            var reader = new BufferedReader(new InputStreamReader(stream));
            var lines = reader.lines().collect(Collectors.toList());
            for (var line : lines) {
                System.out.println("loader resource: " + line);
            }
        }
        try {
            var maincls = loader.loadClass(mainclsname);
            System.out.println("main class found: " + maincls.getName());
        } catch (ClassNotFoundException ex) {
            System.err.println("main class not found " + mainclsname);
            System.exit(1);
        }
    }
}
