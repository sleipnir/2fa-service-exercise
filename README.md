# 2fa-service-exercise
Create and validate TOTP: Time-Based One-Time Password Algorithm tokens - RFC6238

This application creates and sends a TOTP token to a phone number. Based on the consumption of an external API (https://notifye.io)

### Index

  - Dependencies
  - Build Application
  - Running in Windows, Linux, and Docker environments
  - Use application

### Architectural Decisions

I have chosen not to implement the algorithm for generating tokens *TOTP* or *HOTP* directly. 
Since there are many libraries and services that offer this functionality to me. And since these algorithms are specified by the IETF via **RFCs 4226 and 6238** I decided to use an external API that implements these specifications 
and provides an abstraction layer that facilitates my work.
Since providers like Google Authenticator and Twillio Authy implement these specifications I believe they are adhering to the requested requirement.
In this case I chose to write a Java / Springboot Webflux application that bridges the server application to the authentication client 
and the tokens generation mechanism. 
This application implements the external API responsible for generating and sending the token via cell phone, as well as the validation of the generated token. 
This external API is called Notify-e (of which I am a founding member and therefore no cost to use. :-)).

The basic flow of interaction can be described as follows:

1. The end user requests the login in the application of our client.
2. The frontend application sends a POST to our API.
3. Our API sends a request to the external API informing the data of the end user (telephone), as well as the time of expiration of the token that we wish.
4. The external API generates the Token and sends it to the end user.
5 The end user receives the message containing the token code and informs on our client's website.
6. The site in turn sends us a GET request informing the code informed.
7. We consume the external API that tells us whether the code is valid or not.
8. We return the response to the caller.

I have defined that the application should provide some minimal security mechanism. And for simplicity I chose to work with a level of authentication 
and basic authorization.
For this reason, it is necessary to send in all requests, except for the Hook feature, the Authorization header with the Basic type and the encrypted user 
and password as defined in *RFC2617*.
The user and password will be informed in the contact email.

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
$ cd 2fa-service-exercise\2fa-service

# compile code
$ mvn install

# On Linux Build docker container image
$ cd ../; docker build -t com.creativesource/2fa-service .

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

# Enter in application directory
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

Linux:
```sh
# install localtunnel and running for catch the webhooks events
$ npm install -g localtunnel

$ lt --port 8080
your url is: https://lazy-otter-12.localtunnel.me

# And in other terminal and if you wish to see the logs of webhooks then you should set the external access url in an environment variable

$ export HOOK_URL="https://lazy-otter-12.localtunnel.me/auth/tokens/events"
$ echo $HOOK_URL
https://lazy-otter-12.localtunnel.me/auth/tokens/events

# Enter in application directory
$ cd 2fa-service

# Running application
$ java -Xms256m -Xmx256m -XX:+UseG1GC -XX:+UseStringDeduplication -jar target/2fa-service-0.0.1-SNAPSHOT.jar
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

Docker:
```sh
# On host install localtunnel and running for catch the webhooks events
$ npm install -g localtunnel

$ lt --port 8080
your url is: https://lazy-otter-12.localtunnel.me

# And another prompt instance running application. Don´t forget to set HOOK_URL with the correct URL (Or not passed -e parameter!This resource is optional)

$ docker run -d \
    -p 8080:8080 \
    --name 2fa-service \
    -e HOOK_URL=https://ancient-wasp-35.localtunnel.me/auth/tokens/events \
	--memory=512m \
   com.creativesource/2fa-service:latest
   
```

Logs looks like this...
```sh
$ docker logs -t -f 2fa-service

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

### Use application

You can test the application in two ways, via Postman or via Curl directly from the command line.
Import the 2fa-service-api.json file, which is located in the project root, into your Postman application if you wish to run via Postman Collections, or follow the examples below to test directly via CURL

Send Request for create TOTP Token and send This Token to the client via phone number:

```sh
$ curl -X POST \
  http://localhost:8080/auth/tokens \
  -H 'Authorization: Basic c2Vuc2VkaWE6c2Vuc2VkaWEqMTIz' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -d '{
	"countryCode" : "+55",
	"areaCode" : "11",
	"phoneNumber" : "959734939",
	"tokenTTL" : 300
}' 
```
**In this request we define that a valid Token must be created, set to expire in 5 minutes and sent to the client whose phone number we enter in Json's phoneNumber attribute.
Do not forget to enter a valid phone number that you have access to to receive the token.**

Send Request for validate the received token:
```sh
$ curl -X GET \
  'http://localhost:8080/auth/tokens?code=274008' \
  -H 'Authorization: Basic c2Vuc2VkaWE6c2Vuc2VkaWEqMTIz' \
  -H 'Cache-Control: no-cache'
```
**Do not forget to replace the query string 'code' with the value of the token received in your phone.**

### TODO

  - Implement circuit-breaker for cascade failure treatment.
  - Implement persistence and caching to avoid unnecessary network calls.
  - Include new validations.