package org.jboss.arquillian.jbehave.example.stories;

import com.google.common.util.concurrent.MoreExecutors;

import de.codecentric.jbehave.junit.monitoring.JUnitReportingRunner;

public abstract class AbstractArquillianStory extends SingleStory {

	public AbstractArquillianStory() {
		super();
		/*
		 * Configure JBehave to use the Guava SameThreadExecutorService. This
		 * enables the ArquillianInstanceStepsFactory to access the ThreadLocal
		 * contexts and datastores.
		 */
		try {
			this.configuredEmbedder().useExecutorService(MoreExecutors.sameThreadExecutor());
			JUnitReportingRunner.recommandedControls(this.configuredEmbedder());
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}

}
