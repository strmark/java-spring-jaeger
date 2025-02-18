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
package io.opentracing.contrib.java.spring.jaeger.starter;

import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
        JaegerAutoConfiguration.class
})
public abstract class AbstractTracerSpringTest {

    @Autowired(required = false)
    protected Tracer tracer;

    protected io.jaegertracing.internal.JaegerTracer getTracer() {
        return (io.jaegertracing.internal.JaegerTracer) tracer;
    }
}
