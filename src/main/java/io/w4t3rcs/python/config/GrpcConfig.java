package io.w4t3rcs.python.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.ManagedChannel;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.executor.impl.GrpcPythonExecutor;
import io.w4t3rcs.python.proto.PythonServiceGrpc;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

/**
 * Configuration class for gRPC integration.
 * This class creates a gRPC client that enables communication between Java and Python.
 *
 * <p>The configuration is conditionally enabled based on the "spring.python.executor.type" property</p>
 */
@Configuration
@ConditionalOnProperty(name = "spring.python.executor.type", havingValue = "grpc")
public class GrpcConfig {
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";

    @Bean
    @ConditionalOnMissingBean(PythonExecutor.class)
    public PythonExecutor grpcPythonExecutor(PythonServiceGrpc.PythonServiceBlockingStub stub, ObjectMapper objectMapper) {
        return new GrpcPythonExecutor(stub, objectMapper);
    }

    @Bean
    public PythonServiceGrpc.PythonServiceBlockingStub stub(PythonExecutorProperties properties, GrpcChannelFactory channels) {
        PythonExecutorProperties.GrpcProperties grpcProperties = properties.grpc();
        ManagedChannel channel = channels.createChannel(grpcProperties.uri());
        Metadata headers = new Metadata();
        var marshaller = Metadata.ASCII_STRING_MARSHALLER;
        Metadata.Key<String> usernameKey = Metadata.Key.of(USERNAME_KEY, marshaller);
        Metadata.Key<String> passwordKey = Metadata.Key.of(PASSWORD_KEY, marshaller);
        headers.put(usernameKey, grpcProperties.username());
        headers.put(passwordKey, grpcProperties.password());
        return PythonServiceGrpc.newBlockingStub(channel)
                .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(headers));
    }
}