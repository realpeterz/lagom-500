# Lagom 500 Error Problem

## Reproducing Steps

1. `$ sbt runAll`
2. `$ curl --request GET --url 'http://localhost:9000/api/hello/Marvel?from=Doctor>Strange'`
3. Notice a HTML page with a 500 code due to a `java.net.URISyntaxException` has returned instead of catching the error in `CustomHttpErrorHandler` and `hello` ServiceCall is never invoked.

At the moment, this is no known way to handle this kind of errors as proper client errors. Wrongly triggered 500 server errors in production is very problematic.
