## spring-boot-read-http-request-header-write-http-response-header

Purpose : Read http request header sent from client to the server into ThreadLocal as current user context data. <br/>
Reason : Return http response header back to the current HttpServletResponse.  <br/>

Filter, Aspect, Controller and HandlerInterceptor execution order :
<pre>
                                                            Client HTTP Request ( POSTMAN  )
                                                                        &darr;
                    org.springframework.web.filter.OncePerRequestFilter (com.company.customerinfo.filter.PerRequestFilter) doFilterInternal
                                                                        &darr;
                                           javax.servlet.Filter (com.company.customerinfo.filter.RequestFilter) doFilter
                                                                        &darr;
                                           javax.servlet.Filter (com.company.customerinfo.filter.ResponseFilter) doFilter
                                                                        &darr;    
                                    org.aspectj.lang.annotation.Aspect (com.company.customerinfo.aspect.RequestAspect) beforeAdvice
                                                                        &darr;
                org.springframework.web.bind.annotation.RestController (com.company.customerinfo.controller) save(@RequestBody Customer customer)
                                                                        &darr;
                    org.springframework.web.servlet.HandlerInterceptor (com.company.customerinfo.interceptor) afterCompletion
</pre>

### Local run steps <br/>
1- Add "transaction-id" as http request header from POSTMAN request. <br/>
2- Start Spring Boot REST API by running main method containing class CustomerInfoApplication.java in your IDE. <br/>
3- Alternatively you can start your Docker container by following the commands below. <br/>
NOT : Execute maven command from where the pom.xml is located in the project directory to create Spring Boot executable jar. <br/>
<pre> 
$ mvn clean install -U -X <br/>
$ mvn spring-boot:run <br/>
</pre>

swagger_ui can be accessed via https secure port 8443 from localhost : <br/>
https://localhost:8443/customer-info/swagger-ui/index.html <br/><br/>
![https_swagger_ui](doc/https_swagger_ui.png) <br/>

### Tech Stack
<pre>
Java 11
H2 Database Engine
spring boot
spring boot starter data jpa
spring boot starter web
spring boot starter test
spring boot starter aop
spring boot starter actuator
spring security web
springdoc openapi ui
springfox swagger ui
hibernate
logback
maven
hikari connection pool
Docker
</pre>
### Docker build run steps
NOT : Execute docker commands from where the DockerFile is located. <br/>
NOT : Tested on Windows 10 with Docker Desktop Engine Version : 20.10.11 <br/>
<pre>
$ docker system prune -a --volumes <br/>
$ docker build . --tag demo  <br/>
$ docker images <br/>
  REPOSITORY   TAG       IMAGE ID       CREATED         SIZE <br/>
  demo         latest    9d4a0ec3294e   6 minutes ago   288MB <br/>
$ docker run -p 8443:8443 -e "SPRING_PROFILES_ACTIVE=dev" demo:latest <br/>
</pre>

## API OPERATIONS
### Save a new customer to database

Method : HTTP.POST <br/>
URL : https://localhost:8443/customer-info/customer/save <br/>
HTTP Request Body : <br/>
<pre>
{
    "name": "name1",
    "age": 1,
    "shippingAddress": {
        "address": {
            "streetName": "software",
            "city": "ankara",
            "country": "TR"
        }
    }
}
</pre>
HTTP Request Headers : <br/>
<pre>
transaction-id: 123-123-123
Content-Type: application/json
User-Agent: PostmanRuntime/7.28.4
Accept: */*
Postman-Token: aee7b189-8004-4fd7-a050-bd4e09bba6bd
Host: localhost:8443
Accept-Encoding: gzip, deflate, br
Connection: keep-alive
Content-Length: 206
</pre>

Curl Request : <br/>
<pre>
curl --location --request POST 'https://localhost:8443/customer-info/customer/save' \
--header 'transaction-id: 123-123-123' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "name1",
    "age": 1,
    "shippingAddress": {
        "address": {
            "streetName": "software",
            "city": "ankara",
            "country": "TR"
        }
    }
}'
</pre><br/>

Response : 

HTTP response code 200 <br/>
<pre>
{
    "id": 1,
    "name": "name1",
    "age": 1,
    "shippingAddress": {
        "id": 1,
        "address": {
            "id": 1,
            "streetName": "software",
            "city": "ankara",
            "country": "TR"
        }
    }
}
</pre>

HTTP Response Headers : </br>
<pre>
request-id: 68182bbf-996d-4732-a6ff-2c49a90012d1
correlation-id: 68182bbf-996d-4732-a6ff-2c49a90012d1
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
</pre>

### List all customers saved to database

Method : HTTP.GET <br/>
URL : https://localhost:8443/customer-info/customer/list <br/>
Request Body : <br/>
<pre>
{}
</pre>
Curl Request : <br/>
<pre>
curl --location --request GET 'https://localhost:8443/customer-info/customer/list' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=5E6B21C9533643F4A7EE462DCBB3B312' \
--data-raw '{}'
</pre>
<br/>

Response : 

HTTP response code 200 <br/>
<pre>
[
    {
        "id": 1,
        "name": "name1",
        "age": 1,
        "shippingAddress": {
            "id": 1,
            "address": {
                "id": 1,
                "streetName": "software",
                "city": "ankara",
                "country": "TR"
            }
        }
    }
]
</pre>
<br/>
HTTP Response Headers : </br>
<pre>
request-id: 411b4b33-6af5-4f78-b185-4171e779222d
correlation-id: 411b4b33-6af5-4f78-b185-4171e779222d
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
</pre>
