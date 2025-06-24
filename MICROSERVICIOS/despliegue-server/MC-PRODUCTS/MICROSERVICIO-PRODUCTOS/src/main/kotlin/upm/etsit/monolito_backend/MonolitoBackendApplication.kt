package upm.etsit.monolito_backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication



// HttpConfig.kt o cualquier clase con @Configuration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class HttpConfig {
    @Bean
    fun restClient(): RestClient = RestClient.create()
}



@SpringBootApplication
class MonolitoBackendApplication

fun main(args: Array<String>) {
    runApplication<MonolitoBackendApplication>(*args)
}
