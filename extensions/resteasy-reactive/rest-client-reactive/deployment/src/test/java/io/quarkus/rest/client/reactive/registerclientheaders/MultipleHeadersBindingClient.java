package io.quarkus.rest.client.reactive.registerclientheaders;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.quarkus.rest.client.reactive.TestJacksonBasicMessageBodyReader;

@RegisterRestClient
@RegisterClientHeaders(MyHeadersFactory.class)
@ClientHeaderParam(name = "my-header", value = "constant-header-value")
@ClientHeaderParam(name = "computed-header", value = "{io.quarkus.rest.client.reactive.registerclientheaders.ComputedHeader.get}")
@RegisterProvider(TestJacksonBasicMessageBodyReader.class)
public interface MultipleHeadersBindingClient {
    @GET
    @Path("/describe-request")
    @ClientHeaderParam(name = "header-from-properties", value = "${header.value}")
    RequestData call(@HeaderParam("jaxrs-style-header") String headerValue);
}
