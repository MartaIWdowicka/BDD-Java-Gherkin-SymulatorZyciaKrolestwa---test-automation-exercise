package BDDStoryExercise.acceptace.framework;

import org.jbehave.core.ConfigurableEmbedder;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.LoadFromURL;
import org.jbehave.core.reporters.FilePrintStreamFactory;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.core.reporters.Format.HTML;
/**
 * A class to fully encapsulate all of the JBehave plumbing behind a builder style API. The expected usage for this
 * would be: {@code aBehaviouralTestRunner().usingStepsFrom(this).withStory("your.story").run()}
 */
public final class BehaviourTestEmbedder extends ConfigurableEmbedder {

    private static final Logger LOG = LoggerFactory.getLogger(BehaviourTestEmbedder.class);
    public static final String BAD_USE_OF_API_MESSAGE = "You are trying to set the steps factory twice...";

    private String wildcardStoryFilename;
    private InjectableStepsFactory stepsFactory;

    private BehaviourTestEmbedder() {
    }

    public static BehaviourTestEmbedder aBehaviouralTestRunner() {
        return new BehaviourTestEmbedder();
    }

    @Override
    public void run() throws Exception {
        List<String> paths = createStoryPaths();
        if (paths == null || paths.isEmpty()) {
            throw new IllegalStateException("No story paths found for state machine");
        }
        LOG.debug("Running {} with spring_stories {}", getClass().getSimpleName(), paths);
        configuredEmbedder().runStoriesAsPaths(paths);
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        assertThat(stepsFactory).isNotNull();
        return stepsFactory;
    }

    @Override
    public Configuration configuration() {
        return new MostUsefulConfiguration()
                .useStoryLoader(new LoadFromURL())
                .useStoryReporterBuilder(new SandboxStoryReporterBuilder());
    }

    private List<String> createStoryPaths() {
        return ClasspathStoryFinder.findFileNamesThatMatch(wildcardStoryFilename);
    }

    public BehaviourTestEmbedder withStory(String aWildcardStoryFilename) {
        wildcardStoryFilename = aWildcardStoryFilename;
        return this;
    }

    public BehaviourTestEmbedder usingStepsFrom(Object... stepsSource) {
        assertThat(stepsFactory).isNull();
        stepsFactory = new InstanceStepsFactory(configuration(), stepsSource);
        return this;
    }

    private static class SandboxStoryReporterBuilder extends StoryReporterBuilder {
        SandboxStoryReporterBuilder() {
            withCodeLocation(codeLocationFromClass(SandboxStoryReporterBuilder.class));
            withDefaultFormats();
            withFormats(HTML, CONSOLE);
            withFailureTrace(true);
            withPathResolver(new FilePrintStreamFactory.ResolveToSimpleName());
        }
    }
}
