import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Manifest;

public class ModularClassLoader extends URLClassLoader {

	public ModularClassLoader(String name, URL[] urls, ClassLoader parent) {
		super(name, urls, parent);
		// TODO Auto-generated constructor stub
		System.out.println("custom class loader started");
		var man = new Manifest();
		super.definePackage("domain", man, urls[0]);
	}
	
	public Class<?> findClass(String className) throws ClassNotFoundException {
		System.out.println("Attempting to load custom " + className);
		throw new ClassNotFoundException();
	}

}
