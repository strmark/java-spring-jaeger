/**
 * Copyright 2018-2022 The OpenTracing Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.opentracing.contrib.java.spring.jaeger.starter.customizer;

import static org.assertj.core.api.Assertions.assertThat;

import io.jaegertracing.internal.JaegerSpanContext;
import io.opentracing.Tracer;
import io.opentracing.contrib.java.spring.jaeger.starter.AbstractTracerSpringTest;
import io.opentracing.contrib.java.spring.jaeger.starter.TracerBuilderCustomizer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;
import io.opentracing.propagation.TextMapAdapter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;


@TestPropertySource(
    properties = {
        "spring.main.banner-mode=off",
        "opentracing.jaeger.enable-b3-propagation=true"
    }
)
@Import(JaegerTracerB3CustomizerCustomSpringTest.TestConfiguration.class)
public class JaegerTracerB3CustomizerCustomSpringTest extends AbstractTracerSpringTest {

  @Autowired
  private Tracer tracer;

  public static class TestConfiguration {
    @Bean
    public TracerBuilderCustomizer myCustomizer() {
      // Noop
      return builder -> {
      };
    }
  }

  @Test
  public void testCustomizersHttpHeadersShouldContainB3() {
    TextMap textMap = createTextMap();

    JaegerSpanContext context = (JaegerSpanContext) tracer.extract(Format.Builtin.HTTP_HEADERS, textMap);

    assertOnB3Headers(context);
  }

  @Test
  public void testCustomizersTextMapShouldContainB3() {
    TextMap textMap = createTextMap();

    JaegerSpanContext context = (JaegerSpanContext) tracer.extract(Format.Builtin.TEXT_MAP, textMap);

    assertOnB3Headers(context);
  }

  private TextMapAdapter createTextMap() {
    Map<String, String> carrier = new HashMap<>();
    carrier.put("X-B3-TraceId", "abc");
    carrier.put("X-B3-SpanId", "def");

    return new TextMapAdapter(carrier);
  }

  private void assertOnB3Headers(JaegerSpanContext context) {
    // Note: This test ensures that B3 codec actually works
    // If it would not, values would never be extracted from B3 headers and context will be null
    assertThat(context).isNotNull();
    assertThat(context.getTraceId()).isEqualTo("0000000000000abc");
    assertThat(context.getSpanId()).isEqualTo(3567L);
  }
}
