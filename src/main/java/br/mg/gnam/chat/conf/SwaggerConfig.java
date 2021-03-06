package br.mg.gnam.chat.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>Classe de configuração para habilitar swagger.</p>
 * <p>Esta classe permite ao sistema exibir a documetação dos web-services cadastradas no sistema.</p>
 * 
 * @author rafael.altagnam
 * @since 10/02/2019
 * @version 1.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket greetingApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("br.mg.gnam.chat")).build().apiInfo(metaData());

	}

	private ApiInfo metaData() {
		return new ApiInfoBuilder().title("Spring Boot REST API")
				.description("\"Spring Boot REST API for greeting people\"").version("1.0.0")
				.license("Apache License Version 2.0").licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")
				.build();
	}

}