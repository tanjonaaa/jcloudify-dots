package com.dots.app.conf;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.dots.app.PojaGenerated;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@PojaGenerated
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Slf4j
public class FacadeIT {

  @SneakyThrows
  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {

    new BucketConf().configureProperties(registry);
    new EmailConf().configureProperties(registry);

    registry.add("sentry.dsn", () -> "https://public@sentry.example.com/1");
    registry.add("sentry.environment", () -> "dummy");

    try {
      var envConfClazz = Class.forName("com.dots.app.conf.EnvConf");
      var envConfConfigureProperties =
          envConfClazz.getDeclaredMethod("configureProperties", DynamicPropertyRegistry.class);
      var envConf = envConfClazz.getConstructor().newInstance();
      envConfConfigureProperties.invoke(envConf, registry);
    } catch (ClassNotFoundException e) {
      log.warn("EnvConf missing: no project-specific test env vars will be set");
    }
  }
}
