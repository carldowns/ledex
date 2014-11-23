package app;


import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CatHealth extends HealthCheck {
    private final String template;

    @Inject
    public CatHealth(CatConfiguration config) {
        this.template = config.getTemplate();
    }

    @Override
    protected Result check() throws Exception {
        final String saying = String.format(template, "TEST");
        if (!saying.contains("TEST")) {
            return Result.unhealthy("template doesn't include a name");
        }
        return Result.healthy();
    }
}