package org.jboss.arquillian.jbehave.example.stories;

import java.io.InputStream;
import java.util.Collection;

import org.jbehave.core.steps.InjectableStepsFactory;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.jbehave.example.steps.ExampleTestSteps;
import org.jboss.arquillian.jbehave.extensions.annotations.StoryFile;
import org.jboss.arquillian.jbehave.injection.ArquillianInstanceStepsFactory;
import org.jboss.arquillian.junit.ArquillianJbehaveRunner;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;


@RunWith(ArquillianJbehaveRunner.class)
@StoryFile("example")
public class ExampleStory extends AbstractArquillianStory {

	public static ExampleTestSteps steps = new ExampleTestSteps();
	
	@Deployment
	public static WebArchive createDeployement() {
		
		final WebArchive deploymentArchive = ShrinkWrap.create(WebArchive.class, "test.war");
		
		/* Add test classes */
		deploymentArchive.addPackages(true, "org.jboss.arquillian.jbehave.example");
		deploymentArchive.addClass("org.jboss.arquillian.jbehave.example.stories.ExampleStoryCopycatMerge");

		Collection<JavaArchive> extraLibs = DependencyResolvers
			.use(MavenDependencyResolver.class).goOffline()
			.loadMetadataFromPom("pom.xml")
			.artifact("org.jboss.arquillian:arquillian-jbehave:jar:0.0.1-SNAPSHOT")
			.resolveAs(JavaArchive.class);
		
		/* add extra libraries */
		for (final JavaArchive library : extraLibs) {
			deploymentArchive.addAsLibraries(library);
		}
		
		deploymentArchive.addAsWebInfResource(new Asset() {
			
			public InputStream openStream() {
				return Thread.currentThread().getContextClassLoader().getResourceAsStream("test-jboss-deployment-structure.xml");
			}
		}, "jboss-deployment-structure.xml").addAsResource("stories");

		System.out.println(deploymentArchive.toString(true));
		return deploymentArchive;
	}

	@Override
	public InjectableStepsFactory stepsFactory() {
		return new ArquillianInstanceStepsFactory(this.configuration(), steps);
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

}
