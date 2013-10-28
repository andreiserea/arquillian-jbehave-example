package org.jboss.arquillian.jbehave.example.stories;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;

public final class ArquillianJbehaveCodeLocations {

	@SuppressWarnings("all")
	public static URL codeLocationFromClass(final Class<?> codeLocationClass) {
		return locateJBossDeploymentClassesFolder(codeLocationClass);
	}
	
	public static URL locateJBossDeploymentClassesFolder(final Class<?> codeLocationClass) {
    	String relativeClasspath = codeLocationClass.getCanonicalName().replace(".", "/") + ".class";
    	try {
	    	final URI classResource = Thread.currentThread().getContextClassLoader()
	                .getResource(relativeClasspath).toURI();
            final VirtualFile vf = VFS.getChild(classResource);
            Path absPath = Paths.get(vf.getPhysicalFile().toURI());
            Path relPath = Paths.get(relativeClasspath);
            if (absPath.endsWith(relPath)) {
            	return absPath.subpath(0, absPath.getNameCount() - relPath.getNameCount()).toUri().toURL();
            }
        } catch (final URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    	return null;
    }
}
