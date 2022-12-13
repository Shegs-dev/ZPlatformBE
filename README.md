# ZPlatformService

This is the backend service for the companyz zplatform. To run this service, run the following commands:
- mvn clean install
- docker build -t techeverywhere/zplatform .
- docker run -d -p7002:7002 --name zplatform-image techeverywhere/zplatform
  This would run the project locally on any server.

However there is a live url link to swagger for this backend service:
https://zplatform.plutospace.space/swagger-ui/index.html?url=/v3/api-docs&validatorUrl=#/
