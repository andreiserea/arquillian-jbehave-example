package org.jboss.arquillian.jbehave.example.stories;

import java.text.SimpleDateFormat;
import java.util.Properties;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.i18n.LocalizedKeywords;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.junit.JUnitStory;
import org.jbehave.core.model.ExamplesTableFactory;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.core.parsers.RegexStoryParser;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.ParameterConverters.DateConverter;
import org.jbehave.core.steps.ParameterConverters.ExamplesTableConverter;
import org.jboss.arquillian.jbehave.extensions.AnnotationStoryPathResolver;
import org.jboss.arquillian.jbehave.extensions.JBossUnderscoredCamelCaseResolver;


/**
 * <p>
 * Runs a single story via JUnit
 * </p>
 * <p>
 * Stories are specified in classpath and correspondingly the
 * {@link LoadFromClasspath} story loader is configured.
 * </p>
 */
public class SingleStory extends JUnitStory {

	private final static String REPORTS_DIR_PROPERTY = "jbehave.reports.dir";
	private final CrossReference xref = new CrossReference();

	public SingleStory() {
		try {
			this.configuredEmbedder().embedderControls().doGenerateViewAfterStories(true).doIgnoreFailureInStories(false)
			.doIgnoreFailureInView(true).useThreads(2).useStoryTimeoutInSecs(60);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
		// configuredEmbedder().useEmbedderControls(new
		// PropertyBasedEmbedderControls());
	}

	@Override
	public Configuration configuration() {
		final Class<?> embeddableClass = this.getClass().getSuperclass();
		final Properties viewResources = new Properties();
		viewResources.put("decorateNonHtml", "true");
		// Start from default ParameterConverters instance
		final ParameterConverters parameterConverters = new ParameterConverters();
		// factory to allow parameter conversion and loading from external
		// resources (used by StoryParser too)
		final ExamplesTableFactory examplesTableFactory = new ExamplesTableFactory(new LocalizedKeywords(),
				new LoadFromClasspath(embeddableClass), parameterConverters);
		// add custom converters
		parameterConverters.addConverters(new DateConverter(new SimpleDateFormat("yyyy-MM-dd")),
				new ExamplesTableConverter(examplesTableFactory));
		final StoryReporterBuilder storyReporterBuilder = new StoryReporterBuilder()
		.withCodeLocation(ArquillianJbehaveCodeLocations.codeLocationFromClass(embeddableClass))
		.withDefaultFormats()
		.withViewResources(viewResources).withFormats(Format.CONSOLE, Format.TXT, Format.HTML_TEMPLATE)
		.withFailureTrace(true).withFailureTraceCompression(true).withCrossReference(this.xref)
		.withRelativeDirectory(System.getProperty(SingleStory.REPORTS_DIR_PROPERTY));

		return new MostUsefulConfiguration()
		.useStoryLoader(new LoadFromClasspath(embeddableClass))
		.useStoryParser(new RegexStoryParser(examplesTableFactory))
		.useStoryPathResolver(
				new AnnotationStoryPathResolver().removeFromClassName("Story", "CopycatMerge"))
				.useStoryReporterBuilder(storyReporterBuilder)
				.useParameterConverters(parameterConverters)
				// use '%' instead of '$' to identify parameters
				.useStepPatternParser(new RegexPrefixCapturingPatternParser("%"))
				.useStepMonitor(this.xref.getStepMonitor());
	}

}