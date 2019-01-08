package cn.cloudbot.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@CrossOrigin
@SpringBootApplication
@EnableDiscoveryClient
@EnableAutoConfiguration
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	private String BotManagerAddress;

	private String ServiceManagerAddress;

	@Autowired
	private DiscoveryClient client;

	@Bean
	public RouterFunction<ServerResponse> testFunRouterFunction() {
		RouterFunction<ServerResponse> route = RouterFunctions.route(
				RequestPredicates.path("/testfun"),
				request -> ServerResponse.ok().body(BodyInserters.fromObject("hello")));
		return route;
	}

	@Bean
	public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
		// @formatter:off
		return builder.routes()
				.route(r -> r.path("/test/**")
//						.filters(f -> f.filter(new RequestTimeFilter())
//								.addResponseHeader("X-Response-Default-Foo", "Default-Bar"))
						.uri("http://localhost:8101/test/ip")

				).route(r-> r.path("/robots/**").uri("http://bot-manager:8101/robots"))
				.route(r->r.path("/groups/**").uri("http://service-manager:8102/groups"))
				.route(r->r.path("/services/**").uri("http://service-manager:8102/services"))
				.build();
		// @formatter:on
	}

}

