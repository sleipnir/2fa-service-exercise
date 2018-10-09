# 2fa-service-exercise
Create and validate TOTP: Time-Based One-Time Password Algorithm tokens - RFC6238

This application creates and sends a TOTP token to a phone number. Based on the consumption of an external API (https://notifye.io)

### Index

  - Dependencies
  - Build Application
  - Running in Windows, Linux, and Docker environments
  - Use application

#### Dependencies

  - Java 8
  - Maven
  - Docker (Optional)
  - NPM (Only for install the next dependency)
  - Local Tunnel (https://localtunnel.github.io/www/) (Optional, for webhooks only)

#### Build Application

First of all you should clone this repository:
```sh
# clone
$ git clone https://github.com/sleipnir/2fa-service-exercise.git

# chance to directory
$ cd 2fa-service-exercise

# compile code
$ mvn install

# Build docker container image
docker build -t com.creativesource/2fa-service .

# if all tests passed then running....
```

### Running in Windows, Linux, and Docker environments

Windows:
```sh
# install localtunnel and running for catch the webhooks events
C:\Windows\system32> npm install -g localtunnel

C:\Windows\system32> lt --port 8080
your url is: https://lazy-otter-12.localtunnel.me

# And in other terminal and if you wish to see the logs of webhooks then you should set the external access url in an environment variable
# Windows CMD Prompt (not powershell !)

C:\Windows\system32> set HOOK_URL "https://lazy-otter-12.localtunnel.me/auth/tokens/events"
PS C:\Windows\system32> echo %HOOK_URL%
https://lazy-otter-12.localtunnel.me/auth/tokens/events

# Enter in application direcotry
C:\Users\adriano.pereira\pocs\2fa-service-exercise> cd 2fa-service

# Running application
C:\Users\adriano.pereira\pocs\2fa-service-exercise\2fa-service> java -Xms256m -Xmx256m -XX:+UseG1GC -XX:+UseStringDeduplication -jar target/2fa-service-0.0.1-SNAPSHOT.jar
# Logs looks like this...

Two Factor Authentication Service

2018-10-09 12:16:51.548  INFO 30140 --- [           main] c.c.twofactor.service.Application        : Starting Application v0.0.1-SNAPSHOT on adriano-prog with PID 30140 (C:\Users\adriano.pereira\app\development\workspaces\notifye\pocs\2fa-service-exercise\2fa-service\target\2fa-service-0.0.1-SNAPSHOT.jar started by adriano.pereira in C:\Users\adriano.pereira\app\development\workspaces\notifye\pocs\2fa-service-exercise\2fa-service)
2018-10-09 12:16:51.587 DEBUG 30140 --- [           main] c.c.twofactor.service.Application        : Running with Spring Boot v2.0.5.RELEASE, Spring v5.0.9.RELEASE
2018-10-09 12:16:51.591  INFO 30140 --- [           main] c.c.twofactor.service.Application        : No active profile set, falling back to default profiles: default
2018-10-09 12:16:51.711  INFO 30140 --- [           main] onfigReactiveWebServerApplicationContext : Refreshing org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext@4c75cab9: startup date [Tue Oct 09 12:16:51 BRT 2018]; root of context hierarchy
2018-10-09 12:16:53.778  WARN 30140 --- [           main] o.s.security.core.userdetails.User       : User.withDefaultPasswordEncoder() is considered unsafe for production and is only intended for sample applications.
2018-10-09 12:16:55.050  INFO 30140 --- [           main] s.w.r.r.m.a.RequestMappingHandlerMapping : Mapped "{[/auth/tokens],methods=[POST]}" onto public reactor.core.publisher.Mono<org.springframework.http.ResponseEntity<com.creativesource.twofactor.service.model.TokenResponse>> com.creativesource.twofactor.service.web.TokenController.createToken(com.creativesource.twofactor.service.model.TokenRequest)
2018-10-09 12:16:55.054  INFO 30140 --- [           main] s.w.r.r.m.a.RequestMappingHandlerMapping : Mapped "{[/auth/tokens],methods=[GET]}" onto public reactor.core.publisher.Mono<org.springframework.http.ResponseEntity<com.creativesource.twofactor.service.model.TokenStatus>> com.creativesource.twofactor.service.web.TokenController.validateToken(java.lang.String)
2018-10-09 12:16:55.057  INFO 30140 --- [           main] s.w.r.r.m.a.RequestMappingHandlerMapping : Mapped "{[/auth/tokens/events],methods=[POST]}" onto public reactor.core.publisher.Mono<org.springframework.http.ResponseEntity<java.lang.Void>> com.creativesource.twofactor.service.web.WebHookController.event(java.lang.String)
2018-10-09 12:16:55.231  INFO 30140 --- [           main] o.s.w.r.handler.SimpleUrlHandlerMapping  : Mapped URL path [/webjars/**] onto handler of type [class org.springframework.web.reactive.resource.ResourceWebHandler]
2018-10-09 12:16:55.232  INFO 30140 --- [           main] o.s.w.r.handler.SimpleUrlHandlerMapping  : Mapped URL path [/**] onto handler of type [class org.springframework.web.reactive.resource.ResourceWebHandler]
2018-10-09 12:16:55.390  INFO 30140 --- [           main] o.s.w.r.r.m.a.ControllerMethodResolver   : Looking for @ControllerAdvice: org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext@4c75cab9: startup date [Tue Oct 09 12:16:51 BRT 2018]; root of context hierarchy
2018-10-09 12:16:56.403  INFO 30140 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
2018-10-09 12:16:58.117  INFO 30140 --- [ctor-http-nio-1] r.ipc.netty.tcp.BlockingNettyContext     : Started HttpServer on /0:0:0:0:0:0:0:0:8080
2018-10-09 12:16:58.118  INFO 30140 --- [           main] o.s.b.web.embedded.netty.NettyWebServer  : Netty started on port(s): 8080
2018-10-09 12:16:58.126  INFO 30140 --- [           main] c.c.twofactor.service.Application        : Started Application in 7.613 seconds (JVM running for 9.259)
2
``` 

Linux
export HOOK_URL="https://lazy-otter-12.localtunnel.me/auth/tokens/events"
echo $HOOK_URL

java -Xms256m -Xmx256m -XX:+UseG1GC -XX:+UseStringDeduplication -jar target/2fa-service-0.0.1-SNAPSHOT.jar

docker build -t com.creativesource/2fa-service .

docker run -d \
    -p 8080:8080
    --name 2fa-service \
    -e HOOK_URL=localtunnel_url_her \
	--memory=256m
   2fa-service:latest
   

/*
	 * Request Example:
	 * curl -X POST \
	 *	  http://localhost:8080/auth/tokens \
	 *	  -H 'Authorization: Basic c2Vuc2VkaWE6c2Vuc2VkaWEqMTIz' \
	 *	  -H 'Content-Type: application/json' \
	 *	  -d '{
	 *		"countryCode" : "+55",
	 *		"areaCode" : "11",
	 *		"phoneNumber" : "959734939",
	 *		"tokenTTL" : 300
	 *	}'
	 * */
	 
	 
/*
	 * curl -X GET http://localhost:8080/auth/tokens?code=569868 -H 'Authorization: Basic c2Vuc2VkaWE6c2Vuc2VkaWEqMTIz'
	 * */
	